package uk.co.caprica.vlcj.factory;

/**
 *
 */
final class NativeLibraryPath {

    /**
     * Parse out the complete file path of the native library.
     * <p>
     * This depends on the format of the toString() of the JNA implementation class.
     *
     * @param library native library instance
     * @return native library path, or simply the toString of the instance if the path could not be parsed out
     */
    static String getNativeLibraryPath(Object library) {
        String s = library.toString();
        int start = s.indexOf('<');
        if(start != -1) {
            start ++ ;
            int end = s.indexOf('@', start);
            if(end != -1) {
                s = s.substring(start, end);
                return s;
            }
        }
        return s;
    }

    private NativeLibraryPath() {
    }

}
