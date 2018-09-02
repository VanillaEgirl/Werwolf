package root.Persona.Rollen.Hauptrollen.Vampire;

import root.Frontend.Constants.FrontendControlType;
import root.Frontend.FrontendControl;
import root.Phases.NightBuilding.Constants.StatementType;
import root.ResourceManagement.ImagePath;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Vampire;
import root.Persona.Hauptrolle;
import root.mechanics.Opfer;

import java.util.ArrayList;

public class MissVerona extends Hauptrolle {
    public static String title = "Angegriffene Opfer";
    public static final String beschreibung = "Miss Verona erwacht und lässt sich Auskunft über die Spieler geben, die angegriffen wurden";
    public static StatementType statementType = StatementType.ROLLE_INFO;
    public static final String name = "Miss Verona";
    public static Fraktion fraktion = new Vampire();
    public static final String imagePath = ImagePath.MISS_VERONA_KARTE;
    public static boolean spammable = false;
    public static boolean killing = true;

    @Override
    public FrontendControl getInfo() {
        return new FrontendControl(FrontendControlType.LIST, findUntote());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getBeschreibung() {
        return beschreibung;
    }

    @Override
    public StatementType getStatementType() {
        return statementType;
    }

    @Override
    public Fraktion getFraktion() {
        return fraktion;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean isSpammable() {
        return spammable;
    }

    @Override
    public boolean isKilling() {
        return killing;
    }

    public ArrayList<String> findUntote() {
        ArrayList<String> untote = new ArrayList<>();

        for (Opfer possibleOpfer : Opfer.possibleVictims) {
            boolean überlebt = true;
            String currentPossibleOpferName = possibleOpfer.opfer.name;

            for (Opfer deadOpfer : Opfer.deadVictims) {
                if (currentPossibleOpferName.equals(deadOpfer.opfer.name)) {
                    überlebt = false;
                }
            }

            if (überlebt) {
                if (!untote.contains(currentPossibleOpferName))
                    untote.add(currentPossibleOpferName);
            }
        }

        return untote;
    }
}