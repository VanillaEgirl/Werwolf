package root.Persona.Rollen.Hauptrollen.Bürger;

import root.Persona.Fraktion;
import root.Persona.Fraktionen.Bürger;
import root.Persona.Hauptrolle;
import root.ResourceManagement.ImagePath;

public class Bruder extends Hauptrolle {
    public static final String NAME = "Bruder";
    public static final String IMAGE_PATH = ImagePath.BRUDER_KARTE;
    public static final Fraktion FRAKTION = new Bürger();

    public Bruder() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.fraktion = FRAKTION;

        this.numberOfPossibleInstances = 2;
    }
}