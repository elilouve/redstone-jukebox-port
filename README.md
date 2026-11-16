# Redstone Jukeboxes — NeoForge 1.21.1 port

Unofficial port of [Redstone Jukeboxes](https://www.curseforge.com/minecraft/mc-mods/rsj) by **Tau'ri Dev**
([original source](https://github.com/Tau-ri-Dev/Mod-RS-Jukeboxes)) from Forge 1.20.1 to **NeoForge 1.21.1**.

Port by **elilouve**. The gameplay is unchanged from version 1.0.2.1 of the original mod:

- **Redstone Jukebox** – starts playing when it receives a redstone signal.
- **Repeating Jukebox** – loops its disc while powered; side inputs change the delay between plays.
- **Advanced Jukeboxes (tier 1/2/3)** – playlists of 5/10/15 discs with a GUI (play, stop, previous, next),
  redstone control (power toggles playback, sides skip tracks) and a redstone pulse when a disc ends.
- Optional **CC: Tweaked** integration: advanced jukeboxes are peripherals (`play`, `stop`, `next`, `prev`,
  `goTo`, `isPlaying`, `getProgress`, `getLength`, `getInventorySize`, ...).

## What changed for 1.21.1

- Forge APIs replaced by NeoForge ones (registries, networking payloads, menus, block capabilities).
- Music discs now use the 1.21 `JukeboxSong` system (length, comparator output, sound event),
  so modded discs that use the vanilla component also work.
- Data folders renamed for 1.21 (`recipe`, `loot_table`, `tags/block`).

## Building

Requires Java 21. Run `gradlew build`; the jar is created in `build/libs/`.

## License

MIT — see [LICENSE](LICENSE). Original work © 2024 Tau'ri Dev, port © 2026 elilouve.
