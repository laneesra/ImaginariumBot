import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Chat {
    private static FileOutputStream fos;
    private Log logging;
    private HashMap<Long, Player> players;
    private Long currentPlayer;

    static {
        try {
            fos = new FileOutputStream(Files.playersDB, false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    Chat(Log logging){
        this.players = new HashMap<>();
        this.logging = logging;
    }

    public void addPlayer(Player p){
        if (!players.isEmpty()) {

        }
        players.put(p.getUserId(), p);
        JSONObject player = new JSONObject();
        player.put("userId", p.getUserId());
        player.put("firstName", p.getFirstName());
        player.put("lastName", p.getLastName());
        player.put("username", p.getUsername());
        //TODO добавить рандомный выбор картинок
        player.put("pictures", new int[6]);
        player.put("score", 0);

        try {
            writeJSONtoFile(player);
            logging.log(String.valueOf(p.getUserId()),"Action", "New player appeared");
        } catch (IOException e) {
            logging.log(String.valueOf(p.getUserId()),"Exception", e.getMessage());
        }
    }


    public boolean isEmpty(){
        return players.isEmpty();
    }


    public HashMap<Long, Player> getPlayers(){
        return players;
    }

    /*
     * Метод проверяет наличие игрока в игре.
     */
    public boolean findPlayer(long userId) {
        if (players.isEmpty()) {
            logging.log(String.valueOf(userId), "Info", "There is no players in game");
            return false;
        }
        return players.containsKey(userId);
    }

    public void writeJSONtoFile(JSONObject jsonObject) throws IOException{
        byte[] buffer = jsonObject.toString().getBytes();
        fos.write(buffer, 0, buffer.length);
        fos.write("\n".getBytes());
    }
}
