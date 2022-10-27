package de.keine_arme_keine_kekse;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.keine_arme_keine_kekse.parser.MiniJavaParser;
import de.keine_arme_keine_kekse.parser.ParseException;
import de.keine_arme_keine_kekse.syntaxtree.Statement;

class AppTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "_TEST_Blatt01",
            "_TEST_Blatt02",
            "Factorial",
            "BubbleSort",
            "BinarySearch",
            "BinaryTree",
            "LinearSearch",
            "LinkedList",
            "QuickSort",
            "TreeVisitor"
    })
    public void recognizesTokens(String sample) throws FileNotFoundException, ParseException {
        App app = new App(sample);
        app.parse();
    }
}
