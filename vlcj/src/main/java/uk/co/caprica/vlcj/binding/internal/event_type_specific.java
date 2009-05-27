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
 * Copyright 2009 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Union;

/**
 *
 */
public class event_type_specific extends Union {

  /**
   * 
   */
  public media_meta_changed media_meta_changed;

  /**
   * 
   */
  public media_subitem_added media_subitem_added;

  /**
   * 
   */
  public media_duration_changed media_duration_changed;

  /**
   * 
   */
  public media_preparsed_changed media_preparsed_changed;

  /**
   * 
   */
  public media_freed media_freed;

  /**
   * 
   */
  public media_state_changed media_state_changed;

  /**
   * 
   */
  public media_player_position_changed media_player_position_changed;

  /**
   * 
   */
  public media_player_time_changed media_player_time_changed;

  /**
   * 
   */
  public media_list_item_added media_list_item_added;

  /**
   * 
   */
  public media_list_will_add_item media_list_will_add_item;

  /**
   * 
   */
  public media_list_item_deleted media_list_item_deleted;

  /**
   * 
   */
  public media_list_will_delete_item media_list_will_delete_item;

  /**
   * 
   */
  public media_list_view_item_added media_list_view_item_added;

  /**
   * 
   */
  public media_list_view_will_add_item media_list_view_will_add_item;

  /**
   * 
   */
  public media_list_view_item_deleted media_list_view_item_deleted;

  /**
   * 
   */
  public media_list_view_will_delete_item media_list_view_will_delete_item;
}
