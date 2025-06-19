package br.dev.zancanela.sudoku.repository;

import br.dev.zancanela.sudoku.util.DatabaseUtil;
import br.dev.zancanela.sudoku.domain.model.Jogador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorRepository {
    public JogadorRepository() {
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS jogador (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL UNIQUE)";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(final Jogador jogador) {
        String sql = "INSERT INTO jogador(id, nome) VALUES(?, ?) ON CONFLICT(id) DO UPDATE SET nome = excluded.nome";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (jogador.getId() != null) {
                pstmt.setLong(1, jogador.getId());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, jogador.getNome());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Jogador> listarTodos() {
        List<Jogador> jogadores = new ArrayList<>();
        String sql = "SELECT id, nome FROM jogador";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                jogadores.add(new Jogador(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jogadores;
    }
}