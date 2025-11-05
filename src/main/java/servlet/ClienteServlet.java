package servlet;

import dao.ClienteDAO;
import model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private int id = 1;

    private final CLienteDAO clienteDAO = new ClienteDAO();

    // Lista dinâmica em memória
    private final List<Cliente> clientes = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if (acao == null) {
            acao = "listar";
        }

        switch (acao) {
            case "listar":
                listarClientes(req, resp);
                break;
            case "criar":
                req.getRequestDispatcher("/criarCliente.jsp").forward(req, resp);
                break;
            case "deletar":
                deletarCliente(req, resp); // mantém lógica original
                break;
            case "editar":
                editarCliente(req, resp); // mantém lógica original
                break;
            default:
                listarClientes(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if (acao == null) {
            acao = "inserir";
        }

        switch (acao) {
            case "inserir":
                inserirCliente(req, resp);
                break;
            case "atualizar":
                atualizarCliente(req, resp);
                break;
        }
    }

    private void listarClientes(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Busca no banco
            List<Cliente> clienteDB = clienteDAO.listarTodos();

            // Junta com os da lista (em memória)
            List<Cliente> todosClientes = new ArrayList<>(clienteDB);
            todosClientes.addAll(clientes);

            req.setAttribute("clientes", todosClientes);
            req.getRequestDispatcher("/listaClientes.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar clientes", e);
        }
    }

    private void inserirCliente(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String telefone = req.getParameter("telefone");
        String cpf = req.getParameter("cpf");
        String endereco = req.getParameter("endereco");

        Cliente cliente = new Cliente(id, nome, email, telefone, cpf, endereco);

        try {
            // ✅ salva no banco
            clienteDAO.inserir(cliente);

            // também adiciona na lista (mantendo comportamento original)
            id++;
            clientes.add(cliente);

            // redireciona para listagem
            resp.sendRedirect("/clientes?acao=listar");

        } catch (SQLException e) {
            throw new ServletException("Erro ao inserir cliente no banco", e);
        }
    }

    private void deletarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idCliente = req.getParameter("id");

        int id = Integer.parseInt(idCliente);

        // Remove cliente da lista em memória (mantido)
        clientes.removeIf(cliente -> cliente.getId() == id);

        resp.sendRedirect("/clientes?acao=listar");
    }

    private void editarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String idCliente = req.getParameter("id");
        int id = Integer.parseInt(idCliente);

        // Busca cliente na lista (mantido)
        Cliente cliente = clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (cliente == null) {
            resp.sendRedirect("/clientes?acao=listar");
            return;
        }

        req.setAttribute("cliente", cliente);
        req.getRequestDispatcher("/criarCliente.jsp").forward(req, resp);
    }

    private void atualizarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idCliente = req.getParameter("id");
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String telefone = req.getParameter("telefone");
        String cpf = req.getParameter("cpf");
        String endereco = req.getParameter("endereco");

        int id = Integer.parseInt(idCliente);

        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == id) {
                Cliente atualizado = new Cliente(id, nome, email, telefone, cpf, endereco);
                clientes.set(i, atualizado);
                break;
            }
        }

        resp.sendRedirect("/clientes?acao=listar");
    }
}
