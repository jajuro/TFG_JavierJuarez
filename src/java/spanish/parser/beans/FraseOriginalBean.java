/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.beans;

import java.io.Serializable;

public class FraseOriginalBean implements Serializable {
    
    private int frase_orig_id;
    private String frase;
    private boolean modificada, spacy, udpipe; 
    
    public int getFrase_orig_id() {
        return frase_orig_id;
    }

    public void setFrase_orig_id(int frase_orig_id) {
        this.frase_orig_id = frase_orig_id;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public boolean isModificada() {
        return modificada;
    }

    public void setModificada(boolean modificada) {
        this.modificada = modificada;
    }

    public boolean isSpacy() {
        return spacy;
    }

    public void setSpacy(boolean spacy) {
        this.spacy = spacy;
    }

    public boolean isUdpipe() {
        return udpipe;
    }

    public void setUdpipe(boolean udpipe) {
        this.udpipe = udpipe;
    }
}
