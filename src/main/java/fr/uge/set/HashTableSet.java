package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;

public final class HashTableSet {

    private static final int DEFAULT_CAPACITY = 16;
    private Entry[] table;
    private int size;

    private record Entry(Object element, Entry next){}

    public HashTableSet() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(Object element) {
        Objects.requireNonNull(element);
        if(size + 1 > table.length / 2){
            resize();
        }

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

    private void resize() {
        Entry[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0;
        for (Entry entry : oldTable) {
            while (entry != null) {
                reinsert(entry.element);
                entry = entry.next;
            }
        }
    }

    private void reinsert(Object element) {
        int hash = element.hashCode();
        int index = hash & (table.length - 1);
        table[index] = new Entry(element, table[index]);
        size++;
    }
}
