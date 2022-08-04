package huffman;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class compression {
	public class HuffmanTreeNode {
		private int count = 0;
		private String code = "0";
		private String msgChars = "";
		public HuffmanTreeNode left = null;
		public HuffmanTreeNode right = null;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getMsgChars() {
			return msgChars;
		}

		public void setMsgChars(String msgChars) {
			this.msgChars = msgChars;
		}

		public HuffmanTreeNode(int count, String msgChars) {
			this.count = count;
			this.msgChars = msgChars;
		}

		@Override
		public String toString() {
			return "HuffmanTreeNode [msgChars=" + msgChars + ", count=" + count + ", code=" + code + "]";
		}

	}

	public class HuffmanTreeNodeComparator implements Comparator<HuffmanTreeNode> {
		@Override
		public int compare(HuffmanTreeNode arg0, HuffmanTreeNode arg1) {
			return arg0.count - arg1.count;
		}
	}

	public static void selectionSort(ArrayList<Integer> arr, ArrayList<Character> sarr) {
		int min;
		for (int i = 0; i < arr.size() - 1; i++) {
			min = i;
			for (int j = i + 1; j < arr.size(); j++) {
				if (arr.get(j) < arr.get(min)) {
					Collections.swap(arr, j, min);
					Collections.swap(sarr, j, min);
				}
			}
		}
	}

//	public void Sum(int[] arr, String[] sarr) {
//		for (int i = 0; i < arr.length - 2; i++) {
//			selectionSort(arr, sarr);
//			arr[i] = arr[i] + arr[i + 1];
//			arr[i + 1] = -1;
//			sarr[i] = sarr[i] + sarr[i + 1];
//			sarr[i + 1] = "";
//		}
//		selectionSort(arr, sarr);
//	}

	public class countTable {
		private ArrayList<Integer> count = new ArrayList<Integer>();
		private ArrayList<Character> msgChar = new ArrayList<Character>();
//		private ArrayList<Boolean> used = new ArrayList<Boolean>();
		private int length = 0;

		public countTable(String msg) {
			char[] carr = msg.toCharArray();
			int k = 0;
			String checker = "";
			for (int i = 0; i < msg.length(); i++) {
				k = 0;
				if (checker.contains(carr[i] + ""))
					continue;
				for (int j = 0; j < msg.length(); j++) {
					if (carr[i] == carr[j])
						k++;
				}
				msgChar.add(carr[i]);
				count.add(k);
//				used.add(false);
				length++;
				checker += carr[i];
			}
			selectionSort(count, msgChar);
		}

		public int length() {
			return this.length;
		}

		@Override
		public String toString() {
			return "countTable [count=" + count + ", msgChar=" + msgChar + "]";
		}

		public int getCountAt(int index) {
			return this.count.get(index);
		}

		public char getChartAt(int index) {
			return msgChar.get(index);
		}
	}

//A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED
	public String compress(String msg) {
		countTable ct = new countTable(msg);
		PriorityQueue<HuffmanTreeNode> huffmanTreeBuilder = new PriorityQueue<HuffmanTreeNode>(
				new HuffmanTreeNodeComparator());
		for (int i = 0; i < ct.length(); i++) {
			HuffmanTreeNode huffmanNode = new HuffmanTreeNode(ct.getCountAt(i), ct.getChartAt(i) + "");
			huffmanTreeBuilder.add(huffmanNode);
		}
		HuffmanTreeNode root = null;
		while (huffmanTreeBuilder.size() != 1) {
			HuffmanTreeNode left = huffmanTreeBuilder.remove();
			left.setCode("0");
			HuffmanTreeNode right = huffmanTreeBuilder.remove();
			right.setCode("1");
			root = new HuffmanTreeNode(left.getCount() + right.getCount(), left.getMsgChars() + right.getMsgChars());
			root.left = left;
			root.right = right;
			huffmanTreeBuilder.add(root);
		}
		HashMap<String, String> encodeTabel = createEncodeTable(root, ct);
		String compressedMsg = "";
		for (int i = 0; i < msg.length(); i++) {
			compressedMsg += encodeTabel.get(msg.charAt(i) + "");
		}
		return compressedMsg;
	}

	private HashMap<String, String> createEncodeTable(HuffmanTreeNode root, countTable ct) {
		HuffmanTreeNode temp = root;
		HashMap<String, String> tabel = new HashMap<String, String>();
		boolean found = false;
		String code = "";
		String arr[] = new String[ct.length()];
		for (int i = 0; i < ct.length(); i++) {
			arr[i] = ct.getChartAt(i) + "";
		}
		for (String s : arr) {
			while (!found) {
				if (temp.left != null && temp.left.getMsgChars().contains(s)) {
					code += temp.left.getCode();
					if (temp.left.getMsgChars().equals(s)) {
						found = true;
					}
					temp = temp.left;

				} else if (temp.right != null && temp.right.getMsgChars().contains(s)) {
					code += temp.right.getCode();
					if (temp.right.getMsgChars().equals(s)) {
						found = true;
					}
					temp = temp.right;
				}
			}
			tabel.put(s, code);
			code = "";
			found = false;
			temp = root;
		}
		try {
			File tabelFile = new File("huffmanTabel.txt");
			if (!tabelFile.createNewFile()) {
				tabelFile.delete();
				tabelFile.createNewFile();
			}
			String theTable = "";
			for (String key : tabel.keySet()) {
				theTable += tabel.get(key) + ":" + key + "\n";
			}
			theTable = theTable.substring(0, theTable.length() - 1);
			FileWriter tabelWriter = new FileWriter(tabelFile);
			tabelWriter.write(theTable);
			tabelWriter.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
		}
		return tabel;
	}

//	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		String msg = scan.next();
//		compression cp = new compression();
//		cp.compress(msg);
//		scan.close();
//	}
}
