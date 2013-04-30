package fuj1n.globalChestMod;

//May(should) be renamed to Global Links once this mod progresses.
import java.io.File;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import fuj1n.globalChestMod.client.CreativeTabGlobalChestMod;
import fuj1n.globalChestMod.client.gui.GuiHandler;
import fuj1n.globalChestMod.client.nbt.NBTData;
import fuj1n.globalChestMod.common.CommonProxyGlobalChests;
import fuj1n.globalChestMod.common.blocks.BlockGlobalChest;
import fuj1n.globalChestMod.common.blocks.BlockLibrary;
import fuj1n.globalChestMod.common.enchantment.EnchantmentRange;
import fuj1n.globalChestMod.common.inventory.ManagerGlobalChest;
import fuj1n.globalChestMod.common.items.ItemGlobalLink;
import fuj1n.globalChestMod.common.items.ItemMulti;
import fuj1n.globalChestMod.common.items.ItemPocketLink;
import fuj1n.globalChestMod.common.items.ItemVoidStone;
import fuj1n.globalChestMod.common.items.recipe.RecipeVoidStone;
import fuj1n.globalChestMod.common.tileentity.TileEntityGlobalChest;
import fuj1n.globalChestMod.lib.MultiItemReference;

@Mod(modid = "fuj1n.GlobalChests", name = CommonProxyGlobalChests.modName, version = CommonProxyGlobalChests.version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GlobalChests {

	public static NBTData[] chestNBT = { new NBTData(NBTData.filenames.NAME_GLOBALCHEST, "Creative"), new NBTData(NBTData.filenames.NAME_GLOBALCHEST, "Survival"), new NBTData(NBTData.filenames.NAME_GLOBALCHEST, "Adventure"), new NBTData(NBTData.filenames.NAME_GLOBALCHEST, "Misc") };

	public static NBTTagCompound globalChestCreative;
	public static NBTTagCompound globalChestSurvival;
	public static NBTTagCompound globalChestAdventure;
	public static NBTTagCompound globalChestMisc;

	@SidedProxy(clientSide = "fuj1n.globalChestMod.client.ClientProxyGlobalChests", serverSide = "fuj1n.globalChestMod.common.CommonProxyGlobalChests")
	public static CommonProxyGlobalChests proxy;
	public static Configuration config;

	public static CreativeTabs creativeTabGlobalChest;

	// Block IDs
	public static int globalChestId = 2564;
	public static int bookLibraryId = 2565;

	// Item IDs
	public static int globalLinkId = 6328;
	public static int voidStoneId = 6329;
	public static int pocketGlobalChestId = 6330;
	public static int itemMultiId = 6331;

	// Enchantment IDs
	public static int rangeEnchantmentId = 0;

	// Misc config
	public static int maxGlobalChestPrice = 4096;
	public static int maxPocketLinkRange = 16;

	// Blocks
	public static Block globalChest;
	public static Block bookLibrary;

	// Items
	public static Item globalLink;
	public static Item voidStone;
	public static Item pocketLink;
	public static Item multiItem;

	public static File modLocation;

	// Enchantments
	public static Enchantment enchantmentRange;

	// Global Chest Manager
	public static ManagerGlobalChest globalChestManager;

	// Enum
	public static EnumEnchantmentType pocketLinkEnchantment = EnumHelper.addEnchantmentType("pocketLinkEnchantment");

	@Instance("fuj1n.GlobalChests")
	public static GlobalChests instance;

	@PreInit
	public void PreInit(FMLPreInitializationEvent event) {
		modLocation = event.getSourceFile();
		proxy.PreInit();
		configPreInit();
		config = new Configuration(event.getSuggestedConfigurationFile(), true);
		config.load();
		// Blocks
		globalChestId = config.getBlock("Global Chest Id", globalChestId).getInt();
		bookLibraryId = config.getBlock("Library Id", bookLibraryId).getInt();
		// Items
		globalLinkId = config.getItem("Global Link Id", globalLinkId).getInt();
		voidStoneId = config.getItem("Void Stone Id", voidStoneId).getInt();
		pocketGlobalChestId = config.getItem("Pocket Link Id", pocketGlobalChestId).getInt();
		itemMultiId = config.getItem("Multi Item Id", itemMultiId).getInt();
		// Misc
		Property propRangeEnchantment = config.get("Enchantments", "Pocket Link Range Enchantment Id", rangeEnchantmentId);
		propRangeEnchantment.comment = "This enchantment ID is automatically allocated and you should not have to change it.";
		rangeEnchantmentId = propRangeEnchantment.getInt();
		maxGlobalChestPrice = config.get("Global Linking Configuration", "Max Total Content Weight", maxGlobalChestPrice).getInt();
		maxPocketLinkRange = config.get("Global Linking Configuration", "Max Pocket Link Range", maxPocketLinkRange).getInt();
		config.save();
	}

	public void configPreInit() {
		rangeEnchantmentId = getNextAvailableID(Enchantment.enchantmentsList);
	}

	@Init
	public void Init(FMLInitializationEvent event) {
		proxy.Init();
		initCreativeTab();
		initAllBlocks();
		initAllItems();
		initAllEnchantments();
		registerAllBlocks();
		mapAllTileEntities();
		addAllNames();
		removeUnwantedRecipes();
		addAllRecipes();
		globalChestManager = new ManagerGlobalChest(maxGlobalChestPrice);
		NetworkRegistry.instance().registerGuiHandler(instance, new GuiHandler());
	}

	@PostInit
	public void PostInit(FMLPostInitializationEvent event) {
		proxy.PostInit();
	}

	public void initAllBlocks() {
		globalChest = new BlockGlobalChest(globalChestId).setCreativeTab(creativeTabGlobalChest).setHardness(3.0F).setResistance(10F).setUnlocalizedName("fuj1n.globalChests.GlobalChest");
		bookLibrary = new BlockLibrary(bookLibraryId).setCreativeTab(creativeTabGlobalChest).setHardness(3.0F).setResistance(10F).setUnlocalizedName("fuj1n.globalChests.bookLibrary");
	}

	public void initAllItems() {
		globalLink = new ItemGlobalLink(globalLinkId).setCreativeTab(creativeTabGlobalChest).setUnlocalizedName("fuj1n.GlobalChests.globalLink");
		voidStone = new ItemVoidStone(voidStoneId).setCreativeTab(creativeTabGlobalChest).setUnlocalizedName("fuj1n.GlobalChests.voidStone");
		pocketLink = new ItemPocketLink(pocketGlobalChestId).setCreativeTab(creativeTabGlobalChest).setUnlocalizedName("fuj1n.GlobalChests.pocketLink");
		multiItem = new ItemMulti(itemMultiId).setCreativeTab(creativeTabGlobalChest).setUnlocalizedName("fuj1n.GlobalChests.multiItem");
	}

	public void initAllEnchantments() {
		enchantmentRange = new EnchantmentRange(rangeEnchantmentId, 1, true).setName("fuj1n.GlobalChests.enchanmentRange");
	}

	public int getNextAvailableID(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				return i;
			}
		}
		return 0;
	}

	public void registerAllBlocks() {
		GameRegistry.registerBlock(globalChest, "fuj1n.globalChests.GlobalChest");
		GameRegistry.registerBlock(bookLibrary, "fuj1n.globalChests.BookLibrary");
	}

	public void mapAllTileEntities() {
		GameRegistry.registerTileEntity(TileEntityGlobalChest.class, "fuj1n.GlobalChests.tileEntityGlobalChest");
	}

	public void addAllNames() {
		LanguageRegistry.addName(globalChest, "Global Chest");
		LanguageRegistry.addName(bookLibrary, "Library");
		LanguageRegistry.addName(globalLink, "Global Link");
		LanguageRegistry.addName(voidStone, "Void Stone");
		LanguageRegistry.addName(pocketLink, "Pocket Link");
		LanguageRegistry.addName(multiItem, "Unknown Multi Item");
		
		LanguageRegistry.instance().addStringLocalization("enchantment.fuj1n.GlobalChests.enchantmentRange", "Range");
		LanguageRegistry.instance().addStringLocalization("itemGroup.fuj1n.GlobalChests.creativeTab", "Global Chests Mod");
		
		for(int i = 0; i < MultiItemReference.NAMES.length; i++){
			LanguageRegistry.addName(new ItemStack(multiItem, 0, i), MultiItemReference.NAMES[i]);
		}
	}

	public void removeUnwantedRecipes(){
		for(int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++){
			IRecipe recipe = (IRecipe)CraftingManager.getInstance().getRecipeList().get(i);
			if(recipe.getRecipeOutput() != null){
				switch(recipe.getRecipeOutput().itemID){
				case 130:
					CraftingManager.getInstance().getRecipeList().remove(i);
				}
			}
		}
	}
	
	public void addAllRecipes() {
		GameRegistry.addRecipe(new ItemStack(globalLink, 1), new Object[] { "GEG", "ENE", "GEG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('N'), Item.netherStar });

		GameRegistry.addRecipe(new ItemStack(globalChest, 1), new Object[] { "BDB", "GEG", "ILI", Character.valueOf('B'), Block.blockSteel, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('L'), globalLink, Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Block.enderChest });

		GameRegistry.addRecipe(new ItemStack(voidStone, 1), new Object[] { "GOG", "ONO", "GOG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('O'), Block.obsidian, Character.valueOf('N'), Item.field_94584_bZ });
		
		GameRegistry.addRecipe(new ItemStack(multiItem, 1, MultiItemReference.VALUE_RETROPEARL), new Object[]{
			"GEG", "BGB", "GEG", Character.valueOf('G'), Block.glass, Character.valueOf('E'), Item.enderPearl, Character.valueOf('B'), Item.blazePowder
		});
		
		GameRegistry.addRecipe(new RecipeVoidStone());
		
		GameRegistry.addRecipe(new ItemStack(Block.enderChest, 1), new Object[] { "###", "#E#", "###", '#', Block.obsidian, 'E', new ItemStack(multiItem, 1, MultiItemReference.VALUE_RETROPEARL)});
	}

	public void initCreativeTab() {
		creativeTabGlobalChest = new CreativeTabGlobalChestMod("fuj1n.GlobalChests.creativeTab");
	}

	public static <var> void log(var s, Level level) {
		FMLLog.log(level, "[Global Chests] %s", s);
	}

}
