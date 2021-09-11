package PlanerApp;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.awt.TrayIcon.MessageType;


public class Plan {
private LocalDateTime startTime;
private LocalDateTime finishTime;
private String head;
private String content;
private int hashNote;
private static int globalHashNote=0;
private static ExecutorService pool = Executors.newSingleThreadExecutor();
private static boolean isPoolLaunched = false;
private static TreeSet<LocalDateTime> notifications = new TreeSet<LocalDateTime>();
public static HashMap<Integer,Plan> plans = new HashMap<Integer, Plan>();
public static    SystemTray tray;
public static    Image planerIcon;
public static    TrayIcon trayIcon;

protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);



   public static Runnable noteDetector = new Runnable() {
        @Override
        public void run() {
            isNotificationEmpty();
            startNote();
        }
    };
//НЕ ЗАБЫТЬ ПРО shutdown!!!!!!!!!!!!!!!
//Сделать так чтобы весь TreeSet улетал в пул, а при добавлении новый элемент просто добавляется в пул. Результат выводится по готовности. P.S. отказался от такой реализации в пользу отслеживания первого элементра в TreeSet
// При этом .get() должен забирать результат у первого завершенного потока.
    //Статичный пул и TreeSet, позволют значительно снизить нагрузку на процессо при создании параллельных планов с оповещениями в каждом.
    //Нагрузка при старой реализации равна 15% на каждый новый план. При новой: 10-12% на всё!!!!!




Plan(){
    hashNote = globalHashNote;
    startTime = LocalDateTime.of(1970,1,1,0,0,0);
    finishTime = LocalDateTime.of(1970,1,1,0,0,1);
    head = "";
    content ="";
    if (isPoolLaunched == false){
        pool.execute(noteDetector);
        isPoolLaunched = true;


    }


};
    Plan(String hed){
        hashNote = globalHashNote;
        startTime = LocalDateTime.of(1970,1,1,0,0,0);
        finishTime = LocalDateTime.of(1970,1,1,0,0,1);
        head = hed;
        content ="";
        if (isPoolLaunched == false){
            pool.execute(noteDetector);
            isPoolLaunched = true;
        }
    };
    Plan(String hed, String body, LocalDateTime start, LocalDateTime finish){
        hashNote = globalHashNote;
        startTime = start;
        finishTime = finish;
        head = hed;
        content =body;
        if (isPoolLaunched == false){
            pool.execute(noteDetector);
            isPoolLaunched = true;
        }
    }
    public static int newPlan(String hed){
        int hash = getHash();
        plans.put(hash,new Plan(hed));
        return hash;
    }
    public static int newPlan(String hed, String body, LocalDateTime start, LocalDateTime finish){
        int hash = getHash();
        plans.put(hash,new Plan(hed, body, start, finish));
        return hash;
    }
    public static Plan toPlan(int key){
        return plans.get(key);
    }
public String getHead(){return head;}
public void setStartTime(LocalDateTime sTime){startTime = sTime;}
public LocalDateTime getStartTime(){return startTime;}
public void setFinishTime(LocalDateTime fTime){finishTime = fTime;}
public LocalDateTime getFinishTime(){return finishTime;}
public static TreeSet<LocalDateTime> getAllNotifications () { return notifications; }
private static int getHash(){
        globalHashNote+=1;
        return globalHashNote;
    };


/* Реализация уведомлений и будильников:
* при добавлении нового уведомления или будильника(далее объект) наносекунды в добавляемом объекте меняются на 1(уведомление) или 2(будильник)
* для дальнейшего распознавания . Затем объект добавляется в TreeSet, где автоматически сортируется(first() - самое близкое к текущему время).
* Затем result присваивается будущая переменная(Future) и задача(isNotificationNow) добавляется в пул, где в дальнейшем запускается фоновый
* асинхронный поток (см.ниже) */
// 1 - note
// 2 - alarm

    /*Новая реализация:
    * в конструкторе запускается асинхронный поток с постоянной проверкой пустой ли TreeSet. Как только он не пустой запускается isNotificationNow.
    * Когда рекурсия завершается, должно сработать событие, которое запускает оповещение, удаляет notifications.first(), и запускает поток */

    /* Новейшая реализация!!! От рекурсии отказался по причине система не вывозит столько рекурсий. Вместо рекурсии теперь цикл while. Он дает точность в отслеживании оповещений
    вплоть до 0.01 наносекунды, но при этом увеличивает нагрузку на процессор при остлеживании одного оповещения на ~10%. А если пользователю надо будет отслеживать 10 оповещений
    в 10 разных планах??? Тогда компьютор просто взорвется, если я попытаюсь вызвать 10 фоновых потоков по 10% нагрузки процессора каждый. Что же делать?? ПРАВИЛЬНО:
    создать один общий TreeSet для всех оповещений и будильников! При этом, если оповещения совпадают, одно из них удалять. Есди по времени совпадают оповещение и будильник то их
    индекс(наносекунды) сделать равным 3. Остается только решить вопрос с привязкой оповещения к конкретному плану!*/
