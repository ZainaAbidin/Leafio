package com.example.leafio;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private RecyclerView rvDaftarTanaman;
    private PlantAdapter adapter;
    private ArrayList<Tanaman> listTanaman;
    private TextView tvEmptyState;
    private Button btnBuatJadwal;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvDaftarTanaman = findViewById(R.id.rv_daftar_tanaman);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        btnBuatJadwal = findViewById(R.id.btn_buat_jadwal);
        rvDaftarTanaman.setLayoutManager(new LinearLayoutManager(this));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateLocationAndLanguage();

        btnBuatJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InputJadwalActivity.class);
            startActivity(intent);
        });
    }

    private void updateLocationAndLanguage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        // --- MULAI KODE TERBARU ---
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                // Toast untuk memastikan koordinat berhasil ditarik
                Toast.makeText(this, "Koordinat: " + location.getLatitude(), Toast.LENGTH_SHORT).show();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String countryCode = addresses.get(0).getCountryCode();

                        // Cek di Logcat (Filter: DEBUG_NEGARA)
                        Log.d("DEBUG_NEGARA", "Negara terdeteksi: " + countryCode);
                        Toast.makeText(this, "Negara: " + countryCode, Toast.LENGTH_SHORT).show();

                        if ("ID".equalsIgnoreCase(countryCode)) {
                            setLocale("in");
                        } else {
                            setLocale("en");
                        }
                    }
                } catch (IOException e) {
                    Log.e("LOC_ERROR", "Geocoder Error: " + e.getMessage());
                }
            } else {
                // Muncul jika emulator/HP belum "mengunci" lokasi GPS
                Toast.makeText(this, "Lokasi Null. Coba buka Google Maps dulu.", Toast.LENGTH_LONG).show();
            }
        });
        // --- SELESAI KODE TERBARU ---
    }

    public void setLocale(String langCode) {
        String currentLang = getResources().getConfiguration().getLocales().get(0).getLanguage();
        if (currentLang.equals(langCode)) {
            return;
        }

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateLocationAndLanguage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LEAFIO_PREFS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("PLANT_LIST", null);
        Type type = new TypeToken<ArrayList<Tanaman>>() {}.getType();

        if (json == null) {
            listTanaman = new ArrayList<>();
        } else {
            listTanaman = gson.fromJson(json, type);
        }

        if (listTanaman.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvDaftarTanaman.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvDaftarTanaman.setVisibility(View.VISIBLE);
            adapter = new PlantAdapter(listTanaman, position -> showDeleteDialog(position));
            rvDaftarTanaman.setAdapter(adapter);
        }
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Tanaman")
                .setMessage("Yakin ingin menghapus " + listTanaman.get(position).getNama() + "?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    listTanaman.remove(position);
                    saveDataToPrefs();
                    loadData();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void saveDataToPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("LEAFIO_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listTanaman);
        editor.putString("PLANT_LIST", json);
        editor.apply();
    }
}