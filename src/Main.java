import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Main {
    private static Log logging;

    public static void main(String[] args) {
        logging = new Log();
        // set logging mode
        logging.setDebugMode();
        ApiContextInitializer.init();
        logging.log("API Context is initialized");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot(logging));
            logging.log("Telegram bot is registered");
        } catch (TelegramApiRequestException e) {
            logging.log("ERROR: Telegram bot can't be registered");
            logging.log("TelegramApiRequestException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable e) {
            logging.log("ERROR: Telegram bot can't be registered");
            logging.log(e.toString() + e.getMessage());
            e.printStackTrace();
        }
    }
}