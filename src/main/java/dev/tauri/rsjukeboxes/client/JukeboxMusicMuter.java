package dev.tauri.rsjukeboxes.client;

import dev.tauri.rsjukeboxes.RSJConfig;
import dev.tauri.rsjukeboxes.RSJukeboxes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Cote client uniquement : coupe la musique du jeu pendant qu'un disque joue dans un jukebox.
 * Marche pour tous les jukebox (ceux du mod, celui de Minecraft et ceux d'autres mods),
 * car tous les disques utilisent la categorie de son "Jukebox/Blocs musicaux" (RECORDS).
 */
@EventBusSubscriber(modid = RSJukeboxes.MOD_ID, value = Dist.CLIENT)
public class JukeboxMusicMuter {
    // Disques en cours -> nombre de ticks pendant lesquels on les garde meme s'ils ne sont pas encore "actifs"
    private static final Map<SoundInstance, Integer> PLAYING_RECORDS = new HashMap<>();
    private static final int START_GRACE_TICKS = 5;

    private static boolean isEnabled() {
        return RSJConfig.MUTE_MUSIC_WHILE_JUKEBOX_PLAYS.get();
    }

    // Appele a chaque fois qu'un son va etre joue
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlaySound(PlaySoundEvent event) {
        if (!isEnabled()) return;
        SoundInstance sound = event.getSound();
        if (sound == null) return;

        if (sound.getSource() == SoundSource.RECORDS) {
            // Un disque commence : on retient le son et on coupe la musique en cours
            boolean firstRecord = PLAYING_RECORDS.isEmpty();
            PLAYING_RECORDS.put(sound, START_GRACE_TICKS);
            if (firstRecord) {
                Minecraft.getInstance().getSoundManager().stop(null, SoundSource.MUSIC);
            }
        } else if (sound.getSource() == SoundSource.MUSIC && !PLAYING_RECORDS.isEmpty()) {
            // Un disque joue deja : on empeche la musique du jeu de (re)demarrer
            event.setSound(null);
        }
    }

    // Chaque tick : on oublie les disques termines ou arretes
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (PLAYING_RECORDS.isEmpty()) return;
        var soundManager = Minecraft.getInstance().getSoundManager();
        Iterator<Map.Entry<SoundInstance, Integer>> it = PLAYING_RECORDS.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            if (soundManager.isActive(entry.getKey())) {
                entry.setValue(0);
            } else if (entry.getValue() > 0) {
                entry.setValue(entry.getValue() - 1);
            } else {
                it.remove();
            }
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        PLAYING_RECORDS.clear();
    }
}
