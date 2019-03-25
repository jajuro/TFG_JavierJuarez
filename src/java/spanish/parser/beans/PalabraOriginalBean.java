/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.beans;

import java.io.Serializable;


public class PalabraOriginalBean extends PalabraBean implements Serializable {
    private int tabla_orig_id;

    public int getTabla_orig_id() {
        return tabla_orig_id;
    }

    public void setTabla_orig_id(int tabla_orig_id) {
        this.tabla_orig_id = tabla_orig_id;
    }    
}
