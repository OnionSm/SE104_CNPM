package fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TaoDeThiViewPagerAdapter2 extends FragmentStateAdapter
{
    public TaoDeThiViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity)
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
                return new SuaNganHangCauHoiFragment();
            case 1:
                return new SuaCauHoiDaChonFragment();
            default:
                return new SuaNganHangCauHoiFragment();
        }
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
