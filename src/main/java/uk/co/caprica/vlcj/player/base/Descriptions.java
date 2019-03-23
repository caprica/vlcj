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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_chapter_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_title_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_audio_get_track_description;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_chapter_descriptions_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_full_chapter_descriptions;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_full_title_descriptions;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_title_descriptions_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_track_description_list_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_spu_description;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_track_description;

final class Descriptions {

    static List<TitleDescription> titleDescriptions(libvlc_media_player_t mediaPlayerInstance) {
        List<TitleDescription> result;
        PointerByReference titles = new PointerByReference();
        int titleCount = libvlc_media_player_get_full_title_descriptions(mediaPlayerInstance, titles);
        if (titleCount != -1) {
            result = new ArrayList<TitleDescription>(titleCount);
            Pointer[] pointers = titles.getValue().getPointerArray(0, titleCount);
            for (Pointer pointer : pointers) {
                libvlc_title_description_t titleDescription = Structure.newInstance(libvlc_title_description_t.class, pointer);
                titleDescription.read();
                result.add(new TitleDescription(titleDescription.i_duration, NativeString.copyNativeString(titleDescription.psz_name), titleDescription.b_menu != 0));
            }
            libvlc_title_descriptions_release(titles.getValue(), titleCount);
        } else {
            result = new ArrayList<TitleDescription>(0);
        }
        return result;

    }

    static List<ChapterDescription> chapterDescriptions(libvlc_media_player_t mediaPlayerInstance, int title) {
        List<ChapterDescription> result;
        PointerByReference chapters = new PointerByReference();
        int chapterCount = libvlc_media_player_get_full_chapter_descriptions(mediaPlayerInstance, title, chapters);
        if (chapterCount != -1) {
            result = new ArrayList<ChapterDescription>(chapterCount);
            Pointer[] pointers = chapters.getValue().getPointerArray(0, chapterCount);
            for (Pointer pointer : pointers) {
                libvlc_chapter_description_t chapterDescription = Structure.newInstance(libvlc_chapter_description_t.class, pointer);
                chapterDescription.read();
                result.add(new ChapterDescription(chapterDescription.i_time_offset, chapterDescription.i_duration, NativeString.copyNativeString(chapterDescription.psz_name)));
            }
            libvlc_chapter_descriptions_release(chapters.getValue(), chapterCount);
        } else {
            result = new ArrayList<ChapterDescription>(0);
        }
        return result;
    }

    static List<TrackDescription> videoTrackDescriptions(libvlc_media_player_t mediaPlayerInstance) {
        libvlc_track_description_t trackDescriptions = libvlc_video_get_track_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    static List<TrackDescription> audioTrackDescriptions(libvlc_media_player_t mediaPlayerInstance) {
        libvlc_track_description_t trackDescriptions = libvlc_audio_get_track_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    static List<TrackDescription> spuTrackDescriptions(libvlc_media_player_t mediaPlayerInstance) {
        libvlc_track_description_t trackDescriptions = libvlc_video_get_spu_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    private static List<TrackDescription> getTrackDescriptions(libvlc_track_description_t trackDescriptions) {
        List<TrackDescription> trackDescriptionList = new ArrayList<TrackDescription>();
        libvlc_track_description_t trackDescription = trackDescriptions;
        while (trackDescription != null) {
            trackDescriptionList.add(new TrackDescription(trackDescription.i_id, trackDescription.psz_name));
            trackDescription = trackDescription.p_next;
        }
        if (trackDescriptions != null) {
            libvlc_track_description_list_release(trackDescriptions.getPointer());
        }
        return trackDescriptionList;
    }

    private Descriptions() {
    }

}
