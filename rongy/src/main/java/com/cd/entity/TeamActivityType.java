package com.cd.entity;

import java.io.Serializable;
import java.util.Date;

public class TeamActivityType implements Serializable {
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

    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TeamActivityType other = (TeamActivityType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTypeNo() == null ? other.getTypeNo() == null : this.getTypeNo().equals(other.getTypeNo()))
            && (this.getTypeTitle() == null ? other.getTypeTitle() == null : this.getTypeTitle().equals(other.getTypeTitle()))
            && (this.getActivityPromoter() == null ? other.getActivityPromoter() == null : this.getActivityPromoter().equals(other.getActivityPromoter()))
            && (this.getDesContent() == null ? other.getDesContent() == null : this.getDesContent().equals(other.getDesContent()))
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTypeNo() == null) ? 0 : getTypeNo().hashCode());
        result = prime * result + ((getTypeTitle() == null) ? 0 : getTypeTitle().hashCode());
        result = prime * result + ((getActivityPromoter() == null) ? 0 : getActivityPromoter().hashCode());
        result = prime * result + ((getDesContent() == null) ? 0 : getDesContent().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", typeNo=").append(typeNo);
        sb.append(", typeTitle=").append(typeTitle);
        sb.append(", activityPromoter=").append(activityPromoter);
        sb.append(", desContent=").append(desContent);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", status=").append(status);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}