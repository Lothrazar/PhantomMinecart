package com.lothrazar.fixmyminecart.carts;

import com.lothrazar.fixmyminecart.ExampleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReinforcedMinecart extends AbstractMinecartEntity {

  public static final String ID = "reinforced_minecart";
  public CartType type = CartType.SPEED;

  public ReinforcedMinecart(EntityType<?> type, World worldIn) {
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

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    ActionResultType ret = super.processInitialInteract(player, hand);
    if (ret.isSuccessOrConsume() || player.isSecondaryUseActive() || this.isBeingRidden()) {
      return ret;
    }
    if (player.startRiding(this)) {
      return ActionResultType.CONSUME;
    }
    return ret;
  }

  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      if (this.isBeingRidden()) {
        this.removePassengers();
      }
      if (this.getRollingAmplitude() == 0) {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(50.0F);
        this.markVelocityChanged();
      }
    }
  }
  //  public static AttributeModifierMap.MutableAttribute func_234226_m_() {
  //    return MobEntity.func_233666_p_()
  //        .createMutableAttribute(Attributes.MAX_HEALTH, 4.0D)
  //        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F);
  //  }

  @Override
  public boolean hasDisplayTile() {
    return false;
  }

  @Override
  protected float getSpeedFactor() {
    return super.getSpeedFactor() * type.getSpeed();
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
      if (this.isBeingRidden()) {
        Entity entity = this.getPassengers().get(0);
      }
    }
  }
}
