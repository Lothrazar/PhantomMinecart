package com.lothrazar.fixmyminecart.carts;

import com.lothrazar.fixmyminecart.ExampleMod;
import net.minecraft.util.ResourceLocation;

public enum CartType {

  SPEED, SLOW, POWER;

  private static final ResourceLocation CACTICART = new ResourceLocation(ExampleMod.MODID, "textures/entity/" + ReinforcedMinecart.ID + ".png");

  public ResourceLocation getTexture() {
    switch (this) {
      case POWER:
        return CACTICART;
      case SLOW:
        return CACTICART;
      case SPEED:
        return CACTICART;
    }
    return null;
  }

  public float getSpeed() {
    switch (this) {
      case POWER:
        return 1F;
      case SLOW:
        return 0.5F;
      case SPEED:
        return 1.5F;
    }
    return 0;
  }
}
