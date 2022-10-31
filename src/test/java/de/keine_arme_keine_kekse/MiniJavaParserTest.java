package de.keine_arme_keine_kekse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import de.keine_arme_keine_kekse.parser.MiniJavaParser;
import de.keine_arme_keine_kekse.parser.ParseException;
import de.keine_arme_keine_kekse.syntaxtree.And;
import de.keine_arme_keine_kekse.syntaxtree.Assign;
import de.keine_arme_keine_kekse.syntaxtree.BooleanTyping;
import de.keine_arme_keine_kekse.syntaxtree.Call;
import de.keine_arme_keine_kekse.syntaxtree.Exp;
import de.keine_arme_keine_kekse.syntaxtree.ExpList;
import de.keine_arme_keine_kekse.syntaxtree.False;
import de.keine_arme_keine_kekse.syntaxtree.IdentifierExp;
import de.keine_arme_keine_kekse.syntaxtree.IdentifierTyping;
import de.keine_arme_keine_kekse.syntaxtree.IntArrayTyping;
import de.keine_arme_keine_kekse.syntaxtree.IntegerLiteral;
import de.keine_arme_keine_kekse.syntaxtree.IntegerTyping;
import de.keine_arme_keine_kekse.syntaxtree.LessThan;
import de.keine_arme_keine_kekse.syntaxtree.MethodDecl;
import de.keine_arme_keine_kekse.syntaxtree.Minus;
import de.keine_arme_keine_kekse.syntaxtree.Not;
import de.keine_arme_keine_kekse.syntaxtree.Plus;
import de.keine_arme_keine_kekse.syntaxtree.Statement;
import de.keine_arme_keine_kekse.syntaxtree.StatementList;
import de.keine_arme_keine_kekse.syntaxtree.This;
import de.keine_arme_keine_kekse.syntaxtree.Times;
import de.keine_arme_keine_kekse.syntaxtree.True;
import de.keine_arme_keine_kekse.syntaxtree.VarDecl;
import de.keine_arme_keine_kekse.syntaxtree.VarDeclList;

public class MiniJavaParserTest {

    @Test()
    public void parsesVarDeclaration() throws ParseException {
        MiniJavaParser parser = parserFor("int number;");
        VarDecl decl = parser.VarDecl();
        assertEquals("number", decl.id.getName());
        assertEquals(IntegerTyping.class, decl.type.getClass());
    }

    @Test()
    public void parsesFalse() throws ParseException {
        MiniJavaParser parser = parserFor("false");
        Exp result = parser.UnaryExpression();
        assertEquals(False.class, result.getClass());
    }

    @Test()
    public void parsesTrue() throws ParseException {
        MiniJavaParser parser = parserFor("true");
        Exp result = parser.UnaryExpression();
        assertEquals(True.class, result.getClass());
    }

    @Test()
    public void parsesThis() throws ParseException {
        MiniJavaParser parser = parserFor("this");
        Exp result = parser.UnaryExpression();
        assertEquals(This.class, result.getClass());
    }

    @Test()
    public void parsesNot() throws ParseException {
        MiniJavaParser parser = parserFor("!true");
        Exp result = parser.UnaryExpression();
        assertEquals(Not.class, result.getClass());

        Not not = (Not) result;
        assertEquals(True.class, not.exp.getClass());
    }

    @Test()
    public void parsesParenthesis() throws ParseException {
        MiniJavaParser parser = parserFor("(true)");
        Exp result = parser.UnaryExpression();
        assertEquals(True.class, result.getClass());
    }

    @Test()
    public void parsesAndExpression() throws ParseException {
        MiniJavaParser parser = parserFor("true && false");
        Exp result = parser.Expression();
        assertEquals(And.class, result.getClass());

        And and = (And) result;
        assertEquals(True.class, and.left.getClass());
        assertEquals(False.class, and.right.getClass());
    }

    @Test()
    public void parsesAnd3Expression() throws ParseException {
        MiniJavaParser parser = parserFor("true && false && true");
        Exp result = parser.Expression();
        assertEquals(And.class, result.getClass());

        And and = (And) result;
        assertEquals(True.class, and.left.getClass());
        assertEquals(And.class, and.right.getClass());

        And andRight = (And) and.right;
        assertEquals(False.class, andRight.left.getClass());
        assertEquals(True.class, andRight.right.getClass());
    }

