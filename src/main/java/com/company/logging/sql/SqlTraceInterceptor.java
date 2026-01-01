package com.company.logging.sql;

import com.company.logging.support.SqlUtil.SQLUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class
                }
        )
})
public class SqlTraceInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        try{
            return invocation.proceed();
        }finally{

            long elapsed = System.currentTimeMillis() - start;

            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object param = args.length > 1 ? args[1] : null;
            String sqlId = ms.getId();


            String sql = null;
            SqlTraceContext ctx = SqlTraceContextHolder.get();

            if(ctx != null){
                sql = SQLUtil.buildSql(ms, param);
            }

            if(ctx != null){
                ctx.add(sqlId, sql, extractSqlParam(ms, param),elapsed);
            }
        }

    }

    private String extractSqlParam(MappedStatement ms, Object param) {
        if(param == null) return null;

        BoundSql boundSql = ms.getBoundSql(param);
        List<ParameterMapping> mappings = boundSql.getParameterMappings();

        if(mappings == null || mappings.isEmpty()){
            return null;
        }

        Configuration configuration = ms.getConfiguration();
        MetaObject metaObject = configuration.newMetaObject(param);

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for(ParameterMapping pm : mappings){
            String prop = pm.getProperty();
            Object value;

            if(boundSql.hasAdditionalParameter(prop)){
                value = boundSql.getAdditionalParameter(prop);
            }else if(metaObject.hasGetter(prop)){
                value = metaObject.getValue(prop);
            }else{
                value = null;
            }

            if(!first) sb.append(", ");
            sb.append(prop).append("=").append(formatValue(value));
            first = false;
        }

        sb.append("}");

        return sb.toString();
    }

    private String formatValue(Object value) {
        if(value == null) return "null";

        if(value instanceof  String s){
            return "'" + s + "'";
        }
        if(value instanceof java.util.Date d){
            return "'" + d + "'";
        }


        return String.valueOf(value);
    }
}
