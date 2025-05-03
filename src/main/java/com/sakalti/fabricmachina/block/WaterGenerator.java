package com.sakalti.fabricmachina.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.entity.BlockEntityTicker;

import com.sakalti.fabricmachina.energy.MachinaEnergy;

public class WaterGeneratorBlock extends Block implements BlockEntityProvider {
    public WaterGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratorEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (w, p, s, be) -> {
            if (be instanceof GeneratorEntity entity) entity.tick();
        };
    }

    public static class GeneratorEntity extends BlockEntity {
        private final MachinaEnergy energy = new MachinaEnergy(10000, 67, 0);
        private int tickCount = 0;

        public GeneratorEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.WATER_GENERATOR, pos, state);
        }

        public void tick() {
            if (world == null || world.isClient) return;

            tickCount++;
            if (tickCount % 20 == 0 && isAdjacentToWater()) {
                energy.addRf(67); // 1秒あたり67rf
                energy.setTq(10);
            }
        }

        private boolean isAdjacentToWater() {
            for (var dir : Direction.values()) {
                if (world.getBlockState(pos.offset(dir)).getMaterial().isLiquid()) {
                    return true;
                }
            }
            return false;
        }

        public MachinaEnergy getEnergy() {
            return energy;
        }
    }
}
