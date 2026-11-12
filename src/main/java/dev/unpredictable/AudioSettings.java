package dev.unpredictable;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Persistent preferences for optional local sound effects. */
public final class AudioSettings {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
        .resolve("knock-off-broken-script").resolve("audio-settings.properties");
    private static final Properties VALUES = new Properties();
    private static boolean loaded;
    private static boolean adultEnabled;
    private static boolean ytTwitchMode;
    private static int adultPresses;

    private AudioSettings() {}

    private static synchronized void load() {
        if (loaded) return;
        loaded = true;
        try (InputStream in = Files.newInputStream(FILE)) {
            VALUES.load(in);
            adultEnabled = Boolean.parseBoolean(VALUES.getProperty("adultAudio", "false"));
            ytTwitchMode = Boolean.parseBoolean(VALUES.getProperty("ytTwitchMode", "false"));
        } catch (IOException ignored) {
            adultEnabled = false;
            ytTwitchMode = false;
        }
    }

    private static synchronized void save() {
        try {
            Files.createDirectories(FILE.getParent());
            VALUES.setProperty("adultAudio", Boolean.toString(adultEnabled));
            VALUES.setProperty("ytTwitchMode", Boolean.toString(ytTwitchMode));
            try (OutputStream out = Files.newOutputStream(FILE)) {
                VALUES.store(out, "Knock off broken script audio settings");
            }
        } catch (IOException ignored) {}
    }

    public static synchronized boolean isAdultEnabled() { load(); return adultEnabled; }
    public static synchronized boolean isYtTwitchMode() { load(); return ytTwitchMode; }

    public static synchronized boolean toggleYtTwitchMode() {
        load();
        ytTwitchMode = !ytTwitchMode;
        save();
        return ytTwitchMode;
    }

    /** Ten presses enable adult audio; one press disables it again. */
    public static synchronized boolean pressAdultToggle() {
        load();
        if (adultEnabled) {
            adultEnabled = false;
            adultPresses = 0;
            save();
            return false;
        }
        adultPresses++;
        if (adultPresses >= 10) {
            adultEnabled = true;
            adultPresses = 0;
            save();
        }
        return adultEnabled;
    }

    public static synchronized int getAdultPresses() { load(); return adultPresses; }
}
