package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.*;
import uk.co.caprica.vlcj.player.events.semantic.SemanticEventFactory;

import java.util.ArrayList;
import java.util.List;

public final class SubItemService extends BaseService {

    private static final SubItemsCounter subItemsCounter = new SubItemsCounter();

    private final SubItemsMrls subItemsMrls = new SubItemsMrls();

    /**
     * Flag whether or not to automatically play media sub-items if there are any.
     */
    private boolean playSubItems;

    /**
     * Index of the current sub-item, or -1 if there is no current sub-item.
     */
    private int subItemIndex;

    SubItemService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set whether or not the media player should automatically play media sub-items.
     *
     * @param playSubItems <code>true</code> to automatically play sub-items, otherwise <code>false</code>
     */
    public void setPlaySubItems(boolean playSubItems) {
        this.playSubItems = playSubItems;
    }

    public boolean getPlaySubItems() {
        return this.playSubItems;
    }

    /**
     * Get the number of sub-items (if any).
     *
     * @return sub-item count, or -1 if there is no current media
     */
    public int subItemCount() {
        Integer result = handleSubItems(subItemsCounter);
        return result != null ? result : -1;
    }

    /**
     * Get the list of sub-items (if any).
     * <p>
     * The MRL of each sub-item is returned in the list.
     *
     * @return sub-item list, or <code>null</code> if there is no current media
     */
    // FIXME needs a rename we have subItems().subItems() consider dropping it anyway and using MediaList instead
    public List<String> subItems() {
        return handleSubItems(subItemsMrls);
    }

    /**
     * Get the sub-items as a {@link MediaList}.
     *
     * @return sub-item media list, or <code>null</code> if there is no current media
     */
    public MediaList subItemsMediaList() {
        libvlc_media_t media = mediaPlayer.media().media();
        if (media != null) {
            libvlc_media_list_t mediaList = libvlc.libvlc_media_subitems(media);
            try {
                // It is acceptable for mediaList to be null here, in which case a new empty MediaList will be created
                return new MediaList(libvlc, libvlcInstance, mediaList);
            }
            finally {
                libvlc.libvlc_media_list_release(mediaList);
            }
        } else {
            return null;
        }
    }


    /**
     * Get the index of the current sub-item.
     *
     * @return sub-item index, or -1 if no sub-items or no current sub-item
     */
    public int subItemIndex() {
        return subItemIndex;
    }

    /**
     * Play a particular sub-item.
     * <p>
     * If any standard media options have been set via {@link #setStandardMediaOptions(String...)}
     * then those options will be applied to the sub-item.
     * <p>
     *
     * @param index sub-item index
     * @param mediaOptions zero or more media options for the sub-item
     * @return <code>true</code> if there is a sub-item to play, otherwise <code>false</code>
     */
    public boolean playSubItem(int index, String... mediaOptions) {
        subItemIndex = handleSubItems(new SubItemsPlayer(index, mediaOptions));
        return subItemIndex != -1;
    }

    /**
     * Play the next sub-item (if there is one).
     * <p>
     * If any standard media options have been set via {@link #setStandardMediaOptions(String...)}
     * then those options will be applied to the sub-item.
     * <p>
     * If the media player has been set to automatically repeat, then the sub-items will be
     * repeated once the last one has been played.
     *
     * @param mediaOptions zero or more media options for the sub-item
     * @return <code>true</code> if there is a sub-item, otherwise <code>false</code>
     */
    public boolean playNextSubItem(String... mediaOptions) {
        // The sub-item handler implementation deals with the range-checking of the subItemIndex and whether it shoud
        // repeat or not
        return playSubItem(subItemIndex + 1, mediaOptions);
    }

    /**
     * Get the track (i.e. "elementary streams") information for all sub-items if there are any.
     * <p>
     * See {@link #getTrackInfo(TrackType...)}.
     *
     * @param types zero or more types of track to get, or <em>all</em> tracks if omitted
     * @return collection of track information for each sub-item, or <code>null</code> if there is no current media
     */
    public List<List<TrackInfo>> getSubItemTrackInfo(TrackType... types) {
        return handleSubItems(new SubItemsTrackInfo(types));
    }

    /**
     * Get local meta data for all of the current media sub-items (if there are any).
     * <p>
     * See {@link #getMediaMeta()}, the same notes with regard to parsing hold here.
     *
     * @return collection of meta data for the media sub-items, may be empty but never <code>null</code>
     */
    public List<MediaMeta> getSubItemMediaMeta() {
        return handleSubItems(new SubItemsMeta());
    }

    /**
     * Get local meta data for all of the current media sub-items (if there are any).
     * <p>
     * See {@link #getMediaMeta()}, the same notes with regard to parsing hold here.
     * <p>
     * This function returns the meta data in a "detached" value object, i.e. there is no link to
     * the native media handle (so the meta data can <em>not</em> be updated using this function.
     *
     * @return collection of meta data for the media sub-items, may be empty but never <code>null</code>
     */
    public List<MediaMetaData> getSubItemMediaMetaData() {
        List<MediaMetaData> result;
        List<MediaMeta> metas = getSubItemMediaMeta();
        if (metas != null) {
            result = new ArrayList<MediaMetaData>(metas.size());
            for (MediaMeta meta : getSubItemMediaMeta()) {
                result.add(meta.asMediaMetaData());
            }
        }
        else {
            result = null;
        }
        return result;
    }

