package service;

import dbconnection.DBConnection;
import model.UserSession;

import java.sql.*;

public class AuthService {

    public static UserSession login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");

                // Ambil ID pelanggan/admin dari tabel user
                Integer pelangganId = rs.getObject("pelanggan_id") != null ? rs.getInt("pelanggan_id") : null;
                Integer adminId = rs.getObject("admin_id") != null ? rs.getInt("admin_id") : null;

                System.out.println("Login berhasil sebagai " + role+"!");
                return new UserSession(userId, role, pelangganId, adminId);
            } else {
                System.out.println("Username atau password salah!");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat login:");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean registerPelanggan(String nama, String noHp, String alamat, String username, String password) {
    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false); // Penting untuk rollback jika gagal

        // 1. Insert ke tabel pelanggan dulu
        String insertPelanggan = "INSERT INTO pelanggan (nama, no_hp, alamat) VALUES (?, ?, ?)";
        PreparedStatement psPelanggan = conn.prepareStatement(insertPelanggan, Statement.RETURN_GENERATED_KEYS);
        psPelanggan.setString(1, nama);
        psPelanggan.setString(2, noHp);
        psPelanggan.setString(3, alamat);
        psPelanggan.executeUpdate();

        ResultSet rsPelanggan = psPelanggan.getGeneratedKeys();
        if (rsPelanggan.next()) {
            int pelangganId = rsPelanggan.getInt(1);

            // 2. Insert ke tabel user, dengan pelanggan_id
            String insertUser = "INSERT INTO user (username, password, role, pelanggan_id) VALUES (?, ?, 'pelanggan', ?)";
            PreparedStatement psUser = conn.prepareStatement(insertUser);
            psUser.setString(1, username);
            psUser.setString(2, password);
            psUser.setInt(3, pelangganId);
            psUser.executeUpdate();

            conn.commit();
            return true;
        }

        conn.rollback(); // Jika tidak dapat pelangganId
    } catch (SQLException e) {
        System.out.println("Gagal registrasi: " + e.getMessage());
    }
    return false;
}

    // Tidak perlu method getPelangganIdByUserId karena relasi sudah diambil dari tabel user
}