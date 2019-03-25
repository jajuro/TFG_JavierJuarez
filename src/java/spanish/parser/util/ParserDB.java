/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import spanish.parser.beans.FraseOriginalBean;
import spanish.parser.beans.PalabraModificadaBean;
import spanish.parser.beans.PalabraOriginalBean;
import spanish.parser.beans.UserBean;

/**
 *
 * @author Javier Juárez Rodríguez
 */
public class ParserDB {

    private static String dbURL = "jdbc:derby://localhost:1527/tfg-db;create=true;user=tfg;password=tfg";
    private static Connection conn = null;
    private static PreparedStatement pstmt = null;

    public static void conectar() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        String query1 = "create table frase_original ("
                + "frase_orig_id integer not null generated always as identity (start with 1, increment by 1),"
                + "frase varchar(800),"
                + "modificada boolean,"
                + "spacy boolean,"
                + "udpipe boolean,"
                + "primary key (frase_orig_id)"
                + ")";
        String query2 = "create table tabla_original ("
                + "tabla_orig_id integer not null generated always as identity (start with 1, increment by 1),"
                + "id integer,"
                + "form varchar(30),"
                + "lemma varchar(30),"
                + "upos varchar(10),"
                + "xpos varchar(10),"
                + "feats varchar(200),"
                + "head integer,"
                + "deprel varchar(10),"
                + "deps varchar(200),"
                + "misc varchar(30),"
                + "frase_orig_id integer not null references frase_original(frase_orig_id),"
                + "primary key (tabla_orig_id)"
                + ")";
        String query3 = "create table tabla_modificada ("
                + "tabla_mod_id integer not null generated always as identity (start with 1, increment by 1),"
                + "id integer,"
                + "form varchar(30),"
                + "lemma varchar(30),"
                + "upos varchar(10),"
                + "xpos varchar(10),"
                + "feats varchar(200),"
                + "head integer,"
                + "deprel varchar(10),"
                + "deps varchar(200),"
                + "misc varchar(30),"
                + "frase_orig_id integer not null references frase_original(frase_orig_id),"
                + "tabla_orig_id integer not null references tabla_original(tabla_orig_id),"
                + "primary key (tabla_mod_id)"
                + ")";
        String query4 = "create table usuarios ("
                + "username varchar(30) not null,"
                + "password varchar(30) not null,"
                + "role varchar(10) not null,"
                + "primary key (username)"
                + ")";
        String insertAdmin = "insert into usuarios(username, password, role) values(?,?,?)";
        try {
            pstmt = conn.prepareStatement(query1);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(query2);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(query3);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(query4);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(insertAdmin);
            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin");
            pstmt.setString(3, "Admin");
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void limpiarBD() {

        String query1 = "drop table frase_original";
        String query2 = "drop table tabla_original";
        String query3 = "drop table tabla_modificada";
        String query4 = "drop table usuarios";
        try {
            pstmt = conn.prepareStatement(query4);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(query3);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(query2);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(query1);
            pstmt.executeUpdate();
            pstmt.close();
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String authenticateUser(UserBean ub) {
        String userNameDB = "", passwordDB = "", roleDB = "";
        String query = "select username,password,role from usuarios";
        try {
            pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                userNameDB = result.getString("username");
                passwordDB = result.getString("password");
                roleDB = result.getString("role");

                if (ub.getUserName().equals(userNameDB) && 
                        ub.getPassword().equals(passwordDB) && roleDB.equals("Admin")) {
                    return "Admin_Role";
                } else if (ub.getUserName().equals(userNameDB) && 
                        ub.getPassword().equals(passwordDB) && roleDB.equals("User")) {
                    return "User_Role";
                }
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error de login";
    }

    public static boolean addUser(String username, String password) {
        String query = "insert into usuarios(username, password, role) values (?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, "User");
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<FraseOriginalBean> getFrasesOriginales() {
        List<FraseOriginalBean> lista = new ArrayList<>();
        String query = "select * from frase_original";

        try {
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                FraseOriginalBean FOBean = new FraseOriginalBean();
                FOBean.setFrase_orig_id(rs.getInt("frase_orig_id"));
                FOBean.setFrase(rs.getString("frase"));
                FOBean.setModificada(rs.getBoolean("modificada"));
                FOBean.setSpacy(rs.getBoolean("spacy"));
                FOBean.setUdpipe(rs.getBoolean("udpipe"));
                lista.add(FOBean);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<PalabraOriginalBean> getPalabrasOriginales(int frase_orig_id) {
        List<PalabraOriginalBean> lista = new ArrayList<>();
        String query = "select * from tabla_original where frase_orig_id=?";

        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, frase_orig_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PalabraOriginalBean POBean = new PalabraOriginalBean();
                POBean.setTabla_orig_id(rs.getInt("tabla_orig_id"));
                POBean.setId(rs.getInt("id"));
                POBean.setForm(rs.getString("form"));
                POBean.setLemma(rs.getString("lemma"));
                POBean.setUpos(rs.getString("upos"));
                POBean.setXpos(rs.getString("xpos"));
                POBean.setFeats(rs.getString("feats"));
                POBean.setHead(rs.getInt("head"));
                POBean.setDeprel(rs.getString("deprel"));
                POBean.setDeps(rs.getString("deps"));
                POBean.setMisc(rs.getString("misc"));
                POBean.setFrase_orig_id(rs.getInt("frase_orig_id"));
                lista.add(POBean);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<PalabraModificadaBean> getPalabrasModificadas(int frase_orig_id) {
        List<PalabraModificadaBean> lista = new ArrayList<>();
        String query = "select * from tabla_modificada where frase_orig_id=?";
        String query2 = "select * from tabla_modificada t1, tabla_modificada t2 "
                + "where t1.tabla_orig_id=t2.tabla_orig_id "
                + "and t1.frase_orig_id=t2.frase_orig_id "
                + "and t1.tabla_mod_id <> t2.tabla_mod_id "
                + "and t1.frase_orig_id=? "
                + "and t1.tabla_orig_id=?";

        int count = ParserDB.getPalabrasOriginales(frase_orig_id).size();
        try {
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setInt(1, frase_orig_id);;
            ResultSet rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next() && i < count) {
                PreparedStatement stmt = conn.prepareStatement(query2, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.setInt(1, frase_orig_id);
                stmt.setInt(2, rs.getInt("tabla_orig_id"));
                ResultSet rs2 = stmt.executeQuery();
                if (rs2.next()) {
                    rs2.last();
                    rs.updateInt("id", rs2.getInt("id"));
                    rs.updateString("form", rs2.getString("form"));
                    rs.updateString("lemma", rs2.getString("lemma"));
                    rs.updateString("upos", rs2.getString("upos"));
                    rs.updateString("xpos", rs2.getString("xpos"));
                    rs.updateString("feats", rs2.getString("feats"));
                    rs.updateInt("head", rs2.getInt("head"));
                    rs.updateString("deprel", rs2.getString("deprel"));
                    rs.updateString("deps", rs2.getString("deps"));
                    rs.updateString("misc", rs2.getString("misc"));
                    rs.updateRow();
                }
                PalabraModificadaBean PMBean = new PalabraModificadaBean();
                PMBean.setTabla_mod_id(rs.getInt("tabla_mod_id"));
                PMBean.setId(rs.getInt("id"));
                PMBean.setForm(rs.getString("form"));
                PMBean.setLemma(rs.getString("lemma"));
                PMBean.setUpos(rs.getString("upos"));
                PMBean.setXpos(rs.getString("xpos"));
                PMBean.setFeats(rs.getString("feats"));
                PMBean.setHead(rs.getInt("head"));
                PMBean.setDeprel(rs.getString("deprel"));
                PMBean.setDeps(rs.getString("deps"));
                PMBean.setMisc(rs.getString("misc"));
                PMBean.setFrase_orig_id(rs.getInt("frase_orig_id"));
                PMBean.setTabla_orig_id(rs.getInt("tabla_orig_id"));
                lista.add(PMBean);
                i++;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int getFraseID(FraseOriginalBean FOBean) {
        int id = 0;
        String query_frase = "select * from frase_original where frase_original.frase=?";

        try {
            pstmt = conn.prepareStatement(query_frase);
            pstmt.setString(1, FOBean.getFrase());
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                id = result.getInt(1);
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    public static int getPalabraId(PalabraOriginalBean POBean) {
        int id = 0;
        String query = "select * from tabla_original where tabla_original.id=? "
                + "and tabla_original.form=? and tabla_original.lemma=? "
                + "and tabla_original.upos=? and tabla_original.xpos=? "
                + "and tabla_original.feats=? and tabla_original.head=? "
                + "and tabla_original.deprel=? and tabla_original.deps=? "
                + "and tabla_original.misc=?";

        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, POBean.getId());
            pstmt.setString(2, POBean.getForm().replaceAll("\"", ""));
            pstmt.setString(3, POBean.getLemma().replaceAll("\"", ""));
            pstmt.setString(4, POBean.getUpos().replaceAll("\"", ""));
            pstmt.setString(5, POBean.getXpos().replaceAll("\"", ""));
            pstmt.setString(6, POBean.getFeats().replaceAll("\"", ""));
            pstmt.setInt(7, POBean.getHead());
            pstmt.setString(8, POBean.getDeprel().replaceAll("\"", ""));
            pstmt.setString(9, POBean.getDeps().replaceAll("\"", ""));
            pstmt.setString(10, POBean.getMisc().replaceAll("\"", ""));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("tabla_orig_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    public static void insertFraseOriginal(FraseOriginalBean FOBean) {
        String query = "insert into frase_original(frase, modificada, spacy, udpipe) "
                + "values(?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, FOBean.getFrase().replaceAll("\"", ""));
            pstmt.setBoolean(2, FOBean.isModificada());
            pstmt.setBoolean(3, FOBean.isSpacy());
            pstmt.setBoolean(4, FOBean.isUdpipe());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertPalabraOriginal(PalabraOriginalBean POBean) {
        String query = "insert into tabla_original(id, form, lemma, upos, xpos, feats, head, deprel, deps, misc, frase_orig_id) values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, POBean.getId());
            pstmt.setString(2, POBean.getForm().replaceAll("\"", ""));
            pstmt.setString(3, POBean.getLemma().replaceAll("\"", ""));
            pstmt.setString(4, POBean.getUpos().replaceAll("\"", ""));
            pstmt.setString(5, POBean.getXpos().replaceAll("\"", ""));
            pstmt.setString(6, POBean.getFeats().replaceAll("\"", ""));
            pstmt.setInt(7, POBean.getHead());
            pstmt.setString(8, POBean.getDeprel().replaceAll("\"", ""));
            pstmt.setString(9, POBean.getDeps().replaceAll("\"", ""));
            pstmt.setString(10, POBean.getMisc().replaceAll("\"", ""));
            pstmt.setInt(11, POBean.getFrase_orig_id());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertPalabraModificada(PalabraModificadaBean PMBean) {
        String query = "insert into tabla_modificada(id, form, lemma, upos, xpos, feats, head, deprel, deps, misc, frase_orig_id, tabla_orig_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, PMBean.getId());
            pstmt.setString(2, PMBean.getForm().replaceAll("\"", ""));
            pstmt.setString(3, PMBean.getLemma().replaceAll("\"", ""));
            pstmt.setString(4, PMBean.getUpos().replaceAll("\"", ""));
            pstmt.setString(5, PMBean.getXpos().replaceAll("\"", ""));
            pstmt.setString(6, PMBean.getFeats().replaceAll("\"", ""));
            pstmt.setInt(7, PMBean.getHead());
            pstmt.setString(8, PMBean.getDeprel().replaceAll("\"", ""));
            pstmt.setString(9, PMBean.getDeps().replaceAll("\"", ""));
            pstmt.setString(10, PMBean.getMisc().replaceAll("\"", ""));
            pstmt.setInt(11, PMBean.getFrase_orig_id());
            pstmt.setInt(12, PMBean.getTabla_orig_id());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateParsersFraseOriginal(FraseOriginalBean FOBean) {
        String query = "update frase_original set spacy=?, udpipe=? where frase_orig_id=?";
        try {
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setBoolean(1, FOBean.isSpacy());
            pstmt.setBoolean(2, FOBean.isUdpipe());
            pstmt.setInt(3, FOBean.getFrase_orig_id());
            int count = pstmt.executeUpdate();
            if (count == 0) {
                throw new IllegalArgumentException("Not found: " + FOBean.getFrase());
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateModificadaFraseOriginal(int frase_orig_id){
        String query = "update frase_original set modificada=? where frase_orig_id=?";
        try{
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, frase_orig_id);
            int count = pstmt.executeUpdate();
            if (count == 0)
                throw new IllegalArgumentException("Update failed");
            pstmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void desconectar() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
