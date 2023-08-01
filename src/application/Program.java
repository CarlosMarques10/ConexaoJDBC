package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;
import db.DbExceptions;
import db.DbIntegrityExcpetion;

public class Program {
    public static void main(String[] args) {
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //pegando dados do banco
        try {
            conn = DB.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM department");

            while(rs.next()){
                System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DB.closeResultSet(rs);;
            DB.closeStatement(st);
            DB.closeConnection();
        }



        //inserindo dados no banco
        PreparedStatement ps = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            conn = DB.getConnection();
            ps = conn.prepareStatement(
                "INSERT INTO seller (name, email, salary, data_nascimento) VALUES (?, ?, ?,?)",
                Statement.RETURN_GENERATED_KEYS//retornar o id
                );

            ps.setString(1, "Carlos");
            ps.setString(2, "Carlos@gmail.com");
            ps.setDouble(3, 700.00);
            ps.setDate(4, new java.sql.Date(sdf.parse("23/12/2001").getTime()));
          
            ps.executeUpdate();

            if(ps.executeUpdate() > 0){
                ResultSet resultSet = ps.getGeneratedKeys();
                int id = resultSet.getInt(1);//pegando o id
            }else{

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }finally{
            DB.closeStatement(ps);
            DB.closeConnection();
        }



        //Atualizando dados do banco
        try {
            conn = DB.getConnection();

            ps = conn.prepareStatement(
                "UPDATE seller SET salary = salary + ? WHERE id = ?"
            );
            ps.setDouble(1, 400.00);
            ps.setInt(2, 4);
            ps.executeUpdate();


        } catch (SQLException e) {
           e.printStackTrace();
        }finally{
            DB.closeStatement(ps);
            DB.closeConnection();
        }



        //Deletar dados do banco
        try {
            conn = DB.getConnection();
            ps = conn.prepareStatement(
                "DELETE FROM department WHERE id = ?"
            );

            ps.setInt(1, 4);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DbIntegrityExcpetion(e.getMessage());
        }finally{
            DB.closeStatement(ps);
            DB.closeConnection();
        }


        //transações
         try {
            conn = DB.getConnection();

            conn.setAutoCommit(false);
            
            st = conn.createStatement();
            st.executeUpdate("UPDATE seller SET salary = 2090 WHERE department = 2");
            st.executeUpdate("UPDATE seller SET salary = 309 0 WHERE department = 1");

            conn.commit();

            
         } catch (Exception e) {
            try {
                conn.rollback();
                throw new DbExceptions("A transação no ocorreu, motivo: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbExceptions("Erro no rolboack " + e1.getMessage());
            }
         }


      


    }

    
}
