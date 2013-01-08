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

package uk.co.caprica.vlcj.player.manager;

// FIXME might have an encapsulation for Broadcast and VOD
// FIXME don't know where instanceId params come from - maybe show()'s returned JSON? - i think this
// is right, one instance for each collected client - opens up the possibility for more api e.g. get
// list of client instances??? depends how much useful info is there i suppose

/**
 * Specification for a media manager component.
 * <p>
 * The basic life-cycle is:
 *
 * <pre>
 *   // Set some options for libvlc
 *   String[] libvlcArgs = {...add options here...};
 *
 *   // Create a factory
 *   MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
 *
 *   // Create a media manager
 *   MediaManager mediaManager = mediaPlayerFactory.newMediaManager();
 *
 *   // Do some interesting things in the application
 *   ...
 *
 *   // Cleanly dispose of the media manager instance and any associated native resources
 *   mediaManager.release();
 *
 *   // Cleanly dispose of the media player factory and any associated native resources
 *   mediaPlayerFactory.release();
 * </pre>
 *
 * <strong>All of the MediaManager API is experimental and is subject to change or removal at any
 * time.</strong>
 */
public interface MediaManager {

    boolean addBroadcast(String name, String inputMrl, String outputMrl, boolean enable, boolean loop, String... options);

    boolean addVideoOnDemand(String name, String inputMrl, boolean enable, String mux, String... options);

    boolean removeMedia(String name);

    boolean enableMedia(String name, boolean enable);

    boolean setOutput(String name, String outputMrl);

    boolean setInput(String name, String inputMrl);

    boolean addInput(String name, String inputMrl);

    boolean setLoop(String name, boolean loop);

    boolean setMux(String name, String mux);

    boolean changeMedia(String name, String inputMrl, String outputMrl, boolean enable, boolean loop, String... options);

    boolean play(String name);

    boolean stop(String name);

    boolean pause(String name);

    boolean seek(String name, float percentage);

    String show(String name);

    float getPosition(String name, int instanceId);

    int getTime(String name, int instanceId);

    int getLength(String name, int instanceId);

    int getRate(String name, int instanceId);

    void release();
}
