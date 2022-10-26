package de.keine_arme_keine_kekse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import de.keine_arme_keine_kekse.parser.MiniJavaParser;
import de.keine_arme_keine_kekse.parser.MiniJavaParserConstants;
import de.keine_arme_keine_kekse.parser.ParseException;
import de.keine_arme_keine_kekse.parser.Token;
import de.keine_arme_keine_kekse.syntaxtree.Program;
import de.keine_arme_keine_kekse.visitor.JasminVisitor;
import de.keine_arme_keine_kekse.visitor.SymbolTableVisitor;
import de.keine_arme_keine_kekse.visitor.TreePrintVisitor;
import de.keine_arme_keine_kekse.visitor.TypeVisitor;
import de.keine_arme_keine_kekse.visitor.symboltable.SymbolTable;

public final class App {
    private static final String jasmineOut = "target/jasmineOut/";
    private final String sampleName;
    private final String samplePath;

    public static void main(String[] args) {
        String sourceDir = "./samples/";
        String[] samples = new String[] {
                "_TEST_Blatt01",
                "_TEST_Blatt02",
                "_TEST_Blatt03",
                "Factorial",
                "BubbleSort",
                "BinarySearch",
                "BinaryTree",
                "LinearSearch",
                "LinkedList",
                "QuickSort",
                "TreeVisitor"
        };

        for (String sample : samples) {
            try {
                App app = new App(sourceDir + sample);
                app.readTokens();
                // app.generateJasminCode(null, table);
            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                System.out.println("[ERROR] Type checking");
                System.exit(1);
            }
        }
    }

    public App(String sampleName) {
        this.sampleName = sampleName;
        this.samplePath = "./samples/" + this.sampleName + ".mjava";
    }

    public void readTokens() throws FileNotFoundException {
        PrintWriter writer = this.createOutWriter("tok");

        FileInputStream in = new FileInputStream(this.samplePath);
        MiniJavaParser parser = new MiniJavaParser(in);

        Token token = parser.getNextToken();
        while (token.kind != MiniJavaParserConstants.EOF) {
            token = parser.getNextToken();
            writer.println("Image: " + token.image + " ".repeat(32 - token.image.length())
                    + "Tag: " + token.kind + ", "
                    + "[" + token.beginLine + ":" + token.beginColumn
                    + "-" + token.endLine + ":" + token.endColumn + "]");
        }
        writer.close();

        System.out.println("[DONE] Scanning Minijava program");
    }

    public Program parse() throws FileNotFoundException, ParseException {
        FileInputStream in = new FileInputStream(this.samplePath);
        MiniJavaParser parser = new MiniJavaParser(in);
        Program root = parser.Program();
        System.out.println(root);

        // PrintWriter writer = this.createOutWriter("ast");
        // writer.println("ABSTRAKTER SYNTAXBAUM:\n----------------------");
        // root.accept(new TreePrintVisitor(writer)); // Pretty print of AST
        // writer.close();

        // writer = this.createOutWriter("mjava-ast");
        // root.accept(new PrettyPrintVisitor(writer)); // Pretty print of program
        // writer.close();

        System.out.println("[DONE] Parsing Minijava program");

        return root;
    }

    public SymbolTable checkTypes(Program root) throws FileNotFoundException {
        SymbolTableVisitor symbolTableVisitor = new SymbolTableVisitor();
        root.accept(symbolTableVisitor);
        SymbolTable symbolTable = symbolTableVisitor.getSymbolTable();

        System.out.println("[DONE] Building symbol table");

        PrintWriter writer = this.createOutWriter("ast");
        TypeVisitor typeVisitor = new TypeVisitor(symbolTable);
        root.accept(typeVisitor);
        writer.println("SYMBOLTABELLE:\n--------------");
        writer.println(symbolTable); // Pretty print of symbol table
        writer.println("ABSTRAKTER SYNTAXBAUM:\n----------------------");
        root.accept(new TreePrintVisitor(writer)); // Pretty print of typed AST
        writer.close();

        System.out.println("[DONE] Type checking");

        return symbolTable;
    }

    public PrintWriter createOutWriter(String extension) throws FileNotFoundException {
        String fullFilename = jasmineOut + sampleName + "." + extension;
        return new PrintWriter(new FileOutputStream(fullFilename));
    }

    public void generateJasminCode(Program root, SymbolTable symbolTable) {
        JasminVisitor jasminVisitor = new JasminVisitor(jasmineOut, symbolTable);
        root.accept(jasminVisitor);

        System.out.println("[DONE] Generating Jasmin code");
    }
}
