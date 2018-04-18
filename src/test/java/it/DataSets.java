package it;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class DataSets {
    public static Table<Integer, String, String> data(String... values) {
        Table<Integer, String, String> t = HashBasedTable.create();
        int row = 1;
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            if (i % 2 == 0) {
                t.put(row, "cat", s);
            } else {
                t.put(row, "sum", s);
                row++;
            }
        }
        return t;
    }
}
