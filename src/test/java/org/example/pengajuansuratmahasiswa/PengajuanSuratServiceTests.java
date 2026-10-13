package org.example.pengajuansuratmahasiswa;

import org.example.pengajuansuratmahasiswa.model.PengajuanSurat;
import org.example.pengajuansuratmahasiswa.model.StatusPengajuan;
import org.example.pengajuansuratmahasiswa.repository.PengajuanRepository;
import org.example.pengajuansuratmahasiswa.service.PengajuanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PengajuanSuratServiceTests {

    private PengajuanRepository repository;
    private PengajuanService service;

    @BeforeEach
    void setUp() {
        repository = new PengajuanRepository();
        repository.initDataAwal();
        service = new PengajuanService(repository);
    }

    @Test
    @DisplayName("Fitur 1: Mahasiswa berhasil mengisi formulir dan mengajukan surat")
    void testBuatPengajuanBaru() {
        PengajuanSurat baru = new PengajuanSurat();
        baru.setNama("Bambang Kurniawan");
        baru.setNim("220101999");
        baru.setProgramStudi("Teknik Informatika");
        baru.setSemester(6);
        baru.setJenisSurat("Surat Rekomendasi Beasiswa");
        baru.setKeperluan("Untuk mendaftar beasiswa Djarum Plus");

        PengajuanSurat hasil = service.buatPengajuan(baru);

        assertNotNull(hasil.getId(), "ID pengajuan harus otomatis digenerate");
        assertEquals(StatusPengajuan.MENUNGGU_PEMERIKSAAN, hasil.getStatus(), "Status awal harus Menunggu Pemeriksaan");
        assertEquals("Bambang Kurniawan", hasil.getNama());
        assertEquals("220101999", hasil.getNim());
        assertNotNull(hasil.getTanggalPengajuan());
    }

    @Test
    @DisplayName("Fitur 2: Sistem menampilkan daftar pengajuan yang masuk beserta statusnya")
    void testDaftarPengajuanDanFilter() {
        List<PengajuanSurat> semua = service.getAllPengajuan(null, "ALL");
        assertFalse(semua.isEmpty(), "Daftar pengajuan tidak boleh kosong");

        List<PengajuanSurat> menunggu = service.getAllPengajuan(null, "MENUNGGU_PEMERIKSAAN");
        for (PengajuanSurat s : menunggu) {
            assertEquals(StatusPengajuan.MENUNGGU_PEMERIKSAAN, s.getStatus());
        }

        List<PengajuanSurat> cariNim = service.getByNim("220101001");
        assertFalse(cariNim.isEmpty());
        assertEquals("220101001", cariNim.get(0).getNim());
    }

    @Test
    @DisplayName("Fitur 3: Petugas dapat menyetujui pengajuan surat")
    void testPetugasMenyetujuiPengajuan() {
        // Cari pengajuan dengan status menunggu
        List<PengajuanSurat> list = service.getAllPengajuan(null, "MENUNGGU_PEMERIKSAAN");
        assertFalse(list.isEmpty());
        String id = list.get(0).getId();

        boolean hasil = service.setujuiPengajuan(id, "Ibu Endang (Bagian Akademik)", "Semua syarat lengkap dan valid");
        assertTrue(hasil);

        Optional<PengajuanSurat> updated = service.getById(id);
        assertTrue(updated.isPresent());
        assertEquals(StatusPengajuan.DISETUJUI, updated.get().getStatus());
        assertNotNull(updated.get().getNomorSuratResmi(), "Nomor surat resmi harus diterbitkan");
        assertEquals("Ibu Endang (Bagian Akademik)", updated.get().getNamaPetugas());
    }

    @Test
    @DisplayName("Fitur 3: Petugas dapat menolak pengajuan dan memberikan alasan jika ditolak")
    void testPetugasMenolakPengajuanDenganAlasan() {
        List<PengajuanSurat> list = service.getAllPengajuan(null, "MENUNGGU_PEMERIKSAAN");
        assertFalse(list.isEmpty());
        String id = list.get(0).getId();

        String alasanTolak = "IPK belum memenuhi kriteria minimum untuk pengajuan surat rekomendasi.";
        boolean hasil = service.tolakPengajuan(id, "Budi Santoso, S.Kom", alasanTolak);
        assertTrue(hasil);

        Optional<PengajuanSurat> updated = service.getById(id);
        assertTrue(updated.isPresent());
        assertEquals(StatusPengajuan.DITOLAK, updated.get().getStatus());
        assertEquals(alasanTolak, updated.get().getCatatanPetugas(), "Alasan penolakan harus tersimpan");
    }

    @Test
    @DisplayName("Fitur 4: Sistem menampilkan hasil pengajuan (Menunggu, Disetujui, Ditolak)")
    void testHasilPengajuanStatus() {
        var stats = service.getStatistik();
        assertTrue(((Number) stats.get("total")).intValue() >= 3);
        assertTrue(((Number) stats.get("menunggu")).longValue() >= 1);
        assertTrue(((Number) stats.get("disetujui")).longValue() >= 1);
        assertTrue(((Number) stats.get("ditolak")).longValue() >= 1);
    }
}
