package org.dhis2.usescases.teiDashboard.adapters;

import android.content.Context;
import android.os.Parcelable;

import org.dhis2.R;
import org.dhis2.usescases.teiDashboard.dashboardfragments.indicators.IndicatorsFragment;
import org.dhis2.usescases.teiDashboard.dashboardfragments.notes.NotesFragment;
import org.dhis2.usescases.teiDashboard.dashboardfragments.relationships.RelationshipFragment;
import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * QUADRAM. Created by ppajuelo on 29/11/2017.
 */

public class DashboardPagerTabletAdapter extends FragmentStatePagerAdapter {

    private static final int MOVILE_DASHBOARD_SIZE = 3;
    private final Context context;
    private String currentProgram;


    public DashboardPagerTabletAdapter(Context context, FragmentManager fm, String program) {
        super(fm);
        this.currentProgram = program;
        this.context = context;
    }

    @Override
    public Parcelable saveState() {
        // Do Nothing
        return null;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return new IndicatorsFragment();
            case 1:
                return new RelationshipFragment();
            case 2:
                return new NotesFragment();
        }
    }

    @Override
    public int getCount() {
        return currentProgram != null ? MOVILE_DASHBOARD_SIZE : 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return context.getString(R.string.dashboard_indicators);
            case 1:
                return context.getString(R.string.dashboard_relationships);
            case 2:
                return context.getString(R.string.dashboard_notes);
        }
    }
}
