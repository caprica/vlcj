package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.enums.MediaSlaveType;

public final class MediaSlave {

    private final String uri;

    private final MediaSlaveType type;

    private final int priority;

    public MediaSlave(String uri, MediaSlaveType type, int priority) {
        this.uri = uri;
        this.type = type;
        this.priority = priority;
    }

    public String uri() {
        return uri;
    }

    public MediaSlaveType type() {
        return type;
    }

    public int priority() {
        return priority;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("uri=").append(uri).append(',');
        sb.append("type=").append(type).append(',');
        sb.append("priority=").append(priority).append(']');
        return sb.toString();
    }

}
