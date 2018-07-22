package ca.nerdnet.brucie.core.wrangler;

public interface Wrangler<T> {

    void config(WranglerConfig config);

    void initialize();

    T wrangle(String key, WrangleParams params);

}
