package br.dev.zancanela.sudoku.util;

import br.dev.zancanela.sudoku.domain.model.Celula;
import br.dev.zancanela.sudoku.domain.model.Tabuleiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TabuleiroUtil {

    private static final Random RANDOM = new Random();

    public static Tabuleiro iniciaNovoTabuleiro() {
        List<List<Celula>> celulas = new ArrayList<>();
        int[][] grid = new int[9][9];

        // Função auxiliar para verificar se é seguro colocar um número
        BiPredicate<Integer, Integer> isSafe = (row, col) -> {
            int num = grid[row][col];
            for (int i = 0; i < 9; i++) {
                if (i != col && grid[row][i] == num) return false;
                if (i != row && grid[i][col] == num) return false;
            }
            int boxRow = (row / 3) * 3, boxCol = (col / 3) * 3;
            for (int i = boxRow; i < boxRow + 3; i++)
                for (int j = boxCol; j < boxCol + 3; j++)
                    if ((i != row || j != col) && grid[i][j] == num) return false;
            return true;
        };

        // Função recursiva para preencher o tabuleiro
        Function<int[], Boolean> fill = new Function<>() {
            public Boolean apply(int[] pos) {
                if (pos[0] == 9) return true;
                int row = pos[0], col = pos[1];
                List<Integer> nums = IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList());
                Collections.shuffle(nums);
                for (int num : nums) {
                    grid[row][col] = num;
                    if (isSafe.test(row, col)) {
                        int nextCol = (col + 1) % 9;
                        int nextRow = nextCol == 0 ? row + 1 : row;
                        if (apply(new int[]{nextRow, nextCol})) return true;
                    }
                    grid[row][col] = 0;
                }
                return false;
            }
        };
        fill.apply(new int[]{0, 0});

        boolean[][] fixa = new boolean[9][9];
        int fixas = 25; // quantidade de células fixas
        while (fixas > 0) {
            int i = RANDOM.nextInt(9), j = RANDOM.nextInt(9);
            if (!fixa[i][j]) {
                fixa[i][j] = true;
                fixas--;
            }
        }

        for (int i = 0; i < 9; i++) {
            List<Celula> linha = new java.util.ArrayList<>();
            for (int j = 0; j < 9; j++) {
                boolean ehFixa = fixa[i][j];
                int esperado = grid[i][j];
                int valor = fixa[i][j] ? esperado : 0;
                linha.add(new Celula(valor, ehFixa, esperado));
            }
            celulas.add(linha);
        }

        return new Tabuleiro(celulas);

    }

    private TabuleiroUtil() {
        throw new UnsupportedOperationException("Esta classe utilitária não pode ser instanciada.");
    }
}
