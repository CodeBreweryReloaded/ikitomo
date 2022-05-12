package ch.zhaw.ikitomo.trayicon;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.IkitomoApplication;
import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.settings.SettingsController;
import javafx.application.Platform;

import javax.imageio.ImageIO;

/**
 * The tray icon controller
 */
public class TrayIconController implements Killable {

    /**
     * {@link Logger} of the application.
     */
    private static final Logger LOGGER = Logger.getLogger(TrayIconController.class.getName());

    /**
     * Instance of SettingsController
     */
    private SettingsController settingsController;

    @Override
    public CompletableFuture<Void> kill() {
        if (this.settingsController != null) {
            this.settingsController.kill();
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Constructor of Class TrayIconController
     * Checks if Tray is supported on the Operating System.
     * If supported the constructor will set the tray with the icon and clickevents.
     *
     * @param application The global application object
     */
    public TrayIconController(IkitomoApplication application) {
        if (!SystemTray.isSupported()) {
            LOGGER.log(Level.WARNING, "OS does not support SystemTray.");
            return;
        }

        try {
            final PopupMenu popup = new PopupMenu();

            URL iconUrl = TrayIconController.class.getResource("/icon.png");
            if (iconUrl == null) {
                LOGGER.log(Level.SEVERE, "Could not find icon.png in resources.");
                return;
            }
            final TrayIcon trayIcon = new TrayIcon(ImageIO.read(iconUrl));
            final SystemTray tray = SystemTray.getSystemTray();

            MenuItem showSettingsItem = new MenuItem("Settings");

            showSettingsItem.addActionListener(e -> {
                Platform.runLater(() -> {
                    if (this.settingsController == null || !settingsController.isVisible()) this.settingsController = SettingsController.newSettingsUI(application.getEnvironment());
                });
            });

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> {
                IkitomoApplication app = new IkitomoApplication();
                app.close();
            });

            popup.add(showSettingsItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (AWTException | IOException ioE) {
            LOGGER.log(Level.SEVERE,"An error occured during initialization of program.", ioE);
        }
    }

    /**
     * Creates and returns an instance of TrayIconController
     *
     * @param application The Ikitomo application itself
     * @return The new {@link TrayIconController}
     */
    public static TrayIconController newOverlayUI(IkitomoApplication application) {
        return new TrayIconController(application);
    }

}
