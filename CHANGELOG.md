# Changelog

## Unreleased

### Added

- Added on map click event support
- Added `LMap#openPopup(LMarker)`
- Added polylines

### Changed

- Made `LPoint` class immutable

### Removed

- Removed the deprecated default constructor for `LMap`
- Removed the close button on the popups, it redirects to `/#close` instead
  of `#close` because of Vaadin

### Fixed

- Fixed `Invalid LatLng object: (NaN, NaN)` error when clicking an icon with
  unset size
