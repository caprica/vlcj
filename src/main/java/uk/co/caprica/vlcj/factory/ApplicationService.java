package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.log.NativeLog;

public final class ApplicationService extends BaseService {

    ApplicationService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Get the libvlc version.
     *
     * @return native library version
     */
    public String version() {
        return libvlc.libvlc_get_version();
    }

    /**
     * Get the compiler used to build libvlc.
     *
     * @return compiler
     */
    public String compiler() {
        return libvlc.libvlc_get_compiler();
    }

    /**
     * Get the source code change-set id used to build libvlc.
     *
     * @return change-set
     */
    public String changeset() {
        return libvlc.libvlc_get_changeset();
    }

    /**
     * Set the application name.
     *
     * @param userAgent application name
     */
    public void setUserAgent(String userAgent) {
        setUserAgent(userAgent, null);
    }

    /**
     * Set the application name.
     *
     * @param userAgent application name
     * @param httpUserAgent application name for HTTP
     */
    public void setUserAgent(String userAgent, String httpUserAgent) {
        libvlc.libvlc_set_user_agent(instance, userAgent, httpUserAgent);
    }

    /**
     * Set the application identification information.
     *
     * @param id application id, e.g. com.somecompany.myapp
     * @param version application version
     * @param icon path to application icon
     */
    public void setApplicationId(String id, String version, String icon) {
        libvlc.libvlc_set_app_id(instance, id, version, icon);
    }

    /**
     * Create a new native log component.
     *
     * @return native log component, or <code>null</code> if the native log is not available
     */
    public NativeLog newLog() {
        return new NativeLog(libvlc, instance);
    }

    /**
     * Get the time as defined by LibVLC.
     * <p>
     * The time is not meaningful in the sense of what time is it, rather it is a monotonic clock
     * with an arbitrary starting value.
     *
     * @return current clock time value, in microseconds
     */
    public long clock() {
        return libvlc.libvlc_clock();
    }

}
