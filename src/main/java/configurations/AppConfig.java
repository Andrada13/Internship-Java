package configurations;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import interfaces.CommandProcessingServiceInterface;

@ComponentScan(basePackages = "interfaces")
@Configuration
public class AppConfig {
	public static void main(String[] args) throws Exception {

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
			// JSONParser jsonParser = new JSONParser();

			CommandProcessingServiceInterface commandProcessingService = context.getBean("commandProcessingService",
					CommandProcessingServiceInterface.class);

			try (Scanner scanner = new Scanner(System.in)) {

				String input;
				/*
				 * try (FileReader reader = new FileReader("testcase.json")) { JSONObject obj =
				 * (JSONObject) jsonParser.parse(reader); command.setProducts((JSONArray)
				 * obj.get("stock")); command.setUsers((JSONArray) obj.get("clients")); } catch
				 * (IOException | ParseException e) { e.printStackTrace(); }
				 */
				System.out.println("type HELP to show available actions");
				// command.createCategoriesList();

				while (!(input = scanner.nextLine()).equals("STOP")) {
					// command.setCommand(input.split(" "));
					// command.readCommand();
					commandProcessingService.executeCommand(input.split(" "));
				}
				{
					System.out.println("Stopping");
					System.exit(1);
				}
			}
		}

	}

}
