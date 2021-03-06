package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.zhaw.ikitomo.behavior.NextPositionStrategy;
import ch.zhaw.ikitomo.behavior.NextPositionStrategyFactory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Represents the user settings of a Tomodachi. These may also be modified by
 * the user.
 */
public class TomodachiSettings {
    /**
     * The chance to sleep
     */
    @JsonIgnore
    private DoubleProperty sleepChance = new SimpleDoubleProperty();

    /**
     * The chance to wake up
     */
    @JsonIgnore
    private DoubleProperty wakeChance = new SimpleDoubleProperty();

    /**
     * The speed of the tomodachi
     */
    @JsonIgnore
    private DoubleProperty speed = new SimpleDoubleProperty();

    /**
     * The factory for the {@link NextPositionStrategy} used. It defaults to
     * {@link NextPositionStrategyFactory#FOLLOW_MOUSE}
     */
    @JsonIgnore
    private ObjectProperty<NextPositionStrategyFactory> nextPositionStrategyFactory = new SimpleObjectProperty<>(
            NextPositionStrategyFactory.FOLLOW_MOUSE);

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
    public TomodachiSettings(double sleepChance, double wakeChance, double speed) {
        this.sleepChance.set(sleepChance);
        this.wakeChance.set(wakeChance);
        this.sleepChance.set(speed);
    }

    /**
     * Gets the chance to sleep
     *
     * @return The chance to sleep
     */
    @JsonProperty(TomodachiSettingKey.SLEEP_CHANCE)
    public double getSleepChance() {
        return sleepChance.get();
    }

    /**
     * Sets the sleep chance
     * 
     * @param sleepChance The sleep chance
     */
    @JsonProperty(TomodachiSettingKey.SLEEP_CHANCE)
    public void setSleepChance(double sleepChance) {
        this.sleepChance.set(sleepChance);
    }

    /**
     * Gets the wake-up chance
     *
     * @return The chance to wake-up
     */
    @JsonProperty(TomodachiSettingKey.WAKE_CHANCE)
    public double getWakeChance() {
        return wakeChance.get();
    }

    /**
     * Sets the wake-up chance
     *
     * @param wakeChance The wake-up chance
     */
    @JsonProperty(TomodachiSettingKey.WAKE_CHANCE)
    public void setWakeChance(double wakeChance) {
        this.wakeChance.set(wakeChance);
    }

    /**
     * Gets a property holding the sleep chance
     *
     * @return The sleep chance property
     */
    public DoubleProperty sleepChanceProperty() {
        return sleepChance;
    }

    /**
     * Gets a property holding the chance to wake up
     *
     * @return The wake up chance property
     */
    public DoubleProperty wakeChanceProperty() {
        return wakeChance;
    }

    /**
     * Gets the property holding the speed of the tomodachi
     * 
     * @return The property
     */
    public DoubleProperty speedProperty() {
        return speed;
    }

    /**
     * Sets the speed of the tomodachi
     * 
     * @param speed The speed
     */
    @JsonProperty(TomodachiSettingKey.SPEED)
    public void setSpeed(double speed) {
        this.speed.set(speed);
    }

    /**
     * Gets the speed of the tomodachi
     * 
     * @return The speed
     */
    @JsonProperty(TomodachiSettingKey.SPEED)
    public double getSpeed() {
        return speed.get();
    }

    /**
     * Gets the property to the factory for the {@link NextPositionStrategy}
     * 
     * @return The property
     */
    @JsonIgnore
    public ObjectProperty<NextPositionStrategyFactory> nextPositionStrategyFactoryProperty() {
        return nextPositionStrategyFactory;
    }

    /**
     * Sets the factory for the {@link NextPositionStrategy}
     * 
     * @param nextPositionStrategyFactory The new factory
     */
    @JsonProperty(TomodachiSettingKey.NEXT_POSITION_STRATEGY)
    public void setNextPositionStrategyFactory(NextPositionStrategyFactory nextPositionStrategyFactory) {
        this.nextPositionStrategyFactory.set(nextPositionStrategyFactory);
    }

    /**
     * Gets the factory for the {@link NextPositionStrategy}
     * 
     * @return The factory
     */
    @JsonProperty(TomodachiSettingKey.NEXT_POSITION_STRATEGY)
    public NextPositionStrategyFactory getNextPositionStrategyFactory() {
        return nextPositionStrategyFactory.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextPositionStrategyFactory, sleepChance, wakeChance);
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
        return Objects.equals(nextPositionStrategyFactory, other.nextPositionStrategyFactory)
                && Objects.equals(sleepChance, other.sleepChance) && Objects.equals(wakeChance, other.wakeChance);
    }

}
