# Knock off broken script

A client-side Fabric horror/prank mod for Minecraft Java 1.21.11.

## Consent and effects
- Shows a startup consent screen every launch. Buttons and settings appear after a 30-second reading period; there is no countdown.
- Nothing is written to disk until you accept. Choosing **I do NOT agree** exits Minecraft.
- After consent, occasional Windows message boxes and harmless text notes may appear. Notes are created in `Desktop/MinecraftPranks`; existing files are never overwritten or deleted.
- Random audio is played only from local files you place in the mod's config audio folders.
- No startup persistence, background service, remote access, shell execution, or hidden installation.

## Startup settings
- **YT/Twitch Mode** selects the separate `yt-twitch` audio folder. Put only music you have permission to use in streams/recordings there. It does not use the normal folder.
- **18+ Audio** is OFF by default. It takes 10 presses to enable and one press to disable. The track named `WIJMSOAIJOMCAIHAHCSOMDTAGIUAJOTB` is excluded unless this setting is enabled.
- Settings are saved in `.minecraft/config/knock-off-broken-script/settings.properties` after you accept.

## Add your audio
After accepting once, these folders are created:

- `.minecraft/config/knock-off-broken-script/audio/normal/`
- `.minecraft/config/knock-off-broken-script/audio/yt-twitch/`

Put MP3 files in `normal` for regular mode. Put stream-safe / recording-safe MP3 files in `yt-twitch` for YT/Twitch Mode. The mod does not download or bundle music. The 18+ track should keep `WIJMSOAIJOMCAIHAHCSOMDTAGIUAJOTB` in its filename so the adult-audio safeguard can recognize it. You are responsible for having the necessary rights to play any audio in public streams or recordings; another fan project using a song does not prove that you have permission.

The MP3 decoder is MP3SPI 1.9.5.4, distributed under LGPL 2.1. See <https://central.sonatype.com/artifact/com.googlecode.soundlibs/mp3spi/1.9.5.4> for its artifact and license information.

## Build
Requires JDK 21 and Gradle 9.2.1 or compatible. Run `gradle build`. JARs are written to `build/libs/`.

## GitHub Actions
The workflow builds regular and source JARs on pushes and pull requests. Push a tag such as `v1.0.0` to attach both JARs to a GitHub Release.