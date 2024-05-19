package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class ContactActivity extends AppCompatActivity {
    //
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contact);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
        ImageView imageProfile = findViewById(R.id.image_profile);
        TextView textViewName = findViewById(R.id.textview_name);
        TextView textViewDescription = findViewById(R.id.textview_description);
        TextView textViewWebsite = findViewById(R.id.textview_website);


        SpannableString spannableString = new SpannableString("Website Coffee");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Mở URL khi người dùng nhấp vào
                String url = "http://www.coffeeexample.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewWebsite.setText(spannableString);
        textViewWebsite.setMovementMethod(LinkMovementMethod.getInstance());


        LinearLayout layoutFacebook = findViewById(R.id.layout_facebook);
        layoutFacebook.setOnClickListener(v -> openFacebook());


        LinearLayout layoutCallPhone = findViewById(R.id.layout_callphone);
        layoutCallPhone.setOnClickListener(v -> openCallPhone());


        LinearLayout layoutGmail = findViewById(R.id.layout_gmail);
        layoutGmail.setOnClickListener(v -> openGmail());


        LinearLayout layoutSkype = findViewById(R.id.layout_skype);
        layoutSkype.setOnClickListener(v -> openSkype());


        LinearLayout layoutInstagram = findViewById(R.id.layout_instagram);
        layoutInstagram.setOnClickListener(v -> openInstagram());


        LinearLayout layoutZalo = findViewById(R.id.layout_zalo);
        layoutZalo.setOnClickListener(v -> openZalo());
    }


    private void openFacebook() {
        String facebookPageId = "100022369530407";
        String facebookUrl = "https://www.facebook.com/" + facebookPageId;


        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            facebookIntent.setData(Uri.parse("fb://page/" + facebookPageId));
        } catch (Exception e) {
            facebookIntent.setData(Uri.parse(facebookUrl));
        }


        try {
            startActivity(facebookIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void openCallPhone() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:your_phone_number"));
        startActivity(callIntent);
    }


    private void openGmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:your_email@example.com"));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void openSkype() {
        String skypeId = "live:.cid.8fb61bae2b11daab";
        String skypeUri = "skype:" + skypeId + "?chat";
        String skypeWebUrl = "https://web.skype.com/";


        Intent skypeIntent = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.skype.raider", 0);
            skypeIntent.setData(Uri.parse(skypeUri));
        } catch (Exception e) {
            skypeIntent.setData(Uri.parse(skypeWebUrl));
        }


        try {
            startActivity(skypeIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void openInstagram() {
        String instagramUsername = "gnort_aihgn.2602";
        String instagramUri = "http://instagram.com/_u/" + instagramUsername;
        String instagramWebUrl = "https://www.instagram.com/" + instagramUsername;


        Intent instagramIntent = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            instagramIntent.setData(Uri.parse(instagramUri));
        } catch (Exception e) {
            instagramIntent.setData(Uri.parse(instagramWebUrl));
        }


        try {
            startActivity(instagramIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void openZalo() {
        String zaloId = "your_zalo_id";
        String zaloUri = "https://zalo.me/" + zaloId;


        Intent zaloIntent = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.zing.zalo", 0);
            zaloIntent.setData(Uri.parse(zaloUri));
        } catch (Exception e) {
            zaloIntent.setData(Uri.parse(zaloUri));
        }


        try {
            startActivity(zaloIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}