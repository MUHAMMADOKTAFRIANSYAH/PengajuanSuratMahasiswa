package org.example.pengajuansuratmahasiswa.model;

public enum StatusPengajuan {
    MENUNGGU_PEMERIKSAAN("Menunggu Pemeriksaan", "badge-warning", "amber"),
    DISETUJUI("Disetujui", "badge-success", "emerald"),
    DITOLAK("Ditolak", "badge-danger", "rose");

    private final String label;
    private final String badgeClass;
    private final String color;

    StatusPengajuan(String label, String badgeClass, String color) {
        this.label = label;
        this.badgeClass = badgeClass;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public String getColor() {
        return color;
    }
}
