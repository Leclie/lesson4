package ru.geekbrains.lesson4;

import java.util.Iterator;
import java.util.LinkedList;

public class MyHashMap<K, V> implements Iterable<MyHashMap.Entry<K, V>> {

    private static final int DEFAULT_CAPACITY = 16;
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    public V put(K key, V value) {
        int index = calculateBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        if (bucket == null) {
            bucket = new LinkedList<>();
            buckets[index] = bucket;
        }

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return entry.value;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
        return value;
    }

    public V get(K key) {
        int index = calculateBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }

        return null;
    }

    public V remove(K key) {
        int index = calculateBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        if (bucket != null) {
            Iterator<Entry<K, V>> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove();
                    size--;
                    return entry.value;
                }
            }
        }

        return null;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<Entry<K, V>> {
        private int currentBucket = 0;
        private Iterator<Entry<K, V>> currentIterator = null;

        public HashMapIterator() {
            findNextIterator();
        }

        private void findNextIterator() {
            while (currentBucket < buckets.length) {
                if (buckets[currentBucket] != null) {
                    currentIterator = buckets[currentBucket].iterator();
                    return;
                }
                currentBucket++;
            }
        }

        @Override
        public boolean hasNext() {
            return currentIterator != null && currentIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Entry<K, V> nextEntry = currentIterator.next();

            if (!currentIterator.hasNext()) {
                currentBucket++;
                findNextIterator();
            }

            return nextEntry;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int calculateBucketIndex(K key) {
        int hash = key.hashCode();
        return Math.abs(hash) % buckets.length;
    }


    public static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
