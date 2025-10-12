package sircow.preservedinferno.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", shift = At.Shift.AFTER))
    private <S extends EntityRenderState> void preserved_inferno$injectFlameLayer(
            S renderState, CameraRenderState cameraRenderState, double camX, double camY, double camZ, PoseStack poseStack, SubmitNodeCollector nodeCollector, CallbackInfo ci
    ) {
        if (!(renderState instanceof AvatarRenderState playerRenderState)) return;
        if (Minecraft.getInstance().level == null) return;

        Entity entity = Minecraft.getInstance().level.getEntity(playerRenderState.id);
        if (!(entity instanceof AbstractClientPlayer player)) return;
        if (!(player instanceof HeatAccessor accessor)) return;
        if (accessor.preserved_inferno$getHeat() < 100 || player.isOnFire() || player.isCreative()) return;

        Camera camera = ((EntityRenderDispatcher)(Object)this).camera;
        Quaternionf cameraRotation = camera != null ? camera.rotation() : new Quaternionf();

        renderFlame(poseStack, nodeCollector, renderState, cameraRotation);
    }

    @Unique
    private void renderFlame(PoseStack poseStack, SubmitNodeCollector nodeCollector, EntityRenderState renderState, Quaternionf cameraRotation) {
        AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
        TextureAtlasSprite fire0 = atlasManager.get(ModelBakery.FIRE_0);
        TextureAtlasSprite fire1 = atlasManager.get(ModelBakery.FIRE_1);

        float f = renderState.boundingBoxWidth * 1.4F;
        float g = 0.5F;
        float h = 0.0F;
        float i = renderState.boundingBoxHeight / f;
        float j = 0.0F;
        float k = 0.0F;

        poseStack.pushPose();
        poseStack.scale(f, f, f);
        poseStack.mulPose(cameraRotation);
        poseStack.translate(0.0F, 0.0F, 0.3F - (int)i * 0.02F);

        int l = 0;

        nodeCollector.submitCustomGeometry(poseStack, RenderType.cutout(), (pose, vertexConsumer) -> {
            float localG = g;
            float localI = i;
            float localJ = j;
            float localK = k;
            int localL = l;

            while (localI > 0.0F) {
                TextureAtlasSprite sprite = localL % 2 == 0 ? fire0 : fire1;
                float u0 = sprite.getU0();
                float v0 = sprite.getV0();
                float u1 = sprite.getU1();
                float v1 = sprite.getV1();

                if (localL / 2 % 2 == 0) {
                    float tmp = u1;
                    u1 = u0;
                    u0 = tmp;
                }

                addFireVertex(pose, vertexConsumer, -localG, -localJ, localK, u1, v1);
                addFireVertex(pose, vertexConsumer, localG, -localJ, localK, u0, v1);
                addFireVertex(pose, vertexConsumer, localG, 1.4F - localJ, localK, u0, v0);
                addFireVertex(pose, vertexConsumer, -localG, 1.4F - localJ, localK, u1, v0);

                localI -= 0.45F;
                localJ -= 0.45F;
                localG *= 0.9F;
                localK -= 0.03F;
                localL++;
            }
        });

        poseStack.popPose();
    }


    @Unique
    private static void addFireVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v) {
        buffer.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setUv1(0, 10).setLight(240).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
