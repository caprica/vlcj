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

package uk.co.caprica.vlcj.player.base;

/**
 * Behaviour pertaining to userdata.
 */
public final class UserDataService extends BaseService {

    private Object userData;

    UserDataService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the user data associated with the media player.
     *
     * @return user data
     */
    public Object userData() {
        return userData;
    }

    /**
     * Set user data to associate with the media player.
     *
     * @param userData user data
     */
    public void userData(Object userData) {
        this.userData = userData;
    }

}
