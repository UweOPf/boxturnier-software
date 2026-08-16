package de.boxturnier.service;

import de.boxturnier.model.AgeCategory;
import de.boxturnier.model.Boxer;
import de.boxturnier.model.WeightCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

/**
 * Verwaltet die für ein Turnier angemeldeten Boxer und ordnet ihnen automatisch
 * die passende Alters- und Gewichtsklasse nach DBV-Regelwerk zu (§11, §19).
 */
public class ParticipantRegistry {

    private final ObservableList<Boxer> participants = FXCollections.observableArrayList();

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

    public Optional<WeightCategory> findMatchingWeightCategoryOptional(AgeCategory ageCategory,
                                                                        WeightCategory.Gender gender,
                                                                        double weightKg) {
        return Optional.ofNullable(findMatchingWeightCategory(ageCategory, gender, weightKg));
    }

    private WeightCategory findMatchingWeightCategory(AgeCategory ageCategory, WeightCategory.Gender gender,
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

    public ObservableList<Boxer> getParticipants() {
        return participants;
    }

    public int getCompetitionYear() { return competitionYear; }
    public void setCompetitionYear(int competitionYear) { this.competitionYear = competitionYear; }
}
