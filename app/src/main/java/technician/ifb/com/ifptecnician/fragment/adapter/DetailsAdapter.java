package technician.ifb.com.ifptecnician.fragment.adapter;

import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.DetailsFragment;
import technician.ifb.com.ifptecnician.fragment.OverviewFragment;
import technician.ifb.com.ifptecnician.fragment.ProductFragment;

public class DetailsAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public DetailsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new OverviewFragment();
        } else if (position == 1){
            return new ProductFragment();
        } else {
            return new DetailsFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.overview);
            case 1:
                return mContext.getString(R.string.product);
            case 2:
                return mContext.getString(R.string.details);

            default:
                return null;
        }
    }

}