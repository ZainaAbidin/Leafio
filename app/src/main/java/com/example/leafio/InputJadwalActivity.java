package com.example.leafio;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Tambahkan import GSON dan List
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class InputJadwalActivity extends BaseActivity {

    private EditText etNamaTanaman, etJenisTanaman, etPenyiraman, etPemupukan;
    private ImageView ivCalPenyiraman, ivCalPemupukan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_jadwal);

        etNamaTanaman = findViewById(R.id.et_nama_tanaman);
        etJenisTanaman = findViewById(R.id.et_jenis_tanaman);
        etPenyiraman = findViewById(R.id.et_jadwal_penyiraman);
        etPemupukan = findViewById(R.id.et_jadwal_pemupukan);
        ivCalPenyiraman = findViewById(R.id.iv_cal_penyiraman);
        ivCalPemupukan = findViewById(R.id.iv_cal_pemupukan);
        Button btnBuatJadwal = findViewById(R.id.btn_buat_jadwal_final);

        ivCalPenyiraman.setOnClickListener(v -> showDatePicker(etPenyiraman));
        etPenyiraman.setOnClickListener(v -> showDatePicker(etPenyiraman));
        ivCalPemupukan.setOnClickListener(v -> showDatePicker(etPemupukan));
        etPemupukan.setOnClickListener(v -> showDatePicker(etPemupukan));

        btnBuatJadwal.setOnClickListener(v -> {
            saveDataAndMove();
        });
    }

    private void saveDataAndMove() {
        String nama = etNamaTanaman.getText().toString().trim();
        String jenis = etJenisTanaman.getText().toString().trim();
        String siram = etPenyiraman.getText().toString().trim();
        String pupuk = etPemupukan.getText().toString().trim();

        if (nama.isEmpty()) {
            etNamaTanaman.setError("Nama tanaman wajib diisi");
            return;
        }
        if (siram.isEmpty()) {
            etPenyiraman.setError("Pilih tanggal penyiraman");
            return;
        }

        // --- LOGIKA PENYIMPANAN LIST (MULTI-DATA) ---
        SharedPreferences sharedPreferences = getSharedPreferences("LEAFIO_PREFS", MODE_PRIVATE);
        Gson gson = new Gson();

        // 1. Ambil list yang sudah ada dari SharedPreferences
        String json = sharedPreferences.getString("PLANT_LIST", null);
        Type type = new TypeToken<ArrayList<Tanaman>>() {}.getType();
        ArrayList<Tanaman> listTanaman;

        if (json == null) {
            listTanaman = new ArrayList<>();
        } else {
            listTanaman = gson.fromJson(json, type);
        }

        // 2. Buat objek tanaman baru menggunakan data dari input
        Tanaman tanamanBaru = new Tanaman(
                nama.toUpperCase(),
                jenis.isEmpty() ? "Tidak diketahui" : jenis,
                siram,
                pupuk.isEmpty() ? "Belum diatur" : pupuk,
                "Daun muda agak sensitif"
        );

        // 3. Tambahkan tanaman baru ke dalam list
        listTanaman.add(tanamanBaru);

        // 4. Simpan kembali list yang sudah diperbarui ke SharedPreferences
        String updatedJson = gson.toJson(listTanaman);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PLANT_LIST", updatedJson);
        editor.putBoolean("IS_REGISTERED", true);

        // Simpan data terakhir secara individual untuk kebutuhan Slide Ringkasan (DetailJadwal)
        editor.putString("PLANT_NAME", tanamanBaru.getNama());
        editor.putString("PLANT_TYPE", tanamanBaru.getJenis());
        editor.putString("WATERING_DATE", tanamanBaru.getSiram());
        editor.putString("FERTILIZER_DATE", tanamanBaru.getPupuk());
        editor.apply();

        closeKeyboard();
        Toast.makeText(this, "Jadwal " + nama + " Berhasil Dibuat!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(InputJadwalActivity.this, DetailJadwalActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDatePicker(final EditText targetEditText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    targetEditText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void closeKeyboard() {
        android.view.View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}