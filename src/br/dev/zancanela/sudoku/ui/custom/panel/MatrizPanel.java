package br.dev.zancanela.sudoku.ui.custom.panel;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;
import br.dev.zancanela.sudoku.domain.enums.JogoStatusEnum;
import br.dev.zancanela.sudoku.domain.event.JogoEventListener;
import br.dev.zancanela.sudoku.domain.model.Celula;
import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.repository.JogoRepository;
import br.dev.zancanela.sudoku.service.JogoService;
import br.dev.zancanela.sudoku.ui.custom.input.NumberDocument;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.*;
import static br.dev.zancanela.sudoku.domain.enums.JogoStatusEnum.COMPLETO;
import static br.dev.zancanela.sudoku.domain.enums.JogoStatusEnum.NAO_INICIADO;
import static javax.swing.SwingConstants.CENTER;

public class MatrizPanel extends JPanel implements JogoEventListener {
    private static final Font CELULA_FONTE = new Font("Arial", Font.BOLD, 24);
    private final Map<Celula, Set<Integer>> rascunhosPorCelula = new HashMap<>();
    private final List<JTextField> camposEditaveis = new ArrayList<>();
    private transient Jogo jogo;
    private final transient JogoService jogoService;

    public MatrizPanel(Jogo jogo) {
        setPreferredSize(new Dimension(600, 600));
        setLayout(new GridLayout(3, 3, 0, 0));
        setBorder(new LineBorder(Color.DARK_GRAY, 3, true));
        jogo.addJogoEventListener(TOGGLE_MODO_RASCUNHO, this);
        jogo.addJogoEventListener(JOGO_LIMPO, this);
        jogo.addJogoEventListener(JOGO_MODIFICADO, this);
        this.jogo = jogo;
        this.jogoService = new JogoService(new JogoRepository());
        construirMatriz(jogo);
    }

    private void construirMatriz(Jogo jogo) {
        var matriz = jogo.getTabuleiro().celulas();
        for (int blocoLin = 0; blocoLin < 3; blocoLin++)
            for (int blocoCol = 0; blocoCol < 3; blocoCol++) {
                JPanel bloco = new JPanel(new GridLayout(3, 3));
                bloco.setBorder(new LineBorder(Color.BLACK, 1, true));
                for (int lin = 0; lin < 3; lin++)
                    for (int col = 0; col < 3; col++) {
                        int gl = blocoLin * 3 + lin, gc = blocoCol * 3 + col;
                        Celula celula = matriz.get(gl).get(gc);
                        bloco.add(criarCelulaPanel(celula, jogo));
                    }
                add(bloco);
            }
    }

    @Override
    public void onEvent(JogoEventEnum eventType, int novoValor) {
        switch (eventType) {
            case TOGGLE_MODO_RASCUNHO -> repaint();
            case JOGO_LIMPO -> {
                for (var campo : camposEditaveis) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
                repaint();
            }
            case JOGO_MODIFICADO -> {
                if(NAO_INICIADO.equals(jogo.getStatus())) {
                    jogo.setStatusIniciado();
                }
                if(COMPLETO.equals(jogo.getStatus())) {
                    for (var campo : camposEditaveis) {
                        campo.setEditable(false);
                        campo.setFocusable(false);
                    }
                }
                this.jogo = jogoService.atualizarJogo(jogo);
                repaint();
            }
            default -> {
                // Nada a fazer
            }
        }
    }

    private JComponent criarCelulaPanel(Celula celula, Jogo jogo) {
        JPanel overlay = new JPanel();
        overlay.setLayout(new OverlayLayout(overlay));
        overlay.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        JTextField campo = criarCampoCelula(celula);

        Set<Integer> rascunhos = rascunhosPorCelula.computeIfAbsent(celula, c -> new HashSet<>());
        RascunhoDotsPanel dots = new RascunhoDotsPanel(rascunhos);
        dots.setOpaque(false);

        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    int valor = Character.getNumericValue(c);
                    if (valor == 0) {
                        celula.setValor(0);
                        campo.setText("");
                        campo.setForeground(Color.BLACK);
                        jogo.getNotifier().notify(JOGO_MODIFICADO, 0);
                        atualizarDotsPanel(dots, rascunhos, overlay);
                        e.consume();
                    } else if (jogo.isModoRascunho()) {
                        if (!rascunhos.add(valor)) rascunhos.remove(valor);
                        campo.setText("");
                        atualizarDotsPanel(dots, rascunhos, overlay);
                        e.consume();
                    } else {
                        celula.setValor(valor);
                        campo.setText(String.valueOf(valor));
                        if (celula.getEsperado() == valor) {
                            campo.setForeground(Color.BLACK);
                        } else {
                            campo.setForeground(Color.RED);
                            jogo.incrementarErro();
                        }
                        jogo.getNotifier().notify(JOGO_MODIFICADO, 0);
                        atualizarDotsPanel(dots, rascunhos, overlay);
                    }
                } else if (c == '\b' || c == KeyEvent.VK_DELETE) {
                    celula.setValor(0);
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                    jogo.getNotifier().notify(JOGO_MODIFICADO, 0);
                    atualizarDotsPanel(dots, rascunhos, overlay);
                }
            }
        });

        campo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    rascunhos.clear();
                    atualizarDotsPanel(dots, rascunhos, overlay);
                }
            }
        });

        overlay.add(campo, 0);
        overlay.add(dots, 1);
        return overlay;
    }

    private JTextField criarCampoCelula(Celula celula) {
        JTextField campo = new JTextField();
        campo.setHorizontalAlignment(CENTER);
        campo.setFont(CELULA_FONTE);
        campo.setBorder(BorderFactory.createEmptyBorder());
        campo.setOpaque(false);
        campo.setBackground(new Color(0, 0, 0, 0));
        if (celula.isFixa()) {
            campo.setForeground(Color.DARK_GRAY);
            campo.setBackground(new Color(215, 215, 215));
            campo.setFocusable(false);
            campo.setEditable(false);
        } else {
            if(celula.getValor() != celula.getEsperado()) {
                campo.setForeground(Color.RED);
            } else {
                campo.setForeground(Color.BLACK);
            }
            campo.setDocument(new NumberDocument());
            if (JogoStatusEnum.COMPLETO.equals(jogo.getStatus())) {
                campo.setEditable(false);
                campo.setFocusable(false);
            }
            camposEditaveis.add(campo);
        }
        campo.setText(celula.getValor() != 0 ? String.valueOf(celula.getValor()) : "");
        return campo;
    }

    private void atualizarDotsPanel(RascunhoDotsPanel dots, Set<Integer> rascunhos, JPanel overlay) {
        dots.setRascunhos(rascunhos);
        overlay.revalidate();
        overlay.repaint();
    }
}