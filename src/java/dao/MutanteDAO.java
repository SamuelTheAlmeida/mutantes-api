/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import beans.Habilidade;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import beans.Mutante;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SAMUEL
 */
public class MutanteDAO {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ResultSet resultSet2 = null;
    
    public List<Mutante> listar() throws Exception{
        List<Mutante> lista = new ArrayList<Mutante>();
        String result = "";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/mutantes;user=root;password=admin");
            PreparedStatement statement = connect
                    .prepareStatement("SELECT * from Mutante");
            
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Mutante m = new Mutante();
                m.setId(resultSet.getInt("id"));
                m.setNome(resultSet.getString("nome"));
                m.setIdFoto(resultSet.getString("idFoto"));
                lista.add(m);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }        
    }
    
    public List<Mutante> listarPorHabilidade(String habilidade) throws Exception{
        List<Mutante> lista = new ArrayList<Mutante>();
        String result = "";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/mutantes;user=root;password=admin");
            PreparedStatement statement = connect
                    .prepareStatement("SELECT M.id, M.nome, M.idFoto FROM Mutante M WHERE Id IN \n" +
"(SELECT idMutante FROM Habilidade WHERE id IN\n" +
"(SELECT Id FROM Habilidade WHERE UPPER(descricao) = UPPER(?)))");
            statement.setString(1, habilidade);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Mutante m = new Mutante();
                m.setId(resultSet.getInt("id"));
                m.setNome(resultSet.getString("nome"));
                m.setIdFoto(resultSet.getString("idFoto"));
                lista.add(m);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }        
    }
    
    public int cadastrar(Mutante mutante, List<String> habilidades) {
        int id = 0;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/mutantes;user=root;password=admin");
            String query = "INSERT INTO Mutante(nome, idUsuario) VALUES(?, ?)";
            PreparedStatement statement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, mutante.getNome());
            statement.setInt(2, 1);
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            statement.close();
            query = "INSERT INTO Habilidade(descricao, idMutante) VALUES(?, ?)";
            PreparedStatement st;
            for (int i = 0; i < habilidades.size(); i++) {
                st = connect.prepareStatement(query);
                st.setString(1, habilidades.get(i));
                st.setInt(2, id);
                st.executeUpdate();
                st.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
            return id;
        }                
    }
    
        
    public int remover(int id) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/mutantes;user=root;password=admin");
            PreparedStatement statement = connect
                    .prepareStatement("DELETE FROM Habilidade WHERE idMutante = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            statement = connect
                    .prepareStatement("DELETE FROM Mutante WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
            return 0;
        }                
    }
    
    public Mutante obter(int id) {
        Mutante mutante = new Mutante();
        String result = "";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/mutantes;user=root;password=admin");
            PreparedStatement statement = connect
                    .prepareStatement("SELECT id, nome, idFoto FROM Mutante WHERE id = " + id);
            
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                mutante.setId(resultSet.getInt("id"));
                mutante.setNome(resultSet.getString("nome"));
                mutante.setIdFoto(resultSet.getString("idFoto"));
            }
            List<Habilidade> habilidades = listarHabilidades(mutante.getId());
            mutante.setHabilidades(habilidades);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
            return mutante;
        }        
    }
    
    public List<Habilidade> listarHabilidades(int idMutante) throws Exception{
        List<Habilidade> lista = new ArrayList<Habilidade>();
        String result = "";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/mutantes;user=root;password=admin");
            PreparedStatement statement = connect
                    .prepareStatement("SELECT * from Habilidade WHERE idMutante = " + idMutante);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Habilidade h = new Habilidade();
                h.setId(resultSet.getInt("id"));
                h.setDescricao(resultSet.getString("descricao"));
                h.setMutante(new Mutante());
                lista.add(h);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }        
    }
     
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (resultSet2 != null) {
                resultSet2.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
}
