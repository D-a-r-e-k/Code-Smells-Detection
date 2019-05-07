package org.lnicholls.galleon.apps.traffic;

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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.database.PersistentValue;
import org.lnicholls.galleon.database.PersistentValueManager;
import org.lnicholls.galleon.media.ImageManipulator;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultMenuScreen;
import org.lnicholls.galleon.widget.DefaultScreen;

import com.tivo.hme.bananas.BEvent;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.interfaces.IContext;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.sdk.View;

public class Traffic extends DefaultApplication {

	private static Logger log = Logger.getLogger(Traffic.class.getName());

	private final static Runtime runtime = Runtime.getRuntime();

	public final static String TITLE = "Traffic";

	private Resource mMenuBackground;

	private Resource mInfoBackground;

	private Resource mFolderIcon;

	private Resource mIncidentIcon;

	private Resource mConstructionIcon;

	public void init(IContext context) throws Exception {
		super.init(context);

		mMenuBackground = getSkinImage("menu", "background");
		mInfoBackground = getSkinImage("info", "background");
		mFolderIcon = getSkinImage("menu", "folder");
		mIncidentIcon = getSkinImage("menu", "incident");
		mConstructionIcon = getSkinImage("menu", "construction");

		TrafficConfiguration trafficConfiguration = (TrafficConfiguration) ((TrafficFactory) getFactory())
				.getAppContext().getConfiguration();

		if (trafficConfiguration.getLocations().size() == 1) {
			try {
				TrafficConfiguration.Location value = (TrafficConfiguration.Location) trafficConfiguration
						.getLocations().get(0);
				TrafficMenuScreen trafficMenuScreen = new TrafficMenuScreen(this, value, true);
				push(trafficMenuScreen, TRANSITION_LEFT);
			} catch (Throwable ex) {
				Tools.logException(Traffic.class, ex);
			}
		} else
			push(new LocationMenuScreen(this), TRANSITION_NONE);

		initialize();
	}

