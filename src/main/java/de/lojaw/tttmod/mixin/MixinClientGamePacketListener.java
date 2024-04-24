package de.lojaw.tttmod.mixin;

import de.lojaw.tttmod.EntityLogger;
import de.lojaw.tttmod.WorldLoadListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ClientGamePacketListener.class)
public abstract class MixinClientGamePacketListener {

    @Inject(method = "handleAddEntity(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V", at = @At("RETURN"), cancellable = true)
    private void onHandleAddEntity(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        int entityId = packet.getId();
        MobCategory category = packet.getType().getCategory();

        if (category == MobCategory.CREATURE || category == MobCategory.MONSTER) {
            Entity entity = packet.getType().create(null); // Erstellen einer temporären Entität für Typüberprüfung
            if (entity instanceof net.minecraft.server.level.ServerPlayer) {
                String playerName = ((net.minecraft.server.level.ServerPlayer) entity).getGameProfile().getName();
                String logEntry = "Spawned entity for player: " + playerName + " (ID: " + entityId + ")";

                Set<String> threadLoggedEntities = WorldLoadListener.getLoggedEntities().get();
                if (!threadLoggedEntities.contains(logEntry)) {
                    //EntityLogger.logEntityId(entityId, playerName);
                    threadLoggedEntities.add(logEntry);
                }
            } else {
                String logEntry = "Spawned entity (ID: " + entityId + ")";

                Set<String> threadLoggedEntities = WorldLoadListener.getLoggedEntities().get();
                if (!threadLoggedEntities.contains(logEntry)) {
                    //EntityLogger.logEntityId(entityId, null);
                    threadLoggedEntities.add(logEntry);
                }
            }
            ci.cancel(); // Verhindert die weitere Ausführung der ursprünglichen handleAddEntity-Methode
        }
    }
}