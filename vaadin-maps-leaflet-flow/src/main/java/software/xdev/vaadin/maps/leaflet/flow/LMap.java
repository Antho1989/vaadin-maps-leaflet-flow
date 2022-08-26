package software.xdev.vaadin.maps.leaflet.flow;

/*-
 * #%L
 * vaadin-maps-leaflet-flow
 * %%
 * Copyright (C) 2019 XDEV Software
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

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;
import software.xdev.vaadin.maps.leaflet.flow.data.*;
import software.xdev.vaadin.maps.leaflet.flow.event.ClickLEvent;
import software.xdev.vaadin.maps.leaflet.flow.event.LEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@NpmPackage(value = "leaflet", version = "^1.6.0")
@Tag("leaflet-map")
@JsModule("./leaflet/leafletCon.js")
public class LMap extends Component implements HasSize, HasStyle
{
	private static final String SET_VIEW_POINT_FUNCTION = "setViewPoint";

	private static final String DELETE_FUNCTION = "deleteItem";
	private static final String TILE_LAYER_FUNCTION = "setTileLayer";
	private static final String SET_ZOOM_FUNCTION = "setZoomLevel";

	private LCenter center;

	private final Map<Integer, LComponent> components = new HashMap<>();
	private final Map<LComponent, Integer> componentIds = new HashMap<>();

	private final AtomicInteger nextComponentId = new AtomicInteger(1);

	public LMap(final double lat, final double lon, final int zoom)
	{
		this.center = new LCenter(lat, lon, zoom);
		this.setViewPoint(this.center);
		this.setFixZIndexEnabled(true);
	}

	public void setZoom(final int zoom)
	{
		this.getElement().callJsFunction(SET_ZOOM_FUNCTION, zoom);
	}

	public void setViewPoint(final LCenter viewpoint)
	{
		this.getElement().callJsFunction(SET_VIEW_POINT_FUNCTION, viewpoint.toJson());
	}

	public void setTileLayer(final LTileLayer tl)
	{
		this.getElement().callJsFunction(TILE_LAYER_FUNCTION, tl.toJson());
	}

	/**
	 * This fixes situations where the leafletmap overlays components like Dialogs
	 *
	 * @param enabled enable or disable the fix
	 */
	protected void setFixZIndexEnabled(final boolean enabled)
	{
		this.getStyle().set("z-index", enabled ? "1" : null);
	}

	public boolean containsLComponent(final LComponent lComponent)
	{
		return componentIds.containsKey(lComponent);
	}

	private int addComponent(final LComponent lComponent)
	{
		if (componentIds.containsKey(lComponent))
			throw new IllegalArgumentException("Component already added to this map");

		final int id = nextComponentId.getAndIncrement();
		components.put(id, lComponent);
		componentIds.put(lComponent, id);
		return id;
	}

	private int removeComponent(final LComponent lComponent)
	{
		if (!containsLComponent(lComponent))
			throw new IllegalArgumentException("Component not in this map");

		final int id = componentIds.remove(lComponent);
		components.remove(id);
		return id;
	}

	private int getComponentId(final LComponent lComponent)
	{
		if (!containsLComponent(lComponent))
			throw new IllegalArgumentException("Component not in this map");

		return componentIds.get(lComponent);
	}

	private LComponent getComponentById(final int id)
	{
		return components.get(id);
	}

	/**
	 * add Leaflet component(s) to the map
	 *
	 * @param lComponents
	 */
	public void addLComponents(final LComponent... lComponents)
	{
		this.addLComponents(Arrays.asList(lComponents));
	}

	/**
	 * add Leaflet components to the map
	 *
	 * @param lComponents
	 */
	public void addLComponents(final Collection<LComponent> lComponents)
	{
		for (final LComponent lComponent : lComponents)
		{
			this.addLComponent(lComponent);
		}
	}

	protected void addLComponent(final LComponent lComponent)
	{
		this.getElement().callJsFunction(
				lComponent.getJsFunctionForAddingToMap(),
				addComponent(lComponent),
				lComponent.toJson()
		);
	}

	/**
	 * remove Leaflet component(s) to the map
	 *
	 * @param lComponents
	 */
	public void removeLComponents(final LComponent... lComponents)
	{
		this.removeLComponents(Arrays.asList(lComponents));
	}

	/**
	 * remove Leaflet components to the map
	 *
	 * @param lComponents
	 */
	public void removeLComponents(final Collection<LComponent> lComponents)
	{
		for (final LComponent lComponent : lComponents)
		{
			this.removeLComponent(lComponent);
		}
	}

	protected void removeLComponent(final LComponent lComponent)
	{
		this.getElement().callJsFunction(DELETE_FUNCTION, removeComponent(lComponent));
	}

	public LCenter getCenter()
	{
		return this.center;
	}

	/**
	 * Starting Point of the map with latitude, longitude and zoom level
	 *
	 * @param start
	 */
	public void setCenter(final LCenter start)
	{
		this.center = start;
		this.setViewPoint(start);
	}
	
	public void invalidateSize() {
		this.getElement().executeJs("this.map.invalidateSize()");
	}
	
	@ClientCallable
	protected void onMapCenterChanged(final double lat, final double lng, final int zoom) {
		this.center = new LCenter(lat, lng, zoom);
	}


	@ClientCallable
	protected void onMapClick(final double lat, final double lng)
	{
		ComponentUtil.fireEvent(this, new MapClickEvent(this, true, lat, lng));
	}

	public Registration addMapClickListener(final ComponentEventListener<MapClickEvent> listener)
	{
		return ComponentUtil.addListener(this, MapClickEvent.class, listener);
	}

	public static class MapClickEvent extends ComponentEvent<LMap>
	{
		private final double lat;
		private final double lng;

		public MapClickEvent(final LMap source, final boolean fromClient, final double lat, final double lng)
		{
			super(source, fromClient);
			this.lat = lat;
			this.lng = lng;
		}

		public double getLat()
		{
			return lat;
		}

		public double getLng()
		{
			return lng;
		}
	}

	@ClientCallable
	protected void onComponentEvent(final int id, final String type)
	{
		final LComponent c = getComponentById(id);

		if (c == null)
		{
			throw new IllegalArgumentException("Component not in map");
		}
		if (!(c instanceof LEvented))
		{
			throw new IllegalArgumentException("Component not Evented");
		}

		final LEvented source = (LEvented) c;
		final LEvent event;

		if ("click".equals(type))
		{
			event = new ClickLEvent(source, true);
		}
		else
		{
			throw new IllegalArgumentException("Unknown event type '" + type + "'");
		}

		source.fireEvent(event);
	}

	/**
	 * Opens the popup of the given {@link LMarker}, if the map contains
	 * the marker.
	 */
	public void openPopup(LMarker marker)
	{
		this.getElement().callJsFunction("openPopup", getComponentId(marker));
	}

}
