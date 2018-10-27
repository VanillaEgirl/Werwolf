package root.Persona.Rollen.Constants.InformationsCluster;

import root.Persona.Rollen.Constants.Zeigekarten.Nicht_Tötend;
import root.Persona.Rollen.Constants.Zeigekarten.Tötend;
import root.Persona.Rollen.Constants.Zeigekarten.Zeigekarte;

import java.util.ArrayList;
import java.util.Arrays;

public class TötendInfo extends InformationsCluster {
    private ArrayList<Zeigekarte> informations = new ArrayList<>(Arrays.asList(
            new Tötend(), new Nicht_Tötend()));
}
