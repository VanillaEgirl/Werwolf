package root.Phases;

import root.Frontend.FrontendControl;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Schattenpriester_Fraktion;
import root.Persona.Fraktionen.Vampire;
import root.Persona.Fraktionen.Werwölfe;
import root.Persona.Hauptrolle;
import root.Persona.Nebenrolle;
import root.Persona.Rolle;
import root.Persona.Rollen.Constants.DropdownConstants;
import root.Persona.Rollen.Hauptrollen.Bürger.Sammler;
import root.Persona.Rollen.Hauptrollen.Bürger.Wirt;
import root.Persona.Rollen.Hauptrollen.Schattenpriester.Schattenpriester;
import root.Persona.Rollen.Hauptrollen.Vampire.GrafVladimir;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Blutwolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Chemiker;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Schreckenswolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Wölfin;
import root.Persona.Rollen.Nebenrollen.*;
import root.Phases.NightBuilding.Constants.IndieStatements;
import root.Phases.NightBuilding.Constants.ProgrammStatements;
import root.Phases.NightBuilding.Constants.StatementType;
import root.Phases.NightBuilding.NormalNightStatementBuilder;
import root.Phases.NightBuilding.Statement;
import root.Phases.NightBuilding.StatementFraktion;
import root.Phases.NightBuilding.StatementRolle;
import root.ResourceManagement.ImagePath;
import root.Spieler;
import root.mechanics.Game;
import root.mechanics.Liebespaar;
import root.mechanics.Opfer;
import root.mechanics.Torte;

import java.util.ArrayList;

public class Nacht extends Thread {
    Game game;

    public static final String TORTE_TITLE = "";
    public static final String TOT_TITLE = "Tot";
    public static final String DEAKTIVIERT_TITLE = "Deaktiviert";
    public static final String AUFGEBRAUCHT_TITLE = "Aufgebraucht";

    public static ArrayList<Statement> statements;
    public static Object lock;

    public static ArrayList<Spieler> playersAwake = new ArrayList<>();
    public static boolean wölfinKilled;
    public static Spieler wölfinSpieler;
    public static Spieler beschworenerSpieler;

    public Nacht(Game game) {
        this.game = game;
    }

