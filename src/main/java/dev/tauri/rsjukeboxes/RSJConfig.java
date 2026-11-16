package dev.tauri.rsjukeboxes;

import net.neoforged.neoforge.common.ModConfigSpec;

// Options du mod (fichier config/rsjukeboxes-client.toml, cree au premier lancement)
public class RSJConfig {
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec.BooleanValue MUTE_MUSIC_WHILE_JUKEBOX_PLAYS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        MUTE_MUSIC_WHILE_JUKEBOX_PLAYS = builder
                .comment("Coupe la musique de fond du jeu pendant qu'un disque est joue dans un jukebox proche.",
                        "Stops the game's background music while a music disc is playing in a nearby jukebox.")
                .define("muteMusicWhileJukeboxPlays", true);
        CLIENT_SPEC = builder.build();
    }
}
