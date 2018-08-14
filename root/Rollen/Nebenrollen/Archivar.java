package root.Rollen.Nebenrollen;

import root.Frontend.FrontendControl;
import root.ResourceManagement.ResourcePath;
import root.Rollen.Fraktionen.Bürger;
import root.Rollen.Hauptrollen.Bürger.Bestienmeister;
import root.Rollen.Nebenrolle;
import root.Rollen.NebenrollenTyp;
import root.Spieler;

/**
 * Created by Steve on 12.11.2017.
 */
public class Archivar extends Nebenrolle
{
    public static final String PASSIV_TITLE = "Passiv";
    public static final String AKTIV_TITLE = "Aktiv";
    public static final String INFORMATIV_TITLE = "Informativ";
    public static final String TARNUMHANG_TITLE = "Tarnumhang";

    public static final String name = "Archivar";
    public static final String imagePath = ResourcePath.ARCHIVAR_KARTE;
    public static boolean spammable = true;
    public NebenrollenTyp type = NebenrollenTyp.INFORMATIV;

    @Override
    public FrontendControl getDropdownOptions() {
        return game.getPlayerCheckSpammableFrontendControl(this);
    }

    @Override
    public FrontendControl processChosenOptionGetInfo(String chosenOption) {
        Spieler chosenPlayer = game.findSpieler(chosenOption);

        if(chosenPlayer != null) {
            besucht = chosenPlayer;

            if(chosenPlayer.hauptrolle.getName().equals(Bestienmeister.name)) {
                Spieler spieler = game.findSpielerPerRolle(name);

                if(!spieler.hauptrolle.getFraktion().getName().equals(Bürger.name)) {
                    return new FrontendControl(FrontendControl.IMAGE, TARNUMHANG_TITLE, ResourcePath.TARNUMHANG);
                }
            }

            switch(chosenPlayer.nebenrolle.getType()) {
                case AKTIV:
                    return new FrontendControl(FrontendControl.IMAGE, AKTIV_TITLE, ResourcePath.AKTIV);

                case PASSIV:
                    return new FrontendControl(FrontendControl.IMAGE, PASSIV_TITLE, ResourcePath.PASSIV);

                case INFORMATIV:
                    return new FrontendControl(FrontendControl.IMAGE, INFORMATIV_TITLE, ResourcePath.INFORMATIV);

                case TARNUMHANG:
                    return new FrontendControl(FrontendControl.IMAGE, TARNUMHANG_TITLE, ResourcePath.TARNUMHANG);
            }
        }

        return new FrontendControl();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean isSpammable() {
        return spammable;
    }

    @Override
    public NebenrollenTyp getType() { return type; }
}
