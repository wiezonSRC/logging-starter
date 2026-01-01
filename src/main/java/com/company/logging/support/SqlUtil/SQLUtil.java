package com.company.logging.support.SqlUtil;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.time.LocalDateTime;
import java.util.List;

public class SQLUtil {

    public static String buildSql(MappedStatement ms, Object param){
        BoundSql boundSql = ms.getBoundSql(param);
        String sql = boundSql.getSql();
        List<ParameterMapping> mappings = boundSql.getParameterMappings();

        if(mappings == null || mappings.isEmpty()){
            return sql;
        }

        return replacePlaceholders(sql, mappings, boundSql, ms.getConfiguration(), param);
    }

    private static String replacePlaceholders(String sql, List<ParameterMapping> mappings, BoundSql boundSql, Configuration configuration, Object param) {
        StringBuilder sb = new StringBuilder();
        SqlContext ctx = SqlContext.NORMAL;

        int paramIdx = 0;
        MetaObject metaObject = param == null ? null : configuration.newMetaObject(param);
        for(int i = 0; i < sql.length(); i++){
            char c = sql.charAt(i);

            if(ctx==SqlContext.NORMAL){
                if(c == '\'') ctx = SqlContext.SINGLE_QUOTE;
                else if(c == '-' && i+1 <sql.length() && sql.charAt(i+1) == '-') ctx = SqlContext.LINE_COMMENT;
                else if(c == '/' && i+1 < sql.length() && sql.charAt(i+1) == '*') ctx = SqlContext.BLOCK_COMMENT;
            }else if (ctx == SqlContext.SINGLE_QUOTE && c == '\'') {
                ctx = SqlContext.NORMAL;
            } else if (ctx == SqlContext.LINE_COMMENT && c == '\n') {
                ctx = SqlContext.NORMAL;
            } else if (ctx == SqlContext.BLOCK_COMMENT && c == '*' && i + 1 < sql.length() && sql.charAt(i + 1) == '/') {
                ctx = SqlContext.NORMAL;
            }


            if(c == '?' && ctx == SqlContext.NORMAL && paramIdx < mappings.size()){
                ParameterMapping pm = mappings.get(paramIdx++);
                Object value = resolveValue(pm, boundSql, metaObject);
                sb.append(formatValue(value));
                continue;
            }

            sb.append(c);
        }
        return sb.toString();
    }

    private static Object resolveValue(ParameterMapping pm, BoundSql boundSql, MetaObject metaObject) {
        String prop = pm.getProperty();

        if(boundSql.hasAdditionalParameter(prop)){
            return boundSql.getAdditionalParameter(prop);
        }

        if(metaObject != null && metaObject.hasGetter(prop)){
            return metaObject.getValue(prop);
        }

        return null;
    }

    private static String formatValue(Object value) {
        if(value == null) return "NULL";

        if(value instanceof String){
            return "'" + ((String) value).replace("'", "''") + "'";
        }

        if(value instanceof LocalDateTime){
            return "'" + value + "'";
        }

        if(value instanceof Number || value instanceof Boolean ){
            return value.toString();
        }

        return "'" + value + "'";
    }


}
