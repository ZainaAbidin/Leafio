package com.example.leafio;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Tetap terapkan bahasa saat activity dibuat
        applyLocale(getResources().getConfiguration().getLocales().get(0).getLanguage());
    }

    // Fungsi pusat untuk mengubah bahasa
    public void setLocale(String langCode) {
        String currentLang = getResources().getConfiguration().getLocales().get(0).getLanguage();
        if (currentLang.equals(langCode)) {
            return;
        }

        applyLocale(langCode);

        // Restart activity agar perubahan terlihat
        Intent refresh = new Intent(this, this.getClass());
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
        finish();
    }

    private void applyLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}