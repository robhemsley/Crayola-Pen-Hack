package uk.co.robhemsley.penpaint;

/**
 * TouchEvent - Class
 * Object representing a screen touch event
 * 
 * @author roberthemsley
 * @version 0.1
 * 
 * robhemsley.co.uk
 * Copyright (c) 2012 Rob Hemsley
 */

import uk.co.robhemsley.utils.Time;
import android.graphics.Point;
import android.view.MotionEvent;

public class TouchEvent {
	private float xVal;
	private float yVal;
	private float pressure;
	private String className;
	private String packageName;
	private long timeStamp;

	/*
	 * Class Constructor representing a new touch event.
	 * @param me The MotionEvent that has just be detected
	 * @param className A string representing the class name at point of call
	 * @param packageName A string representing the package name at point of call
	 */
	public TouchEvent(MotionEvent me, String className, String packageName){
		this.xVal = Math.abs(me.getX());
		this.yVal = Math.abs(me.getY());
		this.pressure = me.getPressure();
		this.className = className;
		this.packageName = packageName;
		this.timeStamp = Time.getTimeStamp();
	}
	
	/*
	 * Accessor method to get x coordinate of event
	 * @return The x coordinate of the event
	 */
	public int getX(){
		return (int) this.xVal;
	}
	
	/*
	 * Accessor method to get y coordinate of event
	 * @return The y coordinate of the event
	 */
	public int getY(){
		return (int) this.yVal;
	}
	
	/*
	 * Accessor method to get the point of touch
	 * @return The point of touch on the screen
	 */
	public Point getPoint(){
		return new Point(this.getX(), this.getY());
	}
	
	/*
	 * Accessor method to get the pressure
	 * @return The pressure applied to the screen
	 */
	public float getPressure(){
		return this.pressure;
	}

	/*
	 * Accessor method to get the class name
	 * @return The className where the event occurred
	 */
	public String getClassName(){
		return this.className;
	}
	
	/*
	 * Accessor method to get the package name
	 * @return The packageName where the event occurred
	 */
	public String getPackageName(){
		return this.packageName;
	}
	
	/*
	 * Accessor method to get the timestamp of event
	 * @return The timestamp of the event
	 */
	public long getTimeStamp(){
		return this.timeStamp;
	}
	
	/*
	 * Override of toString method
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return new StringBuilder()
	    .append("{TouchEvent:")
	    .append(" x=").append(this.getX())
	    .append(", y=").append(this.getY())
	    .append(", pressure=").append(this.getPressure())
	    .append(", className=").append(this.className)
	    .append(", packageName=").append(this.packageName)
	    .append(" timestamp=").append(this.getTimeStamp())
	    .append("}").toString();
	}
}
