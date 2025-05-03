package com.sakalti.fabricmachina.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;

public class CrusherScreen extends HandledScreen<CrusherScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("fabricmachina", "textures/gui/crusher.png");

    public CrusherScreen(CrusherScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166;
        this.backgroundWidth = 176;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // 進行状況バーの描画
        int progress = this.handler.getCrusherEntity().progress;
        this.drawTexture(matrices, this.x + 56, this.y + 35, 176, 0, progress / 2, 16);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, 8, 6);
    }
}
