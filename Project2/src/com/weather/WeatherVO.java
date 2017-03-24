package com.weather;



public class WeatherVO {
	   public String DATE; 
	   public double TMAX; 
	   public double TMIN;
	  
		  

				public WeatherVO(){
					
				}
				public WeatherVO(String DATE){
					this.DATE=DATE;
				}

				public WeatherVO(String DATE, double TMAX, double TMIN){
					this.DATE=DATE;
					this.TMAX=TMAX;
					this.TMIN=TMIN;
				}
		   

}
