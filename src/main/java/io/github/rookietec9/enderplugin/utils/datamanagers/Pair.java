package io.github.rookietec9.enderplugin.utils.datamanagers;

/**
 * @param <K> key
 * @param <V> value
 * @author Jeremi
 * @version 23.2.8
 * @since 19.7.8
 */
public class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public String toString() {
        return "[K " + key.toString() + " V " + value.toString() + "]";
    }
}