    void reset() {
        this.subItemIndex = -1;
    }

    /**
     * Handle sub-items.
     * <p>
     * This method contains the common code that is required when iterating over the media sub-items - the sub-items are
     * obtained from the media player as a list, the list is locked, the sub-items are processed by a {@link SubItemsHandler}
     * implementation, then the list is unlocked and released.
     *
     * @param <T> type of result
     * @param subItemsHandler handler implementation
     * @return result, or <code>null</code> if there is no current media
     */
    private <T> T handleSubItems(SubItemsHandler<T> subItemsHandler) {
        libvlc_media_t media = mediaPlayer.media().media();
        if (media != null) {
            libvlc_media_list_t subItemList = libvlc.libvlc_media_subitems(media);
            if (subItemList != null) {
                // If there is a sub-item list, iterate it under the lock...
                libvlc.libvlc_media_list_lock(subItemList);
                try {
                    return subItemsHandler.subItems(subItemList != null ? libvlc.libvlc_media_list_count(subItemList) : 0, subItemList);
                }
                finally {
                    libvlc.libvlc_media_list_unlock(subItemList);
                    libvlc.libvlc_media_list_release(subItemList);
                }
            } else {
                // If there is no sub-item list, we still invoke the handler with 0 count and no sub-items
                return subItemsHandler.subItems(0, null);
            }
        } else {
            return null;
        }
    }

    // FIXME consider making this interface and the handlers external to this file

    /**
     * Specification for a component that handles media list sub-items.
     *
     * @param <T> desired result type
     */
    private interface SubItemsHandler<T> {

        /**
         * Handle sub-items.
         *
         * @param count number of sub-items in the list, may be zero
         * @param subItems sub-item list, may be <code>null</code>
         * @return result of processing the sub-items
         */
        T subItems(int count, libvlc_media_list_t subItems);
    }

    private static class SubItemsCounter implements SubItemsHandler<Integer> {
        @Override
        public Integer subItems(int count, libvlc_media_list_t subItems) {
            return count;
        }
    }

    private class SubItemsMrls implements SubItemsHandler<List<String>> {
        @Override
        public List<String> subItems(int count, libvlc_media_list_t subItems) {
            List<String> result = new ArrayList<String>(count);
            for(libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                result.add(NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(subItem)));
            }
            return result;
        }
    }

    private class SubItemsPlayer implements SubItemsHandler<Integer> {

        private final int index;

        private final String[] options;

        private SubItemsPlayer(int index, String... options) {
            this.index = index;
            this.options = options;
        }

        @Override
        public Integer subItems(int count, libvlc_media_list_t subItems) {
            int playIndex = index;
            if (count > 0) {
                // Check if the end of the playlist has been reached
                if (playIndex >= count) {
                    if (!mediaPlayer.media().getRepeat()) {
                        // No repeat, so raise an event to signal the last item has been played
                        mediaPlayer.events().raiseEvent(SemanticEventFactory.createMediaEndOfSubItemsEvent(mediaPlayer));
                        return -1;
                    } else {
                        // Repeat, so go back to the first item in the playlist
                        playIndex = 0;
                    }
                }
                playSubItem(index, subItems, options);
                return playIndex;
            } else {
                return -1;
            }
        }
    }

    private boolean playSubItem(int index, libvlc_media_list_t subItems, String... mediaOptions) {
        libvlc_media_t subItem = libvlc.libvlc_media_list_item_at_index(subItems, index);
        if (subItem != null) {
            try {
//                MediaOptions.addMediaOptions(libvlc, subItem, standardMediaOptions); // FIXME frommMedia() service?
                MediaOptions.addMediaOptions(libvlc, subItem, mediaOptions);

                libvlc.libvlc_media_player_set_media(mediaPlayerInstance, subItem);
                libvlc.libvlc_media_player_play(mediaPlayerInstance);

                mediaPlayer.events().raiseEvent(SemanticEventFactory.createMediaSubItemPlayedEvent(mediaPlayer, subItemIndex));
                return true;
            }
            finally {
                libvlc.libvlc_media_release(subItem);
            }
        } else {
            return false;
        }
    }

    private class SubItemsTrackInfo implements SubItemsHandler<List<List<TrackInfo>>> {

        private final TrackType[] trackTypes;

        private SubItemsTrackInfo(TrackType[] trackTypes) {
            this.trackTypes = trackTypes;
        }

        @Override
        public List<List<TrackInfo>> subItems(int count, libvlc_media_list_t subItems) {
            List<List<TrackInfo>> result = new ArrayList<List<TrackInfo>>(count);
            for (libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                result.add(TrackInformation.getTrackInfo(libvlc, subItem, trackTypes));
            }
            return result;
        }
    }

    private class SubItemsMeta implements SubItemsHandler<List<MediaMeta>> {

        @Override
        public List<MediaMeta> subItems(int count, libvlc_media_list_t subItems) {
            List<MediaMeta> result = new ArrayList<MediaMeta>(count);
            for (libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                result.add(new DefaultMediaMeta(libvlc, subItem));
            }
            return result;
        }
    }

}
