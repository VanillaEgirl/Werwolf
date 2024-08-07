package root.Logic.Persona.Rollen.Hauptrollen.Henker;

import root.Controller.FrontendObject.DropdownListFrontendObject;
import root.Controller.FrontendObject.FrontendObject;
import root.Frontend.Utils.DropdownOptions;
import root.Logic.Game;
import root.Logic.Persona.Fraktion;
import root.Logic.Persona.Fraktionen.HenkerFraktion;
import root.Logic.Persona.Hauptrolle;
import root.Logic.Persona.Rollen.Constants.DropdownConstants;
import root.Logic.Persona.Rollen.Hauptrollen.Bürger.Dorfbewohner;
import root.Logic.Phases.Statement.Constants.StatementType;
import root.Logic.Spieler;
import root.ResourceManagement.ImagePath;

public class Überläufer extends Hauptrolle {
    public static final String ID = "ID_Überläufer";
    public static final String NAME = "Überläufer";
    public static final String IMAGE_PATH = ImagePath.ÜBERLÄUFER_KARTE;
    public static final Fraktion FRAKTION = new HenkerFraktion();

    public static final String STATEMENT_ID = ID;
    public static final String STATEMENT_TITLE = "Karte tauschen";
    public static final String STATEMENT_BESCHREIBUNG = "Überläufer erwacht und entscheidet ob er seine Hauptrollenkarte tauschen möchte";
    public static final StatementType STATEMENT_TYPE = StatementType.PERSONA_CHOOSE_ONE;

    public Überläufer() {
        this.id = ID;
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.fraktion = FRAKTION;

        this.statementID = STATEMENT_ID;
        this.statementTitle = STATEMENT_TITLE;
        this.statementBeschreibung = STATEMENT_BESCHREIBUNG;
        this.statementType = STATEMENT_TYPE;
    }

    @Override
    public FrontendObject getFrontendObject() {
        return new DropdownListFrontendObject(new DropdownOptions(getMitteHauptrollenNames(), DropdownConstants.EMPTY));
    }

    @Override
    public void processChosenOption(String chosenOption) {
        Hauptrolle chosenHauptrolle = Game.game.findHauptrollePerName(chosenOption);
        if (chosenHauptrolle != null) {
            try {
                Spieler spielerHauptrolle = Game.game.findSpielerPerRolle(chosenHauptrolle.id);
                chosenHauptrolle = spielerHauptrolle.hauptrolle;

                Spieler spielerÜberläufer = Game.game.findSpielerPerRolle(this.id);
                spielerÜberläufer.hauptrolle = chosenHauptrolle;
                spielerHauptrolle.hauptrolle = new Dorfbewohner();

                Game.game.mitteHauptrollen.remove(chosenHauptrolle);
                Game.game.mitteHauptrollen.add(this);
            } catch (NullPointerException e) {
                System.out.println(NAME + " nicht gefunden");
            }
        }
    }
}