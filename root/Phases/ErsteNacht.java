package root.Phases;

import root.Frontend.Frame.ErzählerFrame;
import root.Frontend.Frame.SpielerFrame;
import root.Frontend.FrontendControl;
import root.Frontend.Page.Page;
import root.ResourceManagement.ResourcePath;
import root.Rollen.Fraktion;
import root.Rollen.Fraktionen.Schattenpriester_Fraktion;
import root.Rollen.Fraktionen.Vampire;
import root.Rollen.Fraktionen.Werwölfe;
import root.Rollen.Hauptrolle;
import root.Rollen.Hauptrollen.Bürger.Bruder;
import root.Rollen.Hauptrollen.Bürger.Seherin;
import root.Rollen.Hauptrollen.Werwölfe.Alphawolf;
import root.Rollen.Nebenrolle;
import root.Rollen.Nebenrollen.*;
import root.Rollen.Rolle;
import root.Spieler;
import root.mechanics.Liebespaar;

import java.util.ArrayList;
import java.util.Objects;

public class ErsteNacht extends Thread {
    public static final String ALLE_SCHLAFEN_EIN = "Alle schlafen ein";
    public static final String ALLE_WACHEN_AUF = "Alle wachen auf";

    public static final String LIEBESPAAR = "Der Erzähler wählt ein Liebespaar aus";
    public static final String LIEBESPAAR_FINDEN = "Das Liebespaar erwacht, findet und verliebt sich";
    public static final String REINES_LICHT = "Reines Licht erwacht und tauscht seine Karte je nach Hauptrolle aus";
    public static final String LAMM = "Lamm erwacht und tauscht ggf. seine Karte aus";
    public static final String VAMPIRUMHANG = "Träger des Vampirumhangs erwacht und tauscht ggf. seine Karte aus";
    public static final String WOLFSPELZ = "Träger des Wolfspelzes erwacht und tauscht ggf. seine Karte aus";
    public static final String IMITATOR = "Imitator erwacht und entscheidet welche Rolle er imitieren möchte";
    public static final String VAMPIRE = "Die Vampire erwachen und sehen einander";
    public static final String WERWÖLFE = "Die Werwölfe erwachen und sehen einander";
    public static final String ALPHAWOLF = "Alpha Wolf erwacht und erfährt die Rollen der Wolfsfraktion";
    public static final String SCHATTENPRIESTER = "Die Schattenpriester erwachen und sehen einander";
    public static final String BRÜDER = "Brüder erwachen und sehen einander";
    public static final String SEHERIN = "Seherin erwacht und lässt sich Auskunft über einen Mitspieler geben";

    public static final String ALLE_SCHLAFEN_EIN_TITLE = "Alle schlafen ein";
    public static final String ALLE_WACHEN_AUF_TITLE = "Alle wachen auf";

    public static final String LIEBESPAAR_TITLE = "Liebespaar wählen";
    public static final String LIEBESPAAR_FINDEN_TITLE = "Liebespaar";
    public static final String REINES_LICHT_TITLE = "neue Karte";
    public static final String LAMM_TITLE = "neue Karte";
    public static final String VAMPIRUMHANG_TITLE = "neue Karte";
    public static final String WOLFSPELZ_TITLE = "neue Karte";
    public static final String IMITATOR_TITLE = "Imitieren";
    public static final String VAMPIRE_TITLE = "Vampire";
    public static final String WERWÖLFE_TITLE = "Werwölfe";
    public static final String ALPHAWOLF_TITLE = "Werwölfe";
    public static final String SCHATTENPRIESTER_TITLE = "Schattenpriester";
    public static final String BRÜDER_TITLE = "Brüder";
    public static final String SEHERIN_TITLE = "Spieler wählen";

    public static final String TARNUMHANG_TITLE = "Tarnumhang";

    ErzählerFrame erzählerFrame;
    public static SpielerFrame spielerFrame;
    public static ArrayList<Statement> statements;
    public static Object lock;

    public ErsteNacht(ErzählerFrame erzählerFrame, SpielerFrame spielerFrame) {
        this.erzählerFrame = erzählerFrame;
        this.spielerFrame = spielerFrame;
    }

