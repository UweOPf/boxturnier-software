package de.boxturnier.model;

import java.util.List;

/**
 * Gewichtsklasse gemäß DBV-Wettkampfbestimmungen (gültig ab 01.01.2025), §19.
 * minKg = Untergrenze (exklusiv, "über"), maxKg = Obergrenze (inklusive, "bis einschl.").
 * maxKg == null bedeutet "und darüber" (oberste Gewichtsklasse ohne Obergrenze).
 */
public record WeightCategory(AgeCategory ageCategory, Gender gender, double minKg, Double maxKg,
                              String name, int gloveOunces) {

    public enum Gender { MALE, FEMALE }

    /** Prüft, ob ein Körpergewicht (nach Abzug der 300g für Wettkampfkleidung, §20 Abs. 2) passt. */
    public boolean matches(double bodyWeightKg) {
        boolean aboveMin = bodyWeightKg > minKg;
        boolean belowOrEqualMax = (maxKg == null) || (bodyWeightKg <= maxKg);
        return aboveMin && belowOrEqualMax;
    }

    // ---- §19 Abs. 1: Männer und männliche Jugend (U19) ----
    public static final List<WeightCategory> MEN_AND_MALE_U19 = List.of(
            wc(AgeCategory.JUGEND, Gender.MALE, 47, 50.0, "Fliegengewicht (M50kg)", 10),
            wc(AgeCategory.JUGEND, Gender.MALE, 50, 55.0, "Bantamgewicht (M55kg)", 10),
            wc(AgeCategory.JUGEND, Gender.MALE, 55, 60.0, "Leichtgewicht (M60kg)", 10),
            wc(AgeCategory.JUGEND, Gender.MALE, 60, 65.0, "Weltergewicht (M65kg)", 10),
            wc(AgeCategory.JUGEND, Gender.MALE, 65, 70.0, "Halbmittelgewicht (M70kg)", 12),
            wc(AgeCategory.JUGEND, Gender.MALE, 70, 75.0, "Mittelgewicht (M75kg)", 12),
            wc(AgeCategory.JUGEND, Gender.MALE, 75, 80.0, "Halbschwergewicht (M80kg)", 12),
            wc(AgeCategory.JUGEND, Gender.MALE, 80, 85.0, "Cruisergewicht (M85kg)", 12),
            wc(AgeCategory.JUGEND, Gender.MALE, 85, 90.0, "Schwergewicht (M90kg)", 12),
            wc(AgeCategory.JUGEND, Gender.MALE, 90, null, "Superschwergewicht (M90+kg)", 12)
    );

    // ---- §19 Abs. 2: Frauen und weibliche Jugend (U19) ----
    public static final List<WeightCategory> WOMEN_AND_FEMALE_U19 = List.of(
            wc(AgeCategory.JUGEND, Gender.FEMALE, 45, 48.0, "Halbfliegengewicht (W48kg)", 10),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 48, 51.0, "Fliegengewicht (W51kg)", 10),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 51, 54.0, "Bantamgewicht (W54kg)", 10),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 54, 57.0, "Federgewicht (W57kg)", 10),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 57, 60.0, "Leichtgewicht (W60kg)", 10),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 60, 65.0, "Weltergewicht (W65kg)", 10),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 65, 70.0, "Halbmittelgewicht (W70kg)", 12),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 70, 75.0, "Mittelgewicht (W75kg)", 12),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 75, 80.0, "Halbschwergewicht (W80kg)", 12),
            wc(AgeCategory.JUGEND, Gender.FEMALE, 80, null, "Schwergewicht (W80+kg)", 12)
    );

    // ---- §19 Abs. 3: Junioren (U17), weiblich und männlich, identische Grenzen ----
    public static final List<WeightCategory> JUNIOREN_U17 = List.of(
            wc(AgeCategory.JUNIOREN, null, 40, 42.0, "Papiergewicht (42kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 42, 44.0, "Papiergewicht (44kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 44, 46.0, "Papiergewicht (46kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 46, 48.0, "Halbfliegengewicht (48kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 48, 50.0, "Fliegengewicht (50kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 50, 52.0, "Halbbantamgewicht (52kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 52, 54.0, "Bantamgewicht (54kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 54, 57.0, "Federgewicht (57kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 57, 60.0, "Leichtgewicht (60kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 60, 63.0, "Halbweltergewicht (63kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 63, 66.0, "Weltergewicht (66kg)", 10),
            wc(AgeCategory.JUNIOREN, null, 66, 70.0, "Halbmittelgewicht (70kg)", 12),
            wc(AgeCategory.JUNIOREN, null, 70, 75.0, "Mittelgewicht (75kg)", 12),
            wc(AgeCategory.JUNIOREN, null, 75, 80.0, "Halbschwergewicht (80kg)", 12),
            wc(AgeCategory.JUNIOREN, null, 80, null, "Schwergewicht (80+kg)", 12)
    );

    // ---- §19 Abs. 4: Kadetten (U15), weiblich und männlich, identische Grenzen ----
    public static final List<WeightCategory> KADETTEN_U15 = List.of(
            wc(AgeCategory.KADETTEN, null, 38, 40.0, "Papiergewicht (40kg)", 10),
            wc(AgeCategory.KADETTEN, null, 40, 42.0, "Papiergewicht (42kg)", 10),
            wc(AgeCategory.KADETTEN, null, 42, 44.0, "Papiergewicht (44kg)", 10),
            wc(AgeCategory.KADETTEN, null, 44, 46.0, "Papiergewicht (46kg)", 10),
            wc(AgeCategory.KADETTEN, null, 46, 48.0, "Halbfliegengewicht (48kg)", 10),
            wc(AgeCategory.KADETTEN, null, 48, 50.0, "Fliegengewicht (50kg)", 10),
            wc(AgeCategory.KADETTEN, null, 50, 52.0, "Halbbantamgewicht (52kg)", 10),
            wc(AgeCategory.KADETTEN, null, 52, 54.0, "Bantamgewicht (54kg)", 10),
            wc(AgeCategory.KADETTEN, null, 54, 57.0, "Federgewicht (57kg)", 10),
            wc(AgeCategory.KADETTEN, null, 57, 60.0, "Leichtgewicht (60kg)", 10),
            wc(AgeCategory.KADETTEN, null, 60, 63.0, "Halbweltergewicht (63kg)", 10),
            wc(AgeCategory.KADETTEN, null, 63, 66.0, "Weltergewicht (66kg)", 10),
            wc(AgeCategory.KADETTEN, null, 66, 70.0, "Halbmittelgewicht (70kg)", 12),
            wc(AgeCategory.KADETTEN, null, 70, 75.0, "Mittelgewicht (75kg)", 12),
            wc(AgeCategory.KADETTEN, null, 75, 80.0, "Halbschwergewicht (80kg)", 12),
            wc(AgeCategory.KADETTEN, null, 80, null, "Schwergewicht (80+kg)", 12)
    );

    // ---- §19 Abs. 5: Schüler (U13), weiblich und männlich, identische Grenzen ----
    public static final List<WeightCategory> SCHUELER_U13 = List.of(
            wc(AgeCategory.SCHUELER, null, 38, 40.0, "Papiergewicht (40kg)", 10),
            wc(AgeCategory.SCHUELER, null, 40, 42.0, "Papiergewicht (42kg)", 10),
            wc(AgeCategory.SCHUELER, null, 42, 44.0, "Papiergewicht (44kg)", 10),
            wc(AgeCategory.SCHUELER, null, 44, 46.0, "Papiergewicht (46kg)", 10),
            wc(AgeCategory.SCHUELER, null, 46, 48.0, "Halbfliegengewicht (48kg)", 10),
            wc(AgeCategory.SCHUELER, null, 48, 50.0, "Fliegengewicht (50kg)", 10),
            wc(AgeCategory.SCHUELER, null, 50, 52.0, "Halbbantamgewicht (52kg)", 10),
            wc(AgeCategory.SCHUELER, null, 52, 54.0, "Bantamgewicht (54kg)", 10),
            wc(AgeCategory.SCHUELER, null, 54, 57.0, "Federgewicht (57kg)", 10),
            wc(AgeCategory.SCHUELER, null, 57, 60.0, "Leichtgewicht (60kg)", 10),
            wc(AgeCategory.SCHUELER, null, 60, 63.0, "Halbweltergewicht (63kg)", 10),
            wc(AgeCategory.SCHUELER, null, 63, 66.0, "Weltergewicht (66kg)", 10),
            wc(AgeCategory.SCHUELER, null, 66, 70.0, "Halbmittelgewicht (70kg)", 12),
            wc(AgeCategory.SCHUELER, null, 70, 75.0, "Mittelgewicht (75kg)", 12),
            wc(AgeCategory.SCHUELER, null, 75, 80.0, "Halbschwergewicht (80kg)", 12),
            wc(AgeCategory.SCHUELER, null, 80, null, "Schwergewicht (80+kg)", 12)
    );

    private static WeightCategory wc(AgeCategory age, Gender gender, double min, Double max, String name, int oz) {
        return new WeightCategory(age, gender, min, max, name, oz);
    }

    /** Liefert die passende Tabelle für eine Alters-/Geschlechtskombination (§19). */
    public static List<WeightCategory> tableFor(AgeCategory age, Gender gender) {
        return switch (age) {
            case SCHUELER -> SCHUELER_U13;
            case KADETTEN -> KADETTEN_U15;
            case JUNIOREN -> JUNIOREN_U17;
            case JUGEND, ERWACHSENE -> gender == Gender.FEMALE ? WOMEN_AND_FEMALE_U19 : MEN_AND_MALE_U19;
        };
    }
}
