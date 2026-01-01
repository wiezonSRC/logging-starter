package com.company.logging.sql;


public class SqlTrace {
    private String sqlId;
    private String sql;
    private String sqlParam;
    private long elapsed;


    public SqlTrace(){}

    public SqlTrace(String sqlId, String sql, String sqlParam, long elapsed){
        this.sqlId=sqlId;
        this.sql=sql;
        this.sqlParam = sqlParam;
        this.elapsed=elapsed;
    }


    public String getSqlId(){ return this.sqlId;}
    public String getSql(){ return this.sql;}
    public String getSqlParam() {return this.sqlParam;}
    public long getElapsed(){ return this.elapsed;}
}
