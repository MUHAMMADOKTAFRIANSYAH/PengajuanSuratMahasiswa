# Sistem Pengajuan Surat Mahasiswa Berbasis Web

Aplikasi berbasis web sederhana menggunakan **Java**, **Spring Boot**, dan **Thymeleaf** untuk mendigitalkan proses pengajuan surat keterangan mahasiswa kepada bagian administrasi akademik.

---

## 📌 Latar Belakang & Studi Kasus
Pada proses manual, mahasiswa harus datang langsung ke bagian administrasi untuk mengajukan permohonan dan menunggu petugas memeriksa serta memproses surat. Hal ini menyebabkan antrean fisik dan mahasiswa kesulitan mengetahui kepastian status pengajuan mereka.

Aplikasi ini mengatasi kendala tersebut dengan menyediakan formulir online, antrean verifikasi untuk petugas, sistem penolakan dengan alasan yang transparan, serta penerbitan surat resmi yang dapat langsung dicetak.

> **Catatan Struktur Data:**  
> Sesuai batasan studi kasus, aplikasi ini **tidak menggunakan database eksternal**. Seluruh data pengajuan disimpan sementara di memori runtime Java menggunakan objek `ArrayList` (`Collections.synchronizedList(new ArrayList<PengajuanSurat>())`), sehingga data hanya tersedia selama server aplikasi berjalan.

---

## 🚀 4 Fitur Utama Aplikasi

1. **Formulir Pengajuan Surat** (`/mahasiswa/pengajuan/baru`)
   - Mahasiswa mengisi formulir online: Nama, NIM, Program Studi, Semester, Jenis Surat, dan Alasan / Keperluan pengajuan.
   - Dilengkapi validasi kelengkapan form input.

2. **Daftar Pengajuan** (`/petugas`)
   - Sistem menampilkan daftar pengajuan yang masuk beserta status proses terkininya.
   - Dilengkapi pencarian keyword dan filter tab status (*Semua*, *Menunggu Pemeriksaan*, *Disetujui*, *Ditolak*).

3. **Pemeriksaan Petugas** (`/petugas/periksa/{id}`)
   - Petugas administrasi dapat meninjau detail pengajuan mahasiswa.
   - Petugas dapat memilih:
     - **Setujui Surat**: Otomatis menerbitkan Nomor Surat Resmi.
     - **Tolak Surat**: Petugas wajib mengisi catatan **alasan penolakan**.

4. **Hasil Pengajuan & Cetak Surat** (`/mahasiswa/lacak`)
   - Mahasiswa dapat melacak status secara real-time berdasarkan NIM atau Nomor Pengajuan.
   - Status yang ditampilkan:
     - 🟡 **Menunggu Pemeriksaan**: Dalam antrean review petugas.
     - 🟢 **Disetujui**: Dilengkapi tombol **Cetak Surat Resmi** bertanda tangan dan stempel digital (`/mahasiswa/surat/{id}/cetak`).
     - 🔴 **Ditolak**: Menampilkan keterangan alasan penolakan dari petugas agar mahasiswa memahami perbaikan yang dibutuhkan.

5. **Pemodelan Alur Bisnis BPMN** (`/bpmn`)
   - Menampilkan visualisasi interaktif BPMN Swimlanes (*Mahasiswa*, *Sistem Web*, *Petugas Administrasi*) dari pengajuan awal hingga keputusan akhir.

---

## 📂 Struktur Proyek

```
PengajuanSuratMahasiswa/
├── src/
│   ├── main/
│   │   ├── java/org/example/pengajuansuratmahasiswa/
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java        # Beranda & BPMN routing
│   │   │   │   ├── MahasiswaController.java   # Formulir, Lacak, Cetak
│   │   │   │   └── PetugasController.java     # Panel Petugas, Setujui, Tolak
│   │   │   ├── model/
│   │   │   │   ├── PengajuanSurat.java        # Entity data surat
│   │   │   │   └── StatusPengajuan.java       # Enum status pengajuan
│   │   │   ├── repository/
│   │   │   │   └── PengajuanRepository.java   # In-Memory ArrayList storage
│   │   │   ├── service/
│   │   │   │   └── PengajuanService.java      # Business logic & filter
│   │   │   └── PengajuanSuratMahasiswaApplication.java
│   │   └── resources/
│   │       ├── static/css/
│   │       │   └── style.css                  # Styling modern & print preview
│   │       ├── templates/
│   │       │   ├── fragments/                 # Navbar & Footer
│   │       │   ├── mahasiswa/                 # Form & Lacak status
│   │       │   ├── petugas/                   # Daftar & Detail verifikasi
│   │       │   ├── surat/                     # Kop resmi & cetak surat
│   │       │   ├── bpmn.html                  # Diagram alur proses BPMN
│   │       │   └── index.html                 # Dashboard utama
│   │       └── application.properties
│   └── test/
│       └── java/org/example/pengajuansuratmahasiswa/
│           ├── PengajuanSuratMahasiswaApplicationTests.java
│           └── PengajuanSuratServiceTests.java # 5 Unit test fitur utama
├── pom.xml
└── README.md
```

---

## 💻 Cara Menjalankan Aplikasi

Pastikan Java 17 atau yang lebih baru (misal Java 21 / 23) telah terpasang.

1. Buka terminal di direktori proyek:
   ```powershell
   cd c:\Users\Lenovo\IdeaProjects\PengajuanSuratMahasiswa
   ```

2. Jalankan perintah Maven:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

3. Buka browser dan akses alamat:
   - **Beranda & Dashboard**: [http://localhost:8080/](http://localhost:8080/)
   - **Form Pengajuan Mahasiswa**: [http://localhost:8080/mahasiswa/pengajuan/baru](http://localhost:8080/mahasiswa/pengajuan/baru)
   - **Pelacakan Status Surat**: [http://localhost:8080/mahasiswa/lacak](http://localhost:8080/mahasiswa/lacak)
   - **Panel Petugas Administrasi**: [http://localhost:8080/petugas](http://localhost:8080/petugas)
   - **Diagram Alur BPMN**: [http://localhost:8080/bpmn](http://localhost:8080/bpmn)

4. Menjalankan Unit Tests:
   ```powershell
   .\mvnw.cmd test
   ```
