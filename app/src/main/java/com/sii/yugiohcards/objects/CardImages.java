package com.sii.yugiohcards.objects;

public class CardImages {
    int id;
    String image_url_small;
    String image_url_cropped;
    String image_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setImage_url_cropped(String image_url_cropped) {
        this.image_url_cropped = image_url_cropped;
    }

    public void setImage_url_small(String image_url_small) {
        this.image_url_small = image_url_small;
    }

    public int getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getImage_url_cropped() {
        return image_url_cropped;
    }

    public String getImage_url_small() {
        return image_url_small;
    }
}
