package ch.zhaw.ikitomo.overlay;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OverlayUI extends Application {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        Pane pane = new Pane();
        Image image = new Image("file:Assets/neko-classic-dev/sprites/awake.png");
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-background-color: rgba(0,0,0,0);");
        pane.getChildren().add(imageView);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}
