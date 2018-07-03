package com.dhis2.usescases.qrCodes;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dhis2.R;
import com.dhis2.usescases.general.FragmentGlobalAbstract;
import com.dhis2.utils.Preconditions;

public class QrFragment extends FragmentGlobalAbstract {

    private static final String ARGUMENTS = "args";
    private Bitmap bitmap;

    @NonNull
    public static QrFragment create(@NonNull Bitmap bitmap) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENTS, bitmap);

        QrFragment qrFragment = new QrFragment();
        qrFragment.setArguments(bundle);

        return qrFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bitmap = Preconditions.isNull(getArguments().getParcelable(ARGUMENTS), "bitmap == null");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView qrCode = view.findViewById(R.id.qr_code);
        if (qrCode != null && bitmap != null)
            qrCode.setImageBitmap(bitmap);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_page, container, false);
    }
}
