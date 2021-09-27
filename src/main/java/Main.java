
import java.util.Scanner;



public class Main {
	public static void main(String[] args) throws Exception {

		
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
			//command.createCategoriesList();
			
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
