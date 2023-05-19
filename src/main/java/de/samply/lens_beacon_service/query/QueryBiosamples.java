package de.samply.lens_beacon_service.query;

import de.samply.lens_beacon_service.EntryType;
import de.samply.lens_beacon_service.beacon.BeaconQueryService;
import de.samply.lens_beacon_service.measurereport.MeasureReportAdmin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryBiosamples extends Query {
    /**
     * Run a query on the biosamples endpoint at a given Beacon site, using the supplied filters.
     *
     * The measureReportAdmin will be used to store the results of the query.
     *
     * @param entryType
     * @param measureReportAdmin
     */
    public void runQueryAtSite(BeaconQueryService beaconQueryService, EntryType entryType, MeasureReportAdmin measureReportAdmin) {
        Integer count = beaconQueryService.runBeaconEntryTypeQueryAtSite(entryType, entryType.baseFilters);
        measureReportAdmin.biosamplesGroupAdmin.setCount(count);
        runBiosamplesTypeQueryAtSite(beaconQueryService, entryType, measureReportAdmin);
    }

    /**
     * Runs the query for the sample type stratifier.
     *
     * @param entryType
     * @param measureReportAdmin
     */
    private void runBiosamplesTypeQueryAtSite(BeaconQueryService beaconQueryService,
                                              EntryType entryType,
                                              MeasureReportAdmin measureReportAdmin) {
        Integer bloodCount = beaconQueryService.runFilterQueryAtSite(entryType, "id", "UBERON:0000178");
        Integer bloodSerumCount = beaconQueryService.runFilterQueryAtSite(entryType, "id", "UBERON:0001977");
        Integer bloodPlasmaCount = beaconQueryService.runFilterQueryAtSite(entryType, "id", "UBERON:0001969");
        Integer lymphCount = beaconQueryService.runFilterQueryAtSite(entryType, "id", "UBERON:0002391");

        measureReportAdmin.biosamplesGroupAdmin.setTypeCounts((bloodCount>=0)?bloodCount:0,
                (bloodSerumCount>=0)?bloodSerumCount:0,
                (bloodPlasmaCount>=0)?bloodPlasmaCount:0,
                (lymphCount>=0)?lymphCount:0);
    }
}
