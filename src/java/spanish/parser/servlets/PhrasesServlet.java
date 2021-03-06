/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import spanish.parser.beans.PalabraModificadaBean;
import spanish.parser.util.GraphViz;
import spanish.parser.util.ParserDB;


public class PhrasesServlet extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    
    public PhrasesServlet(){
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        final int idx = 1;
        int frase_orig_id = Integer.parseInt(request.getParameter("frase_original"));
        
        List<PalabraModificadaBean> palabras = ParserDB.getPalabrasModificadas(frase_orig_id);
        List<String> forms = new ArrayList<>();
        List<Integer> heads = new ArrayList<>();
        List<String> deprels = new ArrayList<>();
        for (PalabraModificadaBean palabra: palabras){
            forms.add(palabra.getForm());
            heads.add(palabra.getHead());
            deprels.add(palabra.getDeprel());
        }
        
        GraphViz.printGraphInFile(GraphViz.getGraphString(forms, heads, deprels), idx, getServletContext());
        String file = request.getContextPath() + "/ImgServlet?fileid=" + GraphViz.getJPGGraph(idx, getServletContext()) + "&guest=no";
        
        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException ex) {
            Logger.getLogger(ParseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        request.setAttribute("Palabras", palabras);
        request.setAttribute("Grafo", file);
        request.getRequestDispatcher("/JSP/PhraseView.jsp").forward(request, response);
    }
}
