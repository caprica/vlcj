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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.enums.Meta;
import uk.co.caprica.vlcj.version.Version;

/**
 * Representation of all available media meta data.
 * <p>
 * This implementation retains a reference to the supplied native media instance, and releases
 * this native reference in {@link #release()}.
 * <p>
 * Invoking {@link #getArtworkUrl()}, {@link #getArtwork()} or {@link #toString()} may cause an
 * HTTP request to be made to download artwork.
 */
public class DefaultMediaMeta implements MediaMeta {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(DefaultMediaMeta.class);

    /**
     * Set to true when the player has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Native library interface.
     */
    private final LibVlc libvlc;

    /**
     * Associated media instance.
     * <p>
     * May be <code>null</code>.
     */
    private final libvlc_media_t media;

    /**
     * Runtime version of LibVLC.
     */
    private final Version actualVersion;

    /**
     * Artwork.
     * <p>
     * Lazily loaded.
     */
    private BufferedImage artwork;

    /**
     * Create media meta.
     *
     * @param libvlc native library instance
     * @param media media instance
     */
    // FIXME really don't want this public (due to packages, damn...)
    public DefaultMediaMeta(LibVlc libvlc, libvlc_media_t media) {
        this.libvlc = libvlc;
        this.media = media;
        this.actualVersion = new Version(libvlc.libvlc_get_version());
        // Keep a native reference
        libvlc.libvlc_media_retain(media);
    }

    @Override
    public final void parse() {
        logger.debug("parse()");
        // FIXME, options come from libvlc_parse_flag_t, timeout  0 means indefinite
        libvlc.libvlc_media_parse_with_options(media, 0, 0);
    }

    @Override
    public final String getTitle() {
        return getMeta(Meta.TITLE);
    }

    @Override
    public final void setTitle(String title) {
        setMeta(Meta.TITLE, title);
    }

    @Override
    public final String getArtist() {
        return getMeta(Meta.ARTIST);
    }

    @Override
    public final void setArtist(String artist) {
        setMeta(Meta.ARTIST, artist);
    }

    @Override
    public final String getGenre() {
        return getMeta(Meta.GENRE);
    }

    @Override
    public final void setGenre(String genre) {
        setMeta(Meta.GENRE, genre);
    }

    @Override
    public final String getCopyright() {
        return getMeta(Meta.COPYRIGHT);
    }

    @Override
    public final void setCopyright(String copyright) {
        setMeta(Meta.COPYRIGHT, copyright);
    }

    @Override
    public final String getAlbum() {
        return getMeta(Meta.ALBUM);
    }

    @Override
    public final void setAlbum(String album) {
        setMeta(Meta.ALBUM, album);
    }

    @Override
    public final String getTrackNumber() {
        return getMeta(Meta.TRACK_NUMBER);
    }

    @Override
    public final void setTrackNumber(String trackNumber) {
        setMeta(Meta.TRACK_NUMBER, trackNumber);
    }

    @Override
    public final String getDescription() {
        return getMeta(Meta.DESCRIPTION);
    }

    @Override
    public final void setDescription(String description) {
        setMeta(Meta.DESCRIPTION, description);
    }

    @Override
    public final String getRating() {
        return getMeta(Meta.RATING);
    }

    @Override
    public final void setRating(String rating) {
        setMeta(Meta.RATING, rating);
    }

    @Override
    public final String getDate() {
        return getMeta(Meta.DATE);
    }

    @Override
    public final void setDate(String date) {
        setMeta(Meta.DATE, date);
    }

    @Override
    public final String getSetting() {
        return getMeta(Meta.SETTING);
    }

    @Override
    public final void setSetting(String setting) {
        setMeta(Meta.SETTING, setting);
    }

    @Override
    public final String getUrl() {
        return getMeta(Meta.URL);
    }

    @Override
    public final void setUrl(String url) {
        setMeta(Meta.URL, url);
    }

    @Override
    public final String getLanguage() {
        return getMeta(Meta.LANGUAGE);
    }

