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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import elemental.json.Json;
import elemental.json.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

//@JsonSerialize(using = LZoom.LZoomSerializer.class)
public class LZoom
//public class LZoom implements Serializable
{
	private double southWestLng;
	private double southWestLat;
	private double northEastLng;
	private double northEastLat;
	public LZoom(final LPoint northEast, final LPoint southWest)
	{
		this.northEastLng = northEast.getLng();
		this.northEastLat = northEast.getLat();
		this.southWestLng = southWest.getLng();
		this.southWestLat = southWest.getLat();
	}

	public double getSouthWestLng() {
		return southWestLng;
	}

	public double getSouthWestLat() {
		return southWestLat;
	}

	public double getNorthEastLng() {
		return northEastLng;
	}

	public double getNorthEastLat() {
		return northEastLat;
	}

	public void setSouthWestLng(double southWestLng) {
		this.southWestLng = southWestLng;
	}

	public void setSouthWestLat(double southWestLat) {
		this.southWestLat = southWestLat;
	}

	public void setNorthEastLng(double northEastLng) {
		this.northEastLng = northEastLng;
	}

	public void setNorthEastLat(double northEastLat) {
		this.northEastLat = northEastLat;
	}

//	public void extend (LPoint... points) {
//		for (LPoint p : points)
//			extend (p.getLat(), p.getLng());
//	}

	public void extend (double lat, double lng) {
		setNorthEastLat(Math.max(getNorthEastLat(), lat));
		setNorthEastLng(Math.max(getNorthEastLng(), lng));
		setSouthWestLat(Math.max(getSouthWestLat(), lat));
		setSouthWestLng(Math.max(getSouthWestLng(), lng));
	}

	public LCenter getCenter() {
		//TODO
		int zoom = 17;
		return new LCenter(
				(getNorthEastLat() + getSouthWestLat()) / 2,
				(getNorthEastLng() + getSouthWestLng()) / 2,
				zoom);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final LZoom that = (LZoom) o;
		return (
				that.getNorthEastLat() == this.getNorthEastLat()
				&& that.getNorthEastLng() == this.getNorthEastLng()
				&& that.getSouthWestLat() == this.getSouthWestLat()
				&& that.getSouthWestLng() == this.getSouthWestLng());

	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this);
	}

	@Override
	public String toString()
	{
//		if (getNorthWest() == null
//				||  getNorthEast() == null
//				||  getSouthEast() == null
//				||  getSouthWest() == null)
//			return "LZoom is not set";

		return "Bounds {" +
					"NE lat = " + getNorthEastLat() +
					"NE lng = " + getNorthEastLng() +
					"SW lat = " + getSouthWestLat() +
					"SW lng = " + getSouthWestLng() +
					",center-lat=" + getCenter().getCoordinates().getLat() +
					",center-lon=" + getCenter().getCoordinates().getLng() +
					",zoom=" + getCenter().getZoom() +
					'}';
	}

	public JsonObject toJson()
	{
		final JsonObject jsonObject = Json.createObject();
		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			System.out.println(Json.parse(mapper.writeValueAsString(this)));
			jsonObject.put("lzoom", Json.parse(mapper.writeValueAsString(this)));
	}
		catch(final JsonProcessingException e)
	{
		throw new RuntimeException(e);
	}

		return jsonObject;
	}

	//TODO addpoint

//	public static final class LZoomSerializer extends StdSerializer<LZoom>
//	{
//		public LZoomSerializer()
//		{
//			super(LZoom.class);
//		}
//
//		@Override
//		public void serialize(LZoom value, JsonGenerator gen, SerializerProvider provider) throws IOException
//		{
//			System.out.println("################### called !!!");
//			gen.writeStartArray();
//			gen.writeStartArray();
//			gen.writeNumber(value.getNorthWest().getLat());
//			gen.writeNumber(value.getNorthWest().getLng());
//			gen.writeEndArray();
//			gen.writeStartArray();
//			gen.writeNumber(value.getNorthEast().getLat());
//			gen.writeNumber(value.getNorthEast().getLng());
//			gen.writeEndArray();
//			gen.writeStartArray();
//			gen.writeNumber(value.getSouthWest().getLat());
//			gen.writeNumber(value.getSouthWest().getLng());
//			gen.writeEndArray();
//			gen.writeStartArray();
//			gen.writeNumber(value.getSouthEast().getLat());
//			gen.writeNumber(value.getSouthEast().getLng());
//			gen.writeEndArray();
//			gen.writeEndArray();
//		}
//	}
}
