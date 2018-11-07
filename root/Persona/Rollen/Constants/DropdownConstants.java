package root.Persona.Rollen.Constants;

public enum DropdownConstants {
    JA("Ja"),
    NEIN("Nein"),
    GUT("Gut"),
    SCHLECHT("Schlecht");

    public String name;

    DropdownConstants(String name) {
        this.name = name;
    }

    public boolean equals(String string) {
        return name.equals(string);
    }
}
