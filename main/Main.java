package main;

import service.AuthService;
import ui.AdminMenu;
import ui.UserMenu;
import java.util.Scanner;
import model.UserSession;
import dbconnection.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("================= SISTEM BOOKING SERVIS ================");
            System.out.println("Pilih jenis login:");
            System.out.println("1. Admin");
            System.out.println("2. Pelanggan");
            System.out.println("0. Keluar");
            System.out.print("Pilihan Anda: ");
            String pilihan = input.nextLine();

            if (pilihan.equals("0")) {
                System.out.println("Terima kasih. Aplikasi ditutup.");
                break;
            }

            String expectedRole = null;
            switch (pilihan) {
                case "1":
                    expectedRole = "admin";
                    break;
                case "2":
                    System.out.print("Sudah punya akun? (yes/no): ");
                    String jawab = input.nextLine();

                    if (jawab.equalsIgnoreCase("no")) {
                        // REGISTRASI PELANGGAN BARU
                        System.out.print("Nama: ");
                        String nama = input.nextLine();
                        System.out.print("No HP: ");
                        String noHp = input.nextLine();
                        System.out.print("Alamat: ");
                        String alamat = input.nextLine();
                        System.out.print("Buat Username: ");
                        String usernameBaru = input.nextLine();
                        System.out.print("Buat Password: ");
                        String passwordBaru = input.nextLine();

                        boolean sukses = AuthService.registerPelanggan(nama, noHp, alamat, usernameBaru, passwordBaru);
                        if (sukses) {
                            System.out.println("Registrasi berhasil! Silakan login.");
                        } else {
                            System.out.println("Registrasi gagal. Ulangi proses.");
                        }
                        continue; // kembali ke menu awal
                    }
                    expectedRole = "pelanggan";
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.\n");
                    continue;
            }

            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();

            UserSession userSession = AuthService.login(username, password);

            if (userSession != null) {
                String actualRole = userSession.getRole();

                if (expectedRole.equalsIgnoreCase(actualRole)) {
                    Connection conn = DBConnection.getConnection();

                    switch (actualRole.toLowerCase()) {
                        case "admin":
                            AdminMenu adminMenu = new AdminMenu(conn);
                            adminMenu.tampilkanMenu();
                            break;

                        case "pelanggan":
                            UserMenu userMenu = new UserMenu();
                            userMenu.tampilkanMenu(userSession);
                            break;
                    }

                    System.out.println("\n================= LOGOUT BERHASIL ==================\n");

                } else {
                    System.out.println("Akun ini bukan role " + expectedRole + ". Silakan login ulang.\n");
                }
            } else {
                System.out.println("Login gagal. Username atau password salah.\n");
            }
        }
    }
}