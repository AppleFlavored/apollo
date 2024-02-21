package dev.flavored.apollo.wrapper;

import com.lunarclient.apollo.ApolloManager;
import com.lunarclient.apollo.player.AbstractApolloPlayer;
import net.minestom.server.entity.Player;

import java.util.UUID;

public final class MinestomApolloPlayer extends AbstractApolloPlayer {

    private final Player player;

    public MinestomApolloPlayer(Player player) {
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUuid();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public void sendPacket(byte[] messages) {
        player.sendPluginMessage(ApolloManager.PLUGIN_MESSAGE_CHANNEL, messages);
    }

    @Override
    public boolean hasPermission(String permissionNode) {
        return player.hasPermission(permissionNode);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
