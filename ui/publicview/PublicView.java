package ui.publicview;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class PublicView extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(0xF5F0E8);   // warm parchment
    private static final Color CARD_BG     = new Color(0xFFFDF7);   // off-white ivory
    private static final Color CARD_HOVER  = new Color(0xFFF8EC);   // warm cream hover
    private static final Color ACCENT      = new Color(0x8B4513);   // saddle brown
    private static final Color ACCENT_SOFT = new Color(0xC8976A);   // soft copper
    private static final Color BORDER_CLR  = new Color(0xD6C9B0);   // antique border
    private static final Color TITLE_CLR   = new Color(0x2C1810);   // deep espresso
    private static final Color SUB_CLR     = new Color(0x7A6652);   // warm muted

    public PublicView() {
        setTitle("Digital Museum Explorer");
        setSize(780, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ── Root background ───────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // subtle warm-to-cream gradient background
                GradientPaint gp = new GradientPaint(
                        0, 0,          new Color(0xEDE5D4),
                        0, getHeight(), new Color(0xF7F2E9));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // ── HEADER ────────────────────────────────────────────────────────
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // ornamental divider line at bottom
                g2.setColor(BORDER_CLR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(40, getHeight() - 1, getWidth() - 40, getHeight() - 1);
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(28, 20, 16, 20));

        // Decorative top rule
        JPanel topRule = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                g2.setColor(ACCENT_SOFT);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(cx - 120, cy, cx - 14, cy);
                g2.drawLine(cx + 14,  cy, cx + 120, cy);
                // diamond centre ornament
                int[] xp = {cx, cx + 6, cx, cx - 6};
                int[] yp = {cy - 5, cy, cy + 5, cy};
                g2.fillPolygon(xp, yp, 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(1, 18); }
        };
        topRule.setOpaque(false);
        topRule.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JLabel title = new JLabel("Digital Museum Explorer", JLabel.CENTER);
        title.setFont(loadFont("Georgia", Font.BOLD, 30));
        title.setForeground(TITLE_CLR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Explore History Through Artefacts", JLabel.CENTER);
        subtitle.setFont(loadFont("Georgia", Font.ITALIC, 14));
        subtitle.setForeground(SUB_CLR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        header.add(topRule);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(subtitle);

        // ── CATEGORY GRID ─────────────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(24, 36, 28, 36));

        grid.add(createCategoryCard("Anthropology",  "images/anthropology.png", 1));
        grid.add(createCategoryCard("Arms & Armour", "images/armour.png",       2));
        grid.add(createCategoryCard("Painting",      "images/painting.png",     3));
        grid.add(createCategoryCard("Manuscript",    "images/manuscript.png",   4));
        grid.add(createCategoryCard("Jewellery",     "images/jewellery.png",    5));
        grid.add(createCategoryCard("Archaeology",   "images/archaeology.png",  6));

        root.add(header, BorderLayout.NORTH);
        root.add(grid,   BorderLayout.CENTER);

        setVisible(true);
    }

    // ── CATEGORY CARD ─────────────────────────────────────────────────────────
    private JPanel createCategoryCard(String name, String imagePath, int categoryId) {

        JPanel card = new JPanel(new BorderLayout()) {
            private float elevation = 0f;
            private Timer timer;
            {
                setOpaque(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { animateTo(1f); }
                    @Override public void mouseExited(MouseEvent e)  { animateTo(0f); }
                    @Override public void mouseClicked(MouseEvent e) {
                        new ArtefactGridView(categoryId);
                    }
                    private void animateTo(float target) {
                        if (timer != null) timer.stop();
                        timer = new Timer(12, null);
                        timer.addActionListener(ae -> {
                            float diff = target - elevation;
                            if (Math.abs(diff) < 0.04f) { elevation = target; timer.stop(); }
                            else elevation += diff * 0.22f;
                            repaint();
                        });
                        timer.start();
                    }
                });
            }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc   = 10;
                int inset = (int)(elevation * 3);   // subtle lift inset
                int w = getWidth()  - inset * 2;
                int h = getHeight() - inset * 2;

                // drop shadow (lerped)
                int shadowBlur = 4 + (int)(elevation * 10);
                for (int i = shadowBlur; i > 0; i--) {
                    float alpha = (0.04f + 0.025f * elevation) * ((float)(shadowBlur - i + 1) / shadowBlur);
                    g2.setColor(new Color(0, 0, 0, Math.min(1f, alpha)));
                    g2.fill(new RoundRectangle2D.Float(
                            inset - i + 2, inset + i,
                            w + (i * 2) - 2, h, arc + 2, arc + 2));
                }

                // card fill
                Color bg = blend(CARD_BG, CARD_HOVER, elevation);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(inset, inset, w, h, arc, arc));

                // border
                Color border = blend(BORDER_CLR, ACCENT_SOFT, elevation * 0.6f);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(inset + 0.6f, inset + 0.6f,
                        w - 1.2f, h - 1.2f, arc, arc));

                // top accent bar
                float barAlpha = 0.55f + 0.45f * elevation;
                g2.setColor(new Color(
                        ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(),
                        (int)(barAlpha * 255)));
                g2.fill(new RoundRectangle2D.Float(inset, inset, w, 4, arc, arc));
                g2.fillRect(inset, inset + 2, w, 2);

                g2.dispose();
            }

            @Override public boolean isOpaque() { return false; }

            // helper: linear interpolate two colors
            private Color blend(Color a, Color b, float t) {
                t = Math.max(0, Math.min(1, t));
                return new Color(
                        (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t),
                        (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                        (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t));
            }
        };

        // Image
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img), JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        imageLabel.setOpaque(false);

        // Name label
        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        nameLabel.setFont(loadFont("Georgia", Font.BOLD, 13));
        nameLabel.setForeground(TITLE_CLR);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 16, 8));
        nameLabel.setOpaque(false);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(nameLabel,  BorderLayout.SOUTH);

        return card;
    }

    // ── Font helper (falls back gracefully) ──────────────────────────────────
    private static Font loadFont(String family, int style, float size) {
        return new Font(family, style, (int) size);
    }
}