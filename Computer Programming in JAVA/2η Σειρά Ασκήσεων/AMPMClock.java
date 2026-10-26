/*
	Name: 
	Student Number: 
*/

public class AMPMClock extends Clock {
    private boolean isAM;

    public AMPMClock(int hour, int min, int sec) {
        super(hour, min, sec);
        isAM = true;
    }

    public void setAMPM(boolean yes) {
        isAM = yes;
    }

    public String toString() {
        if (isAM) {
            if (hour == 0) {
                return String.format("12:%02d:%02d am", min, sec);
            } else if (hour < 12) {
                return String.format("%d:%02d:%02d am", hour, min, sec);
            } else if (hour == 12) {
                return String.format("12:%02d:%02d pm", min, sec);
            } else {
                return String.format("%d:%02d:%02d pm", hour - 12, min, sec);
            }
        } else {
            return super.toString();
        }
    }
}

