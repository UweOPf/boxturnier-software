package de.boxturnier.service;

import de.boxturnier.model.AgeCategory;
import de.boxturnier.model.Bout;
import de.boxturnier.model.Boxer;
import de.boxturnier.model.WeightCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

/**
 * Verwaltet die für ein Turnier angemeldeten Boxer und ordnet ihnen automatisch
 * die passende Alters- und Gewichtsklasse nach DBV-Regelwerk zu (§11, §19).
 *
 * Verwaltet außerdem den Kampfplan für Veranstaltungen mit Einzelkämpfen
 * (Turnier/Nachwuchsveranstaltung/Staffelkampf: jeder Boxer boxt genau einmal,
 * die Paarungen werden manuell vom Veranstalter festgelegt).
 */
public class ParticipantRegistry {

    private final ObservableList<Boxer> participants = FXCollections.observableArrayList();
    private final ObservableList<Bout> fights = FXCollections.observableArrayList();

    /** Das Jahr, nach dem sich die Altersklasse richtet (§11 Abs. 2: Kalenderjahr-Stichtag). */
    private int competitionYear = java.time.Year.now().getValue();

    /**
     * Registriert einen Boxer und ordnet automatisch Alters- und Gewichtsklasse zu.
     * Wirft eine IllegalArgumentException, wenn keine passende Altersklasse existiert.
     */
    public void registerBoxer(Boxer boxer) {
        int sportAge = boxer.getSportAge(competitionYear);
        AgeCategory ageCategory = AgeCategory.fromSportAge(sportAge);
        boxer.setAssignedAgeCategory(ageCategory);

        WeightCategory match = findMatchingWeightCategory(ageCategory, boxer.getGender(), boxer.getOfficialWeightKg());
        boxer.setAssignedWeightCategory(match); // kann null sein, falls kein Gewicht passt

        participants.add(boxer);
    }

    /**
     * Trägt das beim Wiegen ermittelte Gewicht ein und bestimmt die Gewichtsklasse
     * verbindlich neu auf Basis dieses Waage-Gewichts (§20: das Meldegewicht bei der
     * Anmeldung ist nur eine Schätzung, verbindlich ist das Wiegen).
     */
    public void recordWeighIn(Boxer boxer, double scaleWeightKg) {
        boxer.setScaleWeightKg(scaleWeightKg);
        boxer.setWeighedIn(true);
        WeightCategory match = findMatchingWeightCategory(
                boxer.getAssignedAgeCategory(), boxer.getGender(), scaleWeightKg);
        boxer.setAssignedWeightCategory(match);
    }

    public Optional<WeightCategory> findMatchingWeightCategoryOptional(AgeCategory ageCategory,
                                                                        WeightCategory.Gender gender,
                                                                        double weightKg) {
        return Optional.ofNullable(findMatchingWeightCategory(ageCategory, gender, weightKg));
    }

    public WeightCategory findMatchingWeightCategory(AgeCategory ageCategory, WeightCategory.Gender gender,
                                                        double weightKg) {
        List<WeightCategory> table = WeightCategory.tableFor(ageCategory, gender);
        return table.stream()
                .filter(category -> category.matches(weightKg))
                .findFirst()
                .orElse(null);
    }

    public void removeBoxer(Boxer boxer) {
        participants.remove(boxer);
    }

    /**
     * Legt einen Einzelkampf zwischen zwei Boxern an (Turnier/Staffelkampf-Modus:
     * jeder Boxer boxt genau einmal, Paarung wird manuell festgelegt).
     * Bewusst ohne Fehlerbehandlung/Gewichtsprüfung: die Paarung liegt in der
     * Verantwortung des Veranstalters/Ringrichters.
     */
    public void addFight(Boxer red, Boxer blue, int numberOfJudges) {
        Bout bout = new Bout(red, blue, red.getAssignedAgeCategory(), numberOfJudges);
        fights.add(bout);
    }

    public void removeFight(Bout bout) {
        fights.remove(bout);
    }

    public ObservableList<Bout> getFights() {
        return fights;
    }

    public ObservableList<Boxer> getParticipants() {
        return participants;
    }

    public int getCompetitionYear() { return competitionYear; }
    public void setCompetitionYear(int competitionYear) { this.competitionYear = competitionYear; }
}
