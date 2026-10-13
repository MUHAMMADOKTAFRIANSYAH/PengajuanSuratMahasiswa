package org.example.pengajuansuratmahasiswa.controller;

import org.example.pengajuansuratmahasiswa.model.PengajuanSurat;
import org.example.pengajuansuratmahasiswa.service.PengajuanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    private final PengajuanService pengajuanService;

    public MahasiswaController(PengajuanService pengajuanService) {
        this.pengajuanService = pengajuanService;
    }

    @GetMapping("/pengajuan/baru")
    public String formPengajuan(Model model) {
        model.addAttribute("pengajuan", new PengajuanSurat());
        model.addAttribute("activePage", "pengajuan-baru");
        return "mahasiswa/form-pengajuan";
    }

    @PostMapping("/pengajuan/simpan")
    public String simpanPengajuan(@ModelAttribute("pengajuan") PengajuanSurat pengajuan,
                                  RedirectAttributes redirectAttributes) {
        if (pengajuan.getNama() == null || pengajuan.getNama().trim().isEmpty() ||
            pengajuan.getNim() == null || pengajuan.getNim().trim().isEmpty() ||
            pengajuan.getJenisSurat() == null || pengajuan.getJenisSurat().trim().isEmpty() ||
            pengajuan.getKeperluan() == null || pengajuan.getKeperluan().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mohon lengkapi semua kolom wajib formulir!");
            return "redirect:/mahasiswa/pengajuan/baru";
        }

        PengajuanSurat hasil = pengajuanService.buatPengajuan(pengajuan);
        redirectAttributes.addFlashAttribute("successMessage", 
                "Pengajuan surat berhasil dikirim dengan Nomor: " + hasil.getId() + ". Silakan pantau status pengajuan Anda.");
        return "redirect:/mahasiswa/lacak?keyword=" + hasil.getNim();
    }

    @GetMapping("/lacak")
    public String lacakStatus(@RequestParam(value = "keyword", required = false) String keyword,
                              Model model) {
        List<PengajuanSurat> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = pengajuanService.getAllPengajuan(keyword, null);
        } else {
            list = pengajuanService.getAllPengajuan(null, null);
        }

        model.addAttribute("daftarPengajuan", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activePage", "lacak-status");
        return "mahasiswa/lacak-status";
    }

    @GetMapping("/surat/{id}/cetak")
    public String cetakSurat(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PengajuanSurat> opt = pengajuanService.getById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Data surat tidak ditemukan!");
            return "redirect:/mahasiswa/lacak";
        }

        PengajuanSurat surat = opt.get();
        if (surat.getStatus() != org.example.pengajuansuratmahasiswa.model.StatusPengajuan.DISETUJUI) {
            redirectAttributes.addFlashAttribute("errorMessage", "Surat belum dapat dicetak karena status belum Disetujui!");
            return "redirect:/mahasiswa/lacak?keyword=" + surat.getNim();
        }

        model.addAttribute("surat", surat);
        return "surat/cetak-surat";
    }
}
