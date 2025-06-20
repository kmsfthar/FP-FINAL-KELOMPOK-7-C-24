package report;

public class TeknisiBookingReport {
    private String namaTeknisi;
    private int jumlahBooking;

    public TeknisiBookingReport(String namaTeknisi, int jumlahBooking) {
        this.namaTeknisi = namaTeknisi;
        this.jumlahBooking = jumlahBooking;
    }

    public String getNamaTeknisi() { return namaTeknisi; }
    public int getJumlahBooking() { return jumlahBooking; }
}
