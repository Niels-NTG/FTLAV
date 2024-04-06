package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.xml.ship.ShipEvent;

@Slf4j
@Getter
@Setter
@ToString(of = {"id", "load", "text"})
@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class FTLEvent implements Cloneable {
    @XmlAttribute(name = "name")
    private String id;

    @XmlAttribute
    private String load;

    @XmlAttribute
    private boolean unique;

    @XmlElement
    private NamedText text;

    @XmlElement(name = "img")
    private BackgroundImage image;

    @XmlElement(name = "choice")
    private List<Choice> choiceList;

    @XmlElement
    private ShipEvent ship;

    @XmlElement(name = "item_modify")
    private ItemList itemList;

    public static class Reward {
        @XmlAttribute
        public String type;

        @XmlAttribute
        public int min = 0;

        @XmlAttribute
        public int max = 0;

        public int value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ItemList {
        @XmlElement(name = "item")
        public List<Reward> items;
    }

    @XmlElement
    private AutoReward autoReward;

    @XmlAccessorType(XmlAccessType.NONE)
    public static class AutoReward {
        @XmlAttribute(name = "level")
        public String level;

        @XmlValue
        public String reward;

        public int scrap = 0;
        public int[] resources = {0, 0, 0};
        public String weapon = null;
        public String augment = null;
        public String drone = null;
    }

    @XmlElement
    private CrewMember crewMember;

    @XmlAccessorType(XmlAccessType.NONE)
    public static class CrewMember {
        @XmlAttribute
        public int amount = 0;

        @XmlAttribute(name = "class")
        public String id = null;

        @XmlAttribute
        public int weapons = -1;

        @XmlAttribute
        public int shields = -1;

        @XmlAttribute
        public int pilot = -1;

        @XmlAttribute
        public int engines = -1;

        @XmlAttribute
        public int combat = -1;

        @XmlAttribute
        public int repair = -1;

        @XmlAttribute(name = "all_skills")
        public int all_skills = -1;

        @XmlValue
        public String name = null;
    }

    @XmlElement
    private Item weapon = null;

    @XmlElement
    private Item augment = null;

    @XmlElement
    private Item drone = null;

    @XmlAccessorType(XmlAccessType.NONE)
    public static class Item {
        @XmlAttribute
        public String name = null;
    }

    @XmlElement
    private Boarders boarders = null;

    @XmlAccessorType(XmlAccessType.NONE)
    public static class Boarders {
        @XmlAttribute
        public int min = 0;

        @XmlAttribute
        public int max = 0;

        @XmlAttribute(name = "class")
        public String name = null;
    }


    public Object clone() {
        FTLEvent o = null;
        try {
            o = (FTLEvent) super.clone();
        } catch (CloneNotSupportedException cnse) {
            log.error("Failed to clone. ", cnse);
        }
        if (o == null) {
            o = new FTLEvent();
        }
        o.id = id;
        o.load = load;
        o.unique = unique;

        if (text != null) {
            o.text = text.toBuilder().build();
        }

        if (choiceList != null) {
            o.choiceList = new ArrayList<>(choiceList.size());
            for (Choice c : choiceList) {
                Choice newC = new Choice();
                newC.setHidden(c.isHidden());
                newC.setReq(c.getReq());
                newC.setLevel(c.getLevel());
                if (c.getText() != null)
                    newC.setText(c.getText().toBuilder().build());
                if (c.getEvent() != null)
                    newC.setEvent((FTLEvent) c.getEvent().clone());
                o.choiceList.add(newC);
            }
        }
        if (ship != null) {
            o.setShip(ship.toBuilder().build());
        }

        if (itemList != null) {
            ItemList il = new ItemList();
            ItemList oil = o.getItemList();

            il.items = new ArrayList<>(oil.items.size());
            for (Reward i : oil.items) {
                Reward newI = new Reward();
                newI.type = i.type;
                newI.min = i.min;
                newI.max = i.max;
                il.items.add(newI);
            }
            o.setItemList(il);
        }

        if (autoReward != null) {
            o.autoReward = new AutoReward();
            o.autoReward.level = autoReward.level;
            o.autoReward.reward = autoReward.reward;
        }

        if (crewMember != null) {
            o.crewMember = new CrewMember();
            o.crewMember.amount = crewMember.amount;
            o.crewMember.id = crewMember.id;
            o.crewMember.weapons = crewMember.weapons;
            o.crewMember.shields = crewMember.shields;
            o.crewMember.pilot = crewMember.pilot;
            o.crewMember.engines = crewMember.engines;
            o.crewMember.combat = crewMember.combat;
            o.crewMember.repair = crewMember.repair;
            o.crewMember.all_skills = crewMember.all_skills;
            o.crewMember.name = crewMember.name;
        }

        if (weapon != null) {
            o.weapon = new Item();
            o.weapon.name = weapon.name;
        }

        if (augment != null) {
            o.augment = new Item();
            o.augment.name = augment.name;
        }

        if (drone != null) {
            o.drone = new Item();
            o.drone.name = drone.name;
        }

        if (boarders != null) {
            o.boarders = new Boarders();
            o.boarders.min = boarders.min;
            o.boarders.max = boarders.max;
            o.boarders.name = boarders.name;
        }

        return o;

//        Field[] declaredFields = this.getClass().getDeclaredFields();
//        FTLEvent other = new FTLEvent();
//        try {
//            for (Field field : declaredFields) {
//                Object f = field.get(this);
//                field.set(other, f);
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        FTLEvent o = toBuilder().build();
//        o.setText(getText().toBuilder().build());
//        o.setShip(getShip().toBuilder().build());
//        o.setAutoReward(getAutoReward().toBuilder().build());
//        o.setCrewMember(getCrewMember().toBuilder().build());
//        o.setWeapon(getWeapon().toBuilder().build());
//        o.setAugment(getAugment().toBuilder().build());
//        o.setDrone(getDrone().toBuilder().build());
//        o.setBoarders(getBoarders().toBuilder().build());
    }

    private StringBuilder indent(StringBuilder sb, int level) {
        sb.append(new String(new char[level]).replaceAll("\0", "    "));
        return sb;
    }

    public String toDescription(int level) {
        StringBuilder sb = new StringBuilder();
        if (id != null)
            indent(sb, level).append("id: ").append(id).append("\n");

        if (unique)
            indent(sb, level).append("unique: true\n");

        if (text != null)
            indent(sb, level).append("text: ").append(text.getText()).append("\n");

        if (ship != null)
            indent(sb, level).append("ship: ").append(ship.toString()).append("\n");

        if (itemList != null)
            for (Reward i : itemList.items)
                indent(sb, level).append("item_modify: ").append(i.type).append(" with quantity ").append(i.value).append("\n");

        if (autoReward != null) {
            indent(sb, level).append("autoreward level ").append(autoReward.level).append(" and reward ").append(autoReward.reward).append(":\n");
            indent(sb, level + 1).append("scrap: ").append(autoReward.scrap).append("\n");
            indent(sb, level + 1).append("fuel: ").append(autoReward.resources[0]).append("\n");
            indent(sb, level + 1).append("missiles: ").append(autoReward.resources[1]).append("\n");
            indent(sb, level + 1).append("droneparts: ").append(autoReward.resources[2]).append("\n");
            if (autoReward.weapon != null)
                indent(sb, level + 1).append("weapon: ").append(autoReward.weapon).append("\n");
            if (autoReward.augment != null)
                indent(sb, level + 1).append("augment: ").append(autoReward.augment).append("\n");
            if (autoReward.drone != null)
                indent(sb, level + 1).append("drone: ").append(autoReward.drone).append("\n");
        }

        if (weapon != null)
            indent(sb, level).append("weapon: ").append(weapon.name).append("\n");

        if (augment != null)
            indent(sb, level).append("augment: ").append(augment.name).append("\n");

        if (drone != null)
            indent(sb, level).append("drone: ").append(drone.name).append("\n");

        if (boarders != null) {
            indent(sb, level).append("boarders: ").append("\n");
            indent(sb, level + 1).append("min: ").append(boarders.min).append("\n");
            indent(sb, level + 1).append("max: ").append(boarders.max).append("\n");
            indent(sb, level + 1).append("class: ").append(boarders.name).append("\n");
        }

        if (crewMember != null) {
            indent(sb, level).append("crew: ").append("\n");
            indent(sb, level + 1).append("amount: ").append(crewMember.amount).append("\n");
            if (crewMember.id != null)
                indent(sb, level + 1).append("id: ").append(crewMember.id).append("\n");
            indent(sb, level + 1).append("weapons: ").append(crewMember.weapons).append("\n");
            indent(sb, level + 1).append("shields: ").append(crewMember.shields).append("\n");
            indent(sb, level + 1).append("pilot: ").append(crewMember.pilot).append("\n");
            indent(sb, level + 1).append("engines: ").append(crewMember.engines).append("\n");
            indent(sb, level + 1).append("combat: ").append(crewMember.combat).append("\n");
            indent(sb, level + 1).append("repair: ").append(crewMember.repair).append("\n");
            indent(sb, level + 1).append("all_skills: ").append(crewMember.all_skills).append("\n");
            if (crewMember.name != null)
                indent(sb, level + 1).append("name: ").append(crewMember.name).append("\n");
        }

        sb.append("\n");

        if (choiceList != null) {
            for (Choice c : choiceList) {
                indent(sb, level).append("choice:\n");
                sb.append(c.toDescription(level + 1));
            }
        }

        return sb.toString();
    }

}
