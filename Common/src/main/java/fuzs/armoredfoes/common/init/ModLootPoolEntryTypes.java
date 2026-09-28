package fuzs.armoredfoes.common.init;

import com.mojang.serialization.MapCodec;
import fuzs.armoredfoes.common.world.level.storage.loot.entries.SelectionEntry;
import fuzs.armoredfoes.common.world.level.storage.loot.entries.UnpackingSequenceEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class ModLootPoolEntryTypes {
    public static final Holder.Reference<MapCodec<? extends LootPoolEntryContainer>> SELECTION = ModRegistry.REGISTRIES.register(
            Registries.LOOT_POOL_ENTRY_TYPE,
            "selection",
            () -> SelectionEntry.MAP_CODEC);
    public static final Holder.Reference<MapCodec<? extends LootPoolEntryContainer>> UNPACKING_SEQUENCE = ModRegistry.REGISTRIES.register(
            Registries.LOOT_POOL_ENTRY_TYPE,
            "unpacking_sequence",
            () -> UnpackingSequenceEntry.MAP_CODEC);

    public static void bootstrap() {
        // NO-OP
    }
}
