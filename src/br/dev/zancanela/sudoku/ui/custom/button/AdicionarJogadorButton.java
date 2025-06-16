package br.dev.zancanela.sudoku.ui.custom.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdicionarJogadorButton extends JButton {

    public AdicionarJogadorButton(final ActionListener actionListener) {
        setText("Adicionar Jogador");
        setToolTipText("Clique para adicionar o jogador");
        setFocusable(false);
        setBorderPainted(true);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));
        setForeground(new Color(0, 0, 0));
        addActionListener(actionListener);
    }
}
