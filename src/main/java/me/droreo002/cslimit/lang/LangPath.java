package me.droreo002.cslimit.lang;

import lombok.Getter;

public enum LangPath {

    /*
    Normal values
     */
    NORMAL_CONFIG_RELOADED("Normal.config-reloaded"),
    NORMAL_LANG_RELOADED("Normal.lang-reloaded"),
    NORMAL_NO_PERMISSION("Normal.no-permission"),
    NORMAL_TOO_MUCH_ARGS("Normal.too-much-args"),
    NORMAL_SHOP_CREATED("Normal.shop-created"),
    NORMAL_SHOP_REMOVED("Normal.shop-removed"),
    NORMAL_CONSOLE_ONLY("Normal.console-only"),
    NORMAL_SHOP_CREATED_RESET("Normal.shop-created-reset"),
    NORMAL_SHOP_CREATED_RESET_OTHER("Normal.shop-created-reset-other"),
    NORMAL_DATA_SAVE_SUCCESS("Normal.data-save-success"),

    /*
    Misc values
     */
    MISC_TELEPORT_BUTTON_TEXT("Misc.teleport-button-text"),
    MISC_TELEPORT_BUTTON_HOVER_TEXT("Misc.teleport-button-hover-text"),

    /*
    Error values
     */
    ERROR_USAGE_COMMAND_CHECK("Error.usage.command-check"),
    ERROR_USAGE_COMMAND_UNKNOWN("Error.usage.command-unknown"),
    ERROR_USAGE_COMMAND_RELOAD("Error.usage.command-reload"),
    ERROR_USAGE_COMMAND_RESET("Error.usage.command-reset"),
    ERROR_USAGE_COMMAND_SAVE_DATA("Error.usage.command-save-data"),
    ERROR_USAGE_COMMAND_STATUS("Error.usage.command-status"),
    ERROR_CANNOT_LOAD_DATA("Error.cannot-load-data"),
    ERROR_PLAYER_NEVER_PLAYED("Error.player-never-played"),
    ERROR_LIMIT_REACHED("Error.limit-reached"),
    ERROR_INVALID_RELOAD_TYPE("Error.invalid-reload-type"),

    /*
    List values
     */
    LIST_HELP_MESSAGE("List.help-message"),
    LIST_CHECK_MESSAGE("List.check-message"),
    LIST_PLAYER_STATUS_MESSAGE("List.player-status-message")
    ;
    
    @Getter
    private String path;

    LangPath(String path) {
        this.path = path;
    }
}
