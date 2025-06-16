package br.dev.zancanela.sudoku.ui.custom.panel;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class RascunhoDotsPanel extends JPanel {
    private Set<Integer> rascunhos = new HashSet<>();

    public RascunhoDotsPanel(Set<Integer> rascunhos) {
        if (rascunhos != null) {
            this.rascunhos = new HashSet<>(rascunhos);
        }
        setOpaque(false);
    }

    public void setRascunhos(Set<Integer> rascunhos) {
        this.rascunhos = (rascunhos != null) ? new HashSet<>(rascunhos) : new HashSet<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rascunhos == null || rascunhos.isEmpty()) return;

        int w = getWidth();
        int h = getHeight();
        int cellW = w / 3;
        int cellH = h / 3;
        int dot = (int) (Math.min(cellW, cellH) * 0.4);
        dot = Math.max(4, dot);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int n = 1; n <= 9; n++) {
            if (rascunhos.contains(n)) {
                int row = (n - 1) / 3;
                int col = (n - 1) % 3;
                int cx = col * cellW + cellW / 2;
                int cy = row * cellH + cellH / 2;
                g2.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);
            }
        }
    }
}