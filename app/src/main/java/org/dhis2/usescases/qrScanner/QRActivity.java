package org.dhis2.usescases.qrScanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.dhis2.R;
import org.dhis2.databinding.ActivityQrBinding;
import org.dhis2.usescases.general.ActivityGlobalAbstract;
import org.dhis2.utils.Constants;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import timber.log.Timber;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * QUADRAM. Created by ppajuelo on 15/01/2018.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class QRActivity extends ActivityGlobalAbstract {

    ActivityQrBinding binding;
    CameraSource cameraSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true)
                .build();
        binding.cameraView.getHolder().addCallback(mySurfaceCallback);

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // do nothing
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    String qrCode = barcodes.valueAt(0).rawValue;
                    Intent data = new Intent();
                    data.putExtra(Constants.EXTRA_DATA, qrCode);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.stop();
    }

    private SurfaceHolder.Callback mySurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            setUpSourceCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // do nothing
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // do nothing
        }
    };

    private void setUpSourceCamera() {
        try {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PERMISSION_GRANTED && cameraSource != null)
                cameraSource.start(binding.cameraView.getHolder());
            else
                ActivityCompat.requestPermissions(QRActivity.this, new String[]{Manifest.permission.CAMERA}, 101);
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpSourceCamera();
            } else {
                finish();
            }
        }
    }
}
