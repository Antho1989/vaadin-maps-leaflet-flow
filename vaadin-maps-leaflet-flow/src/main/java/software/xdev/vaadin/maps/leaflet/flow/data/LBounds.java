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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class LBounds implements Serializable
{
    private double minLng;
    private double minLat;
    private double maxLng;
    private double maxLat;

    public LBounds(){
        this.maxLng = Double.NEGATIVE_INFINITY;
        this.maxLat = Double.NEGATIVE_INFINITY;
        this.minLng = Double.POSITIVE_INFINITY;
        this.minLat = Double.POSITIVE_INFINITY;
    }

    public LBounds(final LPoint... points) {
        this();
        addPoints(points);
    }

    public double getMinLng() {
        return minLng;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLng() {
        return maxLng;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMinLng(double minLng) {
        this.minLng = minLng;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public void setMaxLng(double maxLng) {
        this.maxLng = maxLng;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public void addPoints(final LPoint... points) {
        for (LPoint p : Arrays.asList(points))
            addPoint(p.getLat(), p.getLng());
    }

    public void addPoint(final double lat, final double lng) {
        setMaxLat(Math.max(getMaxLat(), lat));
        setMaxLng(Math.max(getMaxLng(), lng));
        setMinLng(Math.min(getMinLng(), lng));
        setMinLat(Math.min(getMinLat(), lat));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LBounds that = (LBounds) o;
        return (
                that.getMaxLat() == this.getMaxLat()
                        && that.getMaxLng() == this.getMaxLng()
                        && that.getMinLat() == this.getMinLat()
                        && that.getMinLng() == this.getMinLng());

    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

    @Override
    public String toString() {
        return "Bounds {" +
                "NE latlon [" + getMaxLat() +
                "," + getMaxLng() +
                "],SW latlon [" + getMinLat() +
                "," + getMinLng() +
                "]}";
    }

    public String toJson() {
        return "[[" + maxLat + "," + minLng + "], [" + minLat + "," + maxLng + "]]";
    }
}
