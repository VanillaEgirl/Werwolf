package root.Persona;

import root.Persona.Fraktionen.SchattenpriesterFraktion;
import root.Persona.Fraktionen.Werwölfe;
import root.Persona.Rollen.Constants.Zeigekarten.FraktionsZeigekarten.BürgerZeigekarte;
import root.Persona.Rollen.Constants.Zeigekarten.Zeigekarte;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Chemiker;
import root.Phases.NormalNight;
import root.Spieler;
import root.mechanics.KillLogik.Opfer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fraktion extends Persona {
    public Zeigekarte zeigekarte = new BürgerZeigekarte();

    public static List<Spieler> getFraktionsMembers(String fraktionID) {
        List<Spieler> fraktionsMembers = new ArrayList<>();

        for (Spieler currentSpieler : game.spieler) {
            if (currentSpieler.lebend && currentSpieler.hauptrolle.fraktion.equals(fraktionID)) {
                fraktionsMembers.add(currentSpieler);
            }
        }

        return fraktionsMembers;
    }

    public static List<String> getFraktionsMemberStrings(String fraktionID) {
        return getFraktionsMembers(fraktionID).stream()
                .map(fraktion -> fraktion.name)
                .collect(Collectors.toList());
    }

    public static boolean fraktionContainedInNight(String fraktionID) {
        if (getFraktionIDs().contains(fraktionID)) {
            return !fraktionOffenkundigTot(fraktionID);
        } else {
            return false;
        }
    }

    public static boolean fraktionLebend(String fraktionID) {
        for (Spieler currentSpieler : game.spieler) {
            if (currentSpieler.hauptrolle.fraktion.equals(fraktionID) && currentSpieler.lebend) {
                return true;
            }
        }

        return false;
    }

    public static boolean fraktionOffenkundigTot(String fraktionID) {
        if (fraktionID.equals(SchattenpriesterFraktion.ID)) {
            return false;
        }
        if (fraktionID.equals(Werwölfe.ID) && game.getHauptrolleInGameIDs().contains(Chemiker.ID)) {
            return false;
        }

        int numberHauptrollenInGame = 0;
        for (Hauptrolle hauptrolle : game.hauptrollenInGame) {
            if (hauptrolle.fraktion.equals(fraktionID)) {
                numberHauptrollenInGame++;
            }
        }

        int numberMitteHauptrollen = 0;
        for (Hauptrolle mitteHauptrolle : game.mitteHauptrollen) {
            if (mitteHauptrolle.fraktion.equals(fraktionID)) {
                numberMitteHauptrollen++;
            }
        }

        return numberMitteHauptrollen >= numberHauptrollenInGame;
    }

    public static boolean fraktionOpfer(String fraktionID) {
        for (Spieler currentSpieler : game.spieler) {
            Fraktion fraktionSpieler = currentSpieler.hauptrolle.fraktion;

            if (fraktionID.equals(Werwölfe.ID)) {
                if (fraktionSpieler.equals(fraktionID) && currentSpieler.hauptrolle.killing) {
                    if (currentSpieler.lebend && !Opfer.isOpfer(currentSpieler.name)) {
                        return false;
                    }
                }
            } else {
                if (fraktionSpieler.equals(fraktionID)) {
                    if (currentSpieler.lebend && !Opfer.isOpfer(currentSpieler.name)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean fraktionAktiv(String fraktionID) {
        List<Spieler> livingSpieler = game.getLivingSpieler();

        for (Opfer opfer : NormalNight.opfer) {
            livingSpieler.remove(opfer.spieler);
        }

        for (Spieler currentSpieler : livingSpieler) {
            Fraktion fraktionSpieler = currentSpieler.hauptrolle.fraktion;

            if (fraktionID.equals(Werwölfe.ID)) {
                if (fraktionSpieler.equals(fraktionID) && currentSpieler.hauptrolle.killing) {
                    if (currentSpieler.aktiv) {
                        return true;
                    }
                }
            } else {
                if (fraktionSpieler.equals(fraktionID)) {
                    if (currentSpieler.aktiv) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static List<Fraktion> getFraktionen() {
        return game.hauptrollenInGame.stream()
                .map(rolle -> rolle.fraktion.id)
                .distinct()
                .map(Fraktion::findFraktion)
                .collect(Collectors.toList());
    }

    public static List<String> getFraktionIDs() {
        return getFraktionen().stream()
                .map(fraktion -> fraktion.id)
                .collect(Collectors.toList());
    }

    public static List<String> getFraktionStrings() {
        return getFraktionen().stream()
                .map(fraktion -> fraktion.name)
                .collect(Collectors.toList());
    }

    public static List<Fraktion> getLivingFraktionen() {
        return game.spieler.stream()
                .filter(spieler -> spieler.lebend)
                .map(spieler -> spieler.hauptrolle.fraktion.id)
                .distinct()
                .map(Fraktion::findFraktion)
                .collect(Collectors.toList());
    }

    public static List<String> getLivingFraktionStrings() {
        return getLivingFraktionen().stream()
                .map(fraktion -> fraktion.name)
                .collect(Collectors.toList());
    }

    public static Fraktion findFraktion(String fraktionID) {
        return game.hauptrollen.stream()
                .map(rolle -> rolle.fraktion)
                .filter(fraktion -> fraktion.equals(fraktionID))
                .findAny().orElse(null);
    }

    public static Fraktion findFraktionPerName(String fraktionName) {
        return game.hauptrollen.stream()
                .map(rolle -> rolle.fraktion)
                .filter(fraktion -> fraktion.name.equals(fraktionName))
                .findAny().orElse(null);
    }
}
