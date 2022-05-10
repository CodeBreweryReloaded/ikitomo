package ch.zhaw.ikitomo.trayicon;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.settings.SettingsController;
import javafx.application.Platform;

import javax.imageio.ImageIO;

/**
 * The tray icon controller
 */
public class TrayIconController implements Killable {

    /**
     * Instance of SettingsController
     */
    private SettingsController settingsController;

    @Override
    public CompletableFuture<Void> kill() {
        if (settingsController != null) { }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Constructor of Class TrayIconController
     * Checks if Tray is supported on the Operating System.
     * If supported the constructor will set the tray with the icon and clickevents.
     *
     * @param environment
     */
    public TrayIconController(TomodachiEnvironment environment) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
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
                    if (this.settingsController == null || !settingsController.isVisible()) this.settingsController = SettingsController.newSettingsUI(environment);
                });
            });

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> {
                System.exit(0);
            });

            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(showSettingsItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (IOException | AWTException ioE) {
            ioE.printStackTrace();
        }
    }

    /**
     * Creates and returns an instance of TrayIconController
     *
     * @param environment The global environment object
     * @return The new {@link TrayIconController}
     */
    public static TrayIconController newOverlayUI(TomodachiEnvironment environment) {
        return new TrayIconController(environment);
    }

}