    public void run() {
        lock = new Object();
        synchronized (lock) {
            Page nightPage;
            FrontendControl dropdownOtions;
            String feedback = null;

            Spieler chosenSpieler;
            Nebenrolle newNebenrolle;
            String cardToDisplay;
            String imagePath;

            ArrayList<String> spieler = Spieler.getLivigPlayerStrings();
            ArrayList<String> secondaryRolesLeft = getSecondaryRolesLeft();

            beginNight();

            ersteNachtBuildStatements();

            for (Statement statement : statements) {
                Rolle rolle = null;
                if (statement.getClass() == StatementRolle.class) {
                    rolle = ((StatementRolle) statement).getRolle();
                }

                if (rolle != null && rolle instanceof Nebenrolle && ((Nebenrolle) rolle).getType().equals(Nebenrolle.PASSIV)) {
                    Nebenrolle nebenrolle = ((Nebenrolle) rolle);
                    newNebenrolle = nebenrolle.getTauschErgebnis();
                    cardToDisplay = newNebenrolle.getImagePath();
                    displayCard(statement, cardToDisplay);
                    if(Rolle.rolleLebend(rolle.getName())) {
                        nebenrolle.tauschen(newNebenrolle);
                        Nebenrolle.secondaryRolesInGame.remove(nebenrolle);
                    }

                } else if(statement.getClass() == StatementFraktion.class){
                    Fraktion fraktion = Fraktion.findFraktion(((StatementFraktion) statement).fraktion);
                    showFraktionMembers(statement, fraktion.getName());
                }
                else{
                    switch (statement.beschreibung) {
                        case LIEBESPAAR:
                            ArrayList<String> spielerOrZufällig = Spieler.getLivigPlayerStrings();
                            spielerOrZufällig.add(Liebespaar.ZUFÄLLIG);
                            nightPage = erzählerFrame.pageFactory.generateDropdownPage(statement, spielerOrZufällig, spielerOrZufällig);
                            erzählerFrame.buildScreenFromPage(nightPage);
                            spielerFrame.dropDownPage = spielerFrame.pageFactory.generateDropdownPage(statement.title, 2);
                            spielerFrame.buildScreenFromPage(spielerFrame.dropDownPage);

                            waitForAnswer();

                            Liebespaar.neuesLiebespaar(erzählerFrame.chosenOption1, erzählerFrame.chosenOption2);
                            break;

                        case LIEBESPAAR_FINDEN:
                            if(Liebespaar.spieler1!=null) {
                                ArrayList<String> liebespaarStrings = new ArrayList<>();

                                liebespaarStrings.add(Liebespaar.spieler1.name);
                                liebespaarStrings.add(Liebespaar.spieler2.name);

                                imagePath = Liebespaar.getImagePath();

                                nightPage = erzählerFrame.pageFactory.generateListPage(statement, liebespaarStrings);
                                erzählerFrame.buildScreenFromPage(nightPage);
                                nightPage = spielerFrame.pageFactory.generateStaticImagePage(statement.title, imagePath, true);
                                spielerFrame.buildScreenFromPage(nightPage);

                                waitForAnswer();
                            }
                            break;

                        case ALPHAWOLF:
                            ArrayList<Spieler> werwölfe = Fraktion.findFraktion(Werwölfe.name).getFraktionsMembers();
                            werwölfe.remove(Spieler.findSpielerPerRolle(Alphawolf.name));
                            for (Spieler currentSpieler : werwölfe) {
                                showHauptrolle(statement, currentSpieler);
                            }
                            break;

                        case BRÜDER:
                            ArrayList<String> brüder = Spieler.findSpielersPerRolle(Bruder.name);
                            nightPage = erzählerFrame.pageFactory.generateListPage(statement, brüder);
                            erzählerFrame.buildScreenFromPage(nightPage);
                            nightPage = spielerFrame.pageFactory.generateStaticImagePage(statement.title, ResourcePath.BRÜDER_KARTE);
                            spielerFrame.buildScreenFromPage(nightPage);

                            waitForAnswer();
                            break;

                        case SEHERIN:
                            dropdownOtions = rolle.getDropdownOtions();
                            showDropdownPage(statement, dropdownOtions);
                            feedback = rolle.aktion(erzählerFrame.chosenOption1);

                            Fraktion fraktion = Fraktion.findFraktion(feedback);
                            if (fraktion != null || feedback != null && feedback.equals(Nebenrolle.TARNUMHANG)) {
                                if (feedback.equals(Nebenrolle.TARNUMHANG)) {
                                    imagePath = ResourcePath.TARNUMHANG;
                                    statement.title = TARNUMHANG_TITLE;
                                } else {
                                    imagePath = fraktion.getImagePath();
                                    statement.title = erzählerFrame.chosenOption1;
                                }

                                showImageOnBothScreens(statement, imagePath);

                                statement.title = SEHERIN_TITLE;
                            }
                            break;

                        default:
                            nightPage = erzählerFrame.pageFactory.generateDefaultNightPage(statement);
                            erzählerFrame.buildScreenFromPage(nightPage);
                            nightPage = spielerFrame.pageFactory.generateTitlePage(statement.title);
                            spielerFrame.buildScreenFromPage(nightPage);

                            waitForAnswer();
                            break;
                    }
                }
            }
        }

        cleanUp();
        PhaseManager.day(erzählerFrame, spielerFrame);
    }

