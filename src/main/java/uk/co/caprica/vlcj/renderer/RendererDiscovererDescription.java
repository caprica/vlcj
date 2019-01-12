package uk.co.caprica.vlcj.renderer;

public final class RendererDiscovererDescription {

    private final String name;

    private final String longName;

    public RendererDiscovererDescription(String name, String longName) {
        this.name = name;
        this.longName = longName;
    }

    public String name() {
        return name;
    }

    public String longName() {
        return longName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("longName=").append(longName).append(']');
        return sb.toString();
    }

}
