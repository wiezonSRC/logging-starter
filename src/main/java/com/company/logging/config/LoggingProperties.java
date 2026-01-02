package com.company.logging.config;

import com.company.logging.trace.TraceLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix ="log")
public class LoggingProperties {
    private final Trace trace = new Trace();
    private final Slow slow = new Slow();

    public static class Trace {
        // log.trace.level
        private TraceLevel level = TraceLevel.PROD;

        public TraceLevel getLevel(){
            return level;
        }

        public void setLevel(TraceLevel level){
            this.level = (level != null) ? level : TraceLevel.PROD;
        }
    }

    public static class Slow{

        // log.slow.query
        private Query query = new Query();

        public static class Query{
            private int ms = 300;
            private int totalMs = 1000;

            public int getMs(){
                return this.ms;
            }
            public int getTotalMs(){
                return this.totalMs;
            }
            public void setMs(int ms){
                this.ms = ms;
            }
            public void setTotalMs(int totalMs){
                this.totalMs = totalMs;
            }

        }

        public Query getQuery(){
            return query;
        }

        public void setQuery(Query query){
            this.query = query;
        }
    }

    public Trace getTrace(){
        return trace;
    }
    public Slow getSlow(){
        return slow;
    }
}
