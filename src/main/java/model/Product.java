package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer ProductID;
	@Column(name = "namePr")
	private String name;
	private Long quantity;
	private Long price;
	private Long maxQuantity;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "CategoryID")
	Category category;

	public Product() {

	}

	// is the one you use to create instances of class to be saved to the database
	public Product(Integer productID, String name, Long quantity, Long price, Long maxQuantity, Category category) {
		super();
		ProductID = productID;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.maxQuantity = maxQuantity;
		this.category = category;
	}

	public void setProductID(Integer productID) {
		ProductID = productID;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getProductID() {
		return ProductID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(Long maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Product [ProductID=" + ProductID + ", name=" + name + ", quantity=" + quantity + ", price=" + price
				+ ", maxQuantity=" + maxQuantity + ", category=" + category + "]";
	}

	/*
	 * public JSONObject getJSONObject() { JSONObject newObj = new JSONObject();
	 * newObj.put("category", this.category); newObj.put("name", this.name);
	 * newObj.put("quantity", this.quantity); newObj.put("price", this.price);
	 * newObj.put("maxQuantity", this.maxQuantity);
	 * 
	 * return newObj; }
	 */
	/*
	 * public Product(JSONObject product) { //category = (String)
	 * product.get("category"); name = (String) product.get("name"); quantity =
	 * (long) product.get("quantity"); price = (long) product.get("price");
	 * maxQuantity = (long) product.get("maxQuantity"); }
	 */
}

/*
 * public class Product {
 * 
 * private String category; private String name; private long quantity; private
 * long price; private long maxQuantity;
 * 
 * public Product(JSONObject product) { category = (String)
 * product.get("category"); name = (String) product.get("name"); quantity =
 * (long) product.get("quantity"); price = (long) product.get("price");
 * maxQuantity = (long) product.get("maxQuantity"); }
 * 
 * public Product() {
 * 
 * }
 * 
 * public String getCategory() { return category; }
 * 
 * public void setCategory(String category) { this.category = category; }
 * 
 * public String getName() { return name; }
 * 
 * public void setName(String name) { this.name = name; }
 * 
 * public long getQuantity() { return quantity; }
 * 
 * public void setQuantity(long quantity) { this.quantity = quantity; }
 * 
 * public long getPrice() { return price; }
 * 
 * public void setPrice(long price) { this.price = price; }
 * 
 * public long getMaxQuantity() { return maxQuantity; }
 * 
 * public void setMaxQuantity(long maxQuantity) { this.maxQuantity =
 * maxQuantity; }
 * 
 * public JSONObject getJSONObject() { JSONObject newObj = new JSONObject();
 * newObj.put("category", this.category); newObj.put("name", this.name);
 * newObj.put("quantity", this.quantity); newObj.put("price", this.price);
 * newObj.put("maxQuantity", this.maxQuantity);
 * 
 * return newObj; }
 * 
 * }
 */
