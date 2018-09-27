package root.Persona.Rollen.Nebenrollen;

import root.Persona.Fraktionen.Bürger;
import root.Persona.Nebenrolle;
import root.Persona.Rollen.Constants.NebenrollenType.NebenrollenType;
import root.Persona.Rollen.Constants.NebenrollenType.Passiv;
import root.ResourceManagement.ImagePath;
import root.Spieler;

public class Seelenlicht extends Nebenrolle {
    public static final String NAME = "Seelenlicht";
    public static final String IMAGE_PATH = ImagePath.SEELENLICHT_KARTE;
    public static final NebenrollenType TYPE = new Passiv();

    public Seelenlicht() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.type = TYPE;
    }

    public void tauschen(Nebenrolle nebenrolle) {
        try {
            Spieler spieler = game.findSpielerPerRolle(NAME);
            spieler.nebenrolle = nebenrolle;
        } catch (NullPointerException e) {
            System.out.println(NAME + " nicht gefunden");
        }
    }

    public Nebenrolle getTauschErgebnis() {
        Spieler spieler = game.findSpielerPerRolle(NAME);

        if (spieler != null) {
            Nebenrolle nebenrolle;

            if (spieler.hauptrolle.fraktion.name.equals(Bürger.NAME)) {
                nebenrolle = new ReineSeele();
            } else {
                nebenrolle = new SchwarzeSeele();
            }

            return nebenrolle;
        } else {
            return new ReineSeele();
        }
    }
}
