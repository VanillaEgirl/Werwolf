package root.Logic.Persona.Rollen.Hauptrollen.Vampire;

import root.Controller.FrontendObject.FrontendObject;
import root.Logic.Game;
import root.Logic.Persona.Fraktion;
import root.Logic.Persona.Fraktionen.Vampire;
import root.Logic.Persona.Hauptrolle;
import root.Logic.Phases.Statement.Constants.StatementType;
import root.Logic.Spieler;
import root.ResourceManagement.ImagePath;

public class GrafVladimir extends Hauptrolle {
    public static final String ID = "ID_Graf_Vladimir";
    public static final String NAME = "Graf Vladimir";
    public static final String IMAGE_PATH = ImagePath.GRAF_VLADIMIR_KARTE;
    public static final Fraktion FRAKTION = new Vampire();

    public static final String STATEMENT_ID = ID;
    public static final String STATEMENT_TITLE = "Spieler unerkennbar machen";
    public static final String STATEMENT_BESCHREIBUNG = "Graf Vladimir erwacht und verschleiert die Identität eines Spielers";
    public static final StatementType STATEMENT_TYPE = StatementType.PERSONA_CHOOSE_ONE;

    public static Spieler verschleierterSpieler;

    public GrafVladimir() {
        this.id = ID;
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.fraktion = FRAKTION;

        this.statementID = STATEMENT_ID;
        this.statementTitle = STATEMENT_TITLE;
        this.statementBeschreibung = STATEMENT_BESCHREIBUNG;
        this.statementType = STATEMENT_TYPE;

        this.spammable = false;
        this.selfuseable = true;
        this.killing = true;
    }

    @Override
    public FrontendObject getFrontendObject() {
        return Game.game.getSpielerFrontendObject(this);
    }

    @Override
    public void processChosenOption(String chosenOption) {
        Spieler chosenSpieler = Game.game.findSpieler(chosenOption);
        if (chosenSpieler != null) {
            besucht = chosenSpieler;
            verschleierterSpieler = chosenSpieler;
        }
    }

    @Override
    public void beginNight() {
        super.beginNight();
        verschleierterSpieler = null;
    }
}