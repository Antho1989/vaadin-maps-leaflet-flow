
package software.xdev.vaadin.maps.leaflet.flow.data;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.Objects;


public class LCenter
{

	private final LPoint coordinates;
	private final int zoom;

	public LCenter(final double lat, final double lng, final int zoom)
	{
		this.coordinates = new LPoint(lat, lng);
		this.zoom = zoom;
	}

	public int getZoom()
	{
		return this.zoom;
	}

	public LPoint getCoordinates()
	{
		return this.coordinates;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LCenter that = (LCenter) o;
		return this.zoom == that.zoom && this.coordinates.equals(that.coordinates);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(coordinates, zoom);
	}

	@Override
	public String toString()
	{
		return "LCenter{" +
				"coordinates=" + coordinates +
				", zoom=" + zoom +
				'}';
	}

	public JsonObject toJson()
	{
		final JsonObject jsonObject = Json.createObject();
		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			jsonObject.put("point", Json.parse(mapper.writeValueAsString(this)));
		}
		catch (final JsonProcessingException e)
		{
			throw new RuntimeException(e);
		}

		return jsonObject;
	}
}
