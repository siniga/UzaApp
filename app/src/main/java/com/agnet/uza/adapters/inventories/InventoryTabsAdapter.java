package com.agnet.uza.adapters.inventories;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.agnet.uza.pages.inventories.categories.CategoryInvFragment;
import com.agnet.uza.pages.inventories.products.ProductInventoryFragment;

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
                ProductInventoryFragment products = new ProductInventoryFragment();
                return products;
            case 1:
                CategoryInvFragment categories = new CategoryInvFragment();
                return categories;
            default:
                return null;
        }
    }
}