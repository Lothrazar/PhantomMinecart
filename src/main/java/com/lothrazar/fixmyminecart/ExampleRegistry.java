package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartItem;
import com.lothrazar.fixmyminecart.carts.ReinforcedMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExampleRegistry {

  static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);
  static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ExampleMod.MODID);
  // now for the content
  public static final RegistryObject<EntityType<ReinforcedMinecart>> E_REINFORCED_MINECART = ENTITIES.register(ReinforcedMinecart.ID, () -> register(ReinforcedMinecart.ID, EntityType.Builder.<ReinforcedMinecart> create(ReinforcedMinecart::new, EntityClassification.MISC)
      .size(0.98F, 0.7F).trackingRange(8).setCustomClientFactory(ReinforcedMinecart::new)));
  public static final RegistryObject<Item> I_REINFORCED_MINECART = ITEMS.register(ReinforcedMinecart.ID, () -> new MinecartItem(new Item.Properties()));

  private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(id);
  }
}