	public class LocationMenuScreen extends DefaultMenuScreen {
		public LocationMenuScreen(Traffic app) {
			super(app, "Traffic");

			getBelow().setResource(mMenuBackground);

			TrafficConfiguration trafficConfiguration = (TrafficConfiguration) ((TrafficFactory) getFactory())
					.getAppContext().getConfiguration();

			List list = trafficConfiguration.getLocations();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				TrafficConfiguration.Location value = (TrafficConfiguration.Location) iterator.next();
				mMenuList.add(value);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push") || action.equals("play")) {
				load();
				new Thread() {
					public void run() {
						try {
							TrafficConfiguration.Location value = (TrafficConfiguration.Location) (mMenuList
									.get(mMenuList.getFocus()));

							getBApp().push(new TrafficMenuScreen((Traffic) getApp(), value), TRANSITION_LEFT);
							getBApp().flush();
						} catch (Exception ex) {
							Tools.logException(Traffic.class, ex);
						}
					}
				}.start();
				return true;
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			TrafficConfiguration.Location value = (TrafficConfiguration.Location) mMenuList.get(index);
			icon.setResource(mFolderIcon);

			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(value.getStreet() + ", " + value.getCity() + ", " + value.getState(), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_PLAY:
				postEvent(new BEvent.Action(this, "play"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}
	}

	public class TrafficMenuScreen extends DefaultMenuScreen {
		public TrafficMenuScreen(Traffic app, TrafficConfiguration.Location location) {
			this(app, location, false);
		}

		public TrafficMenuScreen(Traffic app, TrafficConfiguration.Location location, boolean first) {
			super(app, "Traffic");

			mLocation = location;
			mFirst = first;

			getBelow().setResource(mMenuBackground);

			TrafficConfiguration trafficConfiguration = (TrafficConfiguration) ((TrafficFactory) getFactory())
					.getAppContext().getConfiguration();

			mResults = getTrafficResults(location.getStreet(), location.getCity(), location.getState(), location
					.getZip(), location.getRadius());
			Iterator iterator = mResults.iterator();
			while (iterator.hasNext()) {
				Result result = (Result) iterator.next();
				mMenuList.add(result);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push") || action.equals("play")) {
				load();
				new Thread() {
					public void run() {
						try {
							mTracker = new Tracker(mResults, mMenuList.getFocus());

							getBApp().push(new SlideshowScreen((Traffic) getBApp(), mTracker), TRANSITION_LEFT);
							getBApp().flush();
						} catch (Exception ex) {
							Tools.logException(Traffic.class, ex);
						}
					}
				}.start();
				return true;
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			Result result = (Result) mMenuList.get(index);
			if (result.getType().equals("construction"))
				icon.setResource(mConstructionIcon);
			else
				icon.setResource(mIncidentIcon);

			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			String title = result.getTitle();
			// TODO Use an icon for severity
			if (result.getSeverity().equals("5"))
				title = title.toUpperCase();
			name.setValue(Tools.trim(title, 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_PLAY:
				postEvent(new BEvent.Action(this, "play"));
				return true;
			case KEY_LEFT:
				if (!mFirst) {
					postEvent(new BEvent.Action(this, "pop"));
					return true;
				}
				break;
			}
			return super.handleKeyPress(code, rawcode);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			if (mTracker != null) {
				mFocus = mTracker.getPos();
			}
			return super.handleEnter(arg, isReturn);
		}

		private TrafficConfiguration.Location mLocation;

		private List mResults;

		private Tracker mTracker;

		private boolean mFirst;
	}

	public class SlideshowScreen extends DefaultScreen {

		public SlideshowScreen(Traffic app, Tracker tracker) {
			super(app, null, null, false);

			if (!mShowDescription)
				setFooter("Press INFO for details", mAnim);

			mTracker = tracker;

			TrafficConfiguration imagesConfiguration = (TrafficConfiguration) ((TrafficFactory) getFactory())
					.getAppContext().getConfiguration();
			/*
			 * if (imagesConfiguration.isUseSafe()) mPhoto = new
			 * View(getBelow(), SAFE_ACTION_H, SAFE_ACTION_V, getWidth() - 2 *
			 * SAFE_ACTION_H, getHeight() - 2 * SAFE_ACTION_V); else
			 */
			mPhoto = new View(getBelow(), 0, 0, getWidth(), getHeight());
		}

		private void updateView() {
			try {
				setPainting(false);

				// clearImage();

				updateHints();

				new Thread() {
					public void run() {
						try {
							SlideshowScreen.this.mBusy.setVisible(true);
							getBApp().flush();

							BufferedImage photo = currentImage();

							BufferedImage scaled = ImageManipulator.getScaledImage(photo, 640, 480);

							if (scaled != null) {
								mPhoto.setResource(createImage(scaled), RSRC_IMAGE_BESTFIT);
								mPhoto.setVisible(true);
								//mPhoto.setTransparency(1);
								//mPhoto.setTransparency(0, mAnim);
								scaled.flush();
								scaled = null;
							}

							showDescription();
						} catch (Throwable ex) {
							Tools.logException(Traffic.class, ex, "Could not retrieve image");
						} finally {
							SlideshowScreen.this.mBusy.setVisible(false);
							getBApp().flush();
						}
					}
				}.start();

			} finally {
				setPainting(true);
			}
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			updateView();

			return super.handleEnter(arg, isReturn);
		}

		private void clearImage() {
			Image image = currentImage();
			if (image != null) {
				mPhoto.setVisible(false);
				if (mPhoto.getResource() != null)				{
					mPhoto.getResource().flush();					mPhoto.getResource().remove();				}
				getBApp().flush();
			}
		}

		public boolean handleExit() {
			try {
				setPainting(false);
				clearImage();
			} finally {
				setPainting(true);
			}
			return super.handleExit();
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_UP:
				code = KEY_CHANNELUP;
				getApp().handleKeyPress(code, rawcode);
				return true;
			case KEY_DOWN:
				code = KEY_CHANNELDOWN;
				getApp().handleKeyPress(code, rawcode);
				return true;
			case KEY_CHANNELUP:
				getBApp().play("pageup.snd");
				getBApp().flush();
				getPrevPos();
				updateView();
				return true;
			case KEY_CHANNELDOWN:
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				updateView();
				return true;
			case KEY_SELECT:
			case KEY_RIGHT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_INFO:
			case KEY_NUM0:
				mShowDescription = !mShowDescription;
				showDescription();
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		public void getNextPos() {
			if (mTracker != null && mTracker.getList().size() > 0) {
				int pos = mTracker.getNextPos();
			}
		}

		public void getPrevPos() {
			if (mTracker != null && mTracker.getList().size() > 0) {
				int pos = mTracker.getPrevPos();
			}
		}

		private BufferedImage currentImage() {
			if (mTracker != null && mTracker.getList().size() > 0) {
				try {
					Result result = (Result) mTracker.getList().get(mTracker.getPos());
					if (result != null) {
						return (BufferedImage) Tools.getImage(new URL(result.getMap()), -1, -1);
					}
				} catch (Exception ex) {
					Tools.logException(Traffic.class, ex);
				}
			}
			return null;
		}

		private String currentDescription() {
			if (mTracker != null && mTracker.getList().size() > 0) {
				try {
					Result result = (Result) mTracker.getList().get(mTracker.getPos());
					if (result != null) {
						return result.getDescription();
					}
				} catch (Exception ex) {
					Tools.logException(Traffic.class, ex);
				}
			}
			return null;
		}

		private String currentDirection() {
			if (mTracker != null && mTracker.getList().size() > 0) {
				try {
					Result result = (Result) mTracker.getList().get(mTracker.getPos());
					if (result != null) {
						return result.getDirection();
					}
				} catch (Exception ex) {
					Tools.logException(Traffic.class, ex);
				}
			}
			return null;
		}

		private String currentTitle() {
			if (mTracker != null && mTracker.getList().size() > 0) {
				try {
					Result result = (Result) mTracker.getList().get(mTracker.getPos());
					if (result != null) {
						return result.getTitle();
					}
				} catch (Exception ex) {
					Tools.logException(Traffic.class, ex);
				}
			}
			return null;
		}

		public void showDescription() {
			if (mShowDescription) {
				String value = currentDescription();
				if (value != null) {
					try {
						setPainting(false);
						if (mDescription == null) {
							mDescriptionBackground = new View(getNormal(), SAFE_TITLE_H, getHeight() - SAFE_TITLE_V
									- 60, (getWidth() - (SAFE_TITLE_H * 2)), 75);
							mDescriptionBackground.setResource(Color.BLUE);
							mDescriptionBackground.setTransparency(0.4f);

							mDescription = new BText(getNormal(), SAFE_TITLE_H, getHeight() - SAFE_TITLE_V - 60,
									(getWidth() - (SAFE_TITLE_H * 2)), 75);
							mDescription.setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP | RSRC_TEXT_WRAP);
							mDescription.setFont("default-18.font");
							mDescription.setShadow(true);
						}

						if (value.length() == 0) {
							mDescription.setVisible(false);
							mDescriptionBackground.setVisible(false);
						} else {
							mDescription.setValue(value);
							mDescription.setVisible(true);
							mDescriptionBackground.setVisible(true);
						}
					} finally {
						setPainting(true);
					}
				}
				value = currentTitle();
				if (value != null) {
					try {
						setPainting(false);
						if (mTitle == null) {
							mTitleBackground = new View(getNormal(), SAFE_TITLE_H, SAFE_TITLE_V,
									(getWidth() - (SAFE_TITLE_H * 2)), 75);
							mTitleBackground.setResource(Color.BLUE);
							mTitleBackground.setTransparency(0.4f);

							mTitle = new BText(getNormal(), SAFE_TITLE_H, SAFE_TITLE_V,
									(getWidth() - (SAFE_TITLE_H * 2)), 75);
							mTitle.setFlags(RSRC_HALIGN_CENTER | RSRC_VALIGN_TOP | RSRC_TEXT_WRAP);
							mTitle.setFont("default-18.font");
							mTitle.setShadow(true);
						}

						if (value.length() == 0) {
							mTitle.setVisible(false);
							mTitleBackground.setVisible(false);
						} else {
							String direction = currentDirection();
							if (!direction.equals("N/A"))
								value = value + " (" + direction + ")";
							mTitle.setValue(value);
							mTitle.setVisible(true);
							mTitleBackground.setVisible(true);
						}
					} finally {
						setPainting(true);
					}
				}
			} else {
				if (mDescription != null) {
					mDescription.setVisible(false);
					mDescriptionBackground.setVisible(false);
				}

				if (mTitle != null) {
					mTitle.setVisible(false);
					mTitleBackground.setVisible(false);
				}
			}
		}

		private Resource mAnim = getResource("*2000");

		private BText mTitle;

		private View mTitleBackground;

		private BText mDescription;

		private View mDescriptionBackground;

		private Image mImage;

		private View mPhoto;

		private Tracker mTracker;
	}

	static class Result {
		public Result(String type, String title, String description, String severity, String direction, String map) {
			mType = type;
			mTitle = title;
			mDescription = description;
			mSeverity = severity;
			mDirection = direction;
			mMap = map;
		}

		public String getType() {
			return mType;
		}

		public String getTitle() {
			return mTitle;
		}

		public String getDescription() {
			return mDescription;
		}

		public String getSeverity() {
			return mSeverity;
		}

		public String getDirection() {
			return mDirection;
		}

		public String getMap() {
			return mMap;
		}

		private String mType;

		private String mTitle;

		private String mDescription;

		private String mSeverity;

		private String mDirection;

		private String mMap;
	}

	public static List getTrafficResults(String street, String city, String state, String zip, String radius) {
		List trafficResults = new ArrayList();

		// http://api.local.yahoo.com/MapsService/V1/trafficData?appid=YahooDemo&street=701+First+Street&city=Sunnyvale&state=CA&include_map=1
		String url = "http://api.local.yahoo.com/MapsService/V1/trafficData?appid=" + "galleonhme" + "&street="
				+ URLEncoder.encode(street) + "&city=" + URLEncoder.encode(city) + "&state=" + URLEncoder.encode(state)
				+ "&radius=" + URLEncoder.encode(radius) + "&include_map=1&image_width=640&image_height=480";

		log.info(url);

		PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Traffic.class.getName() + "."
				+ url);
		String content = persistentValue == null ? null : persistentValue.getValue();
		if (PersistentValueManager.isAged(persistentValue)) {
			try {
				String page = Tools.getPage(new URL(url));
				if (page != null && page.length() > 0)
					content = page;
			} catch (Exception ex) {
				Tools.logException(Traffic.class, ex, "Could not cache traffic: " + url);
			}
		}

		if (content != null) {
			try {
				SAXReader saxReader = new SAXReader();
				StringReader stringReader = new StringReader(content);
				// Document document = saxReader.read(new
				// File("d:/galleon/itunes2.rss.xml"));
				Document document = saxReader.read(stringReader);
				stringReader.close();
				stringReader = null;

				Element root = document.getRootElement(); // check for errors
				if (root != null && root.getName().equals("ResultSet")) {
					for (Iterator i = root.elementIterator("Result"); i.hasNext();) {
						Element result = (Element) i.next();

						String value = null;
						String type = null;
						String title = null;
						String description = null;
						String severity = null;
						String direction = null;
						String map = null;

						if ((value = Tools.getAttribute(result, "type")) != null) {
							type = value;
						}

						if ((value = Tools.getAttribute(result, "Title")) != null) {
							title = value;
						}

						if ((value = Tools.getAttribute(result, "Description")) != null) {
							description = Tools.cleanHTML(value);
						}

						if ((value = Tools.getAttribute(result, "Severity")) != null) {
							severity = Tools.cleanHTML(value);
						}

						if ((value = Tools.getAttribute(result, "Direction")) != null) {
							direction = Tools.cleanHTML(value);
						}

						if ((value = Tools.getAttribute(result, "ImageUrl")) != null) {
							map = Tools.cleanHTML(value);
						}

						if ((value = Tools.getAttribute(result, "EndDate")) != null) {
							try {
								long time = Long.parseLong(value) * 1000;
								Date date = new Date(time);
								log.info(date);
								if (date.after(new Date()))
									trafficResults.add(new Result(type, title, description, severity, direction, map));
							} catch (Exception ex) {
								Tools.logException(Traffic.class, ex);
							}
						} else
							trafficResults.add(new Result(type, title, description, severity, direction, map));
					}
				}
				document.clearContent();
				document = null;

				if (PersistentValueManager.isAged(persistentValue)) {
					PersistentValueManager.savePersistentValue(Traffic.class.getName() + "." + url, content, 60);
				}
			} catch (Exception ex) {
				Tools.logException(Traffic.class, ex, "Could not download traffic: " + url);
				return null;
			}
		}
		return trafficResults;
	}

	public static class TrafficFactory extends AppFactory {

		public void initialize() {
			TrafficConfiguration trafficConfiguration = (TrafficConfiguration) getAppContext().getConfiguration();
		}
	}

	private boolean mShowDescription = true;
}