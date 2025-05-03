package com.sakalti.fabricmachina.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.energy.MachinaEnergy;
import com.sakalti.fabricmachina.registry.ModBlockEntities;

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
        private final MachinaEnergy energy = new MachinaEnergy(0, 16, 0);  // 初期のエネルギー値

        public PipeEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.POWER_PIPE, pos, state);
        }

        public void tick() {
            if (world == null || world.isClient) return;

            // 全方向に接続を確認し、エネルギー転送を行う
            for (Direction direction : Direction.values()) {
                BlockEntity neighbor = world.getBlockEntity(pos.offset(direction));

                if (neighbor != null) {
                    // エネルギー転送元と先のブロックを特定
                    if (neighbor instanceof WaterGeneratorBlock.GeneratorEntity gen) {
                        MachinaEnergy genEnergy = gen.getEnergy();
                        if (genEnergy.getRf() > 0 && genEnergy.getTq() >= 5) {
                            // エネルギーを転送
                            int moved = this.addEnergy(genEnergy.getRf(), genEnergy.getTq());
                            genEnergy.consumeRf(moved);
                            genEnergy.consumeTq(moved); // TQも消費
                        }
                    } else if (neighbor instanceof CrusherBlock.CrusherEntity crusher) {
                        MachinaEnergy crusherEnergy = crusher.getEnergy();
                        if (energy.getRf() > 0) {
                            // パイプのエネルギーを転送
                            int moved = crusherEnergy.addRf(energy.getRf());
                            int movedTq = crusherEnergy.addTq(energy.getTq());
                            energy.consumeRf(moved);
                            energy.consumeTq(movedTq);  // TQも消費
                        }
                    }
                }
            }
        }

        private int addEnergy(int rf, int tq) {
            // エネルギーをパイプに追加
            int maxRf = 5120; // パイプの最大RF量
            int currentRf = energy.getRf();
            int availableSpaceRf = maxRf - currentRf;

            int maxTq = 18; // パイプの最大TQ量
            int currentTq = energy.getTq();
            int availableSpaceTq = maxTq - currentTq;

            int transferredRf = Math.min(rf, availableSpaceRf);
            int transferredTq = Math.min(tq, availableSpaceTq);

            energy.produceRf(transferredRf);
            energy.produceTq(transferredTq);
            return transferredRf; // RFだけ返す
        }
    }
}
