package com.company.logging.trace;

public class TraceContextHolder {

    private static final ThreadLocal<TraceLevel> LEVEL = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> FORCE_TRACE = new ThreadLocal<>();

    public static void init(TraceLevel level, boolean forceTrace){
        LEVEL.set(level);
        FORCE_TRACE.set(forceTrace);
    }

    public static TraceLevel level(){
        return LEVEL.get();
    }

    public static boolean isForceTrace(){
        return Boolean.TRUE.equals(FORCE_TRACE.get());
    }

    public static boolean isTrace(){
        return level() == TraceLevel.TRACE || isForceTrace();
    }

    public static boolean isDebug(){
        return level() == TraceLevel.DEBUG;
    }

    public static void clear(){
        LEVEL.remove();
        FORCE_TRACE.remove();
    }


}
