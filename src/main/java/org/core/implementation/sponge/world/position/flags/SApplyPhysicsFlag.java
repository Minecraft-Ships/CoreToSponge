package org.core.implementation.sponge.world.position.flags;

import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.spongepowered.api.world.BlockChangeFlag;
import org.spongepowered.api.world.BlockChangeFlags;

public class SApplyPhysicsFlag implements ApplyPhysicsFlag {

    public static final SApplyPhysicsFlag DEFAULT = new SApplyPhysicsFlag("sponge:default", "Default",
                                                                          BlockChangeFlags.NONE.andFlag(
                                                                                  BlockChangeFlags.NOTIFY_CLIENTS));

    public static final SApplyPhysicsFlag DEFAULT_BREAK = new SApplyPhysicsFlag("sponge:break", "Break",
                                                                                BlockChangeFlags.ALL);
    public static final SApplyPhysicsFlag NONE = new SApplyPhysicsFlag("sponge:none", "None", BlockChangeFlags.NONE);


    protected final String id;
    protected final String name;
    protected final BlockChangeFlag flag;

    public SApplyPhysicsFlag(String id, String name, BlockChangeFlag flag) {
        this.flag = flag;
        this.name = name;
        this.id = id;
    }

    public BlockChangeFlag getFlag() {
        return this.flag;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