    @Override
    public final void setLanguage(String language) {
        setMeta(Meta.LANGUAGE, language);
    }

    @Override
    public final String getNowPlaying() {
        return getMeta(Meta.NOW_PLAYING);
    }

    @Override
    public final void setNowPlaying(String nowPlaying) {
        setMeta(Meta.NOW_PLAYING, nowPlaying);
    }

    @Override
    public final String getPublisher() {
        return getMeta(Meta.PUBLISHER);
    }

    @Override
    public final void setPublisher(String publisher) {
        setMeta(Meta.PUBLISHER, publisher);
    }

    @Override
    public final String getEncodedBy() {
        return getMeta(Meta.ENCODED_BY);
    }

    @Override
    public final void setEncodedBy(String encodedBy) {
        setMeta(Meta.ENCODED_BY, encodedBy);
    }

    @Override
    public final String getArtworkUrl() {
        return getMeta(Meta.ARTWORK_URL);
    }

    @Override
    public final void setArtworkUrl(String artworkUrl) {
        setMeta(Meta.ARTWORK_URL, artworkUrl);
    }

    @Override
    public final String getTrackId() {
        return getMeta(Meta.TRACK_ID);
    }

    @Override
    public final void setTrackId(String trackId) {
        setMeta(Meta.TRACK_ID, trackId);
    }

    @Override
    public final String getTrackTotal() {
        return getMeta(Meta.TRACK_TOTAL);
    }

    @Override
    public final void setTrackTotal(String trackTotal) {
        setMeta(Meta.TRACK_TOTAL, trackTotal);
    }

    @Override
    public final String getDirector() {
        return getMeta(Meta.DIRECTOR);
    }

    @Override
    public final void setDirector(String director) {
        setMeta(Meta.DIRECTOR, director);
    }

    @Override
    public final String getSeason() {
        return getMeta(Meta.SEASON);
    }

    @Override
    public final void setSeason(String season) {
        setMeta(Meta.SEASON, season);
    }

    @Override
    public final String getEpisode() {
        return getMeta(Meta.EPISODE);
    }

    @Override
    public final void setEpisode(String episode) {
        setMeta(Meta.EPISODE, episode);
    }

    @Override
    public final String getShowName() {
        return getMeta(Meta.SHOW_NAME);
    }

    @Override
    public final void setShowName(String showName) {
        setMeta(Meta.SHOW_NAME, showName);
    }

    @Override
    public final String getActors() {
        return getMeta(Meta.ACTORS);
    }

    @Override
    public final void setActors(String actors) {
        setMeta(Meta.ACTORS, actors);
    }

    @Override
    public String getAlbumArtist() {
        return getMeta(Meta.ALBUM_ARTIST);
    }

    @Override
    public void setAlbumArtist(String albumArtist) {
        setMeta(Meta.ALBUM_ARTIST, albumArtist);
    }

    @Override
    public String getDiscNumber() {
        return getMeta(Meta.DISC_NUMBER);
    }

    @Override
    public void setDiscNumber(String discNumber) {
        setMeta(Meta.DISC_NUMBER, discNumber);
    }

    @Override
    public String getDiscTotal() {
        return getMeta(Meta.DISC_TOTAL);
    }

    @Override
    public void setDiscTotal(String discTotal) {
        setMeta(Meta.DISC_TOTAL, discTotal);
    }

    @Override
    public final BufferedImage getArtwork() {
        logger.debug("getArtwork()");
        if(artwork == null) {
            String artworkUrl = getArtworkUrl();
            if(artworkUrl != null && artworkUrl.length() > 0) {
                try {
                    URL url = new URL(artworkUrl);
                    logger.debug("url={}", url);
                    artwork = ImageIO.read(url);
                }
                catch(Exception e) {
                    throw new RuntimeException("Failed to load artwork", e);
                }
            }
        }
        return artwork;
    }

    @Override
    public final long getLength() {
        return libvlc.libvlc_media_get_duration(media);
    }

