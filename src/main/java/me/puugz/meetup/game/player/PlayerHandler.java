package me.puugz.meetup.game.player;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.player.task.WinnerFireworkTask;
import me.puugz.meetup.game.state.states.EndingState;
import me.puugz.meetup.game.state.states.PlayingState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
        gamePlayer.setState(GamePlayer.State.SPECTATING);

        this.players.values().forEach(other -> {
            final Player otherPlayer = other.asPlayer();

            if (otherPlayer == null) return;
            if (other.getState() == GamePlayer.State.PLAYING)
                otherPlayer.hidePlayer(player);
            else
                otherPlayer.showPlayer(player);
        });

        // TODO: Add Ghost Invisibility Effect

        player.sendMessage(this.messages.nowSpectating);
        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().addItem(this.compass);
        player.updateInventory();
    }

    /**
     * Used to check for a winner and transition to the {@link EndingState}
     * <p>
     * Called only from {@link PlayingState}
     */
    public void handleWinnerCheck() {
        this.players()
                .findFirst()
                .ifPresent(winner -> {
                    winner.setGamesWon(winner.getGamesWon() + 1);
                    this.winnerName = winner.getName();

                    final Player winnerPlayer = winner.asPlayer();
                    new WinnerFireworkTask(winnerPlayer)
                            .runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);

                    UHCMeetup.getInstance().getStateHandler().next();
                });
    }

    /**
     * Finds or creates a new {@link GamePlayer}
     * @param uuid The {@link Player}'s id
     * @param name The {@link Player}'s name
     * @return The {@link GamePlayer} object
     */
    public GamePlayer findOrCreate(UUID uuid, String name) {
        if (this.players.containsKey(uuid))
            return this.players.get(uuid);

        GamePlayer gamePlayer = UHCMeetup.getInstance()
                .getMongoHandler()
                .getPlayerRepository()
                .find(uuid);

        if (gamePlayer == null) {
            gamePlayer = new GamePlayer(uuid, name);
            gamePlayer.saveAsync();
        }
        if (!gamePlayer.getName().equals(name)) {
            gamePlayer.setName(name);
            gamePlayer.saveAsync();
        }

        this.players.put(uuid, gamePlayer);

        return gamePlayer;
    }

    public GamePlayer find(UUID uuid) {
        return this.players.get(uuid);
    }

    public CompletableFuture<GamePlayer> find(String name) {
        return CompletableFuture.supplyAsync(() -> this.players.values()
                .stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(UHCMeetup.getInstance().getMongoHandler()
                        .getPlayerRepository()
                        .find(Filters.regex("name", Pattern.compile(name, Pattern.CASE_INSENSITIVE)))
                )
        );
    }

    /**
     * @return A stream of {@link GamePlayer}s that are playing in the game
     */
    public Stream<GamePlayer> players() {
        return this.players.values().stream()
                .filter(player -> player.getState() == GamePlayer.State.PLAYING)
                .filter(player -> player.asPlayer() != null);
    }

    /**
     * @return A stream of {@link Player}s that are playing in the game
     */
    public Stream<Player> bukkitPlayers() {
        return this.players().map(GamePlayer::asPlayer);
    }
}
