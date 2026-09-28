package fuzs.armoredfoes.neoforge;

import fuzs.armoredfoes.common.ArmoredFoes;
import fuzs.armoredfoes.common.data.loot.ModEquipmentLootProvider;
import fuzs.armoredfoes.common.data.tags.ModEntityTagsProvider;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.fml.common.Mod;

@Mod(ArmoredFoes.MOD_ID)
public class ArmoredFoesNeoForge {

    public ArmoredFoesNeoForge() {
        ModConstructor.construct(ArmoredFoes.MOD_ID, ArmoredFoes::new);
        DataProviderBuilder.of(ArmoredFoes.MOD_ID)
                .addLootProvider(ModEquipmentLootProvider::new, LootContextParamSets.EQUIPMENT)
                .addProvider(ModEntityTagsProvider::new);
    }
}
