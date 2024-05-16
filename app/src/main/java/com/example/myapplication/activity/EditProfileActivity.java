package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private User user;
    private String username;
    private ImageView backBtn , image;
    private TextView idTxt, usernameTxT, roleTxt;
    private EditText nameEdt, emailEdt, phoneNumEdt;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);

        backBtn = findViewById(R.id.backBtn);
        image = findViewById(R.id.image);
        idTxt = findViewById(R.id.idTxt);
        usernameTxT = findViewById(R.id.usernameTxT);
        roleTxt = findViewById(R.id.roleTxt);
        nameEdt = findViewById(R.id.nameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        phoneNumEdt = findViewById(R.id.phoneNumEdt);
        saveBtn = findViewById(R.id.saveBtn);
        getUsernameFromSharedPreferences();
        getInforUser(username);

        backBtn.setOnClickListener(v -> finish());
        saveBtn.setOnClickListener(v -> {
            savaInforUser(nameEdt.getText().toString(), emailEdt.getText().toString().trim(), phoneNumEdt.getText().toString().trim());
        });
    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username =  sharedPreferences.getString("username", "");
    }
    private void getInforUser(String username) {
        APIService.apiService.getInforUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();

                    String imageUrl = user.getImageUrl();
                    Log.d("ImageUrl", "Image URL: " + imageUrl);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).into(image);
                    } else {
                        Log.e("EditProfileActivity", "Empty image URL");
                    }

                    idTxt.setText(String.valueOf(user.getUserId()));
                    usernameTxT.setText(user.getUsername());
                    roleTxt.setText(user.getRole());
                    nameEdt.setText(user.getFullname());
                    emailEdt.setText(user.getEmail());
                    phoneNumEdt.setText(user.getPhoneNumber());

                } else {
                    Log.e("EditProfileActivity", "Get infor user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void savaInforUser(String fullname, String email, String phoneNumber) {
        APIService.apiService.saveInforUser(username, fullname, email, phoneNumber).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);

            }
        });
    }
}