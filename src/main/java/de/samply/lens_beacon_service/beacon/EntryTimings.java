package de.samply.lens_beacon_service.beacon;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EntryTimings {
    public String entryType;
    public Integer queryTiming = (-1);
    public Integer queryTimingCount = 0;
    public Map<String, List<Integer>> stratifierTimings = new HashMap<String, List<Integer>>();

    public List<Integer> getStratifierInfo(String stratifierName) {
        if (stratifierTimings.containsKey(stratifierName))
            return stratifierTimings.get(stratifierName);
        else {
            List<Integer> stratifierInfo = new ArrayList<Integer>();
            stratifierTimings.put(stratifierName, stratifierInfo);
            return stratifierInfo;
        }
    }

    public void showTimings() {
        log.info("    ENTRY: " + entryType);
        log.info("        QUERY TIMING: " + queryTiming + " ms");
        for (String stratifierName : stratifierTimings.keySet())
            if (stratifierTimings.get(stratifierName) != null && stratifierTimings.get(stratifierName).size() > 1)
                log.info("        TIMING STRATIFIER " + stratifierName + ": " + stratifierTimings.get(stratifierName).get(0) + " vals, " + stratifierTimings.get(stratifierName).get(1) + " ms");
    }
}
