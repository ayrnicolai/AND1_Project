package com.example.androidproject.repositories;

//If we load data we should do it in a repository class

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidproject.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopRepo {
    private MutableLiveData<List<Product>> mutableProductList;

    public LiveData<List<Product>> getProducts() {
        if (mutableProductList == null) {
            mutableProductList = new MutableLiveData<>();
            loadProducts();
        }
        return mutableProductList;
    }

    private void loadProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(UUID.randomUUID().toString(), "Taco Holder", 50, true, "https://images.fun.com/products/61065/2-1-143299/liberty-tacos-alt-1.jpg", "This amazing Taco holder is the optimal way for you to hold your taco, remeber those times when you held your taco while you put stuff i in your taco, the taco would often fall down, if you buy this taco holder that will be history."));
        productList.add(new Product(UUID.randomUUID().toString(), "Taco Sleeping Bag", 25, true, "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1571765609-tacoblanket-1571765561.jpg?crop=1xw:1xh;center,top&resize=768:*", "This very serious hand-crafted made from italian leather by craftsmen from the Cotswolls, is the best way to enjoys a outdoors trip from Haiti to Harajuku, it well keep you warms aswell as cool, outdoor people  dont go changin!"));
        productList.add(new Product(UUID.randomUUID().toString(), "Taco FR E SH A VOCA DO", 30, true, "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1571683063-51BP1xPPKyL.jpg?crop=1xw:1.00xh;center,top&resize=768:*", "Based on the very popular Vine 'fr-e-sh-a-voca-do' - come to del taco, they've got a new thing called, fresha, fresh avocado! fresh avocado! by chantheflop. The world renowing succes is now coming in form of a Avocado where you can put Avocado dip in your Avocado. That's Special! "));
        productList.add(new Product(UUID.randomUUID().toString(), "Taco T-Shirt", 100, true, "https://m.media-amazon.com/images/I/A1jKzO+1adL._CLa%7C2140%2C2000%7C81GN9sxfwQL.png%7C0%2C0%2C2140%2C2000%2B0.0%2C0.0%2C2140.0%2C2000.0_AC_UX342_.png", "Ever heard of Kanye Drip, or the Supreme Brand. Well now it is possible to go beyond the drip, and proceed to the yayeet. With this new Taco T-Shirt, which will prove to all your peers, that you indeed have the biggest drip, or officially as of now the most yayeet"));
        productList.add(new Product(UUID.randomUUID().toString(), "The Taco Tuesday Cookbook", 100, true, "https://www.lifesavvy.com/p/uploads/2020/10/7a73eb2b-1.jpg", "To kill a Mockingbird, The Great Gatsby, The Bible. All these are nothing compared to the Taco Tuesday Cookbook!. which Harvard professors have called a literary masterpiece, and will go down in history as the possibly the greatest book in history of the world   "));
        productList.add(new Product(UUID.randomUUID().toString(), "John Cena Taco Sign", 10, true, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4taTW-5C6D71r4dWRDIHO34fBI5eH_5ONCQ&usqp=CAU", "Can you see him?, is your time now?, well whether you like it or not the champ is here! with this new John Cena taco sign you will be able to achieve mastery succes in which ever field you desire!"));
        productList.add(new Product(UUID.randomUUID().toString(), "Taco Time", 55, false, "https://cdn.vox-cdn.com/uploads/chorus_asset/file/22354670/unnamed.gif", "Taco Time Yay"));
        productList.add(new Product(UUID.randomUUID().toString(), "Dorito Taco", 5, true, "https://media2.giphy.com/media/AEMm6cDZojeEg/giphy.gif", "Mexican cuisine is world renowned for its complexity and culinary techniques and now two industry kings are combining forces! Introducing the Dorito taco which combines the best of both worlds, it will rock out the show. Mix it all together and you know thats its the best of both worlds!"));
        productList.add(new Product(UUID.randomUUID().toString(), "Boi got his free taco", 100,false, "https://media.tenor.com/images/4e1a845a186590848b455df13dd85651/tenor.gif", "He did get his free taco, but then he fell on his skateboard. Very Sad"));
        mutableProductList.setValue(productList);

    }



}
