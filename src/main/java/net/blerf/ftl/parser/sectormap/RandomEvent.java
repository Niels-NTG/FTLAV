package net.blerf.ftl.parser.sectormap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.random.RandRNG;
import net.blerf.ftl.xml.AugBlueprint;
import net.blerf.ftl.xml.Choice;
import net.blerf.ftl.xml.CrewBlueprint;
import net.blerf.ftl.xml.DroneBlueprint;
import net.blerf.ftl.xml.FTLEvent;
import net.blerf.ftl.xml.FTLEventList;
import net.blerf.ftl.xml.NamedText;
import net.blerf.ftl.xml.SectorDescription;
import net.blerf.ftl.xml.TextList;
import net.blerf.ftl.xml.WeaponBlueprint;
import net.blerf.ftl.xml.ship.ShipEvent;


/**
 * Event processing class.
 */
@Slf4j
public final class RandomEvent {

    private static final boolean fast = true;

    private static String sectorId = "STANDARD_SPACE";
    private static int sectorNumber = 0; // between 0 and 7
    private static Difficulty difficulty = Difficulty.EASY; // between 0 and 2
    private static boolean dlcEnabled = true;

    private static final Set<String> uniqueSectors = new HashSet<String>();

    private static Set<Integer> uniqueCrewNames = null;

    public static void setUniqueNames(Set<Integer> un) {
        uniqueCrewNames = un;
    }

    public static void setSectorId(String si) {
        sectorId = si;
        crewRarities.clear();
        weaponRarities.clear();
        augRarities.clear();
        droneRarities.clear();
    }

    public static void setSectorNumber(int sn) {
        sectorNumber = sn;
    }

    public static void setDifficulty(Difficulty d) {
        difficulty = d;
    }

    public static void setDlc(boolean d) {
        dlcEnabled = d;
    }

    public static void resetUniqueSectors() {
        uniqueSectors.clear();
    }

    /**
     * Load an event from an event id.
     */
    public static FTLEvent loadEventId(String id, RandRNG rng) {

        log.debug("Load event id {}", id);

        /* First, check if the id correspond to an event list */

        FTLEventList list = DataManager.getInstance().getEventListById(id, dlcEnabled);
        if (list != null) {
            List<FTLEvent> eventList = list.getEventList();

            FTLEvent ev = null;

            /* Choose a random event from the list, retry if we chose a
             * unique event that was already chosen.
             */
            do {
                log.debug("Choose random event from eventList");
                int e = rng.rand() % eventList.size();
                ev = loadEvent((FTLEvent) eventList.get(e).clone(), rng);
            }
            while (ev == null);
            return ev;
        }

        /* Get the event */
        FTLEvent event = (FTLEvent) DataManager.getInstance().getEventById(id, dlcEnabled).clone();

        return loadEvent(event, rng);
    }

