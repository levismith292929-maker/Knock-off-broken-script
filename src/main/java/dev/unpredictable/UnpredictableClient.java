package dev.unpredictable;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class UnpredictableClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> client.setScreen(new ConsentScreen()));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ConsentState.isAccepted()) HorrorScheduler.tick(client);
        });
    }
}