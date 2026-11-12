# Knock off broken script

A client-side Fabric horror/prank mod for Minecraft Java 1.21.11.

## Consent and effects
- Shows a startup consent screen every launch. The buttons appear after a 30-second reading period; there is no countdown or helper text.
- Nothing is written to disk until you accept. Choosing **I do NOT agree** exits Minecraft.
- After consent, occasional Windows message boxes and harmless text notes may appear. Notes are created in `Desktop/MinecraftPranks`; existing files are never overwritten or deleted.
- Random creepy Minecraft sound effects play occasionally. The current sound is the vanilla Warden heartbeat event (`minecraft:entity.warden.heartbeat`), played locally at the player's position.
- This mod does not include Deltarune music, other external music, MP3 playback, or user-supplied music folders.
- No startup persistence, background service, remote access, shell execution, or hidden installation.

## Build
Requires JDK 21 and Gradle 9.5.0 or compatible. Run `gradle build`. JARs are written to `build/libs/`.

## GitHub Actions
The workflow builds regular and source JARs on pushes and pull requests. Push a tag such as `v1.0.0` to attach both JARs to a GitHub Release.