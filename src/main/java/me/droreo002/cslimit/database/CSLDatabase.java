package me.droreo002.cslimit.database;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.object.FlatFileData;
import me.droreo002.cslimit.database.object.SQLData;
import me.droreo002.oreocore.database.DatabaseType;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public class CSLDatabase {

    @Getter
    @NotNull
    private DatabaseType databaseType;
    @Getter
    @NotNull
    private DatabaseWrapper wrapper;

    public CSLDatabase(ChestShopLimiter plugin, DatabaseType databaseType) {
        this.databaseType = databaseType;
        switch (databaseType) {
            case FLAT_FILE:
                this.wrapper = new FlatFileData(plugin, plugin.getCslConfig());
                break;
            case MYSQL:
            case SQL:
                this.wrapper = new SQLData(plugin, plugin.getCslConfig());
                break;
            default:
                throw new NullPointerException("Invalid database type!");
        }
    }
}
