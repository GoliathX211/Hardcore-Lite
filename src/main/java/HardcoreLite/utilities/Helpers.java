package HardcoreLite.utilities;

import necesse.engine.GameLog;
import necesse.engine.network.server.ServerClient;
import necesse.engine.team.PlayerTeam;

public class Helpers {

    public static void updatePlayerTeamID(ServerClient serverClient) {
        serverClient.getServer().world.getTeams().getPlayerTeamID(serverClient.authentication);
    }

    public static PlayerTeam getPlayerTeam(ServerClient serverClient) {
        return serverClient.getServer().world.getTeams().getPlayerTeam(serverClient.authentication);
    }

    public static int getPlayerTeamID(ServerClient serverClient) {
        return serverClient.getServer().world.getTeams().getPlayerTeamID(serverClient.authentication);
    }

    public static void ensureClientTeamIsSyncedWithTeamManager(ServerClient serverClient) {
        GameLog.out.println("Ensuring client team is synced with TeamManager");
        int before = serverClient.getTeamID();
        GameLog.out.println("Before: " + before);
        PlayerTeam oldTeam = getPlayerTeam(serverClient);
        updatePlayerTeamID(serverClient);
        int after = serverClient.getTeamID();
        GameLog.out.println("After: " + after);
        if (before != after) {
            GameLog.out.println("Player team ID was out of sync with TeamManager, fixing.");
            PlayerTeam newTeam = getPlayerTeam(serverClient);
            if (oldTeam != newTeam) {
                GameLog.out.println("Player PlayerTeam was out of sync with TeamManager, fixing.");
                PlayerTeam.removeMember(serverClient.getServer(), oldTeam, serverClient.authentication, false);
                PlayerTeam.addMember(serverClient.getServer(), newTeam, serverClient.authentication);
            }
            PlayerTeam.onUpdateTeam(serverClient.getServer(), serverClient.authentication, after);
            serverClient.getLevel().settlementLayer.markBasicsDirty();
        }
    }
}
