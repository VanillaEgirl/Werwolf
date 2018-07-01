package root.Rollen.Hauptrollen.Bürger;

import root.ResourceManagement.ResourcePath;
import root.Rollen.Fraktion;
import root.Rollen.Fraktionen.Bürger;
import root.Rollen.Hauptrolle;
import root.Rollen.Nebenrolle;
import root.Rollen.Nebenrollen.Totengräber;
import root.Rollen.Rolle;

/**
 * Created by Steve on 12.11.2017.
 */
public class Sammler extends Hauptrolle
{
    public static final String beschreibungAddiditon = "Der Sammler als ";
    public static final String beschreibungAddiditonLowerCase = "der Sammler als ";

    public static final String name = "Sammler";
    public static Fraktion fraktion = new Bürger();
    public static final String imagePath = ResourcePath.SAMMLER_KARTE;
    public static boolean unique = true;
    public static boolean spammable = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Fraktion getFraktion() {
        return fraktion;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public boolean isSpammable() {
        return spammable;
    }

    public static boolean isSammlerRolle(String rolle) {
        for (Nebenrolle currentRolle : Rolle.mitteNebenrollen) {
            if (currentRolle.getName().equals(rolle) &&
                    !currentRolle.getName().equals(Totengräber.name) &&
                    !currentRolle.getType().equals(Nebenrolle.PASSIV)) {
                return true;
            }
        }
        return false;
    }
}