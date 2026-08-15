package de.boxturnier.model;

/**
 * Offizielle Kampfentscheidungen gemäß DBV-Wettkampfbestimmungen, §30.
 * Es gibt neun Entscheidungsarten (Buchstaben a-i im Regelwerk).
 */
public enum BoutDecision {
    KO("a) Sieg durch Niederschlag (Knockout)"),
    ABD("b) Sieg durch Aufgabe des Kampfes (Abandonment)"),
    RSC("c) Sieg durch Abbruch wegen Kampf-/Verteidigungsunfähigkeit (Referee Stops Contest)"),
    RSC_I("d) Sieg durch Abbruch wegen Verletzung (RSC-Injury)"),
    PUNKTSIEG("e) Sieg durch Punktwertung (n.P.)"),
    UNENTSCHIEDEN("f) Unentschieden (nicht bei Einzelmeisterschaften/int. Veranstaltungen)"),
    DSQ("g) Sieg durch Disqualifikation des Gegners"),
    WO("h) Sieg durch Nichtantreten des Gegners (Walkover)"),
    NC("i) Abbruch ohne Entscheidung (No Contest)");

    private final String description;

    BoutDecision(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
