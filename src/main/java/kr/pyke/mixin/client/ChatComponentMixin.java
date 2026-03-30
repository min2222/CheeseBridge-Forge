package kr.pyke.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.RenderType;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 0))
    private void handleBackgroundFill(GuiGraphics instance, int x1, int y1, int x2, int y2, int color, @Local GuiMessage.Line line) {
        GuiMessageTag tag = line.tag();

        if (tag != null) {
            int alpha = (color >> 24) & 0xFF;
            int startColor = (alpha << 24) | tag.indicatorColor();

            int middleX = x1 + (int)((x2 - x1) * 0.4);
            instance.fill(x1, y1, middleX, y2, startColor);
            this.drawHorizontalGradient(instance, middleX, y1, x2, y2, startColor);
        } else {
            instance.fill(x1, y1, x2, y2, color);
        }
    }

    @Unique
    private void drawHorizontalGradient(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int startColor) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        VertexConsumer consumer = guiGraphics.bufferSource().getBuffer(RenderType.gui());
        Matrix4f matrix = guiGraphics.pose().last().pose();

        float a1 = (float)(startColor >> 24 & 255) / 255.0F;
        float r1 = (float)(startColor >> 16 & 255) / 255.0F;
        float g1 = (float)(startColor >> 8 & 255) / 255.0F;
        float b1 = (float)(startColor & 255) / 255.0F;

        float a2 = 0.0f;
        float r2 = 0.0f;
        float g2 = 0.0f;
        float b2 = 0.0f;

        consumer.vertex(matrix, (float)x1, (float)y1, 0.0F).color(r1, g1, b1, a1).endVertex();
        consumer.vertex(matrix, (float)x1, (float)y2, 0.0F).color(r1, g1, b1, a1).endVertex();
        consumer.vertex(matrix, (float)x2, (float)y2, 0.0F).color(r2, g2, b2, a2).endVertex();
        consumer.vertex(matrix, (float)x2, (float)y1, 0.0F).color(r2, g2, b2, a2).endVertex();

        guiGraphics.flush();

        RenderSystem.disableBlend();
    }
}