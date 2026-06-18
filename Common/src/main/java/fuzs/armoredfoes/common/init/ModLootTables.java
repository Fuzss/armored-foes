package fuzs.armoredfoes.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static final ResourceKey<LootTable> LEATHER_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/leather_armor");
    public static final ResourceKey<LootTable> COPPER_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/copper_armor");
    public static final ResourceKey<LootTable> GOLDEN_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/golden_armor");
    public static final ResourceKey<LootTable> CHAINMAIL_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/chainmail_armor");
    public static final ResourceKey<LootTable> IRON_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/iron_armor");
    public static final ResourceKey<LootTable> DIAMOND_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/diamond_armor");
    public static final ResourceKey<LootTable> NATURAL_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/natural_armor");
    public static final ResourceKey<LootTable> RAIDER_ARMOR_EQUIPMENT = ModRegistry.REGISTRIES.registerLootTable(
            "equipment/raider_armor");

    public static void bootstrap() {
        // NO-OP
    }

    public static ResourceKey<LootTable> createEntityEquipmentTable(ResourceKey<EntityType<?>> entityType) {
        return ResourceKey.create(Registries.LOOT_TABLE, entityType.identifier().withPrefix("equipment/entities/"));
    }
}
