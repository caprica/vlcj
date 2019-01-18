package uk.co.caprica.vlcj.discoverer;

import uk.co.caprica.vlcj.enums.MediaDiscovererCategory;

public final class MediaDiscovererDescription {

    private final String name;

    private final String longName;

    private final MediaDiscovererCategory category;

    public MediaDiscovererDescription(String name, String longName, MediaDiscovererCategory category) {
        this.name = name;
        this.longName = longName;
        this.category = category;
    }

    public String name() {
        return name;
    }

    public String longName() {
        return longName;
    }

    public MediaDiscovererCategory category() {
        return category;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("longName=").append(longName).append(',');
        sb.append("category=").append(category).append(']');
        return sb.toString();
    }

}
