package root.Persona.Rollen.Nebenrollen;

import root.Persona.Nebenrolle;
import root.Persona.Rollen.Constants.NebenrollenType.NebenrollenType;
import root.Persona.Rollen.Constants.NebenrollenType.Passiv;
import root.ResourceManagement.ImagePath;

public class SchwarzeSeele extends Nebenrolle {
    public static final String NAME = "Schwarze Seele";
    public static final String IMAGE_PATH = ImagePath.SCHWARZE_SEELE_KARTE;
    public static final NebenrollenType TYPE = new Passiv();

    public SchwarzeSeele() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.type = TYPE;
    }
}
