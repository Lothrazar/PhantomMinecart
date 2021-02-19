package com.lothrazar.fixmyminecart;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReinforcedMinecart extends AbstractMinecartEntity {

  public static final String ID = "reinforced_minecart";

  protected ReinforcedMinecart(EntityType<?> type, World worldIn) {
    super(type, worldIn);
  }

  public ReinforcedMinecart(World worldIn, double x, double y, double z) {
    super(ExampleRegistry.E_REINFORCED_MINECART.get(), worldIn, x, y, z);
  }

  public ReinforcedMinecart(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
    this(ExampleRegistry.E_REINFORCED_MINECART.get(), worldIn);
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  @Override
  public ItemStack getCartItem() {
    return new ItemStack(ExampleRegistry.I_REINFORCED_MINECART.get());
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  public static AttributeModifierMap.MutableAttribute func_234226_m_() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 4.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F);
  }

  @Override
  public boolean hasDisplayTile() {
    return false;
  }

  @Override
  protected float getSpeedFactor() {
    return super.getSpeedFactor() * 1.4F;
  }

  @Override
  public boolean canBeRidden() {
    return true;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.world.isRemote) {
      // // //
    }
  }
}
