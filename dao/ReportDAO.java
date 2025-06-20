package dao;

import dbconnection.DBConnection;
import view.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import report.TeknisiBookingReport;
import report.PelangganBookingStatusReport;
import report.PelangganMobilReport;

public class ReportDAO {

    public List<TeknisiBookingReport> getBookingPerTeknisi() {
        List<TeknisiBookingReport> list = new ArrayList<>();
        String sql = """
            WITH total_booking AS (
                SELECT teknisi_id, COUNT(*) AS jumlah_booking
                FROM booking_servis GROUP BY teknisi_id
            )
            SELECT t.nama, tb.jumlah_booking
            FROM total_booking tb
            JOIN teknisi t ON t.teknisi_id = tb.teknisi_id;
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new TeknisiBookingReport(rs.getString("nama"), rs.getInt("jumlah_booking")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PelangganMobilReport> getMobilPerPelanggan() {
        List<PelangganMobilReport> list = new ArrayList<>();
        String sql = """
            SELECT p.nama, (
                SELECT COUNT(*) FROM mobil m WHERE m.pelanggan_id = p.pelanggan_id
            ) AS jumlah_mobil
            FROM pelanggan p;
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new PelangganMobilReport(rs.getString("nama"), rs.getInt("jumlah_mobil")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PelangganBookingStatusReport> getStatusBookingPerPelanggan() {
        List<PelangganBookingStatusReport> list = new ArrayList<>();
        String sql = """
            SELECT 
                p.nama,
                COUNT(CASE WHEN bs.status = 'aktif' THEN 1 END) AS aktif,
                COUNT(CASE WHEN bs.status = 'selesai' THEN 1 END) AS selesai,
                COUNT(CASE WHEN bs.status = 'batal' THEN 1 END) AS batal
            FROM pelanggan p
            JOIN booking_servis bs ON p.pelanggan_id = bs.pelanggan_id
            GROUP BY p.nama;
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new PelangganBookingStatusReport(
                    rs.getString("nama"),
                    rs.getInt("aktif"),
                    rs.getInt("selesai"),
                    rs.getInt("batal")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
