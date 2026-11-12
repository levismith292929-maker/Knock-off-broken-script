package dev.unpredictable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/** Plays one of the user's locally supplied MP3 sound effects. No vanilla fallback. */
public final class ScarySounds {
    private static final AtomicBoolean PLAYING = new AtomicBoolean(false);
    private static final Path AUDIO_DIR = FabricLoader.getInstance().getConfigDir()
        .resolve("knock-off-broken-script").resolve("audio");

    private ScarySounds() {}

    public static void playRandom(Minecraft client) {
        if (client.player == null || client.level == null || PLAYING.get()) return;

        List<Path> candidates = findSoundFiles();
        if (candidates.isEmpty()) return;

        List<Path> allowed = new ArrayList<>();
        for (Path path : candidates) {
            String stem = normalize(path.getFileName().toString());
            boolean adultTrack = stem.startsWith("wijmsoaijomc");
            if (adultTrack && (!AudioSettings.isAdultEnabled() || AudioSettings.isYtTwitchMode())) {
                continue;
            }
            allowed.add(path);
        }
        if (allowed.isEmpty() || !PLAYING.compareAndSet(false, true)) return;

        Path selected = allowed.get(ThreadLocalRandom.current().nextInt(allowed.size()));
        Thread playback = new Thread(() -> playFile(selected), "KnockOffBrokenScript-Audio");
        playback.setDaemon(true);
        playback.start();
    }

    private static List<Path> findSoundFiles() {
        try {
            Files.createDirectories(AUDIO_DIR);
            try (var files = Files.list(AUDIO_DIR)) {
                return files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".mp3"))
                    .filter(path -> {
                        String stem = normalize(path.getFileName().toString());
                        return stem.startsWith("jarona")
                            || stem.startsWith("hereicomesanfrancisco")
                            || stem.startsWith("wijmsoaijomc");
                    })
                    .toList();
            }
        } catch (Exception ignored) {
            return List.of();
        }
    }

    private static String normalize(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        int dot = lower.lastIndexOf('.');
        if (dot >= 0) lower = lower.substring(0, dot);
        return lower.replaceAll("[^a-z0-9]", "");
    }

    private static void playFile(Path file) {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(Files.newInputStream(file)))) {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP || event.getType() == LineEvent.Type.CLOSE) {
                    if (clip.isOpen()) clip.close();
                    PLAYING.set(false);
                }
            });
            clip.open(stream);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(Math.max(gain.getMinimum(), Math.min(-8.0F, gain.getMaximum())));
            }
            clip.start();
        } catch (Exception ignored) {
            PLAYING.set(false);
        }
    }
}
