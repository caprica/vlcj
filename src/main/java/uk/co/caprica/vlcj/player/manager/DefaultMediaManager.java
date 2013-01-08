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

import java.util.Arrays;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.logger.Logger;

// FIXME this implementation is a long way from complete, need to integrate the VLM event manager
// and so on

/**
 * Default implementation of a media manager component.
 * <p>
 * <code>This implementation is incomplete and untested and might be removed at
 * any time.</code>
 */
public class DefaultMediaManager implements MediaManager {

    /**
     * Native library interface.
     */
    protected final LibVlc libvlc;

    /**
     * Libvlc instance.
     */
    protected final libvlc_instance_t instance;

    /**
     * Create a media player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     */
    public DefaultMediaManager(LibVlc libvlc, libvlc_instance_t instance) {
        this.libvlc = libvlc;
        this.instance = instance;
        createInstance();
    }

    @Override
    public boolean addBroadcast(String name, String inputMrl, String outputMrl, boolean enable, boolean loop, String... options) {
        Logger.debug("addBrodcast(name={},inputMrl={},outputMrl={},enable={},loop={},options={})", name, inputMrl, outputMrl, enable, loop, Arrays.toString(options));
        return libvlc.libvlc_vlm_add_broadcast(instance, name, inputMrl, outputMrl, options != null ? options.length : 0, options, enable ? 1 : 0, loop ? 1 : 0) == 0;
    }

    @Override
    public boolean addVideoOnDemand(String name, String inputMrl, boolean enable, String mux, String... options) {
        Logger.debug("addVideoOnDemand(name={},inputMrl={},enable={},mux={},options={})", name, inputMrl, enable, mux, Arrays.toString(options));
        return libvlc.libvlc_vlm_add_vod(instance, name, inputMrl, options != null ? options.length : 0, options, enable ? 1 : 0, mux) == 0;
    }

    @Override
    public boolean removeMedia(String name) {
        Logger.debug("removeMedia(name={})", name);
        return libvlc.libvlc_vlm_del_media(instance, name) == 0;
    }

    @Override
    public boolean enableMedia(String name, boolean enable) {
        Logger.debug("enableMedia(name={},enable={})", name, enable);
        return libvlc.libvlc_vlm_set_enabled(instance, name, enable ? 1 : 0) == 0;
    }

    @Override
    public boolean setOutput(String name, String outputMrl) {
        Logger.debug("setOutput(name={},outputMrl={})", name, outputMrl);
        return libvlc.libvlc_vlm_set_output(instance, name, outputMrl) == 0;
    }

    @Override
    public boolean setInput(String name, String inputMrl) {
        Logger.debug("setInput(name={},inputMrl={})", name, inputMrl);
        return libvlc.libvlc_vlm_set_input(instance, name, inputMrl) == 0;
    }

    @Override
    public boolean addInput(String name, String inputMrl) {
        Logger.debug("addInput(name={},inputMrl={})", name, inputMrl);
        return libvlc.libvlc_vlm_add_input(instance, name, inputMrl) == 0;
    }

    @Override
    public boolean setLoop(String name, boolean loop) {
        Logger.debug("setLoop(name={},loop={})", name, loop);
        return libvlc.libvlc_vlm_set_loop(instance, name, loop ? 1 : 0) == 0;
    }

    @Override
    public boolean setMux(String name, String mux) {
        Logger.debug("setMux(name={},mux={})", name, mux);
        return libvlc.libvlc_vlm_set_mux(instance, name, mux) == 0;
    }

    @Override
    public boolean changeMedia(String name, String inputMrl, String outputMrl, boolean enable, boolean loop, String... options) {
        Logger.debug("changeMedia(name={},inputMrl={},outputMrl={},enable={},loop={},options={})", name, inputMrl, outputMrl, enable, loop, Arrays.toString(options));
        return libvlc.libvlc_vlm_change_media(instance, name, inputMrl, outputMrl, options != null ? options.length : 0, options, enable ? 1 : 0, loop ? 1 : 0) == 0;
    }

    @Override
    public boolean play(String name) {
        Logger.debug("play(name={})", name);
        return libvlc.libvlc_vlm_play_media(instance, name) == 0;
    }

    @Override
    public boolean stop(String name) {
        Logger.debug("stop(name={})", name);
        return libvlc.libvlc_vlm_stop_media(instance, name) == 0;
    }

    @Override
    public boolean pause(String name) {
        Logger.debug("pause(name={})", name);
        return libvlc.libvlc_vlm_pause_media(instance, name) == 0;
    }

    @Override
    public boolean seek(String name, float percentage) {
        Logger.debug("seek(name={},percentage={})", name, percentage);
        return libvlc.libvlc_vlm_seek_media(instance, name, percentage) == 0;
    }

    @Override
    public String show(String name) {
        Logger.debug("show(name={})", name);
        return libvlc.libvlc_vlm_show_media(instance, name);
    }

    @Override
    public float getPosition(String name, int instanceId) {
        Logger.debug("getPosition(name={},instanceId={})", name, instanceId);
        return libvlc.libvlc_vlm_get_media_instance_position(instance, name, instanceId);
    }

    @Override
    public int getTime(String name, int instanceId) {
        Logger.debug("getTime(name={},instanceId={})", name, instanceId);
        return libvlc.libvlc_vlm_get_media_instance_time(instance, name, instanceId);
    }

    @Override
    public int getLength(String name, int instanceId) {
        Logger.debug("getLength(name={},instanceId={})", name, instanceId);
        return libvlc.libvlc_vlm_get_media_instance_length(instance, name, instanceId);
    }

    @Override
    public int getRate(String name, int instanceId) {
        Logger.debug("getRate(name={},instanceId={})", name, instanceId);
        return libvlc.libvlc_vlm_get_media_instance_rate(instance, name, instanceId);
    }

    @Override
    public void release() {
        Logger.debug("release()");
        destroyInstance();
    }

    private void createInstance() {
        Logger.debug("createInstance()");
    }

    private void destroyInstance() {
        Logger.debug("destroyInstance()");
        libvlc.libvlc_vlm_release(instance);
    }
}
