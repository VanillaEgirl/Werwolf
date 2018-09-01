package root.Rollen.Hauptrollen.Bürger;

import root.Frontend.FrontendControl;
import root.ResourceManagement.ImagePath;
import root.Rollen.Fraktion;
import root.Rollen.Fraktionen.Bürger;
import root.Rollen.Hauptrolle;

import java.util.ArrayList;

/**
 * Created by Steve on 12.11.2017.
 */
public class Buchhalter extends Hauptrolle
{
    public static final String JA = "Ja";
    public static final String NEIN = "Nein";
    public static final String USED_TITLE = "Hauptrollen im Spiel";

    public static final String name = "Buchhalter";
    public static Fraktion fraktion = new Bürger();
    public static final String imagePath = ImagePath.BUCHHALTER_KARTE;
    public static boolean spammable = false;
    public ArrayList<String> seenRoles;

    @Override
    public FrontendControl getDropdownOptions() {
        FrontendControl frontendControl = new FrontendControl();

        frontendControl.typeOfContent = FrontendControl.DROPDOWN;
        frontendControl.strings = new ArrayList<>();
        frontendControl.strings.add(JA);
        frontendControl.strings.add(NEIN);

        return frontendControl;
    }

    @Override
    public FrontendControl processChosenOptionGetInfo(String chosenOption) {
        if(chosenOption!=null) {
            if (chosenOption.equals(JA)) {
                abilityCharges--;
                seenRoles = game.getMainRolesAlive();
                return new FrontendControl(FrontendControl.LIST, USED_TITLE, seenRoles);
            }
        }

        return new FrontendControl();
    }

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
    public boolean isSpammable() {
        return spammable;
    }

    public FrontendControl getAufgebrauchtPage() {
        return new FrontendControl(FrontendControl.LIST, USED_TITLE, seenRoles);
    }
}
