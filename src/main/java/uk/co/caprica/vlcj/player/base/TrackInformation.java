package uk.co.caprica.vlcj.player.base;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.player.*;

import java.util.*;

final class TrackInformation {

    static List<TrackInfo> getTrackInfo(LibVlc libvlc, libvlc_media_t media, TrackType... types) {
        List<TrackInfo> result = null;
        if (media != null) {
            Set<TrackType> requestedTypes;
            if (types == null || types.length == 0) {
                requestedTypes = null;
            }
            else {
                requestedTypes = new HashSet<TrackType>(types.length);
                for (TrackType type : types) {
                    requestedTypes.add(type);
                }
            }
            result = TrackInformation.getTrackInfo(libvlc, media, requestedTypes);
        }
        return result;
    }

    private static List<TrackInfo> getTrackInfo(LibVlc libvlc, libvlc_media_t media, Set<TrackType> types) {
        PointerByReference tracksPointer = new PointerByReference();
        int numberOfTracks = libvlc.libvlc_media_tracks_get(media, tracksPointer);
        List<TrackInfo> result = new ArrayList<TrackInfo>(numberOfTracks);
        if (numberOfTracks > 0) {
            Pointer[] tracks = tracksPointer.getValue().getPointerArray(0, numberOfTracks);
            for (Pointer track : tracks) {
                TrackInfo trackInfo = getTrackInfo(libvlc, track, types);
                if (trackInfo != null) {
                    result.add(trackInfo);
                }
            }
            libvlc.libvlc_media_tracks_release(tracksPointer.getValue(), numberOfTracks);
        }
        return result;
    }

    private static TrackInfo getTrackInfo(LibVlc libvlc, Pointer pointer, Set<TrackType> types) {
        TrackInfo result = null;
        libvlc_media_track_t track = Structure.newInstance(libvlc_media_track_t.class, pointer);
        track.read();
        switch (libvlc_track_type_t.valueOf(track.i_type)) {
            case libvlc_track_unknown:
                if (types == null || types.contains(TrackType.UNKNOWN)) {
                    result = getUnknownTrackInfo(libvlc, track);
                }
                break;

            case libvlc_track_video:
                if (types == null || types.contains(TrackType.VIDEO)) {
                    result = getVideoTrackInfo(libvlc, track);
                }
                break;

            case libvlc_track_audio:
                if (types == null || types.contains(TrackType.AUDIO)) {
                    result = getAudioTrackInfo(libvlc, track);
                }
                break;

            case libvlc_track_text:
                if (types == null || types.contains(TrackType.TEXT)) {
                    result = getTextTrackInfo(libvlc, track);
                }
                break;
        }
        return result;
    }

    private static TrackInfo getUnknownTrackInfo(LibVlc libvlc, libvlc_media_track_t track) {
        return new UnknownTrackInfo(
            track.i_codec,
            track.i_original_fourcc,
            track.i_id,
            track.i_profile,
            track.i_level,
            track.i_bitrate,
            NativeString.copyNativeString(track.psz_language),
            NativeString.copyNativeString(track.psz_description),
            codecDescription(libvlc, libvlc_track_type_t.libvlc_track_unknown, track.i_codec)
        );
    }

    private static TrackInfo getVideoTrackInfo(LibVlc libvlc, libvlc_media_track_t track) {
        track.u.setType(libvlc_video_track_t.class);
        return new VideoTrackInfo(
            track.i_codec,
            track.i_original_fourcc,
            track.i_id,
            track.i_profile,
            track.i_level,
            track.i_bitrate,
            NativeString.copyNativeString(track.psz_language),
            NativeString.copyNativeString(track.psz_description),
            track.u.video.i_width,
            track.u.video.i_height,
            track.u.video.i_sar_num,
            track.u.video.i_sar_den,
            track.u.video.i_frame_rate_num,
            track.u.video.i_frame_rate_den,
            libvlc_video_orient_e.orientation(track.u.video.i_orientation),
            libvlc_video_projection_e.projection(track.u.video.i_projection),
            track.u.video.pose.f_yaw,
            track.u.video.pose.f_pitch,
            track.u.video.pose.f_roll,
            track.u.video.pose.f_field_of_view,
            track.u.video.pose.f_zoom,
            libvlc_video_multiview_e.multiview(track.u.video.i_multiview),
            codecDescription(libvlc, libvlc_track_type_t.libvlc_track_video, track.i_codec)
        );
    }

    private static TrackInfo getAudioTrackInfo(LibVlc libvlc, libvlc_media_track_t track) {
        track.u.setType(libvlc_audio_track_t.class);
        return new AudioTrackInfo(
            track.i_codec,
            track.i_original_fourcc,
            track.i_id,
            track.i_profile,
            track.i_level,
            track.i_bitrate,
            NativeString.copyNativeString(track.psz_language),
            NativeString.copyNativeString(track.psz_description),
            track.u.audio.i_channels,
            track.u.audio.i_rate,
            codecDescription(libvlc, libvlc_track_type_t.libvlc_track_audio, track.i_codec)
        );
    }

    private static TrackInfo getTextTrackInfo(LibVlc libvlc, libvlc_media_track_t track) {
        track.u.setType(libvlc_subtitle_track_t.class);
        return new TextTrackInfo(
            track.i_codec,
            track.i_original_fourcc,
            track.i_id,
            track.i_profile,
            track.i_level,
            track.i_bitrate,
            NativeString.copyNativeString(track.psz_language),
            NativeString.copyNativeString(track.psz_description),
            NativeString.copyNativeString(track.u.subtitle.psz_encoding),
            codecDescription(libvlc, libvlc_track_type_t.libvlc_track_text, track.i_codec)
        );
    }

    private static String codecDescription(LibVlc libvlc, libvlc_track_type_t type, int codec) {
        return libvlc.libvlc_media_get_codec_description(type.intValue(), codec);
    }


    private TrackInformation() {
    }

}
