package fuj1n.globalLinkMod.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import fuj1n.globalLinkMod.common.tileentity.TileEntityLibrary;

public class ContainerBookLibrary extends Container {

	public InventoryLibraryDecoration inventoryDecoration;
	/* TODO */public InventoryGlobalChest inventoryGlobal;

	public TileEntityLibrary tileEntity;

	public ContainerBookLibrary(EntityPlayer par1EntityPlayer, TileEntityLibrary par2Library) {
		tileEntity = par2Library;
		inventoryDecoration = par2Library.decoInventory;
		addInventorySlots();
		bindPlayerInventory(par1EntityPlayer.inventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileEntity.isUseableByPlayer(entityplayer);
	}
	
	protected void addInventorySlots(){
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 7; j++) {
				addSlotToContainer(new Slot(inventoryDecoration, j + i * 9 + 9, 8 + j * 18 + 119, 84 + i * 18 - 50));
			}
		}
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18 - 60, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18 - 60, 142));
		}
	}

}
