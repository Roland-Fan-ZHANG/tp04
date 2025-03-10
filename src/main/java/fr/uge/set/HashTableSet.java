// File: src/fr/uge/set/HashTableSet.java
package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;

public final class HashTableSet {

    private static final int DEFAULT_CAPACITY = 16;
    private final Entry[] table;
    private int size;

    private record Entry(Object element, Entry next){}

    public HashTableSet() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(Object element) {
        Objects.requireNonNull(element);

        int hash = element.hashCode();
        int index = hash & (table.length - 1);

        Entry current = table[index];
        while (current != null) {
            if (current.element.equals(element)) {
                return;
            }
            current = current.next;
        }

        table[index] = new Entry(element, table[index]);
        size++;
    }

    public int size() {
        return size;
    }

    public void forEach(Consumer<Object> consumer) {
        Objects.requireNonNull(consumer);
        for (Entry entry : table) {
            while (entry != null) {
                consumer.accept(entry.element);
                entry = entry.next;
            }
        }
    }

    public boolean contains(Object element) {
        Objects.requireNonNull(element);
        int hash = element.hashCode();
        int index = hash & (table.length - 1);

        Entry current = table[index];
        while (current != null) {
            if (current.element.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}
