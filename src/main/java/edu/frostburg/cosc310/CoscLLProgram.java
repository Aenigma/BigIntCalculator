/*
 * Runs our program. Probably don't need to modify much.
 */
package edu.frostburg.cosc310;

import edu.frostburg.Cosc310BigInt.skraoofi0.BigIntCalculator;
import edu.frostburg.Cosc310BigInt.skraoofi0.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
     * @throws java.io.IOException explodes if we had problems reading the
     * resource file.
     */
    public static void main(String[] args) throws IOException {

        final Cosc310BigIntCalculator defaultCalculator = new BigIntCalculator();
        final Cosc310LLTester tester = new Cosc310LLTester(defaultCalculator);

        if (args.length == 0) {
            final URL rsrc = CoscLLProgram.class.getResource("/big_math.txt");
            if (rsrc != null) {
                List<String> lines = readLinesFromResource(rsrc);
                System.out.println("Answer file not found; using pre-packaged "
                        + "answer file...");
                tester.testFromLines(lines);
                System.out.print("We will additionally be running the boring test...");
            }
            System.out.println("Doing boring test...");
            tester.boringTest("234234235678", "25");

        } else if (args.length == 1) { // hopefully we have a valid file name
            String fileName = args[0];
            System.out.printf("Running with file %s%n", fileName);
            tester.testFromFile(fileName);
        }

    }

    /**
     * Returns a list of lines within a resource without new line characters at
     * the end, in the file. Not intended for non-line delimited files,
     * obviously.
     *
     * Perhaps in the future this could be more flexible by providing methods
     * for other kinds of files. Or creating resources.
     *
     * @param resource URL to read from
     * @return a list of lines in the file. Or an empty List if resource is null
     * @throws IOException throws an exception if there was a problem reading
     * from the resource
     */
    static List<String> readLinesFromResource(URL resource) throws IOException {
        List<String> lines = new LinkedList<>();
        if (resource == null) {
            return lines;
        }
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                resource.openStream()))) {
            while (r.ready()) {
                lines.add(r.readLine());
            }
        }
        return lines;
    }
}