    public void run() {
        boolean freibier = false;

        lock = new Object();
        synchronized (lock) {
            FrontendControl dropdownOtions;
            FrontendControl info;
            String chosenOption;
            String chosenOptionLastStatement = null;

            Rolle rolle = null;

            String imagePath;

            Opfer chosenOpfer;
            Spieler chosenPlayer;
            wölfinKilled = false;
            wölfinSpieler = null;
            beschworenerSpieler = null;

            ArrayList<String> spielerOrNon = game.getLivingPlayerOrNoneStrings();

            beginNight();

            statements = NormalNightStatementBuilder.normaleNachtBuildStatements();

            for (Statement statement : statements) {
                chosenOption = null;

                if (statement.isVisible() || statement.type == StatementType.PROGRAMM) {
                    setPlayersAwake(statement);

                    switch (statement.type) {
                        case SHOW_TITLE:
                            showTitle(statement);
                            break;

                        case ROLLE_CHOOSE_ONE:
                            rolle = ((StatementRolle) statement).getRolle();

                            if (rolle.abilityCharges > 0) {
                                dropdownOtions = rolle.getDropdownOptions();
                                chosenOption = showFrontendControl(statement, dropdownOtions);
                                rolle.processChosenOption(chosenOption);
                            } else {
                                showAufgebrauchtPages(statement); //TODO deaktiv/tot beachten
                            }
                            break;

                        case ROLLE_CHOOSE_ONE_INFO:
                            rolle = ((StatementRolle) statement).getRolle();

                            if (rolle.abilityCharges > 0) {
                                dropdownOtions = rolle.getDropdownOptions();
                                chosenOption = showFrontendControl(statement, dropdownOtions);
                                info = rolle.processChosenOptionGetInfo(chosenOption);
                                showFrontendControl(statement, info);
                            } else {
                                showAufgebrauchtPages(statement); //TODO deaktiv/tot beachten
                            }
                            break;

                        case ROLLE_INFO:
                            rolle = ((StatementRolle) statement).getRolle();

                            info = rolle.getInfo();
                            showFrontendControl(statement, info);
                            break;

                        case ROLLE_SPECAL:
                            rolle = ((StatementRolle) statement).getRolle();
                            break;

                        case FRAKTION_CHOOSE_ONE:
                            Fraktion fraktion = ((StatementFraktion) statement).getFraktion();

                            dropdownOtions = fraktion.getDropdownOptions();
                            chosenOption = showFrontendControl(statement, dropdownOtions);
                            fraktion.processChosenOption(chosenOption);
                            break;
                    }

                    switch (statement.beschreibung) {
                        case Wirt.beschreibung:
                            if (DropdownConstants.JA.name.equals(chosenOption)) {
                                freibier = true;
                            }
                            break;

                        case ProgrammStatements.SCHÜTZE:
                            setSchütze();
                            break;

                        case Schreckenswolf.beschreibung:
                            Schreckenswolf schreckenswolf = (Schreckenswolf) rolle;
                            if (schreckenswolf!=null && schreckenswolf.werwölfeKilledOnSchutz()) {
                                dropdownOtions = schreckenswolf.getDropdownOptions();
                                chosenOption = showFrontendControl(statement, dropdownOtions);
                                schreckenswolf.processChosenOption(chosenOption);
                            } else {
                                showImage(statement, ImagePath.PASSIV);
                            }
                            break;

                        case Wölfin.beschreibung:
                            if (!"".equals(chosenOption)) {
                                wölfinKilled = true;
                                wölfinSpieler = game.findSpielerPerRolle(Wölfin.name);
                            }
                            break;

                        case Schattenpriester_Fraktion.NEUER_SCHATTENPRIESTER:
                            chosenPlayer = game.findSpieler(chosenOptionLastStatement);
                            String neuerSchattenpriester = "";
                            imagePath = "";
                            if (chosenPlayer != null) {
                                neuerSchattenpriester = chosenPlayer.name;

                                if (!chosenPlayer.hauptrolle.getFraktion().getName().equals(Schattenpriester_Fraktion.name)) {
                                    imagePath = Schattenkutte.imagePath;
                                }
                            }
                            showListShowImage(statement, neuerSchattenpriester, ImagePath.SCHATTENPRIESTER_ICON, imagePath);
                            break;

                        case Chemiker.NEUER_WERWOLF:
                            chosenPlayer = game.findSpieler(chosenOptionLastStatement);
                            String neuerWerwolf = "";
                            if (chosenPlayer != null) {
                                neuerWerwolf = chosenPlayer.name;
                            }

                            showListShowImage(statement, neuerWerwolf, ImagePath.WÖLFE_ICON);
                            break;

                        case Analytiker.beschreibung:
                            Spieler analytikerSpieler = game.findSpielerPerRolle(rolle.getName());
                            if (Rolle.rolleLebend(Analytiker.name)) {
                                ArrayList<String> spielerOrNonWithoutAnalytiker = (ArrayList<String>) spielerOrNon.clone();
                                spielerOrNonWithoutAnalytiker.remove(analytikerSpieler.name);
                                showDropdownPage(statement, spielerOrNonWithoutAnalytiker, spielerOrNonWithoutAnalytiker);
                            } else {
                                showDropdownPage(statement, spielerOrNon, spielerOrNon);
                            }

                            Spieler chosenSpieler1 = game.findSpieler(FrontendControl.erzählerFrame.chosenOption1);
                            Spieler chosenSpieler2 = game.findSpieler(FrontendControl.erzählerFrame.chosenOption2);

                            if (chosenSpieler1 != null && chosenSpieler2 != null) {
                                if (((Analytiker) rolle).showTarnumhang(chosenSpieler1, chosenSpieler2)) {
                                    imagePath = Tarnumhang.imagePath;
                                    statement.title = Tarnumhang.title;
                                    showImage(statement, imagePath);
                                } else {
                                    String answer = ((Analytiker) rolle).analysiere(chosenSpieler1, chosenSpieler2);
                                    showList(statement, answer);//TODO generisch machen
                                }
                            }
                            break;

                        case ProgrammStatements.WAHRSAGER:
                            if (Wahrsager.isGuessing) {
                                Spieler wahrsagerSpieler2 = game.findSpielerPerRolle(Wahrsager.name);
                                Spieler deadWahrsagerSpieler = game.findSpielerOrDeadPerRolle(Wahrsager.name);
                                if (wahrsagerSpieler2 != null) {
                                    Wahrsager wahrsager = (Wahrsager) deadWahrsagerSpieler.nebenrolle;
                                    if (wahrsager.guessedRight()) {
                                        //schönlinge.add(wahrsagerSpieler2);
                                    }
                                }
                            } else {
                                Wahrsager.isGuessing = true;
                            }

                            if (!(game.getLivingPlayer().size() > 4)) {
                                Wahrsager.isGuessing = false;
                            }
                            break;

                        case Wahrsager.beschreibung:
                            Spieler wahrsagerSpieler1 = game.findSpielerOrDeadPerRolle(Wahrsager.name);
                            if (wahrsagerSpieler1 != null) {
                                Wahrsager wahrsager = (Wahrsager) wahrsagerSpieler1.nebenrolle;

                                wahrsager.tipp = Fraktion.findFraktion(chosenOption);
                            }
                            break;

                        case Konditor.beschreibung:
                        case Konditorlehrling.beschreibung:
                            //TODO evaluieren ob Page angezeigt werden soll wenn gibtEsTorte();
                            if (Opfer.deadVictims.size() == 0) {
                                if (gibtEsTorte()) {
                                    Torte.torte = true;

                                    dropdownOtions = rolle.getDropdownOptions();
                                    chosenOption = showKonditorDropdownPage(statement, dropdownOtions);
                                    rolle.processChosenOption(chosenOption);

                                    Torte.gut = chosenOption.equals(DropdownConstants.GUT.name);
                                }
                            }
                            break;

                        case ProgrammStatements.OPFER:
                            setOpfer();
                            break;

                        case IndieStatements.OPFER:
                            ArrayList<String> opferDerNacht = new ArrayList<>();

                            for (Opfer currentOpfer : Opfer.deadVictims) {
                                if (!opferDerNacht.contains(currentOpfer.opfer.name)) {
                                    if (currentOpfer.opfer.nebenrolle.getName().equals(Wahrsager.name)) {
                                        Wahrsager.isGuessing = false;
                                    }
                                    opferDerNacht.add(currentOpfer.opfer.name);
                                }
                            }

                            FrontendControl.erzählerListPage(statement, IndieStatements.OPFER_TITLE, opferDerNacht);
                            for (String opfer : opferDerNacht) {
                                FrontendControl.spielerAnnounceVictimPage(game.findSpieler(opfer));
                                waitForAnswer();
                            }


                            refreshHexenSchutz();

                            checkVictory();
                            break;

                        case Schreckenswolf.VERSTUMMT:
                            if (beschworenerSpieler != null) {
                                FrontendControl.erzählerListPage(statement, beschworenerSpieler.name);
                                FrontendControl.spielerIconPicturePage(beschworenerSpieler.name, ImagePath.VERSTUMMT);

                                waitForAnswer();
                            }
                            break;

                        case ProgrammStatements.TORTE:
                            if (Torte.torte) {
                                FrontendControl.erzählerTortenPage();
                                FrontendControl.spielerIconPicturePage(TORTE_TITLE, ImagePath.TORTE);

                                waitForAnswer();
                            }
                            break;
                    }

                    chosenOptionLastStatement = chosenOption;

                    if (freibier) {
                        break;
                    }
                }
            }
        }

        cleanUp();

        if (freibier) {
            game.freibierDay();
        } else {
            game.day();
        }
    }

