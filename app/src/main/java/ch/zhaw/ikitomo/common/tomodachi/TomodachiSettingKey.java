package ch.zhaw.ikitomo.common.tomodachi;

/**
 * Represents the key of a setting
 */
public class TomodachiSettingKey {
    /**
     * Indicates the chance of the tomodachi to sleep
     */
    public static final String SLEEP_CHANCE = "sleepChance";

    /**
     * Indicates the chance of the tomodachi to wake up
     */
    public static final String WAKE_CHANCE = "wakeChance";

    /**
     * The key to the speed of the tomodachi
     */
    public static final String SPEED = "speed";

    /**
     * The property name of the
     * {@link ch.zhaw.ikitomo.behavior.NextPositionStrategyFactory} instance
     */
    public static final String NEXT_POSITION_STRATEGY = "nextPositionStrategy";

    /**
     * Initializes a new instance of the {@link TomodachiSettingKey} class
     */
    private TomodachiSettingKey() {
    }
}
