package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;

public final class HashTableSet<T> {
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    private Entry<T>[] table = (Entry<T>[]) new Entry[DEFAULT_CAPACITY];
    private int size;

    private record Entry<T>(T element, Entry<T> next) {}

    public HashTableSet() {
        size = 0;
    }

    public void add(T element) {
        Objects.requireNonNull(element);
        if (size + 1 > table.length / 2) {
            resize();
        }
        int hash = element.hashCode();
        int index = hash & (table.length - 1);

        Entry<T> current = table[index];
        while (current != null) {
            if (current.element.equals(element)) {
                return;
            }
            current = current.next;
        }
        table[index] = new Entry<>(element, table[index]);
        size++;
    }

    public int size() {
        return size;
    }

    public void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        for (Entry<T> entry : table) {
            while (entry != null) {
                consumer.accept(entry.element);
                entry = entry.next;
            }
        }
    }

    public boolean contains(T element) {
        Objects.requireNonNull(element);
        int hash = element.hashCode();
        int index = hash & (table.length - 1);

        Entry<T> current = table[index];
        while (current != null) {
            if (current.element.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private void resize() {
        @SuppressWarnings("unchecked")
        Entry<T>[] oldTable = table;
        table = (Entry<T>[]) new Entry[oldTable.length * 2];
        size = 0;
        for (Entry<T> entry : oldTable) {
            while (entry != null) {
                reinsert(entry.element);
                entry = entry.next;
            }
        }
    }

    private void reinsert(T element) {
        int hash = element.hashCode();
        int index = hash & (table.length - 1);
        table[index] = new Entry<>(element, table[index]);
        size++;
    }
}