# Boxturnier-Software

Neuentwicklung der bisherigen (veralteten) Boxturnier-Verwaltungssoftware
als moderne, quelloffene Java/JavaFX-Anwendung.

## Regelwerk-Grundlage

Die Turnierlogik basiert auf den offiziellen **Wettkampfbestimmungen des
Deutschen BoxsportVerbandes (DBV), gültig ab 01.01.2025**:
https://www.boxverband.de/wp-content/uploads/2024/12/Wettkampfbestimmungen-des-DBV-2025-01.pdf

Bereits im Datenmodell abgebildet (mit Paragraphen-Verweisen im Code):
- **Altersklassen** (§11): Schüler (U13), Kadetten (U15), Junioren (U17), Jugend (U19), Männer/Frauen
- **Rundenzeiten** (§22): 1/1,5/2/3 Minuten je nach Altersklasse, 1 Minute Pause
- **Gewichtsklassen** (§19): vollständige Tabellen für alle 5 Altersklassen inkl. Handschuhgröße (10/12 Unzen)
- **Gewichtskontrolle** (§20): 300g-Abzug für Wettkampfkleidung
- **10-Point-Must-Wertung** (§31): 10:9 / 10:8 / 10:7, 3 oder 5 Punktrichter
- **Leistungsklassen** (§12): C / B / A / Elite nach Siegzahl
- **Entscheidungsarten** (§30): KO, ABD, RSC, RSC-I, Punktsieg, Unentschieden, DSQ, WO, NC
- **Auslosung mit Freilosen** (§21): Reduktion auf 4/8/16 Boxer für die zweite Serie
- **Niederschlag-Abbruchregeln** (§26 Abs. 19): 3x/Runde oder 4x/Kampf (Erwachsene/Jugend),
  bzw. 2x/Runde oder 3x/Kampf (Schüler/Kadetten/Junioren)

**Bewusst noch offen gelassen** (nächste Ausbaustufen):
- Verbotene Handlungen / Foulkatalog (§32) als eigene Prüflogik
- Schutzsperren-Verwaltung (§33) inkl. Fristen (35 Tage / ein Vierteljahr / 1 Jahr)
- Sekundanten- und Kampfrichter-Lizenzverwaltung (§23-§29)
- Persistenz (Datenbank oder Dateispeicherung) für Teilnehmer und Ergebnisse
- Bedienoberfläche für Wiegen, Auslosung, Live-Wertung während des Kampfes
- Symmetrische Auslosung bei Einsatz eines Box-Pointers (§21 Abs. 7)

## Voraussetzungen

- [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/) (kostenlos)
- Java 17 oder neuer (JDK), z. B. [Eclipse Temurin](https://adoptium.net/)
- Maven-Unterstützung ist in Eclipse standardmäßig enthalten (m2e)

## Projekt in Eclipse öffnen

1. Eclipse starten → `File` → `Import...` → `Maven` → `Existing Maven Projects`
2. Diesen Projektordner auswählen → `Finish`
3. Eclipse lädt automatisch die Abhängigkeiten (JavaFX) über Maven herunter
4. `Main.java` per Rechtsklick → `Run As` → `Java Application` starten
   (alternativ im Terminal: `mvn javafx:run`)

## Mit GitHub verbinden

```bash
git init
git add .
git commit -m "Initial commit: Projektgerüst mit DBV-Wettkampfbestimmungen"
git branch -M main
git remote add origin https://github.com/DEIN-NAME/boxturnier-software.git
git push -u origin main
```

In Eclipse geht das auch grafisch über das integrierte **EGit** (Rechtsklick auf
Projekt → `Team` → `Share Project...` bzw. `Commit...` / `Push...`).

## Lizenz

Noch offen – bei einem freien Tool bietet sich z. B. die MIT-Lizenz an.
