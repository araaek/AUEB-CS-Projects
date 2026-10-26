/*
	Name: 
	Student Number: 
*/

public class Clock {
    protected int hour;
    protected int min;
    protected int sec;

    public Clock(int hour, int min, int sec) {
        setHour(hour);
        setMin(min);
        setSec(sec);
    }

    public void setHour(int h) {
        hour = h;
    }

    public void setMin(int m) {
        min = m;
    }

    public void setSec(int s) {
        sec = s;
    }

    public void tick() {
        sec++;
        if (sec == 60) {
            sec = 0;
            min++;
            if (min == 60) {
                min = 0;
                hour++;
                if (hour == 24) {
                    hour = 0;
                }
            }
        }
    }

    public String toString() {
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }
}

