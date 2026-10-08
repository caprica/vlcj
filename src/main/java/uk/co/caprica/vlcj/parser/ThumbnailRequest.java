package uk.co.caprica.vlcj.parser;

import uk.co.caprica.vlcj.media.PictureType;

/**
 * A thumbnail request.
 * <p>
 * If width and height are both zero (the default), the resulting thumbnail
 * will be of the same size as the media.
 * <p>
 * If either of width or height is zero, the resulting thumbnail size will be
 * derived from the media aspect ratio for the other non-zero dimension.
 * <p>
 * If both width and height are non-zero, the resulting thumbnail size will be
 * stretch to match the media aspect ratio within the given size, or cropped to
 * that size if cropped is <code>true</code>.
 * <p>
 * Hardware decoding defaults to false.
 * <p>
 * The default picture type is {@link PictureType#PNG}.
 * <p>
 * If no builder methods are invoked, a thumbnail will still be produced from
 * this request using valid defaults.
 */
public final class ThumbnailRequest {

    private PictureType type = PictureType.PNG;

    private int width;
    private int height;
    private boolean crop;

    private boolean fastSeek;

    private boolean hardwareDecoder;

    public static ThumbnailRequest thumbnailRequest() {
        return new ThumbnailRequest();
    }

    public ThumbnailRequest withType(PictureType type) {
        this.type = type;
        return this;
    }

    public ThumbnailRequest withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ThumbnailRequest withWidth(int width) {
        this.width = width;
        return this;
    }

    public ThumbnailRequest withHeight(int height) {
        this.height = height;
        return this;
    }

    public ThumbnailRequest withCrop() {
        this.crop = true;
        return this;
    }

    public ThumbnailRequest withoutCrop() {
        this.crop = false;
        return this;
    }

    public ThumbnailRequest withFastSeek() {
        this.fastSeek = true;
        return this;
    }

    public ThumbnailRequest withPreciseSeek() {
        this.fastSeek = false;
        return this;
    }

    public ThumbnailRequest withHardwareDecoder() {
        this.hardwareDecoder = true;
        return this;
    }

    public ThumbnailRequest withoutHardwareDecoder() {
        this.hardwareDecoder = false;
        return this;
    }

    public PictureType type() {
        return type;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean crop() {
        return crop;
    }

    public boolean fastSeek() {
        return fastSeek;
    }

    public boolean hardwareDecoder() {
        return hardwareDecoder;
    }

    private ThumbnailRequest() {
    }
}
