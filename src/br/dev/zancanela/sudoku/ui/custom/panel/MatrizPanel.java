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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.TOGGLE_MODO_RASCUNHO;

public class MatrizPanel extends JPanel implements JogoEventListener {
    private static final Font CELULA_FONTE = new Font("Arial", Font.BOLD, 24);
    private final Map<Celula, Set<Integer>> rascunhosPorCelula = new HashMap<>();

    public MatrizPanel(Jogo jogo) {
        configurarPainelPrincipal();
        jogo.addJogoEventListener(TOGGLE_MODO_RASCUNHO, this);
        construirMatriz(jogo);
    }

    private void configurarPainelPrincipal() {
        Dimension dimension = new Dimension(600, 600);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setLayout(new GridLayout(3, 3, 0, 0));
        setBorder(new LineBorder(Color.DARK_GRAY, 3, true));
    }

    private void construirMatriz(Jogo jogo) {
        List<List<Celula>> matriz = jogo.getTabuleiro().getCelulas();
        for (int blocoLin = 0; blocoLin < 3; blocoLin++) {
            for (int blocoCol = 0; blocoCol < 3; blocoCol++) {
                JPanel bloco = criarBlocoPanel();
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

    private JPanel criarBlocoPanel() {
        JPanel bloco = new JPanel(new GridLayout(3, 3));
        bloco.setBorder(new LineBorder(Color.BLACK, 1, true));
        return bloco;
    }

    @Override
    public void onEvent(JogoEventEnum eventType,
                        int novoValor) {
        if (eventType == TOGGLE_MODO_RASCUNHO) {
            repaint();
        }
    }

    private JComponent criarCelulaPanel(Celula celula,
                                        Jogo jogo) {
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        JTextField campo = criarCampoCelula(celula);
        Set<Integer> rascunhos = rascunhosPorCelula.computeIfAbsent(celula, c -> new HashSet<>());
        RascunhoDotsPanel dotsPanel = new RascunhoDotsPanel(rascunhos);
        dotsPanel.setOpaque(false);
        dotsPanel.setVisible(true);

        campo.addKeyListener(criarKeyListenerCelula(campo, celula, jogo, rascunhos, dotsPanel, overlayPanel));
        campo.addMouseListener(criarMouseListenerCelula(rascunhos, dotsPanel, overlayPanel));

        overlayPanel.add(campo, 0);
        overlayPanel.add(dotsPanel, 1);

        return overlayPanel;
    }

    private JTextField criarCampoCelula(Celula celula) {
        JTextField campo = new JTextField();
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setFont(CELULA_FONTE);
        campo.setBorder(BorderFactory.createEmptyBorder());
        campo.setOpaque(false);
        campo.setBackground(new Color(0, 0, 0, 0));

        if (celula.isFixa()) {
            campo.setForeground(Color.DARK_GRAY);
            campo.setBackground(new Color(230, 230, 230));
            campo.setFocusable(false);
            campo.setEditable(false);
        } else {
            campo.setForeground(Color.BLACK);
            campo.setDocument(new NumberDocument());
        }
        campo.setText(celula.getValor() != 0 ? String.valueOf(celula.getValor()) : "");
        return campo;
    }

    private KeyAdapter criarKeyListenerCelula(JTextField campo,
                                              Celula celula,
                                              Jogo jogo,
                                              Set<Integer> rascunhos,
                                              RascunhoDotsPanel dotsPanel,
                                              JPanel overlayPanel) {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) && c != '0') {
                    int valor = Character.getNumericValue(c);
                    if (jogo.isModoRascunho()) {
                        alternarRascunho(rascunhos, valor);
                        campo.setText("");
                        atualizarDotsPanel(dotsPanel, rascunhos, overlayPanel);
                        e.consume();
                    } else {
                        processarValorCelula(campo, celula, valor, jogo);
                        atualizarDotsPanel(dotsPanel, rascunhos, overlayPanel);
                    }
                }
            }
        };
    }

    private void alternarRascunho(Set<Integer> rascunhos,
                                  int valor) {
        if (rascunhos.contains(valor)) {
            rascunhos.remove(valor);
        } else {
            rascunhos.add(valor);
        }
    }

    private void processarValorCelula(JTextField campo,
                                      Celula celula,
                                      int valor,
                                      Jogo jogo) {
        if (celula.getEsperado() == valor) {
            celula.setValor(valor);
            campo.setText(String.valueOf(valor));
            campo.setForeground(Color.BLACK);
        } else {
            campo.setForeground(Color.RED);
            jogo.incrementarErro();
        }
    }

    private void atualizarDotsPanel(RascunhoDotsPanel dotsPanel,
                                    Set<Integer> rascunhos,
                                    JPanel overlayPanel) {
        dotsPanel.setRascunhos(rascunhos);
        overlayPanel.revalidate();
        overlayPanel.repaint();
    }

    private MouseAdapter criarMouseListenerCelula(Set<Integer> rascunhos,
                                                  RascunhoDotsPanel dotsPanel,
                                                  JPanel overlayPanel) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    rascunhos.clear();
                    atualizarDotsPanel(dotsPanel, rascunhos, overlayPanel);
                }
            }
        };
    }
}