    @Test()
    public void parsesAndComplexExpression() throws ParseException {
        MiniJavaParser parser = parserFor("(true && false) && true)");
        Exp result = parser.Expression();
        assertEquals(And.class, result.getClass());

        And and = (And) result;
        assertEquals(And.class, and.left.getClass());
        assertEquals(True.class, and.right.getClass());

        And andLeft = (And) and.left;
        assertEquals(True.class, andLeft.left.getClass());
        assertEquals(False.class, andLeft.right.getClass());
    }

    @Test()
    public void parsesIntegerLiteralAsExpression() throws ParseException {
        MiniJavaParser parser = parserFor("42");
        Exp result = parser.Expression();
        assertEquals(IntegerLiteral.class, result.getClass());

        IntegerLiteral literal = (IntegerLiteral) result;
        assertEquals(42, literal.getValue());
    }

    @Test()
    public void parsesLessThan() throws ParseException {
        MiniJavaParser parser = parserFor("0<1");
        Exp result = parser.Expression();
        assertEquals(LessThan.class, result.getClass());
    }

    @Test()
    public void parsesPlus() throws ParseException {
        MiniJavaParser parser = parserFor("0 + 1");
        Exp result = parser.Expression();
        assertEquals(Plus.class, result.getClass());
    }

    @Test()
    public void parsesMinus() throws ParseException {
        MiniJavaParser parser = parserFor("0 - 1");
        Exp result = parser.Expression();
        assertEquals(Minus.class, result.getClass());
    }

    @Test()
    public void parsesTimes() throws ParseException {
        MiniJavaParser parser = parserFor("0 * 1");
        Exp result = parser.Expression();
        assertEquals(Times.class, result.getClass());
    }

    @Test
    public void parsesCall() throws ParseException {
        MiniJavaParser parser = parserFor("test.foo()");
        Exp result = parser.Expression();
        assertEquals(Call.class, result.getClass());

        Call call = (Call) result;
        assertEquals(IdentifierExp.class, call.exp.getClass());

        IdentifierExp identifier = (IdentifierExp) call.exp;
        assertEquals("test", identifier.getName());
        assertEquals("foo", call.methodId.getName());
    }

    @Test
    public void parsesCallWithArgs() throws ParseException {
        MiniJavaParser parser = parserFor("test.foo(42, true, false)");
        Exp result = parser.Expression();
        assertEquals(Call.class, result.getClass());

        Call call = (Call) result;
        assertEquals(IdentifierExp.class, call.exp.getClass());

        IdentifierExp identifier = (IdentifierExp) call.exp;
        assertEquals("test", identifier.getName());
        assertEquals("foo", call.methodId.getName());
    }

    @Test
    public void parsesExpList() throws ParseException {
        MiniJavaParser parser = parserFor("42, true, false");
        ExpList result = parser.ExpList();

        assertEquals(3, result.size());
    }

    @Test
    public void skipsSingleLineComment() throws ParseException {
        MiniJavaParser parser = parserFor("// Hello World\ntrue");
        Exp result = parser.Expression();

        assertEquals(True.class, result.getClass());
    }

    @Test
    public void skipsBlockCommentSingleLine() throws ParseException {
        MiniJavaParser parser = parserFor("/* test */true");
        Exp result = parser.Expression();

        assertEquals(True.class, result.getClass());
    }

    @Test
    public void skipsBlockCommentMultiLine() throws ParseException {
        MiniJavaParser parser = parserFor("/* test\n Hello 2\nTest */true");
        Exp result = parser.Expression();

        assertEquals(True.class, result.getClass());
    }

    // @Test
    // public void parsesAssign() throws ParseException {
    // MiniJavaParser parser = parserFor("x = 1;");
    // Statement result = parser.AssignStatement();

    // assertEquals(Assign.class, result.getClass());
    // }

