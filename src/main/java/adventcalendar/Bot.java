package adventcalendar;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.StickerSet;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    Dotenv d = Dotenv.load();
    @Override
    public String getBotUsername() {return d.get("botName");}

    @Override
    public String getBotToken() {return d.get("botToken");}

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
    }

    /**
     * Send the 'what' text to 'who' chat_id
     * @param who chat_id where the text will be sent
     * @param what text to be sent
     */
    public void sendText(String who, String what){
        if (who.equals("test")) who = d.get("testChatID");
        else if (who.equals("prod")) who = d.get("prodChatID");
        //Request to send the text
        SendMessage sm = SendMessage.builder()
                .chatId(who)
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a random sticker from the sticker sets available in stickerSets ArrayList
     * @param who chat_id where the sticker will be sent
     * @throws TelegramApiRequestException
     */
    public void sendRandomStickers(String who) throws TelegramApiRequestException {
        if (who.equals("test")) who = d.get("testChatID");
        else if (who.equals("prod")) who = d.get("prodChatID");
        //sticker sets arraylist
        ArrayList<String> stickerSets = new ArrayList<>();
        stickerSets.add("PotterPig");
        stickerSets.add("Lord_Voldemort");
        stickerSets.add("BoyWhoLived");
        stickerSets.add("ElfDobby");

        //Random number to choose the sticker set
        int maxArrayStickersSet = (stickerSets.size())-1;
        int random_intArrayStickersSet = (int)Math.floor(Math.random()*(maxArrayStickersSet-0+1));

        GetStickerSet gss = GetStickerSet
                .builder()
                .name(stickerSets.get(random_intArrayStickersSet))
                .build();
        try {
            //Request to get the sticker set info
            StickerSet ss = execute(gss);

            //Random number to choose the sticker
            int max = (ss.getStickers().size())-1;
            int random_int = (int)Math.floor(Math.random()*(max-0+1));

            //InputFile object creation by chosen sticker file_id
            InputFile sticker = new InputFile();
            sticker.setMedia(ss.getStickers().get(random_int).getFileId());

            //Request to send the chosen sticker
            SendSticker s = SendSticker.builder()
                    .chatId(who)
                    .sticker(sticker)
                    .build();

            try {
                execute(s);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
