/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import spanish.parser.beans.FraseOriginalBean;
import spanish.parser.beans.PalabraModificadaBean;
import spanish.parser.util.ParserDB;

public class HistorialServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public HistorialServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParserDB.conectar();
        List<FraseOriginalBean> listFO = ParserDB.getFrasesOriginales();

        request.setAttribute("Frases", listFO);
        request.getRequestDispatcher("/JSP/NonUser.jsp").forward(request, response);
    }
}
