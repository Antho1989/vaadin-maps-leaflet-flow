import {html,PolymerElement} from "@polymer/polymer/polymer-element.js";
import "leaflet/dist/leaflet.js";

export class LeafletMap extends PolymerElement {
    static get template() {
        return html `
      <link
        rel="stylesheet"
        href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
      />

      <style>
        #map-container {
          width: 100%;
          height: 100%;
        }
        #divMap {
          width: 100%;
          height: 100%;
        }
        .div-icon {
        	background: #fff;
        	border: 1px solid #666;
        	height: auto;
        	width: auto;
        	padding-left: 5px;
        	padding-right: 5px;
        	white-space: nowrap;
        }
      </style>
      <div id="divMap"></div>
    `;
    }

    static get properties() {
        return {
            map: {
                type: Object,
                notify: true
            },
            items: {
                type: Array,
                notify: true
            },
            tile: {
                type: Object,
                notify: true
            }
        };
    }

    ready() {
        this._initMap();
    }

    setViewPoint(obj) {
        this.map.setView(obj.point.coordinates, obj.point.zoom);
    }
    
    setTileLayer(layer)
    {     
        this.tile = L.tileLayer(layer.tile.link,{attribution: layer.tile.attribution, maxZoom: layer.tile.zoom, id: layer.tile.id}).addTo(this.map);
        this.tile.bringToFront();
    }

    setZoomLevel(zoom)
    {
        this.map.setZoom(zoom);
    }

    _initMap() {
        super.ready();
        this.map = new L.map(this.$.divMap);

        this.tile = L.tileLayer(
            "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
                maxZoom: 18
            }
        ).addTo(this.map);

	    this.items = {};

        let vaadinServer = this.$server;

        this.map.on('click', function (e) {
            vaadinServer.onMapClick(e.latlng.lat, e.latlng.lng);
        });
    }

	addMarker(id, obj) {
		let leafIcon;

		if (obj.properties.icon.type === 'DivIcon') {
			leafIcon = new L.divIcon(obj.properties.icon);
		} else {
			leafIcon = new L.Icon(obj.properties.icon);
		}

		let item = L.marker(obj.geometry.coordinates, {icon: leafIcon})
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, {closeButton: false});
		}

		if (obj.tag !== "empty") {
			var vaadinServer = this.$server;

			item.on('click', function (e) {
				vaadinServer.onMarkerClick(obj.tag);
			});
		}

		this.items[id] = item;
	}

	deleteItem(id) {
		this.items[id].remove();
		delete this.items[id];
	}

	addPolygon(id, obj) {
		let item = L.polygon(obj.geometry.coordinates, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, {closeButton: false});
		}

		this.items[id] = item;
	}

	addPolyline(id, obj) {
		let item = L.polyline(obj.geometry.coordinates, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, {closeButton: false});
		}

		this.items[id] = item;
	}

	addCircle(id, obj) {
		let item = L.circle(obj.geometry.coords, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup);
		}

		this.items[id] = item;
	}

	openPopup(id) {
		this.items[id].openPopup();
	}

}

customElements.define("leaflet-map", LeafletMap);
