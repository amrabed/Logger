package xtract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Xtract
{
	private static final String APPLICATIONS = "applications";
	private static final String CALLS = "calls";
	private static final String BLUETOOTH = "bluetooth";
	private static final String MESSAGES = "messages";
	private static final String FILES = "files";
	private static final String DEVICES = "devices";
	private static final String MOBILE = "mobile";
	private static final String WIFI = "wifi";
	private static final String BROWSER = "browser";
	private static final String MODES = "modes";
	private static final String POWER = "power";
	private static final String LOCATION = "location";

	private static final String OUTPUT = "./output/";

	public static void main(String[] args)
	{
		try
		{
			for (String fileName : args)
			{
				String fileContents = readFileAsString("./" + fileName + ".csv");
				fileContents = fileContents.replaceAll("\\s", "");
				fileContents = fileContents.replaceAll("([0-9]{13})(\\|)", "\n$1\t");

				if (fileName.contains(APPLICATIONS))
				{
					fileContents = fileContents.replaceAll("(\\|)([a-z]{3,})", "\t$2");
				}
				else if (fileName.contains(CALLS))
				{
					fileContents = fileContents.replaceAll("(\\|)([0-9]{1})(\\|)", "\t$2\t");
				}
				else if (fileName.contains(BLUETOOTH))
				{
					fileContents = fileContents.replaceAll("(\\|)([0-9]{1,})(\\|)", "\t$2\t");
				}
				else if (fileName.contains(MESSAGES))
				{
					// TODO Handle last line in file
					fileContents = fileContents.replaceAll("(\\|)([0-9]{1}[\n])", "\t$2");
				}
				else if ((fileName.contains(FILES)) || (fileName.contains(MOBILE)))
				{
					fileContents = fileContents.replaceAll("(\\|)([A-Z]{4,})", "\t$2");
				}
				else if(fileName.contains(MODES) || fileName.contains(POWER) || fileName.contains(LOCATION))
				{
					// Any other (not encrypted) file, just replace '|' by a tab
					fileContents = fileContents.replaceAll("\\|", "\t");
				}
				writeOutputFile(fileName, fileContents);
				process(fileName);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static String readFileAsString(String filePath) throws IOException
	{
		StringBuilder fileData = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
		{
			char[] buf = new char[1024 * 1024];
			int numRead;
			while ((numRead = reader.read(buf)) != -1)
			{
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
		}
		return fileData.toString();
	}

	private static void writeOutputFile(String fileName, String fileContents) throws IOException
	{
		String currentLine;
		String lastLine = "";
		try (FileWriter writer = new FileWriter(new File(OUTPUT + fileName + "_out.tsv"));
			 Scanner scanner = new Scanner(fileContents))
		{
			writeHeaders(writer, fileName);
			scanner.useDelimiter("\n");
			while (scanner.hasNext())
			{
				currentLine = scanner.next();
				if (isBadData(fileName, currentLine.split("\t")) ||
						isDuplicate(currentLine, lastLine))
				{
					// Ignore bad data & duplicates
					continue;
				}
				writer.write(currentLine + "\n");
				lastLine = currentLine;
			}
		}
	}

	private static void writeHeaders(FileWriter writer, String fileName)
	{
		try
		{
			if (fileName.contains(APPLICATIONS))
			{
				writer.write("Time\tAppID\tAccessType\n");
			}
			else if (fileName.contains(MODES))
			{
				writer.write("Time\tMode\n");
			}
			else if (fileName.contains(POWER))
			{
				writer.write("Time\tStatus\tLevel\tPlugged\n");
			}
			else if (fileName.contains(MESSAGES))
			{
				writer.write("Time\tNumber\tReceived\n");
			}
			else if (fileName.contains(WIFI))
			{
				writer.write("Time\tBSSID\n");
			}
			else if (fileName.contains(BLUETOOTH))
			{
				writer.write("Time\tName\tAddress\tClass\tChange\n");
			}
			else if (fileName.contains(BROWSER))
			{
				writer.write("Time\tAddress\n");
			}
			else if (fileName.contains(CALLS))
			{
				writer.write("Time\tCallID\tIncoming\tDuration\n");
			}
			else if (fileName.contains(DEVICES))
			{
				writer.write("Time\tDeviceID\tChange\n");
			}
			else if (fileName.contains(FILES))
			{
				writer.write("Time\tFileID\tAccessType\n");
			}
			else if (fileName.contains(LOCATION))
			{
				writer.write("Time\tDistance1\tDistance2\tDistance3\n");
			}
			else if (fileName.contains(MOBILE))
			{

				writer.write("Time\tCellID\tType\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void process(String fileName)
	{
		String line;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		try (BufferedReader reader = new BufferedReader(new FileReader(OUTPUT + fileName + "_out.tsv"));
			 FileWriter writer = new FileWriter(new File(OUTPUT + fileName + "_hr.tsv")))
		{

			if ((line = reader.readLine()) != null)
			{
				// Write headers without change
				writer.write(line + "\n");
			}
			while ((line = reader.readLine()) != null)
			{
				String[] items = line.split("\t");
				// Get date
				line = sdf.format(new Date(Long.parseLong(items[0])));

				if (!(fileName.contains(MODES) || fileName.contains(POWER) || fileName.contains(LOCATION)))
				{
					Integer hash = items[1].hashCode();
					items[1] = hash.toString();
				}

				StringBuilder builder = new StringBuilder(line);
				for (String item : items)
				{
					builder.append("\t");
					builder.append(item);
				}
				builder.append("\n");
				writer.write(builder.toString());
			}
		}
		catch (NumberFormatException | IOException e)
		{
			e.printStackTrace();
		}
	}

	private static boolean isDuplicate(String currentLine, String lastLine)
	{
		return currentLine.equals(lastLine);
	}

	private static boolean isBadData(String fileName, String[] items)
	{
		if (items.length < 2 || "".equals(items[1])) return true;
		if (fileName.contains(LOCATION))
		{
			return items[1].equals(items[2]) && items[2].equals(items[3]);
		}
		else if (fileName.contains(POWER))
		{
			for (int i = 1; i < items.length; i++)
			{
				if (Float.parseFloat(items[i]) == -1) return true;
			}
			return false;
		}
		// TODO: Handle other cases of bad data
		return false;
	}
}