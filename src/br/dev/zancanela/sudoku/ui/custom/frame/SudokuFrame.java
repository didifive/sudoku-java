package br.dev.zancanela.sudoku.ui.custom.frame;

import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.ui.custom.panel.MatrizPanel;
import br.dev.zancanela.sudoku.ui.custom.panel.RodapeTabuleiroPanel;
import br.dev.zancanela.sudoku.ui.custom.panel.TituloTabuleiroPanel;

import javax.swing.*;

public class SudokuFrame extends JFrame {

    public SudokuFrame(Jogo jogo) {
        MatrizPanel matrizPanel = new MatrizPanel(jogo);

        setTitle("Sudoku Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        add(new TituloTabuleiroPanel(jogo));
        add(matrizPanel);
        add(new RodapeTabuleiroPanel(jogo));

        setResizable(false);
        setVisible(true);
    }


}