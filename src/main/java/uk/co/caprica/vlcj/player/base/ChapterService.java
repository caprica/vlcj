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
import java.util.List;

/**
 * Behaviour pertaining to chapters (e.g. for DVD or Bluray).
 */
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
        for (int i = 0; i < titleCount; i ++ ) {
            result.add(getChapterDescriptions(i));
        }
        return result;
    }

}
