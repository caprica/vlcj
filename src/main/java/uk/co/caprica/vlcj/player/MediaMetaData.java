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

/**
 * Meta data value object.
 * <p>
 * This is a "detached" value object.
 * <p>
 * Compare this with {@link MediaMeta} and {@link DefaultMediaMeta} which are used to both read and
 * write meta data and are linked "live" with the native media handle.
 */
public final class MediaMetaData {

    private String title;

    private String artist;

    private String genre;

    private String copyright;

    private String album;

    private String trackNumber;

    private String description;

    private String rating;

    private String date;

    private String setting;

    private String url;

    private String language;

    private String nowPlaying;

    private String publisher;

    private String encodedBy;

    private String artworkUrl;

    private String trackId;

    private String trackTotal;

    private String director;

    private String season;

    private String episode;

    private String showName;

    private String actors;

    private String albumArtist;

    private String discNumber;

    private String discTotal;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(String nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEncodedBy() {
        return encodedBy;
    }

    public void setEncodedBy(String encodedBy) {
        this.encodedBy = encodedBy;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackTotal() {
        return trackTotal;
    }

    public void setTrackTotal(String trackTotal) {
        this.trackTotal = trackTotal;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(String discNumber) {
        this.discNumber = discNumber;
    }

    public String getDiscTotal() {
        return discTotal;
    }

    public void setDiscTotal(String discTotal) {
        this.discTotal = discTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(300);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("title=").append(title).append(',');
        sb.append("artist=").append(artist).append(',');
        sb.append("genre=").append(genre).append(',');
        sb.append("copyright=").append(copyright).append(',');
        sb.append("album=").append(album).append(',');
        sb.append("trackNumber=").append(trackNumber).append(',');
        sb.append("description=").append(description).append(',');
        sb.append("rating=").append(rating).append(',');
        sb.append("date=").append(date).append(',');
        sb.append("setting=").append(setting).append(',');
        sb.append("url=").append(url).append(',');
        sb.append("language=").append(language).append(',');
        sb.append("nowPlaying=").append(nowPlaying).append(',');
        sb.append("publisher=").append(publisher).append(',');
        sb.append("encodedBy=").append(encodedBy).append(',');
        sb.append("artworkUrl=").append(artworkUrl).append(',');
        sb.append("trackId=").append(trackId).append(',');
        sb.append("trackTotal=").append(trackTotal).append(',');
        sb.append("director=").append(director).append(',');
        sb.append("season=").append(season).append(',');
        sb.append("episode=").append(episode).append(',');
        sb.append("showName=").append(showName).append(',');
        sb.append("actors=").append(actors).append(',');
        sb.append("albumArtist=").append(albumArtist).append(',');
        sb.append("discNumber=").append(discNumber).append(',');
        sb.append("discTotal=").append(discTotal).append(']');
        return sb.toString();
   }
}
