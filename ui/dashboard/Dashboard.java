package ui.dashboard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import ui.artefact.AddArtefactForm;
import ui.artefact.ArtefactListView;
import ui.artefact.DeleteArtefactForm;
import ui.artefact.UpdateArtefactForm;
import ui.publicview.PublicView;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Admin Dashboard");
        setSize(580, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(false);

        // Main background color
        Color bgColor = new Color(15, 17, 26);
        getContentPane().setBackground(bgColor);

        //  Header panel
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Subtle gradient strip
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 34, 54),
                        getWidth(), 0, new Color(20, 24, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom accent line
                g2.setColor(new Color(99, 179, 237));
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(22, 30, 22, 30));

        // Museum icon label (unicode glyph as accent)
        JLabel icon = new JLabel("🏛");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Museum Admin Dashboard");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(new Color(226, 232, 240));

        JLabel subtitle = new JLabel("Artefact Management Console");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(99, 179, 237));

        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(3));
        titleGroup.add(subtitle);

        JPanel iconAndTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconAndTitle.setOpaque(false);
        iconAndTitle.add(icon);
        iconAndTitle.add(titleGroup);

        headerPanel.add(iconAndTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Button panel 
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(bgColor);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        // Each card gets its own accent color
        JButton addBtn    = createStyledButton("Add Artefact",    btnFont, "＋", new Color(56, 178, 172));
        JButton viewBtn   = createStyledButton("View Artefacts",  btnFont, "◉", new Color(99, 179, 237));
        JButton updateBtn = createStyledButton("Update Artefact", btnFont, "✎", new Color(246, 173, 85));
        JButton deleteBtn = createStyledButton("Delete Artefact", btnFont, "✕", new Color(252, 129, 129));

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);

        add(panel, BorderLayout.CENTER);

        // Footer 
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(20, 24, 40));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(40, 46, 70)));
        JLabel footerLabel = new JLabel("Museum Management System  •  Admin v1.0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(74, 85, 104));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        //Actions 
        addBtn.addActionListener(e -> new AddArtefactForm());
        viewBtn.addActionListener(e -> new PublicView());
        updateBtn.addActionListener(e -> new UpdateArtefactForm());
        deleteBtn.addActionListener(e -> new DeleteArtefactForm());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Font font, String iconGlyph, Color accent) {
        JButton btn = new JButton() {
            private float hoverAlpha = 0f;
            private Timer hoverTimer;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { animateTo(1f); }
                    @Override public void mouseExited(MouseEvent e)  { animateTo(0f); }

                    private void animateTo(float target) {
                        if (hoverTimer != null) hoverTimer.stop();
                        hoverTimer = new Timer(12, ev -> {
                            hoverAlpha += (target - hoverAlpha) * 0.25f;
                            if (Math.abs(hoverAlpha - target) < 0.01f) {
                                hoverAlpha = target;
                                ((Timer) ev.getSource()).stop();
                            }
                            repaint();
                        });
                        hoverTimer.start();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background
                Color base = new Color(24, 28, 44);
                Color hovered = new Color(
                        (int)(base.getRed()   + (accent.getRed()   - base.getRed())   * 0.15f * hoverAlpha),
                        (int)(base.getGreen() + (accent.getGreen() - base.getGreen()) * 0.15f * hoverAlpha),
                        (int)(base.getBlue()  + (accent.getBlue()  - base.getBlue())  * 0.15f * hoverAlpha)
                );
                g2.setColor(hovered);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                // Left accent bar
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                        (int)(80 + 175 * hoverAlpha)));
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);

                // Border
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                        (int)(40 + 100 * hoverAlpha)));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);

                // Icon glyph (top-left area)
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 200));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(iconGlyph, 18, 28);

                // Label text
                g2.setFont(font);
                g2.setColor(new Color(226, 232, 240));
                FontMetrics fm2 = g2.getFontMetrics(font);
                int tx = (getWidth() - fm2.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm2.getAscent() - fm2.getDescent()) / 2 + 6;
                g2.drawString(getText(), tx, ty);

                g2.dispose();
            }

            @Override public String getText() { return text; }
        };

        btn.setFont(font);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 90));
        return btn;
    }
}