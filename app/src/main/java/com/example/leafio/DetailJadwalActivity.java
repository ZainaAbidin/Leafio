package com.example.leafio;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

// 1. Ganti extends AppCompatActivity menjadi BaseActivity
public class DetailJadwalActivity extends BaseActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private String namaTanamanGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        ImageView btnBack = findViewById(R.id.btn_back);
        SwitchCompat switchNotif = findViewById(R.id.switch_notif);
        TextView tvNamaTanaman = findViewById(R.id.tv_nama_tanaman_detail);
        TextView tvRingkasan = findViewById(R.id.tv_ringkasan_data);

        SharedPreferences sharedPreferences = getSharedPreferences("LEAFIO_PREFS", MODE_PRIVATE);
        namaTanamanGlobal = sharedPreferences.getString("PLANT_NAME", "TANAMAN");
        String jenis = sharedPreferences.getString("PLANT_TYPE", "-");
        String siram = sharedPreferences.getString("WATERING_DATE", "-");
        String pupuk = sharedPreferences.getString("FERTILIZER_DATE", "-");
        String catatan = sharedPreferences.getString("PLANT_NOTE", getString(R.string.default_note));

        tvNamaTanaman.setText(namaTanamanGlobal);

        // 2. Gunakan getString dengan format agar teks ringkasan mendukung multi-bahasa
        String teksRingkasan = getString(R.string.summary_format, siram, pupuk, jenis, catatan);
        tvRingkasan.setText(teksRingkasan);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailJadwalActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkAndShowNotification();
            }
        });
    }

    private void checkAndShowNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                showSimpleNotification(namaTanamanGlobal);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        } else {
            showSimpleNotification(namaTanamanGlobal);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSimpleNotification(namaTanamanGlobal);
            } else {
                // Gunakan string resource untuk pesan Toast
                Toast.makeText(this, getString(R.string.notif_denied), Toast.LENGTH_SHORT).show();
                SwitchCompat switchNotif = findViewById(R.id.switch_notif);
                switchNotif.setChecked(false);
            }
        }
    }

    private void showSimpleNotification(String namaTanaman) {
        String channelId = "leafio_notif";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Leafio Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_splash)
                .setContentTitle("Leafio Reminder")
                // Gunakan string resource untuk konten notifikasi
                .setContentText(getString(R.string.notif_active, namaTanaman))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}