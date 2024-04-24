package de.lojaw.tttmod.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Collection;
import java.util.UUID;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void renderChams(AbstractClientPlayer player, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();

        // Check if the "X" key is pressed
        if (GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS) {
            ClientPacketListener connection = minecraft.getConnection();

            if (connection != null && minecraft.level != null) {
                Collection<PlayerInfo> onlinePlayers = connection.getOnlinePlayers();

                for (PlayerInfo playerInfo : onlinePlayers) {
                    GameProfile profile = playerInfo.getProfile();
                    UUID playerId = profile.getId();

                    // Find the player entity based on the UUID
                    Player playerEntity = minecraft.level.getPlayerByUUID(playerId);

                    if (playerEntity instanceof AbstractClientPlayer && playerEntity != minecraft.player && playerEntity == player) {
                        // Disable depth test and face culling to render players through walls
                        RenderSystem.disableDepthTest();
                        RenderSystem.disableCull();

                        // Render the player with chams effect
                        tttmod$renderPlayerChams(player, entityYaw, partialTicks, matrixStack, buffer, packedLight);

                        // Enable depth test and face culling again
                        RenderSystem.enableDepthTest();
                        RenderSystem.enableCull();

                        System.out.println("CHAMS activated");

                        // Cancel the original rendering
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Unique
    private void tttmod$renderPlayerChams(AbstractClientPlayer player, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        // Render the player model with a custom chams color
        RenderType chamsRenderType = RenderType.eyes(new ResourceLocation("tttmod1204:textures/entity/stevetexture.png"));
        Color chamsColor = new Color(0, 255, 0, 255); // Green color for chams effect

        PlayerModel<AbstractClientPlayer> playerModel = this.getModel();
        playerModel.setupAnim(player, 0, 0, 0, entityYaw, 0);

        VertexConsumer vertexConsumer = buffer.getBuffer(chamsRenderType);
        playerModel.renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, chamsColor.getRed() / 255f, chamsColor.getGreen() / 255f, chamsColor.getBlue() / 255f, chamsColor.getAlpha() / 255f);
    }
}