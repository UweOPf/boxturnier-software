package de.boxturnier.model;

/**
 * Altersklassen gemäß Wettkampfbestimmungen des Deutschen BoxsportVerbandes (DBV),
 * gültig ab 01.01.2025, §11 und §22.
 *
 * Stichtag ist das Kalenderjahr (31. Dezember des Vorjahres), NICHT der exakte
 * Geburtstag (§11 Abs. 2).
 */
public enum AgeCategory {

    // name,        minAge, maxAge, Rundendauer in Sekunden (§22)
    SCHUELER("Schüler (U13)", 10, 12, 60),
    KADETTEN("Kadetten (U15)", 13, 14, 90),
    JUNIOREN("Junioren (U17)", 15, 16, 120),
    JUGEND("Jugend (U19)", 17, 18, 180),
    ERWACHSENE("Männer/Frauen", 19, 200, 180);

    public static final int ROUNDS_PER_BOUT = 3;
    public static final int REST_BETWEEN_ROUNDS_SECONDS = 60; // §22: für alle Klassen 1 Minute

    private final String label;
    private final int minAge;
    private final int maxAge;
    private final int roundDurationSeconds;

    AgeCategory(String label, int minAge, int maxAge, int roundDurationSeconds) {
        this.label = label;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.roundDurationSeconds = roundDurationSeconds;
    }

    public String getLabel() { return label; }
    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }
    public int getRoundDurationSeconds() { return roundDurationSeconds; }

    /**
     * Ermittelt die Altersklasse nach Sportjahr-Alter (Kalenderjahr-Stichtag, §11 Abs. 2).
     * sportAge = Wettkampfjahr - Geburtsjahr.
     */
    public static AgeCategory fromSportAge(int sportAge) {
        if (sportAge < SCHUELER.minAge) {
            throw new IllegalArgumentException(
                    "Boxer ist mit " + sportAge + " Jahren noch nicht startberechtigt (§8 Abs. 1: ab 10. Lebensjahr).");
        }
        for (AgeCategory category : values()) {
            if (sportAge >= category.minAge && sportAge <= category.maxAge) {
                return category;
            }
        }
        return ERWACHSENE;
    }

    /**
     * Prüft, ob zwei Altersklassen laut §11 Abs. 9/10 gegeneinander antreten dürfen
     * (Altersunterschied max. 24 Monate bei den Jugendklassen U13-U19).
     * Hinweis: Vereinfachte Prüfung auf Basis der Klassen; die genaue Monatsdifferenz
     * muss zusätzlich anhand der Geburtsdaten geprüft werden.
     */
    public boolean isAdjacentTo(AgeCategory other) {
        int diff = Math.abs(this.ordinal() - other.ordinal());
        return diff <= 1;
    }
}
