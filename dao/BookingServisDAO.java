/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author alifia ardita
 */


import dbconnection.DBConnection;
import model.BookingServis;
import model.JenisServis;
import model.Teknisi;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingServisDAO {

   public void insertBooking(BookingServis booking, List<Integer> servisDipilih) {
    String sql = "INSERT INTO booking_servis (pelanggan_id, mobil_id, teknisi_id, tanggal, jam, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
    String insertDetailSQL = "INSERT INTO detail_servis (booking_id, jenis_servis_id) VALUES (?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

        stmt.setInt(1, booking.getIdPelanggan());
        stmt.setInt(2, booking.getIdMobil());
        stmt.setInt(3, booking.getIdTeknisi());
        stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now())); // atau booking.getTanggal()
        stmt.setTime(5, java.sql.Time.valueOf(LocalTime.now()));       // atau booking.getJam()
        stmt.setString(6, "aktif");
        stmt.executeUpdate();
        
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int bookingIdBaru = rs.getInt(1);

            // Step 3: Masukkan ke detail_servis
            try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSQL)) {
                    for (Integer idServis : servisDipilih){
                        detailStmt.setInt(1, bookingIdBaru);
                        detailStmt.setInt(2, idServis);
                        detailStmt.addBatch();
                    }// dari booking.getIdServis()
                    detailStmt.executeBatch();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

   // BookingServisDAO.java
public List<BookingServis> getBookingByTanggal(LocalDate tanggal) {
    List<BookingServis> list = new ArrayList<>();
    String sql = "SELECT \n" +
                "b.booking_id, \n" +
                "b.tanggal, \n" +
                "b.jam, \n" +
                "b.status, \n" +
                "j.nama_servis, \n" +
                "t.nama AS nama_teknisi\n" +
                "FROM booking_servis b\n" +
                "JOIN detail_servis ds ON b.booking_id = ds.booking_id\n" +
                "JOIN jenis_servis j ON ds.jenis_servis_id = j.jenis_servis_id\n" +
                "JOIN teknisi t ON b.teknisi_id = t.teknisi_id\n" +
                "WHERE b.tanggal = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDate(1, Date.valueOf(tanggal));
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            BookingServis b = new BookingServis();
            b.setIdBooking(rs.getInt("booking_id"));
            b.setTanggal(rs.getDate("tanggal").toLocalDate());
            b.setJam(rs.getTime("jam").toLocalTime());
            b.setStatus(rs.getString("status"));
            b.setNamaServis(rs.getString("nama_servis"));
            b.setNamaTeknisi(rs.getString("nama_teknisi"));
            list.add(b);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


  public List<BookingServis> getBookingByPelangganId(int pelangganId) {
    List<BookingServis> list = new ArrayList<>();
    String sql = "SELECT b.booking_id, b.tanggal, b.status, " +
                 "js.nama_servis, js.estimasi_menit, " +
                 "t.nama AS nama_teknisi " +
                 "FROM booking_servis b " +
                 "JOIN detail_servis ds ON b.booking_id = ds.booking_id " +
                 "JOIN jenis_servis js ON ds.jenis_servis_id = js.jenis_servis_id " +
                 "JOIN teknisi t ON b.teknisi_id = t.teknisi_id " +
                 "WHERE b.pelanggan_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, pelangganId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            BookingServis b = new BookingServis();
            b.setIdBooking(rs.getInt("booking_id"));
            b.setTanggalBooking(rs.getDate("tanggal"));
            b.setStatus(rs.getString("status"));

            JenisServis js = new JenisServis();
            js.setNamaServis(rs.getString("nama_servis"));
            js.setEstimasiWaktu(rs.getInt("estimasi_menit"));
            b.setJenisServis(js);

            Teknisi t = new Teknisi();
            t.setNamaTeknisi(rs.getString("nama_teknisi"));
            b.setTeknisi(t);

            list.add(b);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

  public boolean deleteBookingById(int idBooking) {
    String sql = "DELETE FROM booking_servis WHERE booking_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idBooking);
        int rows = stmt.executeUpdate();
        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}

