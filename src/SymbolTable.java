// Manages the symbol table

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class SymbolTable {
    private HashMap<String, Integer> symbolMap;
    private String fileName;
    private String line;
    private Instruction instruction;
    private String symbol;
    private String dest;
    private String comp;
    private String jump;
    private int romNumber = 0;
    private int varNumber = 16;

    public SymbolTable(String fileName){
        this.fileName = fileName;
        symbolMap = new HashMap<String, Integer>();
        symbolMap.put("SP",0);
        symbolMap.put("LCL",1);
        symbolMap.put("ARG",2);
        symbolMap.put("THIS",3);
        symbolMap.put("THAT",4);
        symbolMap.put("R0",0);
        symbolMap.put("R1",1);
        symbolMap.put("R2",2);
        symbolMap.put("R3",3);
        symbolMap.put("R4",4);
        symbolMap.put("R5",5);
        symbolMap.put("R6",6);
        symbolMap.put("R7",7);
        symbolMap.put("R8",8);
        symbolMap.put("R9",9);
        symbolMap.put("R10",10);
        symbolMap.put("R11",11);
        symbolMap.put("R12",12);
        symbolMap.put("R13",13);
        symbolMap.put("R14",14);
        symbolMap.put("R15",15);
        symbolMap.put("SCREEN",16384);
        symbolMap.put("KBD",24576);

        try {
            FileReader fileReader = new FileReader(fileName);
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] parts = line.split("//");
                String prepLine = parts[0];
                if (prepLine.length() > 0) {
                    getType(prepLine);
                    if (getInstruction() == Instruction.aInstruction) {
                        //parseAInstruction(prepLine);
                        //checkSymbol();
                        setRomNumber(getRomNumber()+1);

                        //Code nextCode = new Code(this.getSymbol());
                        //binary = nextCode.makeBinary();
                    } else if (getInstruction() == Instruction.lInstruction) {
                        parseLInstruction(prepLine);
                        checkSymbol();
                    } else if (getInstruction() == Instruction.cInstruction) {
                        //parseCInstruction(prepLine);
                        setRomNumber(getRomNumber()+1);
                        //Code nextCode = new Code(getDest(), getComp(), getJump());
                        //binary = nextCode.makeBinary();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public int getRomNumber() {
        return romNumber;
    }

    public void setRomNumber(int romNumber) {
        this.romNumber = romNumber;
    }

    public int getVarNumber() {
        return varNumber;
    }

    public void setVarNumber(int varNumber) {
        this.varNumber = varNumber;
    }

    //Affects: this
    //Effects: Checks the type of instruction for this line of code
    public void getType (String instruction){
        String atSign = "@";
        if(instruction.contains(atSign)){
            setInstruction(Instruction.aInstruction);
        }
        else if(instruction.contains("(")){
            setInstruction(Instruction.lInstruction);
        }
        else {
            setInstruction(Instruction.cInstruction);
        }
    }

    //Affects: this
    //Requires: this is an A instruction
    //Effects: sets the symbol of this A instruction
    public void parseAInstruction (String instruction){
        String[] parts = instruction.split("@");
        setSymbol(parts[1]);
    }

    //Affects: this
    //Requires: this is an L instruction
    //Effects: sets the symbol of this instruction
    public void parseLInstruction (String instruction){
        String[] parts = instruction.split("[(]");
        String[] parts2 = parts[1].split("[)]");
        setSymbol(parts2[0]);
    }

    //Affects: this
    //Requires: this is an L instruction
    //Effects: sets the address of this instruction
    public void parseCInstruction (String instruction) {
        if (instruction.contains("=") & instruction.contains(";")) {
            String[] parts = line.split("=");
            dest = parts[0];
            String compJump = parts[1];
            String[] parts2 = compJump.split(";");
            comp = parts2[0];
            jump = parts2[1];
        } else if (instruction.contains("=")) {
            String[] parts = line.split("=");
            dest = parts[0];
            comp = parts[1];
            jump = "";
        } else if (instruction.contains(";")) {
            String[] parts = line.split(";");
            dest = "";
            comp = parts[0];
            jump = parts[1];
        }
    }

    public void checkSymbol() {
        String symbolToCheck = getSymbol();
        boolean inHashMap = symbolMap.containsKey(symbolToCheck);
        if(inHashMap){
            //do nothing
        }
        else {
            if(instruction==Instruction.lInstruction){
                symbolMap.put(symbolToCheck, getRomNumber());
            }
            else if(instruction==Instruction.cInstruction){
                symbolMap.put(symbolToCheck, getVarNumber());
                setVarNumber(getVarNumber()+1);
            }
        }
    }

    public String replaceVariable(String var) {
        String varToCheck = var;
        if(isInteger(varToCheck, 10)){
            return varToCheck;
        }
        boolean inHashMap = symbolMap.containsKey(varToCheck);
        if(inHashMap){
            return String.valueOf(symbolMap.get(varToCheck));
        }
        else {
            symbolMap.put(varToCheck, getVarNumber());
            setVarNumber(getVarNumber()+1);
            return String.valueOf(symbolMap.get(varToCheck));
        }
    }
    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }

}
