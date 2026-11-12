# Knock off broken script

A client-side Fabric horror/prank mod for Minecraft Java 1.21.11.

## Consent and effects
- Shows a startup consent screen every launch. The buttons appear after a 30-second reading period.
- Nothing is written to disk until you accept. Choosing **I do NOT agree** exits Minecraft.
- After consent, occasional Windows message boxes and harmless text notes may appear. Notes are created in `Desktop/MinecraftPranks`; existing files are never overwritten or deleted.
- Random **custom local MP3 sound effects** play occasionally. The mod does not use vanilla Minecraft sounds and does not bundle external audio.
- Place your own MP3s in `.minecraft/config/knock-off-broken-script/audio/`. Supported filename prefixes are `Jarona`, `Here I Come San Francisco`, and `WIJMSOAIJOMC`. The mod picks randomly from files it finds there. If the folder is empty, it simply plays nothing.
- YT/Twitch Mode is a saved preference that switches playback to `.minecraft/config/knock-off-broken-script/audio/yt-twitch/`. Put only audio you have permission to use in recordings or streams in that separate folder. If it is empty, no sound plays.
- The 18+ track is disabled by default; press its setting 10 times to enable it, then once to disable it. It is always blocked in YT/Twitch Mode.
- No startup persistence, background service, remote access, shell execution, or hidden installation.

## Build
Requires JDK 21 and Gradle 9.5.0 or compatible. Run `gradle build`. JARs are written to `build/libs/`.

## GitHub Actions
The workflow builds regular and source JARs on pushes and pull requests. Push a tag such as `v1.0.0` to attach both JARs to a GitHub Release.
