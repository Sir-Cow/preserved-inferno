package sircow.preservedinferno.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PreservedCauldronBlockEntityRenderer implements BlockEntityRenderer<PreservedCauldronBlockEntity, PreservedCauldronRenderState> {

    private static final float MIN_HEIGHT = 4f / 16f;
    private static final float MAX_HEIGHT = 14f / 16f;

    public PreservedCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {

    }

    @Override
    public @NotNull PreservedCauldronRenderState createRenderState() {
        return new PreservedCauldronRenderState();
    }

    @Override
    public void extractRenderState(
            PreservedCauldronBlockEntity be,
            PreservedCauldronRenderState state,
            float partialTick,
            net.minecraft.world.phys.Vec3 cameraPosition,
            net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay overlay
    ) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPosition, overlay);
        if (be.maxWaterProgress > 0)
            state.fillRatio = (float) be.progressWater / be.maxWaterProgress;
        else
            state.fillRatio = 0f;
    }

    @Override
    public void submit(PreservedCauldronRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        if (state.fillRatio <= 0f) return;

        var handler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER);
        if (handler == null) return;

        var sprite = handler.getFluidSprites(null, null, Fluids.WATER.defaultFluidState())[0];
        int tint = handler.getFluidColor(null, null, Fluids.WATER.defaultFluidState());

        float r = ((tint >> 16) & 0xFF) / 255f;
        float g = ((tint >> 8) & 0xFF) / 255f;
        float b = (tint & 0xFF) / 255f;
        float a = 0.8f;

        float height = MIN_HEIGHT + (MAX_HEIGHT - MIN_HEIGHT) * state.fillRatio;

        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        PoseStack.Pose pose = poseStack.last();

        int packedOverlay = OverlayTexture.NO_OVERLAY;
        int packedLight = 15728880; // full bright

        collector.submitCustomGeometry(poseStack, RenderType.solid(), (p, vc) -> {
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
