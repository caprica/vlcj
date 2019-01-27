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

package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

abstract class AudioMediaPlayerComponentBase extends MediaPlayerEventAdapter {

    protected static final Spec spec() {
        return new Spec();
    }

    public static final class Spec {

        protected MediaPlayerFactory factory;

        public Spec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        private Spec() {
        }

    }

    protected AudioMediaPlayerComponentBase() {
    }

}
