package ui.publicview;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import dao.ArtefactDAO;
import model.Artefact;

public class ArtefactDetailView extends JFrame {

    // ── Obsidian & Gold Palette (matches PublicView) ─────────────────────────
    private static final Color BG_DEEP     = new Color(0x0D0D0D);
    private static final Color BG_PANEL    = new Color(0x141414);
    private static final Color BG_CARD     = new Color(0x1A1A1A);
    private static final Color BG_SURFACE  = new Color(0x1F1F1F);
    private static final Color GOLD        = new Color(0xC9A84C);
    private static final Color GOLD_LIGHT  = new Color(0xE8CB80);
    private static final Color GOLD_DIM    = new Color(0x7A6530);
    private static final Color BORDER_DIM  = new Color(0x2E2E2E);
    private static final Color BORDER_GOLD = new Color(0x5A4A20);
    private static final Color TEXT_BRIGHT = new Color(0xF0EAD6);
    private static final Color TEXT_MUTED  = new Color(0x8A8070);
    private static final Color TEXT_LABEL  = new Color(0xC9A84C);

    public ArtefactDetailView(int artefactId) {
        setTitle("Artefact Details");
        setSize(680, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Root 
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Corner vignette
                float[] frac = {0f, 0.5f, 1f};
                Color[] cols = {new Color(20,18,10,0), new Color(10,9,5,0), new Color(0,0,0,130)};
                g2.setPaint(new RadialGradientPaint(
                    new Point2D.Float(getWidth()/2f, getHeight()/2f),
                    Math.max(getWidth(), getHeight()) * 0.72f, frac, cols));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Grain
                g2.setColor(new Color(255,248,220,3));
                for (int y=0; y<getHeight(); y+=2)
                    for (int x=(y%4==0?0:1); x<getWidth(); x+=2)
                        g2.fillRect(x,y,1,1);
                g2.dispose();
            }
        };
        root.setOpaque(true);
        setContentPane(root);

        ArtefactDAO dao = new ArtefactDAO();
        Artefact a = dao.getArtefactById(artefactId);

        if (a == null) {
            JLabel error = new JLabel("Artefact not found", JLabel.CENTER);
            error.setFont(new Font("Serif", Font.ITALIC, 18));
            error.setForeground(TEXT_MUTED);
            root.add(error);
            setVisible(true);
            return;
        }

        //HEADER
        root.add(buildHeader(a.getName()), BorderLayout.NORTH);

        // CONTENT 
        root.add(buildContent(a), BorderLayout.CENTER);

