package org.lnicholls.galleon.apps.weather;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.io.Serializable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lnicholls.galleon.database.PersistentValue;
import org.lnicholls.galleon.database.PersistentValueManager;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.ReloadCallback;
import org.lnicholls.galleon.util.ReloadTask;
import org.lnicholls.galleon.util.Tools;

// TODO Handle changes in configuration
// TODO Handle errors

public class WeatherData implements Serializable {

    private static Logger log = Logger.getLogger(WeatherData.class.getName());

    /*
     * The following is required by the Weather.com XML data feed. DO NOT USE THESE WITH ANY OTHER PROJECT SINCE THEY
     * HAVE BEEN REGISTERED WITH WEATHER.COM FOR THE GALLEON PROJECT. Obtain your own key by registering at:
     * http://registration.weather.com/registration/xmloap/step1
     */
    private static String PARTNER_ID = "1007257694";

    private static String LICENSE_KEY = "4521b6a53deec6b8";

    public WeatherData(String id, String city, String state, String zip, int width, int height) {
        mId = id;
        mCity = city;
        mState = state;
        mZip = zip;
        mWidth = width;
        mHeight = height;

        mTimeDateFormat = new SimpleDateFormat();
        mTimeDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss"); //2005-03-01T00:54:00

        mLinks = new ArrayList();
        mAlerts = new ArrayList();

        PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(this.getClass().getName() + "."
                + "id");
        if (persistentValue != null) {
            try
            {
                String cachedId = persistentValue.getValue();
                String cachedCity = PersistentValueManager.loadPersistentValue(this.getClass().getName() + "." + "city")
                        .getValue();
                String cachedState = PersistentValueManager.loadPersistentValue(this.getClass().getName() + "." + "state")
                        .getValue();
                String cachedZip = PersistentValueManager.loadPersistentValue(this.getClass().getName() + "." + "zip")
                        .getValue();
                String cachedFip = PersistentValueManager.loadPersistentValue(this.getClass().getName() + "." + "fip")
                        .getValue();
                String cachedLocalRadar = PersistentValueManager.loadPersistentValue(
                        this.getClass().getName() + "." + "localradar").getValue();
                if ((cachedCity != null && cachedCity.equals(city)) && (cachedState != null && cachedState.equals(state))
                        && (cachedZip != null && cachedZip.equals(city))) {
                    mId = cachedId;
                    mFip = cachedFip;
                    mLocalRadar = cachedLocalRadar;
                }
            }
            catch (Throwable ex)
            {
                log.error("Could not retrieve cached weather data", ex);
            }
        }

        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "city", mCity);
        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "state", mState);
        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "zip", mZip);

        new Thread() {
            public void run() {
                getAllWeather();
                determineFip();
                determineAlerts();
                determineLocalRadar();

                try {
                    log.debug("mLocalRadar=" + mLocalRadar);
                    if (mLocalRadar != null)
                        Tools.cacheImage(new URL(mLocalRadar), mWidth, mHeight);
                } catch (MalformedURLException ex) {
                    log.error("Could not download local radar", ex);
                }
                try {
                    if (mNationalRadar != null)
                        Tools.cacheImage(new URL(mNationalRadar), mWidth, mHeight);
                } catch (MalformedURLException ex) {
                    log.error("Could not download national radar", ex);
                }
            }
        }.start();

        Server.getServer().scheduleShortTerm(new ReloadTask(new ReloadCallback() {
            public void reload() {
            	log.debug("Weather");
                if (++mTimeCounter % 144 == 0) // every 720 mins
                {
                    getAllWeather();
                    mTimeCounter = 0;
                } else if (mTimeCounter % 24 == 0) // every 120 mins
                {
                    getForecastWeather();
                }
                if (mTimeCounter % 6 == 0) // every 30 mins
                {
                    getCurrentWeather();
                }

                if (mTimeCounter % 3 == 0) // every 15 mins
                {
                    determineLocalRadar();

                    try {
                        log.debug("mLocalRadar=" + mLocalRadar);
                        if (mLocalRadar != null)
                            Tools.cacheImage(new URL(mLocalRadar), mWidth, mHeight);
                    } catch (MalformedURLException ex) {
                        log.error("Could not download local radar", ex);
                    }
                    try {
                        if (mNationalRadar != null)
                            Tools.cacheImage(new URL(mNationalRadar), mWidth, mHeight);
                    } catch (MalformedURLException ex) {
                        log.error("Could not download national radar", ex);
                    }
                }

                if (mTimeCounter % 2 == 0) // every 10 mins
                {
                    determineFip();
                    determineAlerts();
                }
            }
        }), 5);
    }

    public static List getLocations(String city, String state, String zip) {
        Search search = new Search();

        try {
            SAXReader saxReader = new SAXReader();
            URL url = new URL("http://xoap.weather.com/search/search?where=" + zip); // try city, state too
            String page = Tools.getPage(url);
            if (page != null && page.length()>0)
            {
	            log.debug("Locations: " + page);
	            StringReader stringReader = new StringReader(page);
	            Document document = saxReader.read(stringReader);
	            //Document document = saxReader.read(new File("d:/galleon/location.xml"));
	
	            Element root = document.getRootElement(); // check for errors
	            search.setVersion(Tools.getAttribute(root, "ver"));
	
	            for (Iterator i = root.elementIterator("loc"); i.hasNext();) {
	                Element element = (Element) i.next();
	                Location location = new Location();
	                location.setId(Tools.getAttribute(element, "id"));
	                location.setType(Tools.getAttribute(element, "type"));
	                location.setValue(element.getText());
	                search.addLocation(location);
	            }
            }
        } catch (Exception ex) {
            log.error("Could not determine weather locations", ex);
        }
        return search.mLocations;
    }

    public void getAllWeather() {
        try {
            SAXReader saxReader = new SAXReader();
            PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(this.getClass().getName()
                    + "." + "date");
            String page = null;

            if (persistentValue != null && persistentValue.getValue()!=null) {
                String last = persistentValue.getValue();
                Date lastTime = null;
                try
                {
                	lastTime = new Date(last);
                }
                catch (Exception ex)
                {
                	try {
                        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "date", new Date()
                                .toString());
                    } catch (Exception ex2) {
                        log.error("Could not cache weather data");
                    }
                }
                Date current = new Date();
                if (lastTime!=null && (current.getTime() - lastTime.getTime()) / (1000 * 60) >= 720) {
                    // http://xoap.weather.com/weather/local/USNH0156?cc=*&dayf=2&link=xoap&prod=xoap&par=1007257694&key=4521b6a53deec6b8
                    URL url = new URL("http://xoap.weather.com/weather/local/" + mId
                            + "?cc=*&dayf=5&link=xoap&prod=xoap&par=" + PARTNER_ID + "&key=" + LICENSE_KEY);
                    page = Tools.getPage(url);

                    try {
                        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "content", page);
                        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "date", new Date()
                                .toString());
                    } catch (Exception ex) {
                        log.error("Could not cache weather data");
                    }
                } else {
                    persistentValue = PersistentValueManager.loadPersistentValue(this.getClass().getName() + "."
                            + "content");
                    if (persistentValue != null)
                        page = persistentValue.getValue();
                }
            } else {
                // http://xoap.weather.com/weather/local/USNH0156?cc=*&dayf=2&link=xoap&prod=xoap&par=1007257694&key=4521b6a53deec6b8
                URL url = new URL("http://xoap.weather.com/weather/local/" + mId
                        + "?cc=*&dayf=5&link=xoap&prod=xoap&par=" + PARTNER_ID + "&key=" + LICENSE_KEY);
                page = Tools.getPage(url);

                try {
                    PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "content", page);
                    PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "date", new Date()
                            .toString());
                } catch (Exception ex) {
                    log.error("Could not cache weather data");
                }
            }

            if (page != null) {
                log.debug("AllWeather: " + page);
                parseWeather(page);
                PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "id", mId);
            }
        } catch (Exception ex) {
            log.error("Could not determine weather conditions", ex);
        }
        //parseWeather("");
    }

    public void getCurrentWeather() {
        try {
            //http://xoap.weather.com/weather/local/USNH0156?cc=*&dayf=2&link=xoap&prod=xoap&par=1007257694&key=4521b6a53deec6b8
            URL url = new URL("http://xoap.weather.com/weather/local/" + mId + "?cc=*&prod=xoap&par=" + PARTNER_ID
                    + "&key=" + LICENSE_KEY);
            String page = Tools.getPage(url);
            log.debug("CurrentWeather: " + page);
            parseWeather(page);
        } catch (MalformedURLException ex) {
            log.error("Could not determine weather conditions", ex);
        }
        //parseWeather("");
    }

    public void getForecastWeather() {
        try {
            SAXReader saxReader = new SAXReader();
            //http://xoap.weather.com/weather/local/USNH0156?cc=*&dayf=2&link=xoap&prod=xoap&par=1007257694&key=4521b6a53deec6b8
            URL url = new URL("http://xoap.weather.com/weather/local/" + mId + "?cc=*&dayf=5&prod=xoap&par="
                    + PARTNER_ID + "&key=" + LICENSE_KEY);
            String page = Tools.getPage(url);
            log.debug("ForecastWeather: " + page);
            parseWeather(page);
            PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "id", mId);
        } catch (MalformedURLException ex) {
            log.error("Could not determine weather conditions", ex);
        }
        parseWeather("");
    }

    public void parseWeather(String page) {
        if (page!=null)
        {
	    	try {
	            SAXReader saxReader = new SAXReader();
	            StringReader stringReader = new StringReader(page);
	            Document document = saxReader.read(stringReader);
	
	            //Document document = saxReader.read(new File("d:/galleon/weather.xml"));
	
	            Element root = document.getRootElement();
	            setVersion(Tools.getAttribute(root, "ver"));
	
	            for (Iterator i = root.elementIterator(); i.hasNext();) {
	                Element element = (Element) i.next();
	                if (element.getName().equals("head")) {
	                    setLocale(Tools.getAttribute(element, "locale"));
	                    setTemperatureUnit(Tools.getAttribute(element, "ut"));
	                    setDistanceUnit(Tools.getAttribute(element, "ud"));
	                    setSpeedUnit(Tools.getAttribute(element, "us"));
	                    setPrecipitationUnit(Tools.getAttribute(element, "up"));
	                    setPressureUnit(Tools.getAttribute(element, "ur"));
	                    setFormat(Tools.getAttribute(element, "form"));
	                } else if (element.getName().equals("loc")) {
	                    setId(Tools.getAttribute(element, "id"));
	                    setName(Tools.getAttribute(element, "dnam"));
	                    setTime(Tools.getAttribute(element, "tm"));
	                    setLatitude(Tools.getAttribute(element, "lat"));
	                    setLongitude(Tools.getAttribute(element, "lon"));
	                    setSunrise(Tools.getAttribute(element, "sunr"));
	                    setSunset(Tools.getAttribute(element, "suns"));
	                    setTimeZone(Tools.getAttribute(element, "zone"));
	                } else if (element.getName().equals("lnks")) {
	                    for (Iterator linksIterator = element.elementIterator(); linksIterator.hasNext();) {
	                        Element linkElement = (Element) linksIterator.next();
	                        Link link = new Link();
	                        link.setUrl(Tools.getAttribute(linkElement, "l"));
	                        link.setName(Tools.getAttribute(linkElement, "t"));
	                        addLink(link);
	                    }
	                } else if (element.getName().equals("cc")) {
	                    CurrentConditions currentConditions = new CurrentConditions();
	                    currentConditions.setLastUpdate(Tools.getAttribute(element, "lsup"));
	                    currentConditions.setStation(Tools.getAttribute(element, "obst"));
	                    currentConditions.setTemperature(Tools.getAttribute(element, "tmp"));
	                    currentConditions.setFeelsLike(Tools.getAttribute(element, "flik"));
	                    currentConditions.setConditions(Tools.getAttribute(element, "t"));
	                    currentConditions.setIcon(Tools.getAttribute(element, "icon"));
	                    currentConditions.setHumidity(Tools.getAttribute(element, "hmid"));
	                    currentConditions.setVisibility(Tools.getAttribute(element, "vis"));
	                    currentConditions.setDewPoint(Tools.getAttribute(element, "dewp"));
	
	                    Element child = element.element("bar");
	                    if (child != null) {
	                        currentConditions.setBarometricPressure(Tools.getAttribute(child, "r"));
	                        currentConditions.setBarometricDescription(Tools.getAttribute(child, "d"));
	                    }
	                    child = element.element("wind");
	                    if (child != null) {
	                        currentConditions.setWindSpeed(Tools.getAttribute(child, "s"));
	                        currentConditions.setWindGusts(Tools.getAttribute(child, "gust"));
	                        currentConditions.setWindDirection(Tools.getAttribute(child, "d"));
	                        currentConditions.setWindDescription(Tools.getAttribute(child, "t"));
	                    }
	                    child = element.element("uv");
	                    if (child != null) {
	                        currentConditions.setUltraVioletIndex(Tools.getAttribute(child, "i"));
	                        currentConditions.setUltraVioletDescription(Tools.getAttribute(child, "t"));
	                    }
	                    child = element.element("moon");
	                    if (child != null) {
	                        currentConditions.setMoonPhaseIcon(Tools.getAttribute(child, "icon"));
	                        currentConditions.setMoonPhaseDescription(Tools.getAttribute(child, "t"));
	                    }
	                    setCurrentConditions(currentConditions);
	                } else if (element.getName().equals("dayf")) {
	                    Forecasts forecasts = new Forecasts();
	                    forecasts.setLastUpdate(Tools.getAttribute(element, "lsup"));
	
	                    for (Iterator dayIterator = element.elementIterator("day"); dayIterator.hasNext();) {
	                        Element dayElement = (Element) dayIterator.next();
	                        Forecast forecast = new Forecast();
	
	                        forecast.setDay(Tools.getAttribute(dayElement, "d"));
	                        forecast.setDescription(Tools.getAttribute(dayElement, "t"));
	                        forecast.setDate(Tools.getAttribute(dayElement, "dt"));
	                        forecast.setHigh(Tools.getAttribute(dayElement, "hi"));
	                        forecast.setLow(Tools.getAttribute(dayElement, "low"));
	                        forecast.setSunrise(Tools.getAttribute(dayElement, "sunr"));
	                        forecast.setSunset(Tools.getAttribute(dayElement, "suns"));
	
	                        for (Iterator partIterator = dayElement.elementIterator("part"); partIterator.hasNext();) {
	                            Element partElement = (Element) partIterator.next();
	                            Part part = new Part();
	                            part.setIcon(Tools.getAttribute(partElement, "icon"));
	                            part.setDescription(Tools.getAttribute(partElement, "t"));
	                            part.setPrecipitation(Tools.getAttribute(partElement, "ppcp"));
	                            part.setHumidity(Tools.getAttribute(partElement, "hmid"));
	
	                            Element windElement = partElement.element("wind");
	                            if (windElement != null) {
	                                part.setWindSpeed(Tools.getAttribute(windElement, "s"));
	                                part.setWindGusts(Tools.getAttribute(windElement, "gust"));
	                                part.setWindDirection(Tools.getAttribute(windElement, "d"));
	                                part.setWindDescription(Tools.getAttribute(windElement, "t"));
	                            }
	
	                            String which = Tools.getAttribute(partElement, "p");
	                            if (which.equals("n")) {
	                                forecast.addNightPart(part);
	                            } else
	                                forecast.addDayPart(part);
	                        }
	
	                        forecasts.addForecast(forecast);
	                    }
	                    setForecasts(forecasts);
	                }
	            }
	        } catch (Exception ex) {
	            log.error("Could not determine weather conditions", ex);
	        }
        }
    }

    public void determineLocalRadar() {
        if (mLocalRadar != null)
            return;

        try {
            HttpClient httpclient = new HttpClient();
            httpclient.getParams().setParameter("http.socket.timeout", new Integer(30000));
            httpclient.getParams().setParameter("http.useragent", System.getProperty("http.agent"));

            //Get location/county mapping from census bureau:
            // http://quickfacts.census.gov/cgi-bin/qfd/lookup?state=33000&place=nashua,nh,03060
            GetMethod get = new GetMethod("http://www.weather.com/weather/map/" + mZip);
            get.setFollowRedirects(true);

            String radarurl = "";

            try {
                int iGetResultCode = httpclient.executeMethod(get);
                final String strGetResponseBody = get.getResponseBodyAsString();
                //log.debug(strGetResponseBody);

                if (strGetResponseBody != null) {
                    //if (isMinNS4) var mapNURL = "/maps/local/local/us_close_bos_ultra_bos/1b/index_large.html";
                    String REGEX = "var mapNURL = \"(.*)\";";
                    Pattern p = Pattern.compile(REGEX);
                    Matcher m = p.matcher(strGetResponseBody);
                    if (m.find()) {
                        if (log.isDebugEnabled())
                            log.debug("Local radar URL: " + m.group(1));
                        radarurl = m.group(1);
                    }
                    if (radarurl.length() == 0) {
                        //    <iframe name="mapI" ID="mapI" width=600 height=560
                        // src="/maps/local/local/us_close_bos_ultra_bos/1b/index_large.html"
                        REGEX = "src=\"/maps/local/local(.*)\"";
                        p = Pattern.compile(REGEX);
                        m = p.matcher(strGetResponseBody);
                        if (m.find()) {
                            if (log.isDebugEnabled())
                                log.debug("Local radar URL: " + m.group(1));
                            radarurl = "/maps/local/local" + m.group(1);
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("Could not determine local radar", ex);
            } finally {
                get.releaseConnection();
            }

            get = new GetMethod("http://www.weather.com" + radarurl);
            get.setFollowRedirects(true);

            try {
                int iGetResultCode = httpclient.executeMethod(get);
                final String strGetResponseBody = get.getResponseBodyAsString();
                //log.debug(strGetResponseBody);

                if (strGetResponseBody != null) {
                    //<IMG NAME="mapImg" SRC="http://image.weather.com/web/radar/us_bos_closeradar_large_usen.jpg"
                    // WIDTH=600
                    // HEIGHT=405 BORDER=0 ALT="Doppler Radar 600 Mile"></TD>
                    String REGEX = "NAME=\"mapImg\" SRC=\"([^\"]*)\"";
                    Pattern p = Pattern.compile(REGEX);
                    Matcher m = p.matcher(strGetResponseBody);
                    if (m.find()) {
                        mLocalRadar = m.group(1);
                        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "localradar",
                                mLocalRadar);
                        return;
                    }
                }
            } catch (Exception ex) {
                log.error("Could not determine local radar", ex);
            } finally {
                get.releaseConnection();
            }
        } catch (Throwable ex) {
            //Tools.logException(WeatherData.class, ex);
            ex.printStackTrace();
        }
        log.error("Could not find local radar for: " + mCity + "," + mState + "," + mZip);
    }

    public void determineFip() {
        if (mFip != null)
            return;

        try {
            HttpClient httpclient = new HttpClient();
            httpclient.getParams().setParameter("http.socket.timeout", new Integer(30000));
            httpclient.getParams().setParameter("http.useragent", System.getProperty("http.agent"));

            //Get location/county mapping from census bureau:
            // http://quickfacts.census.gov/cgi-bin/qfd/lookup?state=33000&place=nashua,nh,03060
            GetMethod get = new GetMethod("http://quickfacts.census.gov/cgi-bin/qfd/lookup");
            get.setFollowRedirects(true);
            NameValuePair state = new NameValuePair("state", StateData.getFipFromSymbol(mState));
            NameValuePair place = new NameValuePair("place", mCity + "," + mState + "," + mZip);
            get.setQueryString(new NameValuePair[] { state, place });

            try {
                int iGetResultCode = httpclient.executeMethod(get);
                final String strGetResponseBody = get.getResponseBodyAsString();
                //log.debug(strGetResponseBody);

                if (strGetResponseBody != null) {
                    String REGEX = "href=\"/qfd/states/[0-9]*/([0-9]*).html\">([^<]*)</a>";
                    Pattern p = Pattern.compile(REGEX);
                    Matcher m = p.matcher(strGetResponseBody);
                    if (m.find()) {
                        if (log.isDebugEnabled())
                            log.debug("FIP: " + m.group(1) + "(" + m.group(2) + ")");
                        mFip = m.group(1);
                        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "fip", mFip);
                        return;
                    }
                }
            } catch (Exception ex) {
                log.error("Could not determine FIP", ex);
            } finally {
                get.releaseConnection();
            }

            get = new GetMethod("http://quickfacts.census.gov/cgi-bin/qfd/lookup");
            get.setFollowRedirects(true);
            get.setQueryString(new NameValuePair[] { state, place });

            try {
                int iGetResultCode = httpclient.executeMethod(get);
                final String strGetResponseBody = get.getResponseBodyAsString();
                log.debug(strGetResponseBody);

                if (strGetResponseBody != null) {
                    String REGEX = "href=\"/qfd/states/[0-9]*/([0-9]*).html\">([^<]*)</a>";
                    Pattern p = Pattern.compile(REGEX);
                    Matcher m = p.matcher(strGetResponseBody);
                    if (m.find()) {
                        if (log.isDebugEnabled())
                            log.debug("FIP: " + m.group(1) + "(" + m.group(2) + ")");
                        mFip = m.group(1);
                        PersistentValueManager.savePersistentValue(this.getClass().getName() + "." + "fip", mFip);
                        return;
                    } else {
                        mFip = StateData.getFipFromSymbol(mState);
                        log.info("Could not find county, using state FIP");
                        return;
                    }
                }
            } catch (Exception ex) {
                log.error("Could not determine FIP", ex);
            } finally {
                get.releaseConnection();
            }
        } catch (Exception ex) {
            Tools.logException(WeatherData.class, ex);
        }
        mFip = "000";
        log.error("Could not find FIP for: " + mCity + "," + mState + "," + mZip);
    }

    public void determineAlerts() {
        try {
            // Read CAP XML alerts feed from http://www.nws.noaa.gov/alerts/nh.cap
            HttpClient httpclient = new HttpClient();
            httpclient.getParams().setParameter("http.socket.timeout", new Integer(30000));
            httpclient.getParams().setParameter("http.useragent", System.getProperty("http.agent"));

            //Get location/county mapping from census bureau:
            // http://quickfacts.census.gov/cgi-bin/qfd/lookup?state=33000&place=nashua,nh,03060
            GetMethod get = new GetMethod("http://www.nws.noaa.gov/alerts/" + mState.toLowerCase() + ".cap");
            get.setFollowRedirects(true);

            try {
                int iGetResultCode = httpclient.executeMethod(get);
                final String strGetResponseBody = get.getResponseBodyAsString();
                log.debug(strGetResponseBody);

                if (strGetResponseBody != null) {
                    mAlerts = new ArrayList();
                    parseAlerts(strGetResponseBody);
                }
            } catch (Exception ex) {
                log.error("Could not determine weather alerts", ex);
            } finally {
                get.releaseConnection();
            }
            return;

        } catch (Exception ex) {
            Tools.logException(WeatherData.class, ex);
        }
        log.error("Could not find alerts for: " + mCity + "," + mState + "," + mZip);
        return;
    }

    public static class Alert implements Serializable {
        public Alert(String event, Date effective, Date expires, String headline, String description) {
            mEvent = event.trim();
            mEffective = effective;
            mExpires = expires;
            mHeadline = headline.trim();
            setDescription(description);
        }

        public void setEvent(String value) {
            mEvent = value;
        }

        public String getEvent() {
            return mEvent;
        }

        public void setEffective(Date value) {
            mEffective = value;
        }

        public Date getExpires() {
            return mExpires;
        }

        public void setExpires(Date value) {
            mExpires = value;
        }

        public Date getEffective() {
            return mEffective;
        }

        public void setHeadline(String value) {
            mHeadline = value;
        }

        public String getHeadline() {
            return mHeadline;
        }

        public void setDescription(String value) {
            mDescription = value.trim().replaceAll("\\.\\.\\.", ", ").replaceAll("\\n", " ");
        }

        public String getDescription() {
            return mDescription;
        }

        private String mEvent;

        private Date mEffective;

        private Date mExpires;

        private String mHeadline;

        private String mDescription;
    }

    private static String CAP_ALERT = "alert";

    private static String CAP_INFO = "info";

    private static String CAP_EVENT = "event";

    private static String CAP_EFFECTIVE = "effective";

    private static String CAP_EXPIRES = "expires";

    private static String CAP_HEADLINE = "headline";

    private static String CAP_DESCRIPTION = "description";

    private static String CAP_AREA = "area";

    private static String CAP_GEOCODE = "geocode";

    private void parseAlerts(String value) {
        try {
            SAXReader saxReader = new SAXReader();
            //Document document = saxReader.read(new File("d:/galleon/nh.cap.xml"));
            StringReader stringReader = new StringReader(value);
            Document document = saxReader.read(stringReader);

            // <cap:alert>
            Element root = document.getRootElement();

            for (Iterator i = root.elementIterator(); i.hasNext();) {
                Element element = (Element) i.next();
                boolean found = false;
                String event = "";
                Date effective = null;
                Date expires = null;
                String headline = "";
                String description = "";
                if (element.getName().equals(CAP_INFO)) {
                    for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
                        Element infoNode = (Element) elementIterator.next();
                        if (infoNode.getName().equals(CAP_EVENT)) {
                            event = infoNode.getText();
                            if (log.isDebugEnabled())
                                log.debug("event:" + event);
                        } else if (infoNode.getName().equals(CAP_EFFECTIVE)) {
                            String dateString = infoNode.getText();
                            if (dateString != null)
                                effective = mTimeDateFormat.parse(dateString);
                            if (log.isDebugEnabled())
                                log.debug("effective:" + effective);
                        } else if (infoNode.getName().equals(CAP_EXPIRES)) {
                            String dateString = infoNode.getText();
                            if (dateString != null)
                                expires = mTimeDateFormat.parse(dateString);
                            if (log.isDebugEnabled())
                                log.debug("expires:" + expires);
                        } else if (infoNode.getName().equals(CAP_HEADLINE)) {
                            headline = infoNode.getText();
                            if (log.isDebugEnabled())
                                log.debug("headline:" + headline);
                        } else if (infoNode.getName().equals(CAP_DESCRIPTION)) {
                            description = infoNode.getText();
                            if (log.isDebugEnabled())
                                log.debug("description:" + description);
                        } else if (infoNode.getName().equals(CAP_AREA)) {
                            for (Iterator areaIterator = infoNode.elementIterator(); areaIterator.hasNext();) {
                                Element areaNode = (Element) areaIterator.next();
                                if (log.isDebugEnabled())
                                    log.debug("areaNode:" + areaNode.getName());
                                if (areaNode.getName().equals(CAP_GEOCODE)) {
                                    String geoCode = areaNode.getText();
                                    if (log.isDebugEnabled())
                                        log.debug("geoCode:" + geoCode);
                                    if (log.isDebugEnabled())
                                        log.debug("mFIP:" + mFip);
                                    if (mFip.endsWith("000")) // whole state
                                    {
                                        found = true;
                                    } else if (geoCode.endsWith(mFip)) // specific county
                                    {
                                        found = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (found) {
                    mAlerts.add(new Alert(event, effective, expires, headline, description));
                }
            }
        } catch (Exception ex) {
            log.error("Could not parse weather alerts", ex);
        }
    }

    public static class Location implements Serializable {
        public String getId() {
            return mId;
        }

        public void setId(String value) {
            mId = value;
        }

        public String getType() {
            return mType;
        }

        public void setType(String value) {
            mType = value;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String value) {
            mValue = value;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private String mId;

        private String mType;

        private String mValue;
    }

    public static class Search implements Serializable {
        public Search() {
            mLocations = new ArrayList();
        }

        public void addLocation(Location location) {
            mLocations.add(location);
        }

        public Iterator getLocations() {
            return mLocations.iterator();
        }

        public String getVersion() {
            return mVersion;
        }

        public void setVersion(String version) {
            mVersion = version;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private String mVersion;

        private ArrayList mLocations;
    }

    public static class Link implements Serializable {
        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int value) {
            mPosition = value;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String value) {
            mUrl = value;
        }

        public String getName() {
            return mName;
        }

        public void setName(String value) {
            mName = value;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private int mPosition;

        private String mUrl;

        private String mName;
    }

    public static class CurrentConditions implements Serializable {
        public String getLastUpdate() {
            return mLastUpdate;
        }

        public void setLastUpdate(String value) {
            mLastUpdate = value;
        }

        public String getStation() {
            return mStation;
        }

        public void setStation(String value) {
            mStation = value;
        }

        public String getTemperature() {
            return mTemperature;
        }

        public void setTemperature(String value) {
            mTemperature = value;
        }

        public String getFeelsLike() {
            return mFeelsLike;
        }

        public void setFeelsLike(String value) {
            mFeelsLike = value;
        }

        public String getConditions() {
            return mConditions;
        }

        public void setConditions(String value) {
            mConditions = value;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String value) {
            mIcon = value;
        }

        public String getHumidity() {
            return mHumidity;
        }

        public void setHumidity(String value) {
            mHumidity = value;
        }

        public String getVisibility() {
            return mVisibility;
        }

        public void setVisibility(String value) {
            mVisibility = value;
        }

        public String getDewPoint() {
            return mDewPoint;
        }

        public void setDewPoint(String value) {
            mDewPoint = value;
        }

        public String getBarometricPressure() {
            return mBarometricPressure;
        }

        public void setBarometricPressure(String value) {
            mBarometricPressure = value;
        }

        public String getBarometricDescription() {
            return mBarometricDescription;
        }

        public void setBarometricDescription(String value) {
            mBarometricDescription = value;
        }

        public String getWindSpeed() {
            return mWindSpeed;
        }

        public void setWindSpeed(String value) {
            mWindSpeed = value;
        }

        public String getWindGusts() {
            return mWindGusts;
        }

        public void setWindGusts(String value) {
            mWindGusts = value;
        }

        public String getWindDirection() {
            return mWindDirection;
        }

        public void setWindDirection(String value) {
            mWindDirection = value;
        }

        public String getWindDescription() {
            return mWindDescription;
        }

        public void setWindDescription(String value) {
            mWindDescription = value;
        }

        public String getUltraVioletIndex() {
            return mUltraVioletIndex;
        }

        public void setUltraVioletIndex(String value) {
            mUltraVioletIndex = value;
        }

        public String getUltraVioletDescription() {
            return mUltraVioletDescription;
        }

        public void setUltraVioletDescription(String value) {
            mUltraVioletDescription = value;
        }

        public String getMoonPhaseIcon() {
            return mMoonPhaseIcon;
        }

        public void setMoonPhaseIcon(String value) {
            mMoonPhaseIcon = value;
        }

        public String getMoonPhaseDescription() {
            return mMoonPhaseDescription;
        }

        public void setMoonPhaseDescription(String value) {
            mMoonPhaseDescription = value;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private String mLastUpdate;

        private String mStation;

        private String mTemperature;

        private String mFeelsLike;

        private String mConditions;

        private String mIcon;

        private String mHumidity;

        private String mVisibility;

        private String mDewPoint;

        private String mBarometricPressure;

        private String mBarometricDescription;

        private String mWindSpeed;

        private String mWindGusts;

        private String mWindDirection;

        private String mWindDescription;

        private String mUltraVioletIndex;

        private String mUltraVioletDescription;

        private String mMoonPhaseIcon;

        private String mMoonPhaseDescription;
    }

    public static class Forecasts implements Serializable {
        public Forecasts() {
            mForecast = new ArrayList();
        }

        public String getLastUpdate() {
            return mLastUpdate;
        }

        public void setLastUpdate(String value) {
            mLastUpdate = value;
        }

        public void addForecast(Forecast forecast) {
            mForecast.add(forecast);
        }

        public Iterator getForecast() {
            return mForecast.iterator();
        }

        private String mLastUpdate;

        private ArrayList mForecast;
    }

    public static class Part implements Serializable {
        public Part() {

        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String value) {
            mIcon = value;
        }

        public String getHumidity() {
            return mHumidity;
        }

        public void setHumidity(String value) {
            mHumidity = value;
        }

        public String getPrecipitation() {
            return mPrecipitation;
        }

        public void setPrecipitation(String value) {
            mPrecipitation = value;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String value) {
            mDescription = value;
        }

        public String getWindSpeed() {
            return mWindSpeed;
        }

        public void setWindSpeed(String value) {
            mWindSpeed = value;
        }

        public String getWindGusts() {
            return mWindGusts;
        }

        public void setWindGusts(String value) {
            mWindGusts = value;
        }

        public String getWindDirection() {
            return mWindDirection;
        }

        public void setWindDirection(String value) {
            mWindDirection = value;
        }

        public String getWindDescription() {
            return mWindDescription;
        }

        public void setWindDescription(String value) {
            mWindDescription = value;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private String mIcon;

        private String mHumidity;

        private String mPrecipitation;

        private String mDescription;

        private String mWindSpeed;

        private String mWindGusts;

        private String mWindDirection;

        private String mWindDescription;
    }

    public static class Forecast implements Serializable {
        public Forecast() {

        }

        public String getHigh() {
            return mHigh;
        }

        public void setHigh(String value) {
            mHigh = value;
        }

        public String getLow() {
            return mLow;
        }

        public void setLow(String value) {
            mLow = value;
        }

        public String getSunrise() {
            return mSunrise;
        }

        public void setSunrise(String value) {
            mSunrise = value;
        }

        public String getSunset() {
            return mSunset;
        }

        public void setSunset(String value) {
            mSunset = value;
        }

        public String getDay() {
            return mDay;
        }

        public void setDay(String value) {
            mDay = value;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String value) {
            mDescription = value;
        }

        public String getDate() {
            return mDate;
        }

        public void setDate(String value) {
            mDate = value;
        }

        public void addDayPart(Part part) {
            mDayPart = part;
        }

        public void addNightPart(Part part) {
            mNightPart = part;
        }

        public Part getDayForecast() {
            return mDayPart;
        }

        public Part getNightForecast() {
            return mNightPart;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private String mHigh;

        private String mLow;

        private String mSunrise;

        private String mSunset;

        private String mDay;

        private String mDescription;

        private String mDate;

        private Part mDayPart;

        private Part mNightPart;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String value) {
        mLocale = value;
    }

    public String getTemperatureUnit() {
        return mTemperatureUnit;
    }

    public void setTemperatureUnit(String value) {
        mTemperatureUnit = value;
    }

    public String getDistanceUnit() {
        return mDistanceUnit;
    }

    public void setDistanceUnit(String value) {
        mDistanceUnit = value;
    }

    public String getSpeedUnit() {
        return mSpeedUnit;
    }

    public void setSpeedUnit(String value) {
        mSpeedUnit = value;
    }

    public String getPrecipitationUnit() {
        return mPrecipitationUnit;
    }

    public void setPrecipitationUnit(String value) {
        mPrecipitationUnit = value;
    }

    public String getPressureUnit() {
        return mPressureUnit;
    }

    public void setPressureUnit(String value) {
        mPressureUnit = value;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String value) {
        mFormat = value;
    }

    public String getId() {
        return mId;
    }

    public void setId(String value) {
        mId = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String value) {
        mTime = value;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String value) {
        mLatitude = value;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String value) {
        mLongitude = value;
    }

    public String getSunrise() {
        return mSunrise;
    }

    public void setSunrise(String value) {
        mSunrise = value;
    }

    public String getSunset() {
        return mSunset;
    }

    public void setSunset(String value) {
        mSunset = value;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String value) {
        mTimeZone = value;
    }

    public CurrentConditions getCurrentConditions() {
        return mCurrentConditions;
    }

    public void setCurrentConditions(CurrentConditions value) {
        mCurrentConditions = value;
    }

    public Forecasts getForecasts() {
        return mForecasts;
    }

    public void setForecasts(Forecasts value) {
        mForecasts = value;
    }

    public void addLink(Link link) {
        mLinks.add(link);
    }

    public Iterator getLinks() {
        return mLinks.iterator();
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public String getLocalRadar() {
        return mLocalRadar;
    }

    public String getNationalRadar() {
        return mNationalRadar;
    }

    public boolean hasAlerts() {
        return mAlerts.size() > 0;
    }

    public Iterator getAlerts() {
        return mAlerts.iterator();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private String mCity;

    private String mState;

    private String mZip;

    private String mId;

    private String mVersion;

    // head
    private String mLocale;

    private String mTemperatureUnit;

    private String mDistanceUnit;

    private String mSpeedUnit;

    private String mPrecipitationUnit;

    private String mPressureUnit;

    private String mFormat;

    // loc
    private String mName;

    private String mTime;

    private String mLatitude;

    private String mLongitude;

    private String mSunrise;

    private String mSunset;

    private String mTimeZone;

    private ArrayList mLinks;

    private ArrayList mAlerts;

    private CurrentConditions mCurrentConditions;

    private Forecasts mForecasts;

    private String mLocalRadar;

    private String mNationalRadar = "http://image.weather.com/images/maps/current/curwx_600x405.jpg";

    private String mFip;

    private int mWidth = -1;

    private int mHeight = -1;

    private int mTimeCounter = 0;

    private SimpleDateFormat mTimeDateFormat;
}