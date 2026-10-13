package org.example.pengajuansuratmahasiswa.controller;

import org.example.pengajuansuratmahasiswa.model.PengajuanSurat;
import org.example.pengajuansuratmahasiswa.service.PengajuanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/petugas")
public class PetugasController {

    private final PengajuanService pengajuanService;

    public PetugasController(PengajuanService pengajuanService) {
        this.pengajuanService = pengajuanService;
    }

    @GetMapping
    public String daftarPengajuan(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
                                  Model model) {
        model.addAttribute("daftarPengajuan", pengajuanService.getAllPengajuan(keyword, status));
        model.addAttribute("stats", pengajuanService.getStatistik());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activePage", "petugas-daftar");
        return "petugas/daftar-pengajuan";
    }

    @GetMapping("/periksa/{id}")
    public String periksaPengajuan(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PengajuanSurat> opt = pengajuanService.getById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pengajuan dengan ID " + id + " tidak ditemukan!");
            return "redirect:/petugas";
        }
        model.addAttribute("surat", opt.get());
        model.addAttribute("activePage", "petugas-daftar");
        return "petugas/detail-pemeriksaan";
    }

    @PostMapping("/setujui/{id}")
    public String setujuiPengajuan(@PathVariable("id") String id,
                                   @RequestParam(value = "namaPetugas", defaultValue = "Petugas Administrasi Akademik") String namaPetugas,
                                   @RequestParam(value = "catatan", defaultValue = "Disetujui. Berkas telah diverifikasi.") String catatan,
                                   RedirectAttributes redirectAttributes) {
        boolean berhasil = pengajuanService.setujuiPengajuan(id, namaPetugas, catatan);
        if (berhasil) {
            redirectAttributes.addFlashAttribute("successMessage", "Pengajuan (" + id + ") berhasil DISETUJUI.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyetujui pengajuan (" + id + ").");
        }
        return "redirect:/petugas";
    }

    @PostMapping("/tolak/{id}")
    public String tolakPengajuan(@PathVariable("id") String id,
                                 @RequestParam(value = "namaPetugas", defaultValue = "Petugas Administrasi Akademik") String namaPetugas,
                                 @RequestParam("alasanPenolakan") String alasanPenolakan,
                                 RedirectAttributes redirectAttributes) {
        if (alasanPenolakan == null || alasanPenolakan.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Alasan penolakan wajib diisi!");
            return "redirect:/petugas/periksa/" + id;
        }

        boolean berhasil = pengajuanService.tolakPengajuan(id, namaPetugas, alasanPenolakan);
        if (berhasil) {
            redirectAttributes.addFlashAttribute("warningMessage", "Pengajuan (" + id + ") telah DITOLAK dengan alasan: " + alasanPenolakan);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menolak pengajuan (" + id + ").");
        }
        return "redirect:/petugas";
    }

    @PostMapping("/hapus/{id}")
    public String hapusPengajuan(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        boolean berhasil = pengajuanService.hapusPengajuan(id);
        if (berhasil) {
            redirectAttributes.addFlashAttribute("infoMessage", "Pengajuan (" + id + ") berhasil dihapus.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus pengajuan.");
        }
        return "redirect:/petugas";
    }
}
