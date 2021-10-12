
package interfaces.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import interfaces.DatabaseServiceInterface;
import model.Category;
import model.Product;
import model.User;

@Component
public class DatabaseService implements DatabaseServiceInterface {

	private EntityManagerFactory entityManagerFactory;

	public DatabaseService() {

		this.entityManagerFactory = Persistence.createEntityManagerFactory("databaseService");
	}

	public List<Product> getProductByCategory(String categoryIn) throws Exception {

		List<Product> products = new ArrayList<Product>();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			products = entityManager
					.createQuery("SELECT p FROM Product p JOIN FETCH p.category c WHERE c.categoryPr = :categoryIn",
							Product.class)
					.setParameter("categoryIn", categoryIn).getResultList();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

		return products;

	}

	public List<Product> getAllProducts() throws Exception {

		List<Product> products = new ArrayList<Product>();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			products = entityManager
					.createQuery("SELECT p FROM Product p JOIN FETCH p.category c WHERE p.category = c.categoryID ",
							Product.class)
					.getResultList();
		} catch (NoResultException nre) {

			nre.printStackTrace();
			return null;
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
		return products;
	}

	public Product getProductsByName(String nameIn) throws Exception {

		Product product = new Product();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			product = entityManager.createQuery("SELECT p FROM Product p  WHERE p.name= :nameIn", Product.class)
					.setParameter("nameIn", nameIn).getSingleResult();
		} catch (NoResultException nre) {

			nre.printStackTrace();
			return null;
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}

		return product;
	}

	public User getUserByName(String nameUser) throws Exception {

		User user = new User();

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :nameUser", User.class)
					.setParameter("nameUser", nameUser).getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}

		return user;
	}

	public List<String> getCategoriesName() throws Exception {

		List<String> categories = new ArrayList<String>();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			categories = entityManager.createQuery("SELECT c.categoryPr FROM Category c", String.class).getResultList();

		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
		return categories;
	}

	public long decreaseQuantity(int quantityInp, String name) throws Exception {
		Product product = getProductsByName(name);

		long newQuantity = 0;

		if (product.getQuantity() > quantityInp) {
			newQuantity = product.getQuantity() - quantityInp;
		}

		return newQuantity;
	}

	public long decreaseBalance(int quantityInp, String nameUser, String name) throws Exception {
		User user = getUserByName(nameUser);
		Product product = getProductsByName(name);

		long newBalance = 0;

		long totalPrice = product.getPrice() * quantityInp;
		if (user.getBalance() > totalPrice) {
			newBalance = user.getBalance() - totalPrice;
		}
		return newBalance;
	}

	public void updateBuy(String prod, int quantityIn, String username) throws Exception {

		long q = decreaseQuantity(quantityIn, prod);
		long b = decreaseBalance(quantityIn, username, prod);

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			entityManager.getTransaction().begin();

			Query updateQueryProduct = entityManager
					.createQuery("UPDATE Product p SET p.quantity = ?1 WHERE p.name = ?2");
			Query updateQueryClient = entityManager
					.createQuery("UPDATE User u SET u.balance = ?1 WHERE u.username = ?2");
			updateQueryProduct.setParameter(1, q);
			updateQueryProduct.setParameter(2, prod);

			updateQueryClient.setParameter(1, b);
			updateQueryClient.setParameter(2, username);

			updateQueryProduct.executeUpdate();
			updateQueryClient.executeUpdate();
			entityManager.getTransaction().commit();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

	}

	public long replenishQuantity(String prod, long quantity) throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Product product = getProductsByName(prod);
		long newQuantity = 0;
		try {
			entityManager.getTransaction().begin();

			Query replenishUpdate = entityManager.createQuery("UPDATE Product p SET p.quantity = ?1 WHERE p.name = ?2");

			newQuantity = product.getQuantity() + quantity;
			if (newQuantity <= product.getMaxQuantity()) {
				replenishUpdate.setParameter(1, newQuantity);
				replenishUpdate.setParameter(2, prod);

				replenishUpdate.executeUpdate();

				entityManager.getTransaction().commit();
			}
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
		return newQuantity;

	}

	public void addNewCategory(String categoryIn) throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();

			Category category = new Category();
			category.setCategoryPr(categoryIn);

			entityManager.persist(category);
			entityManager.getTransaction().commit();
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

	public void remove(String nameIn) throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {

			entityManager.getTransaction().begin();

			Product product = getProductsByName(nameIn);
			product = entityManager.find(Product.class, product.getProductID());

			entityManager.remove(product);
			entityManager.getTransaction().commit();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

	public void addProducts(Category id, String prodName, String categoryIn, String quantityIn, String priceIn)
			throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			List<Product> products = getProductByCategory(categoryIn);

			Product newProduct = new Product();
			entityManager.getTransaction().begin();

			for (Product product : products) {
				id = product.getCategory();
			}

			long quantity = Long.parseLong(quantityIn);
			long price = Long.parseLong(priceIn);

			newProduct.setCategory(id);
			newProduct.setName(prodName);
			newProduct.setQuantity(quantity);
			newProduct.setPrice(price);

			entityManager.persist(newProduct);

			entityManager.getTransaction().commit();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

	}

}

