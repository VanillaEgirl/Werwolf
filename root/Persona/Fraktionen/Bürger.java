package root.Persona.Fraktionen;

import root.Persona.Fraktion;
import root.Persona.Rollen.Constants.Zeigekarten.FraktionsZeigekarten.BürgerZeigekarte;
import root.Persona.Rollen.Constants.Zeigekarten.Zeigekarte;
import root.ResourceManagement.ImagePath;

import java.awt.*;

public class Bürger extends Fraktion {
    public static final String NAME = "Bürger";
    public static final String IMAGE_PATH = ImagePath.BÜRGER_ICON;
    public static final Color COLOR = Color.yellow;
    public static final Zeigekarte zeigekarte = new BürgerZeigekarte();

    public Bürger() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;

        this.color = COLOR;
    }

    @Override
    public Zeigekarte getZeigeKarte() {
        return zeigekarte;
    }
}
