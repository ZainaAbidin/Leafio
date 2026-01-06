# Project Leafio

## 1. Sistem Deteksi Lokasi dan Personalisasi Bahasa Otomatis
Aplikasi Leafio dilengkapi dengan kecerdasan untuk mengenali lokasi pengguna saat pertama kali dijalankan 
melalui layar Splash Screen. Dengan memanfaatkan `FusedLocationProviderClient dan Geocoder`, aplikasi mampu mendeteksi kode negara pengguna secara real-time. 
Jika pengguna terdeteksi berada di wilayah Indonesia `("ID")`, sistem secara otomatis memicu fungsi setLocale untuk mengubah seluruh antarmuka aplikasi menjadi Bahasa Indonesia. 
Hal ini menciptakan pengalaman pengguna yang sangat personal dan intuitif, di mana elemen visual 
seperti teks `"Selamat Datang"` dan ikon bendera pada container bahasa akan menyesuaikan secara dinamis tanpa memerlukan pengaturan manual yang rumit.

## 2. Manajemen Jadwal Perawatan Tanaman yang Terpersonalisasi
Fitur utama dari Leafio adalah kemampuannya untuk menyimpan dan mengelola data 
perawatan tanaman secara spesifik bagi setiap pengguna. Melalui interaksi antara `InputJadwalActivity dan DetailJadwalActivity`, pengguna dapat 
mencatat berbagai informasi penting seperti nama tanaman, jenis tanaman, hingga jadwal rutin penyiraman dan pemupukan. 
Data ini disimpan secara permanen menggunakan `SharedPreferences`, sehingga informasi tetap terjaga meskipun aplikasi ditutup. 
Pada halaman detail, aplikasi menyajikan ringkasan informasi yang lengkap termasuk catatan khusus (seperti sensitivitas daun muda), memudahkan 
pengguna untuk memahami kebutuhan nutrisi dan hidrasi tanaman mereka dalam satu tampilan yang bersih.

## 3. Sistem Pengingat Cerdas dengan Keamanan Notifikasi Android 13+
Leafio telah mengadopsi standar keamanan terbaru untuk platform Android, khususnya dalam menangani 
fitur notifikasi pada versi Android 13 ke atas. Aplikasi ini tidak hanya mampu mengirimkan pengingat rutin, 
tetapi juga menerapkan protokol Runtime Permission yang ketat. Sebelum notifikasi diaktifkan melalui komponen `SwitchCompat`, aplikasi akan 
secara otomatis memeriksa izin POST_NOTIFICATIONS dan memberikan dialog permintaan izin kepada pengguna jika diperlukan. Pendekatan ini 
memastikan aplikasi mematuhi kebijakan privasi sistem operasi sekaligus menjamin bahwa pengguna tidak akan melewatkan 
jadwal perawatan tanaman penting mereka melalui notifikasi yang muncul di system tray.

## 4. Arsitektur Kode yang Terstruktur dengan BaseActivity
Pengembangan Leafio menggunakan pola arsitektur yang rapi dengan mengimplementasikan `BaseActivity` sebagai kelas induk 
untuk seluruh aktivitas dalam aplikasi. Dengan struktur ini, logika global seperti pengaturan bahasa (localization) 
dan konfigurasi sumber daya tidak perlu ditulis berulang kali di setiap file Java. Aktivitas seperti `SplashScreenActivity, MainActivity, dan DetailJadwalActivity` cukup 
mewarisi sifat dari BaseActivity, sehingga pembaruan bahasa atau konfigurasi tema dapat dilakukan secara terpusat.
Hal ini tidak hanya meminimalkan risiko bug akibat duplikasi kode, tetapi juga membuat aplikasi lebih mudah untuk dikembangkan lebih lanjut di masa depan (scalability).

## 5. Antarmuka Pengguna Modern yang Responsif dan Informatif
Leafio dirancang dengan estetika visual yang bersih dan modern, menggunakan 
kombinasi warna hijau alami yang merepresentasikan kesehatan tanaman. Layout aplikasi 
dibangun menggunakan ConstraintLayout untuk memastikan tampilan tetap proporsional dan responsif 
di berbagai ukuran layar perangkat Android. Elemen UI seperti logo splash, indikator bahasa di pojok 
kanan atas, hingga tombol navigasi balik (Back Button) diletakkan dengan mempertimbangkan kenyamanan penggunaan 
satu tangan. Selain itu, penggunaan String Resource yang terstandarisasi memungkinkan aplikasi untuk melakukan 
transisi bahasa secara mulus tanpa merusak tata letak teks, memberikan kesan profesional dan solid pada keseluruhan aplikasi.
