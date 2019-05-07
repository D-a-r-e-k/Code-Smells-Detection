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

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppContext;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultMenuScreen;
import org.lnicholls.galleon.widget.DefaultOptionList;
import org.lnicholls.galleon.widget.DefaultScreen;
import org.lnicholls.galleon.widget.ScrollText;

import com.tivo.hme.bananas.BButton;
import com.tivo.hme.bananas.BEvent;
import com.tivo.hme.bananas.BHighlight;
import com.tivo.hme.bananas.BHighlights;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.interfaces.IHttpRequest;
import com.tivo.hme.sdk.IHmeProtocol;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.interfaces.IContext;
import com.tivo.hme.interfaces.IArgumentList;

public class Weather extends DefaultApplication {

    private static Logger log = Logger.getLogger(Weather.class.getName());

    public final static String TITLE = "Weather";

    private int mCurrent = 0;

    private Resource mMenuBackground;

    private Resource mInfoBackground;

    private Resource mAlertIcon;

    private Resource mIcon;

    private Resource mNA;

    public void init(IContext context) throws Exception {
        super.init(context);

        mMenuBackground = getSkinImage("menu", "background");
        mInfoBackground = getSkinImage("info", "background");
        mAlertIcon = getSkinImage("menu", "alert");
        mIcon = getSkinImage("menu", "item");
        mNA = getSkinImage(null, "na");

        try
        {
        	push(new WeatherMenuScreen(this), TRANSITION_NONE);
        }
        catch (Exception ex)
        {
        	Tools.logException(Weather.class, ex);
        }
        
        initialize();
    }

    public class WeatherMenuScreen extends DefaultMenuScreen {

        public WeatherMenuScreen(Weather app) {
            super(app, "Weather");

            getBelow().setResource(mMenuBackground);

            WeatherData weatherData = ((WeatherFactory) getFactory()).getWeatherData();
            
            mMenuList.add("Current Conditions");
            mMenuList.add("Forecast");
            mMenuList.add("Local Radar");
            mMenuList.add("National Radar");
            if (weatherData.hasAlerts())
                mMenuList.add("Alerts");
            
            mGenericWeatherScreen = new GenericWeatherScreen((Weather) getBApp(), weatherData);            
        }

        public boolean handleAction(BView view, Object action) {
            if (action.equals("push")) {
                load();

                WeatherData weatherData = ((WeatherFactory) getFactory()).getWeatherData();
                mGenericWeatherScreen.setScreen(mMenuList.getFocus());
                if (mMenuList.getFocus() == 0)
                    getBApp().push(mGenericWeatherScreen, TRANSITION_LEFT);
                else if (mMenuList.getFocus() == 1)
                    getBApp().push(mGenericWeatherScreen, TRANSITION_LEFT);
                else if (mMenuList.getFocus() == 2)
                    getBApp().push(mGenericWeatherScreen, TRANSITION_LEFT);
                else if (mMenuList.getFocus() == 3)
                    getBApp().push(mGenericWeatherScreen, TRANSITION_LEFT);
                else if (mMenuList.getFocus() == 4)
                    getBApp().push(mGenericWeatherScreen, TRANSITION_LEFT);
                return true;
            }
            return super.handleAction(view, action);
        }

        protected void createRow(BView parent, int index) {
            BView icon = new BView(parent, 10, 3, 30, 30);
            if (index == 4)
                icon.setResource(mAlertIcon);
            else
                icon.setResource(mIcon);

            BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
            name.setShadow(true);
            name.setFlags(RSRC_HALIGN_LEFT);
            name.setValue(mMenuList.get(index).toString());
        }
        
        private GenericWeatherScreen mGenericWeatherScreen;
    }
    
    public class GenericWeatherScreen extends DefaultScreen {
        private final int top = SAFE_TITLE_V + 100;

        private final int border_left = SAFE_TITLE_H + 256;

        private final int text_width = getWidth() - border_left - (SAFE_TITLE_H);

        private WeatherList list;

