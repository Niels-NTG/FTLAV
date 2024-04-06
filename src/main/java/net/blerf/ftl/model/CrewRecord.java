package net.blerf.ftl.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class CrewRecord implements Comparable<CrewRecord> {

    private String name;
    private String race;
    private boolean male;
    private int value;

    /**
     * Copy constructor.
     */
    public CrewRecord(CrewRecord srcRec) {
        name = srcRec.getName();
        race = srcRec.getRace();
        male = srcRec.isMale();
        value = srcRec.getValue();
    }

    @Override
    public int compareTo(CrewRecord that) {
        return Integer.compare(this.value, that.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewRecord that = (CrewRecord) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("Name: %-20s  Race: %-9s  Sex: %s  Score: %4d%n", name, race, (male ? "M" : "F"), value);
    }
}
