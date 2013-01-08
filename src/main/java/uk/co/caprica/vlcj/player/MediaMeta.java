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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.image.BufferedImage;

/**
 * Specification for local media meta data.
 * <p>
 * It is possible that any particular meta data value may be <code>null</code>.
 * <p>
 * It may be necessary for the media to be parsed before any meta data is available.
 * <p>
 * When invoking setter methods to change media meta data then that change is <em>not</em> applied
 * to the media file. It is necessary to call {@link #save()} to commit the changes to the media
 * file.
 * <p>
 * Media meta data instances should be explicitly cleaned up by using {@link #release()}, otherwise
 * a native memory leak may occur.
 * <p>
 * The media may contain meta data over and above that exposed here - this interface provides
 * access to the meta data that vlc can provide.
 * <p>
 * It is <em>not</em> possible to re-read (or re-parse) the media meta data after it has already
 * been parsed - this means that when invoking a setter method on a media meta instance it is not
 * possible to undo that and restore the old value without obtaining a new media instance.
 * <p>
 * Not all media types can be parsed (e.g. network streams) - parsing such media may cause fatal
 * errors or application hangs.
 * <p>
 * The media length is provided if it is available - for some media types the length is not
 * available until some short time after the media has been played.
 */
public interface MediaMeta {

    /**
     * Parse the media to load meta data.
     * <p>
     * If the media is already parsed this will have no effect. If the media is not already parsed
     * then it will be parsed synchronously.
     */
    void parse();

    /**
     * Get the title meta data.
     *
     * @return title
     */
    String getTitle();

    /**
     * Set the title meta data.
     *
     * @param title title
     */
    void setTitle(String title);

    /**
     * Get the artist meta data.
     *
     * @return artits
     */
    String getArtist();

    /**
     * Set the artist meta data.
     *
     * @param artist artist
     */
    void setArtist(String artist);

    /**
     * Get the genre meta data.
     *
     * @return genre
     */
    String getGenre();

    /**
     * Set the genre meta data.
     *
     * @param genre genre
     */
    void setGenre(String genre);

    /**
     * Get the copyright meta data.
     *
     * @return copyright
     */
    String getCopyright();

    /**
     * Set the copyright meta data.
     *
     * @param copyright copyright
     */
    void setCopyright(String copyright);

    /**
     * Get the album meta data.
     *
     * @return album
     */
    String getAlbum();

    /**
     * Set the album meta data.
     *
     * @param album album
     */
    void setAlbum(String album);

    /**
     * Get the track number meta data.
     *
     * @return track number
     */
    String getTrackNumber();

    /**
     * Set the track number meta data.
     *
     * @param trackNumber track number
     */
    void setTrackNumber(String trackNumber);

    /**
     * Get the description meta data.
     *
     * @return description
     */
    String getDescription();

    /**
     * Set the description meta data.
     *
     * @param description description
     */
    void setDescription(String description);

    /**
     * Get the rating meta data.
     *
     * @return rating
     */
    String getRating();

    /**
     * Set the rating meta data.
     *
     * @param rating rating
     */
    void setRating(String rating);

    /**
     * Get the date meta data.
     *
     * @return date
     */
    String getDate();

    /**
     * Set the date meta data.
     *
     * @param date date
     */
    void setDate(String date);

    /**
     * Get the setting meta data.
     *
     * @return setting
     */
    String getSetting();

    /**
     * Set the setting meta data.
     *
     * @param setting setting
     */
    void setSetting(String setting);

    /**
     * Get the URL meta data.
     *
     * @return url
     */
    String getUrl();

    /**
     * Set the URL meta data.
     *
     * @param url url
     */
    void setUrl(String url);

    /**
     * Get the language meta data.
     *
     * @return language
     */
    String getLanguage();

    /**
     * Set the language meta data.
     *
     * @param language language
     */
    void setLanguage(String language);

    /**
     * Get the now playing meta data.
     *
     * @return now playing
     */
    String getNowPlaying();

    /**
     * Set the now playing meta data.
     *
     * @param nowPlaying now playing
     */
    void setNowPlaying(String nowPlaying);

    /**
     * Get the publisher meta data.
     *
     * @return publisher
     */
    String getPublisher();

    /**
     * Set the publisher meta data.
     *
     * @param publisher publisher
     */
    void setPublisher(String publisher);

    /**
     * Get the encoded by meta data.
     *
     * @return encoded by
     */
    String getEncodedBy();

    /**
     * Set the encoded by meta data.
     *
     * @param encodedBy encoded by
     */
    void setEncodedBy(String encodedBy);

    /**
     * Get the artwork URL meta data.
     * <p>
     * <strong>Invoking this method may trigger an HTTP request to download the artwork.</strong>
     *
     * @return artwork URL
     */
    String getArtworkUrl();

    /**
     * Set the artwork URL meta data.
     *
     * @param artworkUrl
     */
    void setArtworkUrl(String artworkUrl);

    /**
     * Get the track id meta data.
     *
     * @return track id
     */
    String getTrackId();

    /**
     * Set the track id meta data.
     *
     * @param trackId track id
     */
    void setTrackId(String trackId);

    /**
     * Load the artwork associated with this media.
     * <p>
     * <strong>Invoking this method may trigger an HTTP request to download the artwork.</strong>
     *
     * @return artwork image, or <code>null</code> if no artwork available
     */
    BufferedImage getArtwork();

    /**
     * Get the media length, if available.
     * <p>
     * Some media must be <em>played</em> before the length becomes available.
     *
     * @return length (milliseconds)
     */
    long getLength();

    /**
     * Write the meta data to the media.
     */
    void save();

    /**
     * Release the resources associated with this meta data instance.
     * <p>
     * If {@link #release()} is not invoked before this instance is discarded, a native memory leak
     * may occur.
     */
    void release();
}
