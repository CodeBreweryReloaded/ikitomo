package ch.zhaw.ikitomo.trayicon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.OverlayController;
import ch.zhaw.ikitomo.settings.SettingsController;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

/**
 * The tray icon controller
 */
public class TrayIconController implements Killable {

    private SettingsController settingsController;

    @Override
    public CompletableFuture<Void> kill() {


        if (settingsController != null) {

        }

        return CompletableFuture.completedFuture(null);
    }

    public TrayIconController(TomodachiEnvironment environment) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
        }

        try {

            final PopupMenu popup = new PopupMenu();
            final TrayIcon trayIcon =
                    new TrayIcon(ImageIO.read(TrayIconController.class.getResource("/icon.png")));
            final SystemTray tray = SystemTray.getSystemTray();

            MenuItem aboutItem = new MenuItem("About");
            MenuItem showSettingsItem = new MenuItem("Settings");

            showSettingsItem.addActionListener(e -> {
                Platform.runLater(() -> {
                    this.settingsController = SettingsController.newSettingsUI(environment);
                    settingsController.isVisible();
                });
            });

            MenuItem exitItem = new MenuItem("Exit");

            //Add components to pop-up menu
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(showSettingsItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);

        } catch (IOException ioE) {
            ioE.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static TrayIconController newOverlayUI(TomodachiEnvironment environment) {
        return new TrayIconController(environment);
    }

}
