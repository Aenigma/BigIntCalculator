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

import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * This class imitates the standard LinkedList. However, it differs from the the
 * original in several ways.</p>
 *
 * <ol>
 * <li>It does not implement any extra interfaces outside of the List interface.
 * The most evident sadness this provides is the lack of Deque methods.</li>
 *
 * <li>It is not fail-fast in concurrency situations. So, if you used this
 * implementation without external synchronization, I couldn't tell you what
 * horrible things may happen.</li>
 * </ol>
 *
 * @author Kevin Raoofi
 * @param <E> the type for the elements being stored
 */
public class LinkedList<E> extends AbstractSequentialList<E> {

    /**
     * Size of the List
     */
    private int size;

    /**
     * I was doing stuff to make it fail-fast for concurrency and then I just
     * stopped because I got bored and it's not remotely part of the
     * requirements.
     */
    private int mods;

    /**
     * <p>
     * So, rather than use a null, I'm using a node which has both the first and
     * last element of the LinkedList. This is, thus, technically a circularly
     * linked list but this reference is used as the terminator.</p>
     *
     * <p>
     * It made coding it a lot easier because I pretty much never had to deal
     * with null values that could either be not instantiated yet or the
     * terminator.</p>
     */
    private Item<E> reference;

    /**
     * Default, no-arg, constructor.
     */
    public LinkedList() {
        this.reference = new Item<>();
        reference.next = reference;
        reference.prev = reference;
    }

    /**
     * Constructor which creates a deep copy of {@code c} with a shallow copy of
     * its elements.
     *
     * @param c the collection to add into this
     */
    public LinkedList(Collection<E> c) {
        this();
        addAll(c);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new MainIter(index);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Does nothing if bounds are OK for accessing. Otherwise, throws
     * {@link IndexOutOfBoundsException}.
     *
     * @param index the index intended to be accessed
     * @throws IndexOutOfBoundsException if (index >= size || index < 0)
     */
    private void verifyAccessible(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Cannot access index: " + index
                    + " with size: " + size);
        }
    }

    /**
     * Does nothing if bounds are OK for insertion. Otherwise, throws
     * {@link IndexOutOfBoundsException}.
     *
     * @param index the index intended to be inserted at
     * @throws IndexOutOfBoundsException if (index > size || index < 0)
     */
    private void verifyInsertable(int index) throws IndexOutOfBoundsException {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Cannot insert index: " + index
                    + " with size: " + size);
        }
    }

    /**
     * This is the actual meat of the implementation as it defines the mechanism
     * for add, delete, traversal, etc. with {@link AbstractList} providing all
     * the boilerplate.
     *
     * @author Kevin Raoofi
     */
    private final class MainIter implements ListIterator<E> {

        /**
         * The current item in question
         */
        Item<E> current;
        /**
         * The last item returned
         */
        Item<E> lastReturned;

        /**
         * The current index
         */
        int currentIndex;

        /**
         * The number of modifications expected as consistent with the iterator
         */
        int iterModCount;

        /**
         * Whether {@link #set(java.lang.Object)} and {@link #remove()} can be
         * called without throwing an {@link IllegalStateException}.
         */
        boolean canSet;

        /**
         * Constructor which traverses the list to the given index
         *
         * @param index the index of the value to be called by the subsequent
         * call to {@link #next()}.
         */
        public MainIter(int index) {
            verifyInsertable(index);
            this.iterModCount = mods;
            this.canSet = false;
            this.currentIndex = -1;
            this.current = reference;

            this.lastReturned = reference;

            /*
             * Decides if we're traversing backwards or forwards depending on
             * which is shorter
             */
            if (index < size / 2) {
                this.currentIndex = -1;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                    this.currentIndex++;
                }
            } else {
                this.currentIndex = size;
                for (int i = size; i >= index; i--) {
                    current = current.prev;
                    this.currentIndex--;
                }
            }
        }

        /**
         * One could run this to throw an exception; but i don't use it since I
         * don't think all my increments to iterModCount and modCount are in
         * place
         */
        private void checkConcurrentModifications() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return this.current.next != reference;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            canSet = true;

            this.current = this.current.next;
            currentIndex++;

            this.lastReturned = this.current;

            return this.current.obj;
        }

        @Override
        public boolean hasPrevious() {
            return this.current != reference;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            canSet = true;

            E elem = this.current.obj;
            this.lastReturned = this.current;

            this.current = this.current.prev;

            this.currentIndex--;

            return elem;
        }

        @Override
        public int nextIndex() {
            return this.currentIndex + 1 < size ? this.currentIndex + 1 : size;
        }

        @Override
        public int previousIndex() {
            return this.currentIndex > -1 ? this.currentIndex : -1;
        }

        @Override
        public void remove() {
            if (lastReturned == reference) {
                throw new IllegalStateException("Cannot remove root reference!");
            }

            if (!canSet) {
                throw new IllegalStateException(
                        "Must call next or previous first");
            }

            canSet = false;

            this.current = this.lastReturned.prev;

            lastReturned.remove();
            lastReturned = reference;

            this.iterModCount++;
            if (this.currentIndex > -1) {
                this.currentIndex--;
            }

            size--;
        }

        @Override
        public void set(E e) {
            if (!canSet) {
                throw new IllegalStateException(
                        "Must call next or previous first");
            }

            this.lastReturned.obj = e;
        }

        @Override
        public void add(E e) {
            canSet = false;

            this.current = this.current.addAfter(e);

            currentIndex++;
            size++;
        }
    }

    /**
     * This class is responsible for holding the links for each node and
     * contains each of the element in the class
     *
     * @param <E>
     */
    private final static class Item<E> {

        /**
         * The object that this item is holding
         */
        public E obj;
        /**
         * The previous item
         */
        public Item<E> prev;
        /**
         * The next item
         */
        public Item<E> next;

        /**
         * Creates a new {@link Item<E>} and inserts it directly after this one.
         * The next item of this Item is subsequently given to the new Item as
         * its next Item.
         *
         * @param e element which the new item will have
         * @return the new item
         */
        public Item<E> addAfter(E e) {
            Item<E> nitem = new Item<>();
            nitem.prev = this;
            nitem.next = next;
            nitem.obj = e;

            next.prev = nitem;
            next = nitem;

            return nitem;
        }

        /**
         * Creates a new {@link Item<E>} and inserts it directly before this
         * one. The previous item of this Item is subsequently given to the new
         * Item as its previous item.
         *
         * @param e element which the new item will have
         * @return the new item
         */
        public Item<E> addBefore(E e) {
            Item<E> nitem = new Item<>();
            nitem.next = this;
            nitem.prev = prev;
            nitem.obj = e;

            prev.next = nitem;
            prev = nitem;

            return nitem;
        }

        /**
         * Removes references to this item by removing the previous item's and
         * the next item's reference to this item with each other. Also sets
         * this item's references to them as well as its reference to the
         * element it contains to null.
         *
         * This item should no longer be used afterwards and any other external
         * references to this should no longer exist.
         *
         * @return the element this item held
         */
        public E remove() {
            E element = obj;

            this.prev.next = next;
            this.next.prev = prev;
            next = prev = null;
            obj = null;

            return element;
        }
    }
}
