package dev.unpredictable;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
public final class ScaryAudio {
    private static final String[] SOUNDS={"/assets/unpredictable/sounds/whisper.wav","/assets/unpredictable/sounds/knock.wav","/assets/unpredictable/sounds/static.wav"};
    private ScaryAudio() {}
    public static void playRandom() {
        String path=SOUNDS[(int)(Math.random()*SOUNDS.length)];
        Thread t=new Thread(() -> {
            try (InputStream raw=ScaryAudio.class.getResourceAsStream(path)) {
                if(raw==null)return;
                try(AudioInputStream audio=AudioSystem.getAudioInputStream(new BufferedInputStream(raw))) {
                    Clip clip=AudioSystem.getClip(); clip.open(audio); clip.start();
                    clip.addLineListener(e -> { if(e.getType()==javax.sound.sampled.LineEvent.Type.STOP)clip.close(); });
                }
            } catch(Exception ignored) {}
        },"Knock-off-broken-script-audio");
        t.setDaemon(true); t.start();
    }
}
