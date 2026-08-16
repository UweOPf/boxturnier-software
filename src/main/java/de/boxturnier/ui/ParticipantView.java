package de.boxturnier.ui;

import de.boxturnier.model.Boxer;
import de.boxturnier.model.WeightCategory;
import de.boxturnier.service.ParticipantRegistry;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;

/**
 * Teilnehmerverwaltung: Formular zur Anmeldung neuer Boxer links,
 * Übersichtstabelle aller angemeldeten Boxer rechts.
 */
public class ParticipantView extends BorderPane {

    private final ParticipantRegistry registry;

    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final DatePicker dobPicker = new DatePicker();
    private final ComboBox<WeightCategory.Gender> genderCombo = new ComboBox<>();
    private final TextField clubField = new TextField();
    private final TextField nationalityField = new TextField();
    private final TextField weightField = new TextField();
    private final Label statusLabel = new Label();

    private final TableView<Boxer> table = new TableView<>();

    public ParticipantView(ParticipantRegistry registry) {
        this.registry = registry;
        setPadding(new Insets(15));
        setLeft(buildForm());
        setCenter(buildTable());
    }

    private VBox buildForm() {
        genderCombo.getItems().addAll(WeightCategory.Gender.MALE, WeightCategory.Gender.FEMALE);
        genderCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(WeightCategory.Gender gender) {
                if (gender == null) return "";
                return gender == WeightCategory.Gender.MALE ? "Männlich" : "Weiblich";
            }

            @Override
            public WeightCategory.Gender fromString(String string) {
                return "Männlich".equals(string) ? WeightCategory.Gender.MALE : WeightCategory.Gender.FEMALE;
            }
        });
        genderCombo.setPromptText("Geschlecht wählen");
        dobPicker.setPromptText("TT.MM.JJJJ");
        weightField.setPromptText("z. B. 64.2");

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(8);
        int row = 0;
        grid.addRow(row++, new Label("Vorname:"), firstNameField);
        grid.addRow(row++, new Label("Nachname:"), lastNameField);
        grid.addRow(row++, new Label("Geburtsdatum:"), dobPicker);
        grid.addRow(row++, new Label("Geschlecht:"), genderCombo);
        grid.addRow(row++, new Label("Verein:"), clubField);
        grid.addRow(row++, new Label("Nationalität:"), nationalityField);
        grid.addRow(row++, new Label("Gewicht (kg):"), weightField);

        Button addButton = new Button("Boxer anmelden");
        addButton.setDefaultButton(true);
        addButton.setOnAction(e -> onAddBoxer());

        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(240);

        VBox box = new VBox(12, new Label("Neuen Teilnehmer anmelden"), grid, addButton, statusLabel);
        box.setPadding(new Insets(0, 20, 0, 0));
        box.setPrefWidth(280);
        return box;
    }

    private VBox buildTable() {
        TableColumn<Boxer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFullName()));

        TableColumn<Boxer, String> dobCol = new TableColumn<>("Geburtsdatum");
        dobCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                String.valueOf(data.getValue().getDateOfBirth())));

        TableColumn<Boxer, String> genderCol = new TableColumn<>("Geschlecht");
        genderCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getGender() == WeightCategory.Gender.MALE ? "M" : "W"));

        TableColumn<Boxer, String> clubCol = new TableColumn<>("Verein");
        clubCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getClub()));

        TableColumn<Boxer, String> weightCol = new TableColumn<>("Gewicht");
        weightCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                String.format("%.1f kg", data.getValue().getOfficialWeightKg())));

        TableColumn<Boxer, String> ageCategoryCol = new TableColumn<>("Altersklasse");
        ageCategoryCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getAssignedAgeCategory() != null
                        ? data.getValue().getAssignedAgeCategory().getLabel() : "-"));

        TableColumn<Boxer, String> weightCategoryCol = new TableColumn<>("Gewichtsklasse");
        weightCategoryCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getAssignedWeightCategory() != null
                        ? data.getValue().getAssignedWeightCategory().name() : "keine Klasse gefunden"));

        table.getColumns().addAll(java.util.List.of(
                nameCol, dobCol, genderCol, clubCol, weightCol, ageCategoryCol, weightCategoryCol));
        table.setItems(registry.getParticipants());
        table.setPlaceholder(new Label("Noch keine Teilnehmer angemeldet."));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Button removeButton = new Button("Ausgewählten entfernen");
        removeButton.setOnAction(e -> {
            Boxer selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                registry.removeBoxer(selected);
            }
        });

        HBox toolbar = new HBox(10, removeButton);
        toolbar.setAlignment(Pos.CENTER_RIGHT);
        toolbar.setPadding(new Insets(0, 0, 8, 0));

        VBox box = new VBox(8, new Label("Angemeldete Teilnehmer (" + registry.getCompetitionYear() + ")"),
                toolbar, table);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
        return box;
    }

    private void onAddBoxer() {
        statusLabel.setText("");
        statusLabel.setTextFill(javafx.scene.paint.Color.FIREBRICK);

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        WeightCategory.Gender gender = genderCombo.getValue();
        String club = clubField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String weightText = weightField.getText().trim().replace(",", ".");

        if (firstName.isEmpty() || lastName.isEmpty()) {
            statusLabel.setText("Bitte Vor- und Nachname eingeben.");
            return;
        }
        if (dob == null) {
            statusLabel.setText("Bitte Geburtsdatum wählen.");
            return;
        }
        if (dob.isAfter(LocalDate.now())) {
            statusLabel.setText("Geburtsdatum liegt in der Zukunft.");
            return;
        }
        if (gender == null) {
            statusLabel.setText("Bitte Geschlecht wählen.");
            return;
        }

        double weightKg;
        try {
            weightKg = Double.parseDouble(weightText);
            if (weightKg <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            statusLabel.setText("Bitte ein gültiges Gewicht in kg eingeben (z. B. 64.2).");
            return;
        }

        Boxer boxer = new Boxer(firstName, lastName, dob, gender,
                nationality.isEmpty() ? "-" : nationality,
                club.isEmpty() ? "-" : club);
        boxer.setOfficialWeightKg(weightKg);

        try {
            registry.registerBoxer(boxer);
        } catch (IllegalArgumentException ex) {
            statusLabel.setText(ex.getMessage());
            return;
        }

        if (boxer.getAssignedWeightCategory() == null) {
            statusLabel.setTextFill(javafx.scene.paint.Color.DARKORANGE);
            statusLabel.setText(firstName + " " + lastName + " wurde angemeldet, aber " + weightKg
                    + " kg passt in keine offizielle Gewichtsklasse der Altersklasse "
                    + boxer.getAssignedAgeCategory().getLabel() + ". Bitte Gewicht prüfen.");
        } else {
            statusLabel.setTextFill(javafx.scene.paint.Color.SEAGREEN);
            statusLabel.setText(firstName + " " + lastName + " angemeldet: "
                    + boxer.getAssignedAgeCategory().getLabel() + ", "
                    + boxer.getAssignedWeightCategory().name() + ".");
        }

        clearForm();
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        dobPicker.setValue(null);
        genderCombo.setValue(null);
        clubField.clear();
        nationalityField.clear();
        weightField.clear();
        firstNameField.requestFocus();
    }
}
