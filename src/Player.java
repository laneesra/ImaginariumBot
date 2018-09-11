public class Player {
    private String firstName;
    private String lastName;
    private String username;
    private long userId;
    private int[] pictures;
    private int score;
    private String color;

    Player(String firstName, String lastName, String username, long userId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userId = userId;
        this.pictures = new int[6];
        this.score = 0;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public long getUserId() {
        return userId;
    }

    public String getColor() {
        return color;
    }
}
