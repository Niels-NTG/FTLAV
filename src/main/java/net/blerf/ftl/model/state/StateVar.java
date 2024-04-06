package net.blerf.ftl.model.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Counters used for event criteria and achievements.
 * <p>
 * FTL 1.5.4 introduced HIGH_O2 and SUFFOCATED_CREW.
 */
@Getter
@AllArgsConstructor
public enum StateVar {
    // TODO: Magic strings.
    BLUE_ALIEN("blue_alien", "Blue event choices clicked. (Only ones that require a race.)"),
    DEAD_CREW("dead_crew", "Ships defeated by killing all enemy crew."),
    DESTROYED_ROCK("destroyed_rock", "Rock ships destroyed, including pirates."),
    ENV_DANGER("env_danger", "Jumps into beacons with environmental dangers."),
    FIRED_SHOT("fired_shot", "Individual beams/blasts/projectiles fired. (See also: used_missile)"),
    HIGH_O2("higho2", "Times oxygen exceeded 20%, incremented when arriving at a beacon. (Bug: Or loading in FTL 1.5.4-1.5.10)"),
    KILLED_CREW("killed_crew", "Enemy crew killed. (And possibly friendly fire?)"),
    LOST_CREW("lost_crew", "Crew you've lost: killed, abandoned on nearby ships, taken by events?, but not dismissed. Even if cloned later. (See also: dead_crew)"),
    NEBULA("nebula", "Jumps into nebula beacons."),
    OFFENSIVE_DRONE("offensive_drone", "The number of times drones capable of damaging an enemy ship powered up."),
    REACTOR_UPGRADE("reactor_upgrade", "Reactor (power bar) upgrades beyond the ship's default levels."),
    STORE_PURCHASE("store_purchase", "Non-repair purchases, such as crew/items. (Selling isn't counted.)"),
    STORE_REPAIR("store_repair", "Store repair button clicks."),
    SUFFOCATED_CREW("suffocated_crew", "???"),
    SYSTEM_UPGRADE("system_upgrade", "System (and subsystem; not reactor) upgrades beyond the ship's default levels."),
    TELEPORTED("teleported", "Teleporter activations, in either direction."),
    USED_DRONE("used_drone", "The number of times drone parts were consumed."),
    USED_MISSILE("used_missile", "Missile/bomb weapon discharges. (See also: fired_shot)"),
    WEAPON_UPGRADE("weapon_upgrade", "Weapons system upgrades beyond the ship's default levels. (See also: system_upgrade)");

    private final String id;
    private final String description;

    @Override
    public String toString() {
        return id;
    }

    public static StateVar findById(String id) {
        for (StateVar v : values()) {
            if (v.getId().equals(id)) return v;
        }
        return null;
    }

    public static String getDescription(String id) {
        StateVar v = StateVar.findById(id);
        if (v != null) return v.getDescription();
        return id + " is an unknown var. Please report it on the forum thread.";
    }
}
