package HardcoreLite.utilities;

import necesse.engine.network.server.ServerClient;

public class Helpers {
    public static int getPlayerTeamID(ServerClient serverClient) {
        return serverClient.getServer().world.getTeams().getPlayerTeamID(serverClient.authentication);
    }
}
