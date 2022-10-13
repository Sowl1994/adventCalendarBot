package adventcalendar;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try{
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());

            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(CountDown.class)
                .withIdentity("myJob","g1")
                .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger","g1")
                    /*PROD -> 9AM & 9PM*/.withSchedule(CronScheduleBuilder.cronSchedule("0 0 9,21 * * ?"))
                    /*TEST -> Every second*///.withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
                    .forJob("myJob","g1")
                    .build();

            scheduler.start();
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}