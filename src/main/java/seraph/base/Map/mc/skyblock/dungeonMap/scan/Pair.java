package seraph.base.Map.mc.skyblock.dungeonMap.scan;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
    public Pair<K, V> setValue(final V val) {
        this.value = val;
        return this;
    }
    public Pair<K, V> setKey(final K key) {
        this.key = key;
        return this;
    }
}