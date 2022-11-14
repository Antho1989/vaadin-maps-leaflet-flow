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
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LBounds implements Serializable
{
    private double southWestLng;
    private double southWestLat;
    private double northEastLng;
    private double northEastLat;

    public LBounds(){}
    public LBounds(final LPoint northEast) {
        this(northEast, northEast);
    }

    public LBounds(final LPoint northEast, final LPoint southWest) {
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

    public void addPoints(final LPoint... points) {
        for (LPoint p : Arrays.asList(points))
            addPoint(p.getLat(), p.getLng());
    }

    public void addPoint(final double lat, final double lng) {
        setNorthEastLat(Math.max(getNorthEastLat(), lat));
        setNorthEastLng(Math.max(getNorthEastLng(), lng));
        setSouthWestLng(Math.min(getSouthWestLng(), lng));
        setSouthWestLat(Math.min(getSouthWestLat(), lat));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LBounds that = (LBounds) o;
        return (
                that.getNorthEastLat() == this.getNorthEastLat()
                        && that.getNorthEastLng() == this.getNorthEastLng()
                        && that.getSouthWestLat() == this.getSouthWestLat()
                        && that.getSouthWestLng() == this.getSouthWestLng());

    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

    @Override
    public String toString() {
        return "Bounds {" +
                "NE latlon [" + getNorthEastLat() +
                "," + getNorthEastLng() +
                "],SW latlon [" + getSouthWestLat() +
                "," + getSouthWestLng() +
                "]}";
    }

    public JsonObject toJson() {
        final JsonObject jsonObject = Json.createObject();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            jsonObject.put("bounds", Json.parse(mapper.writeValueAsString(this)));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }
}
