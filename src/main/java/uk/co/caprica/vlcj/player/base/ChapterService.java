package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_type_t;
import uk.co.caprica.vlcj.player.ChapterDescription;

import java.util.ArrayList;
import java.util.List;

public final class ChapterService extends BaseService {

    ChapterService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the chapter count.
     *
     * @return number of chapters, or -1 if no chapters
     */
    public int getChapterCount() {
        return libvlc.libvlc_media_player_get_chapter_count(mediaPlayerInstance);
    }

    /**
     * Get the current chapter.
     *
     * @return chapter number, where zero is the first chatper, or -1 if no media
     */
    public int getChapter() {
        return libvlc.libvlc_media_player_get_chapter(mediaPlayerInstance);
    }

    /**
     * Set the chapter.
     *
     * @param chapterNumber chapter number, where zero is the first chapter
     */
    public void setChapter(int chapterNumber) {
        libvlc.libvlc_media_player_set_chapter(mediaPlayerInstance, chapterNumber);
    }

    /**
     * Jump to the next chapter.
     * <p>
     * If the play-back is already at the last chapter, this will have no effect.
     */
    public void nextChapter() {
        libvlc.libvlc_media_player_next_chapter(mediaPlayerInstance);
    }

    /**
     * Jump to the previous chapter.
     * <p>
     * If the play-back is already at the first chapter, this will have no effect.
     */
    public void previousChapter() {
        libvlc.libvlc_media_player_previous_chapter(mediaPlayerInstance);
    }

    /**
     * Get the chapter descriptions for a title.
     * <p>
     * The media must be playing before this information is available.
     *
     * @param title title number
     * @return list of descriptions (which may be empty), or <code>null</code> if there is no such title
     */
    public List<ChapterDescription> getChapterDescriptions(int title) {
        return Descriptions.chapterDescriptions(libvlc, mediaPlayerInstance, title);
    }

    /**
     * Get the chapter descriptions for the current title.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return collection of chapter descriptions, may be empty (but not <code>null</code>)
     */
    public List<ChapterDescription> getChapterDescriptions() {
        return getChapterDescriptions(mediaPlayer.titles().getTitle());
    }

    /**
     * Get all of the chapter descriptions for all available titles.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return a collection of chapter description lists, one list for each title (may be empty, but never <code>null</code>)
     */
    public List<List<ChapterDescription>> getAllChapterDescriptions() {
        int titleCount = mediaPlayer.titles().getTitleCount();
        List<List<ChapterDescription>> result = new ArrayList<List<ChapterDescription>>(Math.max(titleCount, 0));
        for(int i = 0; i < titleCount; i ++ ) {
            result.add(getChapterDescriptions(i));
        }
        return result;
    }

}
