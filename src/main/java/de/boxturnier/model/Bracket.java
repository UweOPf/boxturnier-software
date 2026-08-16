package de.boxturnier.model;

import java.util.ArrayList;
import java.util.List;

/**
 * K.o.-Turnierbaum für eine Gewichtsklasse. Runde 1 enthält die echten Paarungen
 * plus Freilose (§21 DBV-Bestimmungen), alle Folgerunden enthalten Platzhalter
 * ("Sieger Spiel X"), bis die tatsächlichen Ergebnisse eingetragen werden.
 *
 * Bewusst ohne Fehlerbehandlung: setzt mindestens 2 Teilnehmer voraus.
 */
public class Bracket {

    public record Match(String topLabel, String bottomLabel, String winnerLabel, boolean isBye) {}

    public record Round(String name, List<Match> matches) {}

    private final WeightCategory weightCategory;
    private final List<Round> rounds = new ArrayList<>();

    private Bracket(WeightCategory weightCategory) {
        this.weightCategory = weightCategory;
    }

    public WeightCategory getWeightCategory() { return weightCategory; }
    public List<Round> getRounds() { return rounds; }

    public static Bracket generate(WeightCategory weightCategory, List<Boxer> participants) {
        Bracket bracket = new Bracket(weightCategory);

        int n = participants.size();
        int targetForSecondRound = Integer.highestOneBit(n - 1);
        int byeCount = Integer.bitCount(n) == 1 ? 0 : n - targetForSecondRound;
        int fighting = n - byeCount;

        List<Match> round1 = new ArrayList<>();
        int[] matchCounter = {1};

        for (int i = 0; i < fighting; i += 2) {
            String top = participants.get(i).getFullName();
            String bottom = participants.get(i + 1).getFullName();
            round1.add(new Match(top, bottom, "Sieger Spiel " + matchCounter[0], false));
            matchCounter[0]++;
        }
        for (int i = fighting; i < n; i++) {
            String name = participants.get(i).getFullName();
            round1.add(new Match(name, "Freilos", name, true));
        }

        bracket.rounds.add(new Round("Runde 1", round1));

        List<Match> currentRound = round1;
        while (currentRound.size() > 1) {
            String roundName = nameForRound(currentRound.size());
            List<Match> nextRound = new ArrayList<>();
            boolean isFinal = currentRound.size() == 2;

            for (int i = 0; i < currentRound.size(); i += 2) {
                String top = currentRound.get(i).winnerLabel();
                String bottom = currentRound.get(i + 1).winnerLabel();
                String winnerLabel = isFinal ? "Turniersieger" : "Sieger Spiel " + matchCounter[0];
                nextRound.add(new Match(top, bottom, winnerLabel, false));
                matchCounter[0]++;
            }

            bracket.rounds.add(new Round(roundName, nextRound));
            currentRound = nextRound;
        }

        return bracket;
    }

    private static String nameForRound(int entrantsIntoRound) {
        return switch (entrantsIntoRound) {
            case 2 -> "Finale";
            case 4 -> "Halbfinale";
            case 8 -> "Viertelfinale";
            case 16 -> "Achtelfinale";
            case 32 -> "Sechzehntelfinale";
            default -> "Zwischenrunde";
        };
    }
}
