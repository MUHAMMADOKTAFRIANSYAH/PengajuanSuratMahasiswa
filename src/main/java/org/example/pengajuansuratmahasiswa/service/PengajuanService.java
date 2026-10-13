package org.example.pengajuansuratmahasiswa.service;

import org.example.pengajuansuratmahasiswa.model.PengajuanSurat;
import org.example.pengajuansuratmahasiswa.model.StatusPengajuan;
import org.example.pengajuansuratmahasiswa.repository.PengajuanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PengajuanService {

    private final PengajuanRepository repository;

    public PengajuanService(PengajuanRepository repository) {
        this.repository = repository;
    }

    public List<PengajuanSurat> getAllPengajuan(String keyword, String statusStr) {
        List<PengajuanSurat> list = repository.findAll();

        if (statusStr != null && !statusStr.isBlank() && !statusStr.equalsIgnoreCase("ALL")) {
            try {
                StatusPengajuan filterStatus = StatusPengajuan.valueOf(statusStr.toUpperCase());
                list = list.stream()
                        .filter(s -> s.getStatus() == filterStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (keyword != null && !keyword.isBlank()) {
            String lowerKw = keyword.toLowerCase().trim();
            list = list.stream()
                    .filter(s -> (s.getNama() != null && s.getNama().toLowerCase().contains(lowerKw))
                            || (s.getNim() != null && s.getNim().toLowerCase().contains(lowerKw))
                            || (s.getId() != null && s.getId().toLowerCase().contains(lowerKw))
                            || (s.getJenisSurat() != null && s.getJenisSurat().toLowerCase().contains(lowerKw)))
                    .collect(Collectors.toList());
        }

        return list;
    }

    public Optional<PengajuanSurat> getById(String id) {
        return repository.findById(id);
    }

    public List<PengajuanSurat> getByNim(String nim) {
        return repository.findByNim(nim);
    }

    public PengajuanSurat buatPengajuan(PengajuanSurat surat) {
        surat.setStatus(StatusPengajuan.MENUNGGU_PEMERIKSAAN);
        surat.setTanggalPengajuan(LocalDateTime.now());
        surat.setCatatanPetugas(null);
        surat.setNamaPetugas(null);
        surat.setTanggalPemeriksaan(null);
        return repository.save(surat);
    }

    public boolean setujuiPengajuan(String id, String namaPetugas, String catatan) {
        Optional<PengajuanSurat> opt = repository.findById(id);
        if (opt.isPresent()) {
            PengajuanSurat surat = opt.get();
            surat.setStatus(StatusPengajuan.DISETUJUI);
            surat.setNamaPetugas(namaPetugas != null && !namaPetugas.isBlank() ? namaPetugas : "Petugas Administrasi Akademik");
            surat.setCatatanPetugas(catatan != null && !catatan.isBlank() ? catatan : "Disetujui. Surat keterangan siap digunakan.");
            surat.setTanggalPemeriksaan(LocalDateTime.now());
            // Format nomor surat resmi otomatis
            String runningNumber = surat.getId().replace("SRT-", "");
            surat.setNomorSuratResmi("B/" + runningNumber + "/BAA/KM/" + LocalDateTime.now().getYear());
            return repository.update(surat);
        }
        return false;
    }

    public boolean tolakPengajuan(String id, String namaPetugas, String alasanPenolakan) {
        Optional<PengajuanSurat> opt = repository.findById(id);
        if (opt.isPresent()) {
            PengajuanSurat surat = opt.get();
            surat.setStatus(StatusPengajuan.DITOLAK);
            surat.setNamaPetugas(namaPetugas != null && !namaPetugas.isBlank() ? namaPetugas : "Petugas Administrasi Akademik");
            surat.setCatatanPetugas(alasanPenolakan != null && !alasanPenolakan.isBlank() ? alasanPenolakan : "Tidak memenuhi persyaratan administrasi.");
            surat.setTanggalPemeriksaan(LocalDateTime.now());
            surat.setNomorSuratResmi(null);
            return repository.update(surat);
        }
        return false;
    }

    public boolean hapusPengajuan(String id) {
        return repository.deleteById(id);
    }

    public Map<String, Object> getStatistik() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", repository.countTotal());
        stats.put("menunggu", repository.countByStatus(StatusPengajuan.MENUNGGU_PEMERIKSAAN));
        stats.put("disetujui", repository.countByStatus(StatusPengajuan.DISETUJUI));
        stats.put("ditolak", repository.countByStatus(StatusPengajuan.DITOLAK));
        return stats;
    }
}
