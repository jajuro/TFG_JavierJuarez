/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import spanish.parser.beans.PalabraBean;
import spanish.parser.beans.PalabraOriginalBean;

public class SpacyParser extends Parser{
    
    public SpacyParser(){
        super(ParserType.SPACY);
    }

    @Override
    public String getConlluParse(String text) {
        String[] cmd = {"python", MyProperties.getProperty("pythonFile"), text};
        StringBuilder res = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput
                    = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError
                    = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String output;
            while ((output = stdInput.readLine()) != null) {
                res.append(output).append('\n');
            }

            while ((output = stdError.readLine()) != null) {
                res.append(output).append('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return res.toString();
    }

    @Override
    public List<PalabraBean> getWordsFromPhrase(String text) {
        List<PalabraBean> lista = new ArrayList<>();
        String delim_linea = "\n";
        String delim_campo = "\t";

        StringTokenizer st1 = new StringTokenizer(text, delim_linea);
        while (st1.hasMoreTokens()) {
            StringTokenizer st2 = new StringTokenizer(st1.nextToken().trim(), delim_campo);
            PalabraBean palabra = new PalabraOriginalBean();
            palabra.setId(Integer.parseInt(st2.nextToken()));
            palabra.setForm(st2.nextToken());
            palabra.setLemma(st2.nextToken());
            palabra.setUpos(st2.nextToken());
            palabra.setXpos(st2.nextToken());
            palabra.setFeats(st2.nextToken());
            palabra.setHead(Integer.parseInt(st2.nextToken()));
            palabra.setDeprel(st2.nextToken());
            palabra.setDeps(st2.nextToken());
            palabra.setMisc(st2.nextToken());
            lista.add(palabra);
        }
        return lista;
    }
    
    
}
