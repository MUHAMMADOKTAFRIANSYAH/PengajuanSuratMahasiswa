package org.example.pengajuansuratmahasiswa.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PengajuanSurat {
    private String id;
    private String nim;
    private String nama;
    private String programStudi;
    private Integer semester;
    private String jenisSurat;
    private String keperluan; // alasan pengajuan
    private LocalDateTime tanggalPengajuan;
    private StatusPengajuan status;
    private String catatanPetugas; // alasan jika ditolak atau catatan
    private String namaPetugas;
    private LocalDateTime tanggalPemeriksaan;
    private String nomorSuratResmi;

    public PengajuanSurat() {
        this.tanggalPengajuan = LocalDateTime.now();
        this.status = StatusPengajuan.MENUNGGU_PEMERIKSAAN;
    }

    public PengajuanSurat(String id, String nim, String nama, String programStudi, Integer semester,
                          String jenisSurat, String keperluan) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.programStudi = programStudi;
        this.semester = semester;
        this.jenisSurat = jenisSurat;
        this.keperluan = keperluan;
        this.tanggalPengajuan = LocalDateTime.now();
        this.status = StatusPengajuan.MENUNGGU_PEMERIKSAAN;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getProgramStudi() {
        return programStudi;
    }

    public void setProgramStudi(String programStudi) {
        this.programStudi = programStudi;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getJenisSurat() {
        return jenisSurat;
    }

    public void setJenisSurat(String jenisSurat) {
        this.jenisSurat = jenisSurat;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public LocalDateTime getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    public void setTanggalPengajuan(LocalDateTime tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }

    public StatusPengajuan getStatus() {
        return status;
    }

    public void setStatus(StatusPengajuan status) {
        this.status = status;
    }

    public String getCatatanPetugas() {
        return catatanPetugas;
    }

    public void setCatatanPetugas(String catatanPetugas) {
        this.catatanPetugas = catatanPetugas;
    }

    public String getNamaPetugas() {
        return namaPetugas;
    }

    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
    }

    public LocalDateTime getTanggalPemeriksaan() {
        return tanggalPemeriksaan;
    }

    public void setTanggalPemeriksaan(LocalDateTime tanggalPemeriksaan) {
        this.tanggalPemeriksaan = tanggalPemeriksaan;
    }

    public String getNomorSuratResmi() {
        return nomorSuratResmi;
    }

    public void setNomorSuratResmi(String nomorSuratResmi) {
        this.nomorSuratResmi = nomorSuratResmi;
    }

    public String getFormattedTanggalPengajuan() {
        if (tanggalPengajuan == null) return "-";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return tanggalPengajuan.format(formatter);
    }

    public String getFormattedTanggalPemeriksaan() {
        if (tanggalPemeriksaan == null) return "-";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return tanggalPemeriksaan.format(formatter);
    }
}
