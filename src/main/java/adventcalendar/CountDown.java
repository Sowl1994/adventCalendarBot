package adventcalendar;

import io.github.cdimascio.dotenv.Dotenv;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CountDown implements Job {
    Dotenv dt = Dotenv.load();
    String environmentEnv = dt.get("environment");
    String chatID = "";



    public long getDaysRemaining(){
        return Math.abs(ChronoUnit.DAYS.between(getTargetDay(), getToday()));
    }

    public LocalDate getToday(){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        return LocalDate.parse(formatter.format(LocalDateTime.now()), formatter);
    }

    public LocalDate getTargetDay(){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        return LocalDate.parse("01 12 2022", formatter);
    }

    public String getTargetDayFormatted(){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(getTargetDay());
    }
    public String getTodayDayFormattedLog(){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(LocalDateTime.now());
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(environmentEnv.equals("test")) chatID = dt.get("testChatID");
        else if(environmentEnv.equals("prod")) chatID = dt.get("prodChatID");

        System.out.println(getTodayDayFormattedLog()+"-> Dia actual: " + getToday());
        System.out.println(getTodayDayFormattedLog()+"-> Días hasta el "+getTargetDayFormatted()+": " + getDaysRemaining());
        Bot b = new Bot();
        b.sendText(chatID,"¡¡Recordatorio!! Días hasta el "+getTargetDayFormatted()+": " + getDaysRemaining());
        try {
            b.sendRandomStickers(chatID);
        } catch (TelegramApiRequestException e) {
            System.out.println(getTodayDayFormattedLog()+"-> ERROR");
            System.out.println(getTodayDayFormattedLog()+"-> "+e);
            throw new RuntimeException(e);
        }
    }
}
