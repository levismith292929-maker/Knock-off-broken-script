package dev.unpredictable;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
/** Random local-only events; runs only after consent and while in a world. */
public final class HorrorScheduler {
    private static long nextSound, nextPopup, nextFile, lastPopup;
    private static boolean initialized;
    private static final String[] TITLES = {"A message for you", "Did you hear that?", "Minecraft notice", "..."};
    private static final String[] MESSAGES = {"Something moved when you weren't looking.", "You should probably check behind you.", "That sound wasn't part of the game.", "Don't worry. This is only a game. Probably.", "Someone left the lights on.", "You are not alone in this world."};
    private static final String[] NOTES = {"I heard you come back.", "This world remembers.", "You left something behind.", "Do not look for the sound.", "Still here."};
    private HorrorScheduler() {}
    public static void tick(Minecraft client) {
        if (!ConsentState.isAccepted() || client.player == null || client.level == null) return;
        long now = System.currentTimeMillis();
        if (!initialized) { nextSound=now+random(45_000,120_000); nextPopup=now+random(150_000,300_000); nextFile=now+random(360_000,720_000); initialized=true; }
        if (now>=nextSound) { ScaryAudio.playRandom(); nextSound=now+random(75_000,210_000); }
        if (now>=nextPopup && now-lastPopup>=240_000) {
            int i=ThreadLocalRandom.current().nextInt(MESSAGES.length);
            WindowsPopups.show(TITLES[ThreadLocalRandom.current().nextInt(TITLES.length)], MESSAGES[i]);
            lastPopup=now; nextPopup=now+random(240_000,600_000);
        }
        if (now>=nextFile) { createNote(); nextFile=now+random(600_000,1_200_000); }
    }
    private static void createNote() {
        String stamp=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String name="Note-"+stamp+".txt";
        String body=NOTES[ThreadLocalRandom.current().nextInt(NOTES.length)]+System.lineSeparator()+System.lineSeparator()+"Created by Knock off broken script after you accepted its startup warning.";
        try { DesktopTextFiles.createTextFile(name,body); } catch (IOException|SecurityException|IllegalArgumentException ignored) {}
    }
    private static long random(long min,long max) { return ThreadLocalRandom.current().nextLong(min,max+1); }
}
