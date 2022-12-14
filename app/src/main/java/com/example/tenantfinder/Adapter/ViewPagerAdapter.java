package com.example.tenantfinder.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tenantfinder.Fragment.LoginFragment;
import com.example.tenantfinder.Fragment.SignUpFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public ViewPagerAdapter(FragmentManager fm, Context context, int totalTabs) {

        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;
            case 1:
                SignUpFragment signUpFragment = new SignUpFragment();
                return signUpFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
