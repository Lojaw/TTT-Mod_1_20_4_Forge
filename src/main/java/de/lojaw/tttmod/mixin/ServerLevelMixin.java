package de.lojaw.tttmod.mixin;

import de.lojaw.tttmod.LoggerUtils;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onWorldLoad(CallbackInfo ci) {
        ServerLevel world = (ServerLevel) (Object) this;
        String worldName = world.dimension().location().toString();
        LoggerUtils.logger.info("ServerLevelMixin: World loaded: " + worldName);
    }
}
