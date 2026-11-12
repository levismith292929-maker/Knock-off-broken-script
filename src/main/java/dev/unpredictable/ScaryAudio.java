package dev.unpredictable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/** Plays only audio files the user has placed in the selected local config folder. */
public final class ScaryAudio {
    private static final String ADULT_TRACK_MARKER = "wijmsoaijomcaiha hcsomdtagiuajotb".replace(" ", "");
    private static volatile Clip activeClip;

    private ScaryAudio() {}

    public static void playRandom() {
        Clip current = activeClip;
        if (current != null && current.isRunning()) return;

        Path directory = SettingsStore.getAudioDirectory();
        List<Path> tracks = new ArrayList<>();
        try {
            if (!Files.isDirectory(directory)) return;
            try (var files = Files.list(directory)) {
                files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".mp3"))
                    .filter(path -> {
                        String normalized = path.getFileName().toString().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                        boolean adultTrack = normalized.contains(ADULT_TRACK_MARKER);
                        return !adultTrack || (!SettingsStore.isYtTwitchMode() && SettingsStore.isAdultAudioEnabled());
                    })
                    .forEach(tracks::add);
            }
        } catch (Exception ignored) { return; }
        if (tracks.isEmpty()) return;

        Path selected = tracks.get(ThreadLocalRandom.current().nextInt(tracks.size()));
        Thread thread = new Thread(() -> playFile(selected), "Knock-off-broken-script-audio");
        thread.setDaemon(true);
        thread.start();
    }

    private static synchronized void playFile(Path file) {
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), -8.0f)));
            }
            activeClip = clip;
            clip.addLineListener(event -> {
                if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close();
                    if (activeClip == clip) activeClip = null;
                }
            });
            clip.start();
        } catch (Exception ignored) {
            activeClip = null;
        }
    }
}