package de.lojaw.tttmod.packets;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

public class PacketSniffer {
    public static void main(String[] args) throws PcapNativeException, NotOpenException {
        // Öffne das Netzwerkinterface zum Abfangen von Paketen
        PcapNetworkInterface nif = Pcaps.getDevByName("eth0"); // Ersetze "eth0" durch das entsprechende Interface

        // Erstelle einen Packet Handler zum Verarbeiten der abgefangenen Pakete
        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                // Verarbeite das abgefangene Paket
                if (packet instanceof ClientboundSetEquipmentPacket) {
                    ClientboundSetEquipmentPacket equipmentPacket = (ClientboundSetEquipmentPacket) packet;
                    // Extrahiere die relevanten Informationen aus dem Paket
                    // Vergleiche die Ausrüstungsinformationen für verschiedene Clients
                    // ...
                }
            }
        };

        // Öffne den Live-Capture für das Netzwerkinterface
        try (PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10)) {
            // Setze einen Filter, um nur die gewünschten Pakete zu erfassen
            handle.setFilter("tcp port 25565", BpfProgram.BpfCompileMode.OPTIMIZE);

            // Starte die Paketerfassung
            try {
                handle.loop(-1, listener);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
