package com.company.logging.sql;

import java.util.List;

public class SqlTraceContextHolder {

    private static ThreadLocal<SqlTraceContext> CTX = new ThreadLocal<>();

    public static void init(){
        CTX.set(new SqlTraceContext());
    }

    public static List<SqlTrace> getAll(){
        SqlTraceContext ctx = CTX.get();
        return ctx != null ? ctx.getTraces() : List.of();
    }

    public static SqlTraceContext get(){
        return CTX.get();
    }

    public static long totalElapsed(){
        long total = 0;
        
        if(CTX.get() != null){
            total = CTX.get().getTotalElapsed();
        }

        return total;
    }

    public static void clear(){
        CTX.remove();
    }

}
