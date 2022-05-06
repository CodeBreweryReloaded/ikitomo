package ch.zhaw.ikitomo.common;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * An utils class for javafx related helper methods
 */
public class JavaFXUtils {
    /**
     * Private constructor
     */
    private JavaFXUtils() {
    }

    /**
     * Runs the given runnable later and returns a {@link CompletableFuture} to void
     * until runnable was invoked
     * 
     * @param runnable The runnable to invoke later
     * @return The future to wait for
     */
    public static CompletableFuture<Void> runLater(Runnable runnable) {
        return supplyLaterWithException(() -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Runs the given supplier later and returns a {@link CompletableFuture} with
     * the returned value of the supplier
     * 
     * @param <T>      The returned type of the supplier
     * @param supplier The supplier to call
     * @return The future to wait for
     */
    public static <T> CompletableFuture<T> supplyLater(Supplier<T> supplier) {
        return supplyLaterWithException(supplier::get);
    }

    /**
     * Runs the given supplier later and returns a {@link CompletableFuture} with
     * the returned value of the supplier. The supplier can throw an exception. If
     * this happens the {@link CompletableFuture#completeExceptionally(Throwable)}
     * is called.
     * 
     * @param <T>      The returned type of the supplier
     * @param supplier The supplier to call
     * @return The future to wait for
     */
    public static <T> CompletableFuture<T> supplyLaterWithException(ExceptionalSupplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                future.complete(supplier.get());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    /**
     * An supplier interface which can throw an exception
     */
    @FunctionalInterface
    public static interface ExceptionalSupplier<T> {
        /**
         * Supplies the value
         * 
         * @return the value
         * @throws Exception if something went wrong
         */
        T get() throws Exception;
    }

    /**
     * Creates an unmodifiable {@link ObservableList} from the values of the given
     * map. The list is updated when the map updates
     * 
     * @param <K> The type of the key of the map
     * @param <T> The type of the value of the map
     * @param map The map to create the list from
     * @return The list which is dependent on the map
     */
    public static <K, T> ObservableList<T> observableValuesFromMap(ObservableMap<K, T> map) {
        Objects.requireNonNull(map, "ObservableMap must not be null");
        ObservableList<T> list = FXCollections.observableArrayList(map.values());
        MapChangeListener<K, T> listener = change -> {
            if (change.wasAdded()) {
                list.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                list.remove(change.getValueRemoved());
            }
        };
        map.addListener(listener);
        list.addListener(new ListChangeListener<>() {

            @Override
            public void onChanged(Change<? extends T> c) {
                System.out.println("added: " + c.getList());
            }

        });
        return FXCollections.unmodifiableObservableList(list);
    }
}
