package me.puugz.meetup.game.scenario;

import lombok.Getter;
import me.puugz.meetup.game.scenario.scenarios.TimeBombScenario;

import java.util.HashMap;
import java.util.Map;

/**
 * @author puugz
 * @since May 22, 2023
 */
@Getter
public class ScenarioHandler {

    private final Map<String, Scenario> scenarios = new HashMap<>();

    private boolean active;

    public ScenarioHandler() {
        this.register(new TimeBombScenario());
    }

    public void enable() {
        this.active = true;
        this.scenarios.values().forEach(Scenario::enable);
    }

    public void disable() {
        this.active = false;
        this.scenarios.values().forEach(Scenario::disable);
    }

    private void register(Scenario scenario) {
        this.scenarios.put(scenario.getName(), scenario);
    }

    public Scenario find(String name) {
        return this.scenarios.get(name);
    }
}
