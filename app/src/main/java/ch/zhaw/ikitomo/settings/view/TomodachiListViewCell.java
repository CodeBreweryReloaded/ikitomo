package ch.zhaw.ikitomo.settings.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.exception.LoadUIException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * A cell for a list showing {@link TomodachiDefinition}s
 */
public class TomodachiListViewCell extends ListCell<TomodachiDefinition> {
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(TomodachiListViewCell.class.getName());

    /**
     * The size the images should be scaled to. All images need to be scaled by
     * {@link Image} because {@link ImageView#setSmooth(boolean)} doesn't work. See
     * <a href=
     * "https://bugs.openjdk.java.net/browse/JDK-8211861">https://bugs.openjdk.java.net/browse/JDK-8211861</a>
     */
    private static final int IMAGE_SIZE = 300;

    /**
     * The root pane
     */
    @FXML
    private BorderPane rootPane;

    /**
     * the label showing the text
     */
    @FXML
    private Label label;
    /**
     * The image view showing an image of the tomodachi
     */
    @FXML
    private ImageView tomodachiImage;

    /**
     * An cache for the images
     */
    private Map<TomodachiDefinition, Image> imageCache = new HashMap<>();

    /**
     * Constructor
     */
    public TomodachiListViewCell() {
    }

    @Override
    protected void updateItem(TomodachiDefinition item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty || item == null) {
            setGraphic(null);
            setStyle("-fx-background-color: transparent;");
        } else {
            // if ui isn't loaded yet, load it
            if (label == null) {
                loadFromFXML();
            }
            label.setText(item.getName());
            if (isSelected()) {
                setStyle("-fx-background-color: -fx-focus-color;");
            } else {
                setStyle("-fx-background-color: -fx-background;");
            }
            tomodachiImage.setImage(loadIconImage(item));
            setGraphic(rootPane);
        }
    }

    /**
     * Loads the fxml file and initializes this instance as a controller
     */
    private void loadFromFXML() {
        URL location = getClass().getResource("tomodachi-list-view-cell.fxml");
        if (location == null) {
            throw new LoadUIException("Cannot find tomodachi-list-view-cell.fxml");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new LoadUIException("Cannot load tomodachi-list-view-cell.fxml", e);
        }
        rootPane.prefHeightProperty().bind(heightProperty().subtract(30));
        rootPane.prefWidthProperty().bind(rootPane.prefHeightProperty());
        tomodachiImage.fitHeightProperty().bind(rootPane.widthProperty());
        tomodachiImage.fitWidthProperty().bind(prefWidthProperty());
    }

    /**
     * Loads the icon image for the tomodachi
     * 
     * @param tomodachiDefinition The definition to load the icon for
     * @return The loaded image
     */
    private Image loadIconImage(TomodachiDefinition tomodachiDefinition) {
        if (tomodachiDefinition.getIcon() == null) {
            LOGGER.log(Level.WARNING, "No icon was set for tomodachi {0}", tomodachiDefinition.getName());
            return null;
        }
        if (imageCache.containsKey(tomodachiDefinition)) {
            return imageCache.get(tomodachiDefinition);
        }
        LOGGER.log(Level.INFO, "Load icon for tomodachi \"{0}\" from \"{1}\"",
                new Object[] { tomodachiDefinition.getName(), tomodachiDefinition.getIcon() });
        try {
            Image img = tomodachiDefinition.isResource() ? loadIconImageFromClasspath(tomodachiDefinition)
                    : loadIconImageFromFile(tomodachiDefinition);
            imageCache.put(tomodachiDefinition, img);
            return img;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn't load icon image of \"%s\" (from classpath: %s)"
                    .formatted(tomodachiDefinition.getID(), tomodachiDefinition.isResource()), e);
            return null;
        }
    }

    /**
     * Loads the icon for the given {@link TomodachiDefinition} from the classpath
     * 
     * @param tomodachiDefinition The definition to load the icon for
     * @return The loaded icon
     * @throws IOException If the icon could not be loaded
     */
    private Image loadIconImageFromClasspath(TomodachiDefinition tomodachiDefinition) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(tomodachiDefinition.getIcon());
        if (in == null) {
            throw new IOException("Couldn't load the icon image of \"" + tomodachiDefinition.getID() + "\" from \""
                    + tomodachiDefinition.getIcon() + "\"");
        }
        try (in) {
            return new Image(in, IMAGE_SIZE, IMAGE_SIZE, true, false);
        }
    }

    /**
     * Loads the icon for the given {@link TomodachiDefinition}
     * 
     * @param tomodachiDefinition The definition
     * @return The loaded image
     * @throws IOException If the file could not be loaded
     */
    private Image loadIconImageFromFile(TomodachiDefinition tomodachiDefinition) throws IOException {
        try (var in = new FileInputStream(tomodachiDefinition.getIcon())) {
            return new Image(in, IMAGE_SIZE, IMAGE_SIZE, true, false);
        }
    }
}
