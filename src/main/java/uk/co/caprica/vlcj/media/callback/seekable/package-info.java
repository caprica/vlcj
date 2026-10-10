/**
 * Components that implement seekable media.
 * <p>
 * Most Java input streams are <em>not</em> seekable.
 * <p>
 * It may be possible to create a Java input stream that can seek, but it would require a bespoke implementation and may
 * need to cache the entire media data in memory.
 */

@NullMarked
package uk.co.caprica.vlcj.media.callback.seekable;

import org.jspecify.annotations.NullMarked;