    @Test
    public void parsesVarDeclListEmpty() throws ParseException {
        MiniJavaParser parser = parserFor("");
        VarDeclList result = parser.VarDeclList();

        assertEquals(VarDeclList.class, result.getClass());

        assertEquals(0, result.size());
    }

    @Test
    public void parsesVarDeclListOne() throws ParseException {
        MiniJavaParser parser = parserFor("int x;");
        VarDeclList result = parser.VarDeclList();

        assertEquals(VarDeclList.class, result.getClass());

        assertEquals(1, result.size());

        VarDecl varDecl = result.elementAt(0);
        assertEquals(IntegerTyping.class, varDecl.type.getClass());
        assertEquals("x", varDecl.id.getName());
    }

    @Test
    public void parsesVarDeclListMany() throws ParseException {
        MiniJavaParser parser = parserFor("int x; int[] y; boolean z; ObjectXyz a;");
        VarDeclList result = parser.VarDeclList();

        assertEquals(VarDeclList.class, result.getClass());

        assertEquals(4, result.size());

        VarDecl first = result.elementAt(0);
        assertEquals("x", first.id.getName());
        assertEquals(IntegerTyping.class, first.type.getClass());

        VarDecl second = result.elementAt(1);
        assertEquals("y", second.id.getName());
        assertEquals(IntArrayTyping.class, second.type.getClass());

        VarDecl third = result.elementAt(2);
        assertEquals("z", third.id.getName());
        assertEquals(BooleanTyping.class, third.type.getClass());

        VarDecl fourth = result.elementAt(3);
        assertEquals("a", fourth.id.getName());
        assertEquals(IdentifierTyping.class, fourth.type.getClass());
    }

    @Test
    public void parsesStatementListEmpty() throws ParseException {
        MiniJavaParser parser = parserFor("");
        StatementList result = parser.StatementList();

        assertEquals(StatementList.class, result.getClass());
        assertEquals(0, result.size());
    }

    @Test
    public void parsesStatementList() throws ParseException {
        MiniJavaParser parser = parserFor("x = 1; y = 2; z = 3;");
        StatementList result = parser.StatementList();

        assertEquals(StatementList.class, result.getClass());

        assertEquals(3, result.size());

        Statement first = result.elementAt(0);
        assertEquals(Assign.class, first.getClass());
        Assign firstAssign = (Assign) first;
        assertEquals("x", firstAssign.id.getName());
        Exp firstExp = firstAssign.exp;
        assertEquals(IntegerLiteral.class, firstExp.getClass());
        IntegerLiteral firstLiteral = (IntegerLiteral) firstExp;
        assertEquals(1, firstLiteral.getValue());

        Statement second = result.elementAt(1);
        assertEquals(Assign.class, second.getClass());
        Assign secondAssign = (Assign) second;
        assertEquals("y", secondAssign.id.getName());
        Exp secondExp = secondAssign.exp;
        assertEquals(IntegerLiteral.class, secondExp.getClass());
        IntegerLiteral secondLiteral = (IntegerLiteral) secondExp;
        assertEquals(2, secondLiteral.getValue());

        Statement third = result.elementAt(2);
        assertEquals(Assign.class, third.getClass());
        Assign thirdAssign = (Assign) third;
        assertEquals("z", thirdAssign.id.getName());
        Exp thirdExp = thirdAssign.exp;
        assertEquals(IntegerLiteral.class, thirdExp.getClass());
        IntegerLiteral thirdLiteral = (IntegerLiteral) thirdExp;
        assertEquals(3, thirdLiteral.getValue());
    }

    @Test
    public void parsesMethodDeclEmpty() throws ParseException {
        MiniJavaParser parser = parserFor("public int test(){\nreturn 0;\n}");
        MethodDecl result = parser.MethodDecl();

        assertEquals(MethodDecl.class, result.getClass());
    }

    @Test
    public void parsesMethodDecl() throws ParseException {
        MiniJavaParser parser = parserFor("public int test(){\nint x;\nx = 1;\n return 0;\n}");
        MethodDecl result = parser.MethodDecl();

        assertEquals(MethodDecl.class, result.getClass());
    }

    private MiniJavaParser parserFor(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return new MiniJavaParser(in);
    }
}
