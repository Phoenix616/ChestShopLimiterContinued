package me.droreo002.cslimit.hook;

public interface ChestShopHook {

    String getPluginName();
    boolean process();
    void hookSuccess();
    void hookFailed();
    boolean disablePluginIfNotFound();
}
