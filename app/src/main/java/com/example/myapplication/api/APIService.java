package com.example.myapplication.api;

import com.example.myapplication.model.Address;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Email;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.Reply;
import com.example.myapplication.model.User;
import com.example.myapplication.modelResponse.AddressResponse;
import com.example.myapplication.modelResponse.TotalPriceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.56.1:3001/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
    @GET("/login")
    Call<List<User>> getListUsers();
    @GET("/products")
    Call<List<Product>> getListProducts();
    @GET("/list-best-cafe")
    Call<List<Product>> getListBestCafe();
    @GET("/list-best-cake")
    Call<List<Product>> getListBestCake();
    @GET("/products-evaluate")
    Call<List<Product>> callApiGetProductsWithEvaluateValue(@Query("evaluateValue") String evaluateValue);
    @GET("/products-price")
    Call<List<Product>> callApiGetProductsWithPriceValue(@Query("priceValue") String priceValue);
    @GET("/products-search")
    Call<List<Product>> getListProductsWithSearchValue(@Query("searchValue") String searchValue);
    @GET("/products-in-category")
    Call<List<Product>> getProductsByCategory(@Query("category") String category);
    @GET("/products-in-cart")
    Call<List<Cart>> getListProductsIncart(@Query("username") String username);
    @GET("/total-price")
    Call<TotalPriceResponse> getTotalPrice(@Query("username") String username);
    @GET("/total-price-for-bill")
    Call<TotalPriceResponse> getTotalPriceForBill(@Query("username") String username);
    @GET("/get-password")
    Call<User> getUserByUsername(@Query("username") String username);
    @GET("/set-ordered")
    Call<Void> setProductInCartIsOdered(@Query("username") String username);
    @GET("/get-product-in-processing")
    Call<List<Cart>> getProductInProcessing(@Query("username") String username);
    @GET("/get-product-in-tracking")
    Call<List<Cart>> getProductInTracking(@Query("username") String username, @Query("_id") int _id);
    @GET("/get-product-in-complete")
    Call<List<Cart>> getProductInComplete(@Query("username") String username);
    @GET("/get-address")
    Call<List<Address>> getListAddress(@Query("username") String username);
    @GET("/get-address-for-user")
    Call<Address> getAddress(@Query("username") String username);
    @GET("/get-address-for-bill")
    Call<AddressResponse> getAddressForBill(@Query("username") String username, @Query("_id") int _id);
    @GET("/receive-order")
    Call<Void> receiveOrder(@Query("username") String username, @Query("_id") int _id);
    @GET("/get-product-for-bill")
    Call<List<Cart>> getProductForBill(@Query("username") String username, @Query("_id") int _id);
    @GET("/get-price-for-bill")
    Call<TotalPriceResponse> getPriceForBill(@Query("username") String username, @Query("_id") int _id);
    @POST("/signup")
    Call<User> signUpUser(@Body User user);
    @POST("/add-to-cart")
    Call<Cart> addToCart(@Body Cart cart, @Query("selectedOptions") ArrayList<String> selectedOptions);
    @POST("update-cart-item/{username}/{cartId}")
    Call<Void> updateCartItem(@Path("username") String username, @Path("cartId") int cartId, @Body Cart cart);
    @POST("/reset-password")
    Call<Email> callApiToGetResetPassWord(@Query("email") String email);
    @POST("/change-password")
    Call<Void> updatePassword(@Query("username") String username, @Query("password") String password);
    @POST("/save-content-reply")
    Call<Void> saveDataReply(@Body Reply reply);
    @POST("/save-data-address")
    Call<Void> saveDataAddress(@Body Address address);
    @POST("/update-data-address")
    Call<Void> updateDataAddress(@Query("username") String username, @Body Address address);
    @POST("/save-address-for-cart")
    Call<Void> saveAddressForCart(@Query("username") String username, @Query("_id") int _id, @Body AddressResponse addressResponse);
    @DELETE("/delete-product")
    Call<Void> deleteProduct(@Query("user") String user, @Query("title") String title);
    @DELETE("/delete-all-products-in-cart/{username}")
    Call<Void> cancelOrder(@Path("username") String username, @Query("_id") int _id);


}
