package HardcoreLite.patches;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.NetworkClient;
import net.bytebuddy.asm.Advice;

import static HardcoreLite.utilities.Helpers.getPlayerTeamID;

@ModMethodPatch(
        target = NetworkClient.class,
        name = "getTeamID",
        arguments = {},
        priority = 1000
)
public class NetworkClientGetTeamIDPatch {
    @Advice.OnMethodExit
    public static void onExit(@Advice.This NetworkClient networkClient, @Advice.Return(readOnly = false) int teamID) {
        // This prevents clients from thinking the player left the team when they re-join the server.
        if (teamID == -1) {
            if (networkClient.isServer()) teamID = getPlayerTeamID(networkClient.getServerClient());
        }
    }
}