        public GenericWeatherScreen(Weather app, WeatherData data) {
            super(app, true);

            getBelow().setResource(mInfoBackground);
            
            mCurrentConditions = new CurrentConditionsScreen(getBelow(), data);
            mCurrentConditions.setVisible(false);
            mForecast = new ForecastScreen(getBelow(), data);
            mForecast.setVisible(false);
            mLocalRadar = new LocalRadarScreen(getBelow(), data);
            mLocalRadar.setVisible(false);
            mNationalRadar = new NationalRadarScreen(getBelow(), data);
            mNationalRadar.setVisible(false);
            mAlerts = new AlertsScreen(getBelow(), data);
            mAlerts.setVisible(false);
            
            mWeatherData = data;

            setTitle("Current Conditions");

            setFooter("weather.com");

            /*
             * list = new WeatherList(this.getNormal(), SAFE_TITLE_H + 10, (height - SAFE_TITLE_V) - 55, (int) Math
             * .round((getWidth() - (SAFE_TITLE_H * 2)) / 2), 90, 35); list.add("Press SELECT to go back");
             * setFocusDefault(list);
             */

            BButton button = new BButton(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
                    .round((getWidth() - (SAFE_TITLE_H * 2)) / 2.5), 35);
            button.setResource(createText("default-24.font", Color.white, "Return to menu"));
            button.setBarAndArrows(BAR_HANG, BAR_DEFAULT, "pop", null, null, null, true);
            setFocus(button);
        }

        public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
            try {
            
            } catch (Exception ex) {
                log.error("Could not update weather text", ex);
            }
            return super.handleEnter(arg, isReturn);
        }

        public boolean handleKeyPress(int code, long rawcode) {
            switch (code) {
            case KEY_SELECT:
                postEvent(new BEvent.Action(this, "pop"));
                return true;
            case KEY_CHANNELUP:
				getBApp().play("pageup.snd");
				getBApp().flush();
				getPrevPos();
				setScreen(mScreen);
				return true;
			case KEY_CHANNELDOWN:
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				setScreen(mScreen);
				return true;
            case KEY_UP:
            case KEY_DOWN:
                return mCurrentScreen.handleKeyPress(code, rawcode);
            }
            
            return super.handleKeyPress(code, rawcode);
        }
        
        public void getNextPos() {
        	if (mWeatherData.hasAlerts())
        	{
        		if (++mScreen > 4)
	        		mScreen = 0;
        	}
        	else
        	{
	        	if (++mScreen > 3)
	        		mScreen = 0;
        	}
		}

		public void getPrevPos() {
			if (mWeatherData.hasAlerts())
        	{
				if (--mScreen < 0)
					mScreen = 4;				
        	}
			else
			{
				if (--mScreen < 0)
					mScreen = 3;				
			}
		}        
        
        public boolean handleAction(BView view, Object action) {
            if (action.equals("pop")) {
                this.getBApp().pop();
                return true;
            }
            return super.handleAction(view, action);
        }
        