    public void beginNight() {
        for (Spieler currentSpieler : game.spieler) {
            String fraktionSpieler = currentSpieler.hauptrolle.getFraktion().getName();

            currentSpieler.ressurectable = !fraktionSpieler.equals(Vampire.name);
        }

        Opfer.possibleVictims = new ArrayList<>();
        Opfer.deadVictims = new ArrayList<>();

        for (Hauptrolle currentHauptrolle : game.mainRoles) {
            currentHauptrolle.besuchtLetzteNacht = currentHauptrolle.besucht;
            currentHauptrolle.besucht = null;
        }

        for (Nebenrolle currentNebenrolle : game.secondaryRoles) {
            currentNebenrolle.besuchtLetzteNacht = currentNebenrolle.besucht;
            currentNebenrolle.besucht = null;

            if (currentNebenrolle.getName().equals(Analytiker.name)) {
                ((Analytiker) currentNebenrolle).besuchtAnalysieren = null;
            }
        }

        if (Rolle.rolleLebend(Prostituierte.name)) {
            Spieler prostituierte = game.findSpielerPerRolle(Prostituierte.name);
            Prostituierte.host = prostituierte;
        }

        for (Spieler currentSpieler : game.spieler) {
            Hauptrolle hauptrolleSpieler = currentSpieler.hauptrolle;

            if (hauptrolleSpieler.getName().equals(Schattenpriester.name)) {
                if (((Schattenpriester) hauptrolleSpieler).neuster) {
                    currentSpieler.geschützt = true;
                    ((Schattenpriester) hauptrolleSpieler).neuster = false;
                }
            }
        }

        if (Torte.torte) {
            for (Spieler currentSpieler : Torte.tortenEsser) {
                if (Torte.gut) {
                    currentSpieler.geschützt = true;
                } else {
                    currentSpieler.aktiv = false;
                }
            }
        }

        Torte.torte = false;

        GrafVladimir.unerkennbarerSpieler = null;
    }

