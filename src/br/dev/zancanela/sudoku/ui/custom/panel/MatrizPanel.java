package br.dev.zancanela.sudoku.ui.custom.panel;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;
import br.dev.zancanela.sudoku.domain.event.JogoEventListener;
import br.dev.zancanela.sudoku.domain.model.Celula;
import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.ui.custom.input.NumberDocument;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.TOGGLE_MODO_RASCUNHO;

public class MatrizPanel extends JPanel implements JogoEventListener {
    private final Map<Celula, Set<Integer>> rascunhosPorCelula = new HashMap<>();

    public MatrizPanel(Jogo jogo) {
        Dimension dimension = new Dimension(600, 600);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        jogo.addJogoEventListener(TOGGLE_MODO_RASCUNHO, this);

        List<List<Celula>> matriz = jogo.getTabuleiro().getCelulas();
        setLayout(new GridLayout(3, 3, 0, 0));
        setBorder(new LineBorder(Color.DARK_GRAY, 3, true));

        for (int blocoLin = 0; blocoLin < 3; blocoLin++) {
            for (int blocoCol = 0; blocoCol < 3; blocoCol++) {
                JPanel bloco = new JPanel(new GridLayout(3, 3));

                for (int lin = 0; lin < 3; lin++) {
                    for (int col = 0; col < 3; col++) {
                        int globalLin = blocoLin * 3 + lin;
                        int globalCol = blocoCol * 3 + col;
                        Celula celula = matriz.get(globalLin).get(globalCol);
                        bloco.add(criarCelulaPanel(celula, jogo));
                    }
                }
                add(bloco);
            }
        }
    }

    @Override
    public void onEvent(JogoEventEnum eventType, int novoValor) {
        if (eventType == TOGGLE_MODO_RASCUNHO) {
            repaint();
        }
    }

    private JComponent criarCelulaPanel(Celula celula, Jogo jogo) {
        List<List<Celula>> matriz = jogo.getTabuleiro().getCelulas();
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        JTextField campo = new JTextField();
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setText(celula.getValor() != 0 ? String.valueOf(celula.getValor()) : "");
        campo.setBorder(BorderFactory.createEmptyBorder());

        if (celula.isFixa()) {
            campo.setFont(new Font("Arial", Font.BOLD, 20));
            campo.setForeground(Color.DARK_GRAY);
            campo.setBackground(new Color(230, 230, 230));
            campo.setFocusable(false);
            campo.setEditable(false);
        } else {
            campo.setFont(new Font("Arial", Font.PLAIN, 20));
            campo.setForeground(Color.BLACK);
            campo.setDocument(new NumberDocument());
        }

        Set<Integer> rascunhos = rascunhosPorCelula.computeIfAbsent(celula, c -> new HashSet<>());
        RascunhoDotsPanel dotsPanel = new RascunhoDotsPanel(rascunhos);
        dotsPanel.setOpaque(false);

        if (celula.getValor() != 0) {
            campo.setText(String.valueOf(celula.getValor()));
            campo.setFont(new Font("Arial", Font.BOLD, 20));
        } else {
            campo.setText("");
        }
        dotsPanel.setVisible(true);

        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) && c != '0') {
                    int valor = Character.getNumericValue(c);
                    if (jogo.isModoRascunho()) {
                        if (rascunhos.contains(valor)) {
                            rascunhos.remove(valor);
                        } else {
                            rascunhos.add(valor);
                        }
                        campo.setText("");
                        dotsPanel.setRascunhos(rascunhos);
                        overlayPanel.revalidate();
                        overlayPanel.repaint();
                        e.consume();
                    } else {
                        if (celula.getEsperado() == valor) {
                            celula.setValor(valor);
                            campo.setText(String.valueOf(valor));
                            campo.setForeground(Color.BLACK);
                        } else {
                            campo.setForeground(Color.RED);
                            jogo.incrementarErro();
                        }
                        dotsPanel.setRascunhos(rascunhos);
                        overlayPanel.revalidate();
                        overlayPanel.repaint();
                    }
                }
            }
        });

        campo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    rascunhos.clear();
                    dotsPanel.setRascunhos(rascunhos);
                    overlayPanel.revalidate();
                    overlayPanel.repaint();
                }
            }
        });

        overlayPanel.add(dotsPanel, 0);
        overlayPanel.add(campo, 1);

        return overlayPanel;
    }

}