package root.Persona.Rollen.Hauptrollen.Bürger;

import root.Persona.Bonusrolle;
import root.Persona.Fraktion;
import root.Persona.Fraktionen.Bürger;
import root.Persona.Hauptrolle;
import root.Persona.Rollen.Constants.BonusrollenType.Passiv;
import root.Persona.Rollen.Constants.BonusrollenType.Tarnumhang_BonusrollenType;
import root.Persona.Rollen.Bonusrollen.Totengräber;
import root.ResourceManagement.ImagePath;

public class Sammler extends Hauptrolle {
    public static final String beschreibungAddiditon = "Der Sammler als ";
    public static final String beschreibungAddiditonLowerCase = "der Sammler als ";

    public static final String NAME = "Sammler";
    public static final String IMAGE_PATH = ImagePath.SAMMLER_KARTE;
    public static final Fraktion FRAKTION = new Bürger();

    public Sammler() {
        this.name = NAME;
        this.imagePath = IMAGE_PATH;
        this.fraktion = FRAKTION;
    }


    public static boolean isSammlerRolle(String rolle) {
        for (Bonusrolle currentRolle : game.mitteNebenrollen) {
            if (currentRolle.name.equals(rolle) &&
                    !currentRolle.name.equals(Totengräber.NAME) &&
                    !currentRolle.type.equals(new Passiv()) &&
                    !currentRolle.type.equals(new Tarnumhang_BonusrollenType())) {
                return true;
            }
        }
        return false;
    }
}