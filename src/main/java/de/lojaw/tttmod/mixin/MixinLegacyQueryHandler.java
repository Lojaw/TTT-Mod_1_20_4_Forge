package de.lojaw.tttmod.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.network.LegacyProtocolUtils;
import net.minecraft.server.network.LegacyQueryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LegacyQueryHandler.class)
public abstract class MixinLegacyQueryHandler {

    @Redirect(method = "channelRead", at = @At(value = "INVOKE", target = "Lio/netty/channel/ChannelHandlerContext;fireChannelRead(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;"))
    private ChannelHandlerContext onFireChannelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        return channelHandlerContext;
    }

    @Inject(method = "channelRead", at = @At("HEAD"), cancellable = true)
    public void channelRead(ChannelHandlerContext p_9686_, Object p_9687_, CallbackInfo ci) {
        ByteBuf bytebuf = (ByteBuf)p_9687_;
        bytebuf.markReaderIndex();
        boolean flag = true;

        try {
            if (bytebuf.readUnsignedByte() == 254) {
                // Rest des Codes bleibt unverändert
            }
        } catch (RuntimeException runtimeexception) {
            ci.cancel();
        } finally {
            if (flag) {
                bytebuf.resetReaderIndex();
            }
        }
    }

    @Inject(method = "readCustomPayloadPacket", at = @At("HEAD"), cancellable = true)
    private static void readCustomPayloadPacket(ByteBuf p_297429_, CallbackInfoReturnable<Boolean> cir) {
        short short1 = p_297429_.readUnsignedByte();
        if (short1 != 250) {
            cir.setReturnValue(false);
        } else {
            String s = LegacyProtocolUtils.readLegacyString(p_297429_);
            if (!"MC|PingHost".equals(s)) {
                cir.setReturnValue(false);
            } else {
                // Lesen Sie die Paketdaten, ohne sie weiter zu verarbeiten
                int i = p_297429_.readUnsignedShort();
                p_297429_.skipBytes(i);
                cir.setReturnValue(true);
            }
        }
    }
}