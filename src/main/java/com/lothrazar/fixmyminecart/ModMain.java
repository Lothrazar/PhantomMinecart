package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartRenderer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModMain.MODID)
public class ModMain {

  public static final String MODID = "fixmyminecart";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModMain() {
    ConfigManager.setup();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    //
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    CartRegistry.ITEMS.register(eventBus);
    CartRegistry.ENTITIES.register(eventBus);
  }

  private void setup(final FMLCommonSetupEvent event) {}

  private void setupClient(final FMLClientSetupEvent event) {
    //for client side only setup
    RenderingRegistry.registerEntityRenderingHandler(CartRegistry.E_REINFORCED_MINECART.get(), MinecartRenderer::new);
  }
}
