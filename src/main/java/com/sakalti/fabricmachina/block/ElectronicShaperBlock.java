package com.sakalti.fabricmachina.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.GameEvent;

import com.sakalti.fabricmachina.energy.MachinaEnergy;
import com.sakalti.fabricmachina.registry.ModBlockEntities;

public class ElectronicShaperBlock extends Block implements BlockEntityProvider {
    public ElectronicShaperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ElectronicShaperBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (w, p, s, be) -> {
            if (be instanceof ElectronicShaperBlockEntity shaper) {
                shaper.tick();
            }
        };
    }
}
