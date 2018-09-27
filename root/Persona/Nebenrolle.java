package root.Persona;

import root.Frontend.Frame.MyFrame;
import root.Persona.Fraktionen.Bürger;
import root.Persona.Rollen.Constants.NebenrollenType.NebenrollenType;
import root.Persona.Rollen.Constants.NebenrollenType.Passiv;
import root.Persona.Rollen.Hauptrollen.Bürger.Schamanin;
import root.Persona.Rollen.Nebenrollen.Schatten;
import root.Persona.Rollen.Nebenrollen.Tarnumhang;
import root.Spieler;

import java.awt.*;

public class Nebenrolle extends Rolle {
    public NebenrollenType type = new Passiv();

    public static final Nebenrolle defaultNebenrolle = new Schatten();
    public static final Color defaultFarbe = MyFrame.DEFAULT_BUTTON_COLOR;

    public void tauschen(Nebenrolle nebenrolle) {
        try {
            Spieler spieler = game.findSpielerPerRolle(this.name);
            if (spieler != null) {
                spieler.nebenrolle = nebenrolle;
            }
        } catch (NullPointerException e) {
            System.out.println(this.name + " nicht gefunden");
        }
    }

    public Nebenrolle getTauschErgebnis() {
        try {
            Spieler spieler = game.findSpielerPerRolle(this.name);
            if (spieler != null) {
                return spieler.nebenrolle;
            } else {
                return this;
            }
        } catch (NullPointerException e) {
            System.out.println(this.name + " nicht gefunden");
        }

        return this;
    }

    public boolean showTarnumhang(Nebenrolle requester, Spieler spieler) {
        return spieler != null && (spieler.nebenrolle.equals(Tarnumhang.NAME) || (playerIsSchamanin(spieler) && thisRolleIsNotBuerger(requester)));
    }

    private boolean playerIsSchamanin(Spieler player) {
        return player.hauptrolle.equals(Schamanin.NAME);
    }

    private boolean thisRolleIsNotBuerger(Nebenrolle requester) {
        Spieler spieler = game.findSpielerPerRolle(requester.name);

        return !spieler.hauptrolle.fraktion.equals(new Bürger());
    }
}
