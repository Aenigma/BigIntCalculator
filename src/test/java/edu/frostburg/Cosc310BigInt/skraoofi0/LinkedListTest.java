/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.frostburg.Cosc310BigInt.skraoofi0;

import edu.frostburg.Cosc310BigInt.skraoofi0.LinkedList;
import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Kevin Raoofi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    LinkedListTest.GuavaTests.class,
    LinkedListTest.CustomTests.class})
public class LinkedListTest {

    public static class CustomTests {

        @Test
        public void testFoo() {

        }
    }

    public static class GuavaTests {

        public static TestSuite suite() {
            return ListTestSuiteBuilder.using(new TestStringListGenerator() {

                @Override
                protected List<String> create(String[] strings) {
                    return new LinkedList<>(Arrays.asList(strings));
                }

            })
                    .named("My List Tests")
                    .withFeatures(
                            ListFeature.GENERAL_PURPOSE,
                            CollectionFeature.ALLOWS_NULL_VALUES,
                            // I like to live dangerously with my Collections
                            // CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                            CollectionSize.ANY
                    )
                    .createTestSuite();
        }
    }

}
