package root.Persona.Rollen.Hauptrollen.Vampire;

import root.Frontend.FrontendControl;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Vampire;
import root.Persona.Hauptrolle;
import root.Phases.NightBuilding.Constants.StatementType;
import root.ResourceManagement.ImagePath;
import root.Spieler;

public class LadyAleera extends Hauptrolle {
    public static final String STATEMENT_IDENTIFIER = "LadyAleera";
    public static final String STATEMENT_TITLE = "Geschützte Spieler";
    public static final String STATEMENT_BESCHREIBUNG = "Lady Aleera erwacht und wählt einen Spieler über den man in dieser Nacht nurnoch falsche Informationen bekommt";
    public static final StatementType STATEMENT_TYPE = StatementType.ROLLE_CHOOSE_ONE;

    public static final String NAME = "Lady Aleera";
    public static final String IMAGE_PATH = ImagePath.LADY_ALEERA_KARTE;
    public static final Fraktion FRAKTION = new Vampire();

    public static Spieler verschleierterSpieler;
    
    public LadyAleera() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.fraktion = FRAKTION;

        this.statementIdentifier = STATEMENT_IDENTIFIER;
        this.statementTitle = STATEMENT_TITLE;
        this.statementBeschreibung = STATEMENT_BESCHREIBUNG;
        this.statementType = STATEMENT_TYPE;

        this.spammable = false;
        this.killing = true;
    }

    @Override
    public FrontendControl getDropdownOptions() {
        return game.getPlayerCheckSpammableFrontendControl(this);
    }

    @Override
    public void processChosenOption(String chosenOption) {
        Spieler chosenPlayer = game.findSpieler(chosenOption);
        if (chosenPlayer != null) {
            besucht = chosenPlayer;

            verschleierterSpieler = chosenPlayer;
        }
    }
}