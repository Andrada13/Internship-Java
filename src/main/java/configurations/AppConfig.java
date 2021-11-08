package configurations;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import interfaces.CommandProcessingServiceInterface;

@ComponentScan(basePackages = "interfaces")
@Configuration
public class AppConfig {

	private static final Logger logger = LogManager.getLogger(AppConfig.class);

	public static void main(String[] args) throws Exception {

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

			CommandProcessingServiceInterface commandProcessingService = context.getBean("commandProcessingService",
					CommandProcessingServiceInterface.class);

			try (Scanner scanner = new Scanner(System.in)) {
				String input;

				logger.info("type HELP to show available actions");

				while (!(input = scanner.nextLine()).equals("STOP")) {
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
