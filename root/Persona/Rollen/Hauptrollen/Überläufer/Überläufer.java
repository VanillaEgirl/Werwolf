package root.Persona.Rollen.Hauptrollen.Überläufer;

import root.Frontend.Constants.FrontendControlType;
import root.Frontend.FrontendControl;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Überläufer_Fraktion;
import root.Persona.Hauptrolle;
import root.Persona.Rollen.Hauptrollen.Bürger.Dorfbewohner;
import root.Phases.NightBuilding.Constants.StatementType;
import root.ResourceManagement.ImagePath;
import root.Spieler;

import java.util.ArrayList;

public class Überläufer extends Hauptrolle {
    public static String title = "Karte tauschen";
    public static final String beschreibung = "Überläufer erwacht und entscheidet ob er seine Hauptrollenkarte tauschen möchte";
    public static StatementType statementType = StatementType.ROLLE_CHOOSE_ONE;
    public static final String NAME = "Überläufer";
    public static Fraktion fraktion = new Überläufer_Fraktion();
    public static final String IMAGE_PATH = ImagePath.ÜBERLÄUFER_KARTE;
    public static boolean spammable = false;

    public Überläufer() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
    }

    @Override
    public FrontendControl getDropdownOptions() {
        ArrayList<String> nehmbareHauptrollen = getMitteHauptrollenStrings();
        nehmbareHauptrollen.add("");
        return new FrontendControl(FrontendControlType.DROPDOWN_LIST, nehmbareHauptrollen);
    }

    @Override
    public void processChosenOption(String chosenOption) {
        Hauptrolle chosenHauptrolle = game.findHauptrolle(chosenOption);
        if (chosenHauptrolle != null) {
            try {
                Spieler spielerHauptrolle = game.findSpielerPerRolle(chosenHauptrolle.name);
                chosenHauptrolle = spielerHauptrolle.hauptrolle;

                Spieler spielerÜberläufer = game.findSpielerPerRolle(NAME);
                spielerÜberläufer.hauptrolle = chosenHauptrolle;
                spielerHauptrolle.hauptrolle = new Dorfbewohner();

                game.mitteHauptrollen.remove(chosenHauptrolle);
                game.mitteHauptrollen.add(this);
            } catch (NullPointerException e) {
                System.out.println(NAME + " nicht gefunden");
            }
        }
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
    public boolean isSpammable() {
        return spammable;
    }
}