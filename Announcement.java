public class Announcement {
    private String text;
    private int framesLeft;
    private int numOfFrames;
    private Boolean pausesGame = true;

    public Announcement(String text, int numOfFrames) {
        if (numOfFrames < 0) {
            throw new IllegalArgumentException("Number of frames cannot be negative.");
        }
        this.text = text;
        this.framesLeft = numOfFrames;
        this.numOfFrames = numOfFrames;
    }

    public Announcement(String text, int numOfFrames, Boolean pausesGame) {
        if (numOfFrames < 0) {
            throw new IllegalArgumentException("Number of frames cannot be negative.");
        }
        this.text = text;
        this.framesLeft = numOfFrames;
        this.numOfFrames = numOfFrames;
        this.pausesGame = pausesGame;
    }

    public String getText() { return text; }

    public void decrementFramesLeft() { framesLeft--; }
    public Boolean isExpired() { return framesLeft <= 0; }
    public double getProgress() { return 1.0 - (double)framesLeft/numOfFrames; }

    public Boolean shouldPauseGame() { return pausesGame; }
}
