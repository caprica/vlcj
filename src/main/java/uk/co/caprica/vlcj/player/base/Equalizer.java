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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulation of audio equalizer settings.
 * <p>
 * Equalizer amplification values are constrained to the range -20.0 Hz to 20.0 Hz, see {@link LibVlcConst#MIN_GAIN} and
 * {@link LibVlcConst#MAX_GAIN}. Attempting to set values outside of this range will cause an
 * {@link IllegalArgumentException} to be thrown.
 * <p>
 * After creating an equalizer, it may be associated with a media player.
 */
public final class Equalizer {

    /**
     * Number of distinct frequency bands in the equalizer.
     */
    private final int bandCount;

    /**
     * Collection of components listening for equalizer state changes.
     */
    private final List<EqualizerListener> listeners = new ArrayList<EqualizerListener>();

    /**
     * Preamplification value.
     */
    private float preamp;

    /**
     * Frequency band amplification values.
     */
    private final float[] bandAmps;

    /**
     * Create an audio equalizer.
     *
     * @param bandCount number of unique frequency bands (not including pre-amp)
     */
    public Equalizer(int bandCount) {
        this.bandCount = bandCount;
        this.bandAmps = new float[bandCount];
    }

    /**
     * Add a listener to be notified of equalizer state-change events.
     *
     * @param listener listener to add
     */
    public final void addEqualizerListener(EqualizerListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener so that it is no longer notified of equalizer state-change
     * events.
     *
     * @param listener listener to remove
     */
    public final void removeEqualizerListener(EqualizerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Get the number of distinct frequency bands in the equalizer.
     *
     * @return number of frequency bands
     */
    public final int bandCount() {
        return bandCount;
    }

    /**
     * Get the current pre-amplification value.
     *
     * @return pre-amplification value (Hz)
     */
    public final float preamp() {
        return preamp;
    }

    /**
     * Set a new pre-amplification value.
     *
     * @param newPreamp pre-amplification value (Hz)
     * @throws IllegalArgumentException if the amplification value is outside of the allowed range
     */
    public final void setPreamp(float newPreamp) {
        checkAmp(newPreamp);
        this.preamp = newPreamp;
        fireEqualizerChanged();
    }

    /**
     * Get an individual amplification value.
     *
     * @param index index of the frequency band to get
     * @return amplification value
     * @throws IllegalArgumentException if the index is outside of the allowed range
     */
    public final float amp(int index) {
        if (index >= 0 && index < bandCount) {
            return bandAmps[index];
        } else {
            throw new IllegalArgumentException("Invalid band index");
        }
    }

    /**
     * Set an individual amplification value.
     *
     * @param index index of the frequency band to set
     * @param newAmp amplification value
     * @throws IllegalArgumentException if the index or amplification value is outside of the allowed range
     */
    public final void setAmp(int index, float newAmp) {
        checkAmp(newAmp);
        if (index >= 0 && index < bandCount) {
            bandAmps[index] = newAmp;
            fireEqualizerChanged();
        } else {
            throw new IllegalArgumentException("Invalid band index");
        }
    }

    /**
     * Get the current amplification values for all frequency bands.
     *
     * @return current amplification values
     */
    public final float[] amps() {
       float[] result = new float[bandCount];
       copy(bandAmps, result);
       return result;
    }

    /**
     * Set new amplification values for all frequency bands.
     *
     * @param newAmps new amplification values
     * @throws IllegalArgumentException if the amplification values are <code>null</code>, the wrong length, or outside of the allowed range
     */
    public final void setAmps(float[] newAmps) {
        if (newAmps != null && newAmps.length == bandCount) {
            for(float newAmp : newAmps) {
                checkAmp(newAmp);
            }
            copy(newAmps, bandAmps);
            fireEqualizerChanged();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Set new amplification values (including pre-amplification) from another equalizer.
     *
     * @param equalizer equalizer to obtain values from
     */
    public final void setEqualizer(Equalizer equalizer) {
        if (equalizer != null) {
            preamp = equalizer.preamp;
            copy(equalizer.bandAmps, bandAmps);
            fireEqualizerChanged();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Reset all of the equalizer amplification values (including pre-amplification) to zero.
     */
    public final void reset() {
        preamp = 0f;
        for (int i = 0; i < bandCount; i++) {
            bandAmps[i] = 0f;
        }
        fireEqualizerChanged();
    }

    /**
     * Range-check an amplification value.
     *
     * @param amp value to check
     * @throws IllegalArgumentException if the amplification value is outside of the allowed range
     */
    private void checkAmp(float amp) {
        if (amp < LibVlcConst.MIN_GAIN || amp > LibVlcConst.MAX_GAIN) {
            throw new IllegalArgumentException("Invalid amplification value: " + amp);
        }
    }

    /**
     * Copy frequency band values from one array to another.
     *
     * @param from copy from this array
     * @param to copy to this array
     */
    private void copy(float[] from, float[] to) {
        System.arraycopy(from, 0, to, 0, bandCount);
    }

    /**
     * Fire an event notification signifying that the equalizer changed.
     */
    private void fireEqualizerChanged() {
        for (EqualizerListener listener : listeners) {
            listener.equalizerChanged(this);
        }
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("preamp=").append(preamp).append(',');
        sb.append("bandAmps=").append(Arrays.toString(bandAmps)).append(']');
        return sb.toString();
    }

}
