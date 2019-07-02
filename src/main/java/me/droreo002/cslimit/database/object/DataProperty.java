package me.droreo002.cslimit.database.object;

import lombok.Getter;

public enum DataProperty {

    UUID("UUID", "Data.uuid"),
    NAME("name", "Data.playerName"),
    SHOP_CREATED("shopCreated", "Data.shopCreated"),
    MAX_SHOP("maxShop", "Data.maxShop"),
    LAST_PERMISSION("lastPermission", "Data.lastPermission"),
    LAST_RANK("lastRank", "Data.lastRank"),
    LAST_SHOP_LOCATION("lastShopLocation", "Data.lastShopLocation");

    @Getter
    private String asString;
    @Getter
    private String asPath;

    DataProperty(String asString, String asPath) {
        this.asString = asString;
        this.asPath = asPath;
    }
}
