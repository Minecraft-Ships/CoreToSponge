package org.core.implementation.sponge.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.world.boss.ServerBossBar;
import org.core.world.boss.colour.BossColour;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class SServerBossBar implements ServerBossBar {

    private final BossBar bossBar;
    private final Set<LivePlayer> players = new HashSet<>();

    public SServerBossBar(BossBar bar) {
        this.bossBar = bar;
    }

    @Override
    public AText getTitle() {
        return new AdventureText(this.bossBar.name());
    }

    @Override
    public ServerBossBar setTitle(AText text) {
        this.bossBar.name(((AdventureText) text).getComponent());
        return this;
    }

    @Override
    public BossColour getColour() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public ServerBossBar setColour(BossColour colour) {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public int getValue() {
        return (int) (this.bossBar.progress() * 10);
    }

    @Override
    public ServerBossBar setValue(int value) {
        if (value > 100) {
            throw new IllegalArgumentException("ServerBossBar.SetValue must be between 0 and 100 (" + value + ")");
        }
        double progress = value / 100.0;
        this.bossBar.progress((float) progress);
        return this;
    }

    @Override
    public Set<LivePlayer> getPlayers() {
        return this.players;
    }

    @Override
    public ServerBossBar register(LivePlayer... players) {
        Stream.of(players).map(p -> (SLivePlayer) p).forEach(p -> {
            p.getSpongeEntity().showBossBar(this.bossBar);
            this.players.add(p);
        });
        return this;
    }

    @Override
    public ServerBossBar deregister(LivePlayer... players) {
        Stream.of(players).map(p -> (SLivePlayer) p).forEach(p -> {
            p.getSpongeEntity().hideBossBar(this.bossBar);
            this.players.remove(p);
        });
        return this;
    }
}
