package dev.tauri.rsjukeboxes.screen.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class GuiHelper {

    public static GuiGraphics graphics = null;

    // 1.21 : le Tesselator renvoie un MeshData (null si rien n'a ete dessine)
    private static void drawMesh(BufferBuilder builder) {
        MeshData mesh = builder.build();
        if (mesh != null) BufferUploader.drawWithShader(mesh);
    }

    public static void drawTexturedRectScaled(int xLeftCoord, int yBottomCoord, TextureAtlasSprite textureSprite, int maxWidth, int maxHeight, float scaleHeight) {
        maxHeight = (int) (maxHeight * scaleHeight);
        yBottomCoord -= maxHeight;

        drawTexturedRect(xLeftCoord, yBottomCoord, textureSprite, maxWidth, maxHeight, scaleHeight);
    }

    public static void drawTexturedRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int maxWidth, int maxHeight, float scaleHeight) {
        double v = textureSprite.getV1() - textureSprite.getV0();
        v *= (1 - scaleHeight);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, textureSprite.atlasLocation());
        Matrix4f matrix = graphics.pose().last().pose();

        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, (xCoord), (yCoord + maxHeight), 0).setUv(textureSprite.getU0(), textureSprite.getV1());
        bufferbuilder.addVertex(matrix, (xCoord + maxWidth), (yCoord + maxHeight), 0).setUv(textureSprite.getU1(), textureSprite.getV1());
        bufferbuilder.addVertex(matrix, (xCoord + maxWidth), (yCoord), 0).setUv(textureSprite.getU1(), (float) (textureSprite.getV0() + v));
        bufferbuilder.addVertex(matrix, (xCoord), (yCoord), 0).setUv(textureSprite.getU0(), (float) (textureSprite.getV0() + v));
        drawMesh(bufferbuilder);
    }

    public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, (x), (y + height), 0).setUv(((float) (textureX) * 0.00390625F), ((float) (textureY + height) * 0.00390625F));
        bufferbuilder.addVertex(matrix, (x + width), (y + height), 0).setUv(((float) (textureX + width) * 0.00390625F), ((float) (textureY + height) * 0.00390625F));
        bufferbuilder.addVertex(matrix, (x + width), (y), 0).setUv(((float) (textureX + width) * 0.00390625F), ((float) (textureY) * 0.00390625F));
        bufferbuilder.addVertex(matrix, (x), (y), 0).setUv(((float) (textureX) * 0.00390625F), ((float) (textureY) * 0.00390625F));
        drawMesh(bufferbuilder);
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        drawTexturedModalRect(x, y, u, v, width, height, 0);
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
        float uScale = 0.00390625F;
        float vScale = 0.00390625F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, (float) x, (float) (y + height), zLevel).setUv((float) u * 0.00390625F, (float) (v + height) * 0.00390625F);
        bufferbuilder.addVertex(matrix, (float) (x + width), (float) (y + height), zLevel).setUv((float) (u + width) * 0.00390625F, (float) (v + height) * 0.00390625F);
        bufferbuilder.addVertex(matrix, (float) (x + width), (float) y, zLevel).setUv((float) (u + width) * 0.00390625F, (float) v * 0.00390625F);
        bufferbuilder.addVertex(matrix, (float) x, (float) y, zLevel).setUv((float) u * 0.00390625F, (float) v * 0.00390625F);
        drawMesh(bufferbuilder);
    }

    public static boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
        return pointX >= rectX && pointY >= rectY && pointX < (rectX + rectWidth) && pointY < (rectY + rectHeight);
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, x, y + height, 0.0f).setUv((u * f), ((v + (float) height) * f1));
        bufferbuilder.addVertex(matrix, (x + width), (y + height), 0.0f).setUv(((u + (float) width) * f), ((v + (float) height) * f1));
        bufferbuilder.addVertex(matrix, (x + width), y, 0.0f).setUv(((u + (float) width) * f), (v * f1));
        bufferbuilder.addVertex(matrix, x, y, 0.0f).setUv((u * f), (v * f1));
        drawMesh(bufferbuilder);
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Matrix4f matrix = graphics.pose().last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, x, (y + height), 0.0f).setUv((u * f), ((v + (float) vHeight) * f1));
        bufferbuilder.addVertex(matrix, (x + width), (y + height), 0.0f).setUv(((u + (float) uWidth) * f), ((v + (float) vHeight) * f1));
        bufferbuilder.addVertex(matrix, (x + width), y, 0.0f).setUv(((u + (float) uWidth) * f), (v * f1));
        bufferbuilder.addVertex(matrix, x, y, 0.0f).setUv((u * f), (v * f1));
        drawMesh(bufferbuilder);
    }

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Matrix4f matrix = graphics.pose().last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        //RenderSystem.setShaderColor(f, f1, f2, f3);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.addVertex(matrix, left, bottom, 0.0f).setColor(f, f1, f2, f3);
        bufferbuilder.addVertex(matrix, right, bottom, 0.0f).setColor(f, f1, f2, f3);
        bufferbuilder.addVertex(matrix, right, top, 0.0f).setColor(f, f1, f2, f3);
        bufferbuilder.addVertex(matrix, left, top, 0.0f).setColor(f, f1, f2, f3);
        drawMesh(bufferbuilder);
        RenderSystem.disableBlend();
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        drawGradientRect(graphics, left, top, right, bottom, startColor, endColor);
    }

    public static void drawGradientRect(GuiGraphics graphics, int left, int top, int right, int bottom, int startColor, int endColor) {
        drawGradientRect(graphics.pose().last().pose(), 0, left, top, right, bottom, startColor, endColor);
    }

    public static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.addVertex(mat, right, top, zLevel).setColor(startRed, startGreen, startBlue, startAlpha);
        bufferbuilder.addVertex(mat, left, top, zLevel).setColor(startRed, startGreen, startBlue, startAlpha);
        bufferbuilder.addVertex(mat, left, bottom, zLevel).setColor(endRed, endGreen, endBlue, endAlpha);
        bufferbuilder.addVertex(mat, right, bottom, zLevel).setColor(endRed, endGreen, endBlue, endAlpha);
        drawMesh(bufferbuilder);

        RenderSystem.disableBlend();
    }

    public static void drawTexturedRectWithShadow(int x, int y, int xOffset, int yOffset, int xSize, int ySize, float color) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(color, color, color, 1);
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, xSize, ySize, xSize, ySize);

        RenderSystem.setShaderColor(color, color, color, 0.2f);
        drawModalRectWithCustomSizedTexture(x + xOffset, y + yOffset, 0, 0, xSize, ySize, xSize, ySize);
        RenderSystem.disableBlend();

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static void drawHoveringText(GuiGraphics graphics, Font font, List<String> textLines, int mouseX, int mouseY) {
        List<Component> components = new ArrayList<>();
        for (String s : textLines)
            components.add(Component.literal(s));
        graphics.renderTooltip(font, components, Optional.empty(), mouseX, mouseY);
    }


    public static void renderTexturedRect(GuiGraphics graphics, ResourceLocation texture, int x, int y, int z, int u, int v, int width, int height, int texWidth, int texHeight) {
        graphics.blit(texture, x, y, z, u, v, texWidth, texHeight, width, height);
    }


    public static void blit(GuiGraphics graphics, ResourceLocation tex, int x, int y, int sizeX, int sizeY, float p_282285_, float p_283199_, int p_282186_, int p_282322_, int p_282481_, int p_281887_) {
        graphics.blit(tex, x, y, sizeX, sizeY, p_282285_, p_283199_, p_282186_, p_282322_, p_282481_, p_281887_);
    }

    public static void renderTransparentBackground(GuiGraphics graphics, Screen screen) {
        graphics.fillGradient(0, 0, screen.width, screen.height, -1072689136, -804253680);
    }

    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite) {
        drawTiledSprite(xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, 16, 16, 0);
    }

    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel) {
        drawTiledSprite(xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth, textureHeight, zLevel, true);
    }

    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel, boolean blend) {
        if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, sprite.atlasLocation());
        int xTileCount = desiredWidth / textureWidth;
        int xRemainder = desiredWidth - (xTileCount * textureWidth);
        int yTileCount = desiredHeight / textureHeight;
        int yRemainder = desiredHeight - (yTileCount * textureHeight);
        int yStart = yPosition + yOffset;
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();
        float uDif = uMax - uMin;
        float vDif = vMax - vMin;
        if (blend) {
            RenderSystem.enableBlend();
        }
        //Note: We still use the tesselator as that is what GuiGraphics#innerBlit does
        BufferBuilder vertexBuffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix4f = graphics.pose().last().pose();
        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            int width = (xTile == xTileCount) ? xRemainder : textureWidth;
            if (width == 0) {
                break;
            }
            int x = xPosition + (xTile * textureWidth);
            int maskRight = textureWidth - width;
            int shiftedX = x + textureWidth - maskRight;
            float uLocalDif = uDif * maskRight / textureWidth;
            float uLocalMin;
            float uLocalMax;
            uLocalMin = uMin + uLocalDif;
            uLocalMax = uMax;
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int height = (yTile == yTileCount) ? yRemainder : textureHeight;
                if (height == 0) {
                    //Note: We don't want to fully break out because our height will be zero if we are looking to
                    // draw the remainder, but there is no remainder as it divided evenly
                    break;
                }
                int y = yStart - ((yTile + 1) * textureHeight);
                int maskTop = textureHeight - height;
                float vLocalDif = vDif * maskTop / textureHeight;
                float vLocalMin;
                float vLocalMax;
                vLocalMin = vMin;
                vLocalMax = vMax - vLocalDif;
                vertexBuffer.addVertex(matrix4f, x, y + textureHeight, zLevel).setUv(uLocalMin, vLocalMax);
                vertexBuffer.addVertex(matrix4f, shiftedX, y + textureHeight, zLevel).setUv(uLocalMax, vLocalMax);
                vertexBuffer.addVertex(matrix4f, shiftedX, y + maskTop, zLevel).setUv(uLocalMax, vLocalMin);
                vertexBuffer.addVertex(matrix4f, x, y + maskTop, zLevel).setUv(uLocalMin, vLocalMin);
            }
        }
        drawMesh(vertexBuffer);
        if (blend) {
            RenderSystem.disableBlend();
        }
    }
}
