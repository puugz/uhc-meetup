package me.puugz.meetup.game.scoreboard;

import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.states.EndingState;
import me.puugz.meetup.game.state.states.PlayingState;
import me.puugz.meetup.game.state.states.StartingState;
import me.puugz.meetup.game.state.states.WaitingState;
import me.puugz.meetup.util.PlayerUtil;
import me.puugz.meetup.util.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class ScoreboardProvider implements ScoreboardElementHandler {

    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();

        final GameState state = UHCMeetup.getInstance().getStateHandler().getCurrentState();
        final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();

        element.setTitle(ChatColor.GOLD + "UHC Meetup");
        element.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------");

        if (state instanceof WaitingState) {
            element.add("Waiting for players");
        } else if (state instanceof StartingState) {
            final StartingState startingState = (StartingState) state;
            final int seconds = startingState.getCountdown().getSeconds();

            element.add("The game will start in:");
            element.add(ChatColor.GOLD + TimeUtil.formatSeconds(seconds));
        } else if (state instanceof PlayingState) {
            final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());
            final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();

            // TODO: Display border shrinking time
            element.add("Border: " + ChatColor.GOLD + borderHandler.getBorderSize());
            element.add("Players: " + ChatColor.GOLD + playerHandler.alive().size());
            element.add("Ping: " + ChatColor.GOLD + PlayerUtil.getPing(player) + " ms");
            element.add("Kills: " + ChatColor.GOLD + gamePlayer.kills);
        } else if (state instanceof EndingState) {
            final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());

            element.add("Kills: " + ChatColor.GOLD + gamePlayer.kills);
            element.add("Winner: " + ChatColor.GOLD + playerHandler.getWinnerName());
        }

        element.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------");

        return element;
    }
}
