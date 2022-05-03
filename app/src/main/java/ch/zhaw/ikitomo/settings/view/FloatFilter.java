package ch.zhaw.ikitomo.settings.view;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class FloatFilter implements UnaryOperator<Change> {
    public static final Predicate<Float> IS_POSITIVE_PREDICATE = num -> num >= 0;
    private static final Pattern FLOAT_PATTERN = Pattern.compile("-?\\d*(\\.\\d*)?");
    private Predicate<Float> predicate = num -> true;

    public FloatFilter() {
    }

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

    public static TextFormatter<?> newFloatTextFormatter() {
        return new TextFormatter<>(new FloatFilter());
    }

    public static TextFormatter<?> newFloatTextFormatter(Predicate<Float> predicate) {
        return new TextFormatter<>(new FloatFilter(predicate));
    }

}
