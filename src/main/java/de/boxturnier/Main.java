package de.boxturnier;

import de.boxturnier.service.ParticipantRegistry;
import de.boxturnier.ui.ParticipantView;
import de.boxturnier.ui.WeighInView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Einstiegspunkt der Boxturnier-Verwaltung.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ParticipantRegistry registry = new ParticipantRegistry();

        Tab anmeldungTab = new Tab("Anmeldung", new ParticipantView(registry));
        anmeldungTab.setClosable(false);

        Tab wiegenTab = new Tab("Wiegen", new WeighInView(registry));
        wiegenTab.setClosable(false);

        TabPane tabPane = new TabPane(anmeldungTab, wiegenTab);

        stage.setTitle("Boxturnier-Software");
        stage.setScene(new Scene(tabPane, 950, 620));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
