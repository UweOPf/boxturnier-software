package de.boxturnier.ui;

import de.boxturnier.model.Boxer;
import de.boxturnier.service.ParticipantRegistry;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

/**
 * Wiege-Ablauf: zeigt je Teilnehmer das bei der Anmeldung gemeldete Gewicht
 * neben dem tatsächlichen Gewicht von der Waage. Die Gewichtsklasse wird beim
 * Wiegen final auf Basis des Waage-Gewichts bestimmt (§20 DBV-Bestimmungen).
 *
 * Bewusst ohne Fehlerbehandlung: es wird von korrekter Zahlen-Eingabe ausgegangen.
 */
public class WeighInView extends VBox {

    private final ParticipantRegistry registry;
    private final TableView<Boxer> table = new TableView<>();

    public WeighInView(ParticipantRegistry registry) {
        super(8);
        this.registry = registry;
        setPadding(new Insets(15));
        Label hint = new Label("Doppelklick auf 'Waage-Gewicht', Wert eingeben, Enter drücken.");
        getChildren().addAll(new Label("Wiegen – Meldegewicht vs. Waage-Gewicht"), hint, buildTable());
    }

    private TableView<Boxer> buildTable() {
        TableColumn<Boxer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFullName()));

        TableColumn<Boxer, String> ageCategoryCol = new TableColumn<>("Altersklasse");
        ageCategoryCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getAssignedAgeCategory().getLabel()));

        TableColumn<Boxer, String> reportedWeightCol = new TableColumn<>("Meldegewicht");
        reportedWeightCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                String.format("%.1f kg", data.getValue().getOfficialWeightKg())));

        TableColumn<Boxer, String> scaleWeightCol = new TableColumn<>("Waage-Gewicht (kg)");
        scaleWeightCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getScaleWeightKg() != null
                        ? String.format("%.1f", data.getValue().getScaleWeightKg()) : ""));
        scaleWeightCol.setCellFactory(TextFieldTableCell.forTableColumn());
        scaleWeightCol.setEditable(true);
        scaleWeightCol.setOnEditCommit(event -> {
            double scaleWeight = Double.parseDouble(event.getNewValue().trim().replace(",", "."));
            registry.recordWeighIn(event.getRowValue(), scaleWeight);
            table.refresh();
        });

        TableColumn<Boxer, String> weightCategoryCol = new TableColumn<>("Gewichtsklasse (verbindlich)");
        weightCategoryCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getAssignedWeightCategory() != null
                        ? data.getValue().getAssignedWeightCategory().name() : "-"));

        TableColumn<Boxer, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().isWeighedIn() ? "gewogen" : "noch nicht gewogen"));

        table.getColumns().addAll(java.util.List.of(
                nameCol, ageCategoryCol, reportedWeightCol, scaleWeightCol, weightCategoryCol, statusCol));
        table.setItems(registry.getParticipants());
        table.setEditable(true);
        table.setPlaceholder(new Label("Noch keine Teilnehmer angemeldet."));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        javafx.scene.layout.VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        return table;
    }
}
