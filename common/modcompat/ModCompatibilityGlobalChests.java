package fuj1n.globalLinkMod.common.modcompat;

import java.util.ArrayList;
import java.util.logging.Level;

import cpw.mods.fml.common.Loader;
import fuj1n.globalLinkMod.GlobalChests;

public class ModCompatibilityGlobalChests {

	public static ArrayList<CompatModule> modulesList;

	public ModCompatibilityGlobalChests() {
		modulesList = new ArrayList<CompatModule>();
		getBuiltInCompatMods();
	}

	public void postInit() {
		callModCompatibilityInitializers();
	}

	/**
	 * Gets an instance of CompatModule from the name.
	 * 
	 * @param modName
	 *            The name of the mod as defined by CompatModule.MODNAME
	 * @return An instance of CompatModule matching the mod name. Returns null
	 *         if none found.
	 */
	public static CompatModule getModuleByName(String modName) {
		for (int i = 0; i < modulesList.size(); i++) {
			if (modulesList.get(i).MODNAME.equals(modName)) {
				return modulesList.get(i);
			}
		}
		return null;
	}

	public void getBuiltInCompatMods() {
		if (Loader.isModLoaded("BuildCraft|Core")) {
			modulesList.add(new CompatModuleBC());
		}
		if (Loader.isModLoaded("IC2")) {
			modulesList.add(new CompatModuleIC2());
		}
	}

	public void callModCompatibilityInitializers() {
		GlobalChests.log("Begin initializing compatibility modules.", Level.INFO);
		for (int i = 0; i < modulesList.size(); i++) {
			GlobalChests.log("Initializing a compatibility module for: " + modulesList.get(i).MODNAME + " v" + modulesList.get(i).MINMODVER, Level.INFO);
			modulesList.get(i).executeModCompat();
		}
	}
}