    public void beginNight() {
        for (Hauptrolle currentHauptrolle : Hauptrolle.mainRoles) {
            currentHauptrolle.besuchtLetzteNacht = null;
            currentHauptrolle.besucht = null;
        }
        for (Nebenrolle currentNebenrolle : Nebenrolle.secondaryRoles) {
            currentNebenrolle.besuchtLetzteNacht = null;
            currentNebenrolle.besucht = null;
        }
    }

    public void showFraktionMembers(Statement statement, String fraktionName) {
        Page nightPage;

        Fraktion fraktion = Fraktion.findFraktion(fraktionName);
        ArrayList<String> fraktionMembers = fraktion.getFraktionsMemberStrings();

        nightPage = erzählerFrame.pageFactory.generateListPage(statement, fraktionMembers);
        erzählerFrame.buildScreenFromPage(nightPage);

        try {
            String fraktionsLogoImagePath = fraktion.getImagePath();

            nightPage = spielerFrame.pageFactory.generateStaticImagePage(statement.title, fraktionsLogoImagePath, true);
            spielerFrame.buildScreenFromPage(nightPage);
        } catch (NullPointerException e) {
            System.out.println(fraktionName + " nicht gefunden");
        }

        waitForAnswer();
    }

    private ArrayList<String> getSecondaryRolesLeft() {
        ArrayList<String> freieNebenrollen = new ArrayList<>();

        for (Nebenrolle currentNebenrolle: Nebenrolle.secondaryRolesInGame) {
            String nebenrolle = currentNebenrolle.getName();

            boolean frei = true;
            for (Spieler currentSpieler: Spieler.spieler) {
                String nebenrolleSpieler = currentSpieler.nebenrolle.getName();

                if (Objects.equals(nebenrolle, nebenrolleSpieler)) {
                    frei = false;
                }
            }

            if(frei) {
                freieNebenrollen.add(nebenrolle);
            }
        }

        return freieNebenrollen;
    }

    private void cleanUp() {
        for (Spieler currentSpieler : Spieler.spieler) {
            currentSpieler.aktiv = true;
            currentSpieler.geschützt = false;
            currentSpieler.ressurectable = true;

            currentSpieler.hauptrolle.aktiv = true;
        }
    }

    public void showNebenrolle(Statement statement, Spieler spieler) {
        if (spieler != null) {
            statement.title = spieler.name;

            String imagePath = spieler.nebenrolle.getImagePath();
            if(spieler.nebenrolle.getName().equals(Tarnumhang.name)) {
                imagePath = ResourcePath.TARNUMHANG;
                statement.title = TARNUMHANG_TITLE;
            }
            displayCard(statement, imagePath);
        }
    }

    public void showHauptrolle(Statement statement, Spieler spieler) {
        if (spieler != null) {
            statement.title = spieler.name;

            String imagePath = spieler.hauptrolle.getImagePath();
            displayCard(statement, imagePath);
        }
    }

    public void displayCard(Statement statement, String imagePath) {
        Page nightPage = erzählerFrame.pageFactory.generateCardPicturePage(statement, imagePath);
        erzählerFrame.buildScreenFromPage(nightPage);
        nightPage = spielerFrame.pageFactory.generateStaticImagePage(statement.title, imagePath);
        spielerFrame.buildScreenFromPage(nightPage);

        waitForAnswer();
    }

    public Spieler choosePlayerOrNon(Statement statement) {
        ArrayList<String> spielerOrNon = Spieler.getLivigPlayerOrNoneStrings();

        showDropdownPage(statement, spielerOrNon);

        return Spieler.findSpieler(erzählerFrame.chosenOption1);
    }

