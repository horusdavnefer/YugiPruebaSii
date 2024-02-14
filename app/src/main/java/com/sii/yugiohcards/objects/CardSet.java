package com.sii.yugiohcards.objects;

public class CardSet {

    String set_name;
    String set_code;
    String set_rarity;
    String set_rarity_code;
    String set_price;

    public void setSet_code(String set_code) {
        this.set_code = set_code;
    }

    public void setSet_name(String set_name) {
        this.set_name = set_name;
    }

    public void setSet_price(String set_price) {
        this.set_price = set_price;
    }

    public void setSet_rarity(String set_rarity) {
        this.set_rarity = set_rarity;
    }

    public void setSet_rarity_code(String set_rarity_code) {
        this.set_rarity_code = set_rarity_code;
    }

    public String getSet_code() {
        return set_code;
    }

    public String getSet_name() {
        return set_name;
    }

    public String getSet_price() {
        return set_price;
    }

    public String getSet_rarity() {
        return set_rarity;
    }

    public String getSet_rarity_code() {
        return set_rarity_code;
    }


}
