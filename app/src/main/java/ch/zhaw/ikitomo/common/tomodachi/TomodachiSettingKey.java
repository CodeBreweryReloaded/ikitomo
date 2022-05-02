package ch.zhaw.ikitomo.common.tomodachi;

/**
 * Represents the key of a setting
 */
public abstract class TomodachiSettingKey {
    /**
     * Indicates the chance of the tomodachi to sleep
     */
    public static final String SLEEP_CHANCE = "sleepChance";

    /**
     * Indicates the chance of the tomodachi to wake up
     */
    public static final String WAKE_CHANCE = "wakeChance";

    /**
     * Initializes a new instance of the {@link TomodachiSettingKey} class
     */
    private TomodachiSettingKey() {
    }
}
