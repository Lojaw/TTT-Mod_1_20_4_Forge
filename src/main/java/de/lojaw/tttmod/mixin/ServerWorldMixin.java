package de.lojaw.tttmod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {
        ServerLevel world = (ServerLevel) (Object) this;
        for (Entity entity : world.getEntities().getAll()) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (player != null) {
                    String playerName = player.getDisplayName().getString();
                    // Führe hier die gewünschte Aktion für Spieler aus
                    System.out.println("ServerWorldMixin: Player in world: " + playerName);
                    // Weitere Aktionen...
                } else {
                    System.out.println("ServerWorldMixin: Player entity is null");
                }
            }
        }
    }
}