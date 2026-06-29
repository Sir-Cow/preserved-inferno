package sircow.preservedinferno.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.entity.custom.ThrownCopperTrident;

public class ThrownCopperTridentRenderer extends EntityRenderer<ThrownCopperTrident, ThrownTridentRenderState> {
    public static final Identifier TRIDENT_LOCATION = Constants.id("textures/entity/copper_trident.png");
    public static final ModelLayerLocation COPPER_TRIDENT = new ModelLayerLocation(Constants.id("copper_trident"), "main");
    private final CopperTridentModel model;

    public ThrownCopperTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CopperTridentModel(context.bakeLayer(COPPER_TRIDENT));
    }

    public void submit(final ThrownTridentRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        submitNodeCollector.order(0).submitModel(
                this.model,
                Unit.INSTANCE,
                poseStack,
                TRIDENT_LOCATION,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
        );
        if (state.isFoil) {
            submitNodeCollector.order(1).submitModel(
                    this.model,
                    Unit.INSTANCE,
                    poseStack,
                    RenderTypes.entityGlint(),
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null
            );
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected @NonNull AABB getBoundingBoxForCulling(final @NonNull ThrownCopperTrident entity) {
        return super.getBoundingBoxForCulling(entity).inflate(1.5);
    }

    public @NotNull ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    public void extractRenderState(final @NonNull ThrownCopperTrident entity, final @NonNull ThrownTridentRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.isFoil();
    }
}
