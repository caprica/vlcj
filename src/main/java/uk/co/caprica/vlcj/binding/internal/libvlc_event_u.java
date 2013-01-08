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

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Union;

/**
 * Native event data.
 */
public class libvlc_event_u extends Union {

    public static class ByValue extends libvlc_event_u implements Union.ByValue {}

    public media_meta_changed              media_meta_changed;
    public media_subitem_added             media_subitem_added;
    public media_duration_changed          media_duration_changed;
    public media_parsed_changed            media_parsed_changed;
    public media_freed                     media_freed;
    public media_state_changed             media_state_changed;
    public media_player_buffering          media_player_buffering;
    public media_player_position_changed   media_player_position_changed;
    public media_player_time_changed       media_player_time_changed;
    public media_player_title_changed      media_player_title_changed;
    public media_player_seekable_changed   media_player_seekable_changed;
    public media_player_pausable_changed   media_player_pausable_changed;
    public media_player_vout               media_player_vout;
    public media_list_item_added           media_list_item_added;
    public media_list_will_add_item        media_list_will_add_item;
    public media_list_item_deleted         media_list_item_deleted;
    public media_list_will_delete_item     media_list_will_delete_item;
    public media_list_player_next_item_set media_list_player_next_item_set;
    public media_player_snapshot_taken     media_player_snapshot_taken;
    public media_player_length_changed     media_player_length_changed;
    public vlm_media_event                 vlm_media_event;
    public media_player_media_changed      media_player_media_changed;
}
