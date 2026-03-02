package ui;

import java.awt.*;
import javax.swing.*;

class DonutChartItem extends JPanel {
    private final Theme theme;
            
    private final String name; 
    private final double percentage;

    public DonutChartItem(String name, double percentage, Theme theme) {
        this.theme = theme;
        this.name = name; this.percentage = percentage; 
        setPreferredSize(new Dimension(260, 300)); 
        setBackground(theme.getColor("CARD_COLOR"));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D) g; 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight(), s = 150, x = (w-s)/2, y = 40;
        g2.setColor(theme.getColor("CARD_COLOR")); 
        g2.fillRoundRect(0, 0, w, h, 20, 20); 
        g2.setColor(theme.getColor("BORDER_COLOR"));
         g2.drawRoundRect(0, 0, w-1, h-1, 20, 20);
        g2.setStroke(new BasicStroke(16f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); 
        g2.setColor(new Color(241, 245, 249)); 
        g2.drawOval(x, y, s, s);
        g2.setColor(theme.getColor("ACCENT_BLUE")); 
        g2.drawArc(x, y, s, s, 90, (int)-(percentage*3.6));
        g2.setColor(theme.getColor("TEXT_DARK")); 
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        String txt = String.format("%.1f%%", percentage); 
        g2.drawString(txt, x + (s-g2.getFontMetrics().stringWidth(txt))/2, y + s/2 + 8);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        g2.setColor(theme.getColor("TEXT_MEDIUM"));
        String n = name.toUpperCase(); g2.drawString(n, (w-g2.getFontMetrics().stringWidth(n))/2, y + s + 55);
    }
}