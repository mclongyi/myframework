package com.longyi.csjl.builder;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SqlBuilder {
    private String userName;
    private String password;
    private String url;
    private String CharSet;
    private Integer activeCount;
    private Integer maxActiveCount;
    private Integer activeTime;

    public static class  Builder{
        private String userName;
        private String password;
        private String url;
        private String CharSet;
        private Integer activeCount;
        private Integer maxActiveCount;
        private Integer activeTime;

        public Builder buildUserName(String userName){
            this.userName=userName;
            return this;
        }

        public Builder buildPassword(String password){
            this.password=password;
            return this;
        }

        public Builder buildUrl(String url){
            this.url=url;
            return this;
        }

        public SqlBuilder build(){
            return new SqlBuilder(this);
        }
    }

    public SqlBuilder(Builder builder){
        this.userName=builder.userName;
        this.password=builder.password;
        this.url=builder.url;
    }


}
