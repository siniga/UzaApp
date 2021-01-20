package com.agnet.uza.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.fragments.InventoryCategoryFragment;
import com.agnet.uza.fragments.InventoryProductsFragment;

public class InventoryTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public InventoryTabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                InventoryProductsFragment products = new InventoryProductsFragment();
                return products;
            case 1:
                InventoryCategoryFragment categories = new InventoryCategoryFragment();
                return categories;
            default:
                return null;
        }
    }
}