package org.example.pengajuansuratmahasiswa.controller;

import org.example.pengajuansuratmahasiswa.service.PengajuanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PengajuanService pengajuanService;

    public HomeController(PengajuanService pengajuanService) {
        this.pengajuanService = pengajuanService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("stats", pengajuanService.getStatistik());
        model.addAttribute("recentPengajuan", pengajuanService.getAllPengajuan(null, null));
        return "index";
    }

    @GetMapping("/bpmn")
    public String bpmnView(Model model) {
        model.addAttribute("activePage", "bpmn");
        return "bpmn";
    }
}