    public void setSchütze() {
        for (Spieler currentSpieler : game.spieler) {
            String nebenrolleCurrentSpieler = currentSpieler.nebenrolle.getName();

            if (nebenrolleCurrentSpieler.equals(SchwarzeSeele.name)) {
                currentSpieler.geschützt = true;
            }
        }
    }

    public void setOpfer() {
        checkLiebespaar();
        killVictims();
    }

    public void setPlayersAwake(Statement statement) {
        playersAwake.clear();
        if (statement.getClass() == StatementFraktion.class) {
            StatementFraktion statementFraktion = (StatementFraktion) statement;
            playersAwake.addAll(Fraktion.getFraktionsMembers(statementFraktion.fraktion));
        } else if (statement.getClass() == StatementRolle.class) {
            StatementRolle statementRolle = (StatementRolle) statement;
            playersAwake.add(game.findSpielerPerRolle(statementRolle.rolle));
        }
    }

    private void cleanUp() {
        for (Spieler currentSpieler : game.spieler) {
            currentSpieler.aktiv = true;
            currentSpieler.geschützt = false;
            currentSpieler.ressurectable = true;
        }
    }

    public void killVictims() {
        for (Opfer currentVictim : Opfer.deadVictims) {
            if (Rolle.rolleLebend(Blutwolf.name)) {
                if (currentVictim.fraktionsTäter && currentVictim.täter.hauptrolle.getFraktion().getName().equals(Werwölfe.name)) {
                    Blutwolf.deadStacks++;
                    if (Blutwolf.deadStacks >= 2) {
                        Blutwolf.deadly = true;
                    }
                }
            }

            game.killSpieler(currentVictim.opfer);
        }
    }

    public void checkLiebespaar() {
        boolean spieler1Lebend = true;
        boolean spieler2Lebend = true;

        Liebespaar liebespaar = game.liebespaar;

        if (liebespaar != null && liebespaar.spieler1 != null && liebespaar.spieler2 != null) {

            for (Opfer currentVictim : Opfer.deadVictims) {
                if (currentVictim.opfer.name.equals(liebespaar.spieler1.name)) {
                    spieler1Lebend = false;
                }
                if (currentVictim.opfer.name.equals(liebespaar.spieler2.name)) {
                    spieler2Lebend = false;
                }
            }

            if (spieler1Lebend && !spieler2Lebend) {
                Opfer.deadVictims.add(new Opfer(liebespaar.spieler1, liebespaar.spieler2, false));
            }

            if (!spieler1Lebend && spieler2Lebend) {
                Opfer.deadVictims.add(new Opfer(liebespaar.spieler2, liebespaar.spieler1, false));
            }
        }
    }

