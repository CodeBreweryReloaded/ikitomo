package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

/**
 * Represents the user settings of a Tomodachi. These may also be modified by the user.
 */
public class TomodachiSettings {
    /**
     * The chance to sleep
     */
    private FloatProperty sleepChance = new SimpleFloatProperty();
    /**
     * The chance to wake up
     */
    private FloatProperty wakeChance = new SimpleFloatProperty();

    /**
     * Constructor
     */
    public TomodachiSettings() {
    }

    /**
     * Constructor
     * 
     * @param sleepChance The sleep chance
     * @param wakeChance  The wake up chance
     */
    public TomodachiSettings(float sleepChance, float wakeChance) {
        this.sleepChance.set(sleepChance);
        this.wakeChance.set(wakeChance);
    }

    /**
     * @return The sleep chance property
     */
    public FloatProperty sleepChanceProperty() {
        return sleepChance;
    }

    /**
     * Sets the sleep chance
     * 
     * @param sleepChance The sleep chance
     */
    public void setSleepChance(Float sleepChance) {
        this.sleepChance.set(sleepChance);
    }

    /**
     * 
     * @return The chance to sleep
     */
    public Float getSleepChance() {
        return sleepChance.get();
    }

    /**
     * @return The wake up chance property
     */
    public FloatProperty wakeChanceProperty() {
        return wakeChance;
    }

    /**
     * Sets the wake up chance property
     * 
     * @param wakeChance The wake up chance
     */
    public void setWakeChance(float wakeChance) {
        this.wakeChance.set(wakeChance);
    }

    /**
     * 
     * @return The chance to wake up
     */
    public float getWakeChance() {
        return wakeChance.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(sleepChance, wakeChance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TomodachiSettings)) {
            return false;
        }
        TomodachiSettings other = (TomodachiSettings) obj;
        return Objects.equals(sleepChance, other.sleepChance) && Objects.equals(wakeChance, other.wakeChance);
    }

}
