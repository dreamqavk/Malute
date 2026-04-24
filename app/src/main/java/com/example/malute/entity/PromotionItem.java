package com.example.malute.entity;

public class PromotionItem {
    private String id;
    private String collectionId;
    private String imageName;
    private String title;
    private String subtitle;

    public PromotionItem(String id, String collectionId, String imageName, String title, String subtitle) {
        this.id = id;
        this.collectionId = collectionId;
        this.imageName = imageName;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getId() {
        return id;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getImageName() {
        return imageName;
    }

    public String getTitle() {
        return title != null ? title : "";
    }

    public String getSubtitle() {
        return subtitle != null ? subtitle : "";
    }
}