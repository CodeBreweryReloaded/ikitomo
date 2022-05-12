package ch.zhaw.ikitomo.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Tests for {@link JFXUtils}
 */
class JavaFXUtilsTest {
    /**
     * Tests if {@link JFXUtils#observableValuesFromMap(ObservableMap)} works
     * correctly
     */
    @Test
    void testObservableValuesFromMap() {
        ObservableMap<String, Integer> map = FXCollections.observableHashMap();
        map.put("test1", 1);
        ObservableList<Integer> list = JFXUtils.observableValuesFromMap(map);
        assertTrue(list.contains(1));

        // check that list is updated when map changes
        map.put("test3", 3);
        assertTrue(list.contains(3));

        // update test3 key
        map.put("test3", 5);
        assertFalse(list.contains(3));
        assertTrue(list.contains(5));

        // remove test3 key
        assertEquals(5, map.remove("test3"));
        assertFalse(list.contains(5));

        // check that list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> list.add(10));
    }

    /**
     * Tests if the nested properties of {@link JFXUtils} work
     */
    @Test
    void testNestedBinding() {
        // an object with a nested property is needed for the tests
        record NestedObject(StringProperty prop) {
        }

        // initialize objects
        var nestedObj = new NestedObject(new SimpleStringProperty("1"));
        var nestedObj2 = new NestedObject(new SimpleStringProperty("2"));

        // initialize properties and bindings
        var value = new SimpleObjectProperty<>(nestedObj);
        var nestedBinding = JFXUtils.nestedBinding(value, obj -> obj == null ? null : obj.prop);

        // start testing
        assertEquals("1", nestedBinding.get());
        nestedObj.prop.set("3");
        assertEquals("3", nestedBinding.get());
        value.set(nestedObj2);
        assertEquals("2", nestedBinding.get());

        value.set(null);
        assertNull(nestedBinding.get());
        value.set(nestedObj);
        assertEquals("3", nestedBinding.get());
        nestedObj.prop.set(null);
        assertNull(nestedBinding.get());
    }
}
