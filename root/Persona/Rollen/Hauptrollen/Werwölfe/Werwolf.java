package root.Persona.Rollen.Hauptrollen.Werwölfe;

import root.Persona.Fraktion;
import root.Persona.Fraktionen.Werwölfe;
import root.Persona.Hauptrolle;
import root.ResourceManagement.ImagePath;

public class Werwolf extends Hauptrolle {
    public static final String ID = "Werwolf";
    public static final String NAME = "Werwolf";
    public static final String IMAGE_PATH = ImagePath.WERWOLF_KARTE;
    public static final Fraktion FRAKTION = new Werwölfe();

    public Werwolf() {
        this.id = ID;
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.fraktion = FRAKTION;

        this.numberOfPossibleInstances = 100;
        this.killing = true;
    }
}