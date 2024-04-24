package de.lojaw.tttmod;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber
public class WorldLoadListener {
    private static final ThreadLocal<Set<String>> loggedEntities = ThreadLocal.withInitial(HashSet::new);

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        //loggedEntities.get().clear();
        //EntityLogger.clearLogFile();
        System.out.println("Neue Welt geladen.");
    }

    public static ThreadLocal<Set<String>> getLoggedEntities() {
        return loggedEntities;
    }
}