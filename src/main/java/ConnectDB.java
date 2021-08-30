import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectDB {

	static final String url = "jdbc:mysql://localhost:3306/store";
	static final String JDBCDriver = "com.mysql.cj.jdbc.Driver";
	static final String usernameDB = "root";
	static final String password = "parola";

	Connection conn = null;
	Statement statement = null;
	Statement statementClients = null;
	ResultSet result = null;
	ResultSet resultClients = null;
	ResultSet categories = null;
	Statement statementCategories = null;

	/*
	public ResultSet getCategories() {
		return categories;
	}

	public void setCategories(ResultSet categories) {
		this.categories = categories;
	}

	public Statement getStatementCategories() {
		return statementCategories;
	}

	public void setStatementCategories(Statement statementCategories) {
		this.statementCategories = statementCategories;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public Statement getStatementClients() {
		return statementClients;
	}

	public void setStatementClients(Statement statementClients) {
		this.statementClients = statementClients;
	}

	public ResultSet getResult() {
		return result;
	}

	public void setResult(ResultSet result) {
		this.result = result;
	}

	public ResultSet getResultClients() {
		return resultClients;
	}

	public void setResultClients(ResultSet resultClients) {
		this.resultClients = resultClients;
	}
	*/

	public void createConnection() throws SQLException {
		try {
			Class.forName(JDBCDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, usernameDB, password);
			statement = conn.createStatement();
			System.out.println("connected");
			// statementClients = conn.createStatement();
			// statementCategories = conn.createStatement();

			//setResult(getStatement().executeQuery(query));

		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		}

		//String queryProduct = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";
		//result = getStatement().executeQuery(queryProduct);
		//setResult(result);

		//String queryCategories = "select *from categories";
		//categories = getStatementCategories().executeQuery(queryCategories);
		//setCategories(categories);

		// String queryClients = "select *from clients";
		// resultClients = getStatementClients().executeQuery(queryClients);
		// setResult(resultClients);
	}

	/*
	 * public ResultSet executeQuery() throws SQLException { String query =
	 * "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID"
	 * ; result = getStatement().executeQuery(query); setResult(result);
	 * 
	 * 
	 * return result; }
	 * 
	 * public ResultSet executeQueryProducts() throws SQLException { String
	 * queryProduct = "select *from product"; result =
	 * getStatement().executeQuery(queryProduct); setResult(result);
	 * 
	 * 
	 * 
	 * return result; }
	 * 
	 * public ResultSet executeQueryClients() throws SQLException { String
	 * queryClients = "select *from clients";
	 * 
	 * 
	 * resultClients = getStatementClients().executeQuery(queryClients);
	 * setResult(resultClients);
	 * 
	 * 
	 * return resultClients; }
	 */
}
