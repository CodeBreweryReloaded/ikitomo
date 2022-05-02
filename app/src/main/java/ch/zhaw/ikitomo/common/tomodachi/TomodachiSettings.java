package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
     * Gets the chance to sleep
     *
     * @return The chance to sleep
     */
    public Float getSleepChance() {
        return sleepChance.get();
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
     * Gets the wake-up chance
     *
     * @return The chance to wake-up
     */
    public float getWakeChance() {
        return wakeChance.get();
    }

    /**
     * Sets the wake-up chance
     *
     * @param wakeChance The wake-up chance
     */
    public void setWakeChance(float wakeChance) {
        this.wakeChance.set(wakeChance);
    }

    /**
     * Gets a property holding the sleep chance
     *
     * @return The sleep chance property
     */
    public FloatProperty sleepChanceProperty() {
        return sleepChance;
    }

    /**
     * Gets a property holding the chance to wake up
     *
     * @return The wake up chance property
     */
    public FloatProperty wakeChanceProperty() {
        return wakeChance;
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