/*
 * package interfaces.impl;
 * 
 * 
 * 
 * import java.io.FileInputStream; import java.io.IOException; import
 * java.io.InputStream; import java.sql.Connection; import
 * java.sql.DriverManager; import java.sql.PreparedStatement; import
 * java.sql.ResultSet; import java.sql.SQLException; import java.sql.Statement;
 * import java.util.ArrayList; import java.util.List; import
 * java.util.Properties;
 * 
 * import javax.annotation.PostConstruct; import
 * javax.persistence.EntityManager; import
 * javax.persistence.EntityManagerFactory; import javax.persistence.Persistence;
 * import javax.persistence.Query;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import interfaces.DatabaseServiceInterface; import model.Categories; import
 * model.Product; import model.User;
 * 
 * @Component public class DatabaseService implements DatabaseServiceInterface {
 * 
 * /* Properties properties = loadPropertiesFile();
 * 
 * @PostConstruct public Properties loadPropertiesFile() { Properties properties
 * = new Properties(); InputStream input; try { input = new
 * FileInputStream("databaseService.properties"); properties.load(input);
 * input.close(); } catch (IOException e) { e.printStackTrace(); }
 * 
 * return properties; }
 * 
 * public Connection createConnection() throws SQLException {
 * 
 * Connection conn = null;
 * 
 * try { String url = (String) properties.get("url"); String JDBCDriver =
 * (String) properties.get("driver"); String usernameDB = (String)
 * properties.get("username"); String password = (String)
 * properties.get("password");
 * 
 * Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(url, usernameDB, password);
 * conn.setAutoCommit(false); System.out.println("connected");
 * 
 * } catch (SQLException ex) { System.out.println("An error occurred.");
 * ex.printStackTrace(); } catch (ClassNotFoundException e) {
 * e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); } return
 * conn;
 * 
 * }
 * 
 * public List<Product> getProductByCategory(String categoryIn) throws Exception
 * {
 * 
 * //List<Product> products = new ArrayList<Product>();
 * 
 * 
 * 
 * EntityManagerFactory a = Persistence.createEntityManagerFactory("data");
 * EntityManager ab = a.createEntityManager();
 * 
 * //ab.getTransaction().begin(); //String query =
 * "SELECT u FROM Product u WHERE u.category=:categoryIn"; //String query =
 * "SELECT p FROM Product p JOIN Categories n ON p.category = n.categoryID where n.categoryPr= :categoryIn"
 * ; //String query =
 * "SELECT e FROM Product e,Categories c where  e.categories = c and e.category = c.categoryID and c.categoryPr = :categoryIn"
 * ; //String query =
 * "SELECT p FROM Product p, Categories c WHERE p.category = c.categoryID AND p.category=:categoryIn"
 * ; //ab.setProperty(query, categoryIn); //Query query1 =
 * ab.createQuery(query);
 * 
 * 
 * //query1.setParameter("categoryIn", categoryIn); //
 * 
 * //ab.setProperty(query, categoryIn); //List<Product> products =
 * query1.getResultList();
 * 
 * 
 * 
 * List<Product> products = ab.
 * createQuery("SELECT p FROM Product p JOIN Categories n ON p.category = n.categoryID where n.categoryPr= :categoryIn"
 * , Product.class) .setParameter("categoryIn", categoryIn) .getResultList();
 * 
 * /*try (Connection conn = createConnection(); Statement statement =
 * conn.createStatement(); ResultSet result = statement.executeQuery(query)) {
 * while (result.next()) { Product product = new Product(); if
 * (result.getString("category").equals(categoryIn)) {
 * product.setCategory(result.getString("category"));
 * product.setQuantity(result.getLong("quantity"));
 * product.setName(result.getString("namePr"));
 * product.setPrice(result.getLong("price")); products.add(product); }
 * 
 * } }
 * 
 * return products; /* String query =
 * "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID"
 * ;
 * 
 * try (Connection conn = createConnection(); Statement statement =
 * conn.createStatement(); ResultSet result = statement.executeQuery(query)) {
 * while (result.next()) { Product product = new Product(); if
 * (result.getString("category").equals(categoryIn)) {
 * product.setCategory(result.getString("category"));
 * product.setQuantity(result.getLong("quantity"));
 * product.setName(result.getString("namePr"));
 * product.setPrice(result.getLong("price")); products.add(product); }
 * 
 * } } return products;
 * 
 * }
 * 
 * public List<Product> getAllProducts() throws Exception {
 * 
 * // List<Product> products = new ArrayList<Product>(); //String query =
 * "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID"
 * ;
 * 
 * 
 * EntityManagerFactory a = Persistence.createEntityManagerFactory("data");
 * EntityManager ab = a.createEntityManager();
 * 
 * ab.getTransaction().begin();
 * 
 * //String query =
 * "SELECT e FROM Product e,Categories c where  e.categories = c and e.category = c.categoryID"
 * ; String query =
 * "SELECT p FROM Product p JOIN Categories n ON p.category = n.categoryID";
 * 
 * Query query1 = ab.createQuery(query);
 * 
 * List<Product> products = query1.getResultList();
 * 
 * /* try (Connection conn = createConnection(); Statement statement =
 * conn.createStatement(); ResultSet result = statement.executeQuery(query)) {
 * while (result.next()) { Product product = new Product();
 * product.setCategoryID(result.getInt("CategoryID"));
 * product.setCategory(result.getString("category"));
 * product.setName(result.getString("namePr"));
 * product.setQuantity(result.getLong("quantity"));
 * product.setPrice(result.getInt("price"));
 * product.setMaxQuantity(result.getLong("maxQuantity"));
 * 
 * }
 * 
 * }
 * 
 * 
 * 
 * return products; }
 * 
 * public Product getProductsByName(String nameIn) throws Exception {
 * 
 * 
 * 
 * 
 * EntityManagerFactory a = Persistence.createEntityManagerFactory("data");
 * EntityManager ab = a.createEntityManager();
 * 
 * 
 * 
 * Product product = ab.
 * createQuery("SELECT p FROM Product p JOIN Categories n ON p.category = n.categoryID where p.name= :nameIn"
 * , Product.class) .setParameter("nameIn", nameIn) .getSingleResult();
 * 
 * /*
 * 
 * //Product product; String query =
 * "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID "
 * ; try (Connection conn = createConnection(); Statement statement =
 * conn.createStatement(); ResultSet result = statement.executeQuery(query)) {
 * product = new Product();
 * 
 * while (result.next()) { if (result.getString("namePr").equals(nameIn)) {
 * product.setName(result.getString("namePr"));
 * product.setCategoryID(result.getInt("CategoryID"));
 * product.setQuantity(result.getLong("quantity"));
 * product.setPrice(result.getLong("price"));
 * product.setMaxQuantity(result.getLong("maxQuantity")); } } }
 * 
 * 
 * return product; }
 * 
 * public User getUserByName(String nameUser) throws Exception {
 * 
 * User user = new User(); String query = "SELECT *FROM clients";
 * 
 * try (Connection conn = createConnection(); Statement statement =
 * conn.createStatement(); ResultSet result = statement.executeQuery(query)) {
 * while (result.next()) { if (result.getString("username").equals(nameUser)) {
 * user.setUsername(result.getString("username"));
 * user.setBalance(result.getLong("balance")); } } } return user; }
 * 
 * public List<String> getProductsByCategory() throws Exception {
 * 
 * 
 * //List<String> categories = new ArrayList<String>(); EntityManagerFactory a =
 * Persistence.createEntityManagerFactory("data"); EntityManager ab =
 * a.createEntityManager();
 * 
 * List<String> categories =
 * ab.createQuery("SELECT c.categoryPr FROM Categories c").getResultList();
 * 
 * //Query query1 = ab.createQuery("SELECT c FROM Categories c");
 * 
 * //ab.getTransaction().begin();
 * 
 * //String query =
 * "SELECT e FROM Product e,Categories c where  e.categories = c and e.category = c.categoryID"
 * ; //String query = "SELECT c FROM Categories c";
 * 
 * //Query query1 = ab.createQuery(query);
 * 
 * //List<String> categories = query1.getResultList(); /* List<String>
 * categories = new ArrayList<String>(); String query =
 * "select *from categories"; try (Connection conn = createConnection();
 * Statement statement = conn.createStatement(); ResultSet result =
 * statement.executeQuery(query)) { while (result.next()) {
 * categories.add(result.getString("category")); } }
 * 
 * return categories; }
 * 
 * public List<User> getUsers() throws Exception {
 * 
 * List<User> clients = new ArrayList<User>(); String queryClients =
 * "select *from clients"; try (Connection conn = createConnection(); Statement
 * statement = conn.createStatement(); ResultSet result =
 * statement.executeQuery(queryClients)) { while (result.next()) { User user =
 * new User();
 * 
 * user.setUsername(result.getString("username"));
 * user.setBalance(result.getLong("balance"));
 * 
 * clients.add(user); } }
 * 
 * return clients; }
 * 
 * public long decreaseQuantity(int quantityInp, String name) throws Exception {
 * Product product = getProductsByName(name);
 * 
 * long newQuantity = 0;
 * 
 * if (product.getQuantity() > quantityInp) { newQuantity =
 * product.getQuantity() - quantityInp; }
 * 
 * return newQuantity; }
 * 
 * public long decreaseBalance(int quantityInp, String nameUser, String name)
 * throws Exception { User user = getUserByName(nameUser); Product product =
 * getProductsByName(name);
 * 
 * long newBalance = 0;
 * 
 * long totalPrice = product.getPrice() * quantityInp; if (user.getBalance() >
 * totalPrice) { newBalance = user.getBalance() - totalPrice; } return
 * newBalance; }
 * 
 * public void updateBuy(String prod, int quantityIn, String username) throws
 * Exception {
 * 
 * long q = decreaseQuantity(quantityIn, prod); long b =
 * decreaseBalance(quantityIn, username, prod);
 * 
 * String updateQueryProduct = "UPDATE product SET quantity=? WHERE namePr=?";
 * String updateQueryClient = "UPDATE clients SET balance=? WHERE username=?";
 * 
 * try (Connection conn = createConnection(); PreparedStatement psUpdateProduct
 * = conn.prepareStatement(updateQueryProduct); PreparedStatement psUpdateClient
 * = conn.prepareStatement(updateQueryClient);) {
 * 
 * System.out.println(q); System.out.println(b);
 * 
 * psUpdateProduct.setLong(1, q); psUpdateProduct.setString(2, prod);
 * 
 * psUpdateClient.setLong(1, b); psUpdateClient.setString(2, username);
 * 
 * psUpdateProduct.executeUpdate(); psUpdateClient.executeUpdate();
 * 
 * conn.commit(); }
 * 
 * }
 * 
 * public long replenishQuantity(String prod, long quantity) throws Exception {
 * 
 * Product product = getProductsByName(prod); String sqlUpdate =
 * "UPDATE product " + "SET quantity = ? " + "WHERE namePr = ?";
 * 
 * long newQuantity = 0; try (Connection conn = createConnection();
 * PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) { newQuantity
 * = product.getQuantity() + quantity;
 * 
 * if (newQuantity <= product.getMaxQuantity()) { psUpdate.setLong(1,
 * newQuantity); psUpdate.setString(2, prod); psUpdate.executeUpdate();
 * conn.commit(); } } return newQuantity;
 * 
 * }
 * 
 * public void addNewCategory(String category) throws Exception {
 * 
 * String queryInsert = "insert into categories (category) values (?)"; try
 * (Connection conn = createConnection(); PreparedStatement psInsert =
 * conn.prepareStatement(queryInsert);) {
 * 
 * psInsert.setString(1, category); psInsert.executeUpdate(); conn.commit(); } }
 * 
 * ///merge public void remove(String nameIn) throws Exception {
 * 
 * EntityManagerFactory a = Persistence.createEntityManagerFactory("data");
 * EntityManager ab = a.createEntityManager();
 * 
 * ab.getTransaction().begin();
 * 
 * Query query = ab.createQuery(
 * "DELETE FROM Product p WHERE p.name = :nameIn"); int deletedCount =
 * query.setParameter("nameIn", nameIn).executeUpdate(); // ab.persist(query);
 * ab.getTransaction().commit(); System.out.println(deletedCount); /* String
 * queryDelete = "delete from product where namePr=?";
 * 
 * try (Connection conn = createConnection(); PreparedStatement psDelete =
 * conn.prepareStatement(queryDelete)) { psDelete.setString(1, name);
 * psDelete.executeUpdate(); conn.commit(); }
 * 
 * }
 * 
 * public void addProducts(int categoryid, String prodName, String categoryIn,
 * String quantityIn, String priceIn) throws Exception {
 * 
 * 
 * 
 * 
 * /* List<Product> products = getAllProducts(); String queryInsert =
 * "INSERT INTO product (CategoryID,namePr, quantity, price) VALUES (?,?, ?, ?)"
 * ;
 * 
 * try (Connection conn = createConnection(); PreparedStatement psInsert =
 * conn.prepareStatement(queryInsert)) { for (Product product : products) { if
 * ((product.getCategory().equals(categoryIn))) { categoryid =
 * product.getCategoryID(); } } psInsert.setInt(1, categoryid);
 * psInsert.setString(2, prodName); psInsert.setString(3, quantityIn);
 * psInsert.setString(4, priceIn); psInsert.executeUpdate(); conn.commit(); }
 * 
 * }
 * 
 * }
 */