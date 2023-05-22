package me.puugz.meetup.game.state;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class StateHandler {

    @Getter
    private GameState currentState;

    public StateHandler() {
        this.setState(GameState.WAITING);
    }

    public void setState(GameState state) {
        if (this.currentState != null) {
            this.currentState.disable();
            HandlerList.unregisterAll(this.currentState);
        }

        this.currentState = state;
        Bukkit.getPluginManager()
                .registerEvents(this.currentState, UHCMeetup.getInstance());
        this.currentState.enable();
    }

    public void next() {
        this.setState(this.currentState.next());
    }
}
