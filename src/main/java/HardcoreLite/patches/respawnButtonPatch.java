package HardcoreLite.patches;

import com.codedisaster.steamworks.SteamUser;
import necesse.engine.GameDeathPenalty;
import necesse.engine.commands.serverCommands.DeletePlayerServerCommand;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.engine.network.packet.PacketPlayerRespawnRequest;
import necesse.engine.state.MainGame;
import necesse.engine.steam.SteamData;
import necesse.gfx.forms.MainGameFormManager;
import necesse.gfx.forms.components.FormButton;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.events.FormEventListener;
import necesse.gfx.forms.events.FormInputEvent;
import net.bytebuddy.asm.Advice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.Consumer;

@ModMethodPatch(target = MainGameFormManager.class, name = "setup", arguments = {})
public class respawnButtonPatch {
    static final Class formManagerClass = MainGameFormManager.class;
    static final Field cooldown;

    static {
        try {
            cooldown = formManagerClass.getDeclaredField("respawnButton");
            cooldown.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    
    
    @Advice.OnMethodExit()
    public static void onExit(
            @Advice.This MainGameFormManager formManager,
            @Advice.FieldValue("respawnButton") FormLocalTextButton respawnButton,
            @Advice.FieldValue("client") Client client,
            @Advice.FieldValue("mainGame") MainGame mainGame

    ) {



        respawnButton.onClicked(onClickMethod(formManager, client));
    }

    public static FormEventListener<FormInputEvent<FormButton>> onClickMethod(MainGameFormManager formManager, Client client) {
        return (e) -> {

            if (client.worldSettings.deathPenalty == GameDeathPenalty.HARDCORE) {
                // mainGame.disconnect("Quit");

                if (client.worldEntity == null) {
                    System.out.println("worldEntity is empty");
                    return;
                }
                if (client.worldEntity.serverWorld == null) {
                    System.out.println("serverWorld is empty");
                    return;
                }
                if (client.worldEntity.serverWorld.fileSystem == null) {
                    System.out.println("fileSystem is empty");
                    return;
                }

                try {
                    client.worldEntity.serverWorld.fileSystem.getPlayerFile(client.getClient().authentication).delete();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                client.network.sendPacket(new PacketPlayerRespawnRequest());
            }

            try {
                cooldown.set(formManager, (System.currentTimeMillis() + 5000L));
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

        };
    }
}
