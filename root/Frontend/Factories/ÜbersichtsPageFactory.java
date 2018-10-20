package root.Frontend.Factories;

import root.Frontend.Frame.ÜbersichtsFrame;
import root.Frontend.Page.Page;
import root.Frontend.Page.PageElement;
import root.mechanics.Liebespaar;

import javax.swing.*;

public class ÜbersichtsPageFactory {
    ÜbersichtsFrame übersichtsFrame;
    ÜbersichtsPageElementFactory pageElementFactory;

    public ÜbersichtsPageFactory(ÜbersichtsFrame frame) {
        übersichtsFrame = frame;
        pageElementFactory = new ÜbersichtsPageElementFactory(übersichtsFrame);
    }

    public Page generateÜbersichtsPage() {
        int tableElementHeight = 25;
        int nameLabelWidth = 150;
        int hauptrolleLabelWidth = 150;
        int secondaryRoleLabelWidth = 150;
        int aliveLabelWidth = 80;
        int activeLabelWidth = 80;
        int protectedLabelWidth = 80;
        int spaceBetween = 5;
        int columns = 1;

        PageElement liebespaarLabel = null;

        Page übersichtsPage = new Page();

        Liebespaar liebespaar = übersichtsFrame.game.liebespaar;

        if (liebespaar != null && liebespaar.spieler1 != null && !liebespaar.spieler1.name.equals(liebespaar.spieler2.name)) {
            liebespaarLabel = pageElementFactory.generateLabel(null, "Liebespaar: " + liebespaar.spieler1.name + ", " + liebespaar.spieler2.name);
            übersichtsPage.add(liebespaarLabel);
        }

        int xOffset = 0;
        übersichtsFrame.playerTable = pageElementFactory.generatePageTable(liebespaarLabel, columns, nameLabelWidth,
                tableElementHeight, 0, spaceBetween, xOffset, spaceBetween);

        xOffset += nameLabelWidth + spaceBetween;
        übersichtsFrame.hauptrolleTable = pageElementFactory.generatePageTable(liebespaarLabel, columns, hauptrolleLabelWidth,
                tableElementHeight, 0, spaceBetween, xOffset, spaceBetween);

        xOffset += hauptrolleLabelWidth + spaceBetween;
        übersichtsFrame.secondaryRoleTable = pageElementFactory.generatePageTable(liebespaarLabel, columns, secondaryRoleLabelWidth,
                tableElementHeight, 0, spaceBetween, xOffset, spaceBetween);

        xOffset += secondaryRoleLabelWidth + spaceBetween;
        übersichtsFrame.aliveTable = pageElementFactory.generatePageTable(liebespaarLabel, columns, aliveLabelWidth,
                tableElementHeight, 0, spaceBetween, xOffset, spaceBetween);

        xOffset += aliveLabelWidth + spaceBetween;
        übersichtsFrame.activeTable = pageElementFactory.generatePageTable(liebespaarLabel, columns, activeLabelWidth,
                tableElementHeight, 0, spaceBetween, xOffset, spaceBetween);

        xOffset += activeLabelWidth + spaceBetween;
        übersichtsFrame.protectedTable = pageElementFactory.generatePageTable(liebespaarLabel, columns, protectedLabelWidth,
                tableElementHeight, 0, spaceBetween, xOffset, spaceBetween);

        übersichtsFrame.refreshJButton = new JButton();
        PageElement refreshButton = pageElementFactory.generateLowestButton(übersichtsFrame.refreshJButton, "Refresh", true);

        übersichtsPage.addTable(übersichtsFrame.playerTable);
        übersichtsPage.addTable(übersichtsFrame.hauptrolleTable);
        übersichtsPage.addTable(übersichtsFrame.secondaryRoleTable);
        übersichtsPage.addTable(übersichtsFrame.aliveTable);
        übersichtsPage.addTable(übersichtsFrame.activeTable);
        übersichtsPage.addTable(übersichtsFrame.protectedTable);
        übersichtsPage.add(refreshButton);

        return übersichtsPage;
    }
}
