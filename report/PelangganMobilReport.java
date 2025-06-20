package report;

public class PelangganMobilReport {
    private String namaPelanggan;
    private int jumlahMobil;

    public PelangganMobilReport(String namaPelanggan, int jumlahMobil) {
        this.namaPelanggan = namaPelanggan;
        this.jumlahMobil = jumlahMobil;
    }

    public String getNamaPelanggan() { return namaPelanggan; }
    public int getJumlahMobil() { return jumlahMobil; }
}
