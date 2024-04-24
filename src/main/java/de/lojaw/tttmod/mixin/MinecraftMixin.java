package de.lojaw.tttmod.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import de.lojaw.tttmod.EntityLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import static de.lojaw.tttmod.EntityLogger.ENTITY_IDS_FILE;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Unique
    private boolean tttmod$isKeyToggled = false;
    @Unique
    //private Set<String> tttmod$loggedPlayers = new HashSet<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;

        // Überprüfe, ob die Taste "X" gedrückt wurde
        if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_X)) {
            if (!tttmod$isKeyToggled) {
                tttmod$isKeyToggled = true;
                //tttmod$loggedPlayers.clear(); // Leere die Liste der geloggten Spieler beim Aktivieren des Toggles
            }
        } else {
            tttmod$isKeyToggled = false;
        }

        // Führe den Code nur aus, wenn der Toggle aktiv ist
        if (tttmod$isKeyToggled) {
            tttmod$executeOnToggle(minecraft);
        }
    }

    @Unique
    private void tttmod$executeOnToggle(Minecraft minecraft) {
        ClientPacketListener connection = minecraft.getConnection();

        if (connection != null && minecraft.level != null) {
            Collection<PlayerInfo> onlinePlayers = connection.getOnlinePlayers();

            for (PlayerInfo playerInfo : onlinePlayers) {
                GameProfile profile = playerInfo.getProfile();
                UUID playerId = profile.getId();

                // Suche nach der Spieler-Entität basierend auf der UUID
                Player playerEntity = minecraft.level.getPlayerByUUID(playerId);

                if (playerEntity != null) {
                    String playerName = playerEntity.getName().getString();
                    int entityId = playerEntity.getId();

                    // Hole die Connection des Spielers
                    Connection playerConnection  = connection.getConnection();

                    // Hole die IP-Adresse aus der Connection
                    String ipAddress = playerConnection.getRemoteAddress().toString();
                    System.out.println("IP-Adresse: " + ipAddress);

                    // Erstelle einen eindeutigen Schlüssel basierend auf Spielername und Entity-ID
                    String uniqueKey = playerName + "_" + entityId;

                    // Überprüfe, ob der eindeutige Schlüssel bereits in der Datei vorhanden ist
                    if (!tttmod$isEntryInFile(playerName, entityId)) {
                        // Logge den Spieler und die Entity-ID mit EntityLogger
                        EntityLogger.logEntityId(entityId, playerName);
                    }
                }
            }
        }
    }

    @Unique
    private boolean tttmod$isEntryInFile(String playerName, int entityId) {
        String searchString = "Spawned entity for player: " + playerName + " (ID: " + entityId + ")";
        try (BufferedReader reader = new BufferedReader(new FileReader(ENTITY_IDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}