package graphics.nim.volterra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	public static String loadFileAsString(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		StringBuilder content = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			content.append(line + '\n');
		}
		in.close();
		
		return content.toString();
	}
	
	public static List<String> loadFileLines(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		List<String> content = new ArrayList<String>();
		String line = null;
		while ((line = in.readLine()) != null) {
			content.add(line);
		}
		in.close();
		
		return content;
	}
}
