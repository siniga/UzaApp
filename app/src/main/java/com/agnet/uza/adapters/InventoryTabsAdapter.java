package com.agnet.uza.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.agnet.uza.fragments.inventories.ManageCategoryFragment;
import com.agnet.uza.fragments.inventories.ManageProductsFragment;

public class InventoryTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public InventoryTabsAdapter(FragmentManager fm, int NoOfTabs){
        super(fm);
        this.mNumOfTabs = NoOfTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                ManageProductsFragment products = new ManageProductsFragment();
                return products;
            case 1:
                ManageCategoryFragment categories = new ManageCategoryFragment();
                return categories;
            default:
                return null;
        }
    }
}