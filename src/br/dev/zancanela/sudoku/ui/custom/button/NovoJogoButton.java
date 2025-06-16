package br.dev.zancanela.sudoku.ui.custom.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NovoJogoButton extends JButton {

    public NovoJogoButton(final ActionListener actionListener) {
        setText("Novo Jogo");
        setToolTipText("Clique para iniciar um novo jogo");
        setFocusable(false);
        setBorderPainted(true);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));
        setForeground(new Color(0, 0, 0));
        addActionListener(actionListener);
    }
}
