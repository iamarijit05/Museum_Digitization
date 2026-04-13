package ui.publicview;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

import dao.ArtefactDAO;
import model.Artefact;

public class ArtefactGridView extends JFrame {

    // ── Obsidian & Gold Palette (matches PublicView / ArtefactDetailView) ────
    private static final Color BG_DEEP     = new Color(0x0D0D0D);
    private static final Color BG_PANEL    = new Color(0x141414);
    private static final Color BG_CARD     = new Color(0x1A1A1A);
    private static final Color BG_CARD_HOV = new Color(0x222218);
    private static final Color GOLD        = new Color(0xC9A84C);
    private static final Color GOLD_LIGHT  = new Color(0xE8CB80);
    private static final Color GOLD_DIM    = new Color(0x7A6530);
    private static final Color BORDER_DIM  = new Color(0x2E2E2E);
    private static final Color BORDER_GOLD = new Color(0x5A4A20);
    private static final Color TEXT_BRIGHT = new Color(0xF0EAD6);
    private static final Color TEXT_MUTED  = new Color(0x8A8070);

    public ArtefactGridView(int categoryId) {
        setTitle("Artefacts");
        setSize(780, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ── Root ──────────────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());
                float[] frac = {0f, 0.5f, 1f};
                Color[] cols = {new Color(20,18,10,0), new Color(10,9,5,0), new Color(0,0,0,130)};
                g2.setPaint(new RadialGradientPaint(
                    new Point2D.Float(getWidth()/2f, getHeight()/2f),
                    Math.max(getWidth(), getHeight()) * 0.72f, frac, cols));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255,248,220,3));
                for (int y=0; y<getHeight(); y+=2)
                    for (int x=(y%4==0?0:1); x<getWidth(); x+=2)
                        g2.fillRect(x, y, 1, 1);
                g2.dispose();
            }
        };
        root.setOpaque(true);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        // ── DATA ──────────────────────────────────────────────────────────
        ArtefactDAO dao = new ArtefactDAO();
        List<Artefact> list = dao.getArtefactsByCategory(categoryId);

        if (list.isEmpty()) {
            root.add(buildEmptyState(), BorderLayout.CENTER);
        } else {
            root.add(buildGrid(list), BorderLayout.CENTER);
        }

        root.add(buildFooter(list.size()), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── HEADER ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_PANEL);
                g2.fillRect(0, 0, getWidth(), getHeight());
                int y = getHeight()-1, m = 50;
                for (int x = m; x < getWidth()-m; x++) {
                    float t = (float)(x-m)/(getWidth()-2*m);
                    float a = (float)Math.sin(t * Math.PI);
                    g2.setColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(),
                            GOLD_DIM.getBlue(), (int)(a*190)));
                    g2.drawLine(x, y, x, y);
                }
                g2.dispose();
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(22, 40, 16, 40));

        // Ornament
        header.add(buildOrnament());
        header.add(Box.createVerticalStrut(9));

        JLabel eyebrow = new JLabel("COLLECTION GALLERY", JLabel.CENTER);
        eyebrow.setFont(new Font("Serif", Font.PLAIN, 10));
        eyebrow.setForeground(GOLD_DIM);
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Artefacts", JLabel.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;
                g2.setColor(new Color(0,0,0,150));
                g2.drawString(getText(), tx+1, ty+2);
                g2.setPaint(new GradientPaint(tx, ty-fm.getAscent(), GOLD_LIGHT, tx, ty+4, GOLD));
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Select an artefact to view its record", JLabel.CENTER);
        subtitle.setFont(new Font("Serif", Font.ITALIC, 13));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(4,0,0,0));

        header.add(eyebrow);
        header.add(Box.createVerticalStrut(5));
        header.add(title);
        header.add(subtitle);
        header.add(Box.createVerticalStrut(2));
        return header;
    }

    // ── GRID ─────────────────────────────────────────────────────────────────
    private JScrollPane buildGrid(List<Artefact> list) {
        JPanel grid = new JPanel(new GridLayout(0, 3, 18, 18)) {
            @Override protected void paintComponent(Graphics g) {
                // transparent — root bg shows through
            }
        };
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(18, 36, 18, 36));

        for (Artefact a : list) {
            grid.add(createArtefactCard(a));
        }

        JScrollPane scroll = new JScrollPane(grid) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(0,0,0,0));
            }
        };
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Style the scrollbar
        JScrollBar vsb = scroll.getVerticalScrollBar();
        vsb.setBackground(BG_DEEP);
        vsb.setForeground(GOLD_DIM);
        vsb.setPreferredSize(new Dimension(6, Integer.MAX_VALUE));
        vsb.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = GOLD_DIM;
                trackColor = new Color(0x1A1A1A);
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroButton(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroButton(); }
            JButton zeroButton() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });

        return scroll;
    }

    // ── ARTEFACT CARD ────────────────────────────────────────────────────────
    private JPanel createArtefactCard(Artefact a) {
        JPanel card = new JPanel(new BorderLayout()) {
            float hover = 0f;
            Timer anim;
            {
                setOpaque(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { slideTo(1f); }
                    @Override public void mouseExited(MouseEvent e)  { slideTo(0f); }
                    @Override public void mouseClicked(MouseEvent e) {
                        new ArtefactDetailView(a.getArtefactId());
                    }
                    void slideTo(float t) {
                        if (anim != null) anim.stop();
                        anim = new Timer(13, null);
                        anim.addActionListener(ae -> {
                            hover += (t - hover) * 0.16f;
                            if (Math.abs(t-hover) < 0.012f) { hover = t; anim.stop(); }
                            repaint();
                        });
                        anim.start();
                    }
                });
            }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), arc = 6;

                // Outer gold glow
                if (hover > 0.02f) {
                    for (int i = 14; i > 0; i--) {
                        float alpha = (hover * 0.055f) * ((float)(15-i) / 14f);
                        g2.setColor(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(),
                                Math.min(255, (int)(alpha * 255))));
                        g2.setStroke(new BasicStroke(i * 1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.draw(new RoundRectangle2D.Float(2, 2, w-4, h-4, arc+4, arc+4));
                    }
                }

                // Card fill
                g2.setColor(lerp(BG_CARD, BG_CARD_HOV, hover));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                // Top sheen
                g2.setPaint(new GradientPaint(0, 0, new Color(255,245,200,(int)(hover*12)),
                        0, h*0.35f, new Color(0,0,0,0)));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                // Gold top accent bar
                for (int x = 0; x < w; x++) {
                    float t = (float)x/w;
                    float al = (float)Math.sin(t * Math.PI);
                    g2.setColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(),
                            (int)(al * (100 + hover * 100))));
                    g2.fillRect(x, 0, 1, 3);
                }

                // Border
                g2.setColor(lerp(BORDER_DIM, BORDER_GOLD, hover));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w-1, h-1, arc, arc));

                // Bottom sweep bar on hover
                if (hover > 0.01f) {
                    float barW = w * hover;
                    float bx = (w - barW) / 2f;
                    for (int xi = 0; xi < (int) barW; xi++) {
                        float t = (float)xi / barW;
                        float al = (float)Math.sin(t * Math.PI);
                        g2.setColor(new Color(GOLD_LIGHT.getRed(), GOLD_LIGHT.getGreen(),
                                GOLD_LIGHT.getBlue(), (int)(al * hover * 220)));
                        g2.fillRect((int)(bx + xi), h-3, 1, 3);
                    }
                }

                g2.dispose();
            }

            @Override public boolean isOpaque() { return false; }
        };

        // Image in a gold-ring circle frame
        ImageIcon raw = new ImageIcon(a.getImagePath());
        Image scaledImg = raw.getImage().getScaledInstance(100, 85, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel("", JLabel.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Framed image box
                int pad = 10;
                int fw = getWidth() - pad*2, fh = getHeight() - pad*2;
                g2.setColor(new Color(255,235,150,12));
                g2.fillRoundRect(pad, pad, fw, fh, 5, 5);
                g2.setColor(BORDER_GOLD);
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(pad, pad, fw, fh, 5, 5);
                // Centre the image
                int ix = (getWidth() - scaledImg.getWidth(null)) / 2;
                int iy = (getHeight() - scaledImg.getHeight(null)) / 2;
                g2.drawImage(scaledImg, ix, iy, null);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(140, 110); }
        };

        // Separator
        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int cx = getWidth()/2;
                for (int dx=0; dx<32; dx++) {
                    float al = (float)(1.0-(double)dx/32); al=al*al;
                    g2.setColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(),
                            GOLD_DIM.getBlue(), (int)(al*130)));
                    g2.drawLine(cx-dx, 0, cx-dx, 0);
                    g2.drawLine(cx+dx, 0, cx+dx, 0);
                }
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(1, 1); }
        };
        sep.setOpaque(false);

        JLabel nameLabel = new JLabel(a.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_BRIGHT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 14, 8));
        nameLabel.setOpaque(false);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);
        south.add(sep);
        south.add(nameLabel);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(south,      BorderLayout.SOUTH);
        return card;
    }

    // ── EMPTY STATE ──────────────────────────────────────────────────────────
    private JPanel buildEmptyState() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        JPanel inner = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.setColor(BORDER_DIM);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,10,10));
                g2.dispose();
            }
        };
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        inner.setPreferredSize(new Dimension(300, 120));

        JLabel icon = new JLabel("◇", JLabel.CENTER);
        icon.setFont(new Font("Serif", Font.PLAIN, 28));
        icon.setForeground(GOLD_DIM);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg = new JLabel("No artefacts found", JLabel.CENTER);
        msg.setFont(new Font("Serif", Font.ITALIC, 15));
        msg.setForeground(TEXT_MUTED);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        msg.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        inner.add(icon);
        inner.add(msg);
        p.add(inner);
        return p;
    }

    // ── FOOTER ────────────────────────────────────────────────────────────────
    private JPanel buildFooter(int count) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0,0,0,70));
                g2.fillRect(0,0,getWidth(),getHeight());
                int m=60;
                for (int x=m; x<getWidth()-m; x++) {
                    float t=(float)(x-m)/(getWidth()-2*m);
                    float al=(float)Math.sin(t*Math.PI);
                    g2.setColor(new Color(GOLD_DIM.getRed(),GOLD_DIM.getGreen(),
                            GOLD_DIM.getBlue(),(int)(al*100)));
                    g2.drawLine(x,0,x,0);
                }
                g2.dispose();
            }
        };
        footer.setOpaque(false);

        String countText = count == 0 ? "No artefacts in this collection"
                : count + (count == 1 ? " artefact" : " artefacts") + " in this collection";
        JLabel fl = new JLabel(countText);
        fl.setFont(new Font("Serif", Font.ITALIC, 12));
        fl.setForeground(new Color(TEXT_MUTED.getRed(), TEXT_MUTED.getGreen(),
                TEXT_MUTED.getBlue(), 160));
        footer.add(fl);
        return footer;
    }

    // ── ORNAMENT ─────────────────────────────────────────────────────────────
    private JPanel buildOrnament() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx=getWidth()/2, cy=getHeight()/2;
                for (int dx=0;dx<100;dx++) {
                    float a=(float)(1.0-(double)dx/100); a=a*a;
                    g2.setColor(new Color(GOLD.getRed(),GOLD.getGreen(),GOLD.getBlue(),(int)(a*150)));
                    g2.drawLine(cx-16-dx,cy,cx-16-dx,cy);
                    g2.drawLine(cx+16+dx,cy,cx+16+dx,cy);
                }
                int[] px={cx,cx+11,cx,cx-11}; int[] py={cy-11,cy,cy+11,cy};
                g2.setColor(GOLD_DIM); g2.setStroke(new BasicStroke(0.8f));
                g2.drawPolygon(px,py,4);
                int[] ipx={cx,cx+6,cx,cx-6}; int[] ipy={cy-6,cy,cy+6,cy};
                g2.setColor(GOLD_LIGHT); g2.fillPolygon(ipx,ipy,4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize(){return new Dimension(1,24);}
        };
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE,24));
        return p;
    }

    // ── Util ──────────────────────────────────────────────────────────────────
    private static Color lerp(Color a, Color b, float t) {
        t = Math.max(0f, Math.min(1f, t));
        return new Color(
            (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t),
            (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
            (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t));
    }
}