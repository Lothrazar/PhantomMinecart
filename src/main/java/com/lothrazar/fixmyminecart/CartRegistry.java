package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartItem;
import com.lothrazar.fixmyminecart.carts.PhantomMinecartRenderer;
import com.lothrazar.fixmyminecart.carts.ReinforcedMinecart;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//@EventBusSubscriber(modid = ModMain.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CartRegistry {

  static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ModMain.MODID);
  static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ModMain.MODID);
  // now for the content
  public static final DeferredHolder<EntityType<?>, EntityType<ReinforcedMinecart>> E_REINFORCED_MINECART = ENTITIES.register(ReinforcedMinecart.ID, () ->
  //actual sub register
  register(ReinforcedMinecart.ID, EntityType.Builder.<ReinforcedMinecart> of(ReinforcedMinecart::new, MobCategory.MISC)
      .sized(0.98F, 0.7F).clientTrackingRange(8)));
  public static final DeferredHolder<Item, MinecartItem> I_REINFORCED_MINECART = ITEMS.register(ReinforcedMinecart.ID, () -> new MinecartItem(new Item.Properties()));

  private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(id);
  }

  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(E_REINFORCED_MINECART.get(), PhantomMinecartRenderer::new);
  }

  @SubscribeEvent
  public static void buildContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
      event.accept(I_REINFORCED_MINECART.get());
    }
  }
}
