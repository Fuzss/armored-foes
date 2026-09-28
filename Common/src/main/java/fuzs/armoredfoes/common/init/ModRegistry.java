package fuzs.armoredfoes.common.init;

import fuzs.armoredfoes.common.ArmoredFoes;
import fuzs.puzzleslib.common.api.init.v3.registry.RegistryManager;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(ArmoredFoes.MOD_ID);

    public static void bootstrap() {
        ModLootConditionTypes.bootstrap();
        ModLootFunctionTypes.bootstrap();
        ModLootPoolEntryTypes.bootstrap();
    }
}
