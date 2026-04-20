package sircow.preservedinferno.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class PreservedCauldronBlockEntityRenderer implements BlockEntityRenderer<PreservedCauldronBlockEntity, PreservedCauldronRenderState> {
    private static final float MIN_HEIGHT = 4f / 16f;
    private static final float MAX_HEIGHT = 14f / 16f;

    public PreservedCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public @NotNull PreservedCauldronRenderState createRenderState() {
        return new PreservedCauldronRenderState();
    }

    @Override
    public void extractRenderState(PreservedCauldronBlockEntity be, PreservedCauldronRenderState state, float partialTick, @NonNull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPosition, overlay);
        if (be.maxWaterProgress > 0) state.fillRatio = (float) be.progressWater / be.maxWaterProgress;
        else state.fillRatio = 0f;
        state.pos = be.getBlockPos();
    }

    @Override
    public void submit(PreservedCauldronRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState cameraRenderState) {
        if (state.fillRatio <= 0f) return;

        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(Fluids.WATER.defaultFluidState());
        int tint = 0xFFFFFFFF;
        var clientLevel = Minecraft.getInstance().level;

        if (clientLevel != null && state.pos != null) {
            tint = BiomeColors.getAverageWaterColor(clientLevel, state.pos);
        }

        float r = ((tint >> 16) & 0xFF) / 255f;
        float g = ((tint >> 8) & 0xFF) / 255f;
        float b = (tint & 0xFF) / 255f;
        float a = 0.8f;
        float height = MIN_HEIGHT + (MAX_HEIGHT - MIN_HEIGHT) * state.fillRatio;
        int packedOverlay = OverlayTexture.NO_OVERLAY;
        int packedLight = 15728880;

        try (var sprite = model.stillMaterial().sprite()) {
            float minU = sprite.getU0();
            float maxU = sprite.getU1();
            float minV = sprite.getV0();
            float maxV = sprite.getV1();

            collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (p, vc) -> {
                // bottom-left
                vc.addVertex(p, 2f/16f, height, 2f/16f)
                        .setColor(r, g, b, a)
                        .setUv(minU, minV)
                        .setOverlay(packedOverlay)
                        .setLight(packedLight)
                        .setNormal(p, 0f, 1f, 0f);
                // top-left
                vc.addVertex(p, 2f/16f, height, 14f/16f)
                        .setColor(r, g, b, a)
                        .setUv(minU, maxV)
                        .setOverlay(packedOverlay)
                        .setLight(packedLight)
                        .setNormal(p, 0f, 1f, 0f);
                // top-right
                vc.addVertex(p, 14f/16f, height, 14f/16f)
                        .setColor(r, g, b, a)
                        .setUv(maxU, maxV)
                        .setOverlay(packedOverlay)
                        .setLight(packedLight)
                        .setNormal(p, 0f, 1f, 0f);
                // bottom-right
                vc.addVertex(p, 14f/16f, height, 2f/16f)
                        .setColor(r, g, b, a)
                        .setUv(maxU, minV)
                        .setOverlay(packedOverlay)
                        .setLight(packedLight)
                        .setNormal(p, 0f, 1f, 0f);
            });
        }
    }
}
