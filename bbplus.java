import java.io.*;
import java.util.HashMap;
import java.util.ArrayList; //used for storing file

public class bbplus {

	public static int tryparseint(String v, HashMap<String, Integer> vars){ //We need to do a fair bit of parsing for our operations so this will attempt it or throw an exception
		int x = 0;
		try {
    		x = Integer.parseInt(v); //succeeds if v is integer, can return the integer to use
    		return x;
		} catch (NumberFormatException e) {
    		try{
    			x = vars.get(v); //if v isnt an integer, it should be referenceing a variable and we can nab the integer from it
    		} catch (Exception e2){
    			System.out.println(v + " is not recognised in the var list or is not an integer."); //if we're here then you dun goofed
    		}
		}
		return x;
	}

	public static String detectVarsRef (String ins, HashMap<Integer, String> varref, HashMap<String, Integer> vars){ //if any argument uses a VARS[x] then use this
		if(ins.length() > 4){
			if(ins.substring(0,4).equals("VARS")){ //special case in loop, we can reference our variable stack directly
				int arg;
				try{
					arg = vars.get(ins.substring(5, ins.length()-1)); //assume it is a variable reference
				} catch (Exception e){
					arg = Integer.parseInt(ins.substring(5, ins.length()-1)); //if its not, assume (and hope) its a number
				}
				ins = varref.get(arg);
			}
		}
		return ins;
	}

