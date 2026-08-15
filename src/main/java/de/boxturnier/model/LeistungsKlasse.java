package de.boxturnier.model;

/**
 * Leistungsklassen gemäß DBV-Wettkampfbestimmungen §12, ermittelt anhand der
 * Anzahl der Siege eines Boxers (sowie Alter/internationaler Vertretung für Elite).
 */
public enum LeistungsKlasse {
    C("weniger als 7 Siege"),
    B("bis zu 14 Siege"),
    A("mehr als 14 Siege"),
    ELITE("über 18 Jahre, mehr als 25 Siege oder internationale Vertretung des DBV");

    private final String description;

    LeistungsKlasse(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }

    /**
     * Ermittelt die Leistungsklasse rein anhand der Siegzahl (§12 Abs. 1-3).
     * Die Elite-Einstufung (Abs. 4, zusätzlich Alter > 18 UND > 25 Siege ODER
     * internationale Vertretung) muss gesondert/manuell gesetzt werden, da sie
     * zusätzliche Kriterien jenseits der reinen Siegzahl erfordert.
     */
    public static LeistungsKlasse fromWinsOnly(int wins) {
        if (wins > 14) return A;
        if (wins >= 7) return B;
        return C;
    }
}
