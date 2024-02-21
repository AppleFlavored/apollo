package dev.flavored.apollo;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.ApolloManager;
import com.lunarclient.apollo.ApolloPlatform;
import com.lunarclient.apollo.module.ApolloModuleManagerImpl;
import com.lunarclient.apollo.option.Options;
import com.lunarclient.apollo.option.OptionsImpl;
import com.lunarclient.apollo.stats.ApolloStats;
import dev.flavored.apollo.listener.ApolloPlayerListener;
import dev.flavored.apollo.listener.ApolloWorldListener;
import dev.flavored.apollo.wrapper.MinestomApolloStats;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ApolloMinestomPlatform implements ApolloPlatform {

    private static ApolloMinestomPlatform instance;
    private final Options options = new OptionsImpl(null);
    private final ApolloStats stats;

    private ApolloMinestomPlatform() {
        stats = new MinestomApolloStats();
    }

    /**
     * Initializes the Apollo platform. This method should only be called once after {@link MinecraftServer#init()} has been called.
     *
     * @throws IllegalStateException if the platform has already been initialized
     */
    public static void init() {
        if (instance != null)
            throw new IllegalStateException("ApolloMinestomPlatform has already been initialized");

        instance = new ApolloMinestomPlatform();
        ApolloManager.bootstrap(instance);

        EventNode<Event> apolloEventNode = EventNode.all("apollo");
        new ApolloPlayerListener(apolloEventNode);
        new ApolloWorldListener(apolloEventNode);
        MinecraftServer.getGlobalEventHandler().addChild(apolloEventNode);

        try {
            ((ApolloModuleManagerImpl) Apollo.getModuleManager()).enableModules();
        } catch (Throwable throwable) {
            instance.getPlatformLogger().log(Level.SEVERE, "Unable to load Apollo modules!", throwable);
        }

        ApolloManager.getStatsManager().enable();
    }

    @Override
    public Kind getKind() {
        return Kind.SERVER;
    }

    @Override
    public Options getOptions() {
        return options;
    }

    @Override
    public String getApolloVersion() {
        return "1.0.9";
    }

    @Override
    public Logger getPlatformLogger() {
        return (Logger) MinecraftServer.LOGGER;
    }

    @Override
    public ApolloStats getStats() {
        return stats;
    }

    @Override
    public Object getPlugin() {
        return null;
    }
}