    public void showDropdownPage(Statement statement, ArrayList<String> strings) {
        Page nightPage = erzählerFrame.pageFactory.generateDropdownPage(statement, strings);
        erzählerFrame.buildScreenFromPage(nightPage);
        spielerFrame.dropDownPage = spielerFrame.pageFactory.generateDropdownPage(statement.title, 1);
        spielerFrame.buildScreenFromPage(spielerFrame.dropDownPage);

        waitForAnswer();
    }

    public void showDropdownPage(Statement statement, FrontendControl frontendControl) {
        Page nightPage = erzählerFrame.pageFactory.generateDropdownPage(statement, frontendControl.content);
        erzählerFrame.buildScreenFromPage(nightPage);

        switch (frontendControl.typeOfContent)
        {
            case FrontendControl.DROPDOWN_WITHOUT_SUGGESTIONS:
                spielerFrame.dropDownPage = spielerFrame.pageFactory.generateDropdownPage(statement.title, 1);
                spielerFrame.buildScreenFromPage(spielerFrame.dropDownPage);
                break;

            case FrontendControl.DROPDOWN_WITH_SUGGESTIONS:
                spielerFrame.dropDownPage = spielerFrame.pageFactory.generateListMirrorPage(statement.title, frontendControl.content);
                spielerFrame.buildScreenFromPage(spielerFrame.dropDownPage);
                break;
        }

        waitForAnswer();
    }

    public void showImageOnBothScreens(Statement statement, String imagePath) {
        Page nightPage = erzählerFrame.pageFactory.generateIconPicturePage(statement, imagePath);
        erzählerFrame.buildScreenFromPage(nightPage);
        nightPage = spielerFrame.pageFactory.generateStaticImagePage(statement.title, imagePath, true);
        spielerFrame.buildScreenFromPage(nightPage);

        waitForAnswer();
    }

    public void waitForAnswer() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ersteNachtBuildStatements() {
        statements = new ArrayList<>();

        addStatement(ALLE_SCHLAFEN_EIN, ALLE_SCHLAFEN_EIN_TITLE);

        addStatement(LIEBESPAAR, LIEBESPAAR_TITLE);
        addStatement(LIEBESPAAR_FINDEN, LIEBESPAAR_FINDEN_TITLE);

        addStatementRolle(REINES_LICHT, REINES_LICHT_TITLE, ReinesLicht.name);
        addStatementRolle(LAMM, LAMM_TITLE, Lamm.name);
        addStatementRolle(VAMPIRUMHANG, VAMPIRUMHANG_TITLE, Vampirumhang.name);
        addStatementRolle(WOLFSPELZ, WOLFSPELZ_TITLE, Wolfspelz.name);

        //addStatementRolle(IMITATOR, IMITATOR_TITLE, Imitator.name);

        addStatementFraktion(VAMPIRE, VAMPIRE_TITLE, Vampire.name);
        addStatementFraktion(WERWÖLFE, WERWÖLFE_TITLE, Werwölfe.name);
        addStatementRolle(ALPHAWOLF, ALPHAWOLF_TITLE, Alphawolf.name);
        addStatementFraktion(SCHATTENPRIESTER, SCHATTENPRIESTER_TITLE, Schattenpriester_Fraktion.name);
        addStatementRolle(BRÜDER, BRÜDER_TITLE, Bruder.name);

        addStatementRolle(SEHERIN, SEHERIN_TITLE, Seherin.name);

        addStatement(ALLE_WACHEN_AUF, ALLE_WACHEN_AUF_TITLE);
    }

    public void addStatement(String statement, String title) {
        statements.add(new StatementIndie(statement, title, Statement.INDIE, true));
    }

    //TODO Statement Type implementieren

    public void addStatementRolle(String statement, String title, String rolle) {
        if (Rolle.rolleInNachtEnthalten(rolle)) {
            statements.add(new StatementRolle(statement, title, rolle, Statement.INDIE, true));
        }
    }

    public void addStatementFraktion(String statement, String title, String fraktionsName) {
        Fraktion fraktion = Fraktion.findFraktion(fraktionsName);

        if (Fraktion.fraktionInNachtEnthalten(fraktionsName)) {
            if(fraktion.getNumberOfFraktionsMembersInGame()>1) {
                statements.add(new StatementFraktion(statement, title, fraktionsName, Statement.INDIE, true));
            }
        }
    }
}
