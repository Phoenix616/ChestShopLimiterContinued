package me.droreo002.cslimit.database;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.object.FlatFileData;
import me.droreo002.cslimit.database.object.MySQLData;
import me.droreo002.cslimit.database.object.SqlData;
import me.droreo002.oreocore.database.DatabaseType;
import org.apache.commons.lang.Validate;

public class CSLDatabase {

    @Getter
    private DatabaseType databaseType;
    @Getter
    private FlatFileData dataFlatFile;
    @Getter
    private MySQLData mySQLData;
    @Getter
    private SqlData sqlData;

    public CSLDatabase(ChestShopLimiter plugin, DatabaseType databaseType) {
        this.databaseType = databaseType;
        switch (databaseType) {
            case FLAT_FILE:
                dataFlatFile = new FlatFileData(plugin, plugin.getConfigManager().getMemory());
                break;
            case MYSQL:
                mySQLData = new MySQLData(plugin, plugin.getConfigManager().getMemory());
                break;
            case SQL:
                sqlData = new SqlData(plugin, plugin.getConfigManager().getMemory());
                break;
        }
    }

    /**
     * Get the database wrapper. Will contains method that is useful for us
     *
     * @return The database
     */
    public DatabaseWrapper getWrapper() {
        switch (databaseType) {
            case FLAT_FILE:
                Validate.notNull(dataFlatFile, "Fatal Error!. Database is null!, please contact the developer!");
                return dataFlatFile;
            case MYSQL:
                Validate.notNull(mySQLData, "Fatal Error!. Database is null!, please contact the developer!");
                return mySQLData;
            case SQL:
                Validate.notNull(sqlData, "Fatal Error!. Database is null!, please contact the developer!");
                return sqlData;
            default:
                return null;
        }
    }
}
