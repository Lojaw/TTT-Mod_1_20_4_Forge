package de.lojaw.tttmod.mixin;

import de.lojaw.tttmod.LoggerUtils;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraftforge.common.util.LogicalSidedProvider;

import java.util.List;
import com.mojang.datafixers.util.Pair;

@Mixin(Connection.class)
public class PacketHandlerMixin {

    private boolean mixinActive = false;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        // Starte einen separaten Thread für den Timer
        new Thread(() -> {
            try {
                // Warte 5 Sekunden (5000 Millisekunden)
                Thread.sleep(5000);
                mixinActive = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Unique
    private static Player tttmod$getSafePlayer(Entity entity) {
        return entity instanceof Player ? (Player) entity : null;
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onPacketReceived(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!mixinActive) {
            return; // Wenn das Mixin noch nicht aktiv ist, breche die Paketverarbeitung ab
        }
        if (packet instanceof ClientboundSetEquipmentPacket) {
            ClientboundSetEquipmentPacket equipmentPacket = (ClientboundSetEquipmentPacket) packet;
            int entityId = equipmentPacket.getEntity();

            Level level = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).orElse(null);
            if (level != null) {
                Entity entity = level.getEntity(entityId);
                Player player = tttmod$getSafePlayer(entity);
                String playerName = "";
                if (player == null) {
                    LoggerUtils.logger.info("Player ist null für Entity mit ID " + entityId);
                    return; // Verlasse die Methode vorzeitig, wenn player null ist
                }

                Inventory inventory = player.getInventory();

// Hauptinventar (Slot 0-35)
                    for (int i = 0; i < 36; i++) {
                        ItemStack item = inventory.getItem(i);
                        if (!item.isEmpty()) {
                            // Verarbeite das Item
                            LoggerUtils.logger.info("Slot " + i + ": " + item.getDisplayName().getString());
                        }
                    }

// Rüstungsslots (Slot 100-103)
                    for (int i = 100; i <= 103; i++) {
                        ItemStack item = inventory.getItem(i);
                        if (!item.isEmpty()) {
                            // Verarbeite das Item
                            LoggerUtils.logger.info("Armor Slot " + (i - 100) + ": " + item.getDisplayName().getString());
                        }
                    }

// Offhand-Slot (Slot 150)
                    ItemStack offhandItem = inventory.getItem(150);
                    if (!offhandItem.isEmpty()) {
                        // Verarbeite das Offhand-Item
                        LoggerUtils.logger.info("Offhand: " + offhandItem.getDisplayName().getString());
                    }






                    playerName = player.getName().getString();

                    // Inventarinhalte des Spielers ausgeben
                    LoggerUtils.logger.info("Inventory contents for player " + playerName + ":");
                    for (ItemStack itemStack : player.getInventory().items) {
                        if (!itemStack.isEmpty()) {
                            LoggerUtils.logger.info("- " + itemStack.getDisplayName().getString());
                        }
                    }


                List<Pair<EquipmentSlot, ItemStack>> slots = equipmentPacket.getSlots();

                for (Pair<EquipmentSlot, ItemStack> slot : slots) {
                    EquipmentSlot equipmentSlot = slot.getFirst();
                    ItemStack itemStack = slot.getSecond();

                    if (equipmentSlot == EquipmentSlot.CHEST) {
                        if (itemStack.getItem() instanceof ArmorItem) {
                            ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                            if (armorItem.getMaterial() == ArmorMaterials.LEATHER) {
                                String itemName = itemStack.getDisplayName().getString();
                                String packetColor = tttmod$getColorName(itemStack);

                                // Tatsächliche Farbe der Brustplatte aus dem Inventar des Spielers abrufen
                                //Player player = (Player) entity;
                                ItemStack chestplate = player.getInventory().player.getItemBySlot(EquipmentSlot.CHEST);
                                String actualColor = tttmod$getColorName(chestplate);

                                LoggerUtils.logger.info("Received Leather Chestplate for player: " + playerName +
                                        ", Packet Color: " + packetColor + ", Actual Color: " + actualColor +
                                        ", Item: " + itemName);

                                // Vergleiche die Farben und gib eine Warnung aus, wenn sie nicht übereinstimmen
                                if (!packetColor.equals(actualColor)) {
                                    LoggerUtils.logger.info("Color mismatch detected for player " + playerName +
                                            "! Packet Color: " + packetColor + ", Actual Color: " + actualColor);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Unique
    private String tttmod$getColorName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains("display")) {
            CompoundTag displayTag = compoundTag.getCompound("display");
            if (displayTag.contains("color")) {
                int colorInt = displayTag.getInt("color");
                return String.format("#%06X", (0xFFFFFF & colorInt));
            }
        }
        return "Default";
    }

}
