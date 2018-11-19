package root.mechanics;

import root.Frontend.Frame.ErzählerFrame;
import root.Frontend.Frame.ÜbersichtsFrame;
import root.Frontend.FrontendControl;
import root.Frontend.Utils.DropdownOptions;
import root.Persona.Bonusrolle;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Bürger;
import root.Persona.Fraktionen.Werwölfe;
import root.Persona.Hauptrolle;
import root.Persona.Rolle;
import root.Persona.Rollen.Bonusrollen.Analytiker;
import root.Persona.Rollen.Bonusrollen.Archivar;
import root.Persona.Rollen.Bonusrollen.Dieb;
import root.Persona.Rollen.Bonusrollen.Gefängniswärter;
import root.Persona.Rollen.Bonusrollen.Konditor;
import root.Persona.Rollen.Bonusrollen.Konditorlehrling;
import root.Persona.Rollen.Bonusrollen.Lamm;
import root.Persona.Rollen.Bonusrollen.Medium;
import root.Persona.Rollen.Bonusrollen.Nachbar;
import root.Persona.Rollen.Bonusrollen.Prostituierte;
import root.Persona.Rollen.Bonusrollen.ReineSeele;
import root.Persona.Rollen.Bonusrollen.Schatten;
import root.Persona.Rollen.Bonusrollen.Schattenkutte;
import root.Persona.Rollen.Bonusrollen.Schnüffler;
import root.Persona.Rollen.Bonusrollen.SchwarzeSeele;
import root.Persona.Rollen.Bonusrollen.Seelenlicht;
import root.Persona.Rollen.Bonusrollen.Spurenleser;
import root.Persona.Rollen.Bonusrollen.Tarnumhang;
import root.Persona.Rollen.Bonusrollen.Totengräber;
import root.Persona.Rollen.Bonusrollen.Vampirumhang;
import root.Persona.Rollen.Bonusrollen.Wahrsager;
import root.Persona.Rollen.Bonusrollen.Wolfspelz;
import root.Persona.Rollen.Constants.DropdownConstants;
import root.Persona.Rollen.Constants.WölfinState;
import root.Persona.Rollen.Hauptrollen.Bürger.Dorfbewohner;
import root.Persona.Rollen.Hauptrollen.Bürger.HoldeMaid;
import root.Persona.Rollen.Hauptrollen.Bürger.Irrlicht;
import root.Persona.Rollen.Hauptrollen.Bürger.Orakel;
import root.Persona.Rollen.Hauptrollen.Bürger.Riese;
import root.Persona.Rollen.Hauptrollen.Bürger.Sammler;
import root.Persona.Rollen.Hauptrollen.Bürger.Schamanin;
import root.Persona.Rollen.Hauptrollen.Bürger.Schattenmensch;
import root.Persona.Rollen.Hauptrollen.Bürger.Seherin;
import root.Persona.Rollen.Hauptrollen.Bürger.Späher;
import root.Persona.Rollen.Hauptrollen.Bürger.Wirt;
import root.Persona.Rollen.Hauptrollen.Schattenpriester.Schattenpriester;
import root.Persona.Rollen.Hauptrollen.Vampire.GrafVladimir;
import root.Persona.Rollen.Hauptrollen.Vampire.LadyAleera;
import root.Persona.Rollen.Hauptrollen.Vampire.MissVerona;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Blutwolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Chemiker;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Geisterwolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Schreckenswolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Werwolf;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Wolfsmensch;
import root.Persona.Rollen.Hauptrollen.Werwölfe.Wölfin;
import root.Persona.Rollen.Hauptrollen.Überläufer.Henker;
import root.Persona.Rollen.Hauptrollen.Überläufer.Überläufer;
import root.Phases.Day;
import root.Phases.FirstNight;
import root.Phases.PhaseManager;
import root.Phases.PhaseMode;
import root.Phases.Winner;
import root.Spieler;
import root.Utils.ListHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static Game game;

    public Day day;

    public boolean freibier = false;

    public Liebespaar liebespaar;

    public List<Spieler> spieler = new ArrayList<>();
    public List<Hauptrolle> hauptrollen = new ArrayList<>();
    public List<Hauptrolle> hauptrollenInGame = new ArrayList<>();
    public List<Bonusrolle> bonusrollen = new ArrayList<>();
    public List<Bonusrolle> bonusrollenInGame = new ArrayList<>();
    public List<Hauptrolle> mitteHauptrollen = new ArrayList<>();
    public List<Bonusrolle> mitteBonusrollen = new ArrayList<>();
    public List<Hauptrolle> stillAvailableHauptrollen = new ArrayList<>();
    public List<Bonusrolle> stillAvailableBonusrollen = new ArrayList<>();
    public List<Spieler> spielerSpecified = new ArrayList<>();

    public boolean secondNight = true;
    private boolean started = false;

    public Game() {
        Game.game = this;

        PhaseManager.phaseMode = PhaseMode.SETUP;

        spieler = new ArrayList<>();
        hauptrollenInGame = new ArrayList<>();
        generateAllAvailableHauptrollen();
        bonusrollenInGame = new ArrayList<>();
        generateAllAvailableBonusrollen();
        spielerSpecified = new ArrayList<>();

        liebespaar = new Liebespaar();
        Torte.tortenEsser = new ArrayList<>();
    }

    public void startGame(ErzählerFrame erzählerFrame) {
        erzählerFrame.übersichtsFrame = new ÜbersichtsFrame(erzählerFrame.frameJpanel.getHeight() + 50);
        erzählerFrame.toFront();

        FrontendControl.erzählerFrame = erzählerFrame;
        FrontendControl.spielerFrame = erzählerFrame.spielerFrame;
        FrontendControl.übersichtsFrame = erzählerFrame.übersichtsFrame;

        PhaseManager phaseManager = new PhaseManager();
        phaseManager.start();
        generateStillAvailableRollen();
    }

    private void generateStillAvailableRollen() {
        generateStillAvailableHauptrollen();
        generateStillAvailableBonusrollen();
    }

    private void generateStillAvailableHauptrollen() {
        stillAvailableHauptrollen = new ArrayList<>();

        stillAvailableHauptrollen.addAll(hauptrollenInGame);

        for (Spieler spieler : spieler) {
            stillAvailableHauptrollen.remove(spieler.hauptrolle);
        }
    }

    private void generateStillAvailableBonusrollen() {
        stillAvailableBonusrollen = new ArrayList<>();

        stillAvailableBonusrollen.addAll(bonusrollenInGame);

        for (Spieler spieler : spieler) {
            stillAvailableBonusrollen.remove(spieler.bonusrolle);
        }
    }

    private void generateAllAvailableHauptrollen() {
        hauptrollen.add(new Dorfbewohner());
        hauptrollen.add(new HoldeMaid());
        hauptrollen.add(new Irrlicht());
        hauptrollen.add(new Orakel());
        hauptrollen.add(new Riese());
        hauptrollen.add(new Sammler());
        hauptrollen.add(new Schamanin());
        hauptrollen.add(new Schattenmensch());
        hauptrollen.add(new Seherin());
        hauptrollen.add(new Späher());
        hauptrollen.add(new Wirt());
        hauptrollen.add(new Schattenpriester());
        hauptrollen.add(new GrafVladimir());
        hauptrollen.add(new LadyAleera());
        hauptrollen.add(new MissVerona());
        hauptrollen.add(new Blutwolf());
        hauptrollen.add(new Chemiker());
        hauptrollen.add(new Geisterwolf());
        hauptrollen.add(new Schreckenswolf());
        hauptrollen.add(new Werwolf());
        hauptrollen.add(new Wolfsmensch());
        hauptrollen.add(new Wölfin());
        hauptrollen.add(new Henker());
        hauptrollen.add(new Überläufer());
    }

    private void generateAllAvailableBonusrollen() {
        bonusrollen.add(new Analytiker());
        bonusrollen.add(new Archivar());
        bonusrollen.add(new Dieb());
        bonusrollen.add(new Gefängniswärter());
        //Imitator
        bonusrollen.add(new Konditor());
        bonusrollen.add(new Konditorlehrling());
        bonusrollen.add(new Lamm());
        bonusrollen.add(new Medium());
        bonusrollen.add(new Nachbar());
        bonusrollen.add(new Prostituierte());
        bonusrollen.add(new ReineSeele());
        bonusrollen.add(new Schatten());
        bonusrollen.add(new Schattenkutte());
        bonusrollen.add(new Schnüffler());
        bonusrollen.add(new SchwarzeSeele());
        bonusrollen.add(new Seelenlicht());
        bonusrollen.add(new Spurenleser());
        bonusrollen.add(new Tarnumhang());
        bonusrollen.add(new Totengräber());
        bonusrollen.add(new Vampirumhang());
        bonusrollen.add(new Wahrsager());
        bonusrollen.add(new Wolfspelz());
    }

    public Winner checkVictory() {
        List<Fraktion> fraktionen = Fraktion.getLivingFraktionen();

        switch (fraktionen.size()) {
            case 0:
                return Winner.ALL_DEAD;
            case 1:
                Winner winner = Winner.FRAKTION;
                winner.fraktion = fraktionen.get(0);
                return winner;
            case 2:
                if (getLivingSpielerStrings().size() == 2) {
                    Spieler spieler1 = findSpieler(getLivingSpielerStrings().get(0));
                    Spieler spieler2 = findSpieler(getLivingSpielerStrings().get(1));
                    if (liebespaar != null && ((liebespaar.spieler1 == spieler1 && liebespaar.spieler2 == spieler2) ||
                            (liebespaar.spieler1 == spieler2 && liebespaar.spieler2 == spieler1))) {
                        return Winner.LIEBESPAAR;
                    } else {
                        return Winner.NO_WINNER;
                    }
                } else {
                    return Winner.NO_WINNER;
                }
            default:
                return Winner.NO_WINNER;
        }
    }

    public void killSpieler(Spieler spieler) {
        if (spieler != null && spieler.lebend) {
            spieler.lebend = false;
            Hauptrolle hauptrolle = spieler.hauptrolle;
            Bonusrolle bonusrolle = spieler.bonusrolle;
            if (!(GrafVladimir.verschleierterSpieler != null && spieler.equals(GrafVladimir.verschleierterSpieler))) {
                mitteHauptrollen.add(hauptrolle);
                mitteBonusrollen.add(bonusrolle); //TODO methode auslagern?
            }

            if (Rolle.rolleLebend(Wölfin.ID) && Wölfin.state == WölfinState.WARTEND) {
                if (hauptrolle.fraktion.equals(Werwölfe.ID)) {
                    Wölfin.state = WölfinState.TÖTEND;
                }
            }

            //TODO rolle.cleanUp()

            if (bonusrolle.equals(Schnüffler.ID)) {
                ((Schnüffler) bonusrolle).informationen = new ArrayList<>();
            }

            if (bonusrolle.equals(Tarnumhang.ID)) {
                ((Tarnumhang) bonusrolle).seenSpieler = new ArrayList<>();
            }
        }
    }

    public Spieler findSpieler(String name) {
        return spieler.stream()
                .filter(spieler -> spieler.equals(name))
                .findAny().orElse(null);
    }

    public boolean spielerExists(String name) {
        return findSpieler(name) != null;
    }

    public Spieler findSpielerPerRolle(String rolleID) {
        for (Bonusrolle bonusrolle : mitteBonusrollen) {
            if (bonusrolle.equals(rolleID)) {
                return findSpielerPerRolle(Sammler.ID);
            }
        }

        for (Spieler currentSpieler : spieler) {
            if (currentSpieler.hauptrolle.equals(rolleID) || currentSpieler.bonusrolle.equals(rolleID)) {
                return currentSpieler;
            }
        }

        return null;
    }

    public Spieler findSpielerOrDeadPerRolle(String rolleID) {
        return spieler.stream()
                .filter(spieler -> spieler.hauptrolle.equals(rolleID) || spieler.bonusrolle.equals(rolleID))
                .findAny().orElse(null);
    }

    public DropdownOptions getSpielerDropdownOptions(boolean addNone) {
        List<String> spielerStrings = spieler.stream()
                .filter(spieler -> spieler.lebend)
                .map(spieler -> spieler.name)
                .collect(Collectors.toList());

        if(addNone) {
            return new DropdownOptions(spielerStrings, DropdownConstants.EMPTY);
        } else {
            return new DropdownOptions(spielerStrings);
        }
    }

    public DropdownOptions getSpielerDropdownOptions(Rolle rolle, boolean mitspieler, boolean checkSpammable, boolean addNone) {
        Spieler caller = findSpielerPerRolle(rolle.id);

        List<String> spielerStrings = spieler.stream()
                .filter(spieler -> spieler.lebend)
                .map(spieler -> spieler.name)
                .collect(Collectors.toList());

        if (mitspieler && caller != null) {
            spielerStrings.remove(caller.name);
        }

        if (checkSpammable && !rolle.spammable && rolle.besuchtLastNight != null) {
            spielerStrings.remove(rolle.besuchtLastNight.name);
        }

        if(addNone) {
            return new DropdownOptions(spielerStrings, DropdownConstants.EMPTY);
        } else {
            return new DropdownOptions(spielerStrings);
        }
    }

    public FrontendControl getSpielerFrontendControl(Rolle rolle, boolean mitspieler, boolean checkSpammable, boolean addNone) {
        return new FrontendControl(getSpielerDropdownOptions(rolle, mitspieler, checkSpammable, addNone));
    }

    public List<Spieler> getLivingSpieler() {
        return spieler.stream()
                .filter(spieler -> spieler.lebend)
                .collect(Collectors.toList());
    }

    public List<String> getLivingSpielerStrings() {
        return spieler.stream()
                .filter(spieler -> spieler.lebend)
                .map(spieler -> spieler.name)
                .collect(Collectors.toList());
    }

    public List<String> getHauptrolleNames() {
        return hauptrollen.stream()
                .map(rolle -> rolle.name)
                .collect(Collectors.toList());
    }

    public List<String> getHauptrolleInGameIDs() {
        return hauptrollenInGame.stream()
                .map(rolle -> rolle.id)
                .collect(Collectors.toList());
    }

    public List<String> getHauptrolleInGameNames() {
        return hauptrollenInGame.stream()
                .map(rolle -> rolle.name)
                .collect(Collectors.toList());
    }

    public List<String> getPossibleInGameHauptrolleNames() {
        List<String> hauptrollenInGame = getHauptrolleInGameNames();

        for (Hauptrolle hauptrolle : mitteHauptrollen) {
            if (!hauptrolle.equals(Schattenpriester.ID)) {
                hauptrollenInGame.remove(hauptrolle.name);
            }
        }

        return hauptrollenInGame;
    }

    public List<Hauptrolle> getStillAvailableHauptrollen() { //TODO remove?
        return stillAvailableHauptrollen;
    }

    public List<Hauptrolle> getStillAvailableBürger() {
        return getStillAvailableHauptrollen().stream()
                .filter(hauptrolle -> hauptrolle.fraktion.equals(Bürger.ID))
                .collect(Collectors.toList());
    }

    public Hauptrolle findHauptrolle(String RolleID) {
        return hauptrollen.stream()
                .filter(rolle -> rolle.equals(RolleID))
                .findAny().orElse(null);
    }

    public Hauptrolle findHauptrollePerName(String rolleName) {
        return hauptrollen.stream()
                .filter(rolle -> rolle.name.equals(rolleName))
                .findAny().orElse(null);
    }

    public int numberOfOccurencesOfHauptrolleInGame(Hauptrolle hauptrolle) {
        return (int) hauptrollenInGame.stream()
                .filter(rolle -> rolle.equals(hauptrolle))
                .count();
    }

    public void addAllHauptrollenToGame() {
        hauptrollenInGame.clear();
        spielerSpecified.clear();
        hauptrollenInGame.addAll(hauptrollen);
        hauptrollenInGame.remove(findHauptrolle(Dorfbewohner.ID));
        hauptrollenInGame.remove(findHauptrolle(Werwolf.ID));
    }

    public List<String> getBonusrollenButtonNames() {
        return bonusrollen.stream()
                .filter(r -> !(r.equals(SchwarzeSeele.ID) || r.equals(ReineSeele.ID)))
                .map(rolle -> rolle.name)
                .collect(Collectors.toList());
    }

    public List<String> getBonusrolleInGameIDs() {
        return bonusrollenInGame.stream()
                .map(rolle -> rolle.id)
                .collect(Collectors.toList());
    }

    public List<String> getBonusrolleInGameNames() {
        return bonusrollenInGame.stream()
                .map(rolle -> rolle.name)
                .collect(Collectors.toList());
    }

    public List<String> getPossibleInGameBonusrolleNames() {
        List<String> bonusrolleInGameNames = getBonusrolleInGameNames();

        for (Bonusrolle bonusrolle : mitteBonusrollen) {
            bonusrolleInGameNames.remove(bonusrolle.name);
        }

        return bonusrolleInGameNames;
    }

    public List<Bonusrolle> getStillAvailableBonusrollen() {
        return stillAvailableBonusrollen;
    }

    public Bonusrolle findBonusrolle(String rolleID) {
        return bonusrollen.stream()
                .filter(rolle -> rolle.equals(rolleID))
                .findAny().orElse(null);
    }

    public Bonusrolle findBonusrollePerName(String rolleName) {
        return bonusrollen.stream()
                .filter(rolle -> rolle.name.equals(rolleName))
                .findAny().orElse(null);
    }

    public int numberOfOccurencesOfBonusrolleInGame(Bonusrolle bonusrolle) {
        return (int) bonusrollenInGame.stream()
                .filter(rolle -> rolle.equals(bonusrolle))
                .count();
    }

    public void addAllBonusrollen() {
        bonusrollenInGame.clear();
        spielerSpecified.clear();
        bonusrollenInGame.addAll(bonusrollen);
        bonusrollenInGame.remove(findBonusrolle(Schatten.ID));
        bonusrollenInGame.remove(findBonusrolle(SchwarzeSeele.ID));
        bonusrollenInGame.remove(findBonusrolle(ReineSeele.ID));
    }

    public List<Spieler> getSpielerUnspecified() {
        List<Spieler> spielerUnspecified = ListHelper.cloneList(spieler);
        spielerUnspecified.removeAll(spielerSpecified);
        return spielerUnspecified;
    }

    public List<String> getSpielerUnspecifiedStrings() {
        return getSpielerUnspecified().stream()
                .map(spieler -> spieler.name)
                .collect(Collectors.toList());
    }

    public List<Hauptrolle> getHauptrollenSpecified() {
        return spielerSpecified.stream()
                .map(spieler -> spieler.hauptrolle)
                .collect(Collectors.toList());
    }

    public List<String> getHauptrollenSpecifiedIDs() {
        return spielerSpecified.stream()
                .map(spieler -> spieler.hauptrolle.id)
                .collect(Collectors.toList());
    }

    public List<Hauptrolle> getHauptrollenUnspecified() {
        List<Hauptrolle> hauptrollenUnspecified = ListHelper.cloneList(hauptrollenInGame);

        getHauptrollenSpecified().forEach(hauptrollenUnspecified::remove);

        return hauptrollenUnspecified;
    }

    public List<String> getHauptrollenUnspecifiedStrings() {
        return getHauptrollenUnspecified().stream()
                .map(rolle -> rolle.name)
                .collect(Collectors.toList());
    }

    public List<Bonusrolle> getBonusrollenSpecified() {
        return spielerSpecified.stream()
                .map(spieler -> spieler.bonusrolle)
                .collect(Collectors.toList());
    }

    public List<String> getBonusrolleSpecifiedIDs() {
        return spielerSpecified.stream()
                .map(spieler -> spieler.bonusrolle.id)
                .collect(Collectors.toList());
    }

    public List<Bonusrolle> getBonusrollenUnspecified() {
        List<Bonusrolle> bonusrollenUnspecified = ListHelper.cloneList(bonusrollenInGame);

        getBonusrollenSpecified().forEach(bonusrollenUnspecified::remove);

        return bonusrollenUnspecified;
    }

    public List<String> getBonusrollenUnspecifiedStrings() {
        return getBonusrollenUnspecified().stream()
                .map(rolle -> rolle.name)
                .collect(Collectors.toList());
    }

    public List<Spieler> getIrrlichter() {
        List<Spieler> livingSpieler = getLivingSpieler();
        return livingSpieler.stream()
                .filter(p -> p.hauptrolle.equals(Irrlicht.ID))
                .collect(Collectors.toList());
    }

    public List<String> getIrrlichterStrings() {
        return getIrrlichter().stream()
                .map(spieler -> spieler.name)
                .collect(Collectors.toList());
    }
}
