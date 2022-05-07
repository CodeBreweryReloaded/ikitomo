package ch.zhaw.ikitomo.settings.view;

import java.io.IOException;
import java.net.URL;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.exception.LoadUIException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * A cell for a list showing {@link TomodachiDefinition}s
 */
public class TomodachiListViewCell extends ListCell<TomodachiDefinition> {

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

}
