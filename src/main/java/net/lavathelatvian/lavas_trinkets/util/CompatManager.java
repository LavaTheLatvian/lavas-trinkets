package net.lavathelatvian.lavas_trinkets.util;

import net.neoforged.fml.ModList;

public class CompatManager {

    public static boolean isBetterCombatLoaded() {
        return ModList.get().isLoaded("bettercombat");
    }

}
