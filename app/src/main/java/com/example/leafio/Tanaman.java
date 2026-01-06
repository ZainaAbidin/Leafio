package com.example.leafio;

public class Tanaman {
    private String nama;
    private String jenis;
    private String siram;
    private String pupuk;
    private String catatan;

    // Konstruktor untuk membuat objek tanaman baru
    public Tanaman(String nama, String jenis, String siram, String pupuk, String catatan) {
        this.nama = nama;
        this.jenis = jenis;
        this.siram = siram;
        this.pupuk = pupuk;
        this.catatan = catatan;
    }

    // Getter (Penting agar data bisa dibaca oleh Adapter/Activity)
    public String getNama() { return nama; }
    public String getJenis() { return jenis; }
    public String getSiram() { return siram; }
    public String getPupuk() { return pupuk; }
    public String getCatatan() { return catatan; }
}