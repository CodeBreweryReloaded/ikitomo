package ch.zhaw.ikitomo.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Tests for {@link JavaFXUtils}
 */
class JavaFXUtilsTest {
    /**
     * Tests if {@link JavaFXUtils#observableValuesFromMap(ObservableMap)} works
     * correctly
     */
    @Test
    void testObservableValuesFromMap() {
        ObservableMap<String, Integer> map = FXCollections.observableHashMap();
        map.put("test1", 1);
        ObservableList<Integer> list = JavaFXUtils.observableValuesFromMap(map);
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
}
