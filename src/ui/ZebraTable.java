package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

class ZebraTable extends JTable {

    private final Theme theme;

    public ZebraTable(TableModel model, Theme theme) { 
        super(model); 
        this.theme = theme;

        setFillsViewportHeight(true);
        setBackground(theme.getColor("BG_COLOR")); 
        setForeground(theme.getColor("TEXT_DARK"));
    }

    @Override 
    public boolean isCellEditable(int row, int column) { 
        return false; 
    } 

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (!isRowSelected(row)) {
            c.setBackground(row % 2 == 0 ? theme.getColor("CARD_COLOR") : theme.getColor("BG_COLOR"));
        }
        else { 
            c.setBackground(theme.getColor("BG_COLOR")); 
            c.setForeground(theme.getColor("ACCENT_BLUE")); 
        }
        return c;
    }
    
}