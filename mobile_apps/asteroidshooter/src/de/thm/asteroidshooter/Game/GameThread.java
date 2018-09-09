package de.thm.asteroidshooter.Game;

import java.text.DecimalFormat;

import de.thm.asteroidshooter.Global.GlobalVars;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;


public class GameThread extends Thread {

	private static final String TAG = GameThread.class.getSimpleName();

	private final static int MAX_FPS = 30;						//max fps the game runs, also update count
	private final static int MAX_FRAME_SKIPS = 5;				//max frame skips
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;		//the frame period, time for one frame

	private DecimalFormat df = new DecimalFormat("0.##");		//decimal format for fps
	private final static int STAT_INTERVAL = 1000;				//interval for statistik
	private final static int FPS_HISTORY_NR = 10;				//how many fps to keep in history
	private long lastStatusStore = 0;							//store status
	private long statusIntervalTimer = 0l;						//intervall time
	private long totalFramesSkipped	= 0l;						//skipped frames
	private long framesSkippedPerStatCycle = 0l;				//frames skipped stat

	private int frameCountPerStatCycle = 0;						//frames per cycle
	private long totalFrameCount = 0l;							//total frames
	private double fpsStore[];									//stored fps
	private long statsCount = 0;								//counter
	private double averageFps = 0.0;							//avg fps

	private SurfaceHolder surfaceHolder;						//surface holder
	private GameView gamePanel;									//game panel, view
	private GlobalVars glob = GlobalVars.getInstance();			//singelton pattern
	
	private long refreshTime;									//refresh time
	private long StartCounter;									//counter when game start
	private int counter = 3;									//start counter
	
	private boolean running;									//thread running
	private boolean runOnce;									//thread run once
	private boolean paused;										//thread is pasued

	//start and stop thread
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	//pase thread
	public void setPause(boolean pause){
		paused = pause;
	}

	//thread constructor 
	public GameThread(SurfaceHolder surfaceHolder, GameView gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		StartCounter  = System.currentTimeMillis();
		runOnce = true;
		paused = true;
	}

	@Override
	public void run() {
		Canvas canvas;
		initTimingElements();

		long beginTime;
		long timeDiff;
		int sleepTime = 0;
		int framesSkipped;
		
		refreshTime = System.currentTimeMillis();

		while (running) {
			//Log.v("grrr", "grrr");
			if(System.currentTimeMillis() - refreshTime > FRAME_PERIOD && (runOnce == true || paused == false)){
				refreshTime = System.currentTimeMillis();
				canvas = null;
				try {
					canvas = this.surfaceHolder.lockCanvas();
					//only do when canvas exists
					if(canvas != null){
						synchronized (surfaceHolder) {
							
							//for counter
							if(counter <= 0 || runOnce == true){
								beginTime = System.currentTimeMillis();
								framesSkipped = 0;
								
								//important methods to update and draw everything
								this.gamePanel.update();
								this.gamePanel.render(canvas);
								
								//only draw counter on first run
								//if(runOnce == true)this.gamePanel.drawCounter(canvas, 3);
		
								//time elements to calculate frame skips or sleep times
								timeDiff = System.currentTimeMillis() - beginTime;
								sleepTime = (int)(FRAME_PERIOD - timeDiff);
		
								//if we were slower we need to skip frames, only 5 max, just updates the game panel
								while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
									//we need to catch up
									this.gamePanel.update(); 		//update without rendering
									sleepTime += FRAME_PERIOD;		//add frame period to check if in next frame
									framesSkipped++;
								}
	
								//debug for fps display and frame skipping message
								if(GlobalVars.DEBUG == true){
									//if (framesSkipped > 0) Log.d(TAG, "Skipped:" + framesSkipped);
									framesSkippedPerStatCycle += framesSkipped;
									storeStats();
								}
								
								runOnce = false;
							}
							//the counter
							else {
								if(System.currentTimeMillis() - StartCounter > GlobalVars.COUNTER_TIME){
									StartCounter  = System.currentTimeMillis();
									counter--;
								}
								this.gamePanel.render(canvas);
								this.gamePanel.drawCounter(canvas, counter);
								
							}
						}
					}
				} finally {if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);}
			}
			if(paused == true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * The statistics - it is called every cycle, it checks if time since last
	 * store is greater than the statistics gathering period (1 sec) and if so
	 * it calculates the FPS for the last period and stores it.
	 *
	 *  It tracks the number of frames per period. The number of frames since
	 *  the start of the period are summed up and the calculation takes part
	 *  only if the next period and the frame count is reset to 0.
	 */
	private void storeStats() {
		frameCountPerStatCycle++;
		totalFrameCount++;

		//check the actual time
		statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

		if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
			//calculate the actual frames pers status check interval
			double actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));
			//stores the latest fps in the array
			fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;
			//increase the number of times statistics was calculated
			statsCount++;
			double totalFps = 0.0;
			//sum up the stored fps values
			for (int i = 0; i < FPS_HISTORY_NR; i++) {
				totalFps += fpsStore[i];
			}

			//obtain the average
			if (statsCount < FPS_HISTORY_NR) {
				//in case of the first 10 triggers
				averageFps = totalFps / statsCount;
			} else {
				averageFps = totalFps / FPS_HISTORY_NR;
			}
			//saving the number of total frames skipped
			totalFramesSkipped += framesSkippedPerStatCycle;
			//resetting the counters after a status record (1 sec)
			framesSkippedPerStatCycle = 0;
			statusIntervalTimer = 0;
			frameCountPerStatCycle = 0;

			statusIntervalTimer = System.currentTimeMillis();
			lastStatusStore = statusIntervalTimer;
			gamePanel.setAvgFps("FPS: " + df.format(averageFps));
		}
	}

	//initialise timing elements
	private void initTimingElements() {
		fpsStore = new double[FPS_HISTORY_NR];
		for (int i = 0; i < FPS_HISTORY_NR; i++) {
			fpsStore[i] = 0.0;
		}
	}

}
