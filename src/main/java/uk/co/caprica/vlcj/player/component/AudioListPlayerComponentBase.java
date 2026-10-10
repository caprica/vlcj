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

package uk.co.caprica.vlcj.player.component;

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

/**
 * Base implementation for an audio list player.
 * <p>
 * This class serves to keep the {@link AudioListPlayerComponent} concrete implementation clean and un-cluttered.
 */
abstract class AudioListPlayerComponentBase extends AudioPlayerComponent {

    /**
     * Create a media player component.
     *
     * @param mediaPlayerFactory factory used to create the component
     */
    protected AudioListPlayerComponentBase(@Nullable MediaPlayerFactory mediaPlayerFactory) {
        super(mediaPlayerFactory);
    }
}