    public void refreshHexenSchutz() {
        /*if (Rolle.rolleLebend(GuteHexe.name)) {
            GuteHexe guteHexe = (GuteHexe) game.findSpielerPerRolle(GuteHexe.name).hauptrolle;
            if (guteHexe.besucht != null) {
                String hexenSchutzSpieler = guteHexe.besucht.name;
                boolean refreshed = false;

                for (Opfer opfer : Opfer.possibleVictims) {
                    if (opfer.opfer.name.equals(hexenSchutzSpieler)) {
                        guteHexe.abilityCharges++;
                        refreshed = true;
                        break;
                    }
                }

                if (!refreshed) {
                    for (Opfer opfer : Opfer.deadVictims) {
                        if (opfer.opfer.name.equals(hexenSchutzSpieler)) {
                            guteHexe.abilityCharges++;
                            break;
                        }
                    }
                }
            }
        }*/
    }

    public boolean gibtEsTorte() {
        if (Rolle.rolleLebend(Konditor.name) && !Opfer.isOpferPerRolle(Konditor.name) && Rolle.rolleAktiv(Konditor.name)) {
            return true;
        }

        if (Rolle.rolleLebend(Konditorlehrling.name) && !Opfer.isOpferPerRolle(Konditorlehrling.name) && Rolle.rolleAktiv(Konditorlehrling.name)) {
            return true;
        }

        if (Sammler.isSammlerRolle(Konditor.name) || Sammler.isSammlerRolle(Konditorlehrling.name)) {
            if (Rolle.rolleLebend(Sammler.name) && !Opfer.isOpferPerRolle(Sammler.name) && Rolle.rolleAktiv(Sammler.name)) {
                return true;
            }
        }

        return false;
    }

    public void checkVictory() {
        String victory = game.checkVictory();

        if (victory != null) {
            showEndScreenPage(victory);
        }
    }

    public String showFrontendControl(Statement statement, FrontendControl frontendControl) {
        if (frontendControl.title == null) {
            frontendControl.title = statement.title;
        }

        switch (statement.getState()) {
            case NORMAL:
                switch (frontendControl.typeOfContent) {
                    case TITLE:
                        showTitle(statement, frontendControl.title);
                        break;

                    case DROPDOWN:
                        showDropdown(statement, frontendControl.title, frontendControl.strings);
                        return FrontendControl.erzählerFrame.chosenOption1;

                    case DROPDOWN_LIST:
                        showDropdownList(statement, frontendControl.title, frontendControl.strings);
                        return FrontendControl.erzählerFrame.chosenOption1;

                    case LIST:
                        showList(statement, frontendControl.title, frontendControl.strings);
                        break;

                    case IMAGE:
                        showImage(statement, frontendControl.title, frontendControl.imagePath);
                        break;

                    case CARD:
                        showCard(statement, frontendControl.title, frontendControl.imagePath);
                        break;

                    case LIST_IMAGE:
                        showListShowImage(statement, frontendControl.title, frontendControl.strings, frontendControl.imagePath);
                }
                break;

            case DEAKTIV:
                showDeaktivPages(statement, frontendControl);
                break;

            case DEAD:
                showTotPages(statement, frontendControl);
                break;

            case NOT_IN_GAME:
                showAusDemSpielPages(statement, frontendControl);
                break;
        }

        return null;
    }

    public void showDropdownPage(Statement statement, ArrayList<String> dropdownOptions1, ArrayList<String> dropdownOptions2) {
        switch (statement.getState()) {
            case NORMAL:
                FrontendControl.erzählerDropdownPage(statement, dropdownOptions1, dropdownOptions2);
                FrontendControl.spielerDropdownPage(statement.title, 2);
                break;

            case DEAKTIV:
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), getEmptyStringList(), ImagePath.DEAKTIVIERT);
                FrontendControl.spielerIconPicturePage(DEAKTIVIERT_TITLE, ImagePath.DEAKTIVIERT);
                break;

