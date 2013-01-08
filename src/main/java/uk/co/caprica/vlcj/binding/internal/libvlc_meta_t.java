/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

/**
 *
 */
public enum libvlc_meta_t {

    libvlc_meta_Title      ( 0),
    libvlc_meta_Artist     ( 1),
    libvlc_meta_Genre      ( 2),
    libvlc_meta_Copyright  ( 3),
    libvlc_meta_Album      ( 4),
    libvlc_meta_TrackNumber( 5),
    libvlc_meta_Description( 6),
    libvlc_meta_Rating     ( 7),
    libvlc_meta_Date       ( 8),
    libvlc_meta_Setting    ( 9),
    libvlc_meta_URL        (10),
    libvlc_meta_Language   (11),
    libvlc_meta_NowPlaying (12),
    libvlc_meta_Publisher  (13),
    libvlc_meta_EncodedBy  (14),
    libvlc_meta_ArtworkURL (15),
    libvlc_meta_TrackID    (16);

    private final int intValue;

    private libvlc_meta_t(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
