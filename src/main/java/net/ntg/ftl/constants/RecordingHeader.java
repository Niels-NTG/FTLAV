package net.ntg.ftl.constants;

public class RecordingHeader {

	// Non Temporal
	public final static String TIME = "Time";
	public final static String SHIP_NAME = "Ship Name";
	public final static String SHIP_TYPE = "Ship Type";
	public final static String DIFFICULTY = "Difficulty";
	public final static String AE_CONTENT = "AE Content Enabled";

	// Ship Location
	public final class Location {
		public final static String BEACON_NUMBER = "Beacon Number";
		public final static String SECTOR_NUMBER = "Sector Number";
		public final static String SECTOR_TYPE = "Sector Type";
		public final static String SECTOR_TITLE = "Sector Title";
		public final static String FLEET_ADVANCEMENT = "Fleet Advancement";
	}

	// Ship Log
	public final class Log {
		public final static String TOTAL_SHIPS_DEFEATED = "Total Ships Defeated";
		public final static String TOTAL_SCRAP_COLLECTED = "Total Scrap Collected";
		public final static String TOTAL_CREW_HIRED = "Total Crew Hired";
		public final static String SCORE = "Score";
	}

	// Encounter
	public final class Encounter {
		public final static String HAZARDS = "Hazards";
		public final static String EVENT_TEXT = "Event Text";
		public final static String STORE_LIST = "Store Listing";
	}

	// Ship Supplies
	public final class Supplies {
		public final static String SCRAP = "Scrap";
		public final static String HULL = "Hull";
		public final static String FUEL = "Fuel";
		public final static String DRONE_PARTS = "Drone Parts";
		public final static String MISSILES = "Missiles";
		public final static String CREW_SIZE = "Crew Size";
		public final static String CARGO_LIST = "Cargo Listing";
		public final static String AUGMENT_LIST = "Augment Listing";
		public final static String OXYGEN_LEVEL = "Oxygen Levels";
	}

	// Ship Systems
	public final class System {
		public final static String POWER_RESERVE = "Power Reserve";
		public final class Shield {
			public final static String CAPACITY = "Shield System Capacity";
			public final static String POWER = "Shield System Power";
			public final static String DAMAGE = "Shield System Damage";
		}
		public final class Engine {
			public final static String CAPACITY = "Engine System Capacity";
			public final static String POWER = "Engine System Power";
			public final static String DAMAGE = "Engine System Damage";
		}
		public final class Oxygen {
			public final static String CAPACITY = "Oxygen System Capacity";
			public final static String POWER = "Oxygen System Power";
			public final static String DAMAGE = "Oxygen System Damage";
		}
		public final class Weapons {
			public final static String CAPACITY = "Weapons System Capacity";
			public final static String POWER = "Weapons System Power";
			public final static String DAMAGE = "Weapons System Damage";
			public final static String SLOT = "Weapon Slot %1$d";
		}
		public final class DroneControl {
			public final static String CAPACITY = "Drone Control System Capacity";
			public final static String POWER = "Drone Control System Power";
			public final static String DAMAGE = "Drone Control System Damage";
			public final static String SLOT = "Drone Slot %1$d";
		}
		public final class MedBay {
			public final static String CAPACITY = "Medical Bay System Capacity";
			public final static String POWER = "Medical Bay System Power";
			public final static String DAMAGE = "Medical Bay System Damage";
		}
		public final class Teleporter {
			public final static String CAPACITY = "Teleporter System Capacity";
			public final static String POWER = "Teleporter System Power";
			public final static String DAMAGE = "Teleporter System Damage";
		}
		public final class Cloaking {
			public final static String CAPACITY = "Cloaking System Capacity";
			public final static String POWER = "Cloaking System Power";
			public final static String DAMAGE = "Cloaking System Damage";
		}
		public final class Artillery {
			public final static String CAPACITY = "Artillery System Capacity";
			public final static String POWER = "Artillery System Power";
			public final static String DAMAGE = "Artillery System Damage";
		}
		public final class CloneBay {
			public final static String CAPACITY = "Clone Bay System Capacity";
			public final static String POWER = "Clone Bay System Power";
			public final static String DAMAGE = "Clone Bay System Damage";
		}
		public final class MindControl {
			public final static String CAPACITY = "Mind Control System Capacity";
			public final static String POWER = "Mind Control System Power";
			public final static String DAMAGE = "Mind Control System Damage";
		}
		public final class Hacking {
			public final static String CAPACITY = "Hacking System Capacity";
			public final static String POWER = "Hacking System Power";
			public final static String DAMAGE = "Hacking System Damage";
		}
		public final class Pilot {
			public final static String CAPACITY = "Pilot System Capacity";
			public final static String DAMAGE = "Pilot System Damage";
		}
		public final class Sensors {
			public final static String CAPACITY = "Sensor System Capacity";
			public final static String DAMAGE = "Sensor System Damage";
		}
		public final class Doors {
			public final static String CAPACITY = "Door System Capacity";
			public final static String DAMAGE = "Door System Damage";
		}
		public final class Battery {
			public final static String CAPACITY = "Battery System Capacity";
			public final static String DAMAGE = "Battery System Damage";
		}
	}

	//	 Crew member
	public final class CrewMember {
		public final static String NAME = "Crew Member %1$d Name";
		public final static String SPECIES = "Crew Member %1$d Species";
		public final static String HEALTH = "Crew Member %1$d Health";
		public final static String PILOT_SKILL = "Crew Member %1$d Pilot Skill";
		public final static String ENGINE_SKILL = "Crew Member %1$d Engine Skill";
		public final static String SHIELD_SKILL = "Crew Member %1$d Shield Skill";
		public final static String WEAPON_SKILL = "Crew Member %1$d Weapon Skill";
		public final static String REPAIR_SKILL = "Crew Member %1$d Repair Skill";
		public final static String COMBAT_SKILL = "Crew Member %1$d Combat Skill";
		public final static String REPAIRS = "Crew Member %1$d Repairs";
		public final static String COMBAT_KILLS = "Crew Member %1$d Combat Kills";
		public final static String PILOTED_EVASIONS = "Crew Member %1$d Piloted Evasions";
		public final static String JUMPS_SURVIVED = "Crew Member %1$d Jumps Survived";
		public final static String SKILLS_MASTERED = "Crew Member %1$d Skills Mastered";
	}

}
