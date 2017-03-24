package com.weather;


import java.util.*;
import java.util.Date;

import javax.ws.rs.core.Response;

import java.sql.*;
import java.text.SimpleDateFormat;

public class WeatherDao {
	
	private Connection con;
	
	public WeatherDao() throws Exception{
		MysqlCon db= new MysqlCon();
		con=db.getConnection();
	}
	
	public ArrayList<Weather> getWeatherDate() throws Exception
	{
		ArrayList<Weather> weatherDatesList=new ArrayList<Weather>();
		try
		{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from cincyweather");
			while(rs.next())
			{
				Weather w=new Weather();
				Date DATE = rs.getDate(1);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String format = formatter.format(DATE);
				w.DATE=format;
				weatherDatesList.add(w);
			}
			return weatherDatesList;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public WeatherVO getWeatherDetails(String DATE) throws Exception
	{
		
		try
		{   
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date d=formatter.parse(DATE);
		    WeatherVO wv=new WeatherVO();
		    PreparedStatement ps=con.prepareStatement("select * from cincyweather where DATE = ?");
			ps.setDate(1,new java.sql.Date(d.getTime()));
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				
				wv.DATE=formatter.format(rs.getDate(1));
				wv.TMAX=rs.getDouble(2);
				wv.TMIN=rs.getDouble(3);
				
			}
			
			return wv;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public String updateWeather(WeatherVO w) throws Exception{
		try
		{
			String DATE=w.DATE;
			Double TMAX=w.TMAX;
			Double TMIN=w.TMIN;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date d=formatter.parse(DATE);
			PreparedStatement ps=con.prepareStatement("insert into cincyweather(DATE,TMAX,TMIN) values (?,?,?)");
			ps.setDate(1,new java.sql.Date(d.getTime()));
			ps.setDouble(2,TMAX);
			ps.setDouble(3,TMIN);
			ps.executeUpdate();
			return DATE;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	
	
	public String deleteWeatherDetails(String DATE) throws Exception{
	try
	{   
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date d=formatter.parse(DATE);
		WeatherVO w= getWeatherDetails(DATE);
		if(w.DATE==null)
			return "BAD REQUEST";
		else
		{
		PreparedStatement ps=con.prepareStatement("delete from cincyweather where DATE = ?");
		ps.setDate(1,new java.sql.Date(d.getTime()));
		ps.executeUpdate();
		return "SUCCESSFULLY DELETED";
	
		}
	}
	catch(Exception e)
	{
		throw e;
	}
	
	}
	
	public WeatherVO forecaster(String DATE) throws Exception{
		System.out.println(DATE);
		String d1="";
		String d2="";
		String d3="";
		String d4="";
		WeatherDao wd=new WeatherDao();
		WeatherVO w= new WeatherVO();
		String mmdd=DATE.substring(4,8);
		if(DATE.compareTo("20170209")>0){
			d1="2016"+mmdd;
			d2="2015"+mmdd;
			d3="2014"+mmdd;
			d4="2013"+mmdd;
			WeatherVO w1= wd.getWeatherDetails(d1);
			WeatherVO w2= wd.getWeatherDetails(d2);
			WeatherVO w3= wd.getWeatherDetails(d3);
			WeatherVO w4= wd.getWeatherDetails(d4);
			
			w.DATE=DATE;
			w.TMAX=(w1.TMAX+w2.TMAX+w3.TMAX+w4.TMAX)/4;
			w.TMIN=(w1.TMIN+w2.TMIN+w3.TMIN+w4.TMIN)/4;
		}else
		{
			w=wd.getWeatherDetails(DATE);
			
		}
	
		return w;
		
	}
	
}
