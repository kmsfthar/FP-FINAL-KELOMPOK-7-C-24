# 🚗 Sistem Booking Servis Bengkel Mobil 
Sistem informasi ini dirancang untuk membantu pengelolaan layanan servis kendaraan secara digital di bengkel. Mulai dari pencatatan pelanggan, mobil, teknisi, hingga proses booking dan pembayaran secara efisien dan otomatis.

---

## 👨‍💻 Anggota Kelompok 7 

| Nama Lengkap              | NPM           |
|---------------------------|---------------|
| Alifia Ardita             | 24082010101   |
| Kemas Fatih A. R.         | 24082010102   |
| Ariel Chavya Ikhrima      | 24082010112   |
| Kholifah Nur Fadilah      | 20482010132   |

> Prodi Sistem Informasi  
> Universitas Pembangunan “Veteran” Jawa Timur

---

## ✨ Fitur Unggulan

🔹 **Manajemen Data Pelanggan, Mobil & Booking**  
🔹 **Role-Based Access (Admin & Pelanggan)**  
🔹 **Trigger & Stored Procedure Otomatis**  
🔹 **Report Interaktif**  
🔹 **Histori Booking dan Cek Jadwal**

---

## 🧱 Struktur Tabel

| Tabel            | Kolom Utama                                         |
|------------------|------------------------------------------------------|
| `user`           | id_user, username, password, role                   |
| `pelanggan`      | id_pelanggan, nama, no_hp, alamat                   |
| `mobil`          | id_mobil, id_pelanggan, merk, model, plat_nomor     |
| `booking_servis` | id_booking, id_pelanggan, id_teknisi, tanggal, status |
| `teknisi`        | id_teknisi, nama, status                            |
| `jenis_servis`   | id_jenis, nama_servis, estimasi_menit, biaya        |
| `detail_servis`  | id_detail, id_booking, id_jenis_servis              |
| `transaksi_servis`| id_transaksi, id_booking, total_bayar, metode      |

---

## 💻 Cara Menjalankan Program

#### 1️⃣ Compile Program
```bash
javac -cp ".:mysql-connector-java-8.0.xx.jar" Main.java
```

#### 2️⃣ Jalankan Program
```bash
java -cp ".:mysql-connector-java-8.0.xx.jar" Main
```

> Gunakan terminal/command prompt di folder project.

---

## 🖥️ Menu Utama Program
| Mode Admin                     | Mode Pelanggan              |
|-------------------------------|-----------------------------|
| Melihat daftar pelanggan      | Tambah mobil       |
| Lihat Daftar Booking Baru | Membuat Booking servis baru            |
| Kelola teknisi (CRUD)    | Lihat riwayat booking              |
| Kelola jenis servis (CRUD)         | Cek jadwal booking       |
| Konfirmasi pembayaran      |           |
| Laporan servis (3 jenis)      |         |

---

## ⚙️ Trigger Otomatis

### 1. Saat booking dibuat (`update_status_teknisi_sibuk`)
```sql
DELIMITER $$
CREATE TRIGGER update_status_teknisi_sibuk
AFTER INSERT ON booking_servis
FOR EACH ROW
BEGIN
    IF NEW.status = 'aktif' THEN
        UPDATE teknisi
        SET status = 'sibuk'
        WHERE teknisi_id = NEW.teknisi_id;
    END IF;
END $$
DELIMITER ;
```

### 2. Saat pembayaran dilakukan (`after_insert_transaksi_servis`)
```sql
DELIMITER $$
CREATE TRIGGER after_insert_transaksi_servis
AFTER INSERT ON transaksi_servis
FOR EACH ROW
BEGIN
    UPDATE booking_servis
    SET status = 'selesai'
    WHERE booking_id = NEW.booking_id;
END $$
DELIMITER ;
```

### 3. Saat booking selesai (`trg_set_teknisi_tersedia`)
```sql
DELIMITER $$
CREATE TRIGGER trg_set_teknisi_tersedia
AFTER UPDATE ON booking_servis
FOR EACH ROW
BEGIN
    IF NEW.status = 'selesai' THEN
        UPDATE teknisi
        SET status = 'tersedia'
        WHERE teknisi_id = NEW.teknisi_id;
    END IF;
END $$
DELIMITER ;
```

---

## 📄 SQL View

### View Booking Aktif (`booking_tersedia`)
```sql
CREATE OR REPLACE VIEW booking_tersedia AS
SELECT 
    b.booking_id,
    p.nama,
    m.merek,
    m.model,
    b.tanggal
FROM booking_servis b
JOIN pelanggan p ON b.pelanggan_id = p.pelanggan_id
JOIN mobil m ON b.mobil_id = m.mobil_id
WHERE b.status = 'aktif';
```

📌 View ini digunakan untuk menampilkan daftar booking servis yang **masih aktif**, dan membantu admin dalam memantau antrean servis.

---

## 🧮 Stored Procedure

### Hitung Estimasi Waktu Servis (`estimasi_waktu_pengerjaan`)
```sql
DELIMITER $$
CREATE PROCEDURE estimasi_waktu_pengerjaan (
    IN p_booking_id INT,
    OUT p_estimasi_total INT
)
BEGIN
    SELECT SUM(js.estimasi_menit)
    INTO p_estimasi_total
    FROM detail_servis ds
    JOIN jenis_servis js ON ds.jenis_servis_id = js.jenis_servis_id
    WHERE ds.booking_id = p_booking_id;
END $$
DELIMITER ;
```

---

## 📌 Catatan Tambahan

🧠 Semua proses backend dirancang dengan **DAO Pattern**  
🔒 Data aman karena menggunakan koneksi terpusat (`DBConnection`)  
🔄 Integrasi MySQL via `mysql-connector-java`  
📊 Laporan dihasilkan via **Query SQL & View**

---

## 🏁 Lisensi

Proyek ini disusun untuk keperluan Final Project EAS pada mata kuliah **Basis Data**. Bebas digunakan untuk pembelajaran. 🚀

---
