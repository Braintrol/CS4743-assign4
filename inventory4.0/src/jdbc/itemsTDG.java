package jdbc;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mvc.model.Item;
import mvc.model.ItemList;
import mvc.model.Part;
import mvc.model.PartList;

public class itemsTDG {
	
	Connection conn;
	MysqlDataSource ds;
	Statement stmt;
	ResultSet rs;
	String sql;
	
	
	
	/*Updates item in data base*/
	public void updateItem(long item_id, String item_location, int item_quantity, long part_id) {
		String sql = "UPDATE items SET "  +
					 "item_location = '"  + item_location + "'" +
					 ",item_quantity = "  + item_quantity +
					 ",part_id = "        + part_id       +
					 "WHERE item_id = "   + item_id       + ";" ;
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
	
	
	
	/*Delete item from database*/
	public void deleteItem(long item_id){
		sql = "DELETE FROM items WHERE item_id = " + item_id + ";";
		try {
			connect();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage()+"\n");
			//conn.rollback();
		}finally{
			finalize();
		}
	}
	
	
	
	
	/*Create new Item in DataBase, return its auto-generated ID */
	public Long insertNewtItem(String item_location, int item_quantity, long part_id){
		Long item_id = null;
	    sql = "INSERT INTO items (item_location, item_quantity, part_id) " +
			   "VALUES ('"+item_location+"',"+item_quantity+","+part_id+");";
		try {
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			rs.next();
			item_id = rs.getLong(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage()+"\n");
			//conn.rollback();
		}finally{
			finalize();
		}
		return item_id;
	}
	
	/*Get all items from database */
	public ItemList getItemList( PartList pl) {
		
		
		sql = "SELECT * FROM items";
		
		/*variables to store retrieved items */
		ItemList items = new ItemList();
		
		try {	/* execute query and get resultSet*/
			connect();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			    /* read every row */
			while(rs.next()){
				
				try{
					/*add item to arrayList */
					for(Part p: pl.parts ){
						if(p.getID() == rs.getLong("part_id")){
							Item it = new Item(rs.getLong(1), rs.getString(2), rs.getInt(3), p);
							items.addItem(it);
							System.out.printf("%d %s %d %s",rs.getLong(1), rs.getString(2), rs.getInt(3), p.getID());
						}
					}
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//conn.rollback();
		}finally{
			finalize();
		}
		return items;
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
