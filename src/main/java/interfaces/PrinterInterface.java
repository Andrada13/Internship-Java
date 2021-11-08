package interfaces;

import java.util.List;

public interface PrinterInterface {

	public void print(List<String> messages, String[] commands);

	public void setDisplayMode(String newDisplayMode);

	public void setPath(String replace);

	public String getDisplayMode();
	
}
