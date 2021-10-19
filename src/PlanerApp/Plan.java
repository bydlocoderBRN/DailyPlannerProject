package PlanerApp;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.awt.TrayIcon.MessageType;
import java.util.regex.Pattern;


public class Plan {
private LocalDateTime startTime;
private LocalDateTime finishTime;
private String head;
private String content;
private int hashNote;
private static int notificationCount=0;
private static int globalHashNote=0;
private static ExecutorService pool = Executors.newSingleThreadExecutor();
private static boolean isPoolLaunched = false;
private static TreeMap<String,LocalDateTime> notifications = new TreeMap<String,LocalDateTime>();
public static HashMap<Integer,Plan> plans = new HashMap<Integer, Plan>();
public static    SystemTray tray;
public static    Image planerIcon;
public static    TrayIcon trayIcon;






//==============================================================================================================================================================================================================================\
    //Plan
//==============================================================================================================================================================================================================================\
    public String getHead(){return head;}
    public void setStartTime(LocalDateTime sTime){startTime = sTime;}
    public LocalDateTime getStartTime(){return startTime;}
    public void setFinishTime(LocalDateTime fTime){finishTime = fTime;}
    public LocalDateTime getFinishTime(){return finishTime;}
    public String getBody(){return content;}


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
    Plan( String hed, String body, LocalDateTime start, LocalDateTime finish){
        hashNote = globalHashNote;
        startTime = start;
        finishTime = finish;
        head = hed;
        if (head == ""){
            head = "Plan" + hashNote;
        }
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

    private static int getHash(){
        globalHashNote+=1;
        return globalHashNote;
    };

    public  void separatePlan(long year, long month, long day, long hour, long minute, long second){
        final long SECOND = second;
        final long MINUTE = minute;
        final long HOUR = hour;
        final long DAY = day;
        final long MONTH = month;
        final long YEAR = year;
        int MAX_DAYS;
        long updatingMonth=0;
        LocalDateTime newTime = startTime;
        while(compareTime(finishTime,newTime)){
            addNotification(newTime);
            second = SECOND;
            minute = MINUTE;
            hour = HOUR;
            day = DAY;
            month = MONTH;
            year = YEAR;
            second = newTime.getSecond()+second;
            /*0т этого момента до начала дней просто обрабатывается введенное пользователем значение. Излищки секунд/минут/часов перекидываются на следущую категорию(излишки секунд в минуты, излишки минут в часы и т.д)
            и в оповещение которое будет добавлено добавляется кореектное значение без излишков. Пример - если пользователь хочет чтобы уведомления выводились каждые 63 минуты, при этом время начала плана ХХХХ:ХХ:ХХ:03:05:00,
            то время первого оповещения будет ХХХХ:ХХ:ХХ:04:08:00 (излишки минут перекинулись в часы, а остаток сложился с исходным временем и зафиксировался в оповещении)*/
            while(second>59){
                second = second - 60;
                minute +=1;
            }
            newTime = newTime.withSecond((int)second);
            minute = newTime.getMinute() + minute;
            while (minute>59){
                minute = minute - 60;
                hour+=1;
            }
            newTime = newTime.withMinute((int)minute);
            hour = newTime.getHour() + hour;
            while (hour>23){
                hour = hour - 24;
                day+=1;
            }
            //1,3,5,7,8,10,12 - 31 день
            newTime = newTime.withHour((int)hour);
            day = newTime.getDayOfMonth() + day;
            /*т.к. дни в зависимости от месяца неравномерны, то реализация обработки дней будет иная. Сначала вычисляется какое максимальное число дней в месяце от которого сейчас идет счет. Если число дней введенных пользователем
            в сумме с днями даты от которой идет отсчет больше чем это максимальное число, то происходит обработка, и излишки дней перекидываются в месяцы. Поскольку за месяцами надо следить динамически, то эти излишки сразу прибавляются
            к месяцам в дате от которой идет отсчет. Излишки мсяцев тут же перекидываются в годы. Затем вычисляется новое максимальное число дней в месяце и итерация завершается.

            */

            MAX_DAYS = calculateMaxDays(newTime);
            while (day>MAX_DAYS){
                day = day - MAX_DAYS;
                updatingMonth = newTime.getMonthValue()+1;
                if(updatingMonth>12){
                    year+=1;
                    updatingMonth = updatingMonth - 12;
                }
                newTime = newTime.withMonth((int)updatingMonth);
                MAX_DAYS = calculateMaxDays(newTime);
            }
            newTime = newTime.withDayOfMonth((int)day);
            month = newTime.getMonthValue() + month;
            while (month > 12){
                month = month-12;
                year+=1;
            }
            newTime = newTime.withMonth((int)month);
            year = newTime.getYear()+year;
            newTime = newTime.withYear((int)year);

        }
    }
    //метод определяет максимальное число дней в конкретном месяце. Нужен для правильной работы separatePlan(...)
    private int calculateMaxDays(LocalDateTime newTime) {
        int max=0;
        if (newTime.getMonthValue() == 1 || newTime.getMonthValue() == 3 || newTime.getMonthValue() == 5 || newTime.getMonthValue() == 7 || newTime.getMonthValue() == 8 || newTime.getMonthValue() == 10 || newTime.getMonthValue() == 12) {
            max = 31;
        } else if (newTime.getMonthValue() == 4 || newTime.getMonthValue() == 6 || newTime.getMonthValue() == 9 || newTime.getMonthValue() == 11) {
            max=30;
        } else if (newTime.getMonthValue() == 2) {
            if (newTime.getYear() % 4 == 0 || (newTime.getYear() % 100 == 0 && newTime.getYear() % 400 == 0)) {
                max=29;
            } else {
                max=28;
            }

        }
        return max;
    }
    /*Метод добавляет заданное пользователем количество оповещений, которые идут через примерно равные интервалы. Сначала вычисляется промежуток (в секундах) между временм начала плана и временем конца плана.
    затем полученный промежуток делится на количество оповещений заданных пользователем. Так получается интервал(в секундах), через который должны идти оповещения. Этот интервал помещается в метод separatePlan(...), и он добавляет нужное количество оповещений.
    Для перевода в LocalDateTime в секунды используется преобразование к типу Instant, который представляет из себя колчество наносекунд от начала Unix исчисления.
    >>>ВАЖНО: разбиение получается с погрешностью +-1 план, потому что при делении интервала на количество планов происходит округление.
    */
    public void segmentPlan(int numberOfNotes){
        LocalDateTime interval = startTime;
        long secStart = interval.toInstant(ZoneOffset.of("+3")).toEpochMilli()/1000;
        interval = finishTime;
        long secFinish = interval.toInstant(ZoneOffset.of("+3")).toEpochMilli()/1000;
        long periodOfSeconds = (secFinish - secStart)/numberOfNotes;
        separatePlan(0,0,0,0,0,periodOfSeconds);
    }
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
    public static ArrayList<Integer> plansDayFilter(LocalDate dateCurr){
        ArrayList<Integer> keys = new ArrayList<Integer>();
        LocalDate finish;
        LocalDate start;
        for (int i : plans.keySet()){
            finish = LocalDate.of(Plan.toPlan(i).getFinishTime().getYear(),Plan.toPlan(i).getFinishTime().getMonthValue(),Plan.toPlan(i).getFinishTime().getDayOfMonth());
            start = LocalDate.of(Plan.toPlan(i).getStartTime().getYear(),Plan.toPlan(i).getStartTime().getMonthValue(),Plan.toPlan(i).getStartTime().getDayOfMonth());
            if(finish.isEqual(dateCurr) || start.isEqual(dateCurr) || (finish.isAfter(dateCurr)&&start.isBefore(dateCurr))){
                keys.add(i);
            }
        }
        return keys;
    }

//==============================================================================================================================================================================================================================\
    //\Plan\
//==============================================================================================================================================================================================================================\


//==============================================================================================================================================================================================================================\
    //Notifications
//==============================================================================================================================================================================================================================\
//1-note 2-alarm
    public static TreeMap<String, LocalDateTime> getAllNotifications () { return notifications; }
    //EDITED FOR TREEMAP
    public static LocalDateTime getNotification(String noteKey){return notifications.get(noteKey);}
    //Структура ключа для оповещений <порядковый_номер.тип.ключ_плана> пример: 15001002
    public void addNotification(LocalDateTime note){
        notificationCount+=1;
        String key = notificationCount + "." + "1" + "." + hashNote;
        if (!note.isBefore(LocalDateTime.now())){
            notifications.put(key,note);}
        else {System.out.println("Выбранное время уже прошло");}
    }
    //EDITED FOR TREEMAP
    public void addAlarm(LocalDateTime alarm) {
        notificationCount+=1;
        String key = notificationCount + "." + "2" + "." + hashNote;
        if (!alarm.isBefore(LocalDateTime.now())){
            notifications.put(key, alarm);}
        else {System.out.println("Выбранное время уже прошло");}
    }
//EDITED FOR TREEMAP

    public static int getNotificationCount(String noteKey){
        return Integer.parseInt(noteKey.split("\\.")[0]);

    }
    public static int getNotificationType(String noteKey){
        return Integer.parseInt(noteKey.split("\\.")[1]);

    }

    public static int getNotificationPLanKey(String noteKey){
        return Integer.parseInt(noteKey.split("\\.")[2]);

    }
    public static Runnable noteDetector = new Runnable() {
        @Override
        public void run() {
            System.out.println("PoolExecuted");
            isNotificationEmpty();
            startNote();
        }
    };
    public static boolean isEmpty(){return notifications.isEmpty();}
    private static void isNotificationEmpty () {
        boolean emptyNote= notifications.isEmpty();
        System.out.println("IsNoteEmtyStarted");
        while(emptyNote){
            emptyNote = notifications.isEmpty();
            try {
                Thread.sleep(1);
            }catch (InterruptedException e){}

        }
        System.out.println("EmptyIsOver");
        isNotificationNow();
    }
    private static void isNotificationNow () {
        System.out.println("isNoteNowStarted");
        boolean itis = notifications.get(notifications.firstKey()).withNano(0).equals(LocalDateTime.now().withNano(0));
        while (itis != true){
            itis = notifications.get(notifications.firstKey()).withNano(0).equals(LocalDateTime.now().withNano(0));
            try {
                Thread.sleep(1);
            }catch (InterruptedException e){}
        }
    }
    //EDITED FOR TREEMAP

    private static String wichNotificationNow(){
        int hash = Plan.getNotificationPLanKey(notifications.firstKey());
        return plans.get(hash).getHead();
    }
    public static void startNote(){
        System.out.println("startNoteHere");
        if((getNotificationType(notifications.firstKey())) == 1){

            System.out.println("Notification!!" + wichNotificationNow());
            trayIcon.displayMessage("Notify!",wichNotificationNow(), MessageType.INFO);
            notifications.remove(notifications.firstKey());
            ControllerClass.updateFilteredNoteList();
            pool.execute(noteDetector);

            }
        else if((getNotificationType(notifications.firstKey())) == 2){
            System.out.println("Alarm!!!" + wichNotificationNow());
            Main.isAlertNow=true;
            Main.alertHeader=wichNotificationNow();
            notifications.remove(notifications.firstKey());
            ControllerClass.updateFilteredNoteList();
            pool.execute(noteDetector);

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
            catch (Exception err){System.err.println(err); }}
    }



    public static ArrayList<String> filterNotes(int planKey){
        ArrayList<String> notes = new ArrayList<String>();
        for(String noteKey : notifications.keySet()){
            if(getNotificationPLanKey(noteKey)==planKey){
                notes.add(noteKey);
            }
        }
        return notes;
    }
//==============================================================================================================================================================================================================================\
    //\Notifications\
//==============================================================================================================================================================================================================================\


//==============================================================================================================================================================================================================================\
    //Comments
//==============================================================================================================================================================================================================================\
    //НЕ ЗАБЫТЬ ПРО shutdown!!!!!!!!!!!!!!!
//Сделать так чтобы весь TreeSet улетал в пул, а при добавлении новый элемент просто добавляется в пул. Результат выводится по готовности. P.S. отказался от такой реализации в пользу отслеживания первого элементра в TreeSet
// При этом .get() должен забирать результат у первого завершенного потока.
    //Статичный пул и TreeSet, позволют значительно снизить нагрузку на процессо при создании параллельных планов с оповещениями в каждом.
    //Нагрузка при старой реализации равна 15% на каждый новый план. При новой: 10-12% на всё!!!!!

    //-------------------------------------------
    // ПОФИКСИТЬ!!! update: пофикшено
    //--Не добавляется первое оповещение которое входит в промежуток
    //--Добавляется лишнее оповещение в конце, которое выходит из промежутка
    //ФИКС (возможный): сделать добавление оповещения в наале цикла while, а не в конце
    //Хорошенько протестить метод!!! Особенно для дней!!!!!!!!!!!
    //-----------------------------------------------
    /*Первая часть автоматического раскидывания уведомлений по плану. Пользователь выбирает через какие промежутки времени будут всплывать уведомления в течении плана
     * и метод автоматически разбивает план на эти промежутки.*/


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


    /* Метод isNotificationNow не прекращается пока время близжайшего уведомления/будильника(first()) не совпадет с временем системы.
     * Этот метод помещен в отдельный поток для асинхронного выполнения в фоне. Как только метод выходит из петли, первая запись в TreeSet
     * стирается
     * т.к. метод каждый раз обращается к notification.first, то при добавлении оповещения, которое должно сработать раньше, метод будет
     * обрабатывать именно его(т.к. notification.first() дает самое близкое к текущему времени оповещение) */




}



