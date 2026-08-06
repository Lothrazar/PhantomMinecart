package com.lothrazar.fixmyminecart.carts;

import com.lothrazar.fixmyminecart.ModMain;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

// AbstractMinecartRenderer#submit hardcodes vanilla's minecart texture with no override hook, so this
// duplicates its logic (same as the pre-26.1 code duplicated vanilla's old render() logic by hand) to
// swap in our own texture.
public class PhantomMinecartRenderer extends AbstractMinecartRenderer<ReinforcedMinecart, MinecartRenderState> {

  private static final Identifier CART = Identifier.fromNamespaceAndPath(ModMain.MODID, "textures/entity/" + ReinforcedMinecart.ID + ".png");

  public PhantomMinecartRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, ModelLayers.MINECART);
  }

  @Override
  public MinecartRenderState createRenderState() {
    return new MinecartRenderState();
  }

  @Override
  public void submit(MinecartRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    if (state.leashStates != null) {
      for (var leashState : state.leashStates) {
        submitNodeCollector.submitLeash(poseStack, leashState);
      }
    }
    this.submitNameDisplay(state, poseStack, submitNodeCollector, camera);

    poseStack.pushPose();
    long seed = state.offsetSeed;
    float offsetX = (((float) (seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float offsetY = (((float) (seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float offsetZ = (((float) (seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    poseStack.translate(offsetX, offsetY, offsetZ);
    if (state.isNewRender) {
      poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
      poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
      poseStack.translate(0.0F, 0.375F, 0.0F);
    }
    else {
      double entityX = state.x;
      double entityY = state.y;
      double entityZ = state.z;
      float xRot = state.xRot;
      float rotation = state.yRot;
      if (state.posOnRail != null && state.frontPos != null && state.backPos != null) {
        Vec3 frontPos = state.frontPos;
        Vec3 backPos = state.backPos;
        poseStack.translate(state.posOnRail.x - entityX, (frontPos.y + backPos.y) / 2.0 - entityY, state.posOnRail.z - entityZ);
        Vec3 direction = backPos.add(-frontPos.x, -frontPos.y, -frontPos.z);
        if (direction.length() != 0.0) {
          direction = direction.normalize();
          rotation = (float) (Math.atan2(direction.z, direction.x) * 180.0 / Math.PI);
          xRot = (float) (Math.atan(direction.y) * 73.0);
        }
      }
      poseStack.translate(0.0F, 0.375F, 0.0F);
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotation));
      poseStack.mulPose(Axis.ZP.rotationDegrees(-xRot));
    }

    float hurt = state.hurtTime;
    if (hurt > 0.0F) {
      poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * state.damageTime / 10.0F * state.hurtDir));
    }

    BlockModelRenderState displayBlockModel = state.displayBlockModel;
    if (!displayBlockModel.isEmpty()) {
      poseStack.pushPose();
      poseStack.scale(0.75F, 0.75F, 0.75F);
      poseStack.translate(-0.5F, (state.displayOffset - 8) / 16.0F, 0.5F);
      poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
      displayBlockModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
      poseStack.popPose();
    }

    poseStack.scale(-1.0F, -1.0F, 1.0F);
    submitNodeCollector.submitModel(this.model, state, poseStack, CART, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
    poseStack.popPose();
  }
}
