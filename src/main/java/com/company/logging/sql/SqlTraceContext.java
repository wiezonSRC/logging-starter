package com.company.logging.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlTraceContext {

    private final List<SqlTrace> traces = new ArrayList<>();
    private long totalElapsed = 0;

    public void add(String sqlId, String sql, String sqlParam, long elapsed){
        traces.add(new SqlTrace(sqlId, sql, sqlParam, elapsed));
        totalElapsed += elapsed;
    }

    public List<SqlTrace> getTraces(){
        return traces;
    }

    public long getTotalElapsed(){
        return totalElapsed;
    }

    public int count(){
        return traces.size();
    }
}
