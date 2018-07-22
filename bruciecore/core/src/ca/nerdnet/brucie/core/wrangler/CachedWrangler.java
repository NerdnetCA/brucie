package ca.nerdnet.brucie.core.wrangler;

/**
 * CachedWrangler is like Wrangler, but caches instances.
 *
 * The intent is that only instance will ever exist per key.
 *
 * @param <T>
 */
public interface CachedWrangler<T> extends Wrangler<T> {

    /** Register a new instance.
     *
     *
     * @param key String uniquely identifying the class
     * @param instance instance to register under key.
     */
    void register(String key, T instance);
}
