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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.component.playlist;

/**
 * An entry within a play-list.
 */
public final class PlaylistEntry {

    /**
     * Media resource locator.
     */
    private final String mrl;
    
    /**
     * Zero or more media options to apply when playing this item.
     */
    private final String[] mediaOptions;
    
    /**
     * Is this play-list entry in error?
     */
    private boolean error;
    
    /**
     * User data.
     */
    private Object userData;
    
    /**
     * Create a play-list entry.
     * 
     * @param mrl media resource locator
     * @param mediaOptions zero or more media options to apply when playing this item
     */
    public PlaylistEntry(String mrl, String... mediaOptions) {
        this(mrl, null, mediaOptions);
    }
    
    /**
     * Create a play-list entry.
     * 
     * @param mrl media resource locator
     * @param userData application-specific data for this item
     * @param mediaOptions zero or more media options to apply when playing this item
     */
    public PlaylistEntry(String mrl, Object userData, String... mediaOptions) {
        this.mrl = mrl;
        this.userData = userData;
        this.mediaOptions = mediaOptions;
    }
    
    /**
     * Get the media resource locator.
     * 
     * @return MRL
     */
    public final String getMrl() {
        return mrl;
    }

    /**
     * Get the media options.
     * 
     * @return media options (may be <code>null</code>)
     */
    public final String[] getMediaOptions() {
        return mediaOptions;
    }

    /**
     * Is this play-list entry in error?
     * 
     * @return <code>true</code> if the entry is in error; <code>false</code> otherwise
     */
    public final boolean isError() {
        return error;
    }
    
    /**
     * Flag this play-list entry as in error.
     */
    public final void error() {
        this.error = true;
    }
    
    /**
     * Clear the error flag for this play-list entry.
     */
    public final void clearError() {
        this.error = false;
    }

    /**
     * Get the user data.
     * 
     * @return user data
     */
    @SuppressWarnings("unchecked")
    public final <T> T userData() {
        return (T)userData;
    }
    
    /**
     * Set the user data.
     * 
     * @param userData user data
     */
    public final void setUserData(Object userData) {
        this.userData = userData;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append(getClass().getSimpleName()).append("[");
        sb.append("mrl=").append(mrl).append(',');
        sb.append("userData=").append(userData).append(',');
        sb.append("mediaOptions=").append(mediaOptions).append(']');
        return sb.toString();
    }
}
