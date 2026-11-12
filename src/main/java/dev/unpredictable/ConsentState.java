package dev.unpredictable;
public final class ConsentState {
    private static volatile boolean accepted;
    private ConsentState() {}
    public static boolean isAccepted() { return accepted; }
    public static void accept() { accepted = true; SettingsStore.saveAfterConsent(); }
}