package net.lavathelatvian.lavas_trinkets.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.entity.custom.HarpoonProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class HarpoonProjectileModel extends EntityModel<HarpoonProjectile> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "harpoon"), "main");
    private final ModelPart harpoon;

    public HarpoonProjectileModel(ModelPart root) {
        this.harpoon = root.getChild("harpoon");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition harpoon = partdefinition.addOrReplaceChild("harpoon", CubeListBuilder.create().texOffs(2, 11).addBox(0.0F, -20.0F, 0.0F, 1.0F, 20.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 23).addBox(-0.5F, -10.0F, -0.5F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = harpoon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(7, 14).addBox(-3.5F, -1.0F, 0.5F, 11.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -22.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(HarpoonProjectile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        harpoon.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
