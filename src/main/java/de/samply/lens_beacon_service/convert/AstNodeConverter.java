package de.samply.lens_beacon_service.convert;

import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Generic template for converting a single Lens AST node into a Beacon 2 filter.
 */
public interface AstNodeConverter {
    public BeaconFilter convert(AstNode astNode);
}
