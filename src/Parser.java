// Unpacks each instruction into its underlying fields

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    private String fileName;
    private String writeFileName = "out.hack";
    private String line;
    private Instruction instruction;
    private String symbol;
    private String symbolNumeric;
    private String dest;
    private String comp;
    private String jump;
    private SymbolTable symbolTable;

    public Parser (String fileName, SymbolTable symbolTable) {
        this.fileName = fileName;
        this.symbolTable = symbolTable;
        List<String> binaryText = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(fileName);
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                String binary = "";
                line = scanner.nextLine();
                line = line.replaceAll("\\s+","");
                String[] parts = line.split("[//]");
                String prepLine = parts[0];
                if (prepLine.length() > 0) {
                    getType(prepLine);
                    if (getInstruction() == Instruction.aInstruction) {
                        parseAInstruction(prepLine);
                        this.setSymbolNumeric(symbolTable.replaceVariable(getSymbol()));
                        Code nextCode = new Code(this.getSymbolNumeric());
                        binary = nextCode.makeBinary();
                    } else if (getInstruction() == Instruction.lInstruction) {
                        parseLInstruction(prepLine);
                    } else if (getInstruction() == Instruction.cInstruction) {
                        parseCInstruction(prepLine);
                        Code nextCode = new Code(getDest(), getComp(), getJump());
                        binary = nextCode.makeBinary();
                    }
                    if (binary.equals("")) {
                        //do nothing
                    } else {
                        binaryText.add(binary);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(writeFileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            for(String binary : binaryText) {
                bufferedWriter.write(binary);
                bufferedWriter.newLine();
            }
            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
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

    public String getSymbolNumeric() {
        return symbolNumeric;
    }

    public void setSymbolNumeric(String symbolNumeric) {
        this.symbolNumeric = symbolNumeric;
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
            String[] parts = instruction.split("=");
            dest = parts[0];
            String compJump = parts[1];
            String[] parts2 = compJump.split(";");
            comp = parts2[0];
            jump = parts2[1];
        } else if (instruction.contains("=")) {
            String[] parts = instruction.split("=");
            dest = parts[0];
            comp = parts[1];
            jump = "";
        } else if (instruction.contains(";")) {
            String[] parts = instruction.split(";");
            dest = "";
            comp = parts[0];
            jump = parts[1];
        }
    }
}
