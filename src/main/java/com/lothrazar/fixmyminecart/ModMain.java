package com.lothrazar.fixmyminecart;

import net.neoforged.fml.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ModMain.MODID)
public class ModMain {

  public static final String MODID = "fixmyminecart";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModMain(IEventBus bus, ModContainer modContainer) {
    bus.addListener(CartRegistry::setupDispenserBehavior);
    bus.addListener(CartRegistry::entityRenderers);
    bus.addListener(CartRegistry::buildContents);
    CartRegistry.ITEMS.register(bus);
    CartRegistry.ENTITIES.register(bus);
  }

}
