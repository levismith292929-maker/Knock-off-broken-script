package dev.unpredictable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ConsentScreen extends Screen {
    private static final long WAIT_MILLIS = 30_000L;
    private static final int BG = 0xF0181818, WHITE = 0xFFFFFFFF;
    private final long openedAt = System.currentTimeMillis();
    private boolean buttonsAdded;
    private int adultPresses;
    private Button adultButton;

    public ConsentScreen() { super(Component.literal("Knock off broken script - Permission")); }
    @Override protected void init() {}

    @Override public void tick() {
        super.tick();
        if (!buttonsAdded && System.currentTimeMillis() - openedAt >= WAIT_MILLIS) {
            buttonsAdded = true;
            int w = Math.min(440, this.width - 32);
            int left = (this.width - w) / 2;
            int y = Math.max(12, this.height / 2 - 4);
            addRenderableWidget(Button.builder(ytLabel(), b -> {
                SettingsStore.toggleYtTwitchMode();
                b.setMessage(ytLabel());
            }).bounds(left, y, w, 22).build());
            adultButton = addRenderableWidget(Button.builder(adultLabel(), b -> pressAdultToggle())
                .bounds(left, y + 27, w, 22).build());
            addRenderableWidget(Button.builder(Component.literal("I agree to let this mod add/alter files on my device"),
                b -> { ConsentState.accept(); Minecraft.getInstance().setScreen(null); })
                .bounds(left, y + 58, w, 24).build());
            addRenderableWidget(Button.builder(Component.literal("I do NOT agree"),
                b -> Minecraft.getInstance().stop()).bounds(left, y + 88, w, 24).build());
        }
    }

    private void pressAdultToggle() {
        if (SettingsStore.isAdultAudioEnabled()) {
            SettingsStore.disableAdultAudio();
            adultPresses = 0;
        } else {
            adultPresses++;
            if (adultPresses >= 10) {
                SettingsStore.enableAdultAudio();
                adultPresses = 0;
            }
        }
        adultButton.setMessage(adultLabel());
    }

    private static Component ytLabel() {
        return Component.literal("YT/Twitch Mode: " + (SettingsStore.isYtTwitchMode() ? "ON" : "OFF"));
    }

    private Component adultLabel() {
        if (SettingsStore.isAdultAudioEnabled()) return Component.literal("18+ Audio: ON (press once to turn OFF)");
        return Component.literal("18+ Audio: OFF (" + adultPresses + "/10 presses to enable)");
    }

    @Override public void render(GuiGraphics g, int mx, int my, float pt) {
        g.fill(0, 0, width, height, BG);
        g.drawCenteredString(font, Component.literal("THIS MOD WILL ADD/ALTER FILES!"), width / 2, height / 2 - 94, WHITE);
        g.drawCenteredString(font, Component.literal("PROCEED WITH CAUTION"), width / 2, height / 2 - 72, WHITE);
        super.render(g, mx, my, pt);
    }

    @Override public boolean shouldCloseOnEsc() { return false; }
    @Override public boolean isPauseScreen() { return false; }
}