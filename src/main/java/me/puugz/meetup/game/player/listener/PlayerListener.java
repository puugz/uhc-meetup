package me.puugz.meetup.game.player.listener;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.menu.Menu;
import me.puugz.meetup.menu.menus.NavigationMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class PlayerListener implements Listener {

    private final UHCMeetup plugin = UHCMeetup.getInstance();
    private final MessagesConfig messages = this.plugin.getMessagesConfig();

    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePreLogin(AsyncPlayerPreLoginEvent event) {
        if (!this.plugin.isReady()) {
            event.setKickMessage(this.messages.notReady);
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        final UUID uuid = event.getUniqueId();
        final String name = event.getName();

        final GamePlayer gamePlayer = this.plugin.getPlayerHandler().findOrCreate(uuid, name);
        gamePlayer.setState(GamePlayer.State.PLAYING);

        if (gamePlayer.getFirstJoin() == -1L)
            gamePlayer.setFirstJoin(System.currentTimeMillis());
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = this.plugin
                .getPlayerHandler().find(player.getUniqueId());

        if (gamePlayer == null)
            player.kickPlayer(this.messages.dataLoadingError);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleQuit(PlayerQuitEvent event) {
        final GamePlayer gamePlayer = this.plugin
                .getPlayerHandler().find(event.getPlayer().getUniqueId());
        if (gamePlayer != null)
            gamePlayer.saveAsync();
    }

    @EventHandler
    public void handleInteraction(PlayerInteractEvent event) {
        if (!event.hasItem())
            return;

        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = this.plugin
                .getPlayerHandler().find(player.getUniqueId());

        if (event.getItem().getType() == Material.COMPASS &&
                gamePlayer.getState() == GamePlayer.State.SPECTATING
        ) {
            event.setCancelled(true);
            player.closeInventory();
            new NavigationMenu().openMenu(player);
        }
    }

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = this.plugin
                .getPlayerHandler().find(player.getUniqueId());

        if (gamePlayer.getState() == GamePlayer.State.SPECTATING) {
            event.setFormat(this.messages.spectatorPrefix
                    .replace("{format}", event.getFormat()));
            event.getRecipients().removeIf(recipient -> this.plugin.getPlayerHandler()
                    .find(recipient.getUniqueId())
                    .getState() == GamePlayer.State.PLAYING);
        }
    }

    @EventHandler
    public void handleDrag(InventoryDragEvent event) {
        final GamePlayer gamePlayer = this.plugin
                .getPlayerHandler()
                .find(event.getWhoClicked().getUniqueId());

        event.setCancelled(gamePlayer.getState() == GamePlayer.State.SPECTATING);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR)
            return;

        final GamePlayer gamePlayer = this.plugin
                .getPlayerHandler()
                .find(event.getWhoClicked().getUniqueId());
        event.setCancelled(gamePlayer.getState() == GamePlayer.State.SPECTATING);

        final InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Menu) {
            event.setCancelled(true);
            ((Menu) holder).handleClick(event);
        }
    }

    @EventHandler
    public void handleCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }
}
