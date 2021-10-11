package com.cd.scheduletask;

import com.cd.dao.TeamActivityPromoteMapper;
import com.cd.dao.TeamActivityTypeMapper;
import com.cd.entity.TeamActivityPromote;
import com.cd.entity.TeamActivityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleUpdateStatusTask {
    @Autowired
    private TeamActivityPromoteMapper teamActivityPromoteMapper;
    @Autowired
    private TeamActivityTypeMapper teamActivityTypeMapper;
    private static final Logger LOGGER  = LoggerFactory.getLogger(ScheduleUpdateStatusTask.class);
    //每分钟一次
    @Scheduled(cron = "* 0/1 * * * ?")
    private void updateStatusTask(){
        //获取所有的活动
        List<TeamActivityPromote> teamActivityPromotes = teamActivityPromoteMapper.selectByDoingStatus();
        if(teamActivityPromotes!=null && !teamActivityPromotes.isEmpty()){
            teamActivityPromotes.stream().forEach(
                    (item) ->{
                        String endTime = item.getSignEndTime() + ":00";
                        Date datetime = new Date();
                        SimpleDateFormat sdf  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date dateD = sdf.parse(endTime);
                            Boolean flag = datetime.getTime() >= dateD.getTime();
                            if(flag){
                                TeamActivityPromote teamActivityPromoteInsert = new TeamActivityPromote();
                                teamActivityPromoteInsert.setId(item.getId());
                                teamActivityPromoteInsert.setActivityStatus("1");
                                teamActivityPromoteInsert.setUpdatedTime(new Date());
                                teamActivityPromoteMapper.updateByPrimaryKeySelective(teamActivityPromoteInsert);
                                LOGGER.info("更新该活动{}状态为已结束", item);
                            }
                        } catch (ParseException e) {
                            LOGGER.error("转化错误", e);
                        }
                    }
            );
        }
    }

    //每天一次
    @Scheduled(cron = "* * * 0/1 * ?")
    private void updateActivityType(){
        List<TeamActivityType> teamActivityTypes = teamActivityTypeMapper.selectBydoingStatus();
        if(teamActivityTypes!=null && !teamActivityTypes.isEmpty()){
            teamActivityTypes.stream().forEach(
                    (item)->{
                        String endDate = item.getEndDate();
                        Date datetime = new Date();
                        SimpleDateFormat sdf  =new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date dateD = sdf.parse(endDate);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dateD);
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            Date tomorrow = calendar.getTime();
                            Boolean flag = datetime.getTime() >= tomorrow.getTime();
                            if(flag){
                                TeamActivityType teamActivityType = new TeamActivityType();
                                teamActivityType.setId(item.getId());
                                teamActivityType.setStatus("1");
                                teamActivityTypeMapper.updateByPrimaryKeySelective(teamActivityType);
                                LOGGER.info("更新该活动大类{}状态为已结束", item);
                            }
                        } catch (ParseException e) {
                            LOGGER.error("转化错误", e);
                        }
                    }
            );
        }
    }
}
