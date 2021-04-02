package com.example.androidproject.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidproject.R;
import com.example.androidproject.databinding.FragmentProductDetailBinding;
import com.example.androidproject.databinding.FragmentProductDetailBindingImpl;
import com.example.androidproject.viewmodels.ShopViewModel;

/**
 * //whenever someone clicks on an item, i want to call my view on model, and then set a product in viewmodel
 * so when i navigate to the product detail fragment i want to use that viewmodel, it is tied to the activity
 * And is therefore never destroyed, until the activity is. So i can use the same viewmodel.

 */
public class ProductDetailFragment extends Fragment {

    FragmentProductDetailBinding fragmentProductDetailBinding;
    ShopViewModel shopViewModel;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProductDetailBinding = fragmentProductDetailBinding.inflate(inflater, container, false);
        return fragmentProductDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        fragmentProductDetailBinding.setShopViewModel(shopViewModel);
    }
}