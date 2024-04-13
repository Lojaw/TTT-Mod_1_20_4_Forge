package de.lojaw.tttmod.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.charset.StandardCharsets;

/**
 * @author Dein Name
 */
@Mixin(ClientIntentionPacket.class)
public abstract class MixinC00Handshake {
   @Final
   @Shadow
   private int protocolVersion;
   @Final
   @Shadow
   private String hostName;
   @Final
   @Shadow
   private int port;
   @Final
   @Shadow
   private ClientIntent intention;

   /**
    * @author Dein Name
    * @reason Grund für die Überschreibung
    */
   @Overwrite
   public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(this.protocolVersion);
      buf.writeBytes(this.hostName.getBytes(StandardCharsets.UTF_8));
      buf.writeShort(this.port);
      buf.writeVarInt(this.intention.id());
   }
}