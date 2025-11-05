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

    private final ClienteDAO clienteDAO = new ClienteDAO();

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

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            request.setAttribute("clientes", clientes);
            request.getRequestDispatcher("/lista-clientes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar clientes", e);
        }
    }

    private void mostrarFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/formulario-cliente.jsp").forward(request, response);
    }

    private void inserirCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String cpf = request.getParameter("cpf");
        String endereco = request.getParameter("endereco");

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);
        cliente.setCpf(cpf);
        cliente.setEndereco(endereco);

        try {
            clienteDAO.inserir(cliente);
            response.sendRedirect(request.getContextPath() + "/clientes?acao=listar");
        } catch (SQLException e) {
            throw new ServletException("Erro ao inserir cliente", e);
        }
    }

    private void deletarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idCliente = req.getParameter("id");

        int id = Integer.parseInt(idCliente);

        clienteDAO.deletar(id);

        resp.sendRedirect("/clientes?acao=listar");
    }

    private void editarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String idCliente = req.getParameter("id");
        int id = Integer.parseInt(idCliente);

        Cliente cliente = clienteDAO.buscarPorId(id);

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

        Cliente cliente = new Cliente(nome, email, telefone, cpf, endereco);

        clienteDAO.atualizar(cliente);

        resp.sendRedirect("/clientes?acao=listar");
    }
}
