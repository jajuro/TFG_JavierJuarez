/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class MyProperties {

    private static final String CONFIG_FILE = "C:\\Users\\TRJJUR\\Desktop\\TFGv3\\web\\WEB-INF\\parser.properties";
    private static HashMap properties;

    static {
        try {
            FileInputStream inputStream = new FileInputStream(CONFIG_FILE);

            Properties temp = new Properties();
            temp.load(inputStream);
            inputStream.close();
            
            properties = new HashMap(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private MyProperties(){}
    
    public static String getProperty(String key){
        return (String) properties.get(key);
    }
}
