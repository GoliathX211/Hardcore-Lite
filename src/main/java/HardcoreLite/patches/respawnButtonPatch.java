package HardcoreLite.patches;

import HardcoreLite.packets.PacketPlayerDeleteRequest;
import necesse.engine.GameDeathPenalty;
import necesse.engine.GameLog;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.engine.network.packet.PacketPlayerRespawnRequest;
import necesse.engine.state.MainGame;
import necesse.gfx.forms.MainGameFormManager;
import necesse.gfx.forms.components.FormButton;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.events.FormEventListener;
import necesse.gfx.forms.events.FormEventsHandler;
import necesse.gfx.forms.events.FormInputEvent;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Field;

@ModMethodPatch(target = MainGameFormManager.class, name = "setup", arguments = {})
public class respawnButtonPatch {

    @Advice.OnMethodExit()
    public static void onExit(
            @Advice.This MainGameFormManager formManager,
            @Advice.FieldValue("respawnButton") FormLocalTextButton respawnButton,
            @Advice.FieldValue("client") Client client,
            @Advice.FieldValue("mainGame") MainGame mainGame

    ) throws IllegalAccessException, NoSuchFieldException {
        Class button = FormButton.class;
        Field clickedEventsField;

        clickedEventsField = button.getDeclaredField("clickedEvents");
        clickedEventsField.setAccessible(true);


        FormEventsHandler<FormInputEvent<FormButton>> clickedEvents = (FormEventsHandler<FormInputEvent<FormButton>>) clickedEventsField.get(respawnButton);
        clickedEvents.clearListeners();
        GameLog.debug.println("Cleared listeners.");

        respawnButton.onClicked(listenerMethod(client, formManager, mainGame));
    }

    public static FormEventListener<FormInputEvent<FormButton>> listenerMethod(Client client, MainGameFormManager formManager, MainGame mainGame) {
        return (e) -> {
            GameLog.debug.println("Event clicked.");

            Class formManagerClass = MainGameFormManager.class;
            Field cooldown;
            try {
                cooldown = formManagerClass.getDeclaredField("respawnButtonCD");
                cooldown.setAccessible(true);
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }


            if (client.worldSettings.deathPenalty == GameDeathPenalty.HARDCORE) {
                client.network.sendPacket(new PacketPlayerDeleteRequest());
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