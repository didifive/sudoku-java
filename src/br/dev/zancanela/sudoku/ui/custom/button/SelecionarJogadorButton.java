package br.dev.zancanela.sudoku.ui.custom.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelecionarJogadorButton extends JButton {

    public SelecionarJogadorButton(final ActionListener actionListener) {
        setText("Selecionar");
        setToolTipText("Clique para selecionar o jogador");
        setFocusable(false);
        setBorderPainted(true);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));
        setForeground(new Color(0, 0, 0));
        addActionListener(actionListener);
    }
}
