package de.boxturnier;

import de.boxturnier.service.ParticipantRegistry;
import de.boxturnier.ui.ParticipantView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Einstiegspunkt der Boxturnier-Verwaltung.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ParticipantRegistry registry = new ParticipantRegistry();

        ParticipantView participantView = new ParticipantView(registry);

        stage.setTitle("Boxturnier-Software – Teilnehmerverwaltung");
        stage.setScene(new Scene(participantView, 900, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
