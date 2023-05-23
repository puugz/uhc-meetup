package me.puugz.meetup.game.scoreboard;

import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.ScoreboardsConfig;
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

    private final ScoreboardsConfig config = UHCMeetup.getInstance()
            .getScoreboardsConfig();

    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();

        final GameState state = UHCMeetup.getInstance().getStateHandler().getCurrentState();
        final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();

        element.setTitle(config.title);

        if (state instanceof WaitingState) {
            element.getLines().addAll(config.waiting);
        } else if (state instanceof StartingState) {
            final StartingState startingState = (StartingState) state;
            final int seconds = startingState.getCountdown().getSeconds();

            for (String line : config.starting) {
                element.add(line.replace("{time}", TimeUtil.formatSeconds(seconds)));
            }
        } else if (state instanceof PlayingState) {
            final PlayingState playingState = (PlayingState) state;
            final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());
            final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();

            for (String line : config.playing) {
                element.add(line
                        .replace("{border_size}", "" + borderHandler.getBorderSize())
                        .replace("{border_time}", "" + playingState.getCountdown().getSeconds())
                        .replace("{players}", "" + playerHandler.alive().size())
                        .replace("{ping}", "" + PlayerUtil.getPing(player))
                        .replace("{kills}", "" + gamePlayer.kills));
            }
        } else if (state instanceof EndingState) {
            final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());

            for (String line : config.ending) {
                element.add(line
                        .replace("{kills}", "" + gamePlayer.kills)
                        .replace("{winner}", playerHandler.getWinnerName()));
            }
        }

        return element;
    }
}
