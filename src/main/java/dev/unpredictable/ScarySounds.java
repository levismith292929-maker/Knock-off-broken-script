package dev.unpredictable;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.concurrent.ThreadLocalRandom;

/** Plays a quiet, local vanilla Minecraft horror sound effect. */
public final class ScarySounds {
    private ScarySounds() {}

    public static void playRandom(Minecraft client) {
        if (client.player == null || client.level == null) return;
        float pitch = 0.92F + ThreadLocalRandom.current().nextFloat() * 0.16F;
        client.level.playLocalSound(
            client.player.getX(),
            client.player.getY(),
            client.player.getZ(),
            SoundEvents.WARDEN_HEARTBEAT,
            SoundSource.AMBIENT,
            0.65F,
            pitch,
            false
        );
    }
}