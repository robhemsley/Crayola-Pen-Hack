package uk.co.robhemsley.penpaint;

/**
 * TimeoutWatcher - Class
 * Countdown timer watching for touch even timeouts
 * 
 * @author roberthemsley
 * @version 0.1
 * 
 * robhemsley.co.uk
 * Copyright (c) 2012 Rob Hemsley
 */

import uk.co.robhemsley.penpaint.FingerPaint.PaintView;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

public class TimeoutWatcher extends CountDownTimer{
	private PaintView fp;

	public TimeoutWatcher(long timeout, PaintView fp) {
		super(timeout, 1);
		this.fp = fp;
	}
	
	public TimeoutWatcher(Context cont, long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onFinish() {
		Log.i("TimeoutWatcher - onFinish", "Timeout Occurred");
		this.fp.touch_up();
	}

	@Override
	public void onTick(long arg0) {
		
	}
	
	public void reset(){
		this.cancel();
		this.start();
	}
}
