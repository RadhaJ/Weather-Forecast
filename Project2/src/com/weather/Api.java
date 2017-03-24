package com.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.owlike.genson.stream.JsonType;

@Path("/api")
public class Api {
	
@GET
@Path("/historical")
@Produces({MediaType.APPLICATION_JSON})
public ArrayList<Weather> getWeather() throws Exception 
{
	WeatherDao wd=new WeatherDao();
	return wd.getWeatherDate();
	
}

@GET
@Path("/historical/{DATE}")
@Produces({MediaType.APPLICATION_JSON})
public Response getWeatherTwo(@PathParam("DATE") String DATE) throws Exception{
	WeatherDao wd=new WeatherDao();
	WeatherVO w= wd.getWeatherDetails(DATE);
	
	if(w.DATE==null)
		return Response.status(Response.Status.NOT_FOUND).build();
	else
		return Response.status(Response.Status.OK).entity(w).build();
}

@POST 
@Path("/historical")  
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response postWeather(WeatherVO wv) throws Exception{ 
   WeatherDao wd = new WeatherDao(); 
   Weather w=new Weather();
   String d= wd.updateWeather(wv);
   w.DATE=wv.DATE;;
   return Response.status(Response.Status.CREATED).entity(w).build();
   
}

@DELETE 
@Path("/historical/{DATE}")
@Produces(MediaType.APPLICATION_JSON) 
public Response deleteWeather(@PathParam("DATE") String DATE) throws Exception{ 
	WeatherDao wd=new WeatherDao();
	String response= wd.deleteWeatherDetails(DATE);
	if(response=="BAD REQUEST")
		return Response.status(Response.Status.BAD_REQUEST).build();
	else
    return Response.status(Response.Status.NO_CONTENT).build();	
}

public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}




@GET
@Path("/forecast/{dat}")
@Produces(MediaType.APPLICATION_JSON)
public Response forcastClimate(@PathParam("dat") int dat) {
	System.out.println(" "+dat);
	try{
		List<WeatherVO> lt=new ArrayList<WeatherVO>();
		String temp = Integer.toString(dat);
		char c[]=temp.toCharArray();
		String dt=""+c[0]+""+c[1]+""+c[2]+""+c[3]+"-"+c[4]+""+c[5]+"-"+c[6]+""+c[7];
		java.sql.Date dd = java.sql.Date.valueOf( dt );
		for(int i=0;i<7;i++)
		{

			String urrl="https://api.darksky.net/forecast/595a9c497b5469ca586d3bf544cfaacd/39.1031182,-84.5120196,"+dd+"T12:00:00";
			JSONObject json=readJsonFromUrl(urrl);
			JSONObject jj=json.getJSONObject("daily");
			WeatherVO d=new WeatherVO();
			JSONArray jr4= jj.getJSONArray("data");
			JSONObject jo=jr4.getJSONObject(0);
			d.DATE=Integer.toString(dat);
	    	double tmax=jo.getLong("temperatureMax");
			d.TMAX=tmax;
	    	double tmin=jo.getLong("temperatureMin");
	    	d.TMIN=tmin;
	    	lt.add(d);
			dat++;
			dd = new Date(dd.getTime() + (1000 * 60 * 60 * 24));

		}
		return Response.status(Response.Status.OK).entity(lt).build();
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return Response.status(Response.Status.BAD_REQUEST).build();
	}	
	
}	
 

@GET
@Path("/forecaster/{DATE}")
@Produces(MediaType.APPLICATION_JSON)
public WeatherVO forecaster(@PathParam("DATE") String DATE) throws Exception{
	System.out.println(DATE);
	WeatherDao wd=new WeatherDao();
	return wd.forecaster(DATE);
		
}

@GET
@Path("/forecasters/{DATE}")
@Produces(MediaType.APPLICATION_JSON)
public ArrayList<WeatherVO> forecasters(@PathParam("DATE") String DATE) throws Exception{
	WeatherDao wd=new WeatherDao();
	ArrayList<WeatherVO> DatesList=new ArrayList<WeatherVO>();
	WeatherVO wv=wd.forecaster(DATE);
	System.out.println(wv);
	DatesList.add(wv);
	
	for(int i=0;i<4;i++)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(DATE));
		c.add(Calendar.DATE, 1);  // number of days to add
		DATE = sdf.format(c.getTime()); 
		wv=wd.forecaster(DATE);
		DatesList.add(wv);
	}
	return DatesList;
	
}


}
 

