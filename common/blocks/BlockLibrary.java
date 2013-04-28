package fuj1n.globalChestMod.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fuj1n.globalChestMod.common.tileentity.TileEntityLibrary;

public class BlockLibrary extends BlockContainer {

	public BlockLibrary(int par1) {
		super(par1, Material.wood);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityLibrary();
	}

}
