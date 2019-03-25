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
import spanish.parser.beans.PalabraModificadaBean;
import spanish.parser.util.ParserDB;

public class ModifiedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ModifiedServlet() {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession actual = request.getSession(true);
        if (actual.isNew() || actual == null || (actual.getAttribute("Admin") == null && actual.getAttribute("User") == null)) {
            request.getRequestDispatcher("/JSP/Login.jsp").forward(request, response);
            return;
        }

        int id = Integer.parseInt(request.getParameter("wordId"));
        String form = request.getParameter("wordForm");
        String lemma = request.getParameter("wordLemma");
        String upos = request.getParameter("wordUpos");
        String xpos = request.getParameter("wordXpos");
        String feats = request.getParameter("wordFeats");
        int head = Integer.parseInt(request.getParameter("wordHead"));
        String deprel = request.getParameter("wordDeprel");
        String deps = request.getParameter("wordDeps");
        String misc = request.getParameter("wordMisc");
        int frase_orig_id = Integer.parseInt(request.getParameter("frase_original"));
        int tabla_orig_id = Integer.parseInt(request.getParameter("tabla_original"));

        PalabraModificadaBean PMBean = new PalabraModificadaBean();
        PMBean.setId(id);
        PMBean.setForm(form);
        PMBean.setLemma(lemma);
        PMBean.setUpos(upos);
        PMBean.setXpos(xpos);
        PMBean.setFeats(feats);
        PMBean.setHead(head);
        PMBean.setDeprel(deprel);
        PMBean.setDeps(deps);
        PMBean.setMisc(misc);
        PMBean.setFrase_orig_id(frase_orig_id);
        PMBean.setTabla_orig_id(tabla_orig_id);

        
        ParserDB.insertPalabraModificada(PMBean);
        ParserDB.updateModificadaFraseOriginal(frase_orig_id);
        request.getRequestDispatcher("/JSP/ModifiedPage.jsp").forward(request, response);
    }
}
