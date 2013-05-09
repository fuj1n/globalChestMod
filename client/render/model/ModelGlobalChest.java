package fuj1n.globalLinkMod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGlobalChest extends ModelBase {
	/** The chest lid in the chest's model. */
	public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);

	/** The model of the bottom of the chest. */
	public ModelRenderer chestBelow;

	/** The chest's knob in the chest model. */
	public ModelRenderer chestKnob;

	/** The nether star on the chest model. */
	public ModelRenderer netherStar;

	/** Nether star rotation points. **/
	public int netherStarRotX;
	public int netherStarRotY;
	public int netherStarRotZ;
	public int ticks = 0;

	public ModelGlobalChest() {
		chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
		chestLid.rotationPointX = 1.0F;
		chestLid.rotationPointY = 7.0F;
		chestLid.rotationPointZ = 15.0F;
		chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
		chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
		chestKnob.rotationPointX = 8.0F;
		chestKnob.rotationPointY = 7.0F;
		chestKnob.rotationPointZ = 15.0F;
		chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
		chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
		chestBelow.rotationPointX = 1.0F;
		chestBelow.rotationPointY = 6.0F;
		chestBelow.rotationPointZ = 1.0F;
		// this.netherStar = (new ModelRenderer(this, 50,
		// -2)).setTextureSize(64, 64);

		// this.netherStar.addBox(0.0F, -15F, -15F, 12, 15, 1, 1.0F);
		// this.netherStar.rotationPointX = 8.0F;
		// this.netherStar.rotationPointY = 8.0F;
		// this.netherStar.rotationPointZ = 8.0F;
	}

	public void netherStarRotation() {
		netherStarRotX++;
		netherStarRotY++;
		netherStar.rotateAngleX = netherStarRotX / 80;
		netherStar.rotateAngleY = netherStarRotZ / 80;
		ticks = 0;
	}

	/**
	 * This method renders out all parts of the chest model.
	 */
	public void renderAll() {
		// netherStarRotation();
		chestKnob.rotateAngleX = chestLid.rotateAngleX;
		chestLid.render(0.0625F);
		chestKnob.render(0.0625F);
		chestBelow.render(0.0625F);
		// this.netherStar.render(0.0625F);
	}
}
