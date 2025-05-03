package com.example.goyimanagementbackend.eNum;

public enum VideoQuality {
    P_240("240p"),   // Chất lượng 240p
    P_360("360p"),   // Chất lượng 360p
    P_480("480p"),   // Chất lượng 480p
    P_720("720p"),   // Chất lượng 720p
    P_1080("1080p"), // Chất lượng 1080p
    P_4K("4K"),      // Chất lượng 4K
    AUTO("Auto");    // Tự động điều chỉnh chất lượng

    private final String value;

    VideoQuality(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}