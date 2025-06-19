package br.dev.zancanela.sudoku.ui.custom.panel;

import br.dev.zancanela.sudoku.domain.enums.JogoStatusEnum;
import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.ui.custom.frame.SelecionaJogoFrame;

import javax.swing.*;
import java.awt.*;

public class RodapeTabuleiroPanel extends JPanel {
    private final JButton btnEncerrar = new JButton("Finalizar Jogo");
    private final JLabel parabensLabel = new JLabel("Parabéns! Sudoku concluído.");

    public RodapeTabuleiroPanel(Jogo jogo) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(600, 200));

        JPanel botoesPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        botoesPanel.setPreferredSize(new Dimension(400, 40));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            new SelecionaJogoFrame(jogo.getJogador()).setVisible(true);
        });

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> jogo.limparCelulasNaoFixas());

        btnEncerrar.addActionListener(e -> {
            if (jogo.verificarTabuleiro()) {
                jogo.setStatusConcluido();
                btnEncerrar.setVisible(false);
                btnLimpar.setVisible(false);
                parabensLabel.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Ainda há erros ou células vazias.");
            }
        });

        botoesPanel.add(btnVoltar);
        botoesPanel.add(btnLimpar);
        botoesPanel.add(btnEncerrar);

        parabensLabel.setHorizontalAlignment(SwingConstants.CENTER);
        parabensLabel.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(botoesPanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(parabensLabel, gbc);

        if (JogoStatusEnum.COMPLETO.equals(jogo.getStatus())) {
            btnEncerrar.setVisible(false);
            parabensLabel.setVisible(true);
        }
    }
}