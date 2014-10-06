/**
 * The interface for the custom Big-Integer assignment.
 *      You shouldn't need to modify this file.
 */

package edu.frostburg.cosc310;

/**
 * This interface is for the custom big-integer application.
 *      Following the program specifications, implement this application.
 * @author stevenkennedy
 */
public interface Cosc310BigIntCalculator {
    
    /**
     * Takes two large, unsigned integers in the form of Strings. Utilizes
     *      a custom big-integer, implemented as a linked-list, to add the
     *      numbers together. Returns the result as a String.
     * @param a first large number as a string
     * @param b second large number as a string
     * @return a String representing the resulting big-integer.
     */
    public String add(String a, String b);
    
    // a - b
    public String subtract(String a, String b);
    
    // a * b
    public String multiply(String a, String b);
    
    /**
        Return your program's name here.
            e.g. "Steve's Big Int Calculator"
     * @return Your program's name, as a String
    */
    @Override
    public String toString();
}
