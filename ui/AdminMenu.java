/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author alifia ardita
 */

import dao.*;
import java.util.InputMismatchException;
import model.*;
import java.util.List;
import java.util.Scanner;
import java.sql.*;
import view.BookingTersediaView;
import report.PelangganBookingStatusReport;
import report.PelangganMobilReport;
import report.TeknisiBookingReport;

public class AdminMenu {
    private static Scanner scanner = new Scanner(System.in);
    private static PelangganDAO pelangganDAO = new PelangganDAO();
    private static TeknisiDAO teknisiDAO = new TeknisiDAO();
    private static JenisServisDAO jenisServisDAO = new JenisServisDAO();
    private Connection conn;

    public AdminMenu(Connection conn) {
        this.conn = conn;
        this.scanner = new Scanner(System.in);
    }

    public void tampilkanMenu() {
        int pilihan;

        do {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Lihat Daftar Pelanggan");
            System.out.println("2. Lihat Booking Aktif");
            System.out.println("3. Kelola Teknisi");
            System.out.println("4. Kelola Jenis Servis");
            System.out.println("5. Pembayaran");
            System.out.println("6. laporan");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");
            pilihan = Integer.parseInt(scanner.nextLine());

            switch (pilihan) {
                case 1:
                    lihatDaftarPelanggan();
                    break;
                case 2:
                    tampilkanBookingTersedia();
                    break;
                case 3:
                    kelolaTeknisi();
                    break;
                case 4:
                    kelolaJenisServis();
                    break;
                case 5:
                    menuPembayaran();
                    break;
                case 6:
                    ReportDAO reportDAO = new ReportDAO();

                    // CTE report
                    System.out.println("Laporan Booking per Teknisi:");
                    for (TeknisiBookingReport v : reportDAO.getBookingPerTeknisi()) {
                        System.out.println("- " + v.getNamaTeknisi() + ": " + v.getJumlahBooking() + " booking");
                    }

                    // Subquery report
                    System.out.println("\nLaporan Mobil per Pelanggan:");
                    for (PelangganMobilReport v : reportDAO.getMobilPerPelanggan()) {
                        System.out.println("- " + v.getNamaPelanggan() + ": " + v.getJumlahMobil() + " mobil");
                    }

                    // Crosstab report
                    System.out.println("\nLaporan Status Booking per Pelanggan:");
                    for (PelangganBookingStatusReport v : reportDAO.getStatusBookingPerPelanggan()) {
                        System.out.println("- " + v.getNamaPelanggan() + ": Aktif=" + v.getAktif() +
                            ", Selesai=" + v.getSelesai() + ", Batal=" + v.getBatal());
                    }

                    break;
                case 0:
                    System.out.println("Logout...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    private static void lihatDaftarPelanggan() {
        List<Pelanggan> pelangganList = pelangganDAO.getAllPelanggan();
        if (pelangganList.isEmpty()) {
            System.out.println("Belum ada data pelanggan.");
        } else {
            System.out.println("\nDaftar Pelanggan:");
            for (Pelanggan p : pelangganList) {
                System.out.printf("ID: %d | Nama: %s | No HP: %s | Alamat: %s\n",
                    p.getIdPelanggan(), p.getNamaPelanggan(), p.getNoHp(), p.getAlamat());
            }
        }
    }

    private static void kelolaTeknisi() {
        int pilihan;
        do {
            System.out.println("\n=== Kelola Teknisi ===");
            System.out.println("1. Lihat Teknisi");
            System.out.println("2. Tambah Teknisi");
            System.out.println("3. Edit Teknisi");
            System.out.println("4. Hapus Teknisi");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");
            pilihan = Integer.parseInt(scanner.nextLine());

            switch (pilihan) {
                case 1:
                    lihatTeknisi();
                    break;
                case 2:
                    tambahTeknisi();
                    break;
                case 3:
                    editTeknisi();
                    break;
                case 4:
                    hapusTeknisi();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    private static void lihatTeknisi() {
        List<Teknisi> teknisiList = teknisiDAO.getAllTeknisi();
        if (teknisiList.isEmpty()) {
            System.out.println("Belum ada data teknisi.");
        } else {
            System.out.println("\nDaftar Teknisi:");
            for (Teknisi t : teknisiList) {
                System.out.printf("ID: %d | Nama: %s | Status: %s\n", t.getIdTeknisi(), 
                        t.getNamaTeknisi(), t.getStatus());
            }
        }
    }

    private static void tambahTeknisi() {
        System.out.print("Masukkan nama teknisi: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan status teknisi (tersedia/sibuk): ");
        String status = scanner.nextLine();

        Teknisi t = new Teknisi();
        t.setNamaTeknisi(nama);
        t.setStatus(status);

        teknisiDAO.insertTeknisi(t);
        System.out.println("Teknisi berhasil ditambahkan.");
    }

    private static void editTeknisi() {
        System.out.print("Masukkan ID teknisi yang ingin diedit: ");
        int id = Integer.parseInt(scanner.nextLine());
        Teknisi t = teknisiDAO.getTeknisiById(id);
        if (t == null) {
            System.out.println("Teknisi tidak ditemukan.");
            return;
        }

        System.out.print("Masukkan nama teknisi baru (" + t.getNamaTeknisi() + "): ");
        String nama = scanner.nextLine();
        if (!nama.isEmpty()) {
            t.setNamaTeknisi(nama);
        }

        System.out.print("Masukkan status teknisi baru (" + t.getStatus() + "): ");
        String status = scanner.nextLine();
        if (!status.isEmpty()) {
            t.setStatus(status);
        }

        teknisiDAO.updateTeknisi(t);
        System.out.println("Teknisi berhasil diperbarui.");
    }

    private static void hapusTeknisi() {
      System.out.print("Masukkan ID teknisi yang ingin dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        boolean berhasil = teknisiDAO.deleteTeknisi(id);
        if (berhasil) {
            System.out.println("Teknisi berhasil dihapus.");
        } else {
            System.out.println("ID tidak ditemukan. Tidak ada yang dihapus.");
        }

    }

   private static void kelolaJenisServis() {
    int pilihan = -1;

    do {
        System.out.println("\n=== Kelola Jenis Servis ===");
        System.out.println("1. Lihat Jenis Servis");
        System.out.println("2. Tambah Jenis Servis");
        System.out.println("3. Edit Jenis Servis");
        System.out.println("4. Hapus Jenis Servis");
        System.out.println("0. Kembali");
        System.out.print("Pilih: ");

        String input = scanner.nextLine();
        if (input.isEmpty()) {
            System.out.println("Input tidak boleh kosong. Silakan coba lagi.");
            continue;
        }

        try {
            pilihan = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka. Silakan coba lagi.");
            continue;
        }

        switch (pilihan) {
            case 1:
                lihatJenisServis();
                break;
            case 2:
                tambahJenisServis();
                break;
            case 3:
                editJenisServis();
                break;
            case 4:
                hapusJenisServis();
                break;
            case 0:
                break;
            default:
                System.out.println("Pilihan tidak valid.");
        }
    } while (pilihan != 0);
}


    private static void lihatJenisServis() {
        List<JenisServis> jenisServisList = jenisServisDAO.getAll();
        if (jenisServisList.isEmpty()) {
            System.out.println("Belum ada data jenis servis.");
        } else {
            System.out.println("\nDaftar Jenis Servis:");
            for (JenisServis js : jenisServisList) {
               System.out.printf("ID: %d | Nama: %s | Estimasi Waktu: %d menit | Biaya: Rp %,.2f\n",
               js.getIdServis(), js.getNamaServis(), js.getEstimasiWaktu(), js.getBiaya());

            }
        }
    }

   private static void tambahJenisServis() {
    System.out.print("Masukkan nama jenis servis: ");
    String nama = scanner.nextLine();
    
    System.out.print("Masukkan estimasi waktu (menit): ");
    int estimasi = Integer.parseInt(scanner.nextLine());
    
    System.out.print("Masukkan biaya (angka saja): ");
    double biaya = Double.parseDouble(scanner.nextLine());

    JenisServis js = new JenisServis();
    js.setNamaServis(nama);
    js.setEstimasiWaktu(estimasi);
    js.setBiaya(biaya);  // <-- Tambahkan ini

    jenisServisDAO.insertJenisServis(js);
    System.out.println("Jenis servis berhasil ditambahkan.");
}


 private static void editJenisServis() {
    System.out.print("Masukkan ID jenis servis yang ingin diedit: ");
    int id = Integer.parseInt(scanner.nextLine());
    JenisServis js = jenisServisDAO.getJenisServisById(id);
    if (js == null) {
        System.out.println("Jenis servis tidak ditemukan.");
        return;
    }

    System.out.print("Masukkan nama jenis servis baru (" + js.getNamaServis() + "): ");
    String nama = scanner.nextLine();
    if (!nama.isEmpty()) {
        js.setNamaServis(nama);
    }

    System.out.print("Masukkan estimasi waktu baru (" + js.getEstimasiWaktu() + " menit): ");
    String estimasiStr = scanner.nextLine();
    if (!estimasiStr.isEmpty()) {
        js.setEstimasiWaktu(Integer.parseInt(estimasiStr));
    }

    System.out.print("Masukkan biaya baru (" + js.getBiaya() + "): ");
    String biayaStr = scanner.nextLine();
    if (!biayaStr.isEmpty()) {
        js.setBiaya(Double.parseDouble(biayaStr));
    }

    jenisServisDAO.updateJenisServis(js);
    System.out.println("Jenis servis berhasil diperbarui.");
}


   private static void hapusJenisServis() {
    System.out.print("Masukkan ID jenis servis yang ingin dihapus: ");
    int id;
    try {
        id = scanner.nextInt();
        scanner.nextLine(); // buang newline
    } catch (InputMismatchException e) {
        System.out.println("Input harus berupa angka.");
        scanner.nextLine(); // buang input buruk
        return;
    }

    boolean deleted = jenisServisDAO.deleteJenisServis(id);
    if (deleted) {
        System.out.println("Jenis servis berhasil dihapus.");
    } else {
        System.out.println("ID tidak ditemukan. Tidak ada yang dihapus.");
    }
}
   public void menuPembayaran() {
    try {
        System.out.print("Masukkan ID Booking: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Hitung total biaya
        String queryTotal = """
            SELECT SUM(js.biaya) AS total_biaya
            FROM detail_servis ds
            JOIN jenis_servis js ON ds.jenis_servis_id = js.jenis_servis_id
            WHERE ds.booking_id = ?
        """;
        PreparedStatement psTotal = conn.prepareStatement(queryTotal);
        psTotal.setInt(1, bookingId);
        ResultSet rs = psTotal.executeQuery();

        if (rs.next()) {
            double totalBiaya = rs.getDouble("total_biaya");
            if (rs.wasNull()) {
                System.out.println("Total Biaya: Rp 0 (tidak ada layanan terdaftar)");
                return; // Keluar dari metode, karena tidak mungkin lanjut ke pembayaran
            } else {
                System.out.println("Total Biaya: Rp " + totalBiaya);
            }

            System.out.print("Masukkan Metode Pembayaran (Cash/Transfer): ");
            String metode = scanner.nextLine();
            Date tanggal = new Date(System.currentTimeMillis());

            // Simpan transaksi
            String insertTransaksi = """
                INSERT INTO transaksi_servis (booking_id, total_biaya, metode_pembayaran, tanggal_transaksi)
                VALUES (?, ?, ?, ?)
            """;
            PreparedStatement psInsert = conn.prepareStatement(insertTransaksi);
            psInsert.setInt(1, bookingId);
            psInsert.setDouble(2, totalBiaya);
            psInsert.setString(3, metode);
            psInsert.setDate(4, tanggal);

            int result = psInsert.executeUpdate();
            if (result > 0) {
                System.out.println("Pembayaran berhasil disimpan.");

                // Konfirmasi update status booking (dilakukan otomatis oleh trigger, tapi kita tampilkan)
                String checkStatus = "SELECT status FROM booking_servis WHERE booking_id = ?";
                PreparedStatement psCheck = conn.prepareStatement(checkStatus);
                psCheck.setInt(1, bookingId);
                ResultSet rsStatus = psCheck.executeQuery();

                if (rsStatus.next()) {
                    String status = rsStatus.getString("status");
                    System.out.println("Status booking sekarang: " + status);
                }
            } else {
                System.out.println("Gagal menyimpan pembayaran.");
            }

        } else {
            System.out.println("Booking ID tidak ditemukan atau belum memiliki layanan.");
        }

    } catch (SQLException e) {
        System.err.println("Terjadi kesalahan database: " + e.getMessage());
    }
}

   public void tampilkanBookingTersedia() {
    BookingTersediaDAO dao = new BookingTersediaDAO();
    List<BookingTersediaView> daftarBooking = dao.getAllBookingTersedia();

    System.out.println("===== DAFTAR BOOKING MENUNGGU =====");
    for (BookingTersediaView booking : daftarBooking) {
        System.out.println("ID Booking: " + booking.getBookingId());
        System.out.println("Nama Pelanggan: " + booking.getNamaPelanggan());
        System.out.println("Mobil: " + booking.getMerkMobil() + " " + booking.getModelMobil());
        System.out.println("Tanggal Booking: " + booking.getTanggalBooking());
        System.out.println("------------------------------------");
    }
    }

}
