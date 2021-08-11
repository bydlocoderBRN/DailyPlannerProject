package PlanerApp;

import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.TreeSet;

public class Plan {
private LocalDateTime startTime;
private LocalDateTime finishTime;
private String head;
private String content;
TreeSet<LocalDateTime> notifications = new TreeSet<LocalDateTime>();
Plan(){
    startTime = LocalDateTime.of(1970,1,1,0,0,0);
    finishTime = LocalDateTime.of(1970,1,1,0,0,1);
    head = "";
    content ="";
    notifications.add(LocalDateTime.of(2100,1,1,0,0,0));
};
// 1 - note
// 2 - alarm
public void setStartTime(LocalDateTime sTime){startTime = sTime;}
public LocalDateTime getStartTime(){return startTime;}
public void setFinishTime(LocalDateTime fTime){finishTime = fTime;}
public LocalDateTime getFinishTime(){return finishTime;}
public void addNotification(LocalDateTime note){
    note = note.withNano(1);
    notifications.add(note);}
    public void addAlarm(LocalDateTime alarm){
        alarm = alarm.withNano(2);
        notifications.add(alarm);}
public TreeSet<LocalDateTime> getAllNotifications(){return notifications;}
      /*  public LocalDateTime getFirst(){return notifications.first();}
            public LocalDateTime getLast(){return notifications.last();}*/
public boolean isNotificationNow(){
    if (notifications.first().equals(LocalDateTime.now())){
        notifications.remove(notifications.first());
        return true;
    }else return false;
}

class MyTimerTask extends TimerTask{
    @Override
    public void run() {
//        long delay = notifications.first().getYear()* (int)Math.pow(3.15,10^16) + notifications.first().getMonthValue();
    }
}
}
