package ui.artefact;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import dao.ArtefactDAO;
import model.Artefact;
import service.ArtefactService;

/**
 * Form for locating and updating an existing museum artefact.
 *
 * <p>The user enters an artefact ID and clicks "Load" to populate all fields
 * with the current record. Any field can then be edited and saved via
 * "Update Artefact". Image replacement is handled through a file-chooser.
 *
 * <p>Visual style mirrors {@link ui.dashboard.Dashboard} — dark navy palette
 * with per-widget accent colours and smooth hover animations.
 */
public class UpdateArtefactForm extends JFrame {

    // ── Form fields ───────────────────────────────────────────────
    private JTextField idField, nameField, materialField;
    private JTextArea  descriptionArea;
    private JLabel     imageLabel;
    private String     imagePath;

    private JComboBox<String> categoryBox, periodBox, regionBox;

    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> periodMap   = new HashMap<>();
    private Map<String, Integer> regionMap   = new HashMap<>();

    /** Base font used for all input fields. */
    private Font font = new Font("Segoe UI", Font.PLAIN, 13);

    // ── Palette (shared with Dashboard / AddArtefactForm) ─────────
    static final Color BG         = new Color(15, 17, 26);
    static final Color CARD_BG    = new Color(24, 28, 44);
    static final Color BORDER_CLR = new Color(40, 46, 70);
    static final Color TEXT_MAIN  = new Color(226, 232, 240);
    static final Color TEXT_MUTED = new Color(99, 130, 165);
    static final Color ACCENT     = new Color(246, 173, 85);   // amber — "Update" action
    static final Color FIELD_BG   = new Color(20, 24, 40);

    // ── Constructor ───────────────────────────────────────────────

    public UpdateArtefactForm() {

        setTitle("Update Artefact");
        setSize(580, 660);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        // ── Header ───────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 34, 54),
                        getWidth(), 0, new Color(20, 24, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Amber accent rule
                g2.setColor(ACCENT);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));

        JLabel icon = new JLabel("✎");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        icon.setForeground(ACCENT);
        icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Update Artefact");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(TEXT_MAIN);

        JLabel subtitle = new JLabel("Load a record by ID, then edit and save");
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

        // ── Form panel ───────────────────────────────────────────
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // ── ID + Load ─────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panel.add(makeLabel("Artefact ID:"), gbc);

        gbc.gridx = 1;
        idField = makeTextField("Enter numeric ID…");
        panel.add(idField, gbc);

        gbc.gridx = 2;
        JButton loadBtn = makeAccentButton("Load", new Color(99, 179, 237));
        loadBtn.setPreferredSize(new Dimension(80, 32));
        panel.add(loadBtn, gbc);

        // ── Name ──────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        panel.add(makeLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        nameField = makeTextField("Artefact name");
        panel.add(nameField, gbc);

        // ── Material ──────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        panel.add(makeLabel("Material:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        materialField = makeTextField("e.g. Bronze, Clay, Marble…");
        panel.add(materialField, gbc);

        // ── Description ───────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(makeLabel("Description:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1; gbc.gridwidth = 2;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(font);
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
        panel.add(descScroll, gbc);

        // ── Category ──────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        panel.add(makeLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        categoryBox = makeComboBox();
        panel.add(categoryBox, gbc);

        // ── Period ────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        panel.add(makeLabel("Period:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        periodBox = makeComboBox();
        panel.add(periodBox, gbc);

        // ── Region ────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        panel.add(makeLabel("Region:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        regionBox = makeComboBox();
        panel.add(regionBox, gbc);

        // ── Image ─────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        panel.add(makeLabel("Image:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 1;
        JButton imageBtn = makeAccentButton("Change", new Color(99, 179, 237));
        imageBtn.setPreferredSize(new Dimension(100, 32));
        panel.add(imageBtn, gbc);

        gbc.gridx = 2;
        imageLabel = new JLabel("No file");
        imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        imageLabel.setForeground(TEXT_MUTED);
        panel.add(imageLabel, gbc);

        // ── Update button ─────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill   = GridBagConstraints.NONE;
        gbc.insets = new Insets(18, 8, 8, 8);

        JButton updateBtn = makeAccentButton("Update Artefact", ACCENT);
        updateBtn.setPreferredSize(new Dimension(200, 42));
        panel.add(updateBtn, gbc);

