package fuzs.armoredfoes.common.data.loot;

import fuzs.armoredfoes.common.init.ModLootTables;
import fuzs.armoredfoes.common.world.level.storage.loot.entries.SelectionEntry;
import fuzs.armoredfoes.common.world.level.storage.loot.entries.UnpackingSequenceEntry;
import fuzs.armoredfoes.common.world.level.storage.loot.functions.ApplyEnchantmentProviderFunction;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.DifficultyCheck;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.EffectiveDifficultyCheck;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.RaidCheck;
import fuzs.puzzleslib.common.api.data.v3.loot.AbstractLootSubProvider;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
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
import net.minecraft.world.level.storage.loot.IntRangePredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class ModEquipmentLootProvider extends AbstractLootSubProvider {

    public ModEquipmentLootProvider(LootTableSubProvider.Context output) {
        super(output);
    }

    @Override
    public void generate() {
        Holder.Reference<LootTable> leatherArmor = this.output.accept(ModLootTables.LEATHER_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.LEATHER_HELMET,
                        Items.LEATHER_CHESTPLATE,
                        Items.LEATHER_LEGGINGS,
                        Items.LEATHER_BOOTS));
        Holder.Reference<LootTable> copperArmor = this.output.accept(ModLootTables.COPPER_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.COPPER_HELMET,
                        Items.COPPER_CHESTPLATE,
                        Items.COPPER_LEGGINGS,
                        Items.COPPER_BOOTS));
        Holder.Reference<LootTable> goldenArmor = this.output.accept(ModLootTables.GOLDEN_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.GOLDEN_HELMET,
                        Items.GOLDEN_CHESTPLATE,
                        Items.GOLDEN_LEGGINGS,
                        Items.GOLDEN_BOOTS));
        Holder.Reference<LootTable> chainmailArmor = this.output.accept(ModLootTables.CHAINMAIL_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.CHAINMAIL_HELMET,
                        Items.CHAINMAIL_CHESTPLATE,
                        Items.CHAINMAIL_LEGGINGS,
                        Items.CHAINMAIL_BOOTS));
        Holder.Reference<LootTable> ironArmor = this.output.accept(ModLootTables.IRON_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.IRON_HELMET,
                        Items.IRON_CHESTPLATE,
                        Items.IRON_LEGGINGS,
                        Items.IRON_BOOTS));
        Holder.Reference<LootTable> diamondArmor = this.output.accept(ModLootTables.DIAMOND_ARMOR_EQUIPMENT,
                this.createArmorEquipment(Items.DIAMOND_HELMET,
                        Items.DIAMOND_CHESTPLATE,
                        Items.DIAMOND_LEGGINGS,
                        Items.DIAMOND_BOOTS));
        Holder.Reference<LootTable> naturalArmor = this.output.accept(ModLootTables.NATURAL_ARMOR_EQUIPMENT,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(new SelectionEntry.Builder(NestedLootTable.lootTableReference(leatherArmor),
                                        NestedLootTable.lootTableReference(copperArmor),
                                        NestedLootTable.lootTableReference(goldenArmor),
                                        NestedLootTable.lootTableReference(chainmailArmor),
                                        NestedLootTable.lootTableReference(ironArmor),
                                        NestedLootTable.lootTableReference(diamondArmor)))));
        Holder.Reference<LootTable> raiderArmor = this.output.accept(ModLootTables.RAIDER_ARMOR_EQUIPMENT,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(new SelectionEntry.Builder(NestedLootTable.lootTableReference(goldenArmor),
                                        NestedLootTable.lootTableReference(chainmailArmor),
                                        NestedLootTable.lootTableReference(ironArmor),
                                        NestedLootTable.lootTableReference(diamondArmor)))));
        this.addNaturalArmorTable(EntityTypeIds.WITHER_SKELETON, naturalArmor);
        this.addNaturalArmorTable(EntityTypeIds.DROWNED, naturalArmor);
        this.addGoldenArmorTable(EntityTypeIds.PIGLIN, goldenArmor);
        this.addGoldenArmorTable(EntityTypeIds.PIGLIN_BRUTE, goldenArmor);
        this.addGoldenArmorTable(EntityTypeIds.ZOMBIFIED_PIGLIN, goldenArmor);
        this.addRaiderArmorTable(EntityTypeIds.VINDICATOR, raiderArmor);
        this.addRaiderArmorTable(EntityTypeIds.EVOKER, raiderArmor);
        this.addRaiderArmorTable(EntityTypeIds.ILLUSIONER, raiderArmor);
        this.addRaiderArmorTable(EntityTypeIds.PILLAGER, raiderArmor);
    }

    private void addNaturalArmorTable(ResourceKey<EntityType<?>> entityType, Holder<LootTable> naturalArmor) {
        this.output.accept(ModLootTables.registerEquipment(entityType),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(NestedLootTable.lootTableReference(naturalArmor)
                                        .when(EffectiveDifficultyCheck.randomChance(0.15F)))));
    }

    private void addGoldenArmorTable(ResourceKey<EntityType<?>> entityType, Holder<LootTable> goldenArmor) {
        this.output.accept(ModLootTables.registerEquipment(entityType),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(NestedLootTable.lootTableReference(goldenArmor)
                                        .when(LootItemRandomChanceCondition.randomChance(0.1F))
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .flags(EntityFlagsPredicate.Builder.flags()
                                                                .setIsBaby(false)))))));
    }

    private void addRaiderArmorTable(ResourceKey<EntityType<?>> entityType, Holder<LootTable> raiderArmor) {
        this.output.accept(ModLootTables.registerEquipment(entityType),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .add(AlternativesEntry.alternatives(NestedLootTable.lootTableReference(raiderArmor)
                                                .when(LootItemRandomChanceCondition.randomChance(0.65F))
                                                .when(RaidCheck.raid(IntRangePredicate.lowerBound(6))),
                                        NestedLootTable.lootTableReference(raiderArmor)
                                                .when(LootItemRandomChanceCondition.randomChance(0.35F))
                                                .when(RaidCheck.raid(IntRangePredicate.lowerBound(3))),
                                        NestedLootTable.lootTableReference(raiderArmor)
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
    private UniformContainerBase.Builder<?> createEnchantedSpawnedWeapon(Item item) {
        return this.createSpawnedEquipment(item, 0.25F);
    }

    /**
     * @see net.minecraft.world.entity.Mob#enchantSpawnedArmor(ServerLevelAccessor, RandomSource, EquipmentSlot,
     *         DifficultyInstance)
     */
    private UniformContainerBase.Builder<?> createEnchantedSpawnedArmor(Item item) {
        return this.createSpawnedEquipment(item, 0.5F);
    }

    /**
     * @see net.minecraft.world.entity.Mob#enchantSpawnedEquipment(ServerLevelAccessor, EquipmentSlot, RandomSource,
     *         float, DifficultyInstance)
     */
    private UniformContainerBase.Builder<?> createSpawnedEquipment(Item item, float randomChance) {
        return LootItem.lootTableItem(item)
                .apply(ApplyEnchantmentProviderFunction.fromProvider(this.output.lookup(Registries.ENCHANTMENT_PROVIDER),
                                VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT)
                        .when(EffectiveDifficultyCheck.randomChance(randomChance)));
    }
}
