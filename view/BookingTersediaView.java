package view;

public class BookingTersediaView {
    private int bookingId;
    private String namaPelanggan;
    private String merkMobil;
    private String modelMobil;
    private String tanggalBooking;

    public BookingTersediaView(int bookingId, String namaPelanggan, String merkMobil, String modelMobil, String tanggalBooking) {
        this.bookingId = bookingId;
        this.namaPelanggan = namaPelanggan;
        this.merkMobil = merkMobil;
        this.modelMobil = modelMobil;
        this.tanggalBooking = tanggalBooking;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }
    public String getMerkMobil() { return merkMobil; }
    public void setMerkMobil(String merkMobil) { this.merkMobil = merkMobil; }
    public String getModelMobil() { return modelMobil; }
    public void setModelMobil(String modelMobil) { this.modelMobil = modelMobil; }
    public String getTanggalBooking() { return tanggalBooking; }
    public void setTanggalBooking(String tanggalBooking) { this.tanggalBooking = tanggalBooking; }
}