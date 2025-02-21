package net.mehvahdjukaar.moonlight.core.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.ExpandedMapData;
import net.mehvahdjukaar.moonlight.api.map.client.MapDecorationClientHandler;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapRenderer.class)
public abstract class MapItemRendererMixin {


    @Inject(method = "render", at = @At("RETURN"))
    private void render(PoseStack poseStack, MultiBufferSource buffer, int mapId, MapItemSavedData mapData, boolean isOnFrame, int light, CallbackInfo ci) {
        if (mapData instanceof ExpandedMapData data) {
            int index = data.getVanillaDecorationSize();
            for (CustomMapDecoration decoration : data.getCustomDecorations().values()) {

                if (MapDecorationClientHandler.render(decoration, poseStack, buffer, mapData, isOnFrame, light, index))
                    index++;
            }
        }
    }

}