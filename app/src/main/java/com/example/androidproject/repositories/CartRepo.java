package com.example.androidproject.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidproject.models.CartItem;
import com.example.androidproject.models.Product;
import com.example.androidproject.viewmodels.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

// intialized in ShopViewModel
public class CartRepo {

    private MutableLiveData<List<CartItem>> mutableCart = new MutableLiveData<>();
    private MutableLiveData<Double> mutableTotalPrice = new MutableLiveData<>();

    public LiveData<List<CartItem>> getCart() {
        if (mutableCart.getValue() == null) {
            initCart();
        }
        return mutableCart;
    }
    public void initCart() {
        mutableCart.setValue(new ArrayList<CartItem>());
        calculateCartTotal();
    }
    public boolean addItemToCart(Product product) {
        if (mutableCart.getValue() == null) {
            initCart();
        }
        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());

        //When we add an item to the kart, that has already been added we want to add to the qunatity instead of adding a new item. we set the max quantity to 5.
        for (CartItem cartItem: cartItemList) {
            if (cartItem.getProduct().getId().equals(product.getId())) {
                if (cartItem.getQuantity() == 5) {
                    return false;
                }
                int index = cartItemList.indexOf(cartItem);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemList.set(index, cartItem);

                mutableCart.setValue(cartItemList);
                calculateCartTotal();

                return true;
            }

        }
//we add the item to the cart
        CartItem cartItem = new CartItem(product, 1);
        cartItemList.add(cartItem);
        mutableCart.setValue(cartItemList);
        calculateCartTotal();
        return true;
    }

    //this removes items from cart,
    public void removeItemFromCart(CartItem cartItem) {
        if (mutableCart.getValue() == null) {
            return;
        }
        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());

        cartItemList.remove(cartItem);

        mutableCart.setValue(cartItemList);
        calculateCartTotal();
    }
    //This changes quantity in the cart
    public void changeQuantity(CartItem cartItem, int quantity) {
        if(mutableCart.getValue() == null) {
            return;
        }

            List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());

            CartItem updatedItem = new CartItem(cartItem.getProduct(), quantity);
            cartItemList.set(cartItemList.indexOf(cartItem), updatedItem);

            mutableCart.setValue(cartItemList);
            calculateCartTotal();
        }



//Sidenote: Hvis en metode er af type livedate så kan man altid return livedata
        public LiveData<Double> getTotalPrice() {
        if (mutableTotalPrice.getValue() == null) {
            mutableTotalPrice.setValue(0.0);

        }
        return mutableTotalPrice;
        }


//we are calling this in method, every time the mutable value is updated, so it can calculate the total price
    private void calculateCartTotal() {
        if (mutableCart.getValue() == null) {
            return;
        }
        double total = 0.0;
        List<CartItem> cartItemList = mutableCart.getValue();
        for (CartItem cartitem: cartItemList) {
            total += cartitem.getProduct().getPrice() + cartitem.getQuantity();
        }
        mutableTotalPrice.setValue(total);
    }
    }



