package me.puugz.meetup.game.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author puugz
 * @since May 29, 2023
 */
@Getter
@RequiredArgsConstructor
public class CustomDeathEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player victim;
    private final Player killer;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
