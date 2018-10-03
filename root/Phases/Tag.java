package root.Phases;

import root.Frontend.Utils.JButtonStyler;
import root.Frontend.Utils.Timer;
import root.Frontend.FrontendControl;
import root.Persona.Fraktionen.Bürger;
import root.Persona.Hauptrolle;
import root.Persona.Rollen.Bonusrollen.ReineSeele;
import root.Persona.Rollen.Bonusrollen.Wahrsager;
import root.Spieler;
import root.mechanics.Game;

import java.util.ArrayList;

public class Tag extends Thread {
    Game game;

    public static Object lock;
    public static boolean umbringenButton = false;
    public static Spieler umbringenSpieler;
    public static Spieler priester;
    public static ArrayList<Spieler> gebürgteSpieler;
    public static Spieler richterin;
    public static ArrayList<Spieler> verurteilteSpieler;

    public static String dayTitle = "Opfer der Dorfabstimmung";

    public Tag(Game game) {
        this.game = game;
    }

    public void run() {
        gebürgteSpieler = new ArrayList<>();
        verurteilteSpieler = new ArrayList<>();
        day();

        if (game.freibier) {
            day();
            game.freibier = false;
        }

        game.night();//TODO remove
    }

    public void day() {
        lock = new Object();
        synchronized (lock) {
            Timer.time = 0;

            FrontendControl.erzählerDefaultDayPage();
            FrontendControl.spielerDayPage();

            waitForAnswer();

            while (umbringenButton) {
                umbringenButton = false;
                killSpielerCheckLiebespaar(umbringenSpieler);
            }

            Spieler chosenSpieler = game.findSpieler(FrontendControl.erzählerFrame.chosenOption1);

            if (chosenSpieler != null) {
                String bonusrolleSpieler = chosenSpieler.bonusrolle.name;
                Hauptrolle hauptrolleSpieler = chosenSpieler.hauptrolle;

                if (bonusrolleSpieler.equals(ReineSeele.NAME) && ((ReineSeele) chosenSpieler.bonusrolle).dayInvincibility ||
                        (gebürgteSpieler.contains(chosenSpieler) && hauptrolleSpieler.fraktion.name.equals(Bürger.NAME))) {
                    FrontendControl.erzählerAnnounceVictimPage(chosenSpieler, ReineSeele.IMAGE_PATH);
                    FrontendControl.spielerCardPicturePage(chosenSpieler.name, ReineSeele.IMAGE_PATH);
                    if (chosenSpieler.bonusrolle.name.equals(ReineSeele.NAME)) {
                        ((ReineSeele) chosenSpieler.bonusrolle).dayInvincibility = false;
                    }

                    waitForAnswer();

                    checkRichterin(chosenSpieler);
                } else {
                    killSpielerCheckLiebespaar(chosenSpieler);
                    Wahrsager.opferFraktion = chosenSpieler.hauptrolle.fraktion;
                    if (!hauptrolleSpieler.fraktion.name.equals(Bürger.NAME)) {
                        if (priester != null && priester.lebend && gebürgteSpieler.contains(chosenSpieler)) {
                            killSpielerCheckLiebespaar(priester);
                        }
                    }

                    checkRichterin(chosenSpieler);
                }
            } else {
                Wahrsager.opferFraktion = null;
            }

            while (umbringenButton) {
                umbringenButton = false;
                killSpielerCheckLiebespaar(umbringenSpieler);
            }
        }
    }

    public void checkVictory() {
        String victory = game.checkVictory();

        if (victory != null) {
            showEndScreenPage(victory);
        }
    }

    public void checkRichterin(Spieler spieler) {
        if (spieler.hauptrolle.fraktion.name.equals(Bürger.NAME)) {
            if (richterin != null && richterin.lebend && verurteilteSpieler.contains(spieler)) {
                killSpielerCheckLiebespaar(richterin);
            }
        }
    }

    public void killSpieler(Spieler spieler) {
        if (spieler != null) {
            game.killSpieler(spieler);

            FrontendControl.erzählerAnnounceVictimPage(spieler);
            FrontendControl.spielerAnnounceVictimPage(spieler);
        }
    }

    public void killSpielerCheckLiebespaar(Spieler spieler) {
        killSpieler(spieler);

        if (game.liebespaar.getPlayerToDie() != null) {
            JButtonStyler.disableButton(FrontendControl.erzählerFrame.umbringenJButton);
            waitForAnswer();
            killSpieler(game.liebespaar.getPlayerToDie());
        }

        waitForAnswer();

        checkVictory();
    }

    public static void showEndScreenPage(String victory) {
        FrontendControl.erzählerEndScreenPage(victory);
        FrontendControl.spielerEndScreenPage(victory);

        waitForAnswer();
    }

    public void bürgen(String priester, String spieler) {
        Spieler priesterSpieler = game.findSpieler(priester);
        Spieler verbürgerSpieler = game.findSpieler(spieler);

        if (priesterSpieler != null && spieler != null) {
            Tag.priester = priesterSpieler;
            Tag.gebürgteSpieler.add(verbürgerSpieler);
        }
    }

    public void verurteilen(String richterin, String spieler) {
        Spieler richterinSpieler = game.findSpieler(richterin);
        Spieler verurteilterSpieler = game.findSpieler(spieler);

        if (richterinSpieler != null && spieler != null) {
            Tag.richterin = richterinSpieler;
            Tag.verurteilteSpieler.add(verurteilterSpieler);
        }
    }

    public static void waitForAnswer() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
