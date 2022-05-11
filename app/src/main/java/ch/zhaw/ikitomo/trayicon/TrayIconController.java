package ch.zhaw.ikitomo.trayicon;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ch.zhaw.ikitomo.IkitomoApplication;
import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.settings.SettingsController;
import javafx.application.Platform;

/**
 * The tray icon controller
 */
public class TrayIconController implements Killable {

    /**
     * Instance of SystemTray
     */
    private SystemTray tray;

    /**
     * Instance of TrayIcon
     */
    private TrayIcon trayIcon;

    /**
     * Instance of PopupMenu
     */
    private PopupMenu popup;

    /**
     * {@link Logger} of the application.
     */
    private static final Logger LOGGER = Logger.getLogger(TrayIconController.class.getName());

    /**
     * Instance of SettingsController
     */
    private SettingsController settingsController;

    /**
     * The application launcher instance
     */
    private IkitomoApplication application;

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
        this.application = application;

        try {
            popup = new PopupMenu();

            URL iconUrl = TrayIconController.class.getResource("/icon.png");
            if (iconUrl == null) {
                LOGGER.log(Level.SEVERE, "Could not find icon.png in resources.");
                return;
            }
            trayIcon = new TrayIcon(ImageIO.read(iconUrl));
            tray = SystemTray.getSystemTray();

            MenuItem showSettingsItem = new MenuItem("Settings");
            showSettingsItem.addActionListener(e -> showSettings());

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> application.close());

            popup.add(showSettingsItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (AWTException | IOException ioE) {
            LOGGER.log(Level.SEVERE, "An error occured during initialization of program.", ioE);
        }
    }

    @Override
    public CompletableFuture<Void> kill() {
        if (this.settingsController != null) {
            this.settingsController.kill();
        }
        tray.remove(trayIcon);

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Displays SettingsController
     */
    public void showSettings() {
        Platform.runLater(() -> {
            if (this.settingsController == null || !settingsController.isVisible()) {
                this.settingsController = SettingsController.newSettingsUI(application.getEnvironment());
            }
        });
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
