package com.wqz.estate.keeper.pojo;

import java.util.Date;

public class ProcessorStartInfo {
    private Integer id;

    private String processTag;

    private Date startTime;

    private Integer delayTime;

    private Integer intervalTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessTag() {
        return processTag;
    }

    public void setProcessTag(String processTag) {
        this.processTag = processTag == null ? null : processTag.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }
}