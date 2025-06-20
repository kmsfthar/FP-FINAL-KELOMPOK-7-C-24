package dao;

import dbconnection.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import view.BookingTersediaView;

public class BookingTersediaDAO {
    public List<BookingTersediaView> getAllBookingTersedia() {
        List<BookingTersediaView> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM booking_tersedia";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                BookingTersediaView booking = new BookingTersediaView(
                    rs.getInt("booking_id"),
                    rs.getString("nama"),
                    rs.getString("merek"),
                    rs.getString("model"),
                    rs.getString("tanggal")
                );
                list.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}