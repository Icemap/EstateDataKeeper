package com.wqz.estate.keeper.pojo;

import java.util.Date;

public class ErrorInfo {
    private Integer id;

    private String processorName;

    private String errorData;

    private Date errorDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName == null ? null : processorName.trim();
    }

    public String getErrorData() {
        return errorData;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData == null ? null : errorData.trim();
    }

    public Date getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }
}