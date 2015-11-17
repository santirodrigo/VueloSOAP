/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author sergi.soriano.bial
 */
@WebService(serviceName = "vueloWS")
public class vueloWS {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "consulta_libres")
    public int consulta_libres(@WebParam(name = "id_vuelo") int id_vuelo, @WebParam(name = "fecha") int fecha){
        //TODO write your implementation code here:
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(vueloWS.class.getName()).log(Level.SEVERE, null, ex);
        }
            try {
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\UNI\\AD\\practica3.db");
            String selectStatement = "SELECT * " 
                                    + "FROM vuelo_fecha "
                                    + "WHERE id_vuelo=? AND fecha = ?";
            PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
            
            prepStmt.setString(1,Integer.toString(id_vuelo));
            prepStmt.setString(2,Integer.toString(fecha));
            
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt("num_plazas_max") - rs.getInt("num_plazas_ocupadas");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(vueloWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
              if(connection != null)
                connection.close();
            }
            catch(SQLException e)
            {
              // connection close failed.
              System.err.println(e.getMessage());
            }
        }
        return -1;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "reserva_plaza")
    public int reserva_plaza(@WebParam(name = "id_vuelo") int id_vuelo, @WebParam(name = "fecha") int fecha) {
        //TODO write your implementation code here:
        //TODO write your implementation code here:
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\UNI\\AD\\practica3.db");
            String selectStatement = "SELECT * " 
                                    + "FROM vuelo_fecha "
                                    + "WHERE id_vuelo=? AND fecha = ?";
            PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
            
            prepStmt.setString(1,Integer.toString(id_vuelo));
            prepStmt.setString(2,Integer.toString(fecha));
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next())
            {
                if (rs.getInt("num_plazas_max") - rs.getInt("num_plazas_ocupadas") > 0 )
                {
                    int plazasOcupadas = rs.getInt("num_plazas_ocupadas")+1;
                    selectStatement = "UPDATE vuelo_fecha " 
                                    + "SET num_plazas_ocupadas=? "
                                    + "WHERE id_vuelo=? AND fecha = ?";
                    prepStmt = connection.prepareStatement(selectStatement);
                    prepStmt.setString(1,Integer.toString(plazasOcupadas));
                    prepStmt.setString(2,Integer.toString(id_vuelo));
                    prepStmt.setString(3,Integer.toString(fecha));
                    prepStmt.executeUpdate();
                    return plazasOcupadas;
                }
                else
                {
                    return -1;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(vueloWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
              if(connection != null)
                connection.close();
            }
            catch(SQLException e)
            {
              // connection close failed.
              System.err.println(e.getMessage());
            }
        }
        return 0;
    }
}
