package com.example.brokenmirror.data;

public class ImageDto {
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