	public static void main(String[] args) throws InterruptedException{
		ArrayList<String> lines = new ArrayList<String>(); //all the lines in the program so we can run through it
		HashMap<String, Integer> vars = new HashMap<String, Integer>(); //all the vars we may discover in our program
		HashMap<Integer, String> varref = new HashMap<Integer, String>(); //reference for all vars in program
		HashMap<String, Integer> fns = new HashMap<String, Integer>(); //all our subroutine
		int i = 0; //will be the PC of sorts
		int varc = 0; //var count of program
		int y = 0, z = 0; //vars for operand values
		int sp = 1, fsp = 1, ifsp = 1; //need to keep track of where our loops are, also useful for subroutines later
		int[] istack = new int[64]; //64 is an arbitrary number but I can't imagine exceeding it
		String[] vstack = new String[64]; //stack for last vars for while loops
		int[] cstack = new int[64]; //condition stack, holds the conditions for each while loop
		int[] fstack = new int[64]; //stack for function returns... its kinda ridiculous how many stacks we have at this point but my efforts to combine them proved futile
		int[] ifstack = new int[64]; //sorry for the 5th stack but we need to keep track of these all separately as they may be on different levels at any point. Holds the lines to jump to if condition doesnt hold
		int nstr = 0; //number of strings, cumulative count

		System.out.println("Input file name (must be in same directory as program, *.txt): ");
		//bring the file into the arraylist
		try (BufferedReader reader = new BufferedReader(new FileReader(System.console().readLine()+".txt"))) {
    		while (reader.ready()) { //remove spaces since we can just use "end"/"endfn"/"endif" statements
    			char[] line = reader.readLine().toCharArray();
    			while(line.length == 0 || line[0] == ';'){ //apparently its empty but its not null... i dont know how to check for that since '' is not valid so length is the only check i can do
    				line = reader.readLine().toCharArray(); //skip lines until its not empty
    			}
    			int c = 0; //using c for each character, finding the first non-space character to get rid of indents
    			int s = 0; //gonna also dig for the semi colon and remove it, so we can have whatever for comments
    			for(c = 0; c < line.length-1; c++){
    				if(line[c] != ' '){
    					break;
    				}
    			}
    			for(s = 0; s < line.length-1; s++){
    				if(line[s] == ';'){
    					break;
    				}
    			}
    			String l = (new String(line)).substring(c, s);
        		lines.add(l);
    		}
		}
		catch (IOException e) {
			System.out.println("Error reading file.");
		}
		
		//begin execution
		while(i <= lines.size()-1){
			String[] ins = lines.get(i).substring(0, lines.get(i).length()).split(" "); //instruction
			for(int s = 0; s < ins.length; s++){ //if any part of the instruction is a VARS reference, return the variable attached to the index
				ins[s] = detectVarsRef(ins[s], varref, vars);
			}
			//perform instruction
			switch(ins[0]){
				case "clear": //sets var to 0
					vars.put(ins[1], 0);
					varref.put(varc, ins[1]);
					varc++;
					break;
				case "incr": //increments var by 1
					vars.put(ins[1], vars.get(ins[1])+1);
					//System.out.println(ins[1] + ": " + vars.get(ins[1]));
					break;
				case "decr": //decrements var by 1
					vars.put(ins[1], vars.get(ins[1])-1);
					System.out.println(ins[1] + ": " + vars.get(ins[1]));
					break;
				case "while": //while loop
					y = tryparseint(ins[3], vars);
					switch(ins[2]){
						case "not": //wish i could compact this but i cant see a way to save repetition easily
							if(vars.get(ins[1]) != y){
								istack[sp] = i;
								vstack[sp] = ins[1];
								cstack[sp] = y;
								sp++;
							} break;
						case "==":
							if(vars.get(ins[1]) == y){
								istack[sp] = i;
								vstack[sp] = ins[1];
								cstack[sp] = y;
								sp++;
							} break;
						case ">":
							if(vars.get(ins[1]) > y){
								istack[sp] = i;
								vstack[sp] = ins[1];
								cstack[sp] = y;
								sp++;
							} break;
						case "<":
							if(vars.get(ins[1]) < y){
								istack[sp] = i;
								vstack[sp] = ins[1];
								cstack[sp] = y;
								sp++;
							} break;
					} break;
				case "end": //break
					Thread.sleep(100); //in case of infinite while loop, slow it down. kept it in since i like the effect.
					if (vars.get(vstack[sp-1]) == cstack[sp-1]){ //if the variable of the while loop is == condition
						sp--;
					}
					else{
						i = istack[sp-1];
					}
					break;
				case "+": //add 2 vars. Expected form: `+ X Y Z` performs X = Y + Z. Y or Z can be numbers or variables
					y = tryparseint(ins[2], vars);
					z = tryparseint(ins[3], vars);
					vars.put(ins[1], y+z);
					break;
				case "-": //sub 2 vars. Expected form: `- X Y Z` performs X = Y - Z. Y or Z can be nums or vars
					y = tryparseint(ins[2], vars);
					z = tryparseint(ins[3], vars);
					vars.put(ins[1], y-z);
					break;
				case "*": //mult 2 vars. Expected form: `* X Y Z` performs X = Y * Z. Y or Z can be nums or vars
					y = tryparseint(ins[2], vars);
					z = tryparseint(ins[3], vars);
					vars.put(ins[1], y * z);
					break;
				case "/": //div 2 vars. Expected form: `/ X Y Z` performs X = Y / Z. Y or Z can be nums or vars. Integer division.
					y = tryparseint(ins[2], vars);
					z = tryparseint(ins[3], vars);
					vars.put(ins[1], y / z);
					break;
				case "%": //mod 2 vars. Expected form: `% X Y Z` performs X = Y % Z. Y or Z can be nums or vars.
					y = tryparseint(ins[2], vars);
					z = tryparseint(ins[3], vars);
					vars.put(ins[1], y % z);
					break;
				case "set": //Sets (and initialises if necessary) X to Y. Y can be num or var.
					y = tryparseint(ins[2], vars);
					varref.put(varc, ins[1]);
					varc++;
					vars.put(ins[1], y);
					break;
				case "fn": //subroutine declaration
					fns.put(ins[1], i); //put it on the function list with its line number + 1 (to the first executable line)
					while(!(ins[0].equals("endf"))){
						i++;
						ins = lines.get(i).substring(0, lines.get(i).length()).split(" "); //advance through the program until the end
					}
					break;
				case "endf": //denotes end of subroutine
					i = fstack[fsp-1];
					fsp--;
					break;
				case "in": //take user input for variable. Can reference another variable.
					y = tryparseint(System.console().readLine(), vars);
					vars.put(ins[1], y);
					break;
				case "out": //output as int
					y = tryparseint(ins[1], vars);
					System.out.print(y);
					break;
				case "outc": //output as char
					y = tryparseint(ins[1], vars);
					System.out.print((char)y);
					break;
				case "data": //input string as sequence of char vars named c_n
					for(int a = 0; a < lines.get(i).substring(5, lines.get(i).length()).length(); a++){
						vars.put("c"+Integer.toString(a)+Integer.toString(nstr), (int)(lines.get(i).substring(5, lines.get(i).length()).toCharArray()[a]));
						varref.put(varc, "c"+Integer.toString(a)+Integer.toString(nstr));
						varc++;
					}
					nstr++;
					break;
				case "if": //if [var] [operator] [var] do;
					y = tryparseint(ins[1], vars); //get the values to compare, be they variable references or integers
					z = tryparseint(ins[3], vars);
					int nest = 0; //need to find the right endif, on the same level as our if, in case of nested ifs
					int currentLine = i;
					int endifLine = i;
					while((!(ins[0].equals("endif")) || nest == 0)){
						//System.out.println("i: " + i + " | ins[0] = " + ins[0]);
						i++;
						ins = lines.get(i).substring(0, lines.get(i).length()).split(" "); //advance through the program until the endif
						if(ins[0].equals("if")){
							nest++; //need to keep track of what level we're on, can only go to an else if nest == 0
						}
						if(ins[0].equals("else")){
							nest++; //spotting an else is a part of a nested if, hence nest++
						}
						if(ins[0].equals("endif")){
							nest--; //endif is only one to reduce nest level
						}
					}
					endifLine = i; //reached the endif, so store this if we need to skip to it
					i = currentLine; //revert back to current line
					ins = lines.get(i).substring(0, lines.get(i).length()).split(" "); //get the instruction again since we need it
					switch(ins[2]){
						case "==":
							i = (y == z) ? currentLine : endifLine; //goto currentLine if true, endifLine if false
							break;
						case "<":
							i = (y < z) ? currentLine : endifLine;
							break;
						case ">":
							i = (y > z) ? currentLine : endifLine;
							break;
						case "!=":
							i = (y != z) ? currentLine : endifLine;
							break;
						default:
							break;
					} break;
					case "endif": //doesnt make sense without if statement, handled with if statement
						break;
					case "else": //doesnt make sense without if statement, handled with if statement
						break;
					case "goto": //goes to a direct line, useful in some cases
						i = tryparseint(ins[1], vars);
						break;
					case "rand": //rand X Y Z. Picks random number between Y and Z and stores in X. Requires Y < Z.
						y = tryparseint(ins[2], vars);
						z = tryparseint(ins[3], vars);
						vars.put(ins[1], (int)(Math.random() * ((((z - y) + 1)) + y)));
						break;
					case "skip": //skip line. Useful in some cases,
						break;

				default: //if its not a known instruction then its an error or a subroutine jump
					try{
						fstack[fsp] = i;
						fsp++;
						i = fns.get(ins[0]); //the line of the function
					} catch (Exception e){
						System.out.println("Unrecognised instruction or subroutine: " + ins[0]);
					} break;
			}
			i++;
		}
	}
}
