package ch.zhaw.ikitomo.settings.view;

import java.io.IOException;
import java.net.URL;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import ch.zhaw.ikitomo.exception.LoadUIException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class TomodachiListViewCell extends ListCell<TomodachiFile> {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label label;

    @FXML
    private ImageView tomodachiImage;

    private FXMLLoader fxmlLoader;

    public TomodachiListViewCell() {
    }

    @Override
    protected void updateItem(TomodachiFile item, boolean empty) {
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
            label.setText(item.getConfig().getName());
            if (isSelected()) {
                setStyle("-fx-background-color: -fx-focus-color;");
            } else {
                setStyle("-fx-background-color: -fx-background;");
            }
            setGraphic(rootPane);
        }
    }

    private void loadFromFXML() {
        URL location = getClass().getResource("tomodachi-list-view-cell.fxml");
        if (location == null) {
            throw new LoadUIException("Cannot find tomodachi-list-view-cell.fxml");
        }
        fxmlLoader = new FXMLLoader(location);
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
