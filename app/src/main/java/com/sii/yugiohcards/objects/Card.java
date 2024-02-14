package com.sii.yugiohcards.objects;

import java.util.List;

public class Card {

    int id;
    String name;
    int atk;
    int def;
    int level;
    String type;
    String frameType;
    String desc;
    String race;
    String archetype;
    String ygoprodeck_url;
    List<CardSet> card_sets;
    List<CardImages> card_images;
    List<CardPrices> card_prices;
    Banlist banlist;

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getLevel() {
        return level;
    }

    public void setBanlist(Banlist banlist) {
        this.banlist = banlist;
    }

    public Banlist getBanlist() {
        return banlist;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCard_images(List<CardImages> card_images) {
        this.card_images = card_images;
    }

    public void setCard_sets(List<CardSet> card_sets) {
        this.card_sets = card_sets;
    }

    public void setCard_prices(List<CardPrices> card_prices) {
        this.card_prices = card_prices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYgoprodeck_url(String ygoprodeck_url) {
        this.ygoprodeck_url = ygoprodeck_url;
    }

    public int getId() {
        return id;
    }

    public List<CardSet> getCard_sets() {
        return card_sets;
    }

    public List<CardImages> getCard_images() {
        return card_images;
    }

    public String getArchetype() {
        return archetype;
    }

    public String getDesc() {
        return desc;
    }

    public String getFrameType() {
        return frameType;
    }

    public List<CardPrices> getCard_prices() {
        return card_prices;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public String getType() {
        return type;
    }

    public String getYgoprodeck_url() {
        return ygoprodeck_url;
    }
}
