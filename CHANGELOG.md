# Changelog

## Unreleased

### Added

- Added on map click event support
- Added `LMap#openPopup(LMarker)`
- Added polylines
- Added `LMap#containsLComponent(LComponent)`
- Added on component click event support

### Changed

- Made `LPoint` class immutable

### Removed

- Removed the deprecated default constructor for `LMap`
- Removed the close button on the popups, it redirects to `/#close` instead
  of `#close` because of Vaadin
- Removed the deprecated function `LMap#addLComponent(LComponent...)`
- Removed the deprecated function `LMap#getItems()`
- Removed the function `LMap#getComponents()`
- Removed the deprecated function `LMap#removeItem(LComponent...)`

### Fixed

- Fixed `Invalid LatLng object: (NaN, NaN)` error when clicking an icon with
  unset size
