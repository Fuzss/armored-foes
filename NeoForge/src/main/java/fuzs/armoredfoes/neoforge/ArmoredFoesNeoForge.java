package fuzs.armoredfoes.neoforge;

import fuzs.armoredfoes.common.ArmoredFoes;
import fuzs.armoredfoes.common.data.loot.ModEquipmentLootProvider;
import fuzs.armoredfoes.common.data.tags.ModEntityTagsProvider;
import fuzs.armoredfoes.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(ArmoredFoes.MOD_ID)
public class ArmoredFoesNeoForge {

    public ArmoredFoesNeoForge() {
        ModConstructor.construct(ArmoredFoes.MOD_ID, ArmoredFoes::new);
        DataProviderHelper.registerDataProviders(ArmoredFoes.MOD_ID,
                ModRegistry.REGISTRY_SET_BUILDER,
                ModEquipmentLootProvider::new,
                ModEntityTagsProvider::new);
    }
}
