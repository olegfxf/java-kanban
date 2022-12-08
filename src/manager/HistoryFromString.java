package manager;

import java.util.ArrayList;
import java.util.List;

public class HistoryFromString {
    static List<Integer> historyFromString(String  strHistoryIds) {
        String[] arrHistoryIds =  strHistoryIds.split(",");

        List<Integer> lstHistoryIds = new ArrayList<>();
        for (String idHistory : arrHistoryIds) {
            lstHistoryIds.add(Integer.valueOf(idHistory));
        }
        return lstHistoryIds;
    }
}
