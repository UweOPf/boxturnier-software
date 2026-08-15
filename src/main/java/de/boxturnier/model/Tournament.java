package de.boxturnier.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Turnier in einer Gewichtsklasse. Single-Elimination (K.o.-System) mit Freilosen
 * nach DBV §21 (Auslosung).
 */
public class Tournament {

    private final WeightCategory weightCategory;
    private final List<Boxer> participants = new ArrayList<>();
    private final List<Bout> bouts = new ArrayList<>();
    private final int numberOfJudges;

    public Tournament(WeightCategory weightCategory, int numberOfJudges) {
        this.weightCategory = weightCategory;
        this.numberOfJudges = numberOfJudges;
    }

    public void addParticipant(Boxer boxer) {
        if (!weightCategory.matches(boxer.getOfficialWeightKg())) {
            throw new IllegalArgumentException(
                    boxer.getFullName() + " passt nicht in die Gewichtsklasse " + weightCategory.name());
        }
        participants.add(boxer);
    }

    /**
     * Ermittelt die Anzahl der benötigten Freilose für die erste Serie (§21 Abs. 2).
     * Reduziert die Starterzahl auf die nächstniedrigere Zweierpotenz (4, 8, 16, ...)
     * für die zweite Serie.
     */
    public int calculateByeCount() {
        int n = participants.size();
        if (n <= 1) return 0;
        int targetForSecondRound = Integer.highestOneBit(n - 1); // nächstniedrigere 2er-Potenz
        // Bei exakter 2er-Potenz sind keine Freilose nötig
        if (Integer.bitCount(n) == 1) return 0;
        return n - targetForSecondRound;
    }

    /**
     * Erstellt die erste Runde inkl. Freilosen für die höchsten Losnummern (§21 Abs. 3/4).
     * Vereinfachte Umsetzung: Die letzten `byeCount` Teilnehmer (nach Eingabe-/Losreihenfolge)
     * erhalten ein Freilos und boxen erst in der zweiten Serie.
     */
    public List<Bout> generateFirstRound() {
        int byeCount = calculateByeCount();
        int fighting = participants.size() - byeCount;

        List<Bout> firstRound = new ArrayList<>();
        for (int i = 0; i + 1 < fighting; i += 2) {
            firstRound.add(new Bout(participants.get(i), participants.get(i + 1),
                    weightCategory.ageCategory(), numberOfJudges));
        }
        bouts.addAll(firstRound);
        return firstRound;
    }

    public WeightCategory getWeightCategory() { return weightCategory; }
    public List<Boxer> getParticipants() { return participants; }
    public List<Bout> getBouts() { return bouts; }
}
