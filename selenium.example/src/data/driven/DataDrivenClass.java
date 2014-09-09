package data.driven;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class DataDrivenClass {

	public String[] readTCsCsvFile(String TCsCsvFile, String startRow) {
		try {
			CSVReader reader = new CSVReader(new FileReader(TCsCsvFile));
			String[] line;
			String inputData = "";
			String header="";
			int count = 0;
			while ((line = reader.readNext()) != null) {
				if (count == 0) {
					for (int i = 0; i < line.length; i++) {
						header += line[i] + ",\t";
					}
				}
				
				if (count == Integer.parseInt(startRow)) {
					for (int i = 0; i < line.length; i++) {
						inputData += line[i] + ",\t";
					}
					System.out.println("The data's header:"+header);
					System.out.println("The data's input: " + inputData);
					System.out.print("\n");
					reader.close();
					return line;
				}
				count++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] readMasterCsvFile(String filePath, int lineNo) {
		try {
			CSVReader reader = new CSVReader(new FileReader(filePath));
			String[] line;
			String inputData = "";
			String header = "";
			String execute = "Y";
			int count = 0;
			while ((line = reader.readNext()) != null) {
				if (count == 0) {
					for (int i = 0; i < line.length; i++) {
						header += line[i] + ",\t";
					}
				}
				if (count == lineNo && line[2].equals(execute)) {
					for (int i = 0; i < line.length; i++) {
						inputData += line[i] + ",\t";
					}
					System.out.println("The master's header:"+header);
					System.out.println("The master's input: " + inputData);
					System.out.print("\n");
					reader.close();
					return readTCsCsvFile(line[3], line[5]);
				}
				count++;
			}
			System.out
					.println("Cannot find the suitable line! \n"
							+ "Please check the line number or its status of execution.");
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
