package sircow.preservedinferno.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class PreservedCauldronBlockEntityRenderer implements BlockEntityRenderer<PreservedCauldronBlockEntity> {
    public static final Material WATER_STILL = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.withDefaultNamespace("block/water_still"));
    private static final float MIN_HEIGHT = 4.0F / 16.0F;
    private static final float MAX_HEIGHT = 14.0F / 16.0F;

    public PreservedCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PreservedCauldronBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
        if (blockEntity.progressWater > 0) {
            float fillRatio = 0.0F;
            if (blockEntity.maxWaterProgress > 0) {
                fillRatio = (float)blockEntity.progressWater / blockEntity.maxWaterProgress;
            }

            float waterHeight = MIN_HEIGHT + (MAX_HEIGHT - MIN_HEIGHT) * fillRatio;
            TextureAtlasSprite waterSprite = WATER_STILL.sprite();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.solid());
            int waterColor = 0xFF3F76E4;
            float minU = waterSprite.getU0();
            float maxU = waterSprite.getU1();
            float minV = waterSprite.getV0();
            float maxV = waterSprite.getV1();

            poseStack.pushPose();
            poseStack.translate(2.0F / 16.0F, 0.0, 2.0F / 16.0F);

            float x1 = 0.0F;
            float z1 = 0.0F;
            float x2 = 12.0F / 16.0F;
            float z2 = 12.0F / 16.0F;

            consumer.addVertex(poseStack.last().pose(), x1, waterHeight, z1).setColor(waterColor).setUv(minU, minV).setLight(packedLight).setOverlay(packedOverlay).setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(poseStack.last().pose(), x1, waterHeight, z2).setColor(waterColor).setUv(minU, maxV).setLight(packedLight).setOverlay(packedOverlay).setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(poseStack.last().pose(), x2, waterHeight, z2).setColor(waterColor).setUv(maxU, maxV).setLight(packedLight).setOverlay(packedOverlay).setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(poseStack.last().pose(), x2, waterHeight, z1).setColor(waterColor).setUv(maxU, minV).setLight(packedLight).setOverlay(packedOverlay).setNormal(0.0F, 1.0F, 0.0F);

            poseStack.popPose();
        }
    }
}
