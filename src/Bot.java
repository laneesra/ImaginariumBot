import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.*;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.awt.*;
import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

public class Bot extends TelegramLongPollingBot {
    private static Log logging;
    private static String username;
    private static String token;
    private Chat chat;

    static {
        username = new String();
        token = new String();
    }

    public Bot(Log logging){
        this.logging = logging;
        this.chat = new Chat(logging);
        try {
            FileInputStream fs = new FileInputStream(Files.dataFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            token = br.readLine();
            username = br.readLine();
            logging.log("\nusername: " + username +"\ntoken: " + token);
        }
        catch (IOException e) {
            logging.log("ERROR: " + e.getMessage());
        }
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
                    String firstName = update.getMessage().getChat().getFirstName();
                    String lastName = update.getMessage().getChat().getLastName();
                    String username = update.getMessage().getChat().getUserName();
                    long userId = update.getMessage().getChat().getId();
                    if (!chat.findPlayer(userId)) {
                        Player player = new Player(firstName, lastName, username, userId);

                        if (chat.isEmpty()){
                            answer = "Hello, my friend! Welcome to the Imaginarium game :)\n" +
                                    "You're the first player.";
                        } else {
                            String joinedPlayers = "";
                            for (Player e : chat.getPlayers().values()){
                                joinedPlayers += e.getUsername() + " ";
                            }
                            if (chat.getPlayers().size() == 1) {
                                answer = "Hello, my friend! Welcome to the Imaginarium game :)\n" +
                                        joinedPlayers + "is in game.";
                            } else {
                                answer = "Hello, my friend! Welcome to the Imaginarium game :)\n" +
                                        joinedPlayers + "are in game.";
                            }
                        }

                        chat.addPlayer(player);
                        sendMsg(update.getMessage().getChatId().toString(), answer);

                        answer = "Please, choose your color.";
                        SendMessage sendMessage = new SendMessage().setChatId(chatId).setText(answer);
                        logging.log(chatId, "Answer", answer);

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
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        answer = "You can't join to another game";
                        sendMsg(update.getMessage().getChatId().toString(), answer);
                    }
                    break;

                case "/help":
                    answer = "You can control me by sending these commands:\n\n" +
                            "/start : Start to play\n" +
                            "/rules : Get the game rules\n" +
                            "/";
                    sendMsg(update.getMessage().getChatId().toString(), answer);
                    break;

                case "/rules":
                    answer = "Follow the link to get rules -> http://imaginarium-game.ru/rules/";
                    sendMsg(update.getMessage().getChatId().toString(), answer);
                    break;

                default:
                    answer = "Type '/help' to get information";
                    sendMsg(update.getMessage().getChatId().toString(), answer);
            }

        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            if (List.of("red", "blue", "green", "yellow").contains(callData)) {
                long messageId = update.getCallbackQuery().getMessage().getMessageId();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                String answer = "Your color is " + callData;
                EditMessageText newMessage = new EditMessageText()
                        .setChatId(chatId)
                        .setMessageId(toIntExact(messageId))
                        .setText(answer);
                try {
                    execute(newMessage);
                    //TODO player.setColor(callData);
                    logging.log(String.valueOf(chatId), "Action", "Player has chosen the color: " + callData);
                    logging.log(String.valueOf(chatId), "Answer", answer);
                } catch (TelegramApiException e) {
                    logging.log(String.valueOf(chatId), "Exception", e.toString());
                    logging.log(String.valueOf(chatId), "Stack trace", e.getStackTrace().toString());
                }
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
        return username;
    }


    /*
     * Метод возвращает token бота для связи с сервером Telegram
     */
    @Override
    public String getBotToken() {
        return token;
    }
}