    /**
     * Load an event.
     * To print which event is loaded on the game, use gdb with:
     * break *0x4a2c38
     * commands
     * silent
     * printf "load event %s\n",(char*)*$rsi
     * cont
     * end
     */
    public static FTLEvent loadEvent(FTLEvent event, RandRNG rng) {
        log.debug("Load event {}", event);

        /* If unique, check if it was already chosen */
        if (event.isUnique()) {
            if (uniqueSectors.contains(event.getId()))
                return null;
            uniqueSectors.add(event.getId());
        }

        /* If there's a load attribute, load the corresponding event */
        String load = event.getLoad();
        if (load != null) {
            return loadEventId(load, rng);
        }

        /* Handle text */
        NamedText text = event.getText();
        if (text != null) {
            load = text.getLoad();
            if (load != null) {
                if (fast) {
                    rng.rand();
                } else {
                    TextList list = DataManager.getInstance().getTextListById(load, dlcEnabled);
                    if (list == null) {
                        throw new UnsupportedOperationException(String.format("Could not find text list %s", load));
                    }
                    List<NamedText> textList = list.getTextList();

                    if (textList.size() == 0) {
                        throw new UnsupportedOperationException(String.format("No more text left in textlist %s", load));
                    }

                    log.debug("Choose random text from textList");
                    int n = rng.rand() % textList.size();
                    event.setText(textList.get(n));
                }
            }
        }

        /* item_modify offer */

        /* Randomize item quantities */
        int ivar12 = 100;
        if (false) {
            ivar12 = 5; // random range
        }

        log.debug("Item offer four random values");

        // 0x4a3681
        /* Randomize fuel quantity */
        int n = rng.rand();
        if ((n & 3) < ivar12) {
            int p = itemOfferQuantity(event, rng, "fuel");
            if (p > 0)
                ivar12 -= 1;
        }

        // 0x4a36cb
        /* Randomize drone quantity */
        n = rng.rand();
        if ((n % 3) < ivar12) {
            int p = itemOfferQuantity(event, rng, "drones");
            if (p > 0)
                ivar12 -= 1;
        }

        // 0x4a371d
        /* Randomize missiles quantity */
        n = rng.rand();
        if ((n & 1) < ivar12) {
            int p = itemOfferQuantity(event, rng, "missiles");
            if (p > 0)
                ivar12 -= 1;
        }

        // 0x4a3764
        /* Randomize scrap quantity */
        rng.rand();
        if (0 < ivar12) {
            int p = itemOfferQuantity(event, rng, "scrap");
            if (p > 0)
                ivar12 -= 1;
        }

        /* crewMember */
        FTLEvent.CrewMember crewMember = event.getCrewMember();
        if ((crewMember != null) && (crewMember.amount > 0)) {

            log.debug("Generating crewMember");

            if (crewMember.id == null)
                crewMember.id = "random";

            CrewBlueprint cb = null;

            if (crewMember.id.equals("traitor")) {
                log.debug("Traitor with crewMember.amount > 0 ???");
            } else if (crewMember.id.equals("random")) {
                /* Pick a random race (0x47f52c) */
                log.debug("Generating crewMember race");
                cb = pickRandomCrew(rng);

                crewMember.id = cb.getId();
                log.debug(String.format("   got %s", crewMember.id));
            } else {
                cb = DataManager.getInstance().getCrew(crewMember.id, dlcEnabled);
                /* Choose a random name here, that will be overwritten later? */

                n = rng.rand() % 169; // TODO: Magic number, look at (sorted?) crew names

                while (uniqueCrewNames.contains(n)) {
                    n = rng.rand() % 169;
                }
                uniqueCrewNames.add(n);
            }

            /* Generate layer colors (0x4a3b82) */
            log.debug("Generating crewMember layer color");

            List<CrewBlueprint.SpriteTintLayer> layers = cb.getSpriteTintLayerList();

            /* Draw as many random numbers as layers */
            if (layers != null) {
                for (int ll = 0; ll < layers.size(); ll++)
                    rng.rand();
            }

            /* Pick a random name if not set (0x4a3bc4) */
            if (crewMember.name.equals("")) {
                log.debug("Generating crewMember name");
                n = rng.rand() % 169; // TODO: Magic number, look at (sorted?) crew names

                while (uniqueCrewNames.contains(n)) {
                    n = rng.rand() % 169;
                }
                uniqueCrewNames.add(n);

                crewMember.name = "TODO";
            }

            /* If no skill is set, take two random ones */
            if ((crewMember.weapons == -1) && (crewMember.shields == -1) &&
                    (crewMember.pilot == -1) && (crewMember.engines == -1) &&
                    (crewMember.combat == -1) && (crewMember.repair == -1) &&
                    (crewMember.all_skills == -1)) {

                /* 0x4a3d3d */
                /* Pick an amount to raise skills */
                int[] skillMins = {0, 0, 0, 0, 1, 1, 1, 2, 0};
                int[] skillMaxs = {0, 0, 1, 2, 2, 2, 3, 3, 0};

                int skillMin = skillMins[sectorNumber];
                int skillMax = skillMaxs[sectorNumber];

                log.debug("Generating crewMember skill amount");
                int skillAmount = skillMin + (rng.rand() % (skillMax + 1 - skillMin));

                /* Pick two random different skills */
                log.debug("Generating crewMember skill type");
                int skillOne = rng.rand() % 6;
                int skillTwo = rng.rand() % 6;
                while (skillTwo == skillOne)
                    skillTwo = rng.rand() % 6;

                /* Raise skills */
                for (int sk = 0; sk < skillAmount; sk++) {
                    /* Pick a random skill among the chosen two */
                    int s = rng.rand() % 1;

                    /* Raise skillOne or skillTwo, somehow */
                }
            }
        }

        /* Weapon */
        FTLEvent.Item weapon = event.getWeapon();
        if (weapon != null) {
            if ((weapon.name != null) && weapon.name.equals("RANDOM")) {
                WeaponBlueprint wb = pickRandomWeapon(rng);
                weapon.name = wb.getId();
            }
        }

        /* Augment */
        FTLEvent.Item augment = event.getAugment();
        if (augment != null) {
            if ((augment.name != null) && augment.name.equals("RANDOM")) {
                AugBlueprint ab = pickRandomAugment(rng);
                augment.name = ab.getId();
            }
        }

        /* Drone */
        FTLEvent.Item drone = event.getDrone();
        if (drone != null) {
            if ((drone.name != null) && drone.name.equals("RANDOM")) {
                DroneBlueprint db = pickRandomDrone(rng);
                drone.name = db.getId();
            }
        }

        /* autoReward */
        FTLEvent.AutoReward autoReward = event.getAutoReward();
        if (autoReward != null) {

            log.debug("Generating autoReward with level {} and type {}", autoReward.level, autoReward.reward);

            /* Alter sector number based on difficulty */
            int newSectorNumber = sectorNumber;
            if (difficulty == Difficulty.EASY)
                newSectorNumber++;
            if ((difficulty == Difficulty.HARD) && (newSectorNumber > 0))
                newSectorNumber--;

            /* Determine reward level */
            int rewardLevel = 0;
            if (autoReward.level.equals("LOW"))
                rewardLevel = 0;
            else if (autoReward.level.equals("MED"))
                rewardLevel = 1;
            else if (autoReward.level.equals("HIGH"))
                rewardLevel = 2;
            else if (autoReward.level.equals("RANDOM"))
                rewardLevel = rng.rand() % 3;
            else
                /* If unknown reward, default to random.
                 * This happens at least once with a typo (MEDIUM instead of MED)
                 */
                rewardLevel = rng.rand() % 3;

            String[] resources = {"fuel", "missiles", "droneparts"};

            boolean extraItem = false;

            /* Standard reward */
            if (autoReward.reward.equals("standard")) {
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
                int resourceOne = rng.rand() % 3;
                int resourceTwo = rng.rand() % 3;
                while (resourceTwo == resourceOne)
                    resourceTwo = rng.rand() % 3;

                autoReward.resources[resourceOne] = autoRewardQuantity(rng, resources[resourceOne], 0, 0);
                autoReward.resources[resourceTwo] = autoRewardQuantity(rng, resources[resourceTwo], 0, 0);

                if ((rng.rand() % 100) < 3)
                    extraItem = true;
            } else if (autoReward.reward.equals("stuff")) {
                autoReward.scrap = autoRewardQuantity(rng, "scrap", 0, newSectorNumber);
                int resourceOne = rng.rand() % 3;
                int resourceTwo = rng.rand() % 3;
                while (resourceTwo == resourceOne)
                    resourceTwo = rng.rand() % 3;

                autoReward.resources[resourceOne] = autoRewardQuantity(rng, resources[resourceOne], rewardLevel, 0);
                autoReward.resources[resourceTwo] = autoRewardQuantity(rng, resources[resourceTwo], rewardLevel, 0);

                if ((rng.rand() % 100) < 6)
                    extraItem = true;

            } else if (autoReward.reward.equals("scrap_only")) {
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
            } else if (autoReward.reward.equals("fuel")) {
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
                autoReward.resources[0] = autoRewardQuantity(rng, "fuel", rewardLevel, 0);
            } else if (autoReward.reward.equals("missiles")) {
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
                autoReward.resources[1] = autoRewardQuantity(rng, "missiles", rewardLevel, 0);
            } else if (autoReward.reward.equals("droneparts")) {
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
                autoReward.resources[2] = autoRewardQuantity(rng, "droneparts", rewardLevel, 0);
            } else if (autoReward.reward.equals("fuel_only")) {
                autoReward.resources[0] = autoRewardQuantity(rng, "fuel", rewardLevel, 0);
            } else if (autoReward.reward.equals("missiles_only")) {
                autoReward.resources[1] = autoRewardQuantity(rng, "missiles", rewardLevel, 0);
            } else if (autoReward.reward.equals("droneparts_only")) {
                autoReward.resources[2] = autoRewardQuantity(rng, "droneparts", rewardLevel, 0);
            }

            int extraItemType = -1;
            if (extraItem) {
                extraItemType = rng.rand() % 3;
            }

            if ((extraItemType == 0) || autoReward.reward.equals("weapon")) {
                WeaponBlueprint wb = pickRandomWeapon(rng);
                autoReward.weapon = wb.getId();
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
            } else if ((extraItemType == 2) || autoReward.reward.equals("augment")) {
                AugBlueprint ab = pickRandomAugment(rng);
                autoReward.augment = ab.getId();
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
            } else if ((extraItemType == 1) || autoReward.reward.equals("drone")) {
                DroneBlueprint db = pickRandomDrone(rng);
                autoReward.drone = db.getId();
                autoReward.scrap = autoRewardQuantity(rng, "scrap", rewardLevel, newSectorNumber);
            }
        }

        /* Ship event: generate the seed */
        ShipEvent se = event.getShip();
        if (se != null) {
            log.debug("Ship seed value, generating");
            se.setSeed(rng.rand());
            log.debug("Ship seed value, set to {}", se.getSeed());
        }

        /* Browse each choice, and load the corresponding event */
        List<Choice> choiceList = event.getChoiceList();
        if (choiceList != null) {
            for (int i = 0; i < choiceList.size(); i++) {
                Choice choice = choiceList.get(i);

                FTLEvent choiceEvent = choice.getEvent();
                log.debug("Will load choice {}", choiceEvent);

                /* Fix: in the data file, at least one event has the field
                 * 'name' filled, where it should be 'load' instead.
                 */
                if (choiceEvent.getId() != null)
                    choiceEvent.setLoad(choiceEvent.getId());

                choice.setEvent(loadEvent(choiceEvent, rng));

                /* Load text it any. It is done after loading the event */
                NamedText cText = choice.getText();
                if (cText != null) {
                    load = cText.getLoad();
                    if (load != null) {
                        if (fast) {
                            rng.rand();
                        } else {
                            TextList list = DataManager.getInstance().getTextListById(load, dlcEnabled);
                            if (list == null) {
                                throw new UnsupportedOperationException(String.format("Could not find text list %s", load));
                            }
                            List<NamedText> textList = list.getTextList();

                            if (textList.size() == 0) {
                                throw new UnsupportedOperationException(String.format("No more text left in textlist %s", load));
                            }

                            log.debug("Choose random text from textList");
                            n = rng.rand() % textList.size();
                            choice.setText(textList.get(n));
                        }
                    }
                }
            }
        }

        // 0x4a4751
        if (true) { // some value == -1 (so uninitialized)
            log.debug("Extra end random value");
            n = rng.rand();
            int a = 1; // some value
            int b = 5; // some value
            int p = n % (b + 1 - a) + a;
        }

        return event;
    }

