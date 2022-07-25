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

import java.util.List;
import java.util.Objects;

public class LPoint
{
	private final List<Double> coords;

	public LPoint(final double lat, final double lng)
	{
		coords = List.of(lat, lng);
	}

	public List<Double> getCoords()
	{
		return this.coords;
	}

	public double getLat()
	{
		return coords.get(0);
	}

	public double getLng()
	{
		return coords.get(1);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final LPoint that = (LPoint) o;
		return this.coords.equals(that.coords);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(coords);
	}
}
