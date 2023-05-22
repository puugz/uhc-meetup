package me.puugz.meetup.game.state;

import me.puugz.meetup.game.state.states.EndingState;
import me.puugz.meetup.game.state.states.PlayingState;
import me.puugz.meetup.game.state.states.StartingState;
import me.puugz.meetup.game.state.states.WaitingState;
import org.bukkit.event.Listener;

/**
 * @author puugz
 * @since May 08, 2023
 */
public interface GameState extends Listener {

    GameState WAITING = new WaitingState();
    GameState STARTING = new StartingState();
    GameState PLAYING = new PlayingState();
    GameState ENDING = new EndingState();

    default void enable() {}

    default void disable() {}

    GameState next();

}
