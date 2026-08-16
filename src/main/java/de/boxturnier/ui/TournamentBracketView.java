package de.boxturnier.ui;

import de.boxturnier.model.Bracket;
import de.boxturnier.model.Boxer;
import de.boxturnier.model.WeightCategory;
import de.boxturnier.service.ParticipantRegistry;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Visueller K.o.-Turnierbaum je Gewichtsklasse.
 * Bewusst ohne Fehlerbehandlung: setzt voraus, dass eine Kategorie mit mind.
 * 2 Teilnehmern gewählt wird.
 */
public class TournamentBracketView extends BorderPane {

    private static final double BOX_WIDTH = 170;
    private static final double BOX_HEIGHT = 44;
    private static final double BASE_VERTICAL_GAP = 70;
    private static final double ROUND_GAP_X = 230;
    private static final double TOP_MARGIN = 40; // Platz für die Rundenüberschriften oberhalb der ersten Box

    private final ParticipantRegistry registry;
    private final ComboBox<WeightCategory> categoryCombo = new ComboBox<>();
    private final Pane canvas = new Pane();

    public TournamentBracketView(ParticipantRegistry registry) {
        this.registry = registry;
        setPadding(new Insets(15));
        setTop(buildControls());

        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        setCenter(scrollPane);
    }

    private HBox buildControls() {
        categoryCombo.setPromptText("Gewichtsklasse wählen");
        categoryCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(WeightCategory category) {
                return category == null ? "" : category.ageCategory().getLabel() + " – " + category.name();
            }

            @Override
            public WeightCategory fromString(String string) {
                return null; // nicht benötigt, ComboBox ist nicht editierbar
            }
        });

        Button refreshButton = new Button("Kategorien aktualisieren");
        refreshButton.setOnAction(e -> refreshCategories());

        Button generateButton = new Button("Turnierbaum erzeugen");
        generateButton.setOnAction(e -> generateBracket());

        refreshCategories();

        HBox box = new HBox(10, new Label("Kategorie:"), categoryCombo, refreshButton, generateButton);
        box.setPadding(new Insets(0, 0, 15, 0));
        return box;
    }

    /** Sammelt alle Gewichtsklassen, denen aktuell mindestens ein Teilnehmer zugeordnet ist. */
    private void refreshCategories() {
        Set<WeightCategory> categories = registry.getParticipants().stream()
                .map(Boxer::getAssignedWeightCategory)
                .filter(c -> c != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        WeightCategory previousSelection = categoryCombo.getValue();
        categoryCombo.getItems().setAll(categories);
        if (categories.contains(previousSelection)) {
            categoryCombo.setValue(previousSelection);
        }
    }

    private void generateBracket() {
        WeightCategory category = categoryCombo.getValue();
        List<Boxer> participants = registry.getParticipants().stream()
                .filter(b -> category.equals(b.getAssignedWeightCategory()))
                .collect(Collectors.toList());

        Bracket bracket = Bracket.generate(category, participants);
        renderBracket(bracket);
    }

    private void renderBracket(Bracket bracket) {
        canvas.getChildren().clear();
        List<Bracket.Round> rounds = bracket.getRounds();

        for (int r = 0; r < rounds.size(); r++) {
            List<Bracket.Match> matches = rounds.get(r).matches();

            // Rundenüberschrift
            Label roundTitle = new Label(rounds.get(r).name());
            roundTitle.setFont(Font.font(13));
            roundTitle.setStyle("-fx-font-weight: bold;");
            roundTitle.setLayoutX(r * ROUND_GAP_X);
            roundTitle.setLayoutY(TOP_MARGIN - 25);
            canvas.getChildren().add(roundTitle);

            for (int i = 0; i < matches.size(); i++) {
                double yCenter = TOP_MARGIN + (i + 0.5) * BASE_VERTICAL_GAP * Math.pow(2, r);
                double x = r * ROUND_GAP_X;
                double y = yCenter - BOX_HEIGHT / 2;

                VBox box = buildMatchBox(matches.get(i));
                box.setLayoutX(x);
                box.setLayoutY(y);
                canvas.getChildren().add(box);
            }

            // Verbindungslinien zur nächsten Runde
            if (r < rounds.size() - 1) {
                for (int i = 0; i + 1 < matches.size(); i += 2) {
                    double y1 = TOP_MARGIN + (i + 0.5) * BASE_VERTICAL_GAP * Math.pow(2, r);
                    double y2 = TOP_MARGIN + (i + 1 + 0.5) * BASE_VERTICAL_GAP * Math.pow(2, r);
                    double xConnector = r * ROUND_GAP_X + BOX_WIDTH + (ROUND_GAP_X - BOX_WIDTH) / 2;

                    Line stub1 = new Line(r * ROUND_GAP_X + BOX_WIDTH, y1, xConnector, y1);
                    Line stub2 = new Line(r * ROUND_GAP_X + BOX_WIDTH, y2, xConnector, y2);
                    Line vertical = new Line(xConnector, y1, xConnector, y2);
                    double yMid = (y1 + y2) / 2;
                    Line toNext = new Line(xConnector, yMid, (r + 1) * ROUND_GAP_X, yMid);

                    for (Line line : List.of(stub1, stub2, vertical, toNext)) {
                        line.setStroke(Color.GRAY);
                    }
                    canvas.getChildren().addAll(stub1, stub2, vertical, toNext);
                }
            }
        }

        canvas.setPadding(new Insets(35, 20, 20, 10));
    }

    private VBox buildMatchBox(Bracket.Match match) {
        Label top = new Label(match.topLabel());
        Label bottom = new Label(match.bottomLabel());
        top.setFont(Font.font(12));
        bottom.setFont(Font.font(12));

        VBox box = new VBox(2, top, bottom);
        box.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        box.setPadding(new Insets(4, 8, 4, 8));
        String background = match.isBye() ? "#f2f2f2" : "white";
        box.setStyle("-fx-border-color: #999999; -fx-border-radius: 4; -fx-background-radius: 4; "
                + "-fx-background-color: " + background + ";");
        return box;
    }
}
