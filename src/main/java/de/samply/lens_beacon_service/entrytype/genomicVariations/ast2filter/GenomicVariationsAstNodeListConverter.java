package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for biosamples.
 */

@Slf4j
public class GenomicVariationsAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconFilter convertSingleAstNode(AstNode astNode) {
        BeaconFilter beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "genomic_variation":
                    beaconFilter = new GenomicVariationsAstNodeConverter().convert(astNode);
                    break;
                case "variant_type":
                    beaconFilter = new VariantTypeAstNodeConverter().convert(astNode);
                    break;
                case "zygosity":
                    beaconFilter = new ZygosityAstNodeConverter().convert(astNode);
                    break;
            }

        return beaconFilter;
    }
}
