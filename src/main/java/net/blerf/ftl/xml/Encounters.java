package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.xml.ship.ShipEvent;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "events")
@XmlAccessorType(XmlAccessType.FIELD)
public class Encounters {

    @XmlElement(name = "event")
    private List<FTLEvent> events = new ArrayList<>();

    @XmlElement(name = "eventList")
    private List<FTLEventList> eventLists = new ArrayList<>();

    @XmlElement(name = "textList")
    private List<TextList> textLists = new ArrayList<>();

    @XmlElement(name = "ship")
    private List<ShipEvent> shipEvents = new ArrayList<>();

    /**
     * Returns an Event with a given id.
     * <p>
     * Events and EventLists share a namespace,
     * so an id could belong to either.
     */
    public FTLEvent getEventById(String id) {
        if (id == null || events == null) return null;

        FTLEvent result = null;
        for (FTLEvent tmpEvent : events) {
            if (id.equals(tmpEvent.getId())) result = tmpEvent;
        }

        return result;
    }

    /**
     * Returns an EventList with a given id.
     * <p>
     * Events and EventLists share a namespace,
     * so an id could belong to either.
     */
    public FTLEventList getEventListById(String id) {
        if (id == null || eventLists == null) return null;

        FTLEventList result = null;
        for (FTLEventList tmpEventList : eventLists) {
            if (id.equals(tmpEventList.getId())) result = tmpEventList;
        }

        return result;
    }

    /**
     * Returns an TextList with a given id.
     */
    public TextList getTextListById(String id) {
        if (id == null || textLists == null) return null;

        TextList result = null;
        for (TextList tmpTextList : textLists) {
            if (id.equals(tmpTextList.getId())) result = tmpTextList;
        }

        return result;
    }

    /**
     * Returns a ShipEvent with a given id.
     * <p>
     * Events and EventLists share a namespace,
     * so an id could belong to either.
     */
    public ShipEvent getShipEventById(String id) {
        if (id == null || shipEvents == null) return null;

        ShipEvent result = null;
        for (ShipEvent tmpEvent : shipEvents) {
            if (id.equals(tmpEvent.getId())) result = tmpEvent;
        }

        return result;
    }
}
