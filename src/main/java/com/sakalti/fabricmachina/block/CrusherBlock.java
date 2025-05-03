package com.sakalti.fabricmachina.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.Direction;
import net.minecraft.text.Text;

import com.sakalti.fabricmachina.energy.MachinaEnergy;

public class CrusherBlock extends Block implements BlockEntityProvider {
    public CrusherBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrusherEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (w, p, s, be) -> {
            if (be instanceof CrusherEntity crusher) crusher.tick();
        };
    }

    public static class CrusherEntity extends BlockEntity {
        private final MachinaEnergy energy = new MachinaEnergy(3000, 100, 0);
        private int progress = 0;
        private ItemStack input = ItemStack.EMPTY;
        private ItemStack output = ItemStack.EMPTY;

        public CrusherEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.CRUSHER, pos, state);
        }

        public void tick() {
            if (world == null || world.isClient) return;

            // 仮のチェック: 常にTQ5以上供給されていると仮定
            if (energy.getTq() < 5 || energy.getRf() < 300) return;

            if (!input.isEmpty()) {
                progress++;
                if (progress >= 200) {
                    progress = 0;
                    if (energy.consumeRf(300)) {
                        output = new ItemStack(input.getItem(), 2);
                        input = ItemStack.EMPTY;
                        markDirty();
                    }
                }
            }
        }

        public void insertItem(ItemStack stack) {
            if (input.isEmpty()) input = stack.copyWithCount(1);
        }

        public ItemStack extractOutput() {
            ItemStack out = output;
            output = ItemStack.EMPTY;
            return out;
        }

        public MachinaEnergy getEnergy() {
            return energy;
        }
    }
}
