package HardcoreLite.registry;

import HardcoreLite.packets.PacketPlayerDeleteRequest;
import necesse.engine.registries.PacketRegistry;

public class ModPacketRegistry {
    public static void RegisterAll() {
        PacketRegistry.registerPacket(true, PacketPlayerDeleteRequest.class);
    }
}
