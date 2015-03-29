package jdbc;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mvc.model.Item;
import mvc.model.Part;
import mvc.model.PartList;

public class partsTDG {
	
	
	private Connection conn;
	private MysqlDataSource ds;
	private Statement stmt;
	private ResultSet rs;
	private String sql;
	
	
	/*Updates part in data base*/
	public void updatePart(long part_id,String part_external_number,String part_number, String part_name, String part_vendor,String part_unit){
		String sql = "UPDATE parts SET "  +
					 "part_external_number = '"  + part_external_number +"'"+
					 ",part_number = '"      + part_number    +"'"+
					 ",part_name = '"        + part_name      +"'"+
					 ",part_vendor = '"      + part_vendor    +"'"+
					 ",part_unit = '"        + part_unit      +"'"+
					 "WHERE part_id = "      + part_id        +";";
		
		try {
			connect();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}catch (SQLException e) {
			System.out.println(e.getMessage()+"\n");
			//conn.rollback();
		}finally{
			finalize();
		}
	}
	
	
	/*Delete part from database*/
	public void deletePart(long part_id) {
		sql = "DELETE FROM parts WHERE part_id = " + part_id + ";";
		try {
			connect();
			stmt = conn.createStatement();
			stmt.execute(sql);
		}catch (SQLException e) {
			System.out.println(e.getMessage()+"\n");
			//conn.rollback();
		}finally{
			finalize();
		}
	}
	
	
	/*Create new part in DataBase, return its auto-generated ID */
	public Long insertNewtPart(final Part p){
		
		Long id = null;
		sql = "INSERT INTO parts (part_external_number, part_number, part_name, part_vendor, part_unit) " +
			   "VALUES ('"+p.getExtrnPartNum()+"','"+p.getPartNumber()+"','"+p.getPartName()+"','"+p.getVendor()+"','"+p.getPartUnit()+"');";
		try {
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getLong(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage()+"\n");
			
		
		}finally{
			finalize();
		}	
		return id;
	}
	
	/*Get all parts from database */
	public PartList getPartList() {
		
		sql = "SELECT * FROM parts";
		
		/* variables */
		PartList parts = new PartList(); 
		
		/* execute query */	
		try {
			connect();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			/* read every row */
			while(rs.next()){   
			
				Part p = new Part(rs.getLong("part_id"),
						          rs.getString("part_external_number"),
						          rs.getString("part_number"),
						          rs.getString("part_name"),
						          rs.getString("part_vendor"),
						          rs.getString("part_unit"));
				
				parts.addPart(p);
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage()+"\n");
			
		}finally{
			finalize();
		}
		return parts;
	}
	
	
	
	private void connect() throws SQLException {
		ds = new MysqlDataSource();
		ds.setURL("jdbc:mysql://devcloud.fulgentcorp.com:3306/iys480");
		ds.setUser("ys480");
		ds.setPassword("ygAis1eYLQI3UKGk76lo");
		conn = ds.getConnection();
		conn.setAutoCommit(true);
	}
	
	
	public void finalize(){
		try{
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close(); 
		}catch(SQLException e){
			System.out.println("failed to finalize - partsTDG\n");
		}
	}
	
	
	
	
}
