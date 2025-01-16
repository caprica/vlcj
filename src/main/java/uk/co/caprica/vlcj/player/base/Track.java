/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.media.TrackType;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_codec_description;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_track_hold;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_track_release;
import static uk.co.caprica.vlcj.binding.support.strings.NativeString.copyNativeString;

abstract public class Track {

    private final TrackType trackType;

    private final libvlc_media_track_t instance;

    /**
     * Codec (fourcc).
     */
    private final int codec;

    /**
     * Codec name.
     */
    private final String codecName;

    /**
     * Original codec (fourcc).
     */
    private final int originalCodec;

    /**
     * Original codec name.
     */
    private final String originalCodecName;

    /**
     * Track id.
     */
    private final int id;

    /**
     * Profile.
     */
    private final int profile;

    /**
     * Level.
     */
    private final int level;

    /**
     * Bit rate.
     */
    private final int bitRate;

    /**
     * Language code, e.g. "en", if available.
     */
    private final String language;

    /**
     * Track description, if available.
     */
    private final String description;

    private final String trackId;

    private final boolean stable;

    private final String name;

    private final boolean selected;

    /**
     * Codec description.
     */
    private final String codecDescription;

    /**
     * Create a new track.
     */
    protected Track(TrackType trackType, libvlc_media_track_t instance) {
        this.trackType = trackType;
        this.instance = instance;
        this.codec = instance.i_codec;
        this.codecName = codecName(codec);
        this.originalCodec = instance.i_original_fourcc;
        this.originalCodecName = codecName(originalCodec);
        this.id = instance.i_id;
        this.profile = instance.i_profile;
        this.level = instance.i_level;
        this.bitRate = instance.i_bitrate;
        this.language = copyNativeString(instance.psz_language);
        this.description = copyNativeString(instance.psz_description);
        this.trackId = copyNativeString(instance.psz_id);
        this.stable = instance.id_stable != 0;
        this.name = copyNativeString(instance.psz_name);
        this.selected = instance.selected != 0;
        this.codecDescription = codecDescription(instance.i_type, instance.i_codec);
    }

    public final TrackType trackType() {
        return trackType;
    }

    public final libvlc_media_track_t instance() {
        return instance;
    }

    /**
     * Get the codec (fourcc).
     *
     * @return codec
     */
    public final int codec() {
        return codec;
    }

    /**
     * Get the codec name.
     *
     * @return codec name
     */
    public final String codecName() {
        return codecName;
    }

    /**
     * Get the original codec (fourcc).
     *
     * @return original codec
     */
    public final int orignalCodec() {
        return codec;
    }

    /**
     * Get the original codec name.
     *
     * @return original codec name
     */
    public final String originalCodecName() {
        return originalCodecName;
    }

    /**
     * Get the track id.
     *
     * @return track id
     */
    public final int id() {
        return id;
    }

    /**
     * Get the profile.
     *
     * @return profile
     */
    public final int profile() {
        return profile;
    }

    /**
     * Get the level.
     *
     * @return level
     */
    public final int level() {
        return level;
    }

    /**
     * Get the bit-rate.
     *
     * @return bit-rate
     */
    public final int bitRate() {
        return bitRate;
    }

    /**
     * Get the language.
     *
     * @return language
     */
    public final String language() {
        return language;
    }

    /**
     * Get the description.
     *
     * @return description
     */
    public final String description() {
        return description;
    }

    public final String trackId() {
        return trackId;
    }

    public final boolean stable() {
        return stable;
    }

    public final String name() {
        return name;
    }

    public final boolean selected() {
        return selected;
    }

    /**
     * Get the codec description.
     * <p>
     * The codec description is only available with VLC 3.0.0 and later.
     *
     * @return codec description
     */
    public final String codecDescription() {
        return codecDescription;
    }

    /**
     * Release the native resources associated with the track.
     * <p>
     * A track instance does <strong>not</strong> always need to be released, it depends how it was obtained. Where a
     * track must be released this is called out in the documentation for the particular method.
     */
    public final void release() {
        libvlc_media_track_release(instance);
    }

    /**
     * Hold the native resources associated with the track.
     * <p>
     * Holding the track increases a reference counter on the underlying native resource. It could be used for example
     * when obtaining the track list - ordinarily deleting a track list will automatically release all associated native
     * track instances, but if a track instance is explicitly held the native resource will still be valid even though
     * the track list was deleted.
     * <p>
     * Any explicitly held track must at some point later be released via {@link #release()}.
     * <p>
     * Holding and releasing tracks is advanced usage and most applications will not use this.
     */
    public final void hold() {
        libvlc_media_track_hold(instance);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("trackType=").append(trackType).append(',');
        sb.append("codec=0x").append(Integer.toHexString(codec)).append(',');
        sb.append("codecName=").append(codecName).append(',');
        sb.append("originalCodec=0x").append(Integer.toHexString(originalCodec)).append(',');
        sb.append("originalCodecName=").append(originalCodecName).append(',');
        sb.append("id=").append(id).append(',');
        sb.append("profile=").append(profile).append(',');
        sb.append("level=").append(level).append(',');
        sb.append("bitRate=").append(bitRate).append(',');
        sb.append("language=").append(language).append(',');
        sb.append("description=").append(description).append(',');
        sb.append("trackId=").append(trackId).append(',');
        sb.append("stable=").append(stable).append(',');
        sb.append("name=").append(name).append(',');
        sb.append("selected=").append(selected).append(',');
        sb.append("codecDescription=").append(codecDescription).append(']');
        return sb.toString();
    }

    /**
     * Make a readable string from a codec identifier.
     *
     * @param codec codec identifier
     * @return string representation of the codec identifier
     */
    private static String codecName(int codec) {
        return codec != 0 ? new String(new byte[] {(byte)codec, (byte)(codec >>> 8), (byte)(codec >>> 16), (byte)(codec >>> 24)}).trim() : null;
    }

    private static String codecDescription(int type, int codec) {
        return libvlc_media_get_codec_description(type, codec);
    }
}
