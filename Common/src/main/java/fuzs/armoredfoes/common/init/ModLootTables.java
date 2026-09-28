package fuzs.armoredfoes.common.init;

import fuzs.armoredfoes.common.ArmoredFoes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static final ResourceKey<LootTable> LEATHER_ARMOR_EQUIPMENT = register("equipment/leather_armor");
    public static final ResourceKey<LootTable> COPPER_ARMOR_EQUIPMENT = register("equipment/copper_armor");
    public static final ResourceKey<LootTable> GOLDEN_ARMOR_EQUIPMENT = register("equipment/golden_armor");
    public static final ResourceKey<LootTable> CHAINMAIL_ARMOR_EQUIPMENT = register("equipment/chainmail_armor");
    public static final ResourceKey<LootTable> IRON_ARMOR_EQUIPMENT = register("equipment/iron_armor");
    public static final ResourceKey<LootTable> DIAMOND_ARMOR_EQUIPMENT = register("equipment/diamond_armor");
    public static final ResourceKey<LootTable> NATURAL_ARMOR_EQUIPMENT = register("equipment/natural_armor");
    public static final ResourceKey<LootTable> RAIDER_ARMOR_EQUIPMENT = register("equipment/raider_armor");

    private static ResourceKey<LootTable> register(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, ArmoredFoes.id(name));
    }

    public static ResourceKey<LootTable> registerEquipment(ResourceKey<EntityType<?>> entityType) {
        return ResourceKey.create(Registries.LOOT_TABLE, entityType.identifier().withPrefix("equipment/entities/"));
    }
}
