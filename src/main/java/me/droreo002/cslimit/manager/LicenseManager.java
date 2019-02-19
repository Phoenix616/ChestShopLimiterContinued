package me.droreo002.cslimit.manager;

import me.droreo002.cslimit.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class LicenseManager {

    private static final String USER_INFORMATION = "%%__USER__%%";

    public static String getBuyerInformation() {
        if (USER_INFORMATION.startsWith("%%")) {
            return StringUtils.color("&7[ &cWarning &7] &fUnregistered user!. Cracked version?!");
        }
        try {
            final URLConnection openConnection = new URL("https://www.spigotmc.org/members/" + USER_INFORMATION + "/").openConnection();
            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String buyerName = sb.toString().split("<title>")[1].split("</title>")[0].split(" | ")[0];
            return StringUtils.color("&bThis instance of &cChestShopLimiter &6is registered to &7(&e" + "https://www.spigotmc.org/members/" + USER_INFORMATION + "&7) &ba.k.a &7(&e" + buyerName + "&7)");
        }
        catch (IOException ex) {
            return StringUtils.color("&7[ &cWarning &7] &fCannot contact &bspigotmc.org! &f. Is the website down?!");
        }
    }
}
