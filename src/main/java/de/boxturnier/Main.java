package de.boxturnier;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Einstiegspunkt der Boxturnier-Verwaltung.
 * Dies ist bewusst nur ein minimales "Hallo Welt" JavaFX-Fenster,
 * das als Startpunkt für die eigentliche Turnierverwaltung dient.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Boxturnier-Software – Startpunkt");
        Label hint = new Label(
                "Nächste Schritte: Teilnehmerverwaltung, Turnierbaum,\n" +
                "Gewichtsklassen-Zuordnung und Rundenzeiten umsetzen."
        );

        VBox root = new VBox(10, title, hint);
        root.setPadding(new Insets(20));

        stage.setTitle("Boxturnier-Software");
        stage.setScene(new Scene(root, 480, 240));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
