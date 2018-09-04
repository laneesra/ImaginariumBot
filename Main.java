import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Main {
    private static Log logging;
    private static String botToken = "556642612:AAFLcDBfJ6eivAcpcCdC6SMyV0w35o-GhXk";
    private static String botUsername = "ImaginariumTheGameBot";
    private static String PROXY_HOST = "78.66.102.104" /* proxy host */;
    private static Integer PROXY_PORT = 3128 /* proxy port */;

    public static void main(String[] args) {
        logging = new Log();
        // set logging mode
        //logging.setDebugMode();
        ApiContextInitializer.init();
        logging.log("API Context is initialized");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot(logging));
            logging.log("Telegram bot is registered");
        } catch (TelegramApiRequestException e) {
            logging.log("ERROR: Telegram bot can't be registered");
            e.printStackTrace();
        }
    }
}
