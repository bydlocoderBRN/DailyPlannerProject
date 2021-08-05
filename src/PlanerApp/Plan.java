package PlanerApp;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class Plan {
private LocalDateTime startTime;
private LocalDateTime finishTime;
TreeSet<LocalDateTime> notifications = new TreeSet<LocalDateTime>();
Plan(){
    startTime = LocalDateTime.of(1970,1,1,0,0,0);
    finishTime = LocalDateTime.of(1970,1,1,0,0,1);
};
public void setStartTime(LocalDateTime sTime){startTime = sTime;}
public LocalDateTime getStartTime(){return startTime;}
public void setFinishTime(LocalDateTime fTime){finishTime = fTime;}
public LocalDateTime getFinishTime(){return finishTime;}
public void addNotification(LocalDateTime note){notifications.add(note);}
public TreeSet<LocalDateTime> getAllNotifications(){return notifications;}


}
