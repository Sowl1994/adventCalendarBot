package adventcalendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CountDown implements Job {
    Long prodID = <prodId>;
    Long testID = <testId>;

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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Dia actual: " + getToday());
        System.out.println("Días hasta el "+getTargetDayFormatted()+": " + getDaysRemaining());
        Bot b = new Bot();
        b.sendText(testID,"¡¡Recordatorio!! Días hasta el "+getTargetDayFormatted()+": " + getDaysRemaining());
        try {
            b.sendRandomStickers(testID);
        } catch (TelegramApiRequestException e) {
            System.out.println("ERROR");
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
