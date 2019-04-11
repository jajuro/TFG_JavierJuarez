/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import spanish.parser.beans.FraseOriginalBean;
import spanish.parser.beans.PalabraBean;
import spanish.parser.beans.PalabraModificadaBean;
import spanish.parser.beans.PalabraOriginalBean;
import spanish.parser.util.GraphViz;
import spanish.parser.util.Parser;
import spanish.parser.util.ParserDB;
import spanish.parser.util.ParserType;
import spanish.parser.util.Segmentador;
import spanish.parser.util.SpacyParser;
import spanish.parser.util.UDPipeParser;
import spanish.parser.util.URLTools;

@MultipartConfig
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public FileServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession actual = request.getSession(true);
        if (actual.isNew() || actual == null || (actual.getAttribute("Admin") == null && actual.getAttribute("User") == null)) {
            request.getRequestDispatcher("/JSP/Login.jsp").forward(request, response);
            return;
        }

        String txt = null;
        try {
            Part filePart = request.getPart("fichero");
            BufferedReader br = new BufferedReader(new InputStreamReader(filePart.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String s;

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            txt = sb.toString().replaceAll("\"", "");
            if (txt.length() == 0){
                request.getRequestDispatcher("/JSP/NoFile.jsp").forward(request, response);
                return;
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        String[] n_parsers = request.getParameterValues("parser");
        if (n_parsers == null) {
            request.getRequestDispatcher("/JSP/NoCheckbox.jsp").forward(request, response);
            return;
        }
        List<Parser> parsers = new ArrayList<>();
        request.setAttribute("udpipe_parsed", "no");
        request.setAttribute("spacy_parsed", "no");

        if (Arrays.asList(n_parsers).contains("udpipe")) {
            parsers.add(new UDPipeParser());
            request.setAttribute("udpipe_parsed", "si");
        }
        if (Arrays.asList(n_parsers).contains("spacy")) {
            parsers.add(new SpacyParser());
            request.setAttribute("spacy_parsed", "si");
        }

        URLTools tools = new URLTools(txt);
        Segmentador sgm;
        if (tools.isURL()) {
            sgm = new Segmentador(tools.extractFromURL());
        } else {
            sgm = new Segmentador(txt);
        }

        List<List<PalabraBean>> allWords = new ArrayList<>();
        List<String> frases = sgm.segmentInPhrases();
        List<String> Gforms = new ArrayList<>();
        List<Integer> Gheads = new ArrayList<>();
        List<String> Gdeprels = new ArrayList<>();
        List<String> graphs = new ArrayList<>();
        List<FraseOriginalBean> fraseOriginalBeans = new ArrayList<>();
        int g = 1;
        boolean varios;
        for (String frase : frases) {
            FraseOriginalBean FOBean = new FraseOriginalBean();
            FOBean.setFrase(frase);
            FOBean.setModificada(false);
            varios = false;
            for (Parser parser : parsers) {
                Gforms.clear();
                Gheads.clear();
                Gdeprels.clear();
                sgm.setTexto(parser.getConlluParse(frase, getServletContext()));
                if (parser.getType().equals(ParserType.SPACY)) {
                    FOBean.setSpacy(true);
                }
                if (parser.getType().equals(ParserType.UDPIPE)) {
                    FOBean.setUdpipe(true);
                }
                List<PalabraBean> listaPalabras = parser.getWordsFromPhrase(sgm.getTexto());
                List<PalabraBean> listaMods = new ArrayList<>();
                fraseOriginalBeans.add(FOBean);
                if (!varios) {
                    ParserDB.insertFraseOriginal(FOBean);
                    FOBean.setFrase_orig_id(ParserDB.getFraseID(FOBean));
                    varios = true;
                } else {
                    ParserDB.updateParsersFraseOriginal(FOBean);
                }
                for (PalabraBean pb : listaPalabras) {
                    PalabraOriginalBean POBean = processPalabraOriginal(pb, FOBean);
                    PalabraModificadaBean PMBean = processPalabraModificada(POBean);
                    listaMods.add(PMBean);
                    Gforms.add(pb.getForm());
                    Gdeprels.add(pb.getDeprel());
                    Gheads.add(pb.getHead());
                }
                allWords.add(listaMods);
                GraphViz.deleteFile(g, getServletContext());
                String graphString = GraphViz.getGraphString(Gforms, Gheads, Gdeprels);
                GraphViz.printGraphInFile(graphString, g, getServletContext());
                String file = GraphViz.getJPGGraph(g, getServletContext());
                graphs.add(file);
                g++;
            }
        }
        try {
            TimeUnit.SECONDS.sleep(9);
        } catch (InterruptedException ex) {
            Logger.getLogger(ParseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("AllWords", allWords);
        request.setAttribute("Grafos", graphs);
        request.setAttribute("Phrases", fraseOriginalBeans);
        request.getRequestDispatcher("/JSP/Parsed.jsp").forward(request, response);
    }

    private PalabraOriginalBean processPalabraOriginal(PalabraBean palabraBean, FraseOriginalBean FOBean) {
        PalabraOriginalBean POBean = (PalabraOriginalBean) palabraBean;
        POBean.setFrase_orig_id(ParserDB.getFraseID(FOBean));
        ParserDB.insertPalabraOriginal(POBean);
        return POBean;
    }

    private PalabraModificadaBean processPalabraModificada(PalabraOriginalBean POBean) {
        PalabraModificadaBean PMBean = new PalabraModificadaBean();
        PMBean.setId(POBean.getId());
        PMBean.setForm(POBean.getForm());
        PMBean.setLemma(POBean.getLemma());
        PMBean.setUpos(POBean.getUpos());
        PMBean.setXpos(POBean.getXpos());
        PMBean.setFeats(POBean.getFeats());
        PMBean.setHead(POBean.getHead());
        PMBean.setDeprel(POBean.getDeprel());
        PMBean.setDeps(POBean.getDeps());
        PMBean.setMisc(POBean.getMisc());
        PMBean.setFrase_orig_id(POBean.getFrase_orig_id());
        PMBean.setTabla_orig_id(ParserDB.getPalabraId(POBean));
        ParserDB.insertPalabraModificada(PMBean);
        return PMBean;
    }
}
