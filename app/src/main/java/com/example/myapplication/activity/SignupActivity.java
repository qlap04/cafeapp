package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private boolean  checkUsername = true, checkEmail = true, checkPhoneNumber = true, checkPassword = true;
    private TextView strUsername;
    private TextView strEmail;
    private TextView strPassword;
    private TextView strPhoneNum;
    private List<User> mListUser;

    private TextInputLayout userContainer, emailContainer, phoneNumContainer, passwordContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        strUsername = findViewById(R.id.userEdt);
        strEmail = findViewById(R.id.emailEdt);
        strPhoneNum = findViewById(R.id.phoneNumEdt);
        strPassword = findViewById(R.id.passEdt);
        userContainer = findViewById(R.id.userContainer);
        emailContainer = findViewById(R.id.emailContainer);
        phoneNumContainer = findViewById(R.id.phoneNumContainer);
        passwordContainer = findViewById(R.id.passwordContainer);
        Button signupBtn = findViewById(R.id.signupBtn);
        TextView mTextLogin = findViewById(R.id.textLogin);

        getListUsers();

        strUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (strUsername.getText().toString().isEmpty()) {
                    userContainer.setHelperTextEnabled(true);
                    userContainer.setError("Bắt buộc");
                    checkUsername = true;
                } else if (isUsernameExists(strUsername.getText().toString().trim())) {
                    userContainer.setHelperTextEnabled(true);
                    userContainer.setError("Tài khoản đã tồn tại");
                    checkUsername = true;
                } else {
                    userContainer.setHelperTextEnabled(false);
                    userContainer.setError(null);
                    checkUsername = false;
                }
            }
        });


        strEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = strEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    emailContainer.setHelperTextEnabled(true);
                    emailContainer.setError("Bắt buộc");
                    checkEmail = true;
                } else if (!isValidEmail(email)) {
                    emailContainer.setHelperTextEnabled(true);
                    emailContainer.setError("Định dạng email không đúng");
                    checkEmail = true;
                } else {
                    emailContainer.setHelperTextEnabled(false);
                    emailContainer.setError(null);
                    checkEmail = false;
                }
            }
        });

        strPhoneNum.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String phoneNumber = strPhoneNum.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    phoneNumContainer.setHelperTextEnabled(true);
                    phoneNumContainer.setError("Bắt buộc");
                    checkPhoneNumber = true;
                } else if (phoneNumber.length() > 11) {
                    phoneNumContainer.setHelperTextEnabled(true);
                    phoneNumContainer.setError("Số điện thoại lớn hơn 11 kí tự");
                    checkPhoneNumber = true;
                } else {
                    phoneNumContainer.setHelperTextEnabled(false);
                    phoneNumContainer.setError(null);
                    checkPhoneNumber = false;
                }
            }
        });
        strPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = strPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    passwordContainer.setHelperTextEnabled(true);
                    passwordContainer.setError("Bắt buộc");
                    checkPassword = true;
                } else if (!isValidPassword(password)) {
                    passwordContainer.setHelperTextEnabled(true);
                    passwordContainer.setError("Mật khẩu phải chứa ít nhất một chữ in hoa, một ký tự đặc biệt và một chữ số");
                    checkPassword = true;
                } else {
                    passwordContainer.setHelperTextEnabled(false);
                    passwordContainer.setError(null);
                    checkPassword = false;
                }
            }
        });

        mTextLogin.setOnClickListener(v -> clickTextSignup());
        signupBtn.setOnClickListener(v -> startRegister());
    }
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    }
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void getListUsers() {
        APIService.apiService.getListUsers()
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        mListUser = response.body();
                        assert mListUser != null;
                        Log.e("List user: ", String.valueOf(mListUser.size()));
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private boolean isUsernameExists(String username) {
        if (mListUser != null) {
            for (User user : mListUser) {
                if (user.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void startRegister() {
        String username =  strUsername.getText().toString().trim();
        String email = strEmail.getText().toString().trim();
        String phoneNum = strPhoneNum.getText().toString().trim();
        String password = strPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || phoneNum.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }
        if(checkUsername || checkEmail || checkPhoneNumber || checkPassword) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập đúng thông tin", Toast.LENGTH_LONG).show();
            return;
        }

        String hashedPassword = hashPassword(password);

        User newUser = new User(username, email, phoneNum, hashedPassword);
        APIService.apiService.signUpUser(newUser)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful()) {
                            clickTextSignup();
                        } else {
                            Toast.makeText(SignupActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                            try {
                                assert response.errorBody() != null;
                                Log.e("SignupActivity", "Lỗi khi đăng ký: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.e("SignupActivity", "Lỗi khi gửi yêu cầu đăng ký: " + t.getMessage(), t);
                    }

                });
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clickTextSignup() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
