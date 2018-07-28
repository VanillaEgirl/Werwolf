package root.Phases;

/**
 * Created by Steve on 27.12.2017.
 */
public class StatementIndie extends Statement {
    public StatementIndie(String beschreibung, String title, int type) {
        this.beschreibung = beschreibung;
        this.title = title;
        this.type = type;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isLebend() {
        return true;
    }

    @Override
    public boolean isOpfer() {
        return false;
    }

    @Override
    public boolean isAktiv() {
        return true;
    }
}