// ==================================================================================================================================================================================
/*Реализация множества планов + некоторые изменения от 30 августа.
 --Отказался от callable в пользу runnable. От первого слищком много мороки и мне уже не нужно ничего возвращать из потока.
 --Ввел глобальный список планов (HashMap), который состоит из ключа и плана. Добавление нового плана осуществляется только через newPlan(). Этот метод создает новый объект без ссылки,
 берет ключ в генераторе и закидывает в HashMap ключ и план. Обязательно надо сохранять ключ, который возвращает метод newPlan, потому что через него осуществляется доступ
  к созданному плану (через hasmap.get(key)). Куда сохранять и как правильно ипользовать ключ я расписал в main
 --Привязал уведомления к конкретному плану через ключ!!!!*/
public void addNotification(LocalDateTime note){
    note = note.withNano(Integer.parseInt(Integer.toString(hashNote)+"1"));
    notifications.add(note);


}
public void addAlarm(LocalDateTime alarm) {
    alarm = alarm.withNano(Integer.parseInt(Integer.toString(hashNote)+"2"));
    notifications.add(alarm);
}




    /* Метод isNotificationNow не прекращается пока время близжайшего уведомления/будильника(first()) не совпадет с временем системы.
     * Этот метод помещен в отдельный поток для асинхронного выполнения в фоне. Как только метод выходит из петли, первая запись в TreeSet
     * стирается
     * т.к. метод каждый раз обращается к notification.first, то при добавлении оповещения, которое должно сработать раньше, метод будет
     * обрабатывать именно его(т.к. notification.first() дает самое близкое к текущему времени оповещение) */
    private static void isNotificationNow () {
        boolean itis = notifications.first().withNano(0).equals(LocalDateTime.now().withNano(0));
        while (itis != true){
            itis = notifications.first().withNano(0).equals(LocalDateTime.now().withNano(0));

        }
    }

    private static void isNotificationEmpty ()
    { boolean emptyNote= notifications.isEmpty();
        while(emptyNote){
            emptyNote = notifications.isEmpty();
        }

        isNotificationNow();
    }


    private static String wichNotificationNow(){
        int hash = notifications.first().getNano()/10;
        return plans.get(hash).getHead();
    }
    public static void startNote(){
        if(notifications.first().getNano()%10 ==1 ){
            System.out.println("Notification!!" + wichNotificationNow());
               trayIcon.displayMessage("Notify!",wichNotificationNow(), MessageType.INFO);
            notifications.remove(notifications.first());
           pool.execute(noteDetector);
            ;}
        else if(notifications.first().getNano()%10 ==2 ){
            System.out.println("Alarm!!!" + wichNotificationNow());
            Main.isAlertNow=true;
            Main.alertHeader=wichNotificationNow();
            notifications.remove(notifications.first());

          pool.execute(noteDetector);
            ;
        }


            }
public static void trayNote(){
        if (SystemTray.isSupported()){
    try {

        tray = SystemTray.getSystemTray();
        planerIcon = Toolkit.getDefaultToolkit().getImage("trayIcon.gif");
        trayIcon = new TrayIcon(planerIcon, "your Daily Planer");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("A daily planer notification");
        tray.add(trayIcon);

    }
    catch (Exception err){System.err.println(err);}}
}
public void separatePlan(LocalDateTime time){
        LocalDateTime newTime = plusTime(startTime,time);
        while (compareTime(finishTime,newTime)){
            notifications.add(newTime.withNano(Integer.parseInt(Integer.toString(hashNote)+"1")));
            newTime = plusTime(newTime,time);
        }


    }
private LocalDateTime getSegmentSeparated(){return null;}
private boolean compareTime(LocalDateTime biggerTime, LocalDateTime smallerTime){
        if(biggerTime.getYear()>smallerTime.getYear())
            return true;
        else if(biggerTime.getYear()==smallerTime.getYear() && biggerTime.getMonthValue()>smallerTime.getMonthValue())
            return true;
        else if(biggerTime.getYear()==smallerTime.getYear() && biggerTime.getMonthValue()==smallerTime.getMonthValue() && biggerTime.getDayOfMonth()>smallerTime.getDayOfMonth())
            return true;
        else if(biggerTime.getYear()==smallerTime.getYear() && biggerTime.getMonthValue()==smallerTime.getMonthValue() && biggerTime.getDayOfMonth()==smallerTime.getDayOfMonth() && biggerTime.getHour()>smallerTime.getHour())
            return true;
        else if(biggerTime.getYear()==smallerTime.getYear() && biggerTime.getMonthValue()==smallerTime.getMonthValue() && biggerTime.getDayOfMonth()==smallerTime.getDayOfMonth() && biggerTime.getHour()==smallerTime.getHour() && biggerTime.getMinute()>smallerTime.getMinute())
            return true;
        else if(biggerTime.getYear()==smallerTime.getYear() && biggerTime.getMonthValue()==smallerTime.getMonthValue() && biggerTime.getDayOfMonth()==smallerTime.getDayOfMonth() && biggerTime.getHour()==smallerTime.getHour() && biggerTime.getMinute()==smallerTime.getMinute() && biggerTime.getSecond()>smallerTime.getSecond())
            return true;
        else return false;
}
private LocalDateTime plusTime(LocalDateTime time1, LocalDateTime time2){
        return LocalDateTime.of(time1.getYear()+time2.getYear(),time1.getMonthValue()+time2.getMonthValue(),
                time1.getDayOfMonth() + time2.getDayOfMonth(),time1.getHour()+time2.getHour(),time1.getMinute()+time2.getMinute(),time1.getSecond()+time2.getSecond());
}


}



