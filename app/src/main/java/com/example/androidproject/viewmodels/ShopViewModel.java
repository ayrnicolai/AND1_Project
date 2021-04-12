package com.example.androidproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidproject.models.CartItem;
import com.example.androidproject.models.Product;
import com.example.androidproject.repositories.CartRepo;
import com.example.androidproject.repositories.ShopRepo;

import java.util.List;

public class ShopViewModel extends ViewModel {

    ShopRepo shopRepo = new ShopRepo();

    CartRepo cartRepo = new CartRepo();

    MutableLiveData<Product> mutableProduct = new MutableLiveData<>();


    //This method is expecting a type of livedata, which getproducts is. Whenever obserces getprodcuts it will return getproducts, which will then return the mutableproduct list.

    public LiveData<List<Product>> getProducts() {

        return shopRepo.getProducts();

    }

    public void setProduct(Product product) {
        mutableProduct.setValue(product);
    }

    //we want to observe product from a product fragment
    public LiveData<Product> getProduct() {
        return mutableProduct;
    }

    public LiveData<List<CartItem>> getCart() {
        return cartRepo.getCart();
    }
    // whenver we want to add an item, we use the shop view model to add the item
    public boolean addItemToCart(Product product) {
        return cartRepo.addItemToCart(product);
    }

    public void removeItemFromCart(CartItem cartItem) {
        cartRepo.removeItemFromCart(cartItem);
    }

}