    /**
     * Randomize an item quantity
     */
    private static int itemOfferQuantity(FTLEvent event, RandRNG rng, String id) {

        FTLEvent.ItemList itemList = event.getItemList();
        if (itemList == null)
            return 0;

        for (int i = 0; i < itemList.items.size(); i++) {
            FTLEvent.Reward item = itemList.items.get(i);
            if (item.type.equals(id)) {
                if (item.max != 0) {
                    int r = item.max + 1 - item.min;
                    item.value = (rng.rand() % r) + item.min;
                    itemList.items.set(i, item);
                    event.setItemList(itemList);
                    log.debug(String.format("Random quantity of %s is %d", id, item.value));
                    return item.value;
                }
                return 0;
            }
        }
        return 0;
    }

    /**
     * Compute autoReward quantity
     */
    private static int autoRewardQuantity(RandRNG rng, String resource, int reward_level, int sector_level) {

        final float[] scrap_min = {0.5f, 0.8f, 1.3f};
        final float[] scrap_max = {0.7f, 1.3f, 1.55f};

        final int[] fuel_min = {1, 2, 3};
        final int[] fuel_max = {3, 4, 6};

        final int[] missiles_min = {1, 2, 4};
        final int[] missiles_max = {2, 4, 8};

        final int[] droneparts_min = {1, 1, 1};
        final int[] droneparts_max = {1, 1, 2};

        if (resource.equals("scrap")) {
            float min = scrap_min[reward_level];
            float max = scrap_max[reward_level];

            int range = ((int) (max * 1000.0f)) + 1 - (int) (min * 1000.0f);
            int qint = (int) (min * 1000.0f) + (rng.rand() % range);

            int q = (int) (((float) qint / 1000.0f) * ((float) (sector_level * 6 + 0xf)));
            log.debug(String.format("autoReward scrap: %d", q));
            return q;
        }

        int min = 0, max = 0;
        if (resource.equals("fuel")) {
            min = fuel_min[reward_level];
            max = fuel_max[reward_level];
        } else if (resource.equals("missiles")) {
            min = missiles_min[reward_level];
            max = missiles_max[reward_level];
        } else if (resource.equals("droneparts")) {
            min = droneparts_min[reward_level];
            max = droneparts_max[reward_level];
        }

        int r = min + (rng.rand() % (max + 1 - min));
        log.debug(String.format("autoReward %s: %d", resource, r));

        return r;
    }

