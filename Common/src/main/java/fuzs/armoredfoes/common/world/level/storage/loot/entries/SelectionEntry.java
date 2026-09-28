package fuzs.armoredfoes.common.world.level.storage.loot.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Picks a single entry from a list.
 *
 * @see net.minecraft.world.entity.Mob#populateDefaultEquipmentSlots(RandomSource, DifficultyInstance)
 */
public class SelectionEntry extends CompositeEntryBase {
    public static final MapCodec<SelectionEntry> MAP_CODEC = RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<SelectionEntry> instance) -> instance.group(
                    LootPoolEntries.CODEC.listOf()
                            .optionalFieldOf("children", List.of())
                            .forGetter((SelectionEntry entry) -> entry.children))
            .and(commonFields(instance).t1())
            .and(commonFields(instance).t2())
            .and(ContextIntProviders.CODEC.fieldOf("base").forGetter((SelectionEntry entry) -> entry.base))
            .and(ContextFloatProviders.CODEC.fieldOf("per_value_above_first")
                    .forGetter((SelectionEntry entry) -> entry.perValueAboveFirst))
            .apply(instance, SelectionEntry::new));

    private final Holder<ContextIntProvider> base;
    private final Holder<ContextFloatProvider> perValueAboveFirst;

    public SelectionEntry(List<LootPoolEntryContainer> children, Optional<Holder<LootItemCondition>> condition, Optional<Holder<LootItemFunction>> modifier) {
        this(children, condition, modifier, ContextIntProviders.between(0, 2), ContextFloatProviders.exactly(0.1087F));
    }

    public SelectionEntry(List<LootPoolEntryContainer> children, Optional<Holder<LootItemCondition>> condition, Optional<Holder<LootItemFunction>> modifier, Holder<ContextIntProvider> base, Holder<ContextFloatProvider> perValueAboveFirst) {
        super(children, condition, modifier);
        this.base = base;
        this.perValueAboveFirst = perValueAboveFirst;
    }

    @Override
    public MapCodec<? extends CompositeEntryBase> codec() {
        return MAP_CODEC;
    }

    @Override
    protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> children) {
        if (children.isEmpty()) {
            return ALWAYS_FALSE;
        } else {
            return (LootContext context, Consumer<LootPoolEntry> consumer) -> {
                int equipmentTier = this.getEquipmentTier(context, children.size() - 1);
                return children.get(equipmentTier).expand(context, consumer);
            };
        }
    }

    protected int getEquipmentTier(LootContext context, int tiers) {
        int equipmentTier = this.base.value().getInt(context);
        // slightly different from vanilla with more runs for increasing equipment tier
        for (int i = 0; i < tiers; i++) {
            if (context.getRandom().nextFloat() < this.perValueAboveFirst.value().getFloat(context)) {
                equipmentTier++;
            }
        }

        return Math.min(equipmentTier, tiers);
    }

    public static class Builder extends CompositeEntryBase.Builder<SelectionEntry, SelectionEntry.Builder> {

        public Builder(LootPoolEntryContainer.Builder<?>... children) {
            super(children);
        }

        @Override
        protected SelectionEntry.Builder getThis() {
            return this;
        }

        public SelectionEntry.Builder and(LootPoolEntryContainer.Builder<?> childBuilder) {
            this.addEntry(childBuilder);
            return this.getThis();
        }

        @Override
        public LootPoolEntryContainer build() {
            return this.build(SelectionEntry::new);
        }
    }
}
