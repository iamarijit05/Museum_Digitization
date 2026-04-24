package ui.artefact;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import dao.ArtefactDAO;
import model.Artefact;
import service.ArtefactService;

public class AddArtefactForm extends JFrame {

    private JTextField nameField, materialField;
    private JTextArea descriptionArea;
    private JLabel imageLabel;
    private String imagePath;

    private JComboBox<String> categoryBox, periodBox, regionBox;

    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> periodMap = new HashMap<>();
    private Map<String, Integer> regionMap = new HashMap<>();

    // Professional font setup
    private Font labelFont  = new Font("Segoe UI", Font.PLAIN, 13);
    private Font fieldFont  = new Font("Segoe UI", Font.PLAIN, 13);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);

    // Palette (mirrors Dashboard) 
    private static final Color BG          = new Color(15, 17, 26);
    private static final Color CARD_BG     = new Color(24, 28, 44);
    private static final Color BORDER_CLR  = new Color(40, 46, 70);
    private static final Color TEXT_MAIN   = new Color(226, 232, 240);
    private static final Color TEXT_MUTED  = new Color(99, 130, 165);
    private static final Color ACCENT      = new Color(56, 178, 172);   // teal (same as "Add" button)
    private static final Color FIELD_BG    = new Color(20, 24, 40);

    public AddArtefactForm() {

        setTitle("Add Artefact");
        setSize(560, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        // Header 
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 34, 54), getWidth(), 0, new Color(20, 24, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));

        JLabel icon = new JLabel("＋");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 26));
        icon.setForeground(ACCENT);
        icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Add Artefact");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(TEXT_MAIN);

        JLabel subtitle = new JLabel("Register a new museum artefact");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(ACCENT);

        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(3));
        titleGroup.add(subtitle);

        JPanel iconAndTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconAndTitle.setOpaque(false);
        iconAndTitle.add(icon);
        iconAndTitle.add(titleGroup);
        headerPanel.add(iconAndTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        //  Form panel 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(makeLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = makeTextField();
        formPanel.add(nameField, gbc);

        // Material
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(makeLabel("Material:"), gbc);
        gbc.gridx = 1;
        materialField = makeTextField();
        formPanel.add(materialField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(makeLabel("Description:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(fieldFont);
        descriptionArea.setBackground(FIELD_BG);
        descriptionArea.setForeground(TEXT_MAIN);
        descriptionArea.setCaretColor(ACCENT);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        descScroll.setBackground(FIELD_BG);
        styleScrollBar(descScroll.getVerticalScrollBar());
        formPanel.add(descScroll, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(makeLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryBox = makeComboBox();
        formPanel.add(categoryBox, gbc);

        // Period
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(makeLabel("Period:"), gbc);
        gbc.gridx = 1;
        periodBox = makeComboBox();
        formPanel.add(periodBox, gbc);

        // Region
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(makeLabel("Region:"), gbc);
        gbc.gridx = 1;
        regionBox = makeComboBox();
        formPanel.add(regionBox, gbc);

        // Image
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(makeLabel("Image:"), gbc);

        gbc.gridx = 1;
        JPanel imagePanel = new JPanel(new BorderLayout(10, 0));
        imagePanel.setBackground(BG);

        JButton imageBtn = makeAccentButton("Select Image", new Color(99, 179, 237));
        imageBtn.setPreferredSize(new Dimension(130, 32));

        imageLabel = new JLabel("No file selected");
        imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        imageLabel.setForeground(TEXT_MUTED);

        imagePanel.add(imageBtn, BorderLayout.WEST);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        formPanel.add(imagePanel, gbc);

        imageBtn.addActionListener(e -> selectImage());

        // Submit Button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(18, 8, 8, 8);

        JButton submitBtn = makeAccentButton("Add Artefact", ACCENT);
        submitBtn.setPreferredSize(new Dimension(200, 42));
        formPanel.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> addArtefact());

        //  Scroll wrapper
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG);
        styleScrollBar(scrollPane.getVerticalScrollBar());
        add(scrollPane, BorderLayout.CENTER);

        //  Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(20, 24, 40));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));
        JLabel footerLabel = new JLabel("Museum Management System  •  Admin v1.0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(74, 85, 104));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        loadDropdownData();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    //  UI factory helpers 

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(labelFont);
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_MAIN);
        field.setCaretColor(ACCENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        field.setPreferredSize(new Dimension(200, 32));
        // Highlight border on focus
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
            @Override public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_CLR),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
        });
        return field;
    }

    private JComboBox<String> makeComboBox() {

        JComboBox<String> box = new JComboBox<>();
    
        box.setFont(fieldFont);
    
        // Closed (selected item) text color
        box.setForeground(TEXT_MAIN);
    
        box.setBackground(FIELD_BG);
        box.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
    
        // Custom renderer for dropdown items
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
    
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    
                // Background
                setBackground(isSelected ? new Color(40, 50, 80) : FIELD_BG);
    
                //TEXT COLOR CONTROL
                if (isSelected) {
                    setForeground(Color.WHITE);      // selected item text
                } else {
                    setForeground(TEXT_MUTED);       // normal item text
                }
    
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    
        // Custom arrow button
        box.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▾");
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                btn.setBackground(FIELD_BG);
                btn.setForeground(TEXT_MUTED);
                btn.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                return btn;
            }
        });
    
        return box;
    }

    private JButton makeAccentButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            private float hover = 0f;
            private Timer t;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { animTo(1f); }
                    @Override public void mouseExited(MouseEvent e)  { animTo(0f); }
                    void animTo(float tgt) {
                        if (t != null) t.stop();
                        t = new Timer(12, ev -> {
                            hover += (tgt - hover) * 0.25f;
                            if (Math.abs(hover - tgt) < 0.01f) { hover = tgt; ((Timer)ev.getSource()).stop(); }
                            repaint();
                        });
                        t.start();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = CARD_BG;
                Color blended = new Color(
                        (int)(base.getRed()   + (accent.getRed()   - base.getRed())   * (0.2f + 0.3f * hover)),
                        (int)(base.getGreen() + (accent.getGreen() - base.getGreen()) * (0.2f + 0.3f * hover)),
                        (int)(base.getBlue()  + (accent.getBlue()  - base.getBlue())  * (0.2f + 0.3f * hover))
                );
                g2.setColor(blended);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int)(120 + 100*hover)));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.setFont(getFont());
                g2.setColor(TEXT_MAIN);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(buttonFont);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleScrollBar(JScrollBar bar) {
        bar.setBackground(BG);
        bar.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(50, 60, 90);
                trackColor = BG;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            JButton zeroBtn() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
    }


    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageLabel.setText(fileChooser.getSelectedFile().getName());
        }
    }

    private void loadDropdownData() {
        ArtefactDAO dao = new ArtefactDAO();

        categoryMap = dao.getCategories();
        for (String name : categoryMap.keySet()) {
            categoryBox.addItem(name);
        }

        periodMap = dao.getPeriods();
        for (String name : periodMap.keySet()) {
            periodBox.addItem(name);
        }

        regionMap = dao.getRegions();
        for (String name : regionMap.keySet()) {
            regionBox.addItem(name);
        }
    }

    private void addArtefact() {
        try {
            if (nameField.getText().trim().isEmpty()) {
                throw new RuntimeException("Name cannot be empty");
            }

            if (imagePath == null) {
                throw new RuntimeException("Please select an image");
            }

            Artefact a = new Artefact();

            a.setName(nameField.getText());
            a.setMaterial(materialField.getText());
            a.setDescription(descriptionArea.getText());

            String cat = (String) categoryBox.getSelectedItem();
            String per = (String) periodBox.getSelectedItem();
            String reg = (String) regionBox.getSelectedItem();

            a.setCategory_id(categoryMap.get(cat));
            a.setPeriod_id(periodMap.get(per));
            a.setRegion_id(regionMap.get(reg));
            a.setImagePath(imagePath);

            ArtefactService service = new ArtefactService();
            service.addArtefact(a);

            JOptionPane.showMessageDialog(this, "Artefact added successfully");

            resetForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void resetForm() {
        nameField.setText("");
        materialField.setText("");
        descriptionArea.setText("");
        imageLabel.setText("No file selected");
        imagePath = null;
    }
}