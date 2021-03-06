package youtube;

import java.net.ConnectException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	private static final long serialVersionUID = 1L;
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public UserDAO() {}
	
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
	
	public List<User> listAllUser() throws SQLException {
        List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM user";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            int age = resultSet.getInt("age");
             
            User newUser = new User(email, password, firstname, lastname, age);
            listUsers.add(newUser);
        }        
        resultSet.close();
        statement.close();         
        disconnect();        
        return listUsers;
    }
	
    public void insert(User user) throws SQLException {
    	connect_func();         
    	String sql = "insert into  user (email, password, firstname, lastname, age) values (?, ?, ?, ?, ?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, user.getEmail());
		preparedStatement.setString(2, user.getPassword());
		preparedStatement.setString(3, user.getFirst());
		preparedStatement.setString(4, user.getlast());
		preparedStatement.setInt(5, user.getAge());
		preparedStatement.executeUpdate();
        preparedStatement.close();
        disconnect();
    }
	
    // Function that drops the table
 	public void dropTable() throws SQLException {
		connect_func();
		statement = (Statement) connect.createStatement();
		statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
		statement.executeUpdate("DROP TABLE IF EXISTS user");
		statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
 	}
 	
	// Function that creates and seeds the table
	public void createTable() throws SQLException {
		try {
			connect_func();
			String s = "CREATE TABLE user (" +
					"email VARCHAR(100) NOT NULL," +
					"password VARCHAR(20) NOT NULL," +
					"firstname VARCHAR(20) NOT NULL," +
					"lastname VARCHAR(20) NOT NULL," +
					"age INTEGER NOT NULL," +
					"PRIMARY KEY(email));";
			String s2 = "INSERT INTO User(email, password, firstname, lastname, age) VALUES" +
					"('root', 'pass1234', 'x', 'x', '0'), " +
					"('mary@gmail.com', 'password1234', 'Mary', 'Smith', '20'), " +
					"('luke@gmail.com', 'password1234', 'Luke', 'Cambell', '13'), " +
					"('john@gmail.com', 'password1234', 'John', 'Lewis', '10'), " +
					"('tess@gmail.com', 'password1234', 'Tess', 'Packer', '90'), " +
					"('tia@gmail.com', 'password1234', 'Tia', 'Gijo', '20'), " +
					"('logan@gmail.com', 'password1234', 'Logan', 'Nguyen', '55'), " +
					"('junwen@gmail.com', 'password1234', 'Juwen', 'Smith', '44'), " +
					"('evan@gmail.com', 'password1234', 'Evan', 'Carr', '33'), " +
					"('evanlog@gmail.com', 'password1234', 'Evanlogan', 'Carry', '35'), " +
					"('bob@gmail.com', 'password1234', 'Bob', 'Dentem', '12');";
			statement.executeUpdate(s);
			System.out.println("'User' table created.");
			statement.executeUpdate(s2);
			System.out.println("10 users added.");
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			close();
		}
	}
	
	// Function to check if the email and username match up
	public boolean isUserValid(String email, String password) throws SQLException {
		connect_func();
		boolean flag = false;

		statement = (Statement) connect.createStatement();
		String s = "Select * from User where email='" + email + "' and password='" + password + "'";
		ResultSet rs = statement.executeQuery(s);
			
		if(rs.next())
			flag = true;
		return flag;
	}
	
	public List<User> getProductiveUsers() throws SQLException {
		List<User> list = new ArrayList<User>();
		
		connect_func();
 		statement = (Statement) connect.createStatement();
		statement.executeUpdate("DROP VIEW IF EXISTS mostVideos");
        statement.executeUpdate("CREATE VIEW mostVideos(postuser, num) AS SELECT postuser, COUNT(*) AS num​ FROM video GROUP BY postuser ORDER BY COUNT(*) DESC");
        String s = "SELECT * FROM mostVideos";
        ResultSet rs = statement.executeQuery(s);
        
        int lastTotalVideos = 0;
        while (rs.next()) {
            String email = rs.getString("postuser");
            int num = rs.getInt("num");
            if(num >= lastTotalVideos) {
            	lastTotalVideos = num;
            	list.add(new User(email, "", "", "", 0));
        	}
        }

		return list;
	}
	
	public List<User> getAllUsers() throws SQLException {
		List<User> list = new ArrayList<User>();
		
		connect_func();
        statement = (Statement) connect.createStatement();
        String s = "SELECT * FROM user where email <> 'root';";      
        
        ResultSet rs = statement.executeQuery(s);
        
        while (rs.next())
        {
            String email = rs.getString("email");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            int age = rs.getInt("age");
            list.add(new User(email, "", firstname, lastname, age));
        }

		return list;
	}
		
	public List<User> getPositiveUsers() throws SQLException {
		List<User> list = new ArrayList<User>();
		
		connect_func();
        statement = (Statement) connect.createStatement();
        String s = "SELECT DISTINCT R.username, U.firstname, U.lastname FROM user U, review R " +
        		"WHERE R.username = U.email AND (R.score = 'G' OR score = 'E') AND R.username NOT IN " +
        		"(SELECT DISTINCT R.username FROM review R WHERE (R.score <> 'G' AND score <> 'E'));";      
        
        ResultSet rs = statement.executeQuery(s);
        
        while (rs.next())
        {
            String email = rs.getString("username");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            list.add(new User(email, "", firstname, lastname, 0));
        }

		return list;
	}
	
	public List<Pair> getIdenticalUsers() throws SQLException {
		List<Pair> list = new ArrayList<Pair>();
		List<String> userList = new ArrayList<String>();

		connect_func();
        statement = (Statement) connect.createStatement();
        String s = "SELECT DISTINCT username FROM favorite";
        ResultSet rs = statement.executeQuery(s);
        
        while(rs.next())
        {
        	String name = rs.getString("username");
        	userList.add(name);
        }
        
        for(int i = 0; i < userList.size(); i++)
        {
        	statement = (Statement) connect.createStatement();
        	String [] array = userList.get(i).split("@gmail.com");
            statement.executeUpdate("DROP VIEW IF EXISTS " + array[0]);
        	statement.executeUpdate("CREATE VIEW " + array[0] + "(username, comedianId) AS SELECT username, comedianId FROM favorite WHERE username='" + userList.get(i) + "'");
        }
        
        for(int i = 0; i < userList.size(); i++)
        {
        	statement = (Statement) connect.createStatement();
        	String [] array = userList.get(i).split("@gmail.com");
            rs = statement.executeQuery("SELECT * FROM " + array[0]);
            
    		List<Integer> idList1 = new ArrayList<Integer>();
            while(rs.next())
            {
            	int id = rs.getInt("comedianId");
            	System.out.println("1: " + id + " : " + userList.get(i));
            	idList1.add(id);
            }
            
        	for(int j = i + 1; j < userList.size() - 1; j++)
            {
        		statement = (Statement) connect.createStatement();
        		String [] array2 = userList.get(j).split("@gmail.com");
                rs = statement.executeQuery("SELECT * FROM " + array2[0]);
                
        		List<Integer> idList2 = new ArrayList<Integer>();
                while(rs.next())
                {
                	int id = rs.getInt("comedianId");
                	System.out.println("2: " + id + " : " + userList.get(j));
                	idList2.add(id);
                }
                
                boolean notEqual = false;
                System.out.println("size:" + idList1.size() + " "+ idList2.size());
                if(idList1.size() == idList2.size())
                {
                	for(int k = 0; k < idList1.size(); k++)
                    {
                    	if(idList1.get(k) != idList2.get(k))
                    		notEqual = true;
                    }
                } else {
                	notEqual = true;
                	System.out.println("wrong size, not equal");
                }
                                
                if(notEqual == false)
                	list.add(new Pair(userList.get(i), userList.get(j)));
            }
        }

		return list;
	}
	
	// Function to check if the username / email already exists
	public boolean isDuplicateEmail(String email) throws SQLException {
		connect_func();
		boolean flag = false;
		statement = (Statement) connect.createStatement();
		String s2 = "Select * from User where email='" + email + "'";
		ResultSet rs = statement.executeQuery(s2);
			
		if(rs.next())
			flag = true;
		return flag;
	}
	
	private void close() throws SQLException {
		if (resultSet != null)
			resultSet.close();
		if (statement != null)
			statement.close();
	}
}