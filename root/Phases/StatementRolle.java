package root.Phases;

import root.Rollen.Rolle;
import root.mechanics.Opfer;

/**
 * Created by Steve on 27.12.2017.
 */
public class StatementRolle extends Statement {
    String rolle;

    public StatementRolle(String beschreibung, String title, String rolle, int type, boolean visible) {
        this.beschreibung = beschreibung;
        this.title = title;
        this.rolle = rolle;
        this.type = type;
        this.visible = visible;
    }

    @Override
    public boolean isLebend() {
        return Rolle.rolleLebend(rolle);
    }

    @Override
    public boolean isOpfer() {
        return Opfer.isOpferPerRolle(rolle);
    }

    @Override
    public boolean isAktiv() {
        return Rolle.rolleAktiv(rolle);
    }

    public Rolle getRolle() {
        return Rolle.findRolle(rolle);
    }
}
