// Initializes the I/O files and drives the process

public class Main {

    public static void main(String[] args) {

        String fileName = "Rect.asm";
        SymbolTable symbolTable = new SymbolTable(fileName);
        Parser parser = new Parser(fileName, symbolTable);
    }
}
