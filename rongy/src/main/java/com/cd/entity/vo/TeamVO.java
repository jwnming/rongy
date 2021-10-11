package com.cd.entity.vo;

import com.cd.entity.TeamActivityPromote;

import java.util.Date;
import java.util.List;

public class TeamVO {
    private Integer id;

    private Integer typeNo;

    private String typeTitle;

    private String activityPromoter;

    private String desContent;

    private String startDate;

    private String endDate;

    private String status;

    private String imageUrl;

    private Integer publishNum;

    private Date createdTime;

    private List<TeamActivityPromote> underway;

    private List<TeamActivityPromote> finished;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(Integer typeNo) {
        this.typeNo = typeNo;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle == null ? null : typeTitle.trim();
    }

    public String getActivityPromoter() {
        return activityPromoter;
    }

    public void setActivityPromoter(String activityPromoter) {
        this.activityPromoter = activityPromoter == null ? null : activityPromoter.trim();
    }

    public String getDesContent() {
        return desContent;
    }

    public void setDesContent(String desContent) {
        this.desContent = desContent == null ? null : desContent.trim();
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? null : startDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(Integer publishNum) {
        this.publishNum = publishNum;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<TeamActivityPromote> getUnderway() {
        return underway;
    }

    public void setUnderway(List<TeamActivityPromote> underway) {
        this.underway = underway;
    }

    public List<TeamActivityPromote> getFinished() {
        return finished;
    }

    public void setFinished(List<TeamActivityPromote> finished) {
        this.finished = finished;
    }

}