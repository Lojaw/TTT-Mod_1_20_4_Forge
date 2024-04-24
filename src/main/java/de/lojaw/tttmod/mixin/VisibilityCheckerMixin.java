package de.lojaw.tttmod.mixin;

import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VisibilityChecker.class)
public abstract class VisibilityCheckerMixin {

    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void onIsVisible(Entity entity1, Entity entity2, CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = Minecraft.getInstance();

        // Check if the "X" key is pressed
        if (GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS) {
            // Check if both entities are players
            if (entity1 instanceof Player && entity2 instanceof Player) {
                // Make the player always visible
                cir.setReturnValue(true);
            }
        }
    }
}
