package class10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectDatabase {

	public static Connection conn;
	public static void main(String[] args) {
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			String dbURL = "jdbc:sqlserver://localhost;encrypt=true;trustServerCertificate=true;databaseName=test";
			String user = "sa" ;
			String pass = "123";
			conn = DriverManager.getConnection(dbURL,user,pass);
			if(conn !=null) {
				System.out.println("Connection Ready!!!");
				String insertQuery = "INSERT INTO Users(Userid,name,email,password,age) VALUES(?,?,?,?,?)";
				PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
				preparedStatement.setInt(1,1103);
				preparedStatement.setString(2,"cheng");
				preparedStatement.setString(3,"cheng@gmail.com");
				preparedStatement.setString(4,"123");
				preparedStatement.setInt(5,20);
				
				int row = preparedStatement.executeUpdate();
				if(row > 0) {
					System.out.println("Insert Successful !!!!");
				}
				
				String selectQuery = " select * from Users";
				PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
				ResultSet resultSet = selectStatement.executeQuery();
				
				System.out.println("Outcome:");
				System.out.println("Userid/name/email/password/age");
				
				while (resultSet.next()) {
					System.out.println(""+resultSet.getInt("Userid")+ "\t" +
									   	resultSet.getString("name")+ "\t" +
										resultSet.getString("email")+ "\t" +
										resultSet.getString("password")+ "\t" +
										resultSet.getInt("age"));
				}
				
				preparedStatement.close();
			}
			else {
				System.out.println("Connection Fail !!!");
			}
		}
		catch(Exception ex) {
			System.out.println("An error occurred while establishing the connection:");
			ex.printStackTrace();
		}
		finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
							
		}
	}

}
	
