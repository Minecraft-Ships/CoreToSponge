package org.core.implementation.sponge.events.events.entity.command;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.command.PlayerCommandEvent;
import org.jetbrains.annotations.NotNull;

public class SEntityCommandEvent implements PlayerCommandEvent {

    private final @NotNull String[] command;
    private final @NotNull LivePlayer player;

    public SEntityCommandEvent(@NotNull LivePlayer player, @NotNull String... command) {
        this.command = command;
        this.player = player;
    }

    @Override
    public String[] getCommand() {
        return this.command;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }
}
