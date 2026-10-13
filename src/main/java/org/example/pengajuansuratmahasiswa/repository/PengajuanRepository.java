package org.example.pengajuansuratmahasiswa.repository;

import jakarta.annotation.PostConstruct;
import org.example.pengajuansuratmahasiswa.model.PengajuanSurat;
import org.example.pengajuansuratmahasiswa.model.StatusPengajuan;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Repository in-memory yang menggunakan struktur data Java ArrayList
 * sesuai dengan spesifikasi studi kasus tanpa database.
 */
@Repository
public class PengajuanRepository {

    // Menggunakan ArrayList yang dibungkus synchronizedList untuk thread-safety
    private final List<PengajuanSurat> listPengajuan = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger counter = new AtomicInteger(103);

    @PostConstruct
    public void initDataAwal() {
        // Data inisialisasi awal (dummy data) untuk mendemonstrasikan status
        PengajuanSurat surat1 = new PengajuanSurat();
        surat1.setId("SRT-2026-101");
        surat1.setNim("220101001");
        surat1.setNama("Ahmad Fauzi");
        surat1.setProgramStudi("Teknik Informatika");
        surat1.setSemester(5);
        surat1.setJenisSurat("Surat Keterangan Aktif Kuliah");
        surat1.setKeperluan("Keperluan pengajuan beasiswa Bank Indonesia");
        surat1.setTanggalPengajuan(LocalDateTime.now().minusHours(3));
        surat1.setStatus(StatusPengajuan.MENUNGGU_PEMERIKSAAN);
        listPengajuan.add(surat1);

        PengajuanSurat surat2 = new PengajuanSurat();
        surat2.setId("SRT-2026-102");
        surat2.setNim("220101002");
        surat2.setNama("Siti Rahmawati");
        surat2.setProgramStudi("Sistem Informasi");
        surat2.setSemester(7);
        surat2.setJenisSurat("Surat Pengantar Magang / PKL");
        surat2.setKeperluan("Pengantar program magang di PT Telekomunikasi Indonesia");
        surat2.setTanggalPengajuan(LocalDateTime.now().minusDays(1));
        surat2.setStatus(StatusPengajuan.DISETUJUI);
        surat2.setNamaPetugas("Budi Santoso, S.Kom (Bagian Akademik)");
        surat2.setTanggalPemeriksaan(LocalDateTime.now().minusHours(20));
        surat2.setCatatanPetugas("Berkas lengkap dan telah memenuhi syarat 100 SKS.");
        surat2.setNomorSuratResmi("045/BAA/FTI/X/2026");
        listPengajuan.add(surat2);

        PengajuanSurat surat3 = new PengajuanSurat();
        surat3.setId("SRT-2026-103");
        surat3.setNim("220101003");
        surat3.setNama("Dimas Pratama");
        surat3.setProgramStudi("Teknik Informatika");
        surat3.setSemester(3);
        surat3.setJenisSurat("Surat Keterangan Cuti Akademik");
        surat3.setKeperluan("Mengajukan cuti karena kendala kesehatan");
        surat3.setTanggalPengajuan(LocalDateTime.now().minusDays(2));
        surat3.setStatus(StatusPengajuan.DITOLAK);
        surat3.setNamaPetugas("Budi Santoso, S.Kom (Bagian Akademik)");
        surat3.setTanggalPemeriksaan(LocalDateTime.now().minusDays(1).minusHours(2));
        surat3.setCatatanPetugas("Batas waktu pengajuan cuti semester ganjil telah ditutup pada kalender akademik.");
        listPengajuan.add(surat3);
    }

    public List<PengajuanSurat> findAll() {
        synchronized (listPengajuan) {
            // Mengembalikan copy ArrayList agar aman dari concurrent modification
            return new ArrayList<>(listPengajuan);
        }
    }

    public Optional<PengajuanSurat> findById(String id) {
        synchronized (listPengajuan) {
            return listPengajuan.stream()
                    .filter(s -> s.getId().equalsIgnoreCase(id))
                    .findFirst();
        }
    }

    public List<PengajuanSurat> findByNim(String nim) {
        synchronized (listPengajuan) {
            List<PengajuanSurat> hasil = new ArrayList<>();
            for (PengajuanSurat s : listPengajuan) {
                if (s.getNim() != null && s.getNim().trim().equalsIgnoreCase(nim.trim())) {
                    hasil.add(s);
                }
            }
            return hasil;
        }
    }

    public PengajuanSurat save(PengajuanSurat surat) {
        if (surat.getId() == null || surat.getId().isEmpty()) {
            surat.setId("SRT-2026-" + counter.incrementAndGet());
        }
        if (surat.getTanggalPengajuan() == null) {
            surat.setTanggalPengajuan(LocalDateTime.now());
        }
        if (surat.getStatus() == null) {
            surat.setStatus(StatusPengajuan.MENUNGGU_PEMERIKSAAN);
        }
        synchronized (listPengajuan) {
            // Sisipkan di posisi pertama agar pengajuan terbaru berada di atas
            listPengajuan.add(0, surat);
        }
        return surat;
    }

    public boolean update(PengajuanSurat updatedSurat) {
        synchronized (listPengajuan) {
            for (int i = 0; i < listPengajuan.size(); i++) {
                if (listPengajuan.get(i).getId().equalsIgnoreCase(updatedSurat.getId())) {
                    listPengajuan.set(i, updatedSurat);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean deleteById(String id) {
        synchronized (listPengajuan) {
            return listPengajuan.removeIf(s -> s.getId().equalsIgnoreCase(id));
        }
    }

    public int countTotal() {
        return listPengajuan.size();
    }

    public long countByStatus(StatusPengajuan status) {
        synchronized (listPengajuan) {
            return listPengajuan.stream()
                    .filter(s -> s.getStatus() == status)
                    .count();
        }
    }
}
