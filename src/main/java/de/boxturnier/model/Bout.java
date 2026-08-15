package de.boxturnier.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein einzelner Kampf (Bout) zwischen zwei Boxern.
 * Wertung nach dem 10-Point-Must-System mit 3 oder 5 Punktrichtern (DBV §27 Abs. 3, §31).
 */
public class Bout {

    private final Boxer redCorner;
    private final Boxer blueCorner;
    private final AgeCategory ageCategory;
    private final int numberOfJudges; // 3 oder 5, §27 Abs. 3 (bei Meisterschaften zwingend 5)

    // Je Runde und je Richter eine Punktzahl, z.B. scores[round][judge] = {10, 9}
    private final List<int[][]> roundScoresPerJudge = new ArrayList<>();

    private BoutDecision decision;
    private Boxer winner;
    private int warningsRed = 0;   // Verwarnungen, je -1 Wertungspunkt vom Endergebnis (§31 Abs. 1b)
    private int warningsBlue = 0;
    private int knockdownsRedTotal = 0;
    private int knockdownsBlueTotal = 0;

    public Bout(Boxer redCorner, Boxer blueCorner, AgeCategory ageCategory, int numberOfJudges) {
        if (numberOfJudges != 3 && numberOfJudges != 5) {
            throw new IllegalArgumentException("Es müssen 3 oder 5 Punktrichter amtieren (§27 Abs. 3).");
        }
        this.redCorner = redCorner;
        this.blueCorner = blueCorner;
        this.ageCategory = ageCategory;
        this.numberOfJudges = numberOfJudges;
    }

    /**
     * Trägt die Wertung eines Richters für eine Runde ein (§31 Abs. 2).
     * Erlaubte Kombinationen: 10:9 (knapp), 10:8 (deutlich), 10:7 (dominant).
     * Ein Unentschieden in einer Runde ist nicht erlaubt (§31 Abs. 2a).
     */
    public void recordJudgeScore(int roundIndex, int judgeIndex, int scoreRed, int scoreBlue) {
        boolean redWinsRound = scoreRed == 10 && List.of(9, 8, 7).contains(scoreBlue);
        boolean blueWinsRound = scoreBlue == 10 && List.of(9, 8, 7).contains(scoreRed);
        if (!redWinsRound && !blueWinsRound) {
            throw new IllegalArgumentException(
                    "Ungültige Rundenwertung. Erlaubt: 10:9, 10:8 oder 10:7 (§31 Abs. 2).");
        }
        while (roundScoresPerJudge.size() <= roundIndex) {
            roundScoresPerJudge.add(new int[numberOfJudges][2]);
        }
        roundScoresPerJudge.get(roundIndex)[judgeIndex][0] = scoreRed;
        roundScoresPerJudge.get(roundIndex)[judgeIndex][1] = scoreBlue;
    }

    /** Verwarnung erteilen; wird erst vom Supervisor am Kampfende vom Endergebnis abgezogen (§31 Abs. 1b). */
    public void addWarning(boolean toRedCorner) {
        if (toRedCorner) warningsRed++; else warningsBlue++;
    }

    /**
     * Registriert einen Niederschlag ("zu Boden gehen"). Nach §26 Abs. 19 muss der
     * Ringrichter den Kampf beenden bei: 3x Niederschlag in einer Runde, oder insgesamt
     * 4x im Kampf (Männer/Frauen/Jugend) bzw. 2x/Runde oder 3x/Kampf (Schüler/Kadetten/Junioren).
     */
    public void recordKnockdown(boolean redCornerDown) {
        if (redCornerDown) knockdownsRedTotal++; else knockdownsBlueTotal++;
    }

    /** Prüft nach §26 Abs. 19, ob der Kampf wegen Gesamt-Niederschlägen abgebrochen werden muss. */
    public boolean mustStopForTotalKnockdowns(boolean redCornerDown) {
        int total = redCornerDown ? knockdownsRedTotal : knockdownsBlueTotal;
        boolean isYouth = ageCategory == AgeCategory.SCHUELER
                || ageCategory == AgeCategory.KADETTEN
                || ageCategory == AgeCategory.JUNIOREN;
        return isYouth ? total >= 3 : total >= 4;
    }

    /** Berechnet je Richter, wer gewonnen hat (Mehrheitsentscheidung, §30 zu e)). */
    public Boxer computeMajorityWinner() {
        int redVotes = 0, blueVotes = 0;
        for (int judge = 0; judge < numberOfJudges; judge++) {
            int redTotal = 0, blueTotal = 0;
            for (int[][] round : roundScoresPerJudge) {
                redTotal += round[judge][0];
                blueTotal += round[judge][1];
            }
            // Verwarnungen erst am Gesamtergebnis abziehen (§31 Abs. 1b)
            redTotal -= warningsRed;
            blueTotal -= warningsBlue;
            if (redTotal > blueTotal) redVotes++;
            else if (blueTotal > redTotal) blueVotes++;
        }
        if (redVotes > blueVotes) return redCorner;
        if (blueVotes > redVotes) return blueCorner;
        return null; // Unentschieden bzw. bei Meisterschaften: Stichentscheid nach §31 Abs. 4b
    }

    public void setDecision(BoutDecision decision, Boxer winner) {
        this.decision = decision;
        this.winner = winner;
    }

    public Boxer getRedCorner() { return redCorner; }
    public Boxer getBlueCorner() { return blueCorner; }
    public AgeCategory getAgeCategory() { return ageCategory; }
    public int getNumberOfJudges() { return numberOfJudges; }
    public BoutDecision getDecision() { return decision; }
    public Boxer getWinner() { return winner; }
    public int getWarningsRed() { return warningsRed; }
    public int getWarningsBlue() { return warningsBlue; }
}
