package com.sakalti.fabricmachina.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.registry.ModBlockEntities;
import com.sakalti.fabricmachina.energy.MachinaEnergy;

public class ThermalGeneratorBlock extends Block implements BlockEntityProvider {
    public ThermalGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThermalGeneratorEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (w, p, s, be) -> {
            if (be instanceof ThermalGeneratorEntity generator) generator.tick();
        };
    }

    public static class ThermalGeneratorEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
        private final MachinaEnergy energy = new MachinaEnergy(4500, 16, 0);
        private boolean isDamaged = false;
        private int iceCooldown = 0;

        public ThermalGeneratorEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.THERMAL_GENERATOR, pos, state);
        }

        public void tick() {
            if (world == null || world.isClient) return;

            // マグマが隣接している場合、エネルギーを生成
            if (world.getBlockState(pos.offset(Direction.DOWN)).isOf(Blocks.LAVA)) {
                if (!isDamaged) {
                    energy.produceRf(450);
                } else {
                    energy.produceRf(45);
                }
            }

            // 氷の消費と修理処理
            if (iceCooldown > 0) {
                iceCooldown--;
            }

            if (isDamaged && world.getTime() % 24000 == 0) {
                // 2日ごとに氷を使って修復する
                if (hasIceInInventory()) {
                    consumeIce();
                    isDamaged = false;
                    iceCooldown = 100;  // クールダウン時間
                    markDirty();
                }
            }
        }

        private boolean hasIceInInventory() {
            // インベントリに氷があるか確認
            return inventory.get(0).getItem() == Items.ICE;
        }

        private void consumeIce() {
            // 氷をインベントリから消費
            ItemStack ice = inventory.get(0);
            ice.decrement(1);
            inventory.set(0, ice);
        }

        @Override
        public void writeNbt(NbtCompound nbt) {
            super.writeNbt(nbt);
            nbt.putBoolean("isDamaged", isDamaged);
        }

        @Override
        public void readNbt(NbtCompound nbt) {
            super.readNbt(nbt);
            isDamaged = nbt.getBoolean("isDamaged");
        }

        @Override
        public Text getDisplayName() {
            return Text.literal("Thermal Generator");
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            return new ThermalGeneratorScreenHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
        }

        @Override
        public DefaultedList<ItemStack> getItems() {
            return DefaultedList.ofSize(1, ItemStack.EMPTY);
        }

        public MachinaEnergy getEnergy() {
            return energy;
        }
    }
}
