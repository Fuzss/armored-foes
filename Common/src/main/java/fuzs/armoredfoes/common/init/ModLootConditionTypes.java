package fuzs.armoredfoes.common.init;

import com.mojang.serialization.MapCodec;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.DifficultyCheck;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.EffectiveDifficultyCheck;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.RaidCheck;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ModLootConditionTypes {
    public static final Holder.Reference<MapCodec<? extends LootItemCondition>> DIFFICULTY_CHECK = ModRegistry.REGISTRIES.register(
            Registries.LOOT_CONDITION_TYPE,
            "difficulty_check",
            () -> DifficultyCheck.MAP_CODEC);
    public static final Holder.Reference<MapCodec<? extends LootItemCondition>> EFFECTIVE_DIFFICULTY_CHECK = ModRegistry.REGISTRIES.register(
            Registries.LOOT_CONDITION_TYPE,
            "effective_difficulty_check",
            () -> EffectiveDifficultyCheck.MAP_CODEC);
    public static final Holder.Reference<MapCodec<? extends LootItemCondition>> RAID_CHECK = ModRegistry.REGISTRIES.register(
            Registries.LOOT_CONDITION_TYPE,
            "raid_check",
            () -> RaidCheck.MAP_CODEC);

    public static void bootstrap() {
        // NO-OP
    }
}
