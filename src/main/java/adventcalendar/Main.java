package adventcalendar;

import io.github.cdimascio.dotenv.Dotenv;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try{
            Dotenv d = Dotenv.load();
            String environment = d.get("environment");
            String scheduleCron = "";
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());

            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(CountDown.class)
                .withIdentity("myJob","g1")
                .build();

            if (environment.equals("test")) /*TEST -> Every second*/scheduleCron = "* * * * * ?";
            else if (environment.equals("prod")) /*PROD -> 9AM & 9PM*/scheduleCron = "0 0 9,21 * * ?";
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger","g1")
                    .withSchedule(CronScheduleBuilder.cronSchedule(scheduleCron))
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