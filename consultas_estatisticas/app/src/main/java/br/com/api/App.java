package br.com.api;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import spark.Spark;
import spark.Request;
import spark.Response;
import spark.Route;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class App {

    static Connection conexao = null;
    static Statement comando = null;
    static ResultSet resultado = null;

    public static void conexao(){
        String url = "jdbc:mysql://localhost:3306/vacinacao";
        String usuario = "root";
        String senha = "Ar08tr10.";
        try {
            conexao = DriverManager.getConnection(url, usuario, senha);
            comando = conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        conexao();
        Spark.port(8080);
        Spark.get("/estatisticas/imunizacoes/paciente/:id", qtdeVacinasAplicadas());
        Spark.get("/estatisticas/proximas_imunizacoes/paciente/:id", qtdeVacinasProximoMes());
        Spark.get("/estatisticas/imunizacoes_atrasadas/paciente/:id", qtdeVacinasAtrasadas());
        Spark.get("/estatisticas/imunizacoes/idade_maior/:meses", qtdeVacinasPorIdade());
        Spark.get("/estatisticas/vacinas/nao_aplicaveis/paciente/:id", qtdeVacinasNaoAplicaveis());
    }

    private static Route qtdeVacinasAplicadas() {
        return (request, response) -> {
            String sql = "SELECT COUNT(*) AS total FROM imunizacoes WHERE id_paciente = ?";
            try {
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(request.params(":id")));
                resultado = stmt.executeQuery();
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            } catch (SQLException e) {
                response.status(500);
                return "Erro ao consultar quantidade de vacinas aplicadas.";
            }
            return 0;
        };
    }

    private static Route qtdeVacinasProximoMes() {
        return (request, response) -> {
            int pacienteId = Integer.parseInt(request.params(":id"));
            String sql = "SELECT COUNT(*) AS total FROM imunizacoes i "
                       + "JOIN dose d ON i.id_dose = d.id "
                       + "JOIN vacina v ON d.id_vacina = v.id "
                       + "WHERE i.id_paciente = ? "
                       + "AND MONTH(i.data_aplicacao) = MONTH(DATE_ADD(CURDATE(), INTERVAL 1 MONTH))";
            try {
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, pacienteId);
                ResultSet resultado = stmt.executeQuery();
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
                response.status(404);  // Caso não encontre vacinas para o próximo mês
                return "Paciente não encontrado ou sem vacinas previstas para o próximo mês.";
            } catch (SQLException e) {
                response.status(500);
                return "Erro ao consultar vacinas do próximo mês.";
            }
        };
    }

    private static Route qtdeVacinasAtrasadas() { 
        return (request, response) -> {
            int pacienteId = Integer.parseInt(request.params(":id"));
            String sql = "SELECT COUNT(*) AS total FROM dose d "
                       + "JOIN vacina v ON d.id_vacina = v.id "
                       + "WHERE v.publico_alvo = (SELECT sexo FROM paciente WHERE id = ?) "
                       + "AND d.idade_recomendada_aplicacao <= (SELECT TIMESTAMPDIFF(YEAR, data_nascimento, CURDATE()) FROM paciente WHERE id = ?)";
            try {
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, pacienteId);
                stmt.setInt(2, pacienteId);
                ResultSet resultado = stmt.executeQuery();
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
                response.status(404);  // Não encontrou vacinas devidas
                return "Paciente não encontrado ou sem vacinas devidas.";
            } catch (SQLException e) {
                response.status(500);
                return "Erro ao consultar vacinas devidas.";
            }
        };
    }
    
    private static Route qtdeVacinasPorIdade() {
        return (request, response) -> {
            String sql = "SELECT COUNT(*) AS total FROM vacinas WHERE limite_aplicacao > ?";
            try {
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(request.params(":meses")));
                resultado = stmt.executeQuery();
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            } catch (SQLException e) {
                response.status(500);
                return "Erro ao consultar vacinas por idade.";
            }
            return 0;
        };
    }

    private static Route qtdeVacinasNaoAplicaveis() {
        return (request, response) -> {
            String sql = "SELECT COUNT(*) AS total FROM vacinas WHERE limite_aplicacao < (SELECT idade FROM paciente WHERE id = ?)";
            try {
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(request.params(":id")));
                resultado = stmt.executeQuery();
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            } catch (SQLException e) {
                response.status(500);
                return "Erro ao consultar vacinas não aplicáveis.";
            }
            return 0;
        };
    }
}
