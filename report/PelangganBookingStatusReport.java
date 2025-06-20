package report;

public class PelangganBookingStatusReport {
    private String namaPelanggan;
    private int aktif, selesai, batal;

    public PelangganBookingStatusReport(String nama, int aktif, int selesai, int batal) {
        this.namaPelanggan = nama;
        this.aktif = aktif;
        this.selesai = selesai;
        this.batal = batal;
    }

    public String getNamaPelanggan() { return namaPelanggan; }
    public int getAktif() { return aktif; }
    public int getSelesai() { return selesai; }
    public int getBatal() { return batal; }
}
