package HardcoreLite;

import HardcoreLite.registry.ModPacketRegistry;
import necesse.engine.modLoader.annotations.ModEntry;

@ModEntry
public class ModEntrypoint {
   public void init() {
      ModPacketRegistry.RegisterAll();
   }

   public void initResources() {
   }

   public void postInit() {
   }
}
