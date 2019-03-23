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

/**
 * Behaviour pertaining to media options.
 * <p>
 * Generally there is no need to specify option flags, by default the option will be {@link OptionFlag#TRUSTED} and
 * {@link OptionFlag#UNIQUE} although it is possible to use different flag values if required.
 */
public final class OptionsApi extends BaseApi {

    OptionsApi(Media media) {
        super(media);
    }

    /**
     * Add options to the media.
     *
     * @param options options to add
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean add(String... options) {
        return MediaOptions.addMediaOptions(mediaInstance, options);
    }

    /**
     * Add options, with flags, to the media.
     *
     * @param options options to add
     * @param flags option flags
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean add(String[] options, OptionFlag... flags) {
        return MediaOptions.addMediaOptions(mediaInstance, options, flags);
    }

    /**
     * Add an option to the media.
     *
     * @param option option to add
     * @param flags option flags
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean add(String option, OptionFlag... flags) {
        return MediaOptions.addMediaOption(mediaInstance, option, flags);
    }

}
