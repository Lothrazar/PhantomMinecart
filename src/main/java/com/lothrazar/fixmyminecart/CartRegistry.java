package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartItem;
import com.lothrazar.fixmyminecart.carts.PhantomMinecartRenderer;
import com.lothrazar.fixmyminecart.carts.ReinforcedMinecart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CartRegistry {

  static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);
  static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModMain.MODID);
  // now for the content
  public static final RegistryObject<EntityType<ReinforcedMinecart>> E_REINFORCED_MINECART = ENTITIES.register(ReinforcedMinecart.ID, () ->
  //actual sub register
  register(ReinforcedMinecart.ID, EntityType.Builder.<ReinforcedMinecart> of(ReinforcedMinecart::new, MobCategory.MISC)
      .sized(0.98F, 0.7F).clientTrackingRange(8).setCustomClientFactory(ReinforcedMinecart::new)));
  public static final RegistryObject<Item> I_REINFORCED_MINECART = ITEMS.register(ReinforcedMinecart.ID, () -> new MinecartItem(new Item.Properties()));

  //.tab(CreativeModeTab.TAB_TRANSPORTATION)
  private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(id);
  }

  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(E_REINFORCED_MINECART.get(), PhantomMinecartRenderer::new);
  }

  @SubscribeEvent
  public static void addCreative(CreativeModeTabEvent.BuildContents event) {
    if (event.getTab() == CreativeModeTabs.REDSTONE_BLOCKS) {
      event.accept(I_REINFORCED_MINECART.get());
    }
  }
}
