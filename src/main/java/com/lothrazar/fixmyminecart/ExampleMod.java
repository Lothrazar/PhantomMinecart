package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartRenderer;
import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExampleMod.MODID)
public class ExampleMod {

  public static final String MODID = "fixmyminecart";
  public static final Logger LOGGER = LogManager.getLogger();

  public ExampleMod() {
    ConfigManager.setup();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    //
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ExampleRegistry.ITEMS.register(eventBus);
    ExampleRegistry.ENTITIES.register(eventBus);
  }

  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist  
    //MinecraftForge.EVENT_BUS.register(new ItemEvents());
    if (ConfigManager.TESTING.get()) {
      float test = Blocks.BEDROCK.getDefaultState().hardness;
      ExampleMod.LOGGER.info("accesstransformer.cfg test bedrock hardness = " + test);
    }
  }

  private void setupClient(final FMLClientSetupEvent event) {
    //for client side only setup
    RenderingRegistry.registerEntityRenderingHandler(ExampleRegistry.E_REINFORCED_MINECART.get(), MinecartRenderer::new);
  }
}
