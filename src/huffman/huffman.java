package huffman;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class huffman {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String task = "";
		while (!task.equals("q")) {
			System.out.println("1- compress");
			System.out.println("2- decompress");
			System.out.print("enter your option or q to exit: ");
			task = scan.next();
			if (task.equals("1")) {
				String compressedMsg = "";
				compression cp = new compression();
				try {
					File plainText = new File("input-plain.txt");
					Scanner inputReader = new Scanner(plainText);
					while (inputReader.hasNextLine()) {
						compressedMsg += cp.compress(inputReader.nextLine());
					}
					inputReader.close();
				} catch (Exception e) {
					System.out.println("An error occurred.");
				}
				try {
					File compressedFile = new File("compressed.txt");
					if (!compressedFile.createNewFile()) {
						compressedFile.delete();
						compressedFile.createNewFile();
					}
					FileWriter compressedWriter = new FileWriter(compressedFile);
					compressedWriter.write(compressedMsg + "\n");
					compressedWriter.close();
				} catch (Exception e) {
					System.out.println("An error occurred.");
				}
			} else if (task.equals("2")) {
				decompression decp = new decompression();
				decp.decompress();
			}
		}
		scan.close();
	}

}
