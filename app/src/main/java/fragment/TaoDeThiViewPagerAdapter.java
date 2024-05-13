package fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TaoDeThiViewPagerAdapter extends FragmentStateAdapter
{
    public TaoDeThiViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch(position)
        {
            case 0:
                return new NganHangCauHoiFragment();
            case 1:
                return new CauHoiDaChonFragment();
            default:
                return new NganHangCauHoiFragment();
        }
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
