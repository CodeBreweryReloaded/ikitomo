package ch.zhaw.ikitomo.settings.view;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

/**
 * An unary operator for a {@link TextFormatter} which only allows float
 * numbers. Additionally a predicate can be given which will further restrict
 * the allowed numbers. An empty string is always allowed for convenience of the
 * user.
 */
public class FloatFilter implements UnaryOperator<Change> {
    /**
     * The pattern for recognizing a float number
     */
    private static final Pattern FLOAT_PATTERN = Pattern.compile("\\d*(\\.\\d*)?");
    /**
     * The predicate which restricts the allowed numbers. By default all floats are
     * allowed
     */
    private Predicate<Float> predicate = num -> true;

    /**
     * Constructor
     */
    public FloatFilter() {
    }

    /**
     * Constructor
     * 
     * @param predicate A predicate to further restirct the allowed numbers
     */
    public FloatFilter(Predicate<Float> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Change apply(Change t) {
        String newText = t.getControlNewText();
        if (newText.isEmpty()) {
            return t;
        }
        Matcher matcher = FLOAT_PATTERN.matcher(newText);
        if (matcher.matches() && predicate.test(Float.parseFloat(newText))) {
            return t;
        }
        return null;
    }

    /**
     * Creates a new TextFormatter which only allows float numbers
     * 
     * @return The created {@link TextFormatter} with a new {@link FloatFilter}
     */
    public static TextFormatter<?> newFloatTextFormatter() {
        return new TextFormatter<>(new FloatFilter());
    }

    /**
     * Creates a new TextFormatter which only allows float numbers. The given
     * predicate is used to further restrict the allowed numbers
     * 
     * @param predicate The predicate to restrict the allowed numbers
     * @return The created {@link TextFormatter} with a new {@link FloatFilter}
     */
    public static TextFormatter<?> newFloatTextFormatter(Predicate<Float> predicate) {
        return new TextFormatter<>(new FloatFilter(predicate));
    }

}
