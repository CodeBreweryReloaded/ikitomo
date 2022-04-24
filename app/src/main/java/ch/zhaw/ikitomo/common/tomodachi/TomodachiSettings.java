package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

/**
 * Represents the settings which the user can change of a tomodachi
 */
public class TomodachiSettings {
    /**
     * the chance to sleep
     */
    private FloatProperty sleepChance = new SimpleFloatProperty();
    /**
     * the chance to wake up
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
     * @param sleepChance the sleep chance
     * @param wakeChance  the wake up chance
     */
    public TomodachiSettings(float sleepChance, float wakeChance) {
        this.sleepChance.set(sleepChance);
        this.wakeChance.set(wakeChance);
    }

    /**
     * @return the sleep chance property
     */
    public FloatProperty sleepChanceProperty() {
        return sleepChance;
    }

    /**
     * Sets the sleep chance
     * 
     * @param sleepChance the sleep chance
     */
    public void setSleepChance(Float sleepChance) {
        this.sleepChance.set(sleepChance);
    }

    /**
     * 
     * @return the chance to sleep
     */
    public Float getSleepChance() {
        return sleepChance.get();
    }

    /**
     * @return the wake up chance property
     */
    public FloatProperty wakeChanceProperty() {
        return wakeChance;
    }

    /**
     * Sets the wake up chance property
     * 
     * @param wakeChance the wake up chance
     */
    public void setWakeChance(float wakeChance) {
        this.wakeChance.set(wakeChance);
    }

    /**
     * 
     * @return the chance to wake up
     */
    public float getWakeChance() {
        return wakeChance.get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(sleepChance, wakeChance);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

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
