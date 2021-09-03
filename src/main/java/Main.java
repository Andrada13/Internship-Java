import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
	public static void main(String[] args) throws SQLException {

	//	ConnectDB db = new ConnectDB();
		 //db.createConnection();
	
		// JSONParser jsonParser = new JSONParser();
	
		Command command = new Command();
		
		try (Scanner scanner = new Scanner(System.in)) {
			String input;
			/*
			 * try (FileReader reader = new FileReader("testcase.json")) { JSONObject obj =
			 * (JSONObject) jsonParser.parse(reader); command.setProducts((JSONArray)
			 * obj.get("stock")); command.setUsers((JSONArray) obj.get("clients")); } catch
			 * (IOException | ParseException e) { e.printStackTrace(); }
			 */
			System.out.println("type HELP to show available actions");
			command.createCategoriesList();
			
			while (!(input = scanner.nextLine()).equals("STOP")) {
				command.setCommand(input.split(" "));
				command.readCommand();
			}
			{
				System.out.println("Stopping");
				System.exit(1);
			}
		}
	}
}
