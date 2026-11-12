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
    private Button streamButton;
    private Button adultButton;

    public ConsentScreen() { super(Component.literal("Knock off broken script - Permission")); }
    @Override protected void init() {}

    @Override public void tick() {
        super.tick();
        if (!buttonsAdded && System.currentTimeMillis() - openedAt >= WAIT_MILLIS) {
            buttonsAdded = true;
            int w = Math.min(440, this.width - 32);
            int left = (this.width - w) / 2;
            int y = Math.max(12, this.height / 2 - 42);

            streamButton = Button.builder(streamLabel(), b -> {
                AudioSettings.toggleYtTwitchMode();
                b.setMessage(streamLabel());
            }).bounds(left, y, w, 24).build();
            addRenderableWidget(streamButton);

            adultButton = Button.builder(adultLabel(), b -> {
                AudioSettings.pressAdultToggle();
                b.setMessage(adultLabel());
            }).bounds(left, y + 30, w, 24).build();
            addRenderableWidget(adultButton);

            addRenderableWidget(Button.builder(Component.literal("I agree to let this mod add/alter files on my device"),
                b -> { ConsentState.accept(); Minecraft.getInstance().setScreen(null); })
                .bounds(left, y + 66, w, 24).build());
            addRenderableWidget(Button.builder(Component.literal("I do NOT agree"),
                b -> Minecraft.getInstance().stop()).bounds(left, y + 96, w, 24).build());
        }
    }

    private static Component streamLabel() {
        return Component.literal("YT/Twitch Mode: " + (AudioSettings.isYtTwitchMode() ? "ON" : "OFF"));
    }

    private static Component adultLabel() {
        if (AudioSettings.isAdultEnabled()) return Component.literal("18+ Audio: ON (press once to turn OFF)");
        int presses = AudioSettings.getAdultPresses();
        return Component.literal("18+ Audio: OFF (" + presses + "/10 presses to enable)");
    }

    @Override public void render(GuiGraphics g, int mx, int my, float pt) {
        g.fill(0, 0, width, height, BG);
        g.drawCenteredString(font, Component.literal("THIS MOD WILL ADD/ALTER FILES!"), width / 2, height / 2 - 124, WHITE);
        g.drawCenteredString(font, Component.literal("PROCEED WITH CAUTION"), width / 2, height / 2 - 102, WHITE);
        super.render(g, mx, my, pt);
    }

    @Override public boolean shouldCloseOnEsc() { return false; }
    @Override public boolean isPauseScreen() { return false; }
}
