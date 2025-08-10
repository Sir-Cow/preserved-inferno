package sircow.preservedinferno.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.entity.custom.ThrownCopperTrident;

public class ThrownCopperTridentRenderer extends EntityRenderer<ThrownCopperTrident, ThrownTridentRenderState> {
    public static final ResourceLocation TRIDENT_LOCATION = Constants.id("textures/entity/copper_trident.png");
    public static final ModelLayerLocation COPPER_TRIDENT = new ModelLayerLocation(Constants.id("copper_trident"), "main");
    private final CopperTridentModel model;

    public ThrownCopperTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CopperTridentModel(context.bakeLayer(COPPER_TRIDENT));
    }

    public void render(ThrownTridentRenderState thrownTridentRenderState, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(thrownTridentRenderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(thrownTridentRenderState.xRot + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(multiBufferSource, this.model.renderType(TRIDENT_LOCATION), false, thrownTridentRenderState.isFoil);
        this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(thrownTridentRenderState, poseStack, multiBufferSource, i);
    }

    public @NotNull ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    public void extractRenderState(@NotNull ThrownCopperTrident thrownTrident, @NotNull ThrownTridentRenderState thrownTridentRenderState, float f) {
        super.extractRenderState(thrownTrident, thrownTridentRenderState, f);
        thrownTridentRenderState.yRot = thrownTrident.getYRot(f);
        thrownTridentRenderState.xRot = thrownTrident.getXRot(f);
        thrownTridentRenderState.isFoil = thrownTrident.isFoil();
    }
}
