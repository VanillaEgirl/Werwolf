package root.Phases.NightBuilding;

import root.Persona.Fraktion;
import root.Persona.Fraktionen.SchattenpriesterFraktion;
import root.Persona.Fraktionen.Vampire;
import root.Persona.Fraktionen.Werwölfe;
import root.Persona.Rolle;
import root.Persona.Rollen.Bonusrollen.Analytiker;
import root.Persona.Rollen.Bonusrollen.Archivar;
import root.Persona.Rollen.Bonusrollen.Dieb;
import root.Persona.Rollen.Bonusrollen.Gefängniswärter;
import root.Persona.Rollen.Bonusrollen.Konditor;
import root.Persona.Rollen.Bonusrollen.Konditorlehrling;
import root.Persona.Rollen.Bonusrollen.Medium;
import root.Persona.Rollen.Bonusrollen.Nachbar;
import root.Persona.Rollen.Bonusrollen.Nachtfürst;
import root.Persona.Rollen.Bonusrollen.Prostituierte;
import root.Persona.Rollen.Bonusrollen.Schnüffler;
import root.Persona.Rollen.Bonusrollen.Spurenleser;
import root.Persona.Rollen.Bonusrollen.Tarnumhang;
import root.Persona.Rollen.Bonusrollen.Totengräber;
import root.Persona.Rollen.Bonusrollen.Wahrsager;
import root.Persona.Rollen.Constants.WölfinState;
import root.Persona.Rollen.Hauptrollen.Bürger.HoldeMaid;
import root.Persona.Rollen.Hauptrollen.Bürger.Irrlicht;
import root.Persona.Rollen.Hauptrollen.Bürger.Orakel;
import root.Persona.Rollen.Hauptrollen.Bürger.Riese;
import root.Persona.Rollen.Hauptrollen.Bürger.Schamanin;
import root.Persona.Rollen.Hauptrollen.Bürger.Schattenmensch;
import root.Persona.Rollen.Hauptrollen.Bürger.Seherin;
import root.Persona.Rollen.Hauptrollen.Bürger.Späher;
import root.Persona.Rollen.Hauptrollen.Bürger.Wirt;
import root.Persona.Rollen.Hauptrollen.Vampire.GrafVladimir;
import root.Persona.Rollen.Hauptrollen.Vampire.LadyAleera;
import root.Persona.Rollen.Hauptrollen.Vampire.MissVerona;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Chemiker;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Schreckenswolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Wölfin;
import root.Persona.Rollen.Hauptrollen.Überläufer.Henker;
import root.Persona.Rollen.Hauptrollen.Überläufer.Überläufer;
import root.Phases.NightBuilding.Constants.IndieStatements;
import root.Phases.NightBuilding.Constants.ProgrammStatements;
import root.Phases.PhaseManager;
import root.Spieler;
import root.mechanics.Game;

import java.util.ArrayList;
import java.util.List;