        public void setScreen(int screen)
        {
        	mScreen = screen;
        	try {
                setPainting(false);
	        	switch (screen) {
				case 0:
					setTitle("Current Conditions");
		            mForecast.setVisible(false);
		            mLocalRadar.setVisible(false);
		            mNationalRadar.setVisible(false);
		            mAlerts.setVisible(false);
		            mCurrentConditions.setVisible(true);
		            mCurrentConditions.updateText();
		            mCurrentScreen = mCurrentConditions;		            
		            setFooter("weather.com");				
					break;
				case 1:
					setTitle("Forecast");
		            mCurrentConditions.setVisible(false);
		            mLocalRadar.setVisible(false);
		            mNationalRadar.setVisible(false);
		            mAlerts.setVisible(false);
		            mForecast.setVisible(true);
		            mForecast.updateText();
		            mCurrentScreen = mForecast;
		            setFooter("weather.com");				
					break;				
				case 2:
					setTitle(" ");
		            mCurrentConditions.setVisible(false);
		            mForecast.setVisible(false);
		            mNationalRadar.setVisible(false);
		            mAlerts.setVisible(false);
		            mLocalRadar.setVisible(true);
		            mLocalRadar.updateImage();
		            mCurrentScreen = mLocalRadar;
		            setFooter(" ");				
					break;
				case 3:
					setTitle(" ");
		            mCurrentConditions.setVisible(false);
		            mForecast.setVisible(false);
		            mLocalRadar.setVisible(false);
		            mAlerts.setVisible(false);
		            mNationalRadar.setVisible(true);
		            mNationalRadar.updateImage();
		            mCurrentScreen = mNationalRadar;		            
		            setFooter(" ");				
					break;				
				case 4:
					setTitle("Alerts");
		            mCurrentConditions.setVisible(false);
		            mForecast.setVisible(false);
		            mLocalRadar.setVisible(false);
		            mNationalRadar.setVisible(false);
		            mAlerts.setVisible(true);
		            mAlerts.updateText();
		            mCurrentScreen = mAlerts;
		            setFooter("weather.gov");				
					break;				
				}
	        	flush();
        	} finally {
                setPainting(true);
            }	        	
        }

        private WeatherData mWeatherData;
        
        private CurrentConditionsScreen mCurrentConditions;
        private ForecastScreen mForecast;
        private LocalRadarScreen mLocalRadar;
        private NationalRadarScreen mNationalRadar;
        private AlertsScreen mAlerts;
        private BView mCurrentScreen;
        
