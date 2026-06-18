package fuzs.armoredfoes.common.data.loot;

import fuzs.armoredfoes.common.init.ModLootTables;
import fuzs.armoredfoes.common.world.level.storage.loot.entries.SelectionEntry;
import fuzs.armoredfoes.common.world.level.storage.loot.entries.UnpackingSequenceEntry;
import fuzs.armoredfoes.common.world.level.storage.loot.functions.ApplyEnchantmentProviderFunction;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.DifficultyCheck;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.EffectiveDifficultyCheck;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.RaidCheck;
import fuzs.puzzleslib.common.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class ModEquipmentLootProvider extends AbstractLootProvider.Simple {

    public ModEquipmentLootProvider(DataProviderContext context) {
        super(LootContextParamSets.EQUIPMENT, context);
    }

    @Override
    public void addLootTables() {
        this.add(ModLootTables.NATURAL_ARMOR_EQUIPMENT,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(new SelectionEntry.Builder(NestedLootTable.lootTableReference(ModLootTables.LEATHER_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.COPPER_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.GOLDEN_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.CHAINMAIL_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.IRON_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.DIAMOND_ARMOR_EQUIPMENT)))));
        this.add(ModLootTables.RAIDER_ARMOR_EQUIPMENT,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(new SelectionEntry.Builder(NestedLootTable.lootTableReference(ModLootTables.GOLDEN_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.CHAINMAIL_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.IRON_ARMOR_EQUIPMENT),
                                        NestedLootTable.lootTableReference(ModLootTables.DIAMOND_ARMOR_EQUIPMENT)))));
        this.add(ModLootTables.LEATHER_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.LEATHER_HELMET,
                        Items.LEATHER_CHESTPLATE,
                        Items.LEATHER_LEGGINGS,
                        Items.LEATHER_BOOTS));
        this.add(ModLootTables.COPPER_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.COPPER_HELMET,
                        Items.COPPER_CHESTPLATE,
                        Items.COPPER_LEGGINGS,
                        Items.COPPER_BOOTS));
        this.add(ModLootTables.GOLDEN_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.GOLDEN_HELMET,
                        Items.GOLDEN_CHESTPLATE,
                        Items.GOLDEN_LEGGINGS,
                        Items.GOLDEN_BOOTS));
        this.add(ModLootTables.CHAINMAIL_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.CHAINMAIL_HELMET,
                        Items.CHAINMAIL_CHESTPLATE,
                        Items.CHAINMAIL_LEGGINGS,
                        Items.CHAINMAIL_BOOTS));
        this.add(ModLootTables.IRON_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.IRON_HELMET,
                        Items.IRON_CHESTPLATE,
                        Items.IRON_LEGGINGS,
                        Items.IRON_BOOTS));
        this.add(ModLootTables.DIAMOND_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.DIAMOND_HELMET,
                        Items.DIAMOND_CHESTPLATE,
                        Items.DIAMOND_LEGGINGS,
                        Items.DIAMOND_BOOTS));
        this.addNaturalArmorTable(EntityTypeIds.WITHER_SKELETON);
        this.addNaturalArmorTable(EntityTypeIds.DROWNED);
        this.addGoldenArmorTable(EntityTypeIds.PIGLIN);
        this.addGoldenArmorTable(EntityTypeIds.PIGLIN_BRUTE);
        this.addGoldenArmorTable(EntityTypeIds.ZOMBIFIED_PIGLIN);
        this.addRaiderArmorTable(EntityTypeIds.VINDICATOR);
        this.addRaiderArmorTable(EntityTypeIds.EVOKER);
        this.addRaiderArmorTable(EntityTypeIds.ILLUSIONER);
        this.addRaiderArmorTable(EntityTypeIds.PILLAGER);
    }

    private void addNaturalArmorTable(ResourceKey<EntityType<?>> entityType) {
        this.add(ModLootTables.createEntityEquipmentTable(entityType),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(NestedLootTable.lootTableReference(ModLootTables.NATURAL_ARMOR_EQUIPMENT)
                                        .when(EffectiveDifficultyCheck.randomChance(0.15F)))));
    }

    private void addGoldenArmorTable(ResourceKey<EntityType<?>> entityType) {
        this.add(ModLootTables.createEntityEquipmentTable(entityType),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(NestedLootTable.lootTableReference(ModLootTables.GOLDEN_ARMOR_EQUIPMENT)
                                        .when(LootItemRandomChanceCondition.randomChance(0.1F))
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .flags(EntityFlagsPredicate.Builder.flags()
                                                                .setIsBaby(false)))))));
    }

    private void addRaiderArmorTable(ResourceKey<EntityType<?>> entityType) {
        this.add(ModLootTables.createEntityEquipmentTable(entityType),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(AlternativesEntry.alternatives(NestedLootTable.lootTableReference(ModLootTables.RAIDER_ARMOR_EQUIPMENT)
                                                .when(LootItemRandomChanceCondition.randomChance(0.65F))
                                                .when(RaidCheck.raid(IntRange.lowerBound(6))),
                                        NestedLootTable.lootTableReference(ModLootTables.RAIDER_ARMOR_EQUIPMENT)
                                                .when(LootItemRandomChanceCondition.randomChance(0.35F))
                                                .when(RaidCheck.raid(IntRange.lowerBound(3))),
                                        NestedLootTable.lootTableReference(ModLootTables.RAIDER_ARMOR_EQUIPMENT)
                                                .when(LootItemRandomChanceCondition.randomChance(0.15F))
                                                .when(RaidCheck.raid())))));
    }

    private LootTable.Builder createArmorEquipment(Item headItem, Item chestItem, Item legsItem, Item feetItem) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(new UnpackingSequenceEntry.Builder(this.createEnchantedSpawnedArmor(headItem),
                                this.createArmorEquipmentPiece(chestItem),
                                this.createArmorEquipmentPiece(legsItem),
                                this.createArmorEquipmentPiece(feetItem))));
    }

    /**
     * @see net.minecraft.world.entity.Mob#populateDefaultEquipmentSlots(RandomSource, DifficultyInstance)
     */
    private LootPoolEntryContainer.Builder<?> createArmorEquipmentPiece(Item item) {
        return AlternativesEntry.alternatives((this.createEnchantedSpawnedArmor(item)).when(
                        LootItemRandomChanceCondition.randomChance(0.9F)).when(DifficultyCheck.difficulty(Difficulty.HARD)),
                (this.createEnchantedSpawnedArmor(item)).when(LootItemRandomChanceCondition.randomChance(0.75F))
                        .when(InvertedLootItemCondition.invert(DifficultyCheck.difficulty(Difficulty.HARD))));
    }

    /**
     * @see net.minecraft.world.entity.Mob#enchantSpawnedWeapon(ServerLevelAccessor, RandomSource,
     *         DifficultyInstance)
     */
    private LootPoolSingletonContainer.Builder<?> createEnchantedSpawnedWeapon(Item item) {
        return this.createSpawnedEquipment(item, 0.25F);
    }

    /**
     * @see net.minecraft.world.entity.Mob#enchantSpawnedArmor(ServerLevelAccessor, RandomSource, EquipmentSlot,
     *         DifficultyInstance)
     */
    private LootPoolSingletonContainer.Builder<?> createEnchantedSpawnedArmor(Item item) {
        return this.createSpawnedEquipment(item, 0.5F);
    }

    /**
     * @see net.minecraft.world.entity.Mob#enchantSpawnedEquipment(ServerLevelAccessor, EquipmentSlot, RandomSource,
     *         float, DifficultyInstance)
     */
    private LootPoolSingletonContainer.Builder<?> createSpawnedEquipment(Item item, float randomChance) {
        return LootItem.lootTableItem(item)
                .apply(ApplyEnchantmentProviderFunction.fromProvider(this.registries(),
                                VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT)
                        .when(EffectiveDifficultyCheck.randomChance(randomChance)));
    }
}
