package br.dev.zancanela.sudoku.ui.custom.panel;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;
import br.dev.zancanela.sudoku.domain.event.JogoEventListener;
import br.dev.zancanela.sudoku.domain.model.Jogo;

import javax.swing.*;

import java.awt.*;

import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.ADICIONAR_ERRO_AO_JOGO;

public class TituloTabuleiroPanel extends JPanel implements JogoEventListener {
    private final JLabel errosLabel = new JLabel();

    private final JButton btnRascunho;

    public TituloTabuleiroPanel(Jogo jogo) {
        jogo.addJogoEventListener(ADICIONAR_ERRO_AO_JOGO, this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.add(new JLabel("Jogador: " + jogo.getJogador().getNome()));
        infoPanel.add(Box.createHorizontalStrut(40));
        infoPanel.add(new JLabel("Iniciado em: " + jogo.getDataHoraInicio()));

        infoPanel.setAlignmentX(CENTER_ALIGNMENT);
        add(infoPanel);
        add(Box.createVerticalStrut(15));

        errosLabel.setText("Erros: " + jogo.getErros());
        errosLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(errosLabel);
        add(Box.createVerticalStrut(15));

        this.btnRascunho = new JButton("Modo Rascunho: OFF");
        Color corFundoPadrao = btnRascunho.getBackground();
        Color corTextoPadrao = btnRascunho.getForeground();
        btnRascunho.addActionListener(e -> {
            jogo.setModoRascunho(!jogo.isModoRascunho());
            btnRascunho.setText("Modo Rascunho: " + (jogo.isModoRascunho() ? "ON" : "OFF"));
            if (jogo.isModoRascunho()) {
                btnRascunho.setBackground(Color.RED);
                btnRascunho.setForeground(Color.WHITE);
                btnRascunho.setToolTipText("Clique para desativar o modo rascunho");
            } else {
                btnRascunho.setBackground(corFundoPadrao);
                btnRascunho.setForeground(corTextoPadrao);
                btnRascunho.setToolTipText("Clique para ativar o modo rascunho");
            }
        });
        btnRascunho.setAlignmentX(CENTER_ALIGNMENT);
        add(btnRascunho);
        add(Box.createVerticalStrut(20));
    }

    @Override
    public void onEvent(JogoEventEnum eventType, int novoValor) {
        if (eventType == ADICIONAR_ERRO_AO_JOGO) {
            errosLabel.setText("Erros: " + novoValor);
        }
    }
}