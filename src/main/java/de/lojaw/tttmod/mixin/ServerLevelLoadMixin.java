//package de.lojaw.tttmod.mixin;
//
//import net.minecraft.server.level.ServerLevel;
//import net.minecraftforge.event.level.LevelEvent;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(ServerLevel.class)
//public class ServerLevelLoadMixin {
//
//    @Inject(method = "level", at = @At("RETURN"))
//    private void onWorldLoad(CallbackInfo ci) {
//        ServerLevel world = (ServerLevel) (Object) this;
//        String worldName = world.dimension().location().toString();
//        System.out.println("ServerLevelLoadMixin: World loaded: " + worldName);
//    }
//}