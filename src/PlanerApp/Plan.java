package PlanerApp;


import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.concurrent.*;

public class Plan {
private LocalDateTime startTime;
private LocalDateTime finishTime;
private String head;
private String content;
//НЕ ЗАБЫТЬ ПРО shutdown!!!!!!!!!!!!!!!
//Сделать так чтобы весь TreeSet улетал в пул, а при добавлении новый элемент просто добавляется в пул. Результат выводится по готовности.
// При этом .get() должен забирать результат у первого завершенного потока.
private ExecutorService pool = Executors.newSingleThreadExecutor();
Callable<Boolean> callableNotify = new MyCallableNote();
Future result;
TreeSet<LocalDateTime> notifications = new TreeSet<LocalDateTime>();
Plan(){
    startTime = LocalDateTime.of(1970,1,1,0,0,0);
    finishTime = LocalDateTime.of(1970,1,1,0,0,1);
    head = "";
    content ="";
    notifications.add(LocalDateTime.of(2100,1,1,0,0,0));
};


public void setStartTime(LocalDateTime sTime){startTime = sTime;}
public LocalDateTime getStartTime(){return startTime;}
public void setFinishTime(LocalDateTime fTime){finishTime = fTime;}
public LocalDateTime getFinishTime(){return finishTime;}

/* Реализация уведомлений и будильников:
* при добавлении нового уведомления или будильника(далее объект) наносекунды в добавляемом объекте меняются на 1(уведомление) или 2(будильник)
* для дальнейшего распознавания . Затем объект добавляется в TreeSet, где автоматически сортируется(first() - самое близкое к текущему время).
* Затем result присваивается будущая переменная(Future) и задача(isNotificationNow) добавляется в пул, где в дальнейшем запускается фоновый
* асинхронный поток (см.ниже) */
// 1 - note
// 2 - alarm
public void addNotification(LocalDateTime note){
    note = note.withNano(1);
    notifications.add(note);
    result = pool.submit(callableNotify);
}
public void addAlarm(LocalDateTime alarm){
    alarm = alarm.withNano(2);
    notifications.add(alarm);
    result = pool.submit(callableNotify);}

public TreeSet<LocalDateTime> getAllNotifications(){return notifications;}
      /*  public LocalDateTime getFirst(){return notifications.first();}
            public LocalDateTime getLast(){return notifications.last();}*/

    /* Рекурсивный метод isNotificationNow не прекращается пока время близжайшего уведомления/будильника(first()) не совпадет с временем системы.
    * Этот метод помещен в отдельный поток для асинхронного выполнения в фоне. Как только метод выходит из петли, первая запись в TreeSet
    * стирается и result.get() возвращает true */
public void isNotificationNow(){
    if (notifications.first().equals(LocalDateTime.now())){
        notifications.remove(notifications.first());

    }else {
        isNotificationNow(); }
}
class MyCallableNote implements Callable<Boolean>{
    @Override
    public Boolean call() throws Exception {
        isNotificationNow();
        return true;
            }

    }



}
