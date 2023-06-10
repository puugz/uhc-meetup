package me.puugz.meetup.game.scoreboard;

import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.ScoreboardsConfig;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.countdown.Countdown;
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

        final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();
        final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());

        element.setTitle(config.title);

        if (gamePlayer == null) {
            element.add(ChatColor.RED + "Error, please rejoin!");
            return element;
        }

        final GameState state = UHCMeetup.getInstance().getStateHandler().getCurrentState();

        if (state instanceof WaitingState) {
            element.getLines().addAll(config.waiting);
        } else if (state instanceof StartingState) {
            final StartingState startingState = (StartingState) state;
            final int seconds = startingState.getCountdown().getSeconds() + 1;

            for (String line : config.starting)
                element.add(line.replace("{time}", TimeUtil.formatSeconds(seconds)));
        } else if (state instanceof PlayingState) {
            final PlayingState playingState = (PlayingState) state;

            final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();
            final boolean isBorderShrinking = Countdown.isActive(playingState.getBorderCountdown());
            final String borderFormat = isBorderShrinking
                    ? config.borderFormat.replace("{border_time}",
                    "" + (playingState.getBorderCountdown().getSeconds() + 1))
                    : "";

            for (String line : config.playing) {
                final String finalLine = gamePlayer.getNoCleanTimer() != null && line.contains("<no_clean_format>") ?
                        config.noCleanFormat.replace("{time}", "" + gamePlayer.getNoCleanTimer().getSeconds()) :
                        line
                                .replace("{border_size}", "" + borderHandler.getBorderSize())
                                .replace("<border_format>", borderFormat)
                                .replace("{players}", "" + playerHandler.players().count())
                                .replace("{ping}", "" + PlayerUtil.getPing(player))
                                .replace("{kills}", "" + gamePlayer.getLocalKills());

                if (line.contains("<no_clean_format>")) {
                    if (gamePlayer.getNoCleanTimer() != null)
                        element.add(finalLine);
                } else {
                    element.add(finalLine);
                }
            }
        } else if (state instanceof EndingState) {
            for (String line : config.ending)
                element.add(line
                        .replace("{kills}", "" + gamePlayer.getLocalKills())
                        .replace("{winner}", playerHandler.getWinnerName()));
        }

        return element;
    }
}
