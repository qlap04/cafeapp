package com.example.myapplication.api;

import com.example.myapplication.model.Address;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Email;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.Reply;
import com.example.myapplication.model.User;
import com.example.myapplication.modelRequest.ProductRequest;
import com.example.myapplication.modelResponse.AddressResponse;
import com.example.myapplication.modelResponse.TotalPriceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @GET("/login/login")
    Call<List<User>> getListUsers();
    @GET("/users/list-user-online")
    Call<List<User>> getListUsersOnline();
    @GET("/users/list-user-offline")
    Call<List<User>> getListUsersOffline();
    @GET("/users/get-infor-user-for-admin")
    Call<User> getInforUserForAdmin(@Query("userId") int userId);
    @GET("/users/get-infor-user")
    Call<User> getInforUser(@Query("username") String username);
    @GET("/users/get-working-status-user")
    Call<Boolean> getWorkingStatusUser(@Query("username") String username);
    @PATCH("/users/set-online-for-user-for-admin")
    Call<Void> setOnLineForUserForAdmin(@Query("userId") int userId);
    @PATCH("/users/set-online-for-user")
    Call<Void> setOnLineForUser(@Query("userId") int userId);
    @PATCH("/users/set-offline-for-user")
    Call<Void> setOffLineForUser(@Query("userId") int userId);
    @PATCH("/users/update-role-for-user-for-admin")
    Call<Void> updateRoleForUserForAdmin(@Query("userId") int userId, @Query("role") String role);
    @GET("/products/products")
    Call<List<Product>> getListProducts();
    @GET("/products/list-best-cafe")
    Call<List<Product>> getListBestCafe();
    @GET("/products/list-best-cake")
    Call<List<Product>> getListBestCake();
    @GET("/products/products-evaluate")
    Call<List<Product>> callApiGetProductsWithEvaluateValue(@Query("evaluateValue") String evaluateValue);
    @GET("/products/products-price")
    Call<List<Product>> callApiGetProductsWithPriceValue(@Query("priceValue") String priceValue);
    @GET("/products/products-search")
    Call<List<Product>> getListProductsWithSearchValue(@Query("searchValue") String searchValue);
    @GET("/products/products-in-category")
    Call<List<Product>> getProductsByCategory(@Query("category") String category);
    @GET("/products/get-infor-product")
    Call<Product> getInforProduct(@Query("title") String title);
    @GET("/products/set-best-product")
    Call<Void> updateProductPopularStatus(@Query("title") String title, @Query("popular") String popular);
    @PUT("/products/update-infor-product")
    Call<Void> updateInforProduct( @Query("title") String title,@Body ProductRequest productRequest);
    @GET("/cart/products-in-cart")
    Call<List<Cart>> getListProductsIncart(@Query("username") String username);
    @POST("/products/add-product")
    Call<Void> addProduct(@Body ProductRequest productRequest);
    @GET("/cart/set-orderId-product")
    Call<Void> setOrderIdProduct(@Query("username") String username);
    @GET("/address/get-address-for-user")
    Call<Address> getAddress(@Query("username") String username);
    @GET("/users/get-password")
    Call<User> getUserByUsername(@Query("username") String username);
    @GET("/users/get-password")
    Call<User> addStaff(@Body User user);
    @GET("/address/get-address")
    Call<List<Address>> getListAddress(@Query("username") String username);
    @GET("/cart/get-product-in-complete")
    Call<List<Cart>> getProductInComplete(@Query("username") String username);
    @GET("/cart/get-product-in-complete-for-staff")
    Call<List<Cart>> getProductInCompleteForStaff();
    @PUT("/cart/set-confirmed")
    Call<Void> setProductInCartIsConfirmed(@Query("username") String username);
    @PUT("/cart/set-ordered")
    Call<Void> setProductInCartIsOdered(@Query("username") String username);
    @GET("/cart/total-price")
    Call<TotalPriceResponse> getTotalPrice(@Query("username") String username);
    @GET("/cart/get-product-in-processing")
    Call<List<Cart>> getProductInProcessing(@Query("username") String username);
    @GET("/cart/get-product-in-processing-for-staff")
    Call<List<Cart>> getProductInProcessingForStaff();
    @GET("/orders/total-price-for-bill")
    Call<TotalPriceResponse> getTotalPriceForBill(@Query("username") String username);
    @GET("/cart/receive-order")
    Call<Void> receiveOrder(@Query("_id") int _id);
    @GET("/cart/get-product-in-tracking")
    Call<List<Cart>> getProductInTracking(@Query("username") String username, @Query("_id") int _id);
    @GET("/cart/get-product-in-tracking-for-staff")
    Call<List<Cart>> getProductInTrackingForStaff(@Query("_id") int _id);
    @GET("/cart/get-payment-method-for-bill")
    Call<String> getPaymentMethodForBill(@Query("username") String username, @Query("_id") int _id);
    @GET("/cart/get-product-for-bill")
    Call<List<Cart>> getProductForBill(@Query("_id") int _id);
    @GET("/orders/get-address-for-bill")
    Call<AddressResponse> getAddressForBill( @Query("_id") int _id);
    @GET("/cart/get-price-for-bill")
    Call<TotalPriceResponse> getPriceForBill(@Query("_id") int _id);
    @GET("/orders/set-payment-method")
    Call<Void> setPaymentMethodInCart(@Query("username") String username, @Query("paymentMethod") String paymentMethod);
    @POST("/login/signup")
    Call<User> signUpUser(@Body User user);
    @POST("/reply/save-content-reply")
    Call<Void> saveDataReply(@Body Reply reply);
    @POST("/users/save-infor-user")
    Call<Void> saveInforUser(@Query("username") String username, @Query("fullname") String fullname, @Query("email") String email, @Query("phoneNumber") String phoneNumber);
    @POST("/users/save-image-user")
    Call<Void> saveImageUser(@Query("username") String username, @Query("imageUrl") String imageUrl);
    @Multipart
    @POST("/users/upload-image")
    Call<Void> uploadImage(@Part("username") String username, @Part MultipartBody.Part image);
    @POST("/address/save-data-address")
    Call<Void> saveDataAddress(@Body Address address);
    @POST("/emails/reset-password")
    Call<Email> callApiToGetResetPassWord(@Query("email") String email);
    @POST("/address/update-data-address")
    Call<Void> updateDataAddress(@Query("username") String username, @Body Address address);
    @POST("/cart/add-to-cart")
    Call<Cart> addToCart(@Body Cart cart, @Query("selectedOptions") ArrayList<String> selectedOptions);
    @POST("/users/change-password")
    Call<Void> updatePassword(@Query("username") String username, @Query("password") String password);
    @POST("/cart/update-cart-item/{username}/{cartId}")
    Call<Void> updateCartItem(@Path("username") String username, @Path("cartId") int cartId, @Body Cart cart);
    @POST("/orders/save-address-for-cart")
    Call<Void> saveAddressForCart(@Query("username") String username, @Query("_id") int _id, @Body AddressResponse addressResponse);
    @DELETE("/cart/delete-product")
    Call<Void> deleteProduct(@Query("user") String user, @Query("_id") int _id);
    @DELETE("/cart/delete-all-products-in-cart")
    Call<Void> cancelOrder(@Query("_id") int _id);

}