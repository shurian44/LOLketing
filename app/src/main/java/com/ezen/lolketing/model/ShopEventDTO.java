package com.ezen.lolketing.model;

public class ShopEventDTO {
    ShopDTO shopDTO = new ShopDTO();
    String image;

    public ShopEventDTO() {
    }

    public ShopEventDTO(ShopDTO shopDTO, String image) {
        this.shopDTO = shopDTO;
        this.image = image;
    }

    public ShopDTO getShopDTO() {
        return shopDTO;
    }

    public void setShopDTO(ShopDTO shopDTO) {
        this.shopDTO = shopDTO;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ShopEvent{" +
                "shopDTO=" + shopDTO +
                ", image='" + image + '\'' +
                '}';
    }
}
