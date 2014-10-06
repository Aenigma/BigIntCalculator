/*
 *  This class is meant to help you get started. Modify it as needed.
 *      However, if you will make many changes, it's safer to extend.
 */
package edu.frostburg.cosc310;

import edu.frostburg.Cosc310BigInt.skraoofi0.LinkedList; // import your classes
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TODO: Please complete me.
 *
 * @author stevenkennedy
 */
public class Cosc310LLTester {

    private static final char ADD = '+';
    private static final char SUB = '-';
    private static final char MUL = '*';

    protected int wrongCount; // keep track of mistakes
    protected int problemNumber; // print nums

    protected Cosc310BigIntCalculator studentCalc;

    public Cosc310LLTester(Cosc310BigIntCalculator calc) {
        wrongCount = 0; // starting off right!
        problemNumber = 0;

        studentCalc = calc;
    }

    protected Cosc310BigIntCalculator getCalc() {
        return studentCalc;
    }

    public void boringTest(String a, String b) {
        Cosc310BigIntCalculator studentCalc = getCalc();

        System.out.printf("Testing %s in boring-mode.%n", studentCalc);
        validateAddition(studentCalc, a, b);
        validateSubtraction(studentCalc, a, b);
        validateMultiplication(studentCalc, a, b);
        finishUp();
    }

    public void testFromFile(String filename) throws IOException {
        List<String> lines = arrayFromFile(filename);
        Cosc310BigIntCalculator studentCalc = getCalc();
        testFromLines(lines);
    }

    public void testFromLines(Iterable<String> lines) {
        for (String line : lines) {
            Scanner lineScan = new Scanner(line);
            String a = lineScan.next();
            String o = lineScan.next();
            String b = lineScan.next();
            char op = o.charAt(0);
            switch (op) {
                case ADD:
                    validateAddition(studentCalc, a, b);
                    break;
                case SUB:
                    validateSubtraction(studentCalc, a, b);
                    break;
                case MUL:
                    validateMultiplication(studentCalc, a, b);
                    break;
                default:
                    System.out.printf("Don't understand %s %s %s%n", a, o, b);
            }
        } // end for
        finishUp();
    }

    private List<String> arrayFromFile(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename));
    }

    /**
     * Compare results from our custom big-integer calculator and Java's.
     *
     * @param studC our calculator
     * @param a     first operand, in String form
     * @param b     second operand
     * @return
     */
    protected void validateAddition(Cosc310BigIntCalculator studC, String a,
            String b) {
        BigInteger binta = new BigInteger(a);
        BigInteger bintb = new BigInteger(b);
        String myResult = binta.add(bintb).toString();
        String otherResult = studC.add(a, b);

        printProblem("%s + %s = %s ", a, b, otherResult);

        showOk(myResult.compareTo(otherResult) == 0);
    }

    // compare subtraction
    protected void validateSubtraction(Cosc310BigIntCalculator calc, String a,
            String b) {
        BigInteger binta = new BigInteger(a);
        BigInteger bintb = new BigInteger(b);
        String myResult = binta.subtract(bintb).toString();
        String otherResult = calc.subtract(a, b);

        printProblem("%s - %s = %s ", a, b, otherResult);

        showOk(myResult.compareTo(otherResult) == 0);
    }

    // compare multiplication
    protected void validateMultiplication(Cosc310BigIntCalculator calc, String a,
            String b) {
        BigInteger binta = new BigInteger(a);
        BigInteger bintb = new BigInteger(b);
        String myResult = binta.multiply(bintb).toString();
        String otherResult = calc.multiply(a, b);

        printProblem("%s * %s = %s ", a, b, otherResult);
        showOk(myResult.compareTo(otherResult) == 0);
    }

    private void showOk(boolean ok) {
        if (ok) {
            System.out.println("<--[OK]-");
        } else {
            wrongCount = wrongCount + 1;
            System.out.println("\n -[WRONG]-");
        }
    }

    private void printProblem(String format, String a, String b, String result) {
        System.out.printf("%d) " + format, ++problemNumber, a, b, result);
    }

    private void finishUp() {
        double score = (double) (problemNumber - wrongCount)
                / (double) problemNumber;
        System.out.printf(
                "%nCompleted %d problems with %d wrong answers (%f).%n",
                problemNumber, wrongCount, score);
        if (wrongCount == 0) {
            System.out.println("Good job!");
        } else { // mistakes were made
            System.out.println("Do you think you can improve?");
        }
    }

    public void bigIntTest() {
        // maybe you could put in a way to test your big-int
    }

    // test the linked list
    public void listTest1() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(5);
        list.add(33);
        list.add(999);
        list.add(121212);
        System.out.println(list);
    }
}