    private static class ItemRarity {
        public String id = null;
        public int rarity = 0;
        public int rarityChildren = 0;
    }

    private static final List<ItemRarity> crewRarities = new ArrayList<ItemRarity>();

    /**
     * Pick a random weapon, accounting for rarity
     */
    private static CrewBlueprint pickRandomCrew(RandRNG rng) {
        Map<String, CrewBlueprint> crews = DataManager.getInstance().getCrews(dlcEnabled);

        /* Compute sum and binary tree of non-zero rarities */
        if (crewRarities.isEmpty()) {

            /* Use 1-based array */
            crewRarities.add(new ItemRarity());

            /* Sector data can overwrite rarities */
            SectorDescription tmpDesc = DataManager.getInstance().getSectorDescriptionById(sectorId);
            SectorDescription.RarityList rarityList = tmpDesc.getRarityList();
            List<SectorDescription.BlueprintRarity> blueprints = null;
            if (rarityList != null)
                blueprints = rarityList.blueprints;

            for (Map.Entry<String, CrewBlueprint> entry : crews.entrySet()) {
                int r = entry.getValue().getRarity();

                /* Check if a new rarity value is specified by sector description */
                if (blueprints != null) {
                    for (SectorDescription.BlueprintRarity b : blueprints) {
                        if (b.id.equals(entry.getKey())) {
                            r = b.rarity;
                            log.debug(String.format("crew %s rarity %d override", b.id, b.rarity));
                        }
                    }
                }

                if (r != 0) {
                    ItemRarity dr = new ItemRarity();
                    dr.id = entry.getKey();
                    dr.rarity = 6 - r;
                    dr.rarityChildren = 6 - r;
                    crewRarities.add(dr);
                }
            }

            /* Compute the binary tree */
            for (int i = crewRarities.size() - 1; i >= 1; i--) {
                crewRarities.get(i >> 1).rarityChildren += crewRarities.get(i).rarityChildren;
            }
        }

        /* Pick a crew with rarity */
        String id = pickRandomBinaryTree(rng, crewRarities);
        return crews.get(id);
    }


