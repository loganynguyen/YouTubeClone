package youtube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
	private static final long serialVersionUID = 1L;
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public ReviewDAO() {}
	
	protected void connect_func() throws SQLException {
        if (connect == null || connect.isClosed()) {
    		System.out.println("Connecting to the database...");        
    		try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/youtube?" + "user=john&password=pass1234");
            System.out.println(connect);
        }
		System.out.println("Connection established.");        
    }
	
	protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
	
	// Function that creates and seeds the table
 	public void dropTable() throws SQLException {
		connect_func();
		
		// drop the table and create a new one
		statement = (Statement) connect.createStatement();
		statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
		statement.executeUpdate("DROP TABLE IF EXISTS review");
		statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
 	}
 	
	// Function that creates and seeds the table
	public void createTable() throws SQLException {
		try {
			connect_func();
			
			// create the review table
			String s = "CREATE TABLE review (" +
					"url VARCHAR(50) NOT NULL," +
					"username VARCHAR(50) NOT NULL," +
					"remark VARCHAR(100) NOT NULL," +
					"score VARCHAR(20) NOT NULL," +
					"reviewId VARCHAR(2) NOT NULL," +
					"PRIMARY KEY(reviewId) );";
			statement.executeUpdate(s);
			System.out.println("'Review' table created.");
			
			// seed the table with 10 reviews
			String s2 = "INSERT INTO review(url, username, remark, score, reviewId) VALUES" +
					"('youtube.com', 'mary@gmail.com', 'Somany videos and really helpful', 'Poor', '1'), " +
					"('google.com', 'luke@gmail.com', 'Can search anything you want', 'Fair', '2'), " +
					"('wix.com', 'john@gmail.com', 'Very helpful in making sites', 'Good', '3'), " +
					"('yahoo.com', 'tess@gmail.com', 'Very helpful search engine', 'Excellent', '4'), " +
					"('gmail.com', 'tia@gmail.com', 'Can send emails from any part to the world and recieve emails too', 'Good', '5'), " +
					"('facebook.com', 'logan@gmail.com', 'Upload photos and videos', 'Poor', '6'), " +
					"('amazon.com', 'junwen@gmail.com', 'purchase anything you want and get delivered in 2 days', 'Fair', '7'), " +
					"('instagram.com', 'evan@gmail.com', 'Upload status and stories', 'Fair', '8'), " +
					"('samsung.com', 'evanlogan@gmail.com', 'purchase phones you want', 'Poor', '9'), " +
					"('ebay.com', 'bob@gmail.com', 'Very cheap shopping but ships slow', 'Excellent', '10');";
			statement.executeUpdate(s2);
			System.out.println("10 reviews added.");
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			close();
		}
	}
	
	private void close() throws SQLException {
		if (resultSet != null)
			resultSet.close();
		if (statement != null)
			statement.close();
	}
}