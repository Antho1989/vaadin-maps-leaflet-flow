package software.xdev.vaadin.maps.leaflet.flow.data;

/*-
 * #%L
 * LeafletMap for Vaadin
 * %%
 * Copyright (C) 2019 - 2022 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.vaadin.flow.shared.Registration;
import software.xdev.vaadin.maps.leaflet.flow.event.ClickLEvent;
import software.xdev.vaadin.maps.leaflet.flow.event.LEvent;
import software.xdev.vaadin.maps.leaflet.flow.event.LEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class LEvented implements LComponent
{
	private final Map<Class<? extends LEvent>, Set<LEventListener<?>>> listeners = new HashMap<>();

	protected LEvented()
	{
	}

	private <T extends LEvent> Set<LEventListener<?>> getListenersForEventType(Class<T> eventType)
	{
		if (!listeners.containsKey(eventType))
		{
			listeners.put(eventType, new HashSet<>());
		}

		return listeners.get(eventType);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void fireEvent(LEvent event)
	{
		for (final LEventListener listener : getListenersForEventType(event.getClass()))
		{
			listener.onEvent(event);
		}
	}

	public Registration addClickListener(LEventListener<ClickLEvent> listener)
	{
		return Registration.addAndRemove(getListenersForEventType(ClickLEvent.class), listener);
	}
}