    @Override
    public final void save() {
        logger.debug("save()");
        libvlc.libvlc_media_save_meta(media);
    }

    @Override
    public final void release() {
        logger.debug("release()");
        if(released.compareAndSet(false, true)) {
            libvlc.libvlc_media_release(media);
        }
    }

    @Override
    public MediaMetaData asMediaMetaData() {
        MediaMetaData result = new MediaMetaData();
        result.setTitle(getTitle());
        result.setArtist(getArtist());
        result.setGenre(getGenre());
        result.setCopyright(getCopyright());
        result.setAlbum(getAlbum());
        result.setTrackNumber(getTrackNumber());
        result.setDescription(getDescription());
        result.setRating(getRating());
        result.setDate(getDate());
        result.setSetting(getSetting());
        result.setUrl(getUrl());
        result.setLanguage(getLanguage());
        result.setNowPlaying(getNowPlaying());
        result.setPublisher(getPublisher());
        result.setEncodedBy(getEncodedBy());
        result.setArtworkUrl(getArtworkUrl());
        result.setTrackId(getTrackId());
        result.setTrackTotal(getTrackTotal());
        result.setDirector(getDirector());
        result.setSeason(getSeason());
        result.setEpisode(getEpisode());
        result.setShowName(getShowName());
        result.setActors(getActors());
        result.setAlbumArtist(getAlbumArtist());
        result.setDiscNumber(getDiscNumber());
        result.setDiscTotal(getDiscTotal());
        result.setLength(getLength());
        return result;
    }

    /**
     * Get a local meta data value for a media instance.
     *
     * @param metaType type of meta data
     * @return meta data value
     */
    private String getMeta(Meta metaType) {
        logger.trace("getMeta(metaType={},media={})", metaType, media);
        return NativeString.copyAndFreeNativeString(libvlc, libvlc.libvlc_media_get_meta(media, metaType.intValue()));
    }

    /**
     * Set a local meta data value for a media instance.
     * <p>
     * Setting meta does not affect the underlying media file until {@link #save()} is called.
     *
     * @param metaType type of meta data
     * @param media media instance
     * @param value meta data value
     */
    private void setMeta(Meta metaType, String value) {
        logger.trace("setMeta(metaType={},media={},value={})", metaType, media, value);
        libvlc.libvlc_media_set_meta(media, metaType.intValue(), value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("title=").append(getTitle()).append(',');
        sb.append("artist=").append(getArtist()).append(',');
        sb.append("genre=").append(getGenre()).append(',');
        sb.append("copyright=").append(getCopyright()).append(',');
        sb.append("album=").append(getAlbum()).append(',');
        sb.append("trackNumber=").append(getTrackNumber()).append(',');
        sb.append("description=").append(getDescription()).append(',');
        sb.append("rating=").append(getRating()).append(',');
        sb.append("date=").append(getDate()).append(',');
        sb.append("setting=").append(getSetting()).append(',');
        sb.append("url=").append(getUrl()).append(',');
        sb.append("language=").append(getLanguage()).append(',');
        sb.append("nowPlaying=").append(getNowPlaying()).append(',');
        sb.append("publisher=").append(getPublisher()).append(',');
        sb.append("encodedBy=").append(getEncodedBy()).append(',');
        sb.append("artworkUrl=").append(getArtworkUrl()).append(',');
        sb.append("trackId=").append(getTrackId()).append(',');
        sb.append("trackTotal=").append(getTrackTotal()).append(',');
        sb.append("director=").append(getDirector()).append(',');
        sb.append("season=").append(getSeason()).append(',');
        sb.append("episode=").append(getEpisode()).append(',');
        sb.append("showName=").append(getShowName()).append(',');
        sb.append("actors=").append(getActors()).append(',');
        sb.append("albumArtist=").append(getAlbumArtist()).append(',');
        sb.append("discNumber=").append(getDiscNumber()).append(',');
        sb.append("length=").append(getLength()).append(']');
        return sb.toString();
    }
}
