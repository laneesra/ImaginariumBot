import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.*;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class Bot extends TelegramLongPollingBot {
    private static Log logging;

    public Bot(Log logging){
        this.logging = logging;
    }


    /*
     * Метод для приема сообщений.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            String answer;
            String chatId = update.getMessage().getChatId().toString();
            logging.log(chatId, "Message", message);

            switch (message) {
                case "/start":
                    answer = "Hello, my friend! Welcome to the Imaginarium game :)\n" +
                            "Please, choose your color.";
                    SendMessage sendMessage = new SendMessage()
                            .setChatId(chatId)
                            .setText(answer);
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("Red").setCallbackData("red"));
                    rowInline.add(new InlineKeyboardButton().setText("Blue").setCallbackData("blue"));
                    rowInline.add(new InlineKeyboardButton().setText("Green").setCallbackData("green"));
                    rowInline.add(new InlineKeyboardButton().setText("Yellow").setCallbackData("yellow"));
                    rowsInline.add(rowInline);
                    inlineKeyboardMarkup.setKeyboard(rowsInline);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "/help":
                    answer = "You can control me by sending these commands:\n\n" +
                            "/rules : Get the game rules\n" +
                            "/";
                    sendMsg(update.getMessage().getChatId().toString(), answer);
                    break;

                case "/rules":

                default:
                    answer = "Type '/help' to get information";
                    sendMsg(update.getMessage().getChatId().toString(), answer);
            }

        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String answer = "Your color is " + callData;
            EditMessageText newMessage = new EditMessageText()
                    .setChatId(chatId)
                    .setMessageId(toIntExact(messageId))
                    .setText(answer);
            try {
                execute(newMessage);
                logging.log(String.valueOf(chatId), "Player has chosen the color: ", callData);
            } catch (TelegramApiException e) {
                logging.log(String.valueOf(chatId), "Exception", e.toString());
                logging.log(String.valueOf(chatId), "Stack trace", e.getStackTrace().toString());
            }
        }
    }


    /*
     * Метод для настройки сообщения и его отправки.
     */
    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            sendMessage(sendMessage);
            logging.log(chatId, "Answer", s);
        } catch (TelegramApiException e) {
            logging.log(chatId, "Exception", e.toString());
            logging.log(chatId, "Stack trace", e.getStackTrace().toString());
        }
    }


    /*
     * Метод возвращает имя бота, указанное при регистрации.
     */
    @Override
    public String getBotUsername() {
        return "ImaginariumTheGameBot";
    }


    /*
     * Метод возвращает token бота для связи с сервером Telegram
     */
    @Override
    public String getBotToken() {
        return "556642612:AAFLcDBfJ6eivAcpcCdC6SMyV0w35o-GhXk";
    }
}