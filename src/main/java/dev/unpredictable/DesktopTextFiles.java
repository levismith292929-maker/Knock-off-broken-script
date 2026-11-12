package dev.unpredictable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
/** Creates new prank notes in a dedicated Desktop folder. Never overwrites or deletes. */
public final class DesktopTextFiles {
    private DesktopTextFiles() {}
    public static Path createTextFile(String requestedName, String contents) throws IOException {
        if (!ConsentState.isAccepted()) throw new SecurityException("Permission not accepted.");
        String name = requestedName == null ? "" : requestedName.trim();
        if (!name.matches("[A-Za-z0-9 _.-]{1,64}\\.txt")) throw new IllegalArgumentException("Use a simple .txt filename.");
        Path desktop = Path.of(System.getProperty("user.home"), "Desktop").toAbsolutePath().normalize();
        Path folder = desktop.resolve("MinecraftPranks").normalize();
        if (!folder.startsWith(desktop)) throw new SecurityException("Invalid Desktop path.");
        Files.createDirectories(folder);
        Path target = folder.resolve(name).normalize();
        if (!target.getParent().equals(folder)) throw new SecurityException("Invalid filename.");
        return Files.writeString(target, contents == null ? "" : contents, StandardCharsets.UTF_8,
            StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
    }
}
