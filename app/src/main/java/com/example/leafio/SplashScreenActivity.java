package com.example.leafio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView; // Penting: Tambahkan ini
import android.widget.TextView;  // Penting: Tambahkan ini
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.List;
import java.util.Locale;

public class SplashScreenActivity extends BaseActivity {

    private ImageView ivFlag;
    private TextView tvWelcome;
    private TextView tvLangCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastikan nama layout di bawah ini sesuai dengan file di res/layout
        setContentView(R.layout.activity_splash);

        ivFlag = findViewById(R.id.iv_flag);
        tvWelcome = findViewById(R.id.tv_welcome);
        tvLangCode = findViewById(R.id.tv_lang_code);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            String code = addresses.get(0).getCountryCode();
                            String lang = code.equalsIgnoreCase("ID") ? "in" : "en";

                            setLocale(lang); // Memanggil fungsi dari BaseActivity
                            updateUIByLanguage(lang);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
                startApp();
            });
        } else {
            updateUIByLanguage("en");
            startApp();
        }
    }

    private void updateUIByLanguage(String lang) {
        if (lang.equalsIgnoreCase("in")) {
            if (ivFlag != null) ivFlag.setImageResource(R.drawable.ic_indonesia);
            if (tvLangCode != null) tvLangCode.setText("IDN");
        } else {
            if (ivFlag != null) ivFlag.setImageResource(R.drawable.ic_english);
            if (tvLangCode != null) tvLangCode.setText("EN");
        }
        if (tvWelcome != null) tvWelcome.setText(getString(R.string.welcome_text));
    }

    private void startApp() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}