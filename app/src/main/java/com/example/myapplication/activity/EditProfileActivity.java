package com.example.myapplication.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private User user;
    private String username;
    private ImageView backBtn, image;
    private LinearLayout linearLayoutImage;
    private TextView idTxt, usernameTxT, roleTxt;
    private EditText nameEdt, emailEdt, phoneNumEdt, imageEdt;
    private Button saveBtn, uploadBtn;
    private Uri selectedImageUri;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

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
        uploadBtn = findViewById(R.id.uploadBtn);
        idTxt = findViewById(R.id.idTxt);
        usernameTxT = findViewById(R.id.usernameTxT);
        roleTxt = findViewById(R.id.roleTxt);
        nameEdt = findViewById(R.id.nameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        phoneNumEdt = findViewById(R.id.phoneNumEdt);
        saveBtn = findViewById(R.id.saveBtn);
        image = findViewById(R.id.image);

        getUsernameFromSharedPreferences();
        getInforUser(username);

        backBtn.setOnClickListener(v -> finish());
        saveBtn.setOnClickListener(v -> saveInforUser(nameEdt.getText().toString(), emailEdt.getText().toString().trim(), phoneNumEdt.getText().toString().trim()));

        uploadBtn.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                openGallery();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        image.setImageURI(selectedImageUri);
                        uploadImageToServer(selectedImageUri);
                    }
                }
            }
    );

    private void uploadImageToServer(Uri imageUri) {
        if (imageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                assert inputStream != null;
                RequestBody requestFile = RequestBody.create(MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))), toByteArray(inputStream));
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
                RequestBody usernamePart = RequestBody.create(MultipartBody.FORM, username);

                APIService.apiService.uploadImage(body, usernamePart).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ToastUtils.showCustomToast(EditProfileActivity.this, "Upload successful");
                        } else {
                            ToastUtils.showCustomToast(EditProfileActivity.this, "Upload failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ToastUtils.showCustomToast(this, "File not found");
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtils.showCustomToast(this, "Failed to read file");
            }
        }
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }

    private void getInforUser(String username) {
        APIService.apiService.getInforUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    idTxt.setText(String.valueOf(user.getUserId()));
                    usernameTxT.setText(user.getUsername());
                    roleTxt.setText(user.getRole());
                    nameEdt.setText(user.getFullname());
                    emailEdt.setText(user.getEmail());
                    phoneNumEdt.setText(user.getPhoneNumber());
                    String imageUrl = user.getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).into(image);
                    } else {
                        Log.e("EditProfileActivity", "Get image user failed");
                    }
                } else {
                    Log.e("EditProfileActivity", "Get info user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void saveInforUser(String fullname, String email, String phoneNumber) {
        APIService.apiService.saveInforUser(username, fullname, email, phoneNumber).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showCustomToast(EditProfileActivity.this, "Save successful");
                } else {
                    ToastUtils.showCustomToast(EditProfileActivity.this, "Save failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}
