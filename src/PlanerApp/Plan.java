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
boolean notificationsIsEmpty;
Plan(){
    startTime = LocalDateTime.of(1970,1,1,0,0,0);
    finishTime = LocalDateTime.of(1970,1,1,0,0,1);
    head = "";
    content ="";
    notificationsIsEmpty = true;
    result = pool.submit(callableNotify);
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

    /*Новая реализация:
    * в конструкторе запускается асинхронный поток с постоянной проверкой пустой ли TreeSet. Как только он ен пустой запускается isNotificationNow.
    * Когда рекурсия завершается, должно сработать событие, которое запускает оповещение, удаляет notifications.first(), и запускает поток */

    /* Новейшая реализация!!! От рекурсии отказался по причине система не вывозит столько рекурсий. Вместо рекурсии теперь цикл while. Он дает точность в отслеживании оповещений
    вплоть до 0.01 наносекунды, но при этом увеличивает нагрузку на процессор при остлеживании одного оповещения на ~10%. А если пользователю надо будет отслеживать 10 оповещений
    в 10 разных планах??? Тогда компьютор просто взорвется, если я попытаюсь вызвать 10 фоновых потоков по 10% нагрузки процессора каждый. Что же делать?? ПРАВИЛЬНО:
    создать один общий TreeSet для всех оповещений и будильников! При этом, если оповещения совпадают, одно из них удалять. Есди по времени совпадают оповещение и будильник то их
    индекс(наносекунды) сделать равным 3. Остается только решить вопрос с привязкой оповещения к конкретному плану!*/
public void addNotification(LocalDateTime note){
    note = note.withNano(1);
    notifications.add(note);
    notificationsIsEmpty = false;

}
public void addAlarm(LocalDateTime alarm) {
    alarm = alarm.withNano(2);
    notifications.add(alarm);
    notificationsIsEmpty = false;
}

    public TreeSet<LocalDateTime> getAllNotifications () {
        return notifications;
    }
      /*  public LocalDateTime getFirst(){return notifications.first();}
            public LocalDateTime getLast(){return notifications.last();}*/

    /* Рекурсивный метод isNotificationNow не прекращается пока время близжайшего уведомления/будильника(first()) не совпадет с временем системы.
     * Этот метод помещен в отдельный поток для асинхронного выполнения в фоне. Как только метод выходит из петли, первая запись в TreeSet
     * стирается и result.get() возвращает true
     * т.к. метод каждый раз обращается к notification.first, то при добавлении оповещения, которое должно сработать раньше, метод будет
     * обрабатывать именно его(т.к. notification.first() дает самое близкое к текущему времени оповещение) */
    public void isNotificationNow () {
        boolean itis = notifications.first().equals(LocalDateTime.now().withNano(0));
//        if (notifications.first().equals(LocalDateTime.now().withNano(0))) {
//            //notifications.remove(notifications.first());
//            //вызов события
//            System.out.println("done");
//            startNote();
//
//        } else {
//            System.out.println(LocalDateTime.now());
//            isNotificationNow();
//        }
        while (itis != true){
            itis = notifications.first().withNano(0).equals(LocalDateTime.now().withNano(0));

        }
    }

    public void isNotificationEmpty ()
    { boolean emptyNote= notifications.isEmpty();
//        if (!notifications.isEmpty())
//            isNotificationNow();
//        else isNotificationEmpty();
        while(emptyNote){
            emptyNote = notifications.isEmpty();
        }
        isNotificationNow();
    }
    class MyCallableNote implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            isNotificationEmpty();
            startNote();
            return true;
        }

    }

    public void startNote(){
        if(notifications.first().getNano() ==1 )
//            ControllerClass.lblNote.setText("Notification!");
            System.out.println("Notification!!");
        if(notifications.first().getNano() ==2 ){
//            ControllerClass.lblNote.setText("Alarm!");
            System.out.println("Alarm!!!");
        }


    }
}



