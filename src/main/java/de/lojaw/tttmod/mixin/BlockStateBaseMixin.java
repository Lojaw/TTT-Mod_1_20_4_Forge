package de.lojaw.tttmod.mixin;

import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {

    @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
    private void onCanOcclude(CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = Minecraft.getInstance();

        // Check if the "X" key is pressed
        //if (GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS) {
            // Make blocks always non-occluding
            cir.setReturnValue(false);
        //}
    }
}