    private static final List<ItemRarity> weaponRarities = new ArrayList<ItemRarity>();

    /**
     * Pick a random weapon, accounting for rarity
     */
    private static WeaponBlueprint pickRandomWeapon(RandRNG rng) {
        Map<String, WeaponBlueprint> weapons = DataManager.getInstance().getWeapons(dlcEnabled);

        /* Compute sum and binary tree of non-zero rarities */
        if (weaponRarities.isEmpty()) {

            /* Use 1-based array */
            weaponRarities.add(new ItemRarity());

            /* Sector data can overwrite rarities */
            SectorDescription tmpDesc = DataManager.getInstance().getSectorDescriptionById(sectorId);
            SectorDescription.RarityList rarityList = tmpDesc.getRarityList();
            List<SectorDescription.BlueprintRarity> blueprints = null;
            if (rarityList != null)
                blueprints = rarityList.blueprints;

            for (Map.Entry<String, WeaponBlueprint> entry : weapons.entrySet()) {
                int r = entry.getValue().getRarity();

                /* Check if a new rarity value is specified by sector description */
                if (blueprints != null) {
                    for (SectorDescription.BlueprintRarity b : blueprints) {
                        if (b.id.equals(entry.getKey())) {
                            r = b.rarity;
                        }
                    }
                }

                if (r != 0) {
                    ItemRarity dr = new ItemRarity();
                    dr.id = entry.getKey();
                    dr.rarity = 6 - r;
                    dr.rarityChildren = 6 - r;
                    weaponRarities.add(dr);
                }
            }

            /* Compute the binary tree */
            for (int i = weaponRarities.size() - 1; i >= 1; i--) {
                weaponRarities.get(i >> 1).rarityChildren += weaponRarities.get(i).rarityChildren;
            }
        }

        /* Pick a weapon with rarity */
        String id = pickRandomBinaryTree(rng, weaponRarities);
        return weapons.get(id);
    }


