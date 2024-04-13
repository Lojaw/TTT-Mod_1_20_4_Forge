package de.lojaw.tttmod.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;

class VanillaPayload implements CustomPacketPayload {
   private static final ResourceLocation ID = new ResourceLocation("mc", "brand");
   private static final byte[] VANILLA_BYTES = "vanilla".getBytes(StandardCharsets.UTF_8);

   @Override
   public @NotNull ResourceLocation id() {
      return ID;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeBytes(VANILLA_BYTES);
   }
}

@Mixin(Connection.class)
public abstract class MixinNetworkManager {
   @Shadow
   protected abstract void sendPacket(Packet<?> packet, PacketSendListener listener, boolean blockingPacket);

   @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
   private void sendPacket(Packet<?> packet, PacketSendListener listener, boolean blockingPacket, CallbackInfo ci) {
      if (packet instanceof ServerboundCustomPayloadPacket customPayloadPacket) {
         ResourceLocation channelId = customPayloadPacket.payload().id();

         if (!channelId.getNamespace().equalsIgnoreCase("mc")) {
            // Cancel sending non-Minecraft packets
            ci.cancel();
         } else if (channelId.getPath().equalsIgnoreCase("brand")) {
            // Replace the brand packet with a modified one
            ServerboundCustomPayloadPacket modifiedPacket = new ServerboundCustomPayloadPacket(new VanillaPayload());
            this.sendPacket(modifiedPacket, listener, blockingPacket);
            ci.cancel();
         }
      }
   }
}