package com.sakalti.fabricmachina.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.energy.MachinaEnergy;

public class PowerPipeBlock extends Block implements BlockEntityProvider {
    public PowerPipeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (w, p, s, be) -> {
            if (be instanceof PipeEntity pipe) pipe.tick();
        };
    }

    public static class PipeEntity extends BlockEntity {
        public PipeEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.POWER_PIPE, pos, state);
        }

        public void tick() {
            if (world == null || world.isClient) return;

            BlockEntity src = world.getBlockEntity(pos.offset(Direction.WEST));
            BlockEntity dst = world.getBlockEntity(pos.offset(Direction.EAST));

            if (src instanceof WaterGeneratorBlock.GeneratorEntity gen && dst instanceof CrusherBlock.CrusherEntity crusher) {
                MachinaEnergy g = gen.getEnergy();
                MachinaEnergy c = crusher.getEnergy();

                if (g.getRf() > 0 && g.getTq() >= 5) {
                    int moved = c.addRf(g.getRf());
                    g.consumeRf(moved);
                    c.setTq(g.getTq());
                }
            }
        }
    }
}
