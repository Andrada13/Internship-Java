
package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String username;
	private long balance;

	public User(Integer id, String username, long balance) {
		super();
		this.id = id;
		this.username = username;
		this.balance = balance;
	}

	public User() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", balance=" + balance + "]";
	}

	/*
	 * public User(JSONObject user) { username = (String) user.get("username");
	 * balance = (long) user.get("balance");
	 * 
	 * }
	 */
	/*
	 * public JSONObject getJSONObject() { JSONObject newObj = new JSONObject();
	 * newObj.put("username", this.username); newObj.put("balance", this.balance);
	 * 
	 * return newObj; }
	 */
}
