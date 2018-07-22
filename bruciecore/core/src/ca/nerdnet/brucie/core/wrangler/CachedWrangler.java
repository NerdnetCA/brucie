package ca.nerdnet.brucie.core.wrangler;

public interface CachedWrangler<T> extends Wrangler<T> {

    void register(String key, T instance);
}
