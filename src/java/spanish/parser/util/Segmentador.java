/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Segmentador {

    private String texto;

    public Segmentador(String txt) {
        this.texto = txt;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<String> segmentInPhrases() {
        List<String> lista = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(this.getTexto(), ".\n\r");
        while (st.hasMoreTokens()) {
            lista.add(st.nextToken().trim());
        }

        return lista;
    }
}
