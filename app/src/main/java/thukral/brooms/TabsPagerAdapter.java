package thukral.brooms;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import thukral.brooms.Fragments.BroomsFragment;
import thukral.brooms.Fragments.TeaFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new BroomsFragment();
            case 1:
                // Games fragment activity
                return new TeaFragment();
            case 2:
                // Movies fragment activity
                return new TeaFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
