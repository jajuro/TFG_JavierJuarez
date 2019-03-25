/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import spanish.parser.util.ParserDB;

public class AddUserServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    public AddUserServlet() {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession actual = request.getSession(true);
        if (actual.isNew() || actual == null || actual.getAttribute("Admin") == null) {
            request.getRequestDispatcher("/JSP/Login.jsp").forward(request, response);
            return;
        }
        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        boolean add = ParserDB.addUser(username, password);
        if (add){
            request.setAttribute("Name", username);
            request.getRequestDispatcher("/JSP/Added.jsp").forward(request, response);
        }
        else{
            request.setAttribute("errMessage", "El usuario ya existe");
            request.getRequestDispatcher("/JSP/AddUser.jsp").forward(request, response);
        }
    }
}
