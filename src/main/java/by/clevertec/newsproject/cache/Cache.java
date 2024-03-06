package by.clevertec.newsproject.cache;

/**
 * Интерфейс для кэша.
 */
public interface Cache<K, V> {

    V put(K key, V value);

    V get(Object key);

    V remove(Object key);
}
