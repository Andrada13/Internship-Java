
package interfaces.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import interfaces.PrinterInterface;

@Component
public class Printer implements PrinterInterface{
	private String displayMode = "CONSOLE"; //modul de afisare implicit este CONSOLE
	private String path;
	

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public void print(List<String> messages, String[] commands) {

		if (displayMode.equals("CONSOLE")) {
			printInConsole(messages);
			return;
		}
		printInFile(messages, commands);

		
	}

	private void printInFile(List<String> messages, String[] commands) {
		try (FileWriter file = new FileWriter(this.getPath(),true)) {
			BufferedWriter bw = new BufferedWriter(file);
			PrintWriter out = new PrintWriter(bw);

			String commandTime = java.time.LocalDateTime.now()
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss, SSS"));
			file.write("[" + commandTime + "] " + String.join(" ", commands) + "\n");

			for (String message : messages) {
				String messageTime = java.time.LocalDateTime.now()
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss, SSS"));
				file.write("[" + messageTime + "] " + message + "\n");
			}
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printInConsole(List<String> messages) {
		for (String message : messages) {
			System.out.println(message);
		}

	}

}
