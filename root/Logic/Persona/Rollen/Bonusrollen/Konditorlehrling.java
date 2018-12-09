package root.Logic.Persona.Rollen.Bonusrollen;

import root.Frontend.FrontendControl;
import root.Logic.Persona.Bonusrolle;
import root.Logic.Persona.Rollen.Constants.BonusrollenType.Aktiv;
import root.Logic.Persona.Rollen.Constants.BonusrollenType.BonusrollenType;
import root.Logic.Phases.Statement.Constants.StatementType;
import root.ResourceManagement.ImagePath;

public class Konditorlehrling extends Bonusrolle {
    public static final String ID = "ID_Konditorlehrling";
    public static final String NAME = "Konditorlehrling";
    public static final String IMAGE_PATH = ImagePath.KONDITORLEHRLING_KARTE;
    public static final BonusrollenType TYPE = new Aktiv();

    public static final String STATEMENT_ID = ID;
    public static final String STATEMENT_TITLE = Konditor.STATEMENT_TITLE;
    public static final String STATEMENT_BESCHREIBUNG = "Konditor und Konditorlehrling erwachen und entscheiden sich ob es eine gute oder schlechte Torte gibt";
    public static final StatementType STATEMENT_TYPE = StatementType.ROLLE_SPECAL;

    public Konditorlehrling() {
        this.id = ID;
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.type = TYPE;

        this.statementID = STATEMENT_ID;
        this.statementTitle = STATEMENT_TITLE;
        this.statementBeschreibung = STATEMENT_BESCHREIBUNG;
        this.statementType = STATEMENT_TYPE;
    }

    @Override
    public FrontendControl getDropdownOptionsFrontendControl() {
        return Konditor.getTortenOptionsFrontendControl();
    }
}