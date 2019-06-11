import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
 
class Data2 {
	private String first = "";
	private String second = "";
	private String third = "";
	private String str = "";
 
	public Data2(String first, String second, String third, String str) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.str = str;
	}
 
	public String getFirst() {
		return first;
	}
 
	public String getSecond() {
		return second;
	}
 
	public String getThird() {
		return third;
	}
 
	public String getStr() {
		return str;
	}
 
}
 
class Pair2 {
	private String symbol = "";
	private String location = "";
 
	public Pair2(String symbol, String location) {
		this.symbol = symbol;
		this.location = location;
	}
 
	public String getSymbol() {
		return symbol;
	}
 
	public String getLocation() {
		return location;
	}
 
}
 
public class SIC {
 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String[] op_TAB = { "ADD", "ADDF", "ADDR", "AND", "CLEAR", "COMP", "COMPF", "COMPR", "DIV", "DIVF", "DIVR",
				"FIX", "FLOAT", "HIO", "J", "JEQ", "JGT", "JLT", "JSUB", "LDA", "LDB", "LDCH", "LDF", "LDL", "LDS",
				"LDT", "LDX", "LPS", "MUL", "MULF", "MULR", "NORM", "OR", "RD", "RMO", "RSUB", "SHIFTL", "SHIFTR",
				"SIO", "SSK", "STA", "STB", "STCH", "STF", "STI", "STL", "STS", "STSW", "STT", "STX", "SUB", "SUBF",
				"SUBR", "SVC", "TD", "TIO", "TIX", "TIXR", "WD" };
		String[] opCode = { "18", "58", "90", "40", "B4", "28", "88", "A0", "24", "64", "9C", "C4", "C0", "F4", "3C",
				"30", "34", "38", "48", "00", "68", "50", "70", "08", "6C", "74", "04", "E0", "20", "60", "98", "C8",
				"44", "D8", "AC", "4C", "A4", "A8", "F0", "EC", "0C", "78", "54", "80", "D4", "14", "7C", "E8", "84",
				"10", "1C", "5C", "94", "B0", "E0", "F8", "2C", "B8", "DC" };
		ArrayList<Pair2> SYM_TAB = new ArrayList<>();
		ArrayList<Integer> Length = new ArrayList<>();
		ArrayList<String> Location = new ArrayList<>();
		ArrayList<String> Target = new ArrayList<>();
		ArrayList<Data2> Data = new ArrayList<>();
		FileReader fr = new FileReader("SIC.txt");
		BufferedReader br = new BufferedReader(fr);
		Scanner scn =new Scanner(br);
		int n = 0, num = 0, DecLoc = 0, j = 0, i = 0;
		String HexLoc,str1=" ",str2=" ",str3=" ";
		boolean isOpCode = false,isLine=false,b=false;
		while(scn.hasNext()){
			String tempString = scn.next();
			
			if(tempString.equals("START")||tempString.equals("END")||tempString.equals("WORD")||tempString.equals("BYTE")||tempString.equals("RESB")||tempString.equals("RESW")){
				str2=tempString;
				isOpCode=true;
			}
			else{
				for(i=0;i<op_TAB.length;i++){
					if(tempString.equals(op_TAB[i])){
						str2=tempString;
						isOpCode=true;
						break;
					}
				}
			}
			if(tempString.equals(".")){
				str1=".";
				isLine=true;
			}
			if(str2.equals("RSUB"))
				isLine=true;
			if(!isOpCode){
				str1=tempString;
			}
			else if(!str2.equals("RSUB")){
					str3=scn.next();
					isLine=true;
					b=false;
			}
			if(isLine){
			Data.add(new Data2(str1,str2,str3,str1+str2+str3));
			str1=" ";str2=" ";str3=" ";
			isLine=false;
			isOpCode=false;
			}
		}
		fr.close();
		for (i = 0; i < Data.size(); i++) {
			// 計算位置//
			if (Data.get(i).getStr().contains(".")) {
				Location.add("");
				HexLoc = (Integer.toHexString(DecLoc += Integer.parseInt(Integer.toString(num),16))).toUpperCase();
			} else {
				if (i != 1) {
					HexLoc = (Integer.toHexString(DecLoc += Integer.parseInt(Integer.toString(num),16))).toUpperCase();
					if (i == Data.size() - 1)
						HexLoc = "";
				} else// 起始位置從1開始
					HexLoc = Integer.toString(n);
				Location.add(HexLoc);
			}
			if (i == 0) {//location初始值設定
				n = Integer.parseInt(Data.get(i).getThird());
				DecLoc = Integer.parseInt(Integer.toString(n), 16);
				Location.remove(0);
				Location.add(0, Integer.toString(n));
			}
			// 計算長度//
			if (Data.get(i).getStr().contains(".") || i == 0) {
				num = 0;
			} else if (Data.get(i).getSecond().equals("BYTE")) { // 當運算子有BYTE時2種C和X
				if (Data.get(i).getThird().contains("C")) {// 當C'EOF'時長度3
					char c[] = Data.get(i).getThird()
							.substring(Data.get(i).getThird().indexOf('\'') + 1, Data.get(i).getThird().length() - 1)
							.toCharArray();
					num = c.length;
				} else
					num = 1;// 當X'F1'時長度1
			} else if (Data.get(i).getSecond().contains("RESW")) {// 當RESW時數字*3
				num = Integer.parseInt(Data.get(i).getThird()) * 3;
			} else if (Data.get(i).getSecond().contains("RESB")) {
				num = Integer.parseInt(Integer.toHexString(Integer.parseInt(Data.get(i).getThird())));
			} else // 其餘運算子都是3包含WORD
				num = 3;
			Length.add(num);
			// 建立SYM_TAB//
			if (!Data.get(i).getFirst().contains(" ") && i != 0 && !Data.get(i).getFirst().contains(".")) {
				SYM_TAB.add(new Pair2(Data.get(i).getFirst(), HexLoc));
			}
 
		}
		/// pass2建立目的碼///
		for (i = 0; i < Data.size(); i++) {
			StringBuilder s = new StringBuilder("");
			for (j = 0; j < op_TAB.length; j++) {
				if (Data.get(i).getSecond().equals(op_TAB[j])) {
					s.append(opCode[j]);
					break;
				}
			}
			for (j = 0; j < SYM_TAB.size(); j++) {
				if (Data.get(i).getThird().contains(",X")) {
					if (SYM_TAB.get(j).getSymbol().equals(Data.get(i).getThird().substring(0,Data.get(i).getThird().length()-2))) {// BUFFER有x加8000
						// 先16->10(8000也要轉16->10)再相加，最後再轉16進位
						s.append(Integer.toHexString(
								Integer.parseInt(SYM_TAB.get(j).getLocation(), 16) + Integer.parseInt("8000",16)));
						break;
					}
				} else if (SYM_TAB.get(j).getSymbol().equals(Data.get(i).getThird())) {// SYM_TAB查表若目的碼小於6位元補0
					if (s.length() + SYM_TAB.get(j).getLocation().length() != 6) {
						int len = s.length() + SYM_TAB.get(j).getLocation().length();
						for (int k = 0; k < 6 - len; k++)
							s.append("0");
						s.append(SYM_TAB.get(j).getLocation());
					} else
						s.append(SYM_TAB.get(j).getLocation());
					break;
				}
			}
			/// BYTE處理分為X和C
			if (Data.get(i).getSecond().equals("BYTE")) {
				char c[] = Data.get(i).getThird()
						.substring(Data.get(i).getThird().indexOf('\'') + 1, Data.get(i).getThird().length() - 1)
						.toCharArray();
				for (int k = 0; k < c.length; k++) {
					if (Data.get(i).getThird().contains("C"))
						s.append(Integer.toHexString(c[k]).toUpperCase());// ASCii由10->16目的碼
					else
						s.append(c[k]);
				}
			} else if (Data.get(i).getSecond().equals("WORD")) {/// WORD處理Data.get(i).getThird()直接輸出並補足6位元
				s.append(Integer.toHexString(Integer.parseInt(Data.get(i).getThird())).toUpperCase());
				if (s.length() < 6) {
					s.reverse();
					int len = s.length();
					for (int k = 0; k < 6 - len; k++)
						s.append("0");
					s.reverse();
				} else if (s.length() > 6) {
					s = new StringBuilder(s.substring(2, 8));
				}
			} else if (Data.get(i).getSecond().equals("RSUB"))/// RSUB=>opCode後補滿0六位
				s.append("0000");
			Target.add(s.toString()); // 結果放入目的
			if(Data.get(i).getFirst().equals(".")){//有註解的
				Target.remove(i);
				Target.add("");
			}
				
		}
		Target.remove(Target.size() - 1);
		Target.add("");
		//// 寫檔
		PrintWriter Write = new PrintWriter("SIC_Final.txt");
		System.out.printf("%s\t%-6s\t%-6s\t%-5s\t%s\t\r\n", "位置", " ", "原始敘述", " ", "目的碼");
		Write.printf("%s\t%-6s\t%-6s\t%-5s\t%s\t\r\n", "位置", " ", "原始敘述", " ", "目的碼");
		System.out.println("------------------------------------------------");
		Write.println("------------------------------------------------");
		for (j = 0; j < Data.size(); j++) {
			Write.printf("%s\t%-6s\t%-6s\t%-10s\t%s\t\r\n", Location.get(j), Data.get(j).getFirst(),
					Data.get(j).getSecond(), Data.get(j).getThird(), Target.get(j));
			System.out.printf("%s\t%-6s\t%-6s\t%-10s\t%s\t\r\n", Location.get(j), Data.get(j).getFirst(),
					Data.get(j).getSecond(), Data.get(j).getThird(), Target.get(j));
		}
		Write.close();
		
			
	}
 
}
