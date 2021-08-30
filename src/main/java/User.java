import org.json.simple.JSONObject;

public class User {
	private String username;
	private long balance;

	public User(JSONObject user) {
		username = (String) user.get("username");
		balance = (long) user.get("balance");

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

	public JSONObject getJSONObject() {
		JSONObject newObj = new JSONObject();
		newObj.put("username", this.username);
		newObj.put("balance", this.balance);

		return newObj;
	}
}
