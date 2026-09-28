package fuzs.armoredfoes.common.world.level.storage.loot.entries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.ComposableEntryContainer;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.SequentialEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Like {@link SequentialEntry}, meaning all entries until the first that fails are returned.
 * <p>
 * In contrast, here they are also unpacked directly, meaning all loot from the valid entries is generated, not just
 * from one single entry picked randomly later on.
 */
public class UnpackingSequenceEntry extends SequentialEntry {
    public static final MapCodec<UnpackingSequenceEntry> MAP_CODEC = createCodec(UnpackingSequenceEntry::new);

    public UnpackingSequenceEntry(List<LootPoolEntryContainer> children, Optional<Holder<LootItemCondition>> condition, Optional<Holder<LootItemFunction>> modifier) {
        super(children, condition, modifier);
    }

    @Override
    public MapCodec<SequentialEntry> codec() {
        return (MapCodec<SequentialEntry>) (MapCodec<?>) MAP_CODEC;
    }

    @Override
    protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> children) {
        return (LootContext context, Consumer<LootPoolEntry> consumer) -> {
            List<LootPoolEntry> lootPoolEntries = new ArrayList<>();
            consumer.accept(new LootPoolEntry() {
                @Override
                public int getWeight(float luck) {
                    return 1;
                }

                @Override
                public void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
                    for (LootPoolEntry lootPoolEntry : lootPoolEntries) {
                        lootPoolEntry.createItemStack(stackConsumer, lootContext);
                    }
                }
            });
            return UnpackingSequenceEntry.super.compose(children).expand(context, lootPoolEntries::add);
        };
    }

    public static class Builder extends CompositeEntryBase.Builder<UnpackingSequenceEntry, UnpackingSequenceEntry.Builder> {

        public Builder(LootPoolEntryContainer.Builder<?>... children) {
            super(children);
        }

        @Override
        protected UnpackingSequenceEntry.Builder getThis() {
            return this;
        }

        public UnpackingSequenceEntry.Builder and(LootPoolEntryContainer.Builder<?> childBuilder) {
            this.addEntry(childBuilder);
            return this.getThis();
        }

        @Override
        public LootPoolEntryContainer build() {
            return this.build(UnpackingSequenceEntry::new);
        }
    }
}