        // ── Scroll wrapper ────────────────────────────────────────
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG);
        styleScrollBar(scrollPane.getVerticalScrollBar());
        add(scrollPane, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────
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

        // ── Action listeners (logic untouched) ───────────────────
        loadBtn.addActionListener(e -> loadArtefact());
        imageBtn.addActionListener(e -> selectImage());
        updateBtn.addActionListener(e -> updateArtefact());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ── UI factory helpers ────────────────────────────────────────

    /**
     * Creates a muted label styled for the dark theme.
     *
     * @param text display text
     * @return styled {@link JLabel}
     */
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    /**
     * Creates a dark-themed text field with placeholder (hint) text.
     * The hint disappears on focus and reappears if the field is left empty.
     *
     * @param hint placeholder string shown when the field is empty
     * @return styled {@link JTextField}
     */
    private JTextField makeTextField(String hint) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_MUTED);
        field.setCaretColor(ACCENT);
        field.setText(hint);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        field.setPreferredSize(new Dimension(200, 32));

        // Placeholder behaviour — clears hint on focus, restores on blur
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(TEXT_MAIN);
                }
                // Teal border highlights the active field
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(TEXT_MUTED);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_CLR),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
        });
        return field;
    }

    /**
     * Creates a dark-themed combo box with a styled drop-down renderer
     * and a minimal custom arrow button.
     *
     * @return styled {@link JComboBox}
     */
    private JComboBox<String> makeComboBox() {
        JComboBox<String> box = new JComboBox<>();
        box.setFont(font);
        box.setBackground(FIELD_BG);
        box.setForeground(TEXT_MAIN);
        box.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(40, 50, 80) : FIELD_BG);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
        box.setUI(new BasicComboBoxUI() {
            @Override protected JButton createArrowButton() {
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

    /**
     * Creates a rounded accent button with a smooth hover animation.
     * Background blends from {@code CARD_BG} toward {@code accent} on mouse enter.
     *
     * @param text   button label
     * @param accent highlight colour used for border, hover tint, and glow
     * @return styled {@link JButton}
     */
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
                            if (Math.abs(hover - tgt) < 0.01f) { hover = tgt; ((Timer) ev.getSource()).stop(); }
                            repaint();
                        });
                        t.start();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Blend card background toward accent colour on hover
                Color base = CARD_BG;
                Color blended = new Color(
                        (int)(base.getRed()   + (accent.getRed()   - base.getRed())   * (0.2f + 0.3f * hover)),
                        (int)(base.getGreen() + (accent.getGreen() - base.getGreen()) * (0.2f + 0.3f * hover)),
                        (int)(base.getBlue()  + (accent.getBlue()  - base.getBlue())  * (0.2f + 0.3f * hover))
                );
                g2.setColor(blended);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Accent border brightens on hover
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                        (int)(120 + 100 * hover)));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                // Centred label
                g2.setFont(getFont());
                g2.setColor(TEXT_MAIN);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth()  - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Applies a minimal dark-styled UI to a {@link JScrollBar},
     * removing arrow buttons and using a subtle thumb colour.
     *
     * @param bar the scroll bar to style
     */
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
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // ── Original business logic — untouched ──────────────────────

    /** Populates all three combo boxes from the database. */
    private void loadDropdownData() {
        ArtefactDAO dao = new ArtefactDAO();
        categoryMap = dao.getCategories();

        for (String name : categoryMap.keySet()) categoryBox.addItem(name);

        periodMap = dao.getPeriods();
        for (String name : periodMap.keySet()) periodBox.addItem(name);

        regionMap = dao.getRegions();
        for (String name : regionMap.keySet()) regionBox.addItem(name);
    }

    /**
     * Loads an existing artefact record into the form fields by numeric ID.
     * Shows a dialog if the ID is invalid or the record does not exist.
     */
    private void loadArtefact() {
        try {
            int id = Integer.parseInt(idField.getText());
            Artefact a = new ArtefactDAO().getArtefactById(id);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "Artefact not Found");
                return;
            }
            nameField.setText(a.getName());
            materialField.setText(a.getMaterial());
            descriptionArea.setText(a.getDescription());
            imagePath = a.getImagePath();
            imageLabel.setText("Loaded");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID");
        }
    }

    /** Opens a file chooser so the user can replace the current image. */
    private void selectImage() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagePath = fc.getSelectedFile().getAbsolutePath();
            imageLabel.setText(fc.getSelectedFile().getName());
        }
    }

    /**
     * Reads all form fields, builds an {@link Artefact} object, and
     * delegates persistence to {@link ArtefactService#updateArtefact}.
     * Shows a success or error dialog depending on the outcome.
     */
    private void updateArtefact() {
        try {
            int id = Integer.parseInt(idField.getText());
            Artefact a = new Artefact();
            a.setArtefactId(id);
            a.setName(nameField.getText());
            a.setMaterial(materialField.getText());
            a.setDescription(descriptionArea.getText());
            a.setImagePath(imagePath);

            String cat = (String) categoryBox.getSelectedItem();
            String per = (String) periodBox.getSelectedItem();
            String reg = (String) regionBox.getSelectedItem();

            a.setCategory_id(categoryMap.get(cat));
            a.setPeriod_id(periodMap.get(per));
            a.setRegion_id(regionMap.get(reg));

            new ArtefactService().updateArtefact(a);

            JOptionPane.showMessageDialog(this, "Updated Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}