package root.Rollen.Hauptrollen.Überläufer;

import root.Frontend.FrontendControl;
import root.ResourceManagement.ResourcePath;
import root.Rollen.Fraktion;
import root.Rollen.Fraktionen.Überläufer_Fraktion;
import root.Rollen.Hauptrolle;
import root.Rollen.Hauptrollen.Bürger.Dorfbewohner;
import root.Spieler;

import java.util.ArrayList;

public class Überläufer extends Hauptrolle
{
    public static final String name = "Überläufer";
    public static Fraktion fraktion = new Überläufer_Fraktion();
    public static final String imagePath = ResourcePath.ÜBERLÄUFER_KARTE;
    public static boolean spammable = false;

    @Override
    public FrontendControl getDropdownOptions() {
        ArrayList<String> nehmbareHauptrollen = getMitteHauptrollenStrings();
        nehmbareHauptrollen.add("");
        return new FrontendControl(FrontendControl.DROPDOWN_LIST, nehmbareHauptrollen);
    }

    @Override
    public void processChosenOption(String chosenOption) {
        Hauptrolle chosenHauptrolle = game.findHauptrolle(chosenOption);
        if (chosenHauptrolle != null) {
            try {
                Spieler spielerHauptrolle = game.findSpielerPerRolle(chosenHauptrolle.getName());
                chosenHauptrolle = spielerHauptrolle.hauptrolle;

                Spieler spielerÜberläufer = game.findSpielerPerRolle(name);
                spielerÜberläufer.hauptrolle = chosenHauptrolle;
                spielerHauptrolle.hauptrolle = new Dorfbewohner();

                game.mitteHauptrollen.remove(chosenHauptrolle);
                game.mitteHauptrollen.add(this);
            }catch (NullPointerException e) {
                System.out.println(name + " nicht gefunden");
            }
        }
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
}