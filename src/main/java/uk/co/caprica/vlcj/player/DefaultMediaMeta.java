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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_meta_t;
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
class DefaultMediaMeta implements MediaMeta {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(DefaultMediaMeta.class);

    /**
     * Minimum version for new meta data fields.
     */
    private static final Version VERSION_220 = new Version("2.2.0");

    /**
     * Minimum version for new meta data fields.
     */
    private static final Version VERSION_300 = new Version("3.0.0");

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
    DefaultMediaMeta(LibVlc libvlc, libvlc_media_t media) {
        this.libvlc = libvlc;
        this.media = media;
        this.actualVersion = new Version(libvlc.libvlc_get_version());
        // Keep a native reference
        libvlc.libvlc_media_retain(media);
    }

    @Override
    public final void parse() {
        logger.debug("parse()");
        libvlc.libvlc_media_parse(media);
    }

    @Override
    public final String getTitle() {
        return getMeta(libvlc_meta_t.libvlc_meta_Title);
    }

    @Override
    public final void setTitle(String title) {
        setMeta(libvlc_meta_t.libvlc_meta_Title, title);
    }

    @Override
    public final String getArtist() {
        return getMeta(libvlc_meta_t.libvlc_meta_Artist);
    }

    @Override
    public final void setArtist(String artist) {
        setMeta(libvlc_meta_t.libvlc_meta_Artist, artist);
    }

    @Override
    public final String getGenre() {
        return getMeta(libvlc_meta_t.libvlc_meta_Genre);
    }

    @Override
    public final void setGenre(String genre) {
        setMeta(libvlc_meta_t.libvlc_meta_Genre, genre);
    }

    @Override
    public final String getCopyright() {
        return getMeta(libvlc_meta_t.libvlc_meta_Copyright);
    }

    @Override
    public final void setCopyright(String copyright) {
        setMeta(libvlc_meta_t.libvlc_meta_Copyright, copyright);
    }

    @Override
    public final String getAlbum() {
        return getMeta(libvlc_meta_t.libvlc_meta_Album);
    }

    @Override
    public final void setAlbum(String album) {
        setMeta(libvlc_meta_t.libvlc_meta_Album, album);
    }

    @Override
    public final String getTrackNumber() {
        return getMeta(libvlc_meta_t.libvlc_meta_TrackNumber);
    }

    @Override
    public final void setTrackNumber(String trackNumber) {
        setMeta(libvlc_meta_t.libvlc_meta_TrackNumber, trackNumber);
    }

    @Override
    public final String getDescription() {
        return getMeta(libvlc_meta_t.libvlc_meta_Description);
    }

    @Override
    public final void setDescription(String description) {
        setMeta(libvlc_meta_t.libvlc_meta_Description, description);
    }

    @Override
    public final String getRating() {
        return getMeta(libvlc_meta_t.libvlc_meta_Rating);
    }

    @Override
    public final void setRating(String rating) {
        setMeta(libvlc_meta_t.libvlc_meta_Rating, rating);
    }

    @Override
    public final String getDate() {
        return getMeta(libvlc_meta_t.libvlc_meta_Date);
    }

    @Override
    public final void setDate(String date) {
        setMeta(libvlc_meta_t.libvlc_meta_Date, date);
    }

    @Override
    public final String getSetting() {
        return getMeta(libvlc_meta_t.libvlc_meta_Setting);
    }

    @Override
    public final void setSetting(String setting) {
        setMeta(libvlc_meta_t.libvlc_meta_Setting, setting);
    }

    @Override
    public final String getUrl() {
        return getMeta(libvlc_meta_t.libvlc_meta_URL);
    }

    @Override
    public final void setUrl(String url) {
        setMeta(libvlc_meta_t.libvlc_meta_URL, url);
    }

    @Override
    public final String getLanguage() {
        return getMeta(libvlc_meta_t.libvlc_meta_Language);
    }

    @Override
    public final void setLanguage(String language) {
        setMeta(libvlc_meta_t.libvlc_meta_Language, language);
    }

    @Override
    public final String getNowPlaying() {
        return getMeta(libvlc_meta_t.libvlc_meta_NowPlaying);
    }

    @Override
    public final void setNowPlaying(String nowPlaying) {
        setMeta(libvlc_meta_t.libvlc_meta_NowPlaying, nowPlaying);
    }

    @Override
    public final String getPublisher() {
        return getMeta(libvlc_meta_t.libvlc_meta_Publisher);
    }

    @Override
    public final void setPublisher(String publisher) {
        setMeta(libvlc_meta_t.libvlc_meta_Publisher, publisher);
    }

    @Override
    public final String getEncodedBy() {
        return getMeta(libvlc_meta_t.libvlc_meta_EncodedBy);
    }

    @Override
    public final void setEncodedBy(String encodedBy) {
        setMeta(libvlc_meta_t.libvlc_meta_EncodedBy, encodedBy);
    }

    @Override
    public final String getArtworkUrl() {
        return getMeta(libvlc_meta_t.libvlc_meta_ArtworkURL);
    }

    @Override
    public final void setArtworkUrl(String artworkUrl) {
        setMeta(libvlc_meta_t.libvlc_meta_ArtworkURL, artworkUrl);
    }

    @Override
    public final String getTrackId() {
        return getMeta(libvlc_meta_t.libvlc_meta_TrackID);
    }

    @Override
    public final void setTrackId(String trackId) {
        setMeta(libvlc_meta_t.libvlc_meta_TrackID, trackId);
    }

