

import org.json.simple.JSONObject;



/*public class Product {

	private String category;
	private String name;
	private long quantity;
	private long price;
	private long maxQuantity;

	public Product(JSONObject product) {
		category = (String) product.get("category");
		name = (String) product.get("name");
		quantity = (long) product.get("quantity");
		price = (long) product.get("price");
		maxQuantity = (long) product.get("maxQuantity");
	}

	public Product() {

	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(long maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public JSONObject getJSONObject() {
		JSONObject newObj = new JSONObject();
		newObj.put("category", this.category);
		newObj.put("name", this.name);
		newObj.put("quantity", this.quantity);
		newObj.put("price", this.price);
		newObj.put("maxQuantity", this.maxQuantity);

		return newObj;
	}

}
*/

public class Product {

private String category;
private String name;
private long quantity;
private long price;
private long maxQuantity;
private int CategoryID;

public Product(JSONObject product) {
	category = (String) product.get("category");
	name = (String) product.get("name");
	quantity = (long) product.get("quantity");
	price = (long) product.get("price");
	maxQuantity = (long) product.get("maxQuantity");
}

public Product() {

}



public int getCategoryID() {
	return CategoryID;
}

public void setCategoryID(int categoryID) {
	CategoryID = categoryID;
}

public String getCategory() {
	return category;
}

public void setCategory(String category) {
	this.category = category;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public long getQuantity() {
	return quantity;
}

public void setQuantity(long quantity) {
	this.quantity = quantity;
}


public long getPrice() {
	return price;
}

public void setPrice(long price) {
	this.price = price;
}

public long getMaxQuantity() {
	return maxQuantity;
}

public void setMaxQuantity(long maxQuantity) {
	this.maxQuantity = maxQuantity;
}

/*public JSONObject getJSONObject() {
	JSONObject newObj = new JSONObject();
	newObj.put("category", this.category);
	newObj.put("name", this.name);
	newObj.put("quantity", this.quantity);
	newObj.put("price", this.price);
	newObj.put("maxQuantity", this.maxQuantity);

	return newObj;
}
*/
}



