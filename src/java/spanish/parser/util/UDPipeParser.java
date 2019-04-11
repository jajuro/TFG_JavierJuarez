/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import cz.cuni.mff.ufal.udpipe.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import spanish.parser.beans.PalabraBean;
import spanish.parser.beans.PalabraOriginalBean;

public class UDPipeParser extends Parser{

    public UDPipeParser(){
        super(ParserType.UDPIPE);
    }
    
    @Override
    public String getConlluParse(String text, ServletContext context) {
        udpipe_java.setLibraryPath(System.getenv("GLASSFISH_HOME") + MyProperties.getProperty("dllPath"));
        String modelFile = MyProperties.getProperty("udModelPath");

        String input = "horizontal";
        String output = "conllu";
        Model model = Model.load(context.getRealPath("/WEB-INF") + modelFile);
        if (model == null) {
            System.out.println("Cannot load model from file '" + modelFile + "'");
            return null;
        }

        System.out.println("done\n");

        Pipeline pipeline = new Pipeline(model, input, Pipeline.getDEFAULT(), Pipeline.getDEFAULT(),
                output);
        ProcessingError error = new ProcessingError();

        String processed = pipeline.process(text, error);

        if (error.occurred()) {
            System.out.println("Cannot read input CoNLL-U: " + error.getMessage());
            return null;
        }

        model = null;
        System.out.println(processed);
        return processed;
    }

    @Override
    public List<PalabraBean> getWordsFromPhrase(String text) {
        List<PalabraBean> lista = new ArrayList<>();
        String delete = "# newdoc\n" + "# newpar\n" + "# sent_id = ";
        String delim_linea = "\n";
        String delim_campo = "\t";

        StringTokenizer st1 = new StringTokenizer(text.replaceAll(delete, ""), delim_linea);
        st1.nextToken();
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
