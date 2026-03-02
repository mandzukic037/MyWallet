package ui;

import java.awt.*;

public final class Theme {
    private Color BG_COLOR = new Color(248, 250, 252); 
    private Color SIDEBAR_COLOR = new Color(15, 23, 42); 
    private Color SIDEBAR_HOVER = new Color(30, 41, 59);
    private Color SIDEBAR_ACTIVE = new Color(51, 65, 85);
    private Color ACCENT_BLUE = new Color(59, 130, 246); 
    private Color CARD_COLOR = Color.WHITE;
    private Color TEXT_DARK = new Color(30, 41, 59);
    private Color TEXT_MEDIUM = new Color(100, 116, 139);
    private Color SUCCESS_EMERALD = new Color(16, 185, 129);
    private Color DANGER_ROSE = new Color(225, 29, 72);
    private Color BORDER_COLOR = new Color(226, 232, 240);
    private Color DARK_MODE =  new Color(20, 24, 35);

    private boolean darkMode = true ;

    public Theme() {
        toggleDarkMode();
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void toggleDarkMode() {
        if(darkMode){
            this.BG_COLOR = new Color(20, 24, 35);  
            this.SIDEBAR_COLOR = new Color(15, 23, 42);   
            this.SIDEBAR_HOVER = new Color(30, 41, 59);
            this.SIDEBAR_ACTIVE = new Color(51, 65, 85);
            this.ACCENT_BLUE = new Color(59, 130, 246); 
            this.CARD_COLOR = new Color(32, 38, 50);
            this.TEXT_DARK =  new Color(220, 220, 220);  
            this.TEXT_MEDIUM = new Color(160, 160, 180);
            this.SUCCESS_EMERALD = new Color(16, 185, 129);
            this.DANGER_ROSE = new Color(225, 29, 72);
            this.BORDER_COLOR = new Color(226, 232, 240);
            this.DARK_MODE = new Color(248, 250, 252);
        }
        else{
            this.BG_COLOR = new Color(248, 250, 252); 
            this.SIDEBAR_COLOR = new Color(15, 23, 42); 
            this.SIDEBAR_HOVER = new Color(30, 41, 59);
            this.SIDEBAR_ACTIVE = new Color(51, 65, 85);
            this.ACCENT_BLUE = new Color(59, 130, 246); 
            this.CARD_COLOR = Color.WHITE;
            this.TEXT_DARK = new Color(30, 41, 59);
            this.TEXT_MEDIUM = new Color(100, 116, 139);
            this.SUCCESS_EMERALD = new Color(16, 185, 129);
            this.DANGER_ROSE = new Color(225, 29, 72);
            this.BORDER_COLOR = new Color(226, 232, 240);
            this.DARK_MODE =  new Color(20, 24, 35);
        }
        
        darkMode = !darkMode;
    }

    public Color getColor(String name) {
        return switch (name) {
            case "BG_COLOR" -> BG_COLOR;
            case "SIDEBAR_COLOR" -> SIDEBAR_COLOR;
            case "SIDEBAR_HOVER" -> SIDEBAR_HOVER;
            case "SIDEBAR_ACTIVE" -> SIDEBAR_ACTIVE;
            case "ACCENT_BLUE" -> ACCENT_BLUE;
            case "CARD_COLOR" -> CARD_COLOR;
            case "TEXT_DARK" -> TEXT_DARK;
            case "TEXT_MEDIUM" -> TEXT_MEDIUM;
            case "SUCCESS_EMERALD" -> SUCCESS_EMERALD;
            case "DANGER_ROSE" -> DANGER_ROSE;
            case "BORDER_COLOR" -> BORDER_COLOR;
            case "DARK_MODE" -> DARK_MODE;
            default -> Color.MAGENTA;
        };
    }
}
