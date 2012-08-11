package uk.co.robhemsley.penpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/*
 * PenPaint
 * 
 * 	Example application providing a canvas drawing app for use with a 
 *  hacked Crayola ColorStudio HD pen. The applicatio is based around the 
 *  Android example touch screen drawing application. 
 *  
 *  robhemsley.co.uk - Crayola Pen Hack Tutorial
 * 
 * 
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class FingerPaint extends GraphicsActivity implements ColourPickerDialog.OnColorChangedListener {
    private Paint mPaint;
    private Paint mPaintTmp;
    
    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int ERASE_MENU_ID = Menu.FIRST + 1;
    private static long tollerance = 0;
    private static long binaryTrue = 0;
    private static long binaryFalse = 0;
    
    private int state = 0;
    private int touchState = 0;
    private TimeoutWatcher timeout;
    private PaintView paintView;
    
    private String out = "";	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paintView = new PaintView(this);
        setContentView(paintView);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        
        mPaintTmp = new Paint();
        mPaintTmp.setAntiAlias(true);
        mPaintTmp.setDither(true);
        mPaintTmp.setColor(Color.BLACK);
        mPaintTmp.setStyle(Style.STROKE);
        mPaintTmp.setStrokeJoin(Paint.Join.ROUND);
        mPaintTmp.setStrokeCap(Paint.Cap.ROUND);
        mPaintTmp.setStrokeWidth(12);
        mPaintTmp.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
        
		tollerance = this.getResources().getInteger(R.integer.tap_tollerance);
		binaryTrue = this.getResources().getInteger(R.integer.binary_true);
		binaryFalse = this.getResources().getInteger(R.integer.binary_false);
        timeout = new TimeoutWatcher(300 , paintView);
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, COLOR_MENU_ID, 0, "Colour").setShortcut('3', 'c');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColourPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    public class PaintView extends View {
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        public PaintView(Context c) {
            super(c);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFAAAAAA);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaintTmp);
        }

        private void touch_start(float x, float y) {
        	timeout.start();
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
    		invalidate();
        }
        
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
    		invalidate();
        }
        
        public void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
            
            invalidate();
            state = 0;
        }
        
        public void setColour(int r, int g, int b){
   			mPaint.setColor(Color.rgb(r, g, b));
   			Log.i("- DataListener", "Colour Change");
   			Toast msg = Toast.makeText(this.getContext(), "Colour Changed", Toast.LENGTH_LONG);
   			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
   			msg.show();
   			
   			mPath.reset();
   			timeout.cancel();
   			state = 0;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            timeout.reset();

			Log.i("FingerPaint - onTouchEvent", "Action: "+event.getAction());
			
        	switch(event.getAction()){
	            case MotionEvent.ACTION_DOWN:
	            	if (state == 0){
	            		touch_start(x, y);
	            		state = 1;
	            	}
	                break;
	            
	            case MotionEvent.ACTION_MOVE:
	                touch_move(x, y);
	                break;
                
				case MotionEvent.ACTION_UP:
					if (touchState == 2 || touchState == 0){
						long tmp = event.getEventTime() - event.getDownTime();
						Log.i("FingerPaint - onTouchEvent", "Touch down For "+tmp+ " ms");
					
					
						if(tmp-tollerance <= binaryTrue && tmp+tollerance >= binaryTrue){
							Log.i("FingerPaint - onTouchEvent", "Binary: True");
							out += "1";
						}else{
							if(tmp-tollerance <= binaryFalse && tmp+tollerance >= binaryFalse){
								Log.i("FingerPaint - onTouchEvent", "Binary: False");
								out += "0";
							}else{
								touchState = 0;
								out = "";
								Log.i("FingerPaint - onTouchEvent", "Binary: Reset");			       			
							}
						}
		       		
						
			       		if (out.length() == 8){
			       			byte touchByte = new Byte(out);
			       			Log.i("FingerPaint - onTouchEvent", "Byte: "+out+" - "+touchByte);

			            	switch(touchByte){
				            	case 1:
				            		setColour(255, 0,0);
				            		break;
				            	
				            	case 2:
				            		setColour(0, 0, 255);
				            		break;
				            		
				            	case 3:
				            		setColour(0, 255, 0);
				            		break;
	
			            	}
			       			out = "";
			       		}
					}
					break;
        		}
        	
        		touchState = event.getAction();        
        		return true;
        	}
    }
}