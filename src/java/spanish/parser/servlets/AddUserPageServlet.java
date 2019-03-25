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

public class AddUserPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public AddUserPageServlet(){
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession actual = request.getSession(true);
        if (actual.isNew() || actual == null || actual.getAttribute("Admin") == null) {
            request.getRequestDispatcher("/JSP/Login.jsp").forward(request, response);
            return;
        }
        
        request.getRequestDispatcher("/JSP/AddUser.jsp").forward(request, response);
    }
}
