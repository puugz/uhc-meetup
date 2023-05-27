package me.puugz.meetup.game.player;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class PlayerHandler {

    @Getter
    private final Map<UUID, GamePlayer> players = new HashMap<>();

    private final MessagesConfig messages = UHCMeetup.getInstance()
            .getMessagesConfig();

    @Getter
    private String winnerName;

    private final ItemStack compass;

    public PlayerHandler() {
        this.compass = new ItemStack(Material.COMPASS);

        final ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(this.messages.navigationItem);
        compass.setItemMeta(meta);
    }

    public void addSpectator(Player player) {
        final GamePlayer gamePlayer = this.find(player.getUniqueId());
        gamePlayer.state = GamePlayer.State.SPECTATING;

        this.players.values().forEach(other -> {
            final Player otherPlayer = other.asPlayer();

            if (otherPlayer == null) return;
            if (other.state == GamePlayer.State.PLAYING)
                otherPlayer.hidePlayer(player);
            else
                otherPlayer.showPlayer(player);
        });

        if (gamePlayer.deathLocation != null)
            player.teleport(gamePlayer.deathLocation);
        else
            player.teleport(UHCMeetup.getInstance()
                    .getMapHandler().getMapWorld()
                    .getSpawnLocation()
            );

        // TODO: Add Ghost Invisibility Effect

        player.sendMessage(this.messages.nowSpectating);
        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);

        player.getInventory().clear();
        player.getInventory().addItem(compass);
        player.updateInventory();
    }

    /**
     * Called only from {@link me.puugz.meetup.game.state.states.PlayingState}
     */
    public void handleWinnerCheck() {
        final List<GamePlayer> alive = this.alive();

        if (alive.size() == 1) {
            final GamePlayer winner = alive.get(0);
            this.winnerName = winner.getName();

            UHCMeetup.getInstance().getStateHandler().next();
        }
    }

    public GamePlayer find(UUID uuid) {
        return this.players.get(uuid);
    }

    public void add(UUID uuid, String name) {
        this.players.put(uuid, new GamePlayer(uuid, name));
    }

    public List<GamePlayer> alive() {
        return this.players.values().stream()
                .filter(player -> player.state == GamePlayer.State.PLAYING)
                .filter(player -> player.asPlayer() != null)
                .collect(Collectors.toList());
    }

    public List<Player> aliveAsPlayers() {
        return this.alive().stream()
                .map(GamePlayer::asPlayer)
                .collect(Collectors.toList());
    }

    public List<GamePlayer> spectators() {
        return this.players.values().stream()
                .filter(player -> player.state == GamePlayer.State.SPECTATING)
                .filter(player -> player.asPlayer() != null)
                .collect(Collectors.toList());
    }
}
