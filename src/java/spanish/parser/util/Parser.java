/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import java.util.List;
import javax.servlet.ServletContext;
import spanish.parser.beans.PalabraBean;

public abstract class Parser {
    
    private ParserType type;
    
    public Parser(ParserType type){
        this.type = type;
    }
    
    public abstract String getConlluParse(String text, ServletContext context);
    
    public abstract List<PalabraBean> getWordsFromPhrase(String text);

    public ParserType getType() {
        return type;
    }

    public void setType(ParserType type) {
        this.type = type;
    }
}
