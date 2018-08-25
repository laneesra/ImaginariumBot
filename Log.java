/* tags:
    - Message
    - Answer
    - Exception
 */

import java.util.Date;
import java.io.*;

public class Log {
    private final static String logFile = "C:/Users/mvideo/IdeaProjects/ImaginariumTheGame/logs.txt";
    private static FileOutputStream fos;
    private boolean mode; //false - производить вывод всей информации в консоль (info); true - выводить значимые логи в файл (debug);

    static {
        try {
            fos = new FileOutputStream(logFile, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Log() {
        mode = false;
    }


    /*
     * Метод установки режима отладки в файл.
     */
    public void setDebugMode() {
        this.mode = true;
        System.out.println("DEBUG");
    }


    /*
     * Метод установки режима отладки в консоль.
     */
    public void setInfoMode() {
        this.mode = false;
        System.out.println("INFO");
    }


    /*
     * Метод вывода общих логов.
     */
    public void log(String msg) {
        String date = new Date().toString();
        String logMsg = date + " " + msg;
        System.out.println(logMsg);
        if (mode) {
            try {
                byte[] buffer = logMsg.getBytes();
                fos.write(buffer, 0, buffer.length);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /*
     * Метод вывода пользовательских логов.
     */
    public void log(String chatId, String tag, String msg) {
        String date = new Date().toString();
        String logMsg = date + " chatId: " + chatId + " " + tag + ": " + msg;
        System.out.println(logMsg);
        if (mode) {
            if (tag.equals("Exception") || tag.equals("Stack trace")) {
                try {
                    byte[] buffer = logMsg.getBytes();
                    fos.write(buffer, 0, buffer.length);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}