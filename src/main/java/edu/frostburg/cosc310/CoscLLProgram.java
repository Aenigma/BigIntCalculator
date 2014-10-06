/*
 * Runs our program. Probably don't need to modify much.
 */
package edu.frostburg.cosc310;

import edu.frostburg.Cosc310BigInt.skraoofi0.BigIntCalculator;
import edu.frostburg.Cosc310BigInt.skraoofi0.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.List;

/**
 * Runs our program.
 *
 * @author stevenkennedy
 */
public class CoscLLProgram {

    /**
     * Run this class with 2 args. args[0] is the filename to read
     *
     * @param args the command line arguments, preferably the file name
     */
    public static void main(String[] args) throws IOException {
        Cosc310LLTester tester;

        // TODO: put your calculator here
        Cosc310BigIntCalculator defaultCalculator = new BigIntCalculator();

        if (args.length == 0) {
            // run in no-file mode
//            System.out.println("No file name. Running in boring mode");
            tester = new Cosc310LLTester(defaultCalculator);

            if (CoscLLProgram.class.getResource("/big_math.txt") != null) {
                List<String> lines = new LinkedList<>();
                try (BufferedReader r = new BufferedReader(
                        new InputStreamReader(
                                CoscLLProgram.class
                                .getResource("/big_math.txt").openStream()))) {
                            while (r.ready()) {
                                lines.add(r.readLine());
                            }
                        }
                        tester.testFromLines(lines);
            } else {
                tester.boringTest("234234235678", "25");
            }

        } else if (args.length == 1) { // hopefully we have a valid file name
            String fileName = args[0];
            System.out.printf("Running with file %s%n", fileName);
            tester = new Cosc310LLTester(defaultCalculator);
            tester.testFromFile(fileName);
        }

    }
}
