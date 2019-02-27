package me.droreo002.cslimit.database.object;

import lombok.Getter;

public enum SqlDataName {

    UUID("UUID"),
    NAME("name"),
    SHOP_CREATED("shopCreated"),
    MAX_SHOP("maxShop"),
    LAST_PERMISSION("lastPermission"),
    LAST_RANK("lastRank"),
    LAST_SHOP_LOCATION("lastShopLocation");

    @Getter
    private String nameAsString;

    SqlDataName(String nameAsString) {
        this.nameAsString = nameAsString;
    }
}
