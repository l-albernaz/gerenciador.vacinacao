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
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    static Connection conexao = null;
    static Statement comando = null;
    static ResultSet resultado = null;

    public static void conexão(){
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
        conexão();
        Spark.port(8080);
        Spark.get("/vacinas/consultar", consultarVacinas());
        Spark.get("/vacinas/consultar/faixa_etaria/:faixa", consultarVacinasPorFaixaEtaria());
        Spark.get("/vacinas/consultar/idade_maior/:meses", consultarVacinasPorIdadeMaior());
        Spark.get("/vacinas/consultar/nao_aplicaveis/paciente/:id", consultarNaoAplicaveis());
        
        
        
    }

    private static Route consultarVacinas() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                int id;
                String vacina;
                String descricao;
                String limite_aplicacao;
                String publico_alvo;
                String result = "";
                String sql = "select * from vacina";
                try {
                    resultado = comando.executeQuery(sql);
                    while(resultado.next()){
                        id = resultado.getInt("id");
                        vacina = resultado.getString("vacina");
                        descricao = resultado.getString("descricao");
                        limite_aplicacao = resultado.getString("limite_aplicacao");
                        publico_alvo = resultado.getString("publico_alvo");
                        result += "\nID: " + id + "\nVacina: " + vacina + "\nDescrição: " + descricao + "\nlimite de Aplicação: " + limite_aplicacao + "\nPúblico Alvo: " + publico_alvo + "\n";
                        
                        
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                response.status(201);
                return result;
            }
        };
    }


    private static Route consultarVacinasPorFaixaEtaria() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                int id;
                String vacina;
                String descricao;
                String limite_aplicacao;
                String publico_alvo;
                String result = "";
                String faixaEtaria = request.params(":faixa").toUpperCase();
                String sql = "SELECT * FROM vacina WHERE publico_alvo = ?";
                
                try {
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, faixaEtaria);
                    resultado = stmt.executeQuery();

                    while(resultado.next()){
                        id = resultado.getInt("id");
                        vacina = resultado.getString("vacina");
                        descricao = resultado.getString("descricao");
                        limite_aplicacao = resultado.getString("limite_aplicacao");
                        publico_alvo = resultado.getString("publico_alvo");
                        result += "\nID: " + id + "\nVacina: " + vacina + "\nDescrição: " + descricao + 
                                  "\nLimite de Aplicação: " + limite_aplicacao + "\nPúblico Alvo: " + publico_alvo + "\n";
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    response.status(500);
                    return "Erro ao consultar vacinas para a faixa etária.";
                }

                if (result.isEmpty()) {
                    response.status(404);
                    return "Nenhuma vacina encontrada para a faixa etária: " + faixaEtaria;
                } else {
                    response.status(201);
                    return result;
                }
            }
        };
    }

    private static Route consultarVacinasPorIdadeMaior() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                int id;
                String vacina;
                String descricao;
                String limite_aplicacao;
                String publico_alvo;
                int idade_recomendada;
                String result = "";
                int idadeInformada = Integer.parseInt(request.params(":meses"));
    
                String sql = "SELECT v.id, v.vacina, v.descricao, v.limite_aplicacao, v.publico_alvo, d.idade_recomendada_aplicacao " +
                             "FROM vacina v " +
                             "JOIN dose d ON v.id = d.id_vacina " +
                             "WHERE d.idade_recomendada_aplicacao > ?";
    
                try {
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, idadeInformada);
                    resultado = stmt.executeQuery();
    
                    while (resultado.next()) {
                        id = resultado.getInt("id");
                        vacina = resultado.getString("vacina");
                        descricao = resultado.getString("descricao");
                        limite_aplicacao = resultado.getString("limite_aplicacao");
                        publico_alvo = resultado.getString("publico_alvo");
                        idade_recomendada = resultado.getInt("idade_recomendada_aplicacao");
    
                        result += "\nID: " + id + "\nVacina: " + vacina + "\nDescrição: " + descricao + 
                                  "\nLimite de Aplicação: " + limite_aplicacao + "\nPúblico Alvo: " + publico_alvo + 
                                  "\nIdade Recomendada (meses): " + idade_recomendada + "\n";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.status(500);
                    return "Erro ao consultar vacinas por idade.";
                }
    
                if (result.isEmpty()) {
                    response.status(404);
                    return "Nenhuma vacina encontrada para idade acima de " + idadeInformada + " meses.";
                } else {
                    response.status(201);
                    return result;
                }
            }
        };
    }

    private static Route consultarNaoAplicaveis() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                int id;
                String vacina;
                String descricao;
                int limite_aplicacao;
                String publico_alvo;
                String result = "";
        
                int pacienteId = Integer.parseInt(request.params(":id"));
                String sql = "SELECT v.id, v.vacina, v.descricao, v.limite_aplicacao, v.publico_alvo " +
                             "FROM vacina v " +
                             "JOIN paciente p ON p.id = " + pacienteId + " " +
                             "WHERE v.limite_aplicacao IS NOT NULL " +
                             "AND TIMESTAMPDIFF(MONTH, p.data_nascimento, CURDATE()) > v.limite_aplicacao";
        
                try {
                    resultado = comando.executeQuery(sql);
                    while (resultado.next()) {
                        id = resultado.getInt("id");
                        vacina = resultado.getString("vacina");
                        descricao = resultado.getString("descricao");
                        limite_aplicacao = resultado.getInt("limite_aplicacao");
                        publico_alvo = resultado.getString("publico_alvo");
        
                        result += "\nID: " + id + "\nVacina: " + vacina + "\nDescrição: " + descricao + 
                                  "\nLimite de Aplicação: " + limite_aplicacao + "\nPúblico Alvo: " + publico_alvo + "\n";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.status(500);
                    return "Erro ao consultar vacinas não aplicáveis.";
                }
        
                if (result.isEmpty()) {
                    response.status(404);
                    return "Nenhuma vacina não aplicável encontrada.";
                }
        
                response.status(200);
                return result;
            }
        };
    }
    
  
}
