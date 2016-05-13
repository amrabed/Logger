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

	public static void main(String[] args)
	{
		try
		{
			for (String fileName : args)
			{
				String fileContents = readFileAsString("./" + fileName + ".csv");
				fileContents = fileContents.replaceAll("\\s", "");
				fileContents = fileContents.replaceAll("([0-9]{13})(\\|)", "\n$1\t");

				if (fileName.contains("applications"))
				{
					fileContents = fileContents.replaceAll("(\\|)([a-z]{3,})", "\t$2");
				}
				else if (fileName.contains("calls"))
				{
					fileContents = fileContents.replaceAll("(\\|)([0-9]{1})(\\|)", "\t$2\t");
				}
				else if (fileName.contains("bluetooth"))
				{
					fileContents = fileContents.replaceAll("(\\|)([0-9]{1,})(\\|)", "\t$2\t");
				}
				else if (fileName.contains("messages"))
				{
					// TODO Handle last line in file
					fileContents = fileContents.replaceAll("(\\|)([0-9]{1}[\n])", "\t$2");
				}
				else if ((fileName.contains("files")) || (fileName.contains("mobile")))
				{
					fileContents = fileContents.replaceAll("(\\|)([A-Z]{4,})", "\t$2");
				}
				else if ((fileName.contains("wifi")) || (fileName.contains("browser")))
				{
					// Do no more!
				}
				else
				// modes, power, locations
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
			System.err.println(e);
		}
	}

	private static String readFileAsString(String filePath) throws IOException
	{
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024 * 1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1)
		{
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	private static void writeOutputFile(String fileName, String fileContents) throws IOException
	{
		String currentLine, lastLine = "";
		FileWriter writer = new FileWriter(new File("./output/" + fileName + "_out.tsv"));
		writeHeaders(writer, fileName);
		Scanner scanner = new Scanner(fileContents);
		scanner.useDelimiter("\n");
		while (scanner.hasNext())
		{
			currentLine = scanner.next();
			if (isBadData(fileName, currentLine.split("\t")))
			{
				// Ignore bad data
				continue;
			}
			if (isDuplicate(currentLine, lastLine))
			{
				// Ignore duplicates
				continue;
			}
			writer.write(currentLine + "\n");
			lastLine = currentLine;
		}
		writer.close();
	}

	public static void writeHeaders(FileWriter writer, String fileName)
	{
		try
		{
			if (fileName.contains("applications"))
			{
				writer.write("Time\tAppID\tAccessType\n");
			}
			else if (fileName.contains("modes"))
			{
				writer.write("Time\tMode\n");
			}
			else if (fileName.contains("power"))
			{
				writer.write("Time\tStatus\tLevel\tPlugged\n");
			}
			else if (fileName.contains("messages"))
			{
				writer.write("Time\tNumber\tReceived\n");
			}
			else if (fileName.contains("wifi"))
			{
				writer.write("Time\tBSSID\n");
			}
			else if (fileName.contains("bluetooth"))
			{
				writer.write("Time\tName\tAddress\tClass\tChange\n");
			}
			else if (fileName.contains("browser"))
			{
				writer.write("Time\tAddress\n");
			}
			else if (fileName.contains("calls"))
			{
				writer.write("Time\tCallID\tIncoming\tDuration\n");
			}
			else if (fileName.contains("devices"))
			{
				writer.write("Time\tDeviceID\tChange\n");
			}
			else if (fileName.contains("files"))
			{
				writer.write("Time\tFileID\tAccessType\n");
			}
			else if (fileName.contains("location"))
			{
				writer.write("Time\tDistance1\tDistance2\tDistance3\n");
			}
			else if (fileName.contains("mobile"))
			{

				writer.write("Time\tCellID\tType\n");
			}
		}
		catch (IOException e)
		{
			System.err.println(e);
		}
	}

	public static void process(String fileName)
	{
		String line;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("./output/" + fileName + "_out.tsv"));
			FileWriter writer = new FileWriter(new File("./output/" + fileName + "_hr.tsv"));
			if ((line = reader.readLine()) != null)
			{
				// Write headers without change
				writer.write(line + "\n");
			}
			while ((line = reader.readLine()) != null)
			{
				String items[] = line.split("\t");
				// Get date
				line = sdf.format(new Date(Long.parseLong(items[0])));

				if (!(fileName.contains("mode") || fileName.contains("power") || fileName.contains("location")))
				{
					Integer hash = items[1].hashCode();
					items[1] = hash.toString();
				}

				for (int i = 1; i < items.length; i++)
				{
					line += "\t" + items[i];
				}
				line += "\n";
				writer.write(line);
			}
			reader.close();
			writer.close();
		}
		catch (NumberFormatException e)
		{
			System.err.println(e);
		}
		catch (IOException e)
		{
			System.err.println(e);
		}
	}

	public static boolean isDuplicate(String currentLine, String lastLine)
	{
		return currentLine.equals(lastLine);
//		if (currentLine.equals(lastLine))
//		{
//			return true;
//		}
//		try
//		{
//			String content1[] = currentLine.split("\t", 2);
//			String content2[] = lastLine.split("\t", 2);
//			return content1[1].equals(content2[1]);
//		}
//		catch (Exception e)
//		{
//			return false;
//		}
	}

	public static boolean isBadData(String fileName, String items[])
	{
		// TODO: Handle other cases of bad data
		if (items.length < 2 || items[1].equals("")) return true;
		if (fileName.contains("location"))
		{
			if (items[1].equals(items[2]) && items[2].equals(items[3]))
			{
				return true;
			}
		}
		else if (fileName.contains("calls"))
		{
			// if(items.length < 4 || items[1].equals(""))
			// {
			// return true;
			// }
		}
		else if (fileName.contains("devices"))
		{

		}
		else if (fileName.contains("power"))
		{
			for (int i = 1; i < items.length; i++)
			{
				if (Float.parseFloat(items[i]) == -1) return true;
			}
			return false;
		}
		else if (fileName.contains("bluetooth"))
		{
		}
		else if (fileName.contains("messages"))
		{
		}
		else if ((fileName.contains("files")) || (fileName.contains("mobile")))
		{
		}
		else if ((fileName.contains("wifi")) || (fileName.contains("browser")))
		{
		}
		else
		// modes, locations
		{
		}
		return false;
	}
}
// // Decode 2nd column
// // Mac mac = Mac.getInstance("hmacSHA256");
// // mac.init(new SecretKeySpec(items[1].getBytes("US-ASCII"),
// "HmacSHA256"));
// // items[1] = new String(mac.doFinal(), "US-ASCII");
//
