/*
 * Copyright 2014 Kevin Raoofi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.frostburg.Cosc310BigInt.skraoofi0;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import edu.frostburg.Cosc310BigInt.skraoofi0.LinkedList;

/**
 * Convenience class for instantiating a List implementation.
 *
 * One could select which List implementation by passing in a {@link Class}
 * object or using its fully qualified name.
 *
 * @author Kevin Raoofi
 */
public class ListFactory {

    /**
     * Constructor for the List
     */
    private final Constructor<? extends List> defCon;

    /**
     * Defaults to {@link LinkedList}
     */
    public ListFactory() {
        this(LinkedList.class);
    }

    /**
     * Constructor which takes a class name as a String
     *
     * @param className fully qualified name of class
     * @throws ClassNotFoundException if class cannot be found
     */
    public ListFactory(String className) throws ClassNotFoundException {
        this((Class<List>) Class.forName(className));
    }

    /**
     * Constructor which takes a class object. Note that if you pass in a class
     * which does not have a no argument constructor, it will be thrown as a
     * {@link RuntimeException}. Otherwise, this method should be safe.
     *
     * @param listImplementation the class to instantiate
     */
    public ListFactory(
            Class<? extends List> listImplementation) {
        try {
            defCon = listImplementation.getConstructor(new Class[]{});
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates an instance of the List
     *
     * @param <E> the type of elements to hold
     * @return an instance of the specified List implementation
     */
    public <E> List<E> create() {
        try {
            return defCon.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a List and populates it with the contents of the given
     * Collection. Just a convenience method as it calls {@link #create()} and
     * then calls {@link Collection#addAll(java.util.Collection)} on the freshly
     * created List.
     *
     * @param <E> the type of elements to hold
     * @param col collection to add to the newly created List
     * @return an instance of the specified List implementation with the
     *         contents of col in it
     */
    public <E> List<E> create(Collection<E> col) {
        List<E> result = create();
        result.addAll(col);
        return result;
    }

}
