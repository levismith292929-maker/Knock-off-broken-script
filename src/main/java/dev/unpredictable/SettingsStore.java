package dev.unpredictable;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Persistent, user-controlled settings and local audio directories. */
public final class SettingsStore {
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("knock-off-broken-script");
    private static final Path SETTINGS_FILE = CONFIG_DIR.resolve("settings.properties");
    private static volatile boolean ytTwitchMode;
    private static volatile boolean adultAudioEnabled;

    private SettingsStore() {}

    public static synchronized void load() {
        if (!Files.isRegularFile(SETTINGS_FILE)) return;
        try (InputStream input = Files.newInputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            ytTwitchMode = Boolean.parseBoolean(properties.getProperty("ytTwitchMode", "false"));
            adultAudioEnabled = Boolean.parseBoolean(properties.getProperty("adultAudioEnabled", "false"));
        } catch (IOException | SecurityException ignored) {
            ytTwitchMode = false;
            adultAudioEnabled = false;
        }
    }

    public static boolean isYtTwitchMode() { return ytTwitchMode; }
    public static boolean isAdultAudioEnabled() { return adultAudioEnabled; }
    public static synchronized void toggleYtTwitchMode() { ytTwitchMode = !ytTwitchMode; }
    public static synchronized void enableAdultAudio() { adultAudioEnabled = true; }
    public static synchronized void disableAdultAudio() { adultAudioEnabled = false; }

    public static Path getAudioDirectory() {
        return CONFIG_DIR.resolve("audio").resolve(ytTwitchMode ? "yt-twitch" : "normal");
    }

    /** Called only after the player accepts the startup warning. */
    public static synchronized void saveAfterConsent() {
        try {
            Files.createDirectories(CONFIG_DIR.resolve("audio/normal"));
            Files.createDirectories(CONFIG_DIR.resolve("audio/yt-twitch"));
            Properties properties = new Properties();
            properties.setProperty("ytTwitchMode", Boolean.toString(ytTwitchMode));
            properties.setProperty("adultAudioEnabled", Boolean.toString(adultAudioEnabled));
            try (OutputStream output = Files.newOutputStream(SETTINGS_FILE)) {
                properties.store(output, "Knock off broken script settings");
            }
        } catch (IOException | SecurityException ignored) {
            // The mod continues with in-memory settings if the config cannot be written.
        }
    }
}