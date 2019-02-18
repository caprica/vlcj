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

package uk.co.caprica.vlcj.player.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

/**
 * Minimal specification for a media player component.
 * <p>
 * Serves only to ensure a {@link MediaPlayerFactory} is available.
 */
public interface MediaPlayerComponent {

    /**
     * Get the media player factory that was used to create the media player component.
     * <p>
     * If the client application did not suppl a media player factory when creating the component, then the component is
     * the "owner" of the returned factory instance. A client application must <em>not</em> releasse the factory in this
     * case.
     * <p>
     * If a client application did supply the factory, then the client application is wholly responsible for the correct
     * life-cycle of that factory and should release it at the appropriate time.
     *
     * @return factory
     */
    MediaPlayerFactory mediaPlayerFactory();

}
