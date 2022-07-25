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
import software.xdev.vaadin.maps.leaflet.flow.data.LCenter;
import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
import software.xdev.vaadin.maps.leaflet.flow.data.LMarker;
import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
	private final List<LComponent> components = new ArrayList<>();

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

	/**
	 * add Leaflet component(s) to the map
	 *
	 * @param lObjects
	 * @deprecated Use {@link LMap#addLComponents(LComponent...)} instead
	 */
	@Deprecated
	public void addLComponent(final LComponent... lObjects)
	{
		this.addLComponents(lObjects);
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
		this.getComponents().add(lComponent);
		this.getElement().callJsFunction(lComponent.getJsFunctionForAddingToMap(), lComponent.toJson());
	}

	/**
	 * Removes a map item
	 *
	 * @param items
	 * @deprecated Use {@link LMap#removeLComponents(LComponent...)}
	 */
	@Deprecated
	public void removeItem(final LComponent... items)
	{
		this.removeLComponents(items);
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
		final int index = this.components.indexOf(lComponent);

		if (index != -1 && this.components.remove(lComponent))
		{
			this.getElement().callJsFunction(DELETE_FUNCTION, index);
		}
	}

	/**
	 * @return
	 * @deprecated Use {@link LMap#getComponents()}
	 */
	@Deprecated
	public List<LComponent> getItems()
	{
		return this.components;
	}

	/**
	 * Returns a new component list
	 *
	 * @return
	 */
	public List<LComponent> getComponents()
	{
		return this.components;
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


	@ClientCallable
	protected void onMarkerClick(final String tag)
	{
		ComponentUtil.fireEvent(this, new MarkerClickEvent(this, true, tag));
	}

	public Registration addMarkerClickListener(final ComponentEventListener<MarkerClickEvent> listener)
	{
		return ComponentUtil.addListener(this, MarkerClickEvent.class, listener);
	}

	public static class MarkerClickEvent extends ComponentEvent<LMap>
	{
		private final String tag;

		public MarkerClickEvent(final LMap source, final boolean fromClient, final String tag)
		{
			super(source, fromClient);
			this.tag = tag;
		}

		public String getTag()
		{
			return this.tag;
		}
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

	/**
	 * Opens the popup of the given {@link LMarker}, if the map contains
	 * the marker.
	 */
	public void openPopup(LMarker marker)
	{
		final int index = this.components.indexOf(marker);

		if (index != -1)
		{
			this.getElement().callJsFunction("openPopup", index);
		}
	}

}
