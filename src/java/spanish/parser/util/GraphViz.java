/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

public class GraphViz {

    public static String getGraphString(
            List<String> forms, List<Integer> heads, List<String> deprels) {
        StringBuilder graph = new StringBuilder();
        graph.append("digraph G" + '\n').append("{" + '\n');

        for (String form : forms) {
            graph.append('\t').append("\"").append(form).append("\"").append(";" + '\n');
        }
        graph.append('\t').append("ROOT").append(";" + '\n');
        graph.append('\n');
        for (int i = 0; i < forms.size(); i++) {
            if (heads.get(i).equals(0)) {
                graph.append('\t').append("\"").append(forms.get(i)).append("\"").append(" -> ")
                        .append("ROOT").append(" [label=")
                        .append("\"").append(deprels.get(i)).append("\"").append("];").append('\n');
            } else {
                graph.append('\t').append("\"").append(forms.get(i)).append("\"").append(" -> ")
                        .append("\"").append(forms.get(heads.get(i) - 1))
                        .append("\"").append(" [label=")
                        .append("\"").append(deprels.get(i)).append("\"").append("];").append('\n');
            }
        }
        graph.append("}");
        return graph.toString();
    }

    public static void printGraphInFile(String graph, int g) {
        final String webPath = MyProperties.getProperty("webPath");

        try {
            BufferedWriter writer
                    = new BufferedWriter(new FileWriter(webPath + "\\graph" + g + ".txt"));
            writer.write(graph);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Segmentador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getJPGGraph(int g, ServletContext context) {
        final String dotPath = MyProperties.getProperty("dotPath");

        try {
            String fileInputPath = context.getRealPath("/WEB-INF") + "\\graph" + g + ".txt";
            String fileOutputPath = context.getRealPath("/WEB-INF") + "\\graph" + g + ".jpg";
            String tParam = "-Tjpg";
            String tOParam = "-o";

            String[] cmd = new String[5];
            cmd[0] = context.getRealPath("/WEB-INF") + dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;

            Runtime rt = Runtime.getRuntime();

            rt.exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(Segmentador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "graph" + g + ".jpg";
    }

    public static void deleteFile(int g) {
        final String webPath = MyProperties.getProperty("webPath");

        File file1 = new File(webPath + "\\graph" + g + ".jpg");
        File file2 = new File(webPath + "\\graph" + g + ".txt");
        file2.delete();
        file1.delete();
    }
}
