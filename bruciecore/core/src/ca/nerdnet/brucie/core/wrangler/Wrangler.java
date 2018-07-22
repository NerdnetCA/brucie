package ca.nerdnet.brucie.core.wrangler;

public interface Wrangler<T> {

    /** Configure the wrangler according to configure object.
     * Extend the configure object as you like.
     *
     * @param config configuration object.
     * @return none
     */
    void configure(WranglerConfig config);

    /** Initialize the wrangler. This is separated from configure
     * so that it can be called after LibGDX initialization.
     *
     */
    void initialize();


    /** Get an instance of the wrangled type, specified
     * by short identifier. (String)
     *
     * @param key String identifier uniquely identifying the class within this wrangler.
     * @param params Use as you like
     * @return none
     */
    T wrangle(String key, WrangleParams params);

}
