package com.cd.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TeamActivityPromote implements Serializable {
    private Integer id;

    private String teamTypeNo;

    private String activityName;

    private String activityTime;

    private String signEndTime;

    private String activityAddress;

    private Integer maxPersons;

    private String desContent;

    private String imageUrl;

    private String vadioUrl;

    private String signUrl;

    private Integer viewCount;

    private Integer presentNum;

    private String activityStatus;

    private String isSticky;

    private String createBy;

    private Date createdTime;

    private Date updatedTime;

    private String deleteFlag;

    private String userNo;

    private List<String> imageUrls;

    private List<String> signUrls;

    private String longitude;

    private String latitude;

    private UserInfo createUserInfo;

    private List<UserInfo> userInfos;

    private UserInfo createrInfo;

    private boolean isParticipateIn;

    private boolean isCouldDelete;

    private static final long serialVersionUID = 1L;

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public List<String> getSignUrls() {
        return signUrls;
    }

    public void setSignUrls(List<String> signUrls) {
        this.signUrls = signUrls;
    }

    public boolean isCouldDelete() {
        return isCouldDelete;
    }

    public void setCouldDelete(boolean couldDelete) {
        isCouldDelete = couldDelete;
    }

    public String getSignEndTime() {
        return signEndTime;
    }

    public void setSignEndTime(String signEndTime) {
        this.signEndTime = signEndTime;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamTypeNo() {
        return teamTypeNo;
    }

    public void setTeamTypeNo(String teamTypeNo) {
        this.teamTypeNo = teamTypeNo == null ? null : teamTypeNo.trim();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public UserInfo getCreateUserInfo() {
        return createUserInfo;
    }

    public void setCreateUserInfo(UserInfo createUserInfo) {
        this.createUserInfo = createUserInfo;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime == null ? null : activityTime.trim();
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress == null ? null : activityAddress.trim();
    }

    public Integer getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
    }

    public String getDesContent() {
        return desContent;
    }

    public void setDesContent(String desContent) {
        this.desContent = desContent == null ? null : desContent.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getVadioUrl() {
        return vadioUrl;
    }

    public void setVadioUrl(String vadioUrl) {
        this.vadioUrl = vadioUrl == null ? null : vadioUrl.trim();
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getPresentNum() {
        return presentNum;
    }

    public void setPresentNum(Integer presentNum) {
        this.presentNum = presentNum;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus == null ? null : activityStatus.trim();
    }

    public String getIsSticky() {
        return isSticky;
    }

    public void setIsSticky(String isSticky) {
        this.isSticky = isSticky == null ? null : isSticky.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }

    public UserInfo getCreaterInfo() {
        return createrInfo;
    }

    public void setCreaterInfo(UserInfo createrInfo) {
        this.createrInfo = createrInfo;
    }

    public boolean isParticipateIn() {
        return isParticipateIn;
    }

    public void setParticipateIn(boolean participateIn) {
        isParticipateIn = participateIn;
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
        TeamActivityPromote other = (TeamActivityPromote) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTeamTypeNo() == null ? other.getTeamTypeNo() == null : this.getTeamTypeNo().equals(other.getTeamTypeNo()))
            && (this.getActivityName() == null ? other.getActivityName() == null : this.getActivityName().equals(other.getActivityName()))
            && (this.getActivityTime() == null ? other.getActivityTime() == null : this.getActivityTime().equals(other.getActivityTime()))
            && (this.getActivityAddress() == null ? other.getActivityAddress() == null : this.getActivityAddress().equals(other.getActivityAddress()))
            && (this.getMaxPersons() == null ? other.getMaxPersons() == null : this.getMaxPersons().equals(other.getMaxPersons()))
            && (this.getDesContent() == null ? other.getDesContent() == null : this.getDesContent().equals(other.getDesContent()))
            && (this.getImageUrl() == null ? other.getImageUrl() == null : this.getImageUrl().equals(other.getImageUrl()))
            && (this.getVadioUrl() == null ? other.getVadioUrl() == null : this.getVadioUrl().equals(other.getVadioUrl()))
            && (this.getViewCount() == null ? other.getViewCount() == null : this.getViewCount().equals(other.getViewCount()))
            && (this.getPresentNum() == null ? other.getPresentNum() == null : this.getPresentNum().equals(other.getPresentNum()))
            && (this.getActivityStatus() == null ? other.getActivityStatus() == null : this.getActivityStatus().equals(other.getActivityStatus()))
            && (this.getIsSticky() == null ? other.getIsSticky() == null : this.getIsSticky().equals(other.getIsSticky()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
            && (this.getUpdatedTime() == null ? other.getUpdatedTime() == null : this.getUpdatedTime().equals(other.getUpdatedTime()))
            && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTeamTypeNo() == null) ? 0 : getTeamTypeNo().hashCode());
        result = prime * result + ((getActivityName() == null) ? 0 : getActivityName().hashCode());
        result = prime * result + ((getActivityTime() == null) ? 0 : getActivityTime().hashCode());
        result = prime * result + ((getActivityAddress() == null) ? 0 : getActivityAddress().hashCode());
        result = prime * result + ((getMaxPersons() == null) ? 0 : getMaxPersons().hashCode());
        result = prime * result + ((getDesContent() == null) ? 0 : getDesContent().hashCode());
        result = prime * result + ((getImageUrl() == null) ? 0 : getImageUrl().hashCode());
        result = prime * result + ((getVadioUrl() == null) ? 0 : getVadioUrl().hashCode());
        result = prime * result + ((getViewCount() == null) ? 0 : getViewCount().hashCode());
        result = prime * result + ((getPresentNum() == null) ? 0 : getPresentNum().hashCode());
        result = prime * result + ((getActivityStatus() == null) ? 0 : getActivityStatus().hashCode());
        result = prime * result + ((getIsSticky() == null) ? 0 : getIsSticky().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdatedTime() == null) ? 0 : getUpdatedTime().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", teamTypeNo=").append(teamTypeNo);
        sb.append(", activityName=").append(activityName);
        sb.append(", activityTime=").append(activityTime);
        sb.append(", activityAddress=").append(activityAddress);
        sb.append(", maxPersons=").append(maxPersons);
        sb.append(", desContent=").append(desContent);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", vadioUrl=").append(vadioUrl);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", presentNum=").append(presentNum);
        sb.append(", activityStatus=").append(activityStatus);
        sb.append(", isSticky=").append(isSticky);
        sb.append(", createBy=").append(createBy);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}