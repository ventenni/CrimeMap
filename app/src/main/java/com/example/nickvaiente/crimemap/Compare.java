package com.example.nickvaiente.crimemap;

import com.example.nickvaiente.crimemap.OSM.LocationInfo;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceInfo;
import com.example.nickvaiente.crimemap.QPS.entity.offence.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Tae's Puter on 17/05/2017.
 */

public class Compare {
    private OffenceBoundary offenceResult = null;
    private LocationInfo locationResult = null;
    private Map<String, Integer> dates;
    private SortedSet<String> sortedKeys;

    public Compare() {

    }

    public OffenceBoundary getOffenceResult() {
        return offenceResult;
    }

    public void setOffenceResult(OffenceBoundary offenceResult) {
        this.offenceResult = offenceResult;
    }

    public LocationInfo getLocationResult() {
        return locationResult;
    }

    public void setLocationResult(LocationInfo locationResult) {
        this.locationResult = locationResult;
    }

    //  Time increments to display on graphs eg. 5 yrs will be displayed in monthly increments
    public void plotData() {
        Map<String, Integer> dates;

        Long timePeriod = QueenslandPoliceService.getInstance().getTimePeriod();
        if (timePeriod == 2629743) { //display in days on the graph
            getOffenceDates(10);
        }
//        else if (timePeriod <= 15778458) { //display in weeks on the graph
//            dates = new HashMap();
//            for (Result result : offenceResult.getResult()) {
//                for(OffenceInfo offence : result.getOffenceInfo()) {
//                    String offenceDate = offence.getStartDate().substring(0, 11);
//                    if (dates.containsKey(offenceDate)) {
//                        int value = dates.get(offenceDate);
//                        dates.put(offenceDate, value+1);
//                    } else {
//                        dates.put(offenceDate, 1);
//                    }
//                }
//            }
//        }
        else { //display in months on the graph
            getOffenceDates(7);
        }
    }


    // Retrieves offences that are within the timeframe specified in the filter.
    private void getOffenceDates(int endIndex) {
        dates = new HashMap();
        for (Result result : offenceResult.getResult()) {
            for(OffenceInfo offence : result.getOffenceInfo()) {
                String offenceDate = offence.getStartDate().substring(0, endIndex);
                if (dates.containsKey(offenceDate)) {
                    int value = dates.get(offenceDate);
                    dates.put(offenceDate, value+1);
                } else {
                    dates.put(offenceDate, 1);
                }
            }
        }
        sortedKeys = new TreeSet<String>(dates.keySet());
    }

    public SortedSet<String> getSortedKeys() {
        return sortedKeys;
    }

    public Map<String, Integer> getDates() {
        return dates;
    }
}