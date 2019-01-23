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

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_module_description_t;
import uk.co.caprica.vlcj.model.ModuleDescription;

import java.util.ArrayList;
import java.util.List;

public final class ModuleService extends BaseService {

    ModuleService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Get the available audio filters.
     *
     * @return collection of audio filter descriptions
     */
    public List<ModuleDescription> audioFilters() {
        libvlc_module_description_t moduleDescriptions = libvlc.libvlc_audio_filter_list_get(instance);
        List<ModuleDescription> result = getModuleDescriptions(moduleDescriptions);
        libvlc.libvlc_module_description_list_release(moduleDescriptions.getPointer());
        return result;
    }

    /**
     * Get the available video filters.
     *
     * @return collection of video filter descriptions
     */
    public List<ModuleDescription> videoFilters() {
        libvlc_module_description_t moduleDescriptions = libvlc.libvlc_video_filter_list_get(instance);
        List<ModuleDescription> result = getModuleDescriptions(moduleDescriptions);
        libvlc.libvlc_module_description_list_release(moduleDescriptions.getPointer());
        return result;
    }

    /**
     * Convert a collection of native module description structures.
     *
     * @param moduleDescriptions module descriptions
     * @return collection of module descriptions
     */
    private List<ModuleDescription> getModuleDescriptions(libvlc_module_description_t moduleDescriptions) {
        List<ModuleDescription> result = new ArrayList<ModuleDescription>();
        libvlc_module_description_t moduleDescription = moduleDescriptions;
        while(moduleDescription != null) {
            result.add(new ModuleDescription(moduleDescription.psz_name, moduleDescription.psz_shortname, moduleDescription.psz_longname, moduleDescription.psz_help));
            moduleDescription = moduleDescription.p_next;
        }
        return result;
    }

}
