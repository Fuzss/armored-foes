package fuzs.armoredfoes.common.init;

import com.mojang.serialization.MapCodec;
import fuzs.armoredfoes.common.world.level.storage.loot.functions.ApplyEnchantmentProviderFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class ModLootFunctionTypes {
    public static final Holder.Reference<MapCodec<? extends LootItemFunction>> APPLY_ENCHANTMENT_PROVIDER = ModRegistry.REGISTRIES.register(
            Registries.LOOT_FUNCTION_TYPE,
            "apply_enchantment_provider",
            () -> ApplyEnchantmentProviderFunction.MAP_CODEC);

    public static void bootstrap() {
        // NO-OP
    }
}