    @Override
    public final String getTrackTotal() {
        checkVersion(VERSION_220);
        return getMeta(libvlc_meta_t.libvlc_meta_TrackTotal);
    }

    @Override
    public final void setTrackTotal(String trackTotal) {
        checkVersion(VERSION_220);
        setMeta(libvlc_meta_t.libvlc_meta_TrackTotal, trackTotal);
    }

    @Override
    public final String getDirector() {
        checkVersion(VERSION_220);
        return getMeta(libvlc_meta_t.libvlc_meta_Director);
    }

    @Override
    public final void setDirector(String director) {
        checkVersion(VERSION_220);
        setMeta(libvlc_meta_t.libvlc_meta_Director, director);
    }

    @Override
    public final String getSeason() {
        checkVersion(VERSION_220);
        return getMeta(libvlc_meta_t.libvlc_meta_Season);
    }

    @Override
    public final void setSeason(String season) {
        checkVersion(VERSION_220);
        setMeta(libvlc_meta_t.libvlc_meta_Season, season);
    }

    @Override
    public final String getEpisode() {
        checkVersion(VERSION_220);
        return getMeta(libvlc_meta_t.libvlc_meta_Episode);
    }

    @Override
    public final void setEpisode(String episode) {
        checkVersion(VERSION_220);
        setMeta(libvlc_meta_t.libvlc_meta_Episode, episode);
    }

    @Override
    public final String getShowName() {
        checkVersion(VERSION_220);
        return getMeta(libvlc_meta_t.libvlc_meta_ShowName);
    }

    @Override
    public final void setShowName(String showName) {
        checkVersion(VERSION_220);
        setMeta(libvlc_meta_t.libvlc_meta_ShowName, showName);
    }

    @Override
    public final String getActors() {
        checkVersion(VERSION_220);
        return getMeta(libvlc_meta_t.libvlc_meta_Actors);
    }

    @Override
    public final void setActors(String actors) {
        checkVersion(VERSION_220);
        setMeta(libvlc_meta_t.libvlc_meta_Actors, actors);
    }

    @Override
    public String getAlbumArtist() {
        checkVersion(VERSION_300);
        return getMeta(libvlc_meta_t.libvlc_meta_AlbumArtist);
    }

    @Override
    public void setAlbumArtist(String albumArtist) {
        checkVersion(VERSION_300);
        setMeta(libvlc_meta_t.libvlc_meta_AlbumArtist, albumArtist);
    }

    @Override
    public String getDiscNumber() {
        checkVersion(VERSION_300);
        return getMeta(libvlc_meta_t.libvlc_meta_DiscNumber);
    }

    @Override
    public void setDiscNumber(String discNumber) {
        checkVersion(VERSION_300);
        setMeta(libvlc_meta_t.libvlc_meta_DiscNumber, discNumber);
    }

    @Override
    public String getDiscTotal() {
        checkVersion(VERSION_300);
        return getMeta(libvlc_meta_t.libvlc_meta_DiscTotal);
    }

    @Override
    public void setDiscTotal(String discTotal) {
        checkVersion(VERSION_300);
        setMeta(libvlc_meta_t.libvlc_meta_DiscTotal, discTotal);
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
        if (actualVersion.atLeast(VERSION_220)) {
            result.setTrackTotal(getTrackTotal());
            result.setDirector(getDirector());
            result.setSeason(getSeason());
            result.setEpisode(getEpisode());
            result.setShowName(getShowName());
            result.setActors(getActors());
        }
        if (actualVersion.atLeast(VERSION_300)) {
            result.setAlbumArtist(getAlbumArtist());
            result.setDiscNumber(getDiscNumber());
            result.setDiscTotal(getDiscTotal());
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        logger.debug("finalize()");
        logger.debug("Meta data has been garbage collected");
        super.finalize();
        // FIXME should this invoke release()?
    }

    /**
     * Get a local meta data value for a media instance.
     *
     * @param metaType type of meta data
     * @return meta data value
     */
    private String getMeta(libvlc_meta_t metaType) {
        logger.trace("getMeta(metaType={},media={})", metaType, media);
        return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_meta(media, metaType.intValue()));
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
    private void setMeta(libvlc_meta_t metaType, String value) {
        logger.trace("setMeta(metaType={},media={},value={})", metaType, media, value);
        libvlc.libvlc_media_set_meta(media, metaType.intValue(), value);
    }

    /**
     * Assert that the current runtime version of LibVLC is at least a particular version.
     * <p>
     * This is required because some meta data fields are only available after certain versions of
     * LibVLC.
     *
     * @param checkVersion minimum version to check
     */
    private void checkVersion(Version checkVersion) {
        if (!actualVersion.atLeast(checkVersion)) {
            throw new RuntimeException("Requires at least VLC version " + checkVersion);
        }
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
        if (actualVersion.atLeast(VERSION_220)) {
            sb.append("trackTotal=").append(getTrackTotal()).append(',');
            sb.append("director=").append(getDirector()).append(',');
            sb.append("season=").append(getSeason()).append(',');
            sb.append("episode=").append(getEpisode()).append(',');
            sb.append("showName=").append(getShowName()).append(',');
            sb.append("actors=").append(getActors()).append(',');
        }
        if (actualVersion.atLeast(VERSION_300)) {
            sb.append("albumArtist=").append(getAlbumArtist()).append(',');
            sb.append("discNumber=").append(getDiscNumber()).append(',');
        }
        sb.append("length=").append(getLength()).append(']');
        return sb.toString();
    }
}
