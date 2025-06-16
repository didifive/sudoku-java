package br.dev.zancanela.sudoku;

import br.dev.zancanela.sudoku.ui.custom.frame.InicioFrame;

import javax.swing.*;

public class SudokuMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InicioFrame().setVisible(true));
    }
}
