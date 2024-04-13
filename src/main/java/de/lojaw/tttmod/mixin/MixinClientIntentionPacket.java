package de.lojaw.tttmod.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientIntentionPacket.class)
public abstract class MixinClientIntentionPacket {

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    public void write(FriendlyByteBuf buf, CallbackInfo ci) {
        // Ändern Sie die Protokollversion hier, wenn gewünscht
        int modifiedProtocolVersion = 765; // Setzen Sie die Protokollversion für 1.20.4, sonst 763
        buf.writeVarInt(modifiedProtocolVersion);
        buf.writeUtf(((ClientIntentionPacket)(Object)this).hostName());
        buf.writeShort(((ClientIntentionPacket)(Object)this).port());
        buf.writeVarInt(((ClientIntentionPacket)(Object)this).intention().id());
        ci.cancel();
    }
}