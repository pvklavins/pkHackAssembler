// Translates each field into its corresponding binary value

public class Code {

    private boolean aInstruction;
    private String symbol;
    private String dest;
    private String comp;
    private String jump;
    public Code (String symbol){
        aInstruction = true;
        this.symbol = symbol;
    }
    public Code (String dest, String comp, String jump){
        aInstruction = false;
        this.dest = dest;
        this.comp = comp;
        this.jump = jump;
    }

    // Affects: this
    // Effects: Returns a binary representation of code in String form.
    public String makeBinary() {
        String tempLine;
        if(aInstruction) {
            Integer number = Integer.parseInt(symbol);
            tempLine = String.format("%16s", Integer.toBinaryString(number)).replace(' ', '0');
        }
        else {
            tempLine = "111" + compConvert(comp) + destConvert(dest) + jumpConvert(jump);
        }
        return tempLine;
    }
    //Affects: This
    //Effects: converts destination symbol to binary String
    public String destConvert(String dest) {
        String d1 = "0";
        String d2 = "0";
        String d3 = "0";
        if(dest.contains("A")){
            d1 = "1";
        }
        if(dest.contains("D")){
            d2 = "1";
        }
        if(dest.contains("M")){
            d3 = "1";
        }
        String binary = d1 + d2 + d3;
        return binary;
    }

    //Affects: This
    //Effects: converts computation symbol to binary String
    public String compConvert(String comp) {
        String a = "0";
        String c1To6 = "000000";
        if (comp.contains("M")) {
            a = "1";
        }
        if (comp.equals("0")){
            c1To6 = "101010";
        }
        else if (comp.equals("1")){
            c1To6 = "111111";
        }
        else if (comp.equals("-1")){
            c1To6 = "111010";
        }
        else if (comp.equals("D")){
            c1To6 = "001100";
        }
        else if (comp.equals("A") | comp.equals("M")){
            c1To6 = "110000";
        }
        else if (comp.equals("!D")){
            c1To6 = "001101";
        }
        else if (comp.equals("!A") | comp.equals("!M")){
            c1To6 = "110001";
        }
        else if (comp.equals("-D")){
            c1To6 = "001111";
        }
        else if (comp.equals("-A") | comp.equals("-M")){
            c1To6 = "110011";
        }
        else if (comp.equals("D+1")){
            c1To6 = "011111";
        }
        else if (comp.equals("A+1") | comp.equals("M+1")){
            c1To6 = "110111";
        }
        else if (comp.equals("D-1")){
            c1To6 = "001110";
        }
        else if (comp.equals("A-1") | comp.equals("M-1")){
            c1To6 = "110010";
        }
        else if (comp.equals("D+A") | comp.equals("D+M")){
            c1To6 = "000010";
        }
        else if (comp.equals("D-A") | comp.equals("D-M")){
            c1To6 = "010011";
        }
        else if (comp.equals("A-D") | comp.equals("M-D")){
            c1To6 = "000111";
        }
        else if (comp.equals("D&A") | comp.equals("D&M")){
            c1To6 = "000000";
        }
        else if (comp.equals("D|A") | comp.equals("D|M")){
            c1To6 = "010101";
        }
        String binary = a + c1To6;
        return binary;
    }

    //Affects: This
    //Effects: converts jump symbol to binary String
    public String jumpConvert(String jump) {
        String binary = "000";
        if(jump == ""){
            binary = "000";
        }
        else if(jump.contains("JGT")){
            binary = "001";
        }
        else if(jump.contains("JEQ")){
            binary = "010";
        }
        else if(jump.contains("JGE")){
            binary = "011";
        }
        else if(jump.contains("JLT")){
            binary = "100";
        }
        else if(jump.contains("JNE")){
            binary = "101";
        }
        else if(jump.contains("JLE")){
            binary = "110";
        }
        else if(jump.contains("JMP")){
            binary = "111";
        }
        return binary;
    }
}
