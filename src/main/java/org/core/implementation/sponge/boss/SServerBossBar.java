package org.core.implementation.sponge.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.world.boss.ServerBossBar;

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
    public BossBar bossBar() {
        return this.bossBar;
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
