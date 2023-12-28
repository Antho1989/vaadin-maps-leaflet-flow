import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
//import "leaflet/dist/leaflet.js";
import "@vaadin/flow-frontend/leaflet/leaflet.js";

export class LeafletMap extends PolymerElement {
	static get template() {
		return html`
			<link
					rel="stylesheet"
					href="frontend/leaflet/leaflet.css"
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

	setTileLayer(layer) {
		if (this.tile) {
			this.tile.remove(this.map);
		}
		this.tile = L.tileLayer(layer.tile.link, {
			attribution: layer.tile.attribution,
			maxZoom: layer.tile.zoom,
			id: layer.tile.id
		}).addTo(this.map);
		this.tile.bringToFront();
	}

	setZoomLevel(zoom) {
		this.map.setZoom(zoom);
	}

	zoomToExtent(rectangle) {
		this.map.fitBounds([
			[rectangle.bounds.maxLat, rectangle.bounds.minLng],
			[rectangle.bounds.minLat, rectangle.bounds.maxLng]
		]);
		if (this.map.getZoom() > 18)
		    this.map.setZoom(18);

	}

	_initMap() {
		super.ready();
		this.map = new L.map(this.$.divMap);

		this.tile = undefined;

		setTimeout(() => this.map.invalidateSize(), 1)

		this.items = {};

		let vaadinServer = this.$server;
		let map = this.map;

		this.map.on('click', function (e) {
			vaadinServer.onMapClick(e.latlng.lat, e.latlng.lng);
		});

		this.map.on('zoomend moveend', function () {
			vaadinServer.onMapCenterChanged(map.getCenter().lat, map.getCenter().lng, map.getZoom());
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

		if (!!obj.properties.tooltip) {
			item.bindTooltip(obj.properties.tooltip, {closeButton: false});
		}

		this.addListenerToEvented(id, item);

		this.items[id] = item;
	}

	addPolygon(id, obj) {
		let item = L.polygon(obj.geometry.coordinates, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, {closeButton: false});
		}

		this.addListenerToEvented(id, item);

		this.items[id] = item;
	}

	addPolyline(id, obj) {
		let item = L.polyline(obj.geometry.coordinates, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, {closeButton: false});
		}

		this.addListenerToEvented(id, item);

		this.items[id] = item;
	}

	addCircle(id, obj) {
		let item = L.circle(obj.geometry, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup);
		}

		this.addListenerToEvented(id, item);

		this.items[id] = item;
	}

	addListenerToEvented(id, evented) {
		let vaadinServer = this.$server;

		evented.on('click', function (e) {
			vaadinServer.onComponentEvent(id, e.type);
		});
	}

	deleteItem(id) {
		this.items[id].remove();
		delete this.items[id];
	}

	openPopup(id) {
		this.items[id].openPopup();
	}

}

customElements.define("leaflet-map", LeafletMap);
