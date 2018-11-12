package root.Frontend.Utils.PageRefresher.Models;

import root.Frontend.Page.PageTable;

import javax.swing.*;
import java.util.List;
import java.util.function.Supplier;

public class LabelTable implements RefreshObject {
    private PageTable table;
    private Supplier<List<String>> labelTexts;

    public LabelTable(PageTable table, Supplier<List<String>> labelTexts) {
        this.table = table;
        this.labelTexts = labelTexts;
    }

    @Override
    public void refresh() {
        table.tableElements.clear();

        for (String text : labelTexts.get()) {
            table.add(new JLabel(text));
        }
    }
}
