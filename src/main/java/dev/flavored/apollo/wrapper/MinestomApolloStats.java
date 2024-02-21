package dev.flavored.apollo.wrapper;

import com.lunarclient.apollo.stats.ApolloPluginDescription;
import com.lunarclient.apollo.stats.ApolloStats;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;

import java.util.Collections;
import java.util.List;

public class MinestomApolloStats implements ApolloStats {

    @Override
    public boolean isOnlineMode() {
        return MojangAuth.isEnabled();
    }

    @Override
    public String getIcon() {
        return "";
    }

    @Override
    public String getVersion() {
        return MinecraftServer.VERSION_NAME;
    }

    @Override
    public List<ApolloPluginDescription> getPlugins() {
        return Collections.emptyList();
    }

    @Override
    public String getPlatformSubtype() {
        return MinecraftServer.getBrandName();
    }

    @Override
    public String getPlatformVersion() {
        return MinecraftServer.VERSION_NAME;
    }

    @Override
    public int getTotalPlayers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayerCount();
    }
}
