package dao;

import dataBase.DataBaseConnector;
import model.Cliente;

import javax.crypto.Cipher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, email, telefone, cpf, endereco) VALUES(?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DataBaseConnector.getConnection();
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());
            stmt.setString(5, cliente.getEndereco());

            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            DataBaseConnector.closeConnection(connection);

        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM cliente ORDER BY id";
        List<Cliente> clientes = new ArrayList<>();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet res = null;

        try {
            connection = DataBaseConnector.getConnection();
            stmt = connection.prepareStatement(sql);
            res = stmt.executeQuery();

            while (res.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(res.getInt("id"));
                cliente.setNome(res.getString("nome"));
                cliente.setEmail(res.getString("email"));
                cliente.setTelefone(res.getString("telefone"));
                cliente.setCpf(res.getString("cpf"));
                cliente.setEndereco(res.getString("endereco"));

                clientes.add(cliente);
            }

            return clientes;

        } finally {
            if (res != null) res.close();
            if (stmt != null) stmt.close();
            if (connection != null) DataBaseConnector.closeConnection(connection);
        }
    }


}
