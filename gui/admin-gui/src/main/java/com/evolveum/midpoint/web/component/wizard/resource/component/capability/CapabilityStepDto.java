/*
 * Copyright (c) 2010-2014 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.web.component.wizard.resource.component.capability;

import com.evolveum.midpoint.web.component.wizard.resource.dto.CapabilityDto;
import com.evolveum.midpoint.xml.ns._public.resource.capabilities_3.CapabilityType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author shood
 * */
public class CapabilityStepDto implements Serializable{

    public static final String F_CAPABILITIES = "capabilities";
	public static final String F_SELECTED_CAPABILITY = "selectedCapability";

	private CapabilityDto selectedDto;

    private List<CapabilityDto<CapabilityType>> capabilities = new ArrayList<>();

	public CapabilityStepDto(List<CapabilityDto<CapabilityType>> capabilities) {
		this.capabilities = capabilities;
	}

	public List<CapabilityDto<CapabilityType>> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityDto<CapabilityType>> capabilities) {
        this.capabilities = capabilities;
    }

	public CapabilityDto getSelectedDto() {
		return selectedDto;
	}

	public CapabilityType getSelectedCapability() {
		return selectedDto != null ? selectedDto.getCapability() : null;
	}

	public void setSelected(CapabilityDto selected) {
		this.selectedDto = selected;
	}

	public int getSelectedIndex() {
		return selectedDto != null ? capabilities.indexOf(selectedDto) : -1;
	}

	public void setSelectedIndex(int index) {
		if (index >= 0 && index < capabilities.size()) {
			setSelected(capabilities.get(index));
		}
	}

}
