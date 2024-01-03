# Changelog

## 1.4.3

### Fixed

- Fixed `zoomToExtent` not working when called just after map is created

## 1.4.2

### Added

- Added `LMarker` tooltip

## 1.2.0 - 1.4.1

### Added

- Added on map click event support
- Added `LMap#openPopup(LMarker)`
- Added polylines
- Added `LMap#containsLComponent(LComponent)`
- Added on component click event support
- Added `LCenter` update
- Added `LMap#invalidateSize()`

### Changed

- Changed `LPoint` class to be immutable and serializable by Jackson
- Changed `LCenter` class to be immutable

### Removed

- Removed the deprecated default constructor for `LMap`
- Removed the close button on the popups, it redirects to `/#close` instead
  of `#close` because of Vaadin
- Removed the deprecated function `LMap#addLComponent(LComponent...)`
- Removed the deprecated function `LMap#getItems()`
- Removed the function `LMap#getComponents()`
- Removed the deprecated function `LMap#removeItem(LComponent...)`
- Removed marker click listener from `LMap`

### Fixed

- Fixed `Invalid LatLng object: (NaN, NaN)` error when clicking an icon with
  unset size
