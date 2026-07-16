package sircow.preservedinferno.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.entity.custom.PrimedBoomBox;

public class BoomBoxRenderer extends EntityRenderer<PrimedBoomBox, TntRenderState> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelResolver blockModelResolver;

    public BoomBoxRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockModelResolver = context.getBlockModelResolver();
    }

    public void submit(TntRenderState state, PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        float fuse = state.fuseRemainingInTicks;
        if (fuse < 10.0F) {
            float scale = 1.0F + getSwellAmount(fuse);
            poseStack.scale(scale, scale, scale);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        if (!state.blockState.isEmpty()) {
            TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, poseStack, submitNodeCollector, state.lightCoords, isLit(fuse), state.outlineColor);
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public static float getSwellAmount(float fuse) {
        float g = 1.0F - fuse / 10.0F;
        g = Mth.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        return g * 0.3F;
    }

    public static boolean isLit(float fuse) {
        return fuse < 0.0F ? false : (int)(fuse / 5.0F) % 2 == 0;
    }

    public @NonNull TntRenderState createRenderState() {
        return new TntRenderState();
    }

    public void extractRenderState(@NonNull PrimedBoomBox entity, @NonNull TntRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.fuseRemainingInTicks = (float)entity.getFuse() - partialTicks + 1.0F;
        this.blockModelResolver.update(state.blockState, entity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
    }
}
