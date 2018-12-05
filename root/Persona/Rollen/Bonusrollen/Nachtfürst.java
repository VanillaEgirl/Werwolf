package root.Persona.Rollen.Bonusrollen;

import root.Frontend.Constants.FrontendControlType;
import root.Frontend.FrontendControl;
import root.Frontend.Utils.DropdownOptions;
import root.Persona.Bonusrolle;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Vampire;
import root.Persona.Fraktionen.Werwölfe;
import root.Persona.Hauptrolle;
import root.Persona.Rollen.Constants.BonusrollenType.Aktiv;
import root.Persona.Rollen.Constants.BonusrollenType.BonusrollenType;
import root.Persona.Rollen.Constants.DropdownConstants;
import root.Persona.Rollen.Hauptrollen.Überläufer.Henker;
import root.Phases.NightBuilding.Constants.StatementType;
import root.ResourceManagement.ImagePath;
import root.Spieler;
import root.mechanics.Game;
import root.mechanics.KillLogik.NormalKill;

import java.util.ArrayList;
import java.util.List;

public class Nachtfürst extends Bonusrolle {
    public static final String ID = "ID_Nachtfürst";
    public static final String NAME = "Nachtfürst";
    public static final String IMAGE_PATH = ImagePath.NACHTFÜRST_KARTE;
    public static final BonusrollenType TYPE = new Aktiv();

    public static final String SCHÄTZEN_ID = ID;
    public static final String STATEMENT_TITLE = "Anzahl der toten Opfer";
    public static final String STATEMENT_BESCHREIBUNG = "Nachtfürst erwacht und schätzt, wieviele Opfer es in der Nacht wird";
    public static final StatementType STATEMENT_TYPE = StatementType.ROLLE_CHOOSE_ONE;

    public static final String TÖTEN_ID = "Nachtfürst_Kill";
    public static final String SECOND_STATEMENT_TITLE = "Opfer wählen";
    public static final String SECOND_STATEMENT_BESCHREIBUNG = "Nachtfürst erwacht und tötet einen Spieler wenn er letzte Nacht richtig lag"; //TODO er ist ja schon wach, wording ändern
    public static final StatementType SECOND_STATEMENT_TYPE = StatementType.ROLLE_SPECAL;

    public static final String KEIN_OPFER = "Kein Opfer";

    private Integer tipp = null;
    public boolean guessedRight = false;

    public Nachtfürst() {
        this.id = ID;
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.type = TYPE;

        this.statementID = SCHÄTZEN_ID;
        this.statementTitle = STATEMENT_TITLE;
        this.statementBeschreibung = STATEMENT_BESCHREIBUNG;
        this.statementType = STATEMENT_TYPE;

        this.secondStatementID = TÖTEN_ID;
        this.secondStatementTitle = SECOND_STATEMENT_TITLE;
        this.secondStatementBeschreibung = SECOND_STATEMENT_BESCHREIBUNG;
        this.secondStatementType = SECOND_STATEMENT_TYPE;

        this.spammable = true;
    }

    @Override
    public FrontendControl getDropdownOptionsFrontendControl() {
        List<String> numbers = new ArrayList<>();
        numbers.add("1");
        numbers.add("2");
        numbers.add("3");
        numbers.add("4");
        numbers.add("5"); //TODO 5 genügt?
        return new FrontendControl(FrontendControlType.DROPDOWN_LIST, new DropdownOptions(numbers, KEIN_OPFER));
    }

    @Override
    public void processChosenOption(String chosenOption) {
        if (chosenOption != null) {
            if (chosenOption.equals(KEIN_OPFER)) {
                tipp = 0;
            } else {
                tipp = Integer.parseInt(chosenOption);
            }
        } else {
            tipp = null;
        }
    }

    public FrontendControl getSecondDropdownOptionsFrontendControl() {
        if (isTötendeFraktion() && guessedRight) {
            return new FrontendControl(Game.game.getSpielerDropdownOptions(true));
        } else {
            return new FrontendControl(new DropdownOptions(new ArrayList<>(), DropdownConstants.EMPTY));
        }
    }

    public void processSecondChosenOption(String chosenOption) {
        Spieler chosenSpieler = Game.game.findSpieler(chosenOption);
        Spieler nachtfürstSpieler = Game.game.findSpielerPerRolle(id);
        if (chosenSpieler != null && nachtfürstSpieler != null) {
            besucht = chosenSpieler;
            NormalKill.execute(chosenSpieler, nachtfürstSpieler);
        }
    }

    public boolean isTötendeFraktion() {
        Spieler nachtfürstSpieler = Game.game.findSpielerPerRolle(id);

        if (nachtfürstSpieler != null) {
            Fraktion fraktion = nachtfürstSpieler.getFraktion();
            Hauptrolle hauptrolle = nachtfürstSpieler.hauptrolle;
            return fraktion.equals(Werwölfe.ID) || fraktion.equals(Vampire.ID) || hauptrolle.equals(Henker.ID);
        } else {
            return false;
        }
    }

    public void checkGuess(int anzahlOpferDerNacht) {
        if (tipp == null) {
            guessedRight = false;
        } else {
            guessedRight = (anzahlOpferDerNacht == tipp);
        }

        if (guessedRight) {
            System.out.println("Nachtfürst lag richtig");
        } else {
            System.out.println("Nachtfürst lga falsch");
        }
    }
}
