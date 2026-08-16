package de.boxturnier.model;

import java.time.LocalDate;

public class Boxer {

    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final WeightCategory.Gender gender;
    private final String nationality; // Regel 2.3: "Sport Nationality"
    private final String club;

    private double officialWeightKg; // wird beim Wiegen gesetzt, abzgl. 300g Kleidung (§20 Abs. 2)
    private WeightCategory assignedWeightCategory;
    private AgeCategory assignedAgeCategory; // bei Anmeldung ermittelt und "eingefroren" (§11)
    private int wins = 0;
    private int losses = 0;

    /** Manuell gesetzt, falls die Elite-Kriterien nach §12 Abs. 4 zutreffen. */
    private boolean eliteStatus = false;

    public Boxer(String firstName, String lastName, LocalDate dateOfBirth,
                 WeightCategory.Gender gender, String nationality, String club) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationality = nationality;
        this.club = club;
    }

    /** Alter wird nach Kalenderjahr bestimmt (§11 Abs. 2), nicht nach exaktem Geburtstag. */
    public int getSportAge(int competitionYear) {
        return competitionYear - dateOfBirth.getYear();
    }

    public AgeCategory getAgeCategory(int competitionYear) {
        return AgeCategory.fromSportAge(getSportAge(competitionYear));
    }

    /** Leistungsklasse nach §12: Siegzahl, ggf. überschrieben durch manuellen Elite-Status. */
    public LeistungsKlasse getLeistungsKlasse() {
        if (eliteStatus) return LeistungsKlasse.ELITE;
        return LeistungsKlasse.fromWinsOnly(wins);
    }

    public void recordWin() { wins++; }
    public void recordLoss() { losses++; }

    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public boolean isEliteStatus() { return eliteStatus; }
    public void setEliteStatus(boolean eliteStatus) { this.eliteStatus = eliteStatus; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getter/Setter
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public WeightCategory.Gender getGender() { return gender; }
    public String getNationality() { return nationality; }
    public String getClub() { return club; }

    public double getOfficialWeightKg() { return officialWeightKg; }
    public void setOfficialWeightKg(double officialWeightKg) { this.officialWeightKg = officialWeightKg; }

    public WeightCategory getAssignedWeightCategory() { return assignedWeightCategory; }
    public void setAssignedWeightCategory(WeightCategory assignedWeightCategory) {
        this.assignedWeightCategory = assignedWeightCategory;
    }

    public AgeCategory getAssignedAgeCategory() { return assignedAgeCategory; }
    public void setAssignedAgeCategory(AgeCategory assignedAgeCategory) {
        this.assignedAgeCategory = assignedAgeCategory;
    }

    @Override
    public String toString() {
        return getFullName() + " (" + club + ", " + nationality + ")";
    }
}
