package models;

public class UserStats {
    private String username;
    private int wins;
    private int losses;
    private int elo;

    public UserStats() {
    }

    public UserStats(String username, int wins, int losses, int elo) {
        this.username = username;
        this.wins = wins;
        this.losses = losses;
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
