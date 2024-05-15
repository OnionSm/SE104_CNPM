package fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BaoCaoViewPagerAdapter extends FragmentStateAdapter
{
    public BaoCaoViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
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
                return new BaoCaoSoDo1Fragment();
            case 1:
                return new BaoCaoSoDo2Fragment();
            case 2:
                return new BaoCaoSoDo3Fragment();
            default:
                return new BaoCaoSoDo1Fragment();
        }
    }

    @Override
    public int getItemCount()
    {
        return 3;
    }
}
