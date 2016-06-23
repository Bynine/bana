package main;

class Timer{
	int clock = 0;
	private final int stop;
	Timer(int stop){ this.stop = stop; }
	void set(int deltaTime){ clock = deltaTime + stop; }
	void set(int time, int deltaTime){ clock = deltaTime + time; }
	void reset(){ clock = 0; }
	boolean stopped(int deltaTime){ return clock <= deltaTime; }
	boolean timed(int deltaTime){ return Math.abs(clock - deltaTime) == 1; }
}