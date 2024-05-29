package HardcoreLite.packets;

import java.io.IOException;
import necesse.engine.GameLog;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.packet.PacketDisconnect;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class PacketPlayerDeleteRequest extends Packet {
   public PacketPlayerDeleteRequest(byte[] data) {
      super(data);
   }

   public PacketPlayerDeleteRequest() {
   }

   public void processServer(NetworkPacket packet, Server server, ServerClient client) {
      try {
         server.disconnectClient(client, new PacketDisconnect(client.slot, new LocalMessage("hardcorelite", "playerdeathmsg")));
         GameLog.debug.println("Player delete packet receiving. Player kicked.");
         server.world.fileSystem.getPlayerFile(client).delete();
         server.world.fileSystem.getMapPlayerFile(client).delete();
     } catch (IOException var5) {
         GameLog.debug.println(var5);
      }

      GameLog.debug.println("End of packet.");
   }
}