        // FOOTER 
        root.add(buildFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // HEADER 
    private JPanel buildHeader(String name) {
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_PANEL);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Gold hairline at bottom
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
        JPanel ornament = buildOrnament();
        header.add(ornament);
        header.add(Box.createVerticalStrut(8));

        // Artefact name as gold-gradient title
        JLabel title = new JLabel(name, JLabel.CENTER) {
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
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Eyebrow label
        JLabel eyebrow = new JLabel("ARTEFACT RECORD", JLabel.CENTER);
        eyebrow.setFont(new Font("Serif", Font.PLAIN, 10));
        eyebrow.setForeground(GOLD_DIM);
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        eyebrow.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        header.add(eyebrow);
        header.add(title);
        header.add(Box.createVerticalStrut(2));
        return header;
    }

    //CONTENT 
    private JPanel buildContent(Artefact a) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 36, 10, 36));

        // Inner card 
        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 8;
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),arc,arc));
                // Top sheen
                g2.setPaint(new GradientPaint(0,0,new Color(255,245,200,8),0,getHeight()*0.3f,new Color(0,0,0,0)));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),arc,arc));
                // Border
                g2.setColor(BORDER_DIM);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,arc,arc));
                // Gold top accent
                g2.setPaint(new GradientPaint(0,0,new Color(0,0,0,0), getWidth()*0.15f,0, GOLD_DIM));
                for (int x=0; x<getWidth(); x++) {
                    float t = (float)x/getWidth();
                    float al = (float)Math.sin(t*Math.PI);
                    g2.setColor(new Color(GOLD_DIM.getRed(),GOLD_DIM.getGreen(),GOLD_DIM.getBlue(),(int)(al*200)));
                    g2.fillRect(x,0,1,3);
                }
                g2.dispose();
            }
        };
        card.setOpaque(false);

        //LEFT: Image
        JPanel imagePanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_SURFACE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Right separator
                int x = getWidth()-1;
                for (int y=20; y<getHeight()-20; y++) {
                    float t = (float)(y-20)/(getHeight()-40);
                    float al = (float)Math.sin(t*Math.PI);
                    g2.setColor(new Color(GOLD_DIM.getRed(),GOLD_DIM.getGreen(),GOLD_DIM.getBlue(),(int)(al*100)));
                    g2.drawLine(x,y,x,y);
                }
                g2.dispose();
            }
        };
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(230, 0));
        imagePanel.setLayout(new GridBagLayout());

        // Load & style image
        ImageIcon icon = new ImageIcon(a.getImagePath());
        Image scaled = icon.getImage().getScaledInstance(190, 160, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaled), JLabel.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Frame behind image
                int pad = 6;
                g2.setColor(new Color(255,235,150,12));
                g2.fillRoundRect(pad, pad, getWidth()-pad*2, getHeight()-pad*2, 6, 6);
                g2.setColor(BORDER_GOLD);
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(pad, pad, getWidth()-pad*2-1, getHeight()-pad*2-1, 6, 6);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        imgLabel.setOpaque(false);
        imagePanel.add(imgLabel);

        //  RIGHT: Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        // Detail rows — use actual getters, no name changes
        addDetailRow(detailsPanel, "Material",    a.getMaterial());
        addDetailRow(detailsPanel, "Category",    a.getCategoryName());
        addDetailRow(detailsPanel, "Period",      a.getPeriodName());
        addDetailRow(detailsPanel, "Region",      a.getRegionName());
        addDetailRow(detailsPanel, "Description", a.getDescription());

        card.add(imagePanel,   BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    // DETAIL ROW 
    private void addDetailRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Bottom separator
                int y = getHeight()-1;
                g2.setColor(BORDER_DIM);
                g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };
        row.setLayout(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height + 28));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Serif", Font.PLAIN, 10));
        lbl.setForeground(TEXT_LABEL);
        lbl.setPreferredSize(new Dimension(80, 20));

        // Gold dot
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GOLD_DIM);
                g2.fillOval(getWidth()/2-2, getHeight()/2-2, 4, 4);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(14, 20));
        dot.setOpaque(false);

        JLabel val = new JLabel("<html><body style='width:150px'>" +
                (value != null ? value : "—") + "</body></html>");
        val.setFont(new Font("Serif", Font.PLAIN, 13));
        val.setForeground(TEXT_BRIGHT);

        JPanel left = new JPanel(new BorderLayout(4,0));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(100, 20));
        left.add(dot, BorderLayout.WEST);
        left.add(lbl, BorderLayout.CENTER);

        row.add(left, BorderLayout.WEST);
        row.add(val,  BorderLayout.CENTER);

        parent.add(row);
        parent.add(Box.createVerticalStrut(2));
    }

    // FOOTER 
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8)) {
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

        // Close button with hover glow
        JButton close = new JButton("Close") {
            float hover = 0f;
            Timer anim;
            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setFont(new Font("Serif", Font.PLAIN, 13));
                setForeground(TEXT_MUTED);
                setPreferredSize(new Dimension(110, 32));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { slideTo(1f); }
                    @Override public void mouseExited(MouseEvent e)  { slideTo(0f); }
                    void slideTo(float t) {
                        if (anim!=null) anim.stop();
                        anim = new Timer(13, null);
                        anim.addActionListener(ae -> {
                            hover += (t-hover)*0.18f;
                            if (Math.abs(t-hover)<0.01f) { hover=t; anim.stop(); }
                            setForeground(lerp(TEXT_MUTED, GOLD_LIGHT, hover));
                            repaint();
                        });
                        anim.start();
                    }
                });
                addActionListener(e -> dispose());
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w=getWidth(), h=getHeight(), arc=5;
                // Glow
                if (hover>0.02f) {
                    for (int i=8;i>0;i--) {
                        float a=(hover*0.06f)*((float)(9-i)/8f);
                        g2.setColor(new Color(GOLD.getRed(),GOLD.getGreen(),GOLD.getBlue(),(int)(a*255)));
                        g2.setStroke(new BasicStroke(i*1.4f));
                        g2.draw(new RoundRectangle2D.Float(1,1,w-2,h-2,arc,arc));
                    }
                }
                g2.setColor(lerp(BORDER_DIM, BORDER_GOLD, hover));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,w-1,h-1,arc,arc));
                super.paintComponent(g);
                g2.dispose();
            }
        };

        footer.add(close);
        return footer;
    }

    //  Ornament (shared style) 
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

    // Util 
    private static Color lerp(Color a, Color b, float t) {
        t = Math.max(0f, Math.min(1f, t));
        return new Color(
            (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t),
            (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
            (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t));
    }
}