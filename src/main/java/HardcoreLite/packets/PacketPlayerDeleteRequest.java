package HardcoreLite.packets;

import necesse.engine.GameLog;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.packet.PacketDisconnect;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

import java.io.IOException;

public class PacketPlayerDeleteRequest extends Packet {
    public PacketPlayerDeleteRequest(byte[] data) {
        super(data);
    }

    public PacketPlayerDeleteRequest() {
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        try {
            server.disconnectClient(client, PacketDisconnect.Code.KICK);
            GameLog.debug.println("Player delete packet receiving. Player kicked.");

            client.playerMob.getWorldEntity().serverWorld.fileSystem.getPlayerFile(client.authentication).delete();

        } catch (IOException ex) {
            GameLog.debug.println(ex);
        }
        GameLog.debug.println("End of packet.");

    }
}