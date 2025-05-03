package com.sakalti.fabricmachina.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.block.CrusherBlock;
import com.sakalti.fabricmachina.block.CrusherBlock.CrusherEntity;

public class CrusherScreenHandler extends ScreenHandler {
    private final CrusherEntity crusherEntity;
    private final Inventory inventory;

    public CrusherScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.CRUSHER, syncId);
        this.inventory = new SimpleInventory(2); // 2スロット（入力と出力）
        context.run((world, pos) -> {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CrusherEntity) {
                this.crusherEntity = (CrusherEntity) entity;
            } else {
                throw new IllegalStateException("Expected Crusher block entity");
            }
        });

        // 入力・出力スロットの配置
        this.addSlot(new Slot(inventory, 0, 56, 17)); // 入力スロット
        this.addSlot(new Slot(inventory, 1, 116, 17)); // 出力スロット

        // プレイヤーインベントリの配置
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        // プレイヤーインベントリのホットバー
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.crusherEntity.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack = slot.getStack();
            stack = itemStack.copy();
            if (index == 1) { // 出力スロット
                if (!this.insertItem(itemStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onStackChanged(itemStack, stack);
            } else if (index != 0) { // 入力スロット
                if (!this.insertItem(itemStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack, 2, 38, false)) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public CrusherEntity getCrusherEntity() {
        return crusherEntity;
    }
}
