package com.example.btl_app_movie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.btl_app_movie.fragments.AccountFragment;
import com.example.btl_app_movie.fragments.AddFragment;
import com.example.btl_app_movie.fragments.DeleteFragment;
import com.example.btl_app_movie.fragments.FavoriteFragment;
import com.example.btl_app_movie.fragments.HistoryFragment;
import com.example.btl_app_movie.fragments.HomeFragment;
import com.example.btl_app_movie.fragments.UpdateFragment;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private boolean isAdmin;
    private int id_user;
    // Hàm tạo
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, boolean isAdmin, int id_user) {
        super(fm, behavior);
        this.isAdmin = isAdmin;
        this.id_user = id_user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Bundle để đóng gói đưa id_user vào fragment
        Bundle bundle = new Bundle();

        if (isAdmin) {   // Hiển thị các fragment phù hợp cho layout admin
            switch (position) {
                case 1:
                    return new UpdateFragment();
                case 2:
                    return new DeleteFragment();
                case 3:
                    // Truyển id_user vào fragment thông qua setArguments(bundle)
                    AccountFragment accountFragment = new AccountFragment();
                    bundle.putInt("id_user", id_user);
                    accountFragment.setArguments(bundle);
                    return accountFragment;
                case 0:
                default:
                    return new AddFragment();
            }
        }
        //==============================================================
        //else
        // Hiển thị các fragment phù hợp cho layout main


        switch (position) {
            case 1:
                // Truyển id_user vào fragment thông qua setArguments(bundle)
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                bundle.putInt("id_user", id_user);
                favoriteFragment.setArguments(bundle);
                System.out.println("id tai favorite:"+id_user);
                return favoriteFragment;
            case 2:
                // Truyển id_user vào fragment thông qua setArguments(bundle)
                HistoryFragment historyFragment = new HistoryFragment();
                bundle.putInt("id_user", id_user);
                historyFragment.setArguments(bundle);
                System.out.println("id tai history:"+id_user);
                return historyFragment;
            case 3:
                // Truyển id_user vào fragment thông qua setArguments(bundle)
                AccountFragment accountFragment = new AccountFragment();
                bundle.putInt("id_user", id_user);
                accountFragment.setArguments(bundle);
                System.out.println("id tai account:"+id_user);
                return accountFragment;
            case 0:
            default:
                // Truyển id_user vào fragment thông qua setArguments(bundle)
                HomeFragment homeFragment = new HomeFragment();
                bundle.putInt("id_user", id_user);
                homeFragment.setArguments(bundle);
                System.out.println("id tai home:"+id_user);
                return homeFragment;
        }
    }

    // Tra ve so lượng tabs
    @Override
    public int getCount() { // Trả về số lượng tab
        return 4;
    }

    // Set title cho fragment
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if (isAdmin)
        {
            switch (position) {
                case 1:
                    title = "Update";
                    break;
                case 2:
                    title = "Delete";
                    break;
                case 3:
                    title = "Account";
                    break;
                case 0:
                default:
                    title = "Add";
                    break;
            }
        }
        else {
            switch (position) {
                case 1:
                    title = "Favorite";
                    break;
                case 2:
                    title = "History";
                    break;
                case 3:
                    title = "Account";
                    break;
                case 0:
                default:
                    title = "Home";
                    break;
            }
        }
        return title;
    }
}
