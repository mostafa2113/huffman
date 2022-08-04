package huffman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class decompression {
	private HashMap<String, String> tabel = new HashMap<String, String>();

	public void decompress() {
		// read table from table file
		try {
			File tableFile = new File("huffmanTabel.txt");
			Scanner tableReader = new Scanner(tableFile);
			while (tableReader.hasNextLine()) {
				StringTokenizer strTok = new StringTokenizer(tableReader.nextLine(), ":");
				tabel.put(strTok.nextToken(), strTok.nextToken());
			}
			tableReader.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
		}
		try {
			File compressedFile = new File("compressed.txt");
			File decompressedFile = new File("decompressed.txt");
			Scanner compressedReader = new Scanner(compressedFile);
			if(!decompressedFile.createNewFile()) {
				decompressedFile.delete();
				decompressedFile.createNewFile();
			}
			else {
				decompressedFile.createNewFile();
			}
			FileWriter decompressedWriter = new FileWriter(decompressedFile, true);
			while (compressedReader.hasNextLine()) {
				decmp(compressedReader.nextLine(), decompressedWriter);
			}
			compressedReader.close();
			decompressedWriter.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
		}
	}

	private void decmp(String cmpMsg, FileWriter decompressedWriter) {
		// read the bytes and turn it to plain text
		String plainTxt = "";
		String current = "";
		for(int i=0;i<cmpMsg.length();i++) {
			current += cmpMsg.charAt(i);
			if(tabel.containsKey(current)) {
				plainTxt += tabel.get(current);
				current = "";
			}
		}
		try {
			decompressedWriter.append(plainTxt);
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}

//	public static void main(String[] args) {
//		decompression decp = new decompression();
//		decp.decompress();
//	}

}
