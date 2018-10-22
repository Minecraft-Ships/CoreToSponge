package org.ships.implementation.sponge.platform;

import org.core.configuration.type.ConfigurationLoaderType;
import org.core.configuration.type.ConfigurationLoaderTypes;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.ItemTypes;
import org.core.platform.Platform;
import org.core.platform.Plugin;
import org.core.text.TextColour;
import org.core.text.TextColours;
import org.core.utils.Guaranteed;
import org.core.utils.Identifable;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.details.BlockDetails;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SpongePlatform implements Platform {

    protected Map<String, Class<? extends BlockDetails>> BLOCK_STATE_ID_TO_BLOCK_DETAILS = new HashMap<>();

    public Optional<BlockDetails> createBlockDetailsInstance(org.spongepowered.api.block.BlockState state){
        Optional<Map.Entry<String, Class<? extends BlockDetails>>> opEntry = this.BLOCK_STATE_ID_TO_BLOCK_DETAILS.entrySet().stream().filter(e -> state.getId().startsWith(e.getKey())).findFirst();
        if (!opEntry.isPresent()) {
            return Optional.empty();
        }
        try {
            return Optional.of(opEntry.get().getValue().getConstructor(org.spongepowered.api.block.BlockState.class).newInstance(state));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int[] getMinecraftVersion() {
        return new int[]{1, 12, 2};
    }

    @Override
    public Set<Plugin> getPlugins() {
        return null;
    }

    @Override
    public <T extends Identifable> Collection<T> get(Class<T> class1) {
        return null;
    }

    @Override
    public <T extends Identifable> T get(Guaranteed<T> guaranteed) {
        return null;
    }

    @Override
    public BlockType get(BlockTypes blockId) {
        return null;
    }

    @Override
    public ItemType get(ItemTypes itemId) {
        return null;
    }

    @Override
    public <E extends Entity, S extends EntitySnapshot<E>> EntityType<E, S> get(EntityTypes<E, S> entityId) {
        return null;
    }

    @Override
    public TextColour get(TextColours id) {
        return null;
    }

    @Override
    public ConfigurationLoaderType get(ConfigurationLoaderTypes id) {
        return null;
    }

    @Override
    public <T extends Identifable> Optional<T> get(String id, Class<T> type) {
        return Optional.empty();
    }
}
