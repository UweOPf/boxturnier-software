package de.boxturnier.ui;

import de.boxturnier.model.Bout;
import de.boxturnier.model.Boxer;
import de.boxturnier.service.ParticipantRegistry;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * Kampfplan für Veranstaltungen mit Einzelkämpfen (Turnier/Nachwuchsveranstaltung/
 * Staffelkampf): jeder Boxer boxt genau einmal, die Paarung (Rot gegen Blau) wird
 * manuell vom Veranstalter festgelegt – kein automatischer K.o.-Baum.
 *
 * Bewusst ohne Fehlerbehandlung: setzt voraus, dass zwei unterschiedliche,
 * bereits angemeldete Boxer gewählt werden.
 */
public class KampfplanView extends BorderPane {

    private final ParticipantRegistry registry;
    private final ComboBox<Boxer> redCombo = new ComboBox<>();
    private final ComboBox<Boxer> blueCombo = new ComboBox<>();
    private final ComboBox<Integer> judgesCombo = new ComboBox<>();
    private final TableView<Bout> table = new TableView<>();

    public KampfplanView(ParticipantRegistry registry) {
        this.registry = registry;
        setPadding(new Insets(15));
        setLeft(buildForm());
        setCenter(buildTable());
    }

    private StringConverter<Boxer> boxerConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Boxer boxer) {
                if (boxer == null) return "";
                String weightClass = boxer.getAssignedWeightCategory() != null
                        ? boxer.getAssignedWeightCategory().name() : "keine Gewichtsklasse";
                return boxer.getFullName() + " (" + boxer.getClub() + ", " + weightClass + ")";
            }

            @Override
            public Boxer fromString(String string) {
                return null; // nicht benötigt, ComboBox ist nicht editierbar
            }
        };
    }

    private VBox buildForm() {
        redCombo.setItems(registry.getParticipants());
        redCombo.setConverter(boxerConverter());
        redCombo.setPromptText("Rote Ecke wählen");

        blueCombo.setItems(registry.getParticipants());
        blueCombo.setConverter(boxerConverter());
        blueCombo.setPromptText("Blaue Ecke wählen");

        judgesCombo.getItems().addAll(3, 5);
        judgesCombo.setValue(3);

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(8);
        grid.addRow(0, new Label("Rote Ecke:"), redCombo);
        grid.addRow(1, new Label("Blaue Ecke:"), blueCombo);
        grid.addRow(2, new Label("Punktrichter:"), judgesCombo);

        Button refreshButton = new Button("Teilnehmerliste aktualisieren");
        refreshButton.setOnAction(e -> {
            redCombo.setItems(null);
            redCombo.setItems(registry.getParticipants());
            blueCombo.setItems(null);
            blueCombo.setItems(registry.getParticipants());
        });

        Button createButton = new Button("Kampf anlegen");
        createButton.setDefaultButton(true);
        createButton.setOnAction(e -> {
            registry.addFight(redCombo.getValue(), blueCombo.getValue(), judgesCombo.getValue());
            redCombo.setValue(null);
            blueCombo.setValue(null);
        });

        VBox box = new VBox(12, new Label("Neuen Kampf anlegen"), grid, refreshButton, createButton);
        box.setPrefWidth(300);
        box.setPadding(new Insets(0, 20, 0, 0));
        return box;
    }

    private VBox buildTable() {
        TableColumn<Bout, String> redCol = new TableColumn<>("Rote Ecke");
        redCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRedCorner().getFullName()));

        TableColumn<Bout, String> redClubCol = new TableColumn<>("Verein (Rot)");
        redClubCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRedCorner().getClub()));

        TableColumn<Bout, String> blueCol = new TableColumn<>("Blaue Ecke");
        blueCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getBlueCorner().getFullName()));

        TableColumn<Bout, String> blueClubCol = new TableColumn<>("Verein (Blau)");
        blueClubCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getBlueCorner().getClub()));

        TableColumn<Bout, String> ageCategoryCol = new TableColumn<>("Altersklasse");
        ageCategoryCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getAgeCategory().getLabel()));

        TableColumn<Bout, String> judgesCol = new TableColumn<>("Punktrichter");
        judgesCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                String.valueOf(data.getValue().getNumberOfJudges())));

        TableColumn<Bout, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getDecision() != null ? "entschieden" : "geplant"));

        table.getColumns().addAll(java.util.List.of(
                redCol, redClubCol, blueCol, blueClubCol, ageCategoryCol, judgesCol, statusCol));
        table.setItems(registry.getFights());
        table.setPlaceholder(new Label("Noch keine Kämpfe angelegt."));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        javafx.scene.layout.VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        VBox box = new VBox(8, new Label("Kampfplan"), table);
        return box;
    }
}
