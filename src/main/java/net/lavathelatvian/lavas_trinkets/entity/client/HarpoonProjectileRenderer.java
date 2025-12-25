package net.lavathelatvian.lavas_trinkets.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.entity.custom.HarpoonProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.RenderShape;

public class HarpoonProjectileRenderer extends EntityRenderer<HarpoonProjectile> {

    private HarpoonProjectileModel model;
    public HarpoonProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HarpoonProjectileModel(context.bakeLayer(HarpoonProjectileModel.LAYER_LOCATION));
    }


    @Override
    public void render(HarpoonProjectile pEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
                VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                buffer, this.model.renderType(this.getTextureLocation(pEntity)),false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(pEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HarpoonProjectile harpoonProjectile) {
        return ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "textures/entity/harpoon/harpoon.png");
    }

}