        private int mScreen;
    }    

    public class CurrentConditionsScreen extends BView {
        private final int top = SAFE_TITLE_V + 100;

        private final int border_left = SAFE_TITLE_H + 256;

        private final int text_width = getWidth() - border_left - (SAFE_TITLE_H);

        private WeatherList list;

        public CurrentConditionsScreen(BView parent, WeatherData data) {
        	super(parent, 0, 0, parent.getWidth(), parent.getHeight());
            
        	mWeatherData = data;

            int start = top;

            icon = new BView(this, SAFE_TITLE_H, SAFE_TITLE_V + 30, 256, 256);
            icon.setResource(mNA);

            Resource font = createFont("Dekadens.ttf", Font.BOLD, 60);
            temperatureText = new BText(this, border_left, SAFE_TITLE_V + 70, text_width - 70, 70);
            temperatureText.setFlags(RSRC_HALIGN_RIGHT | RSRC_VALIGN_TOP);
            temperatureText.setFont(font);
            temperatureText.setColor(new Color(127, 235, 192));
            temperatureText.setShadow(Color.black, 3);

            conditionsText = new BText(this, SAFE_TITLE_H, SAFE_TITLE_V + 280, 256, 80);
            conditionsText.setFlags(IHmeProtocol.RSRC_HALIGN_CENTER | RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
            conditionsText.setFont("default-24-bold.font");
            conditionsText.setColor(new Color(127, 235, 192));
            conditionsText.setShadow(true);
            conditionsText.setValue("Snowing");

            start += 70;

            BText labelText = new BText(this, border_left, start, text_width, 30);
            labelText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
            labelText.setFont("default-18-bold.font");
            labelText.setShadow(true);
            labelText.setValue("UV Index:");

            uvIndexText = new BText(this, border_left, start, text_width, 30);
            uvIndexText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
            uvIndexText.setFont("default-18-bold.font");
            uvIndexText.setShadow(true);

            start += 25;

            labelText = new BText(this, border_left, start, text_width, 30);
            labelText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
            labelText.setFont("default-18-bold.font");
            labelText.setShadow(true);
            labelText.setValue("Wind:");

            windText = new BText(this, border_left, start, text_width, 30);
            windText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
            windText.setFont("default-18-bold.font");
            windText.setShadow(true);

            start += 25;

            labelText = new BText(this, border_left, start, text_width, 30);
            labelText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
            labelText.setFont("default-18-bold.font");
            labelText.setShadow(true);
            labelText.setValue("Humidity:");

            humidityText = new BText(this, border_left, start, text_width, 30);
            humidityText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
            humidityText.setFont("default-18-bold.font");
            humidityText.setShadow(true);

            start += 25;

            labelText = new BText(this, border_left, start, text_width, 30);
            labelText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
            labelText.setFont("default-18-bold.font");
            labelText.setShadow(true);
            labelText.setValue("Pressure:");

            pressureText = new BText(this, border_left, start, text_width, 30);
            pressureText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
            pressureText.setFont("default-18-bold.font");
            pressureText.setShadow(true);

            start += 25;

            labelText = new BText(this, border_left, start, text_width, 30);
            labelText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
            labelText.setFont("default-18-bold.font");
            labelText.setShadow(true);
            labelText.setValue("Dew Point:");

            dewPointText = new BText(this, border_left, start, text_width, 30);
            dewPointText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
            dewPointText.setFont("default-18-bold.font");
            dewPointText.setShadow(true);

            start += 25;

            labelText = new BText(this, border_left, start, text_width, 30);
            labelText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
            labelText.setFont("default-18-bold.font");
            labelText.setShadow(true);
            labelText.setValue("Visibility:");

            visibilityText = new BText(this, border_left, start, text_width, 30);
            visibilityText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
            visibilityText.setFont("default-18-bold.font");
            visibilityText.setShadow(true);

            updateText();
        }

        public void updateText() {
            try {
                temperatureText.setValue(mWeatherData.getCurrentConditions().getTemperature());
                conditionsText.setValue(mWeatherData.getCurrentConditions().getConditions());
                icon.setResource(getSkinImage(null, pad(mWeatherData.getCurrentConditions().getIcon())));
                uvIndexText.setValue(mWeatherData.getCurrentConditions().getUltraVioletIndex() + " "
                        + mWeatherData.getCurrentConditions().getUltraVioletDescription());
                if (mWeatherData.getCurrentConditions().getWindDescription().toLowerCase().equals("calm"))
                    windText.setValue(mWeatherData.getCurrentConditions().getWindDescription());
                else
                    windText.setValue("From " + mWeatherData.getCurrentConditions().getWindDescription() + " at "
                            + mWeatherData.getCurrentConditions().getWindSpeed() + " " + mWeatherData.getSpeedUnit());
                humidityText.setValue(mWeatherData.getCurrentConditions().getHumidity() + "%");
                pressureText.setValue(mWeatherData.getCurrentConditions().getBarometricPressure() + " "
                        + mWeatherData.getPressureUnit() + ".");
                dewPointText.setValue(mWeatherData.getCurrentConditions().getDewPoint() + "\u00BA"
                        + mWeatherData.getTemperatureUnit());
                visibilityText.setValue(mWeatherData.getCurrentConditions().getVisibility() + " "
                        + mWeatherData.getDistanceUnit() + ".");
            } catch (Exception ex) {
                Tools.logException(Weather.class, ex);
            }
        }

        private BView icon;

        private BText temperatureText;

        private BText conditionsText;

        private BText uvIndexText;

        private BText windText;

        private BText humidityText;

        private BText pressureText;

        private BText dewPointText;

        private BText visibilityText;

        WeatherData mWeatherData;
    }

    public class ForecastScreen extends BView {
    	
    	protected final int TOP = SAFE_TITLE_V + 80;
        
        protected final int PAD = 10;
        
        protected final int BORDER_TOP = TOP + PAD;
        
        protected final int BORDER_LEFT = SAFE_TITLE_H + PAD;

        protected final int BODY_WIDTH = getWidth() - BORDER_LEFT - (SAFE_TITLE_H);

        protected final int BODY_HEIGHT = getHeight() - 2 * SAFE_TITLE_V;

        public ForecastScreen(BView parent, WeatherData data) {
        	super(parent, 0, 0, parent.getWidth(), parent.getHeight());

            mWeatherData = data;

            int gap = 6;

            int dayWidth = (BODY_WIDTH - 4 * gap) / 5;

            for (int i = 0; i < 5; i++) {
                int start = TOP;

                int x = (dayWidth + gap / 2) * i;

                dayText[i] = new BText(this, BORDER_LEFT + x, start, dayWidth, 20);
                dayText[i].setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP);
                dayText[i].setFont("default-18-bold.font");
                dayText[i].setShadow(true);

                start = start + 20;

                iconString[i] = "N/A";
                icon[i] = new BView(this, BORDER_LEFT + x, start, dayWidth, dayWidth);
                icon[i].setResource(mNA);

                start = start + dayWidth;

                hiText[i] = new BText(this, BORDER_LEFT + x, start, dayWidth, 30);
                hiText[i].setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP);
                hiText[i].setFont("default-24-bold.font");
                hiText[i].setShadow(true);

                start = start + 30;

                loText[i] = new BText(this, BORDER_LEFT + x, start, dayWidth, 20);
                loText[i].setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP);
                loText[i].setFont("default-18-bold.font");
                loText[i].setShadow(true);

                start = start + 20;

                descriptionText[i] = new BText(this, BORDER_LEFT + x, start, dayWidth, 60);
                descriptionText[i].setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP | RSRC_TEXT_WRAP);
                descriptionText[i].setFont("default-18-bold.font");
                descriptionText[i].setColor(new Color(127, 235, 192));
                descriptionText[i].setShadow(true);
            }

            updateText();
        }

        public void updateText() {

            WeatherData.Forecasts forecasts = mWeatherData.getForecasts();

            int counter = 0;
            Iterator iterator = forecasts.getForecast();
            while (iterator.hasNext()) {
                try {
                    WeatherData.Forecast forecast = (WeatherData.Forecast) iterator.next();
                    WeatherData.Part dayPart = forecast.getDayForecast();
                    WeatherData.Part nightPart = forecast.getNightForecast();
                    if (iconString[counter]!=null && !iconString[counter].equals(dayPart.getIcon()))
                    {
	                    iconString[counter] = dayPart.getIcon();
	                    ByteArrayOutputStream baos = Server.getServer().getSkin().getImage(
	                            Weather.this.getClass().getName(), null, pad(dayPart.getIcon()));
	
	                    java.awt.Image image = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()))
	                            .getScaledInstance(BODY_WIDTH / 5, BODY_WIDTH / 5, java.awt.Image.SCALE_SMOOTH);
	                    //BufferedImage image = (BufferedImage) getSkinImage(null,
	                    // pad(dayPart.getIcon())).getScaledInstance(
	                    //        BODY_WIDTH / 5, BODY_WIDTH / 5, java.awt.Image.SCALE_SMOOTH);
	                    //java.awt.Image image = Tools.getResourceAsImage(getClass(), pad(dayPart.getIcon()) + ".png")
	                    //        .getScaledInstance(BODY_WIDTH / 5, BODY_WIDTH / 5, java.awt.Image.SCALE_SMOOTH);
	                    //image = (BufferedImage) Tools.getImage(image); ???
	                    //icon[counter].setResource(getSkinImage(null, pad(dayPart.getIcon())), RSRC_IMAGE_BESTFIT);
	                    icon[counter].setResource(createImage(image));
                    }
                    dayText[counter].setValue(forecast.getDescription());
                    String value = forecast.getHigh();
                    if (value.equals("N/A"))
                        value = mWeatherData.getCurrentConditions().getTemperature();
                    hiText[counter].setValue(value);
                    loText[counter].setValue(forecast.getLow());
                    value = dayPart.getDescription();
                    if (value.equals("N/A"))
                        value = nightPart.getDescription();
                    descriptionText[counter].setValue(value);
                } catch (Exception ex) {
                    log.error("Could not update weather image", ex);
                }

                counter = counter + 1;
            }
        }

        private String pad(String value) {
            if (value.length() == 0)
                return "00";
            if (value.length() == 1)
                return "0" + value;
            return value;
        }

        private String[] iconString = new String[5];
        
        private BView[] icon = new BView[5];

        private BText[] dayText = new BText[5];

        private BText[] hiText = new BText[5];

        private BText[] loText = new BText[5];

        private BText[] descriptionText = new BText[5];

        private WeatherData mWeatherData;
    }

    public class LocalRadarScreen extends BView {
        private WeatherList list;

        public LocalRadarScreen(BView parent, WeatherData data) {
        	super(parent, 0, 0, parent.getWidth(), parent.getHeight());

            mWeatherData = data;

            image = new BView(this, SAFE_TITLE_H, SAFE_TITLE_V, getWidth() - (SAFE_TITLE_H * 2), getHeight()
                    - (SAFE_TITLE_V * 2));

            /*
             * list = new WeatherList(this.getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
             * .round((getWidth() - (SAFE_TITLE_H * 2)) / 2), 90, 35); list.add("Press SELECT to go back");
             * setFocusDefault(list);
             */

            updateImage();
        }

        public void updateImage() {
            WeatherData.Forecasts forecasts = mWeatherData.getForecasts();

            try {
                if (mWeatherData.getLocalRadar() != null) {
                    java.awt.Image cached = Tools.retrieveCachedImage(new URL(mWeatherData.getLocalRadar()));
                    if (cached != null) {
                        //cached = cached.getScaledInstance(image.getWidth(), image.getHeight(),
                        // java.awt.Image.SCALE_SMOOTH);
                        //cached = Tools.getImage(cached);
                        image.setResource(cached);
                        return;
                    }
                }
            } catch (MalformedURLException ex) {
                log.error("Could not update weather local radar", ex);
            }

            image.setResource(mNA);
        }

        private BView image;

        WeatherData mWeatherData;
    }

    public class NationalRadarScreen extends BView {

        public NationalRadarScreen(BView parent, WeatherData data) {
        	super(parent, 0, 0, parent.getWidth(), parent.getHeight());

            mWeatherData = data;

            image = new BView(this, SAFE_TITLE_H, SAFE_TITLE_V, getWidth() - (SAFE_TITLE_H * 2), getHeight()
                    - (SAFE_TITLE_V * 2));

            /*
             * list = new WeatherList(this.getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
             * .round((getWidth() - (SAFE_TITLE_H * 2)) / 2), 90, 35); list.add("Press SELECT to go back");
             * setFocusDefault(list);
             */

            updateImage();
        }

        public void updateImage() {
            WeatherData.Forecasts forecasts = mWeatherData.getForecasts();

            try {
                if (mWeatherData.getNationalRadar() != null) {
                    java.awt.Image cached = Tools.retrieveCachedImage(new URL(mWeatherData.getNationalRadar()));
                    if (cached != null) {
                        image.setResource(cached);
                        return;
                    }
                }
            } catch (MalformedURLException ex) {
                log.error("Could not update weather local radar", ex);
            }
            image.setResource(mNA);
        }

        private BView image;

        WeatherData mWeatherData;
    }

    public class AlertsScreen extends BView {
    	protected final int TOP = SAFE_TITLE_V + 80;
        
        protected final int PAD = 10;
        
        protected final int BORDER_TOP = TOP + PAD;
        
        protected final int BORDER_LEFT = SAFE_TITLE_H + PAD;

        protected final int BODY_WIDTH = getWidth() - BORDER_LEFT - (SAFE_TITLE_H);

        protected final int BODY_HEIGHT = getHeight() - 2 * SAFE_TITLE_V;

        public AlertsScreen(BView parent, WeatherData data) {
        	super(parent, 0, 0, parent.getWidth(), parent.getHeight());

            mWeatherData = data;

            mDateFormat = new SimpleDateFormat();
            mDateFormat.applyPattern("EEE M/d hh:mm a");

            int start = TOP - 30;

            eventText = new BText(this, BORDER_LEFT, start, BODY_WIDTH, 30);
            eventText.setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP | RSRC_TEXT_WRAP);
            eventText.setFont("default-24-bold.font");
            eventText.setColor(new Color(150, 100, 100));
            eventText.setShadow(Color.black, 3);

            start += 30;

            datesText = new BText(this, BORDER_LEFT, start, BODY_WIDTH, 20);
            datesText.setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP | RSRC_TEXT_WRAP);
            datesText.setFont("default-18-bold.font");
            eventText.setColor(new Color(150, 100, 100));
            datesText.setShadow(true);

            start += 25;

            scrollText = new ScrollText(this, BORDER_LEFT, start, BODY_WIDTH - 20, getHeight() - 2
                    * SAFE_TITLE_V - 193, "");
            scrollText.setVisible(false);

            //setFocusDefault(scrollText);

            /*
             * list = new WeatherList(this.getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
             * .round((getWidth() - (SAFE_TITLE_H * 2)) / 2), 90, 35); list.add("Press SELECT to go back");
             * setFocusDefault(list);
             */

            updateText();
        }

        public void updateText() {
            Iterator iterator = mWeatherData.getAlerts();
            if (iterator.hasNext()) {
                WeatherData.Alert alert = (WeatherData.Alert) iterator.next();

                eventText.setValue(alert.getEvent() != null ? alert.getEvent() : alert.getHeadline());
                if (alert.getEffective() != null)
                    datesText.setValue(mDateFormat.format(alert.getEffective()) + " to "
                            + mDateFormat.format(alert.getExpires()));
                scrollText.setText(alert.getDescription());
                scrollText.setVisible(true);
                scrollText.flush();
            }
        }
        
        public void setVisible(boolean visible)
        {
       		scrollText.setVisible(visible);
        	super.setVisible(visible);
        }

        public boolean handleKeyPress(int code, long rawcode) {
            switch (code) {
            case KEY_LEFT:
            case KEY_SELECT:
                postEvent(new BEvent.Action(this, "pop"));
                return true;
            case KEY_UP:
            case KEY_DOWN:
                return scrollText.handleKeyPress(code, rawcode);
            }
            return super.handleKeyPress(code, rawcode);
        }

        private BText eventText;

        private BText datesText;

        private ScrollText scrollText;

        private SimpleDateFormat mDateFormat;

        WeatherData mWeatherData;
    }

    class WeatherList extends DefaultOptionList {
        public WeatherList(BView parent, int x, int y, int width, int height, int rowHeight) {
            super(parent, x, y, width, height, rowHeight);

            setBarAndArrows(BAR_HANG, BAR_DEFAULT, H_LEFT, null);
        }

        public boolean handleKeyPress(int code, long rawcode) {
            switch (code) {
            case KEY_LEFT:
                getBApp().play("select.snd");
                getBApp().flush();
                postEvent(new BEvent.Action(this, "pop"));
                remove();
                return true;
            }
            return super.handleKeyPress(code, rawcode);
        }
    }

    public static class WeatherFactory extends AppFactory {
        WeatherData weatherData = null;

        public void initialize() {
            WeatherConfiguration weatherConfiguration = (WeatherConfiguration) getAppContext().getConfiguration();
        }
        
        public InputStream getStream(String uri) throws IOException {
        	if (uri.toLowerCase().equals("icon.png")) {
        		if (getWeatherData().hasAlerts()) 
        			return super.getStream("alerticon.png");
            }

            return super.getStream(uri);
        }

        public WeatherData getWeatherData() {
            if (weatherData==null)
            {
            	WeatherConfiguration weatherConfiguration = (WeatherConfiguration) getAppContext().getConfiguration();
                weatherData = new WeatherData(weatherConfiguration.getId(), weatherConfiguration.getCity(),
                        weatherConfiguration.getState(), weatherConfiguration.getZip(), 512, 384); // TODO get real
            }
        	return weatherData;
        }
    }

    private static String pad(String value) {
        if (value.length() == 0)
            return "00";
        if (value.length() == 1)
            return "0" + value;
        return value;
    }
}