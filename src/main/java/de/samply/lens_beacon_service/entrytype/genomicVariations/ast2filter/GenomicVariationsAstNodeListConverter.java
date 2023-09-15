package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for cohorts.
 */

@Slf4j
public class GenomicVariationsAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconSearchParameters convertSingleAstNode(AstNode astNode) {
        BeaconSearchParameters beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "chromosome":
                    beaconFilter = new GenomicVariationChromosomeAstNodeConverter().convert(astNode);
                    break;
                case "base_change":
                    beaconFilter = new GenomicVariationBaseChangeAstNodeConverter().convert(astNode);
                    break;
                case "range":
                    beaconFilter = new GenomicVariationRangeAstNodeConverter().convert(astNode);
                    break;
                case "hgvs":
                    beaconFilter = new HgvsAstNodeConverter().convert(astNode);
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
