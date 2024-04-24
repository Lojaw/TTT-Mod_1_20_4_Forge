//package de.lojaw.tttmod.mixin;
//
//import de.lojaw.tttmod.EntityLogger;
//import de.lojaw.tttmod.WorldLoadListener;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
//import net.minecraft.world.entity.Entity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.Set;
//
//@Mixin(ClientboundAddEntityPacket.class)
//public class MixinClientboundAddEntityPacket {
//
//    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At("HEAD"))
//    private void onHandlePacket(ClientGamePacketListener listener, CallbackInfo ci) {
//        int entityId = entity.getId();
//
//        if (entity instanceof net.minecraft.server.level.ServerPlayer player) {
//            String playerName = player.getGameProfile().getName();
//            String logEntry = "Spawned entity for player: " + playerName + " (ID: " + entityId + ")";
//
//            Set<String> threadLoggedEntities = WorldLoadListener.getLoggedEntities().get();
//            if (!threadLoggedEntities.contains(logEntry)) {
//                EntityLogger.logEntityId(entityId, playerName);
//                threadLoggedEntities.add(logEntry);
//            }
//        } else {
//            String logEntry = "Spawned entity (ID: " + entityId + ")";
//
//            Set<String> threadLoggedEntities = WorldLoadListener.getLoggedEntities().get();
//            if (!threadLoggedEntities.contains(logEntry)) {
//                EntityLogger.logEntityId(entityId, null);
//                threadLoggedEntities.add(logEntry);
//            }
//        }
//    }
//}