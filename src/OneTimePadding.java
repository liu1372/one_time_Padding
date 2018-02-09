import java.io.*;
import java.util.Random;
import java.util.Scanner; 

public class OneTimePadding {
	
	//convert char to decimal number by using ASCII code, and then to binary in string 
	public static String stringToInt(String inString) {
		String result = "";
	
		for( int i = 0; i < inString.length();i++) {
			int a = (int)inString.charAt(i);
			//combine all char in binary together
			result = result + "" + decToBin(a,8);
		}
		return result;
	}

	
	//convert the decimal number to binary number in string format 
	public static String decToBin(int inInterger,int length) {
		int n = inInterger;
		int a;
		String result = "";
		while(n > 0)
        {
            a = n % 2;
            result = a + "" + result;
            n = n / 2;
        }
		//put 0s in front of it make length bits for each char
		if(result.length() != length) {
			int zeros = length-result.length();
			for(int i = 0; i < zeros;i++) {
				result = "0" + "" + result;
			}
		}
		return result;
	}
	
	
	//encryption function 
	public static String encryption(String plainText, String SecretKey) {
		String ciphereText = "";
		int a,b,c;
		//encrypt the plain text to cipher text in binary number 
		for(int i = 0; i < plainText.length(); i++) {
			//XOR function 
			a = Integer.parseInt(String.valueOf(plainText.charAt(i)));
			b = Integer.parseInt(String.valueOf(SecretKey.charAt(i)));
			c = (a+b) % 2;
			//Combine all cipher binary number together 
			ciphereText = ciphereText + "" + c;
		}
		return ciphereText;
	}
	
	//Decryption function 
	public static String decryption(String ciphereText, String SecretKey) {
		String plainText = "";
		String plainBin = "";
		int a,b,c;
		//encrypt the plain text to cipher text in binary number 
		for(int i =0; i < ciphereText.length(); i++) {
			//XOR functiion
			a = Integer.parseInt(String.valueOf(ciphereText.charAt(i)));
			b = Integer.parseInt(String.valueOf(SecretKey.charAt(i)));
			c = (a+b) % 2;
			plainBin = plainBin + "" + c;
		}
		//transfer binary number in string to normal text by using ASCII code
		int j = 0;
		while(j < plainBin.length()) {
			//every 8 digits is one char
			int ascii = Integer.parseInt(plainBin.substring(j, j+8),2);  
			char temp = (char) ascii;
			plainText = plainText+String.valueOf(temp);
			j = j + 8;
		}
		return plainText;	
	}
	
	//key generation function 
	public static String keyGen(int l) {
		String key = "";
		//input the length of key and then random generate 1 or 0 on each digit
		for (int i =0; i < l;i++) {
			int temp = new Random().nextInt(2);
			key = key + "" + temp;
		}
		return key;
	}
	
	
	
    public static void main(String[] args) throws IOException {
    	Scanner reader = new Scanner(System.in);  
    //ask user which function to run 
    	System.out.println("Which function do you what to do? you can choose from 1 for encryption, 2 for decryption, 3 for generate key and 4 for distribution.");
    	int n = reader.nextInt(); 
    	//if 1 then do encryption function 
    	if(n == 1) {
    		//ask user for key 
    		System.out.println("please enter the path for key: ");
    		String keyPath = reader.next();
    		//ask for plain text path 
    		System.out.println("please enter the path for plain text: ");
    		String plainPath = reader.next();
    		//ask for cipher text path to store the cipher text 
    		System.out.println("please enter the path for cipher text: ");
    		String cipherePath = reader.next();
    		long startTime = System.nanoTime();
    		//get the secret key from the key file	  
        	BufferedReader br1 = new BufferedReader(new FileReader(keyPath));
        	String  secretKey = br1.readLine();

        //read the plain text and convert to binary 
        BufferedReader br2 = new BufferedReader(new FileReader(plainPath));
      	String  pText = br2.readLine();
   
      	//if the text is not 32 bits long then return error
      	int temp = pText.length()*8;
     	if(secretKey.length()!=temp) {
     		System.out.println("error: length is incorrect!");
     	}else {
     		String pt = stringToInt(pText);
     		String encM = encryption(pt,secretKey);
     		long endTime   = System.nanoTime();
     		long totalTime = endTime - startTime;
     		System.out.println("The running time is: "+ totalTime +"nanoseconds");
     		//write the cipher text to the designation
     		PrintWriter pw = new PrintWriter(cipherePath);
     		
     	    pw.write(encM);
     	    pw.close();
     	    br1.close();
     	    br2.close();
     	}
    //if 2 then do decryption 
    	}else if(n == 2) {
    		//ask user for key 
    		System.out.println("please enter the path for key: ");
    		String keyPath = reader.next();
    		//ask for cipher text path 
    		System.out.println("please enter the path for cipher text: ");
    		String cipherePath = reader.next();
    		//ask for path where to store the decrypted result  
    		System.out.println("please enter the path for result text: ");
    		String plainPath = reader.next();
    		//get the key
    		BufferedReader br1 = new BufferedReader(new FileReader(keyPath));
        	String  secretKey = br1.readLine();
        	//read the plain text and convert to binary 
        BufferedReader br2 = new BufferedReader(new FileReader(cipherePath));
        String  cText = br2.readLine();
        if(cText.length()!= secretKey.length()) {
     		System.out.println("error: length is incorrect!");
     	}else {
     		String resultText = decryption(cText,secretKey);
     		//write the result text to the designation
     		PrintWriter pw = new PrintWriter(plainPath);
     	    pw.write(resultText);
     	    pw.close();
     	    br1.close();
     	    br2.close();
     	}
        	
    //Generating new key  	
    	}else if(n==3) {
    		//ask user for the length of the new key  
    		System.out.println("please enter the length of key: ");
    		int keyLength = reader.nextInt();
    		System.out.println("please enter the path where to store the new key: ");
    		String keyPath = reader.next();
    		String newKey = keyGen(keyLength);
    		PrintWriter pw = new PrintWriter(keyPath);
     	pw.write(newKey);
     	pw.close();
    	}else if(n==4) {
    		int[] resultCount = new int[8];
    		String key = null;
    		int index = 0;
    		for (int i = 0;i < 5000;i++) {
    			key = keyGen(3);
    			index = Integer.parseInt(key,2);
    			resultCount[index]= resultCount[index]+1;
    		}
    		//print the distribution of all new key with length 3
    		for (int i = 0;i<8;i++) {
    			key = decToBin(i,3);
    			System.out.println(key + ": "+resultCount[i]);
    		}
    	}else {
    		System.out.println("Error: Wrong commond!");
    	}

    	//close readder
    	reader.close(); 
    }




}
