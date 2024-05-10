package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Reply;
import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyActivity extends AppCompatActivity {
    private Button replyBtn;
    private  String name, phoneNum, email, content;
    private EditText nameEdt, phoneNumEdt, emailEdt, contentEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reply);
        ImageView backBtn = findViewById(R.id.backBtn);
        nameEdt = findViewById(R.id.nameEdt);
        phoneNumEdt = findViewById(R.id.phoneNumEdt);
        emailEdt = findViewById(R.id.emailEdt);
        contentEdt = findViewById(R.id.contentEdt);
        replyBtn = findViewById(R.id.replyBtn);
        backBtn.setOnClickListener(v -> finish());
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataReply();
            }
        });
    }

    private void saveDataReply() {
        name = nameEdt.getText().toString();
        phoneNum = phoneNumEdt.getText().toString();
        email = emailEdt.getText().toString();
        content = contentEdt.getText().toString();
        Reply reply = new Reply(name, phoneNum, email, content);
        APIService.apiService.saveDataReply(reply).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReplyActivity.this, "Gửi phàn hồi thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReplyActivity.this, "Gửi phàn hồi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Api Error", "Fail to post model reply from api", t);
            }
        });

    }
}

