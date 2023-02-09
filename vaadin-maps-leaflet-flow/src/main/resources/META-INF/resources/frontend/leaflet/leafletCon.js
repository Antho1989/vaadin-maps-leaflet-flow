import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "leaflet/dist/leaflet.js";
// import "./leaflet-google";

var tilesFromGoogle = true;

export class LeafletMap extends PolymerElement {
	
	static get template() {
		return html`
			<link
					rel="stylesheet"
					href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
			/>

			<style>
				#map-container {
					width: 100%;
					height: 100%;
				}

				#divMaps {
					width: 100%;
					height: 100%;
					position: relative;
				}
				#divMap {
					width: 100%;
					height: 100%;
					position: absolute;
					top: 0px;
					left: 0px;
				}
				#divGMap {
					width: 100%;
					height: 100%;
					position: abolute;
					top: 0px;
					left: 0px;
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

				.leaflet-container {
					#fff0 !important
				}
			</style>
			<div id="divMaps">
				<div id="divGMap">
				</div>
				<div id="divMap"></div>
			</div>
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
			},
			secondTile: {
				type: Object,
				notify: true
			},
			Google: {
				type: Object,
				notify: true
			}
		};
	}

	ready() {
		this._initMap();
	}

	setViewPoint(obj) {
		console.log("obj.point.coordinates : " + JSON.stringify(obj.point.coordinates));
		if (tilesFromGoogle){
			var _center = new google.maps.LatLng(obj.point.coordinates.lat, obj.point.coordinates.lng);
			this.Google.setCenter(obj.point.coordinates);
			this.Google.setZoom(obj.point.zoom);
		} else {
			this.map.setView(obj.point.coordinates, obj.point.zoom);
		}
	}

	addTileLayer(layer, opacity) {
		console.log("########## add tile layer");
		this.secondTile = L.tileLayer(layer.tile.link, {
			attribution: layer.tile.attribution,
			maxZoom: layer.tile.zoom,
			id: layer.tile.id,
			opacity: opacity
		}).addTo(this.map);
		this.secondTile.bringToFront();
	}

	setTileLayer(layer) {
		console.log("####### set tile layer....");
		if (tilesFromGoogle) {
			var _zoom = this.map._zoom;
			var _center = this.map._center;
			console.log("####### tiles from google : init map");
			this._initMap();
			this.setZoomLevel(_zoom);
			this.map.center = _center;
		}
		this.secondTile = undefined;
		if (layer == null) {
			this.map.tile = null;
		} else {
			this.tile = L.tileLayer(layer.tile.link, {
				attribution: layer.tile.attribution,
				maxZoom: layer.tile.zoom,
				id: layer.tile.id
			}).addTo(this.map);
			this.tile.bringToFront();
			tilesFromGoogle = false;
			console.log("##### tile layer is no longer google)");
		}
	}


	setZoomLevel(zoom) {
		if (tilesFromGoogle)
			this.Google._zoom = zoom;
		else
			this.map.setZoom(zoom);
	}

	zoomToExtent(rectangle) {
		var str = '';
		for (var prop in this.Google) {
			if (typeof this.Google[prop] == 'string')
				str += prop + ': ' + this.Google[prop] + '; \n';
			else
				str += prop + ': { \n' + this.Google[prop] + '}';
		}
		console.log("#####>>> map1." + str);

		var maxZoom = 18
		if (tilesFromGoogle) {
			maxZoom = 20;
			const gglBounds = {
				north: rectangle.bounds.maxLat,
				south: rectangle.bounds.minLng,
				west: rectangle.bounds.minLat,
				east: rectangle.bounds.maxLng,
			};
			this.Google.fitBounds(gglBounds);
			if (this.Google._zoom > maxZoom)
				this.Google._zoom = maxZoom;
		} else {
			this.map.fitBounds([
				[rectangle.bounds.maxLat, rectangle.bounds.minLng],
				[rectangle.bounds.minLat, rectangle.bounds.maxLng]
			]);
			if (this.map.getZoom() > maxZoom)
				this.map.setZoom(maxZoom);
		}
		console.log("zoom extended to " + this.map.getZoom); var str = '';
		for (var prop in this.Google) {
			if (typeof this.Google[prop] == 'string')
				str += prop + ': ' + this.Google[prop] + '; \n';
			else
				str += prop + ': { \n' + this.Google[prop] + '}';
		}
		console.log("#####>>> map2." + str);
	}

	switchToGmap(view /* Possible types: SATELLITE, ROADMAP, HYBRID, TERRAIN */) {
		this.Google = new L.Map(this.$.divGMap, {
			center: [41.4583, 12.7059],
			zoom: 5,
			markerZoomAnimation: false,
			zoomControl: false
		});
		
		console.log(JSON.stringify("##### switch to gmap : " + L.version));
		var zoomControl = new L.Control.Zoom({ position: 'topright' });
		
const getCircularReplacer = () => {
  const seen = new WeakSet();
  return (key, value) => {
    if (typeof value === "object" && value !== null) {
      if (seen.has(value)) {
        return;
      }
      seen.add(value);
    }
    return value;
  };
};
console.log(JSON.stringify(this.Google, getCircularReplacer()));
console.log(JSON.stringify(this.map, getCircularReplacer()));
		
		var ggl = new L.Google('ROADMAP');
	   
		  var url = 'https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png',
			attr =
			'Map data: &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://viewfinderpanoramas.org">SRTM</a> | Map style: &copy; <a href="https://opentopomap.org">OpenTopoMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)',
			otm = new L.TileLayer(url, {
			  attribution: attr,
			  /*subdomains:"1234"*/
			});
	   
		  var baseLayers = {
			"Google Map": ggl,
			"Leaflet Map": otm,
		  };
	   
		  var layersControl = L.control.layers(baseLayers, null, { collapsed:false });
	   
		  layersControl.addTo(this.Google);
		  zoomControl.addTo(this.Google);
	   
		  this.Google.addLayer(ggl);
// 		try {
// 			console.log("google map ::: try");
// 			var _center, _zoom;
// 			if (this.map._lastCenter) {
// 				console.log("google map ::: if 1");
// 				_center = {
// 					lat: this.map._lastCenter.lat,
// 					lng: this.map._lastCenter.lng,
// 				};
// 				_zoom = this.map._zoom
// 			} else if (this.map.center) {
// 				console.log("google map ::: if 2");
// 				_center = {
// 					lat: this.map.center.lat,
// 					lng: this.map.center.lng,
// 				};
// 				_zoom = this.map.zoom;
// 			} else {
// 				console.log("google map ::: if 3 ");
// 				console.log("google map ::: if 3 " + JSON.stringify(this));
// 				_center = {
// 					lat: 44.708033124067946,
// 					lng: -0.5830329864848995,
// 				};
// 				_zoom = 19
// 			}
// 			console.log("google map ::: if done");
// 			if (!tilesFromGoogle) {
// 				console.log("google map ::: ! tile from ggl");
// 				this.Google = new google.maps.Map(this.$.divGMap, {
// 					center: _center,
// 					zoom: _zoom,
// 					maxZoom: 19,
// 					tilt: 0,
// 					mapTypeId: google.maps.MapTypeId[view],
// 					disableDefaultUI: true,
// 					keyboardShortcuts: false,
// 					zoomControl: true,
// 					draggable: true,
// 					disableDoubleClickZoom: true,
// 					scrollwheel: true,
// 					streetViewControl: true,
// 					backgroundColor: 'transparent'
// 				});
// 				console.log("google map ::: ! tile from ggl done");
// 			} else
// 				this.Google.center = _center;
// 			console.log("google map ::: else : tile from ggl");
// 			this.$.divMap.style.zIndex = 1;
// 			this.$.divGMap.style.zIndex = 2;
// 			tilesFromGoogle = true;

// const getCircularReplacer = () => {
//   const seen = new WeakSet();
//   return (key, value) => {
//     if (typeof value === "object" && value !== null) {
//       if (seen.has(value)) {
//         return;
//       }
//       seen.add(value);
//     }
//     return value;
//   };
// };
// const stringified = JSON.stringify(this.$.divMap.getElementsByClassName("leaflet-pane leaflet-marker-pane"), getCircularReplacer());
// console.log(stringified);

// 			this.divGMap.onAdd(this.divGMap, this.$.divMap.getElementsByClassName("leaflet-pane leaflet-marker-pane"));
// 		} catch (error) {
// 			console.log("ERROR ggl : " + error);
// 		}
	}

	_initMap() {
		console.log("####### init map");
		console.log(JSON.stringify("##### init map : " + L.version));
		tilesFromGoogle = false;
		super.ready();
		this.map = new L.map(this.$.divMap);

		this.tile = undefined;
		this.Google = undefined;

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
		console.log(JSON.stringify("##### end of init map : " + L.version));
	}

	addMarker(id, obj) {
		let leafIcon;

		if (obj.properties.icon.type === 'DivIcon') {
			leafIcon = new L.divIcon(obj.properties.icon);
		} else {
			leafIcon = new L.Icon(obj.properties.icon);
		}

		let item = L.marker(obj.geometry.coordinates, { icon: leafIcon })
				.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, { closeButton: false });
		}

		this.addListenerToEvented(id, item);

		this.items[id] = item;
	}

	addPolygon(id, obj) {
		let item = L.polygon(obj.geometry.coordinates, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, { closeButton: false });
		}

		this.addListenerToEvented(id, item);

		this.items[id] = item;
	}

	addPolyline(id, obj) {
		let item = L.polyline(obj.geometry.coordinates, obj.properties)
			.addTo(this.map);

		if (obj.properties.popup != null) {
			item.bindPopup(obj.properties.popup, { closeButton: false });
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
