package com.sakalti.fabricmachina.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.registry.ModBlockEntities;
import com.sakalti.fabricmachina.screen.CrusherScreenHandler;
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

    public static class CrusherEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
        private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
        private final MachinaEnergy energy = new MachinaEnergy(3000, 100, 0);
        private int progress = 0;

        public CrusherEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.CRUSHER, pos, state);
        }

        public void tick() {
            if (world == null || world.isClient) return;

            if (energy.getTq() < 5 || energy.getRf() < 300) return;

            ItemStack input = inventory.get(0);
            if (!input.isEmpty() && inventory.get(1).isEmpty()) {
                progress++;
                if (progress >= 200) {
                    if (energy.consumeRf(300)) {
                        inventory.set(0, ItemStack.EMPTY);
                        inventory.set(1, new ItemStack(input.getItem(), 2));
                        markDirty();
                    }
                    progress = 0;
                }
            } else {
                progress = 0;
            }
        }

        @Override
        public void writeNbt(NbtCompound nbt) {
            super.writeNbt(nbt);
            Inventories.writeNbt(nbt, inventory);
        }

        @Override
        public void readNbt(NbtCompound nbt) {
            super.readNbt(nbt);
            Inventories.readNbt(nbt, inventory);
        }

        @Override
        public Text getDisplayName() {
            return Text.literal("Crusher");
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            return new CrusherScreenHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
        }

        @Override
        public DefaultedList<ItemStack> getItems() {
            return inventory;
        }

        public MachinaEnergy getEnergy() {
            return energy;
        }
    }
}