    private static final List<ItemRarity> augRarities = new ArrayList<ItemRarity>();

    /**
     * Pick a random augment, accounting for rarity
     */
    private static AugBlueprint pickRandomAugment(RandRNG rng) {
        Map<String, AugBlueprint> augs = DataManager.getInstance().getAugments(dlcEnabled);

        /* Compute sum and binary tree of non-zero rarities */
        if (augRarities.isEmpty()) {

            /* Use 1-based array */
            augRarities.add(new ItemRarity());

            /* Sector data can overwrite rarities */
            SectorDescription tmpDesc = DataManager.getInstance().getSectorDescriptionById(sectorId);
            SectorDescription.RarityList rarityList = tmpDesc.getRarityList();
            List<SectorDescription.BlueprintRarity> blueprints = null;
            if (rarityList != null)
                blueprints = rarityList.blueprints;

            for (Map.Entry<String, AugBlueprint> entry : augs.entrySet()) {
                int r = entry.getValue().getRarity();

                /* Check if a new rarity value is specified by sector description */
                if (blueprints != null) {
                    for (SectorDescription.BlueprintRarity b : blueprints) {
                        if (b.id.equals(entry.getKey())) {
                            r = b.rarity;
                        }
                    }
                }

                if (r != 0) {
                    ItemRarity dr = new ItemRarity();
                    dr.id = entry.getKey();
                    dr.rarity = 6 - r;
                    dr.rarityChildren = 6 - r;
                    augRarities.add(dr);
                }
            }

            /* Compute the binary tree */
            for (int i = augRarities.size() - 1; i >= 1; i--) {
                augRarities.get(i >> 1).rarityChildren += augRarities.get(i).rarityChildren;
            }
        }

        /* Pick an augment with rarity */
        String id = pickRandomBinaryTree(rng, augRarities);
        return augs.get(id);
    }

    private static final List<ItemRarity> droneRarities = new ArrayList<ItemRarity>();

    /**
     * Pick a random drone, accounting for rarity
     */
    private static DroneBlueprint pickRandomDrone(RandRNG rng) {
        Map<String, DroneBlueprint> drones = DataManager.getInstance().getDrones(dlcEnabled);

        /* Compute sum and binary tree of non-zero rarities */
        if (droneRarities.isEmpty()) {

            /* Use 1-based array */
            droneRarities.add(new ItemRarity());

            /* Sector data can overwrite rarities */
            SectorDescription tmpDesc = DataManager.getInstance().getSectorDescriptionById(sectorId);
            SectorDescription.RarityList rarityList = tmpDesc.getRarityList();
            List<SectorDescription.BlueprintRarity> blueprints = null;
            if (rarityList != null)
                blueprints = rarityList.blueprints;

            for (Map.Entry<String, DroneBlueprint> entry : drones.entrySet()) {
                int r = entry.getValue().getRarity();

                /* Check if a new rarity value is specified by sector description */
                if (blueprints != null) {
                    for (SectorDescription.BlueprintRarity b : blueprints) {
                        if (b.id.equals(entry.getKey())) {
                            r = b.rarity;
                        }
                    }
                }

                if (r != 0) {
                    ItemRarity dr = new ItemRarity();
                    dr.id = entry.getKey();
                    dr.rarity = 6 - r;
                    dr.rarityChildren = 6 - r;
                    droneRarities.add(dr);
                }
            }

            /* Compute the binary tree */
            for (int i = droneRarities.size() - 1; i >= 1; i--) {
                droneRarities.get(i >> 1).rarityChildren += droneRarities.get(i).rarityChildren;
            }
        }

        /* Pick a drone with rarity */
        String id = pickRandomBinaryTree(rng, droneRarities);
        return drones.get(id);
    }

    /**
     * Pick a random item from a binary tree
     */
    private static String pickRandomBinaryTree(RandRNG rng, List<ItemRarity> itemRarities) {

        /* Pick a value among sum of rarities */
        int i = rng.rand() % itemRarities.get(1).rarityChildren;
        int j = 1;

        while (i >= itemRarities.get(j).rarity) {
            i -= itemRarities.get(j).rarity;
            j <<= 1;
            if (i >= itemRarities.get(j).rarityChildren) {
                i -= itemRarities.get(j).rarityChildren;
                j++;
            }
        }

        return itemRarities.get(j).id;
    }


}