public class NormalNightStatementBuilder {
    public static List<Statement> normalNightBuildStatements() {
        List<Statement> statements = new ArrayList<>();

        statements.add(IndieStatements.getAlleSchlafenEinStatement());
        if (Schattenmensch.shallBeTransformed) {
            Schattenmensch.transform();
        }

        if (PhaseManager.nightCount != 1 && Wirt.freibierCharges > 0) {
            addStatementRolle(statements, Wirt.ID);
        }

        if (Game.game.mitteBonusrollen.size() > 0) {
            addStatementRolle(statements, Totengräber.ID);
        }
        addStatementRolle(statements, Dieb.ID);
        addSecondStatementRolle(statements, Dieb.ID);

        addStatementRolle(statements, Gefängniswärter.ID);

        if (Game.game.mitteHauptrollen.size() > 0) {
            addStatementRolle(statements, Überläufer.ID);
        }

        addStatementRolle(statements, HoldeMaid.ID);
        addStatementRolle(statements, Irrlicht.ID);
        addSecondStatementRolle(statements, Irrlicht.ID);
        //TODO comment entfernen wenn detektiv wirklich gerext wird
        //Detektiv erwacht, schätzt die Anzahl der Bürger und bekommt ggf. eine Bürgerhauptrolle die im Spiel ist gezeigt
        addStatementRolle(statements, Schamanin.ID);

        statements.add(ProgrammStatements.getSchützeStatement());

        addStatementRolle(statements, Prostituierte.ID);

        //TODO move nacht add speziallogik into rollen/fraktionen
        //TODO addstatementRolle sollte nicht immer statements mitbekommen, statements sollte eine public klassenvariable sein.
        if (PhaseManager.nightCount == 1) {
            Spieler henkerSpieler = Game.game.findSpielerPerRolle(Henker.ID);
            if (henkerSpieler != null) {
                henkerSpieler.geschützt = true;
            }
        } else {
            addStatementRolle(statements, Henker.ID);
        }
        addStatementRolle(statements, Riese.ID);
        addStatementFraktion(statements, Werwölfe.ID);
        if (Wölfin.state == WölfinState.TÖTEND) {
            addStatementRolle(statements, Wölfin.ID);
        }
        addStatementRolle(statements, Schreckenswolf.ID);
        addStatementFraktion(statements, Vampire.ID);

        if (PhaseManager.nightCount != 1) {
            addSecondStatementRolle(statements, Nachtfürst.ID, false);
        }
        addStatementRolle(statements, Nachtfürst.ID);

        addStatementFraktion(statements, SchattenpriesterFraktion.ID);
        addSecondStatementFraktion(statements, SchattenpriesterFraktion.ID);
        addStatementRolle(statements, Chemiker.ID);
        addSecondStatementRolle(statements, Chemiker.ID);

        addStatementRolle(statements, LadyAleera.ID);
        addStatementRolle(statements, MissVerona.ID);

        addStatementRolle(statements, Analytiker.ID);
        addStatementRolle(statements, Archivar.ID);
        addStatementRolle(statements, Schnüffler.ID);
        addStatementRolle(statements, Tarnumhang.ID);
        addStatementRolle(statements, Seherin.ID);
        addStatementRolle(statements, Späher.ID);
        addStatementRolle(statements, Orakel.ID);
        addStatementRolle(statements, Medium.ID);

        addStatementRolle(statements, GrafVladimir.ID);

        addStatementRolle(statements, Nachbar.ID);
        addStatementRolle(statements, Spurenleser.ID);

        addStatementRolle(statements, Wahrsager.ID);

        if (Game.game.getBonusrolleInGameIDs().contains(Konditorlehrling.ID)) {
            addStatementRolle(statements, Konditorlehrling.ID);
        } else {
            addStatementRolle(statements, Konditor.ID);
        }

        statements.add(IndieStatements.getAlleWachenAufStatement());
        statements.add(IndieStatements.getOpferStatement());

        addSecondStatementRolle(statements, Schreckenswolf.ID); //TODO verstummungsstatement nicht adden wenn schreckenswolf nur bamboozelrolle ist

        if (Wölfin.state == WölfinState.TÖTEND) {
            addSecondStatementRolle(statements, Wölfin.ID);
        }

        statements.add(ProgrammStatements.getTortenProgrammStatement());

        return statements;
    }

    private static void addStatementRolle(List<Statement> statements, String rolleID) {
        Rolle rolle = Rolle.findRolle(rolleID);
        Statement statement = new Statement(rolle);
        statements.add(statement);
    }

    private static void addSecondStatementRolle(List<Statement> statements, String rolleID) {
        addSecondStatementRolle(statements, rolleID, true);
    }

    private static void addSecondStatementRolle(List<Statement> statements, String rolleID, boolean dependendOnFirstStatement) {
        Rolle rolle = Rolle.findRolle(rolleID);

        Statement statement;

        if (dependendOnFirstStatement) {
            Statement firstStatement = statements.stream()
                    .filter(s -> s.id.equals(rolle.statementID))
                    .findAny().orElse(null);
            statement = new Statement(rolle, firstStatement);
        } else {
            statement = new Statement(rolle, true);
        }

        statements.add(statement);
    }

    private static void addStatementFraktion(List<Statement> statements, String fraktionID) {
        Fraktion fraktion = Fraktion.findFraktion(fraktionID);
        Statement statement = new Statement(fraktion);
        statements.add(statement);
    }

    private static void addSecondStatementFraktion(List<Statement> statements, String fraktionID) {
        Fraktion fraktion = Fraktion.findFraktion(fraktionID);

        Statement firstStatement = statements.stream()
                .filter(statement -> statement.id.equals(fraktion.statementID))
                .findAny().orElse(null);

        Statement statement = new Statement(fraktion, firstStatement);
        statements.add(statement);
    }
}
