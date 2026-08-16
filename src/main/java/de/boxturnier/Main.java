package de.boxturnier;

import de.boxturnier.service.ParticipantRegistry;
import de.boxturnier.ui.KampfplanView;
import de.boxturnier.ui.ParticipantView;
import de.boxturnier.ui.TournamentBracketView;
import de.boxturnier.ui.WeighInView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Einstiegspunkt der Boxturnier-Verwaltung.
 *
 * Hauptmodus: Turnier/Nachwuchsveranstaltung/Staffelkampf mit Einzelkämpfen
 * (jeder Boxer boxt genau einmal, Paarungen werden manuell festgelegt, siehe
 * Reiter "Kampfplan"). Der Reiter "Turnierbaum (Meisterschaft)" ist ein
 * separater Baustein für spätere Meisterschaften mit automatischem K.o.-System
 * und mehreren Runden pro Boxer.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ParticipantRegistry registry = new ParticipantRegistry();

        Tab anmeldungTab = new Tab("Anmeldung", new ParticipantView(registry));
        anmeldungTab.setClosable(false);

        Tab wiegenTab = new Tab("Wiegen", new WeighInView(registry));
        wiegenTab.setClosable(false);

        Tab kampfplanTab = new Tab("Kampfplan", new KampfplanView(registry));
        kampfplanTab.setClosable(false);

        Tab bracketTab = new Tab("Turnierbaum (Meisterschaft)", new TournamentBracketView(registry));
        bracketTab.setClosable(false);

        TabPane tabPane = new TabPane(anmeldungTab, wiegenTab, kampfplanTab, bracketTab);

        stage.setTitle("Boxturnier-Software");
        stage.setScene(new Scene(tabPane, 1050, 650));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
