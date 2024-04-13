package de.lojaw.tttmod.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientIntentionPacket.class)
public abstract class MixinProtocolversion {
    @Mutable
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

    @Inject(method = "<init>(ILjava/lang/String;ILnet/minecraft/network/protocol/handshake/ClientIntent;)V", at = @At("TAIL"))
    public void init(int protocolVersion, String hostName, int port, ClientIntent intention, CallbackInfo ci) {
        System.out.println("Original Protocol Version: " + protocolVersion);
        this.protocolVersion = 784; // Setzen Sie die Protokollversion für 1.20.4
        System.out.println("Modified Protocol Version: " + this.protocolVersion);
    }

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("TAIL"))
    public void init(FriendlyByteBuf buf, CallbackInfo ci) {
        System.out.println("Original Protocol Version from Buffer: " + this.protocolVersion);
        this.protocolVersion = 784; // Setzen Sie die Protokollversion für 1.20.4
        System.out.println("Modified Protocol Version from Buffer: " + this.protocolVersion);
    }

    /**
     * @author Me
     * @reason This is a reason.
     */
    @Overwrite
    public void write(FriendlyByteBuf buf) {
        System.out.println("Writing Protocol Version: " + this.protocolVersion);
        buf.writeVarInt(784); // Setzen Sie die Protokollversion für 1.20.4
        buf.writeUtf(this.hostName);
        buf.writeShort(this.port);
        buf.writeVarInt(this.intention.id());
    }
}