# Knock off broken script

A client-side Fabric horror/prank mod for Minecraft Java 1.21.11.

## Features
- Startup consent screen every launch, with a 30-second reading period and no countdown.
- After 30 seconds, Agree enables the session; Disagree exits Minecraft normally.
- Random original audio stingers while in a world.
- Occasional visible Windows message boxes (Windows only).
- Occasional harmless text notes in `Desktop/MinecraftPranks`.
- New text files only; never overwrites or deletes files.
- No startup persistence, background service, remote access, shell execution, or hidden installation.

All effects run locally, only after the player accepts. This mod does not remotely control another computer.

## Build
Requires JDK 21 and Gradle 9.2.1 or compatible.
Run `gradle build`. JARs are written to `build/libs/`.

## GitHub Actions
The workflow builds regular and source JARs on pushes and pull requests. Push a tag such as `v1.0.0` to attach both JARs to a GitHub Release.
