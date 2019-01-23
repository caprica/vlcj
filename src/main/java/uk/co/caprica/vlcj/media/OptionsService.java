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

package uk.co.caprica.vlcj.media;

// FIXME
// adding an option with no flags grants it TRUSTED and UNIQUE
//  (There are no other flags)
//
// if you do unique, it will REPLACE an existing option
// if you do not do unqiue, it ADDs to the existing option
//
// what that means in practical terms i don't know.
//
// generally we have not bothered with flags
//  so i think we just won't implement it

public class OptionsService extends BaseService {

    private Object userData;

    OptionsService(Media media) {
        super(media);
    }

    public boolean addOptions(String... options) {
        return MediaOptions.addMediaOptions(libvlc, mediaInstance, options);
    }

}
