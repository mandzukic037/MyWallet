package ui;

import common.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import service.StorageService;
import storage.*;
import tools.DateUtils;

public class WalletFrame extends JFrame {

    private final Theme theme = new Theme();

    private final StorageService storageService;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private JPanel sidebar;
    private JPanel navTop;
    private JButton darkModeBtn;
    
    private JPanel dashboardItemsContainer;
    private DefaultTableModel expenseModel, incomeModel, storeModel;
    private JPanel chartsContainer;
    private JTextField txtExpFrom, txtExpTo, txtIncFrom, txtIncTo;
    
    private String currentTab = "DASHBOARD";
    private final ArrayList<NavButton> navButtons = new ArrayList<>();
    
    private final Font logoFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font navFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 32);
    private final Font tableHeaderFont = new Font("Segoe UI", Font.BOLD, 13);
    private final Font tableContentFont = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font primaryButtonFont = new Font("Segoe UI", Font.BOLD, 14);

    public WalletFrame() {
        storageService = StorageService.getInstance();
        setTitle("MyWallet - V2.7");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(theme.getColor("BG_COLOR"));

        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBackground(theme.getColor("SIDEBAR_COLOR"));

        navTop = new JPanel();
        navTop.setLayout(new BoxLayout(navTop, BoxLayout.Y_AXIS));
        navTop.setBackground(theme.getColor("SIDEBAR_COLOR"));
        navTop.setBorder(new EmptyBorder(40, 0, 0, 0));

        JLabel logo = new JLabel(" MyWallet");
        logo.setIcon(UIManager.getIcon("FileView.computerIcon"));
        logo.setForeground(Color.WHITE);
        logo.setFont(logoFont);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        navTop.add(logo);
        navTop.add(Box.createVerticalStrut(50));

        String[] navItems = {"DASHBOARD", "EXPENSES", "INCOME", "STORES", "ANALYTICS"};
        for (String item : navItems) {
            NavButton navBtn = new NavButton(item);
            navButtons.add(navBtn);
            navTop.add(navBtn);
        }
        sidebar.add(navTop, BorderLayout.NORTH);

        darkModeBtn = new JButton(theme.isDarkMode() ? "Dark Mode" : "Light Mode"); 
        stylePrimaryButton(darkModeBtn, theme.getColor("SIDEBAR_ACTIVE"));
        darkModeBtn.setPreferredSize(new Dimension(160, 45));
        
        darkModeBtn.addActionListener(e -> {
            theme.toggleDarkMode(); 
            darkModeBtn.setText(theme.isDarkMode() ? "Dark Mode" : "Light Mode");
            deepRefreshUI(this);
            refreshActiveTab();
        });

        JPanel btnWrapper = new JPanel(new GridBagLayout());
        btnWrapper.setOpaque(false);
        btnWrapper.setBorder(new EmptyBorder(0, 0, 20, 20));
        btnWrapper.add(darkModeBtn);

        sidebar.add(btnWrapper, BorderLayout.SOUTH);

        contentPanel.setBackground(theme.getColor("BG_COLOR"));
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createExpensesPanel(), "EXPENSES");
        contentPanel.add(createIncomesPanel(), "INCOME");
        contentPanel.add(createStoresPanel(), "STORES");
        contentPanel.add(createAnalyticsPanel(), "ANALYTICS");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        switchTab("DASHBOARD");
    }

    private void deepRefreshUI(Container container) {
        Color bg = theme.getColor("BG_COLOR");
        Color text = theme.getColor("TEXT_DARK");
        Color side = theme.getColor("SIDEBAR_COLOR");
        Color border = theme.getColor("BORDER_COLOR");

        getContentPane().setBackground(bg);

        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                if (c == sidebar || c == navTop || c instanceof NavButton) {
                    c.setBackground(side);
                } else if (c == contentPanel || !((JPanel)c).isOpaque()) {
                    c.setBackground(bg);
                } else {
                    c.setBackground(bg);
                }
            }
            
            if (c instanceof JLabel) {
                JLabel lbl = (JLabel) c;
                if (lbl.getFont() != logoFont) {
                    lbl.setForeground(text);
                }
            }

            if (c instanceof JTextField) {
                c.setBackground(bg);
                c.setForeground(text);
                ((JTextField) c).setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(border), new EmptyBorder(5, 10, 5, 10)));
            }

            if (c instanceof JTable) {
                JTable table = (JTable) c;
                table.setBackground(bg);
                table.setForeground(text);
                table.getTableHeader().setBackground(bg);
                table.getTableHeader().setForeground(text);
                table.getTableHeader().repaint();
            }

            if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                sp.setBackground(bg);
                sp.getViewport().setBackground(bg);
                sp.setBorder(new LineBorder(border));
            }

            if (c instanceof Container) {
                deepRefreshUI((Container) c);
            }
        }
        
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void switchTab(String tabName) {
        currentTab = tabName;
        cardLayout.show(contentPanel, tabName);
        for (NavButton btn : navButtons) btn.repaint();
        refreshActiveTab();
    }

    class NavButton extends JPanel {
        private final String text;
        private boolean isHovered = false;

        public NavButton(String text) {
            this.text = text;
            setPreferredSize(new Dimension(280, 55));
            setMaximumSize(new Dimension(280, 55));
            setBackground(theme.getColor("SIDEBAR_COLOR"));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { 
                    isHovered = true; repaint(); 
                }

                @Override public void mouseExited(MouseEvent e) { 
                    isHovered = false; repaint(); 
                }

                @Override public void mousePressed(MouseEvent e) { 
                    switchTab(text); 
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean isActive = currentTab.equals(text);

            if (isActive) {
                g2.setColor(theme.getColor("SIDEBAR_ACTIVE"));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(theme.getColor("ACCENT_BLUE"));
                g2.fillRect(0, 0, 5, getHeight());
            } 
            else if (isHovered) {
                g2.setColor(theme.getColor("SIDEBAR_HOVER"));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.setColor(isActive ? Color.WHITE : new Color(148, 163, 184));
            g2.setFont(navFont);
            g2.drawString(text, 40, 32);
        }
    }

    class RibbonBar extends JPanel {
        private final String label; 
        private final double amount; 
        private final double percentage; 
        private final Color color;
        
        public RibbonBar(String label, double amount, double percentage, Color color) {
            this.label = label; 
            this.amount = amount; 
            this.percentage = Math.min(percentage, 100); 
            this.color = color;

            setPreferredSize(new Dimension(0, 130)); 
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 130)); 
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 20, h = getHeight() - 20;

            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(2, 5, w, h, 20, 20);

            g2.setColor(theme.getColor("CARD_COLOR"));
            g2.fillRoundRect(0, 0, w, h, 20, 20);
            g2.setColor(theme.getColor("BORDER_COLOR"));
            g2.drawRoundRect(0, 0, w, h, 20, 20);

            int flagW = 90;
            int[] xP = {0, flagW, flagW - 25, flagW, 0};
            int[] yP = {20, 20, h/2, h-20, h-20};
            g2.setColor(color);
            g2.fillPolygon(xP, yP, 5);

            g2.setColor(theme.getColor("TEXT_MEDIUM")); 
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(label.toUpperCase(), flagW + 35, h/2 - 12);
            g2.setColor(theme.getColor("TEXT_DARK")); 
            g2.setFont(new Font("Segoe UI", Font.BOLD, 42));

            String amtStr = String.format("%,.2f RSD", amount);
            g2.drawString(amtStr, w - g2.getFontMetrics().stringWidth(amtStr) - 40, h/2 + 15);

            int barX = flagW + 35, barY = h/2 + 18, barW = w - barX - 550, barH = 10;
            g2.setColor(new Color(241, 245, 249));
            g2.fillRoundRect(barX, barY, barW, barH, 10, 10);
            g2.setColor(color);
            g2.fillRoundRect(barX, barY, (int)(barW * (percentage/100.0)), barH, 10, 10);
        }
    }

    private JPanel createDashboardPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 40)); 
        main.setBackground(theme.getColor("BG_COLOR")); 
        main.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel welcome = new JLabel("Financial Vault"); 
        welcome.setFont(titleFont); welcome.setForeground(theme.getColor("TEXT_DARK"));

        main.add(welcome, BorderLayout.NORTH);

        dashboardItemsContainer = new JPanel(); 
        dashboardItemsContainer.setLayout(new BoxLayout(dashboardItemsContainer, BoxLayout.Y_AXIS));
        dashboardItemsContainer.setOpaque(false);

        main.add(dashboardItemsContainer, BorderLayout.CENTER); 

        return main;
    }

    private void styleModernTable(JTable table) {
        table.setRowHeight(50);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setFont(tableContentFont);
        table.setBackground(theme.getColor("BG_COLOR"));
        table.setForeground(theme.getColor("TEXT_DARK"));

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(theme.getColor("BG_COLOR"));
                lbl.setForeground(theme.getColor("TEXT_DARK"));
                lbl.setFont(tableHeaderFont);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                return lbl;
            }
        });
        header.setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(table.getColumnCount()-1).setCellRenderer(rightRenderer);
    }

    class ModernScrollBarUI extends BasicScrollBarUI {

        @Override protected void configureScrollBarColors() { 
            trackColor = theme.getColor("BG_COLOR"); 
            thumbColor = new Color(203, 213, 225); 
        }

        @Override protected JButton createDecreaseButton(int orientation) { 
            return createZeroButton(); 
        }

        @Override protected JButton createIncreaseButton(int orientation) { 
            return createZeroButton(); 
        }

        private JButton createZeroButton() { 
            JButton b = new JButton(); 
            b.setPreferredSize(new Dimension(0, 0)); 
            return b; 
        }

        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g; 
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor); 
            g2.fillRoundRect(r.x + 4, r.y + 4, r.width - 8, r.height - 8, 8, 8);
        }

        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) { 
            g.setColor(theme.getColor("BG_COLOR")); 
            g.fillRect(r.x, r.y, r.width, r.height); 
        }
    }

    private JPanel createDataPanel(String title, JTextField from, JTextField to, DefaultTableModel model, boolean isExp) {

        JPanel p = new JPanel(new BorderLayout(0, 25)); 
        p.setBackground(theme.getColor("BG_COLOR")); 
        p.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel header = new JLabel(title); 
        header.setFont(titleFont); 
        header.setForeground(theme.getColor("TEXT_DARK"));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0)); 
        filterBar.setOpaque(true);
        filterBar.setBackground(theme.getColor("BG_COLOR"));
        setupPlaceholder(from, "DD.MM.YYYY."); 
        setupPlaceholder(to, "DD.MM.YYYY.");
        from.setPreferredSize(new Dimension(150, 35)); 
        to.setPreferredSize(new Dimension(150, 35));
        from.setBackground(theme.getColor("BG_COLOR"));
        to.setBackground(theme.getColor("BG_COLOR"));

        JButton btnApply = new JButton("Apply Filter"); 
        stylePrimaryButton(btnApply, theme.getColor("SIDEBAR_COLOR"));
        btnApply.addActionListener(e -> refreshActiveTab());

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setForeground(theme.getColor("TEXT_DARK"));

        JLabel toLabel = new JLabel("To:");
        toLabel.setForeground(theme.getColor("TEXT_DARK"));

        filterBar.add(fromLabel);
        filterBar.add(from);
        filterBar.add(toLabel);
        filterBar.add(to);
        filterBar.add(btnApply);

        JTable table = new ZebraTable(model, theme); 
        styleModernTable(table);

        table.getTableHeader().setReorderingAllowed(true);
        table.getTableHeader().setEnabled(false); 

        JScrollPane scroll = new JScrollPane(table); 
        scroll.setBorder(new LineBorder(theme.getColor("BORDER_COLOR"))); 
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        JButton addBtn = new JButton(isExp ? "+ Add Expense" : "+ Add Income"); 
        stylePrimaryButton(addBtn, isExp ? theme.getColor("DANGER_ROSE") : theme.getColor("SUCCESS_EMERALD"));
        addBtn.addActionListener(e -> showEntryDialog(isExp));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(theme.getColor("BG_COLOR")); 
        bottomPanel.add(addBtn);
        p.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 20)); 
        center.setOpaque(true);
        center.setBackground(theme.getColor("BG_COLOR"));
        center.add(filterBar, BorderLayout.NORTH); 
        center.add(scroll, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER); p.add(bottomPanel, BorderLayout.SOUTH);

        return p;
    }

    private void setupPlaceholder(JTextField tf, String placeholder) {

        tf.setForeground(Color.GRAY); 
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { 
                    tf.setText(""); 
                    tf.setForeground(theme.getColor("TEXT_DARK")); 
                } 
            }

            @Override public void focusLost(FocusEvent e) { 
                if (tf.getText().isEmpty()) { 
                    tf.setForeground(Color.GRAY); 
                    tf.setText(placeholder); 
                } 
            }
        });

    }

    private JPanel createExpensesPanel() { 
        return createDataPanel("Expense Records", txtExpFrom = new JTextField(), txtExpTo = new JTextField(), expenseModel = new DefaultTableModel(new String[]{"Date", "Location", "Description", "Amount (RSD)"}, 0), true); 
    }

    private JPanel createIncomesPanel() { 
        return createDataPanel("Income Records", txtIncFrom = new JTextField(), txtIncTo = new JTextField(), incomeModel = new DefaultTableModel(new String[]{"Date", "Source", "Description", "Amount (RSD)"}, 0), false); 
    }

    private void stylePrimaryButton(JButton b, Color bg) {
        b.setBackground(bg); 
        b.setForeground(Color.WHITE); 
        b.setFont(primaryButtonFont); 
        b.setFocusPainted(false); 
        b.setBorder(new EmptyBorder(12, 30, 12, 30)); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createStoresPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 25)); 
        p.setBackground(theme.getColor("BG_COLOR")); 
        p.setBorder(new EmptyBorder(50, 50, 50, 50));

        storeModel = new DefaultTableModel(new String[]{"Store Name", "Category"}, 0);

        JTable t = new ZebraTable(storeModel, theme); 
        styleModernTable(t);    

        JScrollPane scroll = new JScrollPane(t); 
        scroll.setBorder(new LineBorder(theme.getColor("BORDER_COLOR"))); 
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        JLabel header = new JLabel("Store List"); 
        header.setFont(titleFont); 
        header.setForeground(theme.getColor("TEXT_DARK"));

        p.add(header, BorderLayout.NORTH); 
        p.add(scroll, BorderLayout.CENTER); 
        
        return p;
    }

    private JPanel createAnalyticsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 25)); 
        p.setBackground(theme.getColor("BG_COLOR")); 
        p.setBorder(new EmptyBorder(50,50,50,50));

        JLabel header = new JLabel("Spending Analysis"); 
        header.setFont(titleFont); 
        header.setForeground(theme.getColor("TEXT_DARK"));
        p.add(header, BorderLayout.NORTH);

        chartsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 40)); 
        chartsContainer.setBackground(theme.getColor("BG_COLOR"));

        JPanel wrapper = new JPanel(new BorderLayout()); 
        wrapper.setBackground(theme.getColor("BG_COLOR")); 
        wrapper.add(chartsContainer, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper); 
        scroll.setBorder(null); 
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        p.add(scroll, BorderLayout.CENTER); return p;
    }

    private void refreshActiveTab() {
        ArrayList<Expense> allEx = ExpenseStorage.loadExpenses(); 
        ArrayList<Income> allIn = IncomeStorage.loadIncomes();
        double sEx = allEx.stream().mapToDouble(Expense::getAmount).sum(); 
        double sIn = allIn.stream().mapToDouble(Income::getAmount).sum();

        if (currentTab.equals("DASHBOARD") && dashboardItemsContainer != null) {
            dashboardItemsContainer.removeAll(); 
            dashboardItemsContainer.add(Box.createVerticalStrut(10));
            dashboardItemsContainer.add(new RibbonBar("TOTAL INCOME", sIn, 100, theme.getColor("SUCCESS_EMERALD"))); 
            dashboardItemsContainer.add(Box.createVerticalStrut(25));

            double expPerc = (sIn > 0) ? (sEx / sIn) * 100 : 0;

            dashboardItemsContainer.add(new RibbonBar("TOTAL EXPENSES", sEx, expPerc, theme.getColor("DANGER_ROSE"))); 
            dashboardItemsContainer.add(Box.createVerticalStrut(25));
            dashboardItemsContainer.add(new RibbonBar("NET BALANCE", sIn - sEx, 100, theme.getColor("ACCENT_BLUE")));
            dashboardItemsContainer.revalidate(); dashboardItemsContainer.repaint();
        } 
        else if (currentTab.equals("EXPENSES") && expenseModel != null) {
            expenseModel.setRowCount(0); 
            String f = txtExpFrom.getText().trim(), t = txtExpTo.getText().trim();

            if(f.equals("DD.MM.YYYY.")) f = ""; 
            if(t.equals("DD.MM.YYYY.")) t = "";
            
            ArrayList<Expense> list = (f.isEmpty()) ? allEx : ExpenseStorage.loadExpensesBetween(DateUtils.toDatabaseFormat(f), DateUtils.toDatabaseFormat(t));
            for (Expense e : list) {
                expenseModel.addRow(new Object[]{e.getDate(), e.getMarketName(), e.getDescription(), String.format("%,.2f", e.getAmount())});
            }
        } 
        else if (currentTab.equals("INCOME") && incomeModel != null) {
            incomeModel.setRowCount(0); 
            String f = txtIncFrom.getText().trim(), t = txtIncTo.getText().trim();

            if(f.equals("DD.MM.YYYY.")) f = ""; 
            if(t.equals("DD.MM.YYYY.")) t = "";

            ArrayList<Income> list = (f.isEmpty()) ? allIn : IncomeStorage.loadIncomesBetween(DateUtils.toDatabaseFormat(f), DateUtils.toDatabaseFormat(t));
            for (Income i : list) {
                incomeModel.addRow(new Object[]{i.getDate(), i.getSource(), i.getDescription(), String.format("%,.2f", i.getAmount())});
            }
        } 
        else if (currentTab.equals("STORES") && storeModel != null) {
            storeModel.setRowCount(0); 
            for(Store s : StoreStorage.loadStores()) {
                storeModel.addRow(new Object[]{s.getStoreName(), s.getStoreType()});
            }
        } 
        else if (currentTab.equals("ANALYTICS") && chartsContainer != null) {
            chartsContainer.removeAll(); ArrayList<StoreType> types = StoreTypeStorage.loadStoreTypes();
            double total = types.stream().mapToDouble(StoreType::getAmount).sum();
            int count = 0;

            for (StoreType st : types) {
                if(st.getAmount() > 0) {
                    chartsContainer.add(new DonutChartItem(st.getStoreType(), (st.getAmount()/total)*100, theme));
                    count++;
                }
            }

            int rows = (int) Math.ceil(count / 3.0);
            chartsContainer.setPreferredSize(new Dimension(800, rows * 340));
            chartsContainer.revalidate(); chartsContainer.repaint();
        }
    }

    private void showEntryDialog(boolean isExpense) {
        JDialog dialog = new JDialog(this, isExpense ? "New Expense" : "New Income", true);

        JPanel p = new JPanel(new GridBagLayout()); 
        p.setBackground(theme.getColor("CARD_COLOR")); 
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField d = new JTextField(DateUtils.getCurrentDate()), s = new JTextField(), de = new JTextField(), a = new JTextField();
        
        for(JTextField tf : new JTextField[]{d, s, de, a}) { 
            tf.setPreferredSize(new Dimension(220, 35)); 
            tf.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.getColor("BORDER_COLOR")), new EmptyBorder(5, 10, 5, 10))); 
        }

        gbc.gridx=0; gbc.gridy=0;
        p.add(new JLabel("Date (DD.MM.YYYY.):") {{
            setForeground(theme.getColor("TEXT_DARK"));
        }}, gbc);
        gbc.gridx=1;
        p.add(d, gbc);

        gbc.gridx=0; gbc.gridy=1;
        p.add(new JLabel(isExpense ? "Store/Location:" : "Source:") {{
            setForeground(theme.getColor("TEXT_DARK"));
        }}, gbc);
        gbc.gridx=1;
        p.add(s, gbc);

        gbc.gridx=0; gbc.gridy=2;
        p.add(new JLabel("Description:") {{
            setForeground(theme.getColor("TEXT_DARK"));
        }}, gbc);
        gbc.gridx=1;
        p.add(de, gbc);

        gbc.gridx=0; gbc.gridy=3;
        p.add(new JLabel("Amount (RSD):") {{
            setForeground(theme.getColor("TEXT_DARK"));
        }}, gbc);
        gbc.gridx=1;
        p.add(a, gbc);

        JButton btnSave = new JButton("Save Record"); 
        stylePrimaryButton(btnSave, isExpense ? theme.getColor("DANGER_ROSE") : theme.getColor("SUCCESS_EMERALD"));

        btnSave.addActionListener(e -> {
            try {
                String dv = d.getText().trim(), sv = s.getText().trim(), dev = de.getText().trim();
                double amt = Double.parseDouble(a.getText().trim().replace(",", "."));
                
                if (amt <= 0) {
                    throw new NumberFormatException();
                }
                
                Object[] row = {dv, sv, dev, String.format("%,.2f", amt)};
                if (isExpense) {
                    expenseModel.insertRow(0, row); 
                }
                else {
                    incomeModel.insertRow(0, row); 
                }
                dialog.dispose();
                storageService.submitTask(() -> {
                    if (isExpense) ExpenseStorage.saveExpense(new Expense(dv, dev, amt, sv));
                    else IncomeStorage.saveIncome(new Income(dv, dev, amt, sv));
                    SwingUtilities.invokeLater(this::refreshActiveTab);
                });
            } 
            catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(dialog, "Invalid data!", "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });

        gbc.gridy=4; 
        gbc.gridwidth=2; 
        gbc.insets = new Insets(25, 0, 0, 0); 

        p.add(btnSave, gbc);
        
        dialog.add(p); 
        dialog.pack(); 
        dialog.setLocationRelativeTo(this); 
        dialog.setVisible(true);
    }
}