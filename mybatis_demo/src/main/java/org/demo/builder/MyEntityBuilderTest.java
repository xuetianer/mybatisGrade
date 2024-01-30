package org.demo.builder;

import java.util.Date;
import java.util.Optional;

/**
 * @ClassName MyEntityBuilderTest
 * @description: TODO
 * @author: suhaoran
 * @date 2023年09月19日
 * @version: 1.0
 */
public class MyEntityBuilderTest {

    private Long id;

    private String entityName;

    private Date createTime;

    private String buildContent;

    private Integer num;

    MyEntityBuilderTest(){

    }

    public static class Builder{
        private final MyEntityBuilderTest myEntityBuilderTest=new MyEntityBuilderTest();

        public Builder(Long id,String entityName){
            this(id,entityName,null);
        }

        public Builder(Long id,Date createTime){
            this(id,null,createTime);
        }

        public Builder(Long id,String entityName,Date createTime){
            myEntityBuilderTest.id = id;
            myEntityBuilderTest.entityName= Optional.ofNullable(entityName).orElse("defaultName");
            myEntityBuilderTest.createTime=Optional.ofNullable(createTime).orElse(new Date());
        }

        public Builder setNum(Integer num){
            myEntityBuilderTest.num=num;
            return this;
        }

        public MyEntityBuilderTest build(){
            myEntityBuilderTest.buildContent="buildContent";
            return myEntityBuilderTest;
        }

    }

    public Long getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getBuildContent() {
        return buildContent;
    }

    public Integer getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "MyEntityBuilderTest{" +
                "id=" + id +
                ", entityName='" + entityName + '\'' +
                ", createTime=" + createTime +
                ", buildContent='" + buildContent + '\'' +
                ", num=" + num +
                '}';
    }
}
