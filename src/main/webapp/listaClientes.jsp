<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/styles.css">
    <title>Lista de Clientes</title>
</head>
<body>
    <div class="container">
        <h1>Lista de Clientes</h1>

        <div class="header-actions">
            <a class="btn btn-primary" href="clientes?acao=criar">Cadastrar cliente</a>
        </div>

        <div>
            <c:choose>
                <c:when test="${empty clientes}">
                    <p>Nenhum cliente cadastrado. Clique em "Cadastrar cliente" para adicionar.</p>
                </c:when>

                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>Email</th>
                                <th>Telefone</th>
                                <th>CPF</th>
                                <th>Endereço</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cliente" items="${clientes}">
                                <tr>
                                    <td>${cliente.id}</td>
                                    <td>${cliente.nome}</td>
                                    <td>${cliente.email}</td>
                                    <td>${cliente.telefone}</td>
                                    <td>${cliente.cpf}</td>
                                    <td>${cliente.endereco}</td>
                                    <td class="actions">
                                        <a class="btn btn-edit" href="clientes?acao=editar&id=${cliente.id}">Editar</a>
                                        <a class="btn btn-delete" href="clientes?acao=deletar&id=${cliente.id}"
                                           onclick="return confirm('Tem certeza que deseja excluir esse cliente?');">
                                           Deletar
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
