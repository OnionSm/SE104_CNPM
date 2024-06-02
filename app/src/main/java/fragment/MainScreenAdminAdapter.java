package fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainScreenAdminAdapter extends FragmentStateAdapter
{
    public MainScreenAdminAdapter(@NonNull FragmentActivity fragmentActivity)
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
                return new TrangChuAdminFragment();
            case 1:
                return new ThongBaoFragment();
            case 2:
                return new HoSoFragment();
            default:
                return new TrangChuAdminFragment();
        }
    }

    @Override
    public int getItemCount()
    {
        return 3;
    }
}