            case DEAD:
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), getEmptyStringList(), ImagePath.TOT);
                FrontendControl.spielerIconPicturePage(TOT_TITLE, ImagePath.TOT);
                break;

            case NOT_IN_GAME:
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), getEmptyStringList(), ImagePath.AUS_DEM_SPIEL);
                FrontendControl.spielerDropdownPage(statement.title, 2);
                break;
        }

        waitForAnswer();
    }

    public String showAfterDeathDropdownListPage(Statement statement, ArrayList<String> dropdownOptions) {
        if (statement.isLebend()) {
            if (statement.isAktiv()) {
                FrontendControl.erzählerDropdownPage(statement, dropdownOptions);
                FrontendControl.spielerDropdownListPage(statement.title, dropdownOptions);
            } else {
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ImagePath.DEAKTIVIERT);
                FrontendControl.spielerIconPicturePage(DEAKTIVIERT_TITLE, ImagePath.DEAKTIVIERT);
            }
        } else {
            FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ImagePath.AUS_DEM_SPIEL);
            FrontendControl.spielerDropdownPage(statement.title, 1);
        }

        waitForAnswer();

        return FrontendControl.erzählerFrame.chosenOption1;
    }

    public String showKonditorDropdownPage(Statement statement, FrontendControl frontendControl) {
        /*if (Rolle.rolleLebend(Konditor.name) || Rolle.rolleLebend(Konditorlehrling.name)) {
            if (!Opfer.isOpferPerRolle(Konditor.name) || !Opfer.isOpferPerRolle(Konditorlehrling.name)) {
                if (Rolle.rolleAktiv(Konditor.name) || Rolle.rolleAktiv(Konditorlehrling.name)) {*/
        FrontendControl.erzählerDropdownPage(statement, frontendControl.strings);
        FrontendControl.spielerDropdownPage(statement.title, 1);
                /*} else {
                    FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ResourcePath.DEAKTIVIERT);
                    FrontendControl.spielerIconPicturePage(DEAKTIVIERT_TITLE, ResourcePath.DEAKTIVIERT);
                }
            } else {
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ResourcePath.TOT);
                FrontendControl.spielerIconPicturePage(TOT_TITLE, ResourcePath.TOT);
            }
        } else {
            FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ResourcePath.AUS_DEM_SPIEL);
            FrontendControl.spielerDropdownPage(statement.title, 1);
        }*/

        waitForAnswer();

        return FrontendControl.erzählerFrame.chosenOption1;
    }

    public void showEndScreenPage(String victory) {
        FrontendControl.erzählerEndScreenPage(victory);
        FrontendControl.spielerEndScreenPage(victory);

        waitForAnswer();
    }

    public void showAufgebrauchtPages(Statement statement) {
        FrontendControl.erzählerIconPicturePage(statement, ImagePath.AUFGEBRAUCHT);
        FrontendControl.spielerIconPicturePage(AUFGEBRAUCHT_TITLE, ImagePath.AUFGEBRAUCHT);

        waitForAnswer();
    }

    //TODO Cases die sowieso gleich aussehen zusammenfassen
    public void showDeaktivPages(Statement statement, FrontendControl frontendControl) {
        String erzählerDeaktiviertIconPath = ImagePath.DEAKTIVIERT;

        switch (frontendControl.typeOfContent) {
            case DROPDOWN:
            case DROPDOWN_LIST:
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), erzählerDeaktiviertIconPath);
                FrontendControl.spielerIconPicturePage(DEAKTIVIERT_TITLE, ImagePath.DEAKTIVIERT);

                waitForAnswer();
                break;

            case LIST:
            case LIST_IMAGE:
                FrontendControl.erzählerListPage(statement, getEmptyStringList(), erzählerDeaktiviertIconPath);
                FrontendControl.spielerIconPicturePage(DEAKTIVIERT_TITLE, ImagePath.DEAKTIVIERT);

                waitForAnswer();
                break;

            case TITLE:
            case IMAGE:
            case CARD:
                FrontendControl.erzählerIconPicturePage(statement, erzählerDeaktiviertIconPath);
                FrontendControl.spielerIconPicturePage(DEAKTIVIERT_TITLE, ImagePath.DEAKTIVIERT);

                waitForAnswer();
                break;
        }
    }

    public void showTotPages(Statement statement, FrontendControl frontendControl) {
        switch (frontendControl.typeOfContent) {
            case DROPDOWN:
            case DROPDOWN_LIST:
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ImagePath.TOT);
                FrontendControl.spielerIconPicturePage(TOT_TITLE, ImagePath.TOT);

                waitForAnswer();
                break;

            case LIST:
            case LIST_IMAGE:
                FrontendControl.erzählerListPage(statement, getEmptyStringList(), ImagePath.TOT);
                FrontendControl.spielerIconPicturePage(TOT_TITLE, ImagePath.TOT);

                waitForAnswer();
                break;

            case TITLE:
            case IMAGE:
            case CARD:
                FrontendControl.erzählerIconPicturePage(statement, ImagePath.TOT);
                FrontendControl.spielerIconPicturePage(TOT_TITLE, ImagePath.TOT);

                waitForAnswer();
                break;
        }
    }

    public void showAusDemSpielPages(Statement statement, FrontendControl frontendControl) {
        switch (frontendControl.typeOfContent) {
            case DROPDOWN:
            case DROPDOWN_LIST:
                FrontendControl.erzählerDropdownPage(statement, getEmptyStringList(), ImagePath.AUS_DEM_SPIEL);
                FrontendControl.spielerDropdownPage(statement.title, 1);

                waitForAnswer();
                break;

            case LIST:
            case LIST_IMAGE:
                FrontendControl.erzählerListPage(statement, getEmptyStringList(), ImagePath.AUS_DEM_SPIEL);
                FrontendControl.spielerListPage(statement.title, getEmptyStringList());

                waitForAnswer();
                break;

            case TITLE:
            case IMAGE:
            case CARD:
                FrontendControl.erzählerIconPicturePage(statement, ImagePath.AUS_DEM_SPIEL);
                FrontendControl.spielerIconPicturePage(statement.title, "");

                waitForAnswer();
                break;
        }
    }

    public void showTitle(Statement statement) {
        showTitle(statement, statement.title);
    }

    public void showTitle(Statement statement, String title) {
        FrontendControl.erzählerDefaultNightPage(statement);
        FrontendControl.spielerTitlePage(title);

        waitForAnswer();
    }

    public void showDropdown(Statement statement, String title, ArrayList<String> dropdownOptions) {
        FrontendControl.erzählerDropdownPage(statement, dropdownOptions);
        FrontendControl.spielerDropdownPage(title, 1);

        waitForAnswer();
    }

    public void showDropdownList(Statement statement, String title, ArrayList<String> strings) {
        FrontendControl.erzählerDropdownPage(statement, strings);
        FrontendControl.spielerDropdownListPage(title, strings);

        waitForAnswer();
    }

    public void showList(Statement statement, String string) {
        ArrayList<String> list = new ArrayList<>();
        list.add(string);
        showList(statement, list);
    }

    public void showList(Statement statement, ArrayList<String> strings) {
        showList(statement, statement.title, strings);
    }

    public void showList(Statement statement, String title, ArrayList<String> strings) {
        FrontendControl.erzählerListPage(statement, title, strings);
        FrontendControl.spielerListPage(title, strings);

        waitForAnswer();
    }

    public void showImage(Statement statement, String imagePath) {
        showImage(statement, statement.title, imagePath);
    }

    public void showImage(Statement statement, String title, String imagePath) {
        FrontendControl.erzählerIconPicturePage(statement, title, imagePath);
        FrontendControl.spielerIconPicturePage(title, imagePath);

        waitForAnswer();
    }

    public void showCard(Statement statement, String title, String imagePath) {
        FrontendControl.erzählerCardPicturePage(statement, title, imagePath);
        FrontendControl.spielerCardPicturePage(title, imagePath);

        waitForAnswer();
    }

    public void showListShowImage(Statement statement, String string, String spielerImagePath) {
        showListShowImage(statement, string, spielerImagePath, "");
    }

    public void showListShowImage(Statement statement, String string, String spielerImagePath, String erzählerImagePath) {
        ArrayList<String> list = new ArrayList<>();
        list.add(string);
        showListShowImage(statement, statement.title, list, spielerImagePath, erzählerImagePath);
    }

    public void showListShowImage(Statement statement, String title, ArrayList<String> strings, String spielerImagePath) {
        showListShowImage(statement, title, strings, spielerImagePath, "");
    }

    public void showListShowImage(Statement statement, String title, ArrayList<String> strings, String spielerImagePath, String erzählerImagePath) {
        FrontendControl.erzählerListPage(statement, strings, erzählerImagePath);
        FrontendControl.spielerIconPicturePage(title, spielerImagePath);

        waitForAnswer();
    }

    public static ArrayList<String> getEmptyStringList() {
        ArrayList<String> emptyContent = new ArrayList<>();
        emptyContent.add("");
        return emptyContent;
    }

    public void waitForAnswer() {
        FrontendControl.refreshÜbersichtsFrame();
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
