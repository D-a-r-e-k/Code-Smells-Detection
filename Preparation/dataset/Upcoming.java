package org.lnicholls.galleon.apps.upcoming;

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
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppContext;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.ReloadCallback;
import org.lnicholls.galleon.util.ReloadTask;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.util.UpcomingServices;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultMenuScreen;
import org.lnicholls.galleon.widget.DefaultScreen;
import org.lnicholls.galleon.widget.ScrollText;
import org.lnicholls.galleon.widget.DefaultApplication.Tracker;

import com.socialistsoftware.upcoming.Event;
import com.socialistsoftware.upcoming.Venue;
import com.tivo.hme.bananas.BButton;
import com.tivo.hme.bananas.BEvent;
import com.tivo.hme.bananas.BKeyboard;
import com.tivo.hme.bananas.BList;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.bananas.BKeyboard.KeyboardEvent;
import com.tivo.hme.interfaces.IContext;
import com.tivo.hme.sdk.HmeEvent;
import com.tivo.hme.sdk.IHmeProtocol;
import com.tivo.hme.sdk.Resource;

public class Upcoming extends DefaultApplication {

	private static Logger log = Logger.getLogger(Upcoming.class.getName());

	public final static String TITLE = "Upcoming";

	private Resource mMenuBackground;

	private Resource mInfoBackground;

	private Resource mViewerBackground;

	private Resource mFolderIcon;

	private Resource mItemIcon;

	public void init(IContext context) throws Exception {
		super.init(context);

		mMenuBackground = getSkinImage("menu", "background");
		mInfoBackground = getSkinImage("info", "background");
		mViewerBackground = getSkinImage("viewer", "background");
		mFolderIcon = getSkinImage("menu", "folder");
		mItemIcon = getSkinImage("menu", "item");

		push(new UpcomingMenuScreen(this), TRANSITION_NONE);

		initialize();
	}

	public class UpcomingMenuScreen extends DefaultMenuScreen {
		public UpcomingMenuScreen(Upcoming app) {
			super(app, "Upcoming");

			getBelow().setResource(mMenuBackground);

			mMenuList.add("Metro Events");
			mMenuList.add("Metro Venues");
			mMenuList.add("Metro Search");
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				UpcomingConfiguration upcomingConfiguration = (UpcomingConfiguration) ((UpcomingFactory) getFactory())
						.getAppContext().getConfiguration();
				if (mMenuList.getFocus() == 0) {
					load();
					if (upcomingConfiguration.getLocations().size() > 1) {
						UpcomingEventsLocationsMenuScreen upcomingEventsLocationsMenuScreen = new UpcomingEventsLocationsMenuScreen(
								(Upcoming) getBApp(), upcomingConfiguration.getLocations());
						getBApp().push(upcomingEventsLocationsMenuScreen, TRANSITION_LEFT);
						getBApp().flush();
						return true;
					} else {
						UpcomingEventsMenuScreen upcomingEventsMenuScreen = new UpcomingEventsMenuScreen(
								(Upcoming) getBApp(), (UpcomingConfiguration.Location) upcomingConfiguration
										.getLocations().get(0));
						getBApp().push(upcomingEventsMenuScreen, TRANSITION_LEFT);
						getBApp().flush();
						return true;
					}
				} else if (mMenuList.getFocus() == 1) {
					load();
					if (upcomingConfiguration.getLocations().size() > 1) {
						UpcomingVenuesLocationsMenuScreen upcomingVenuesLocationsMenuScreen = new UpcomingVenuesLocationsMenuScreen(
								(Upcoming) getBApp(), upcomingConfiguration.getLocations());
						getBApp().push(upcomingVenuesLocationsMenuScreen, TRANSITION_LEFT);
						getBApp().flush();
						return true;
					} else {
						UpcomingVenuesMenuScreen upcomingVenuesMenuScreen = new UpcomingVenuesMenuScreen(
								(Upcoming) getBApp(), (UpcomingConfiguration.Location) upcomingConfiguration
										.getLocations().get(0));
						getBApp().push(upcomingVenuesMenuScreen, TRANSITION_LEFT);
						getBApp().flush();
						return true;
					}
				} else {
					UpcomingSearchScreen upcomingSearchScreen = new UpcomingSearchScreen((Upcoming) getBApp());
					getBApp().push(upcomingSearchScreen, TRANSITION_LEFT);
					getBApp().flush();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 10, 3, 30, 30);
			icon.setResource(mFolderIcon);

			String menuName = (String) mMenuList.get(index);
			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(menuName);
		}
	}

	public class UpcomingEventsLocationsMenuScreen extends DefaultMenuScreen {
		public UpcomingEventsLocationsMenuScreen(Upcoming app, List list) {
			super(app, "Metros");

			mList = list;

			getBelow().setResource(mMenuBackground);

			for (int i = 0; i < list.size(); i++) {
				UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) list.get(i);
				mMenuList.add(location);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) mMenuList.get(mMenuList
							.getFocus());

					UpcomingEventsMenuScreen upcomingEventsMenuScreen = new UpcomingEventsMenuScreen(
							(Upcoming) getBApp(), location);
					getBApp().push(upcomingEventsMenuScreen, TRANSITION_LEFT);
					getBApp().flush();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 10, 3, 30, 30);
			icon.setResource(mFolderIcon);

			UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) mMenuList.get(index);
			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(location.getMetro(), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		private List mList;
	}

	public class UpcomingEventsMenuScreen extends DefaultMenuScreen {
		public UpcomingEventsMenuScreen(Upcoming app, UpcomingConfiguration.Location location) {
			super(app, "Metro Events");

			mLocation = location;

			mDateFormat = new SimpleDateFormat();
			mDateFormat.applyPattern("EEE M/dd");

			getBelow().setResource(mMenuBackground);

			String metroId = UpcomingServices.getMetroId(location.getCountry(), location.getState(), location
					.getMetro());

			log.debug(metroId);

			if (metroId != null) {
				mEvents = UpcomingServices.getEvents("", "", metroId);

				for (int i = 0; i < mEvents.size(); i++) {
					Event event = (Event) mEvents.get(i);
					mMenuList.add(event);
				}
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					Event event = (Event) mMenuList.get(mMenuList.getFocus());

					mEventsTracker = new Tracker(mEvents, mMenuList.getFocus());

					UpcomingEventsScreen upcomingEventsScreen = new UpcomingEventsScreen((Upcoming) getBApp(),
							mEventsTracker);
					getBApp().push(upcomingEventsScreen, TRANSITION_LEFT);
					getBApp().flush();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			Event event = (Event) mMenuList.get(index);
			BView icon = new BView(parent, 10, 3, 30, 30);
			icon.setResource(mItemIcon);

			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFont("default-24.font");
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(event.getName(), 29));

			BText date = new BText(parent, parent.getWidth() - 120 - parent.getHeight(), 4, 120, parent.getHeight() - 4);
			date.setShadow(true);
			date.setFlags(RSRC_HALIGN_RIGHT);

			try {
				if (event.getStartDate() != null && event.getStartDate().trim().length() > 0
						&& !event.getStartDate().equals("0000-00-00")) {
					String fixed = event.getStartDate().replaceAll("\"","");					Date startDate = mEventDateFormat.parse(fixed);
					date.setValue(mDateFormat.format(startDate));
				}
				if (event.getEndDate() != null && event.getEndDate().trim().length() > 0
						&& !event.getEndDate().equals("0000-00-00")) {
					String fixed = event.getEndDate().replaceAll("\"","");					Date endDate = mEventDateFormat.parse(fixed);
					date.setValue(mDateFormat.format(endDate));
				}
			} catch (Exception ex) {
				Tools.logException(Upcoming.class, ex, "Could not display date: " + event);
			}
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			if (mEventsTracker != null) {
				mFocus = mEventsTracker.getPos();
				mEventsTracker = (Tracker)mEventsTracker.clone();
			}
			return super.handleEnter(arg, isReturn);
		}

		private UpcomingConfiguration.Location mLocation;

		private SimpleDateFormat mDateFormat = new SimpleDateFormat();

		private List mEvents;

		private Tracker mEventsTracker;
	}

	public class UpcomingEventsScreen extends DefaultScreen {

		public UpcomingEventsScreen(Upcoming app, Tracker tracker) {
			super(app, true);

			mTracker = tracker;

			getBelow().setResource(mInfoBackground);

			mDisplayDateFormat.applyPattern("EEE, MMM d, ''yy"); // Thursday,
			// October
			// 27, 2005
			mDisplayDateFormat.applyPattern("EEE, MMM d");
			mDisplayTimeFormat.applyPattern("h:mm a"); // 7:00 PM

			int start = TOP;

			mDate = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH - 40, 30);
			mDate.setFlags(RSRC_HALIGN_LEFT | RSRC_VALIGN_TOP);
			mDate.setFont("system-25.font");
			mDate.setShadow(true);

			start += 30;

			mVenue = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 20, true);
			mVenue.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mVenue.setFont("default-18-bold.font");
			mVenue.setShadow(true);
			mVenue.setColor(Tools.darken(Color.WHITE));

			start += 20;

			mAddress = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 20, true);
			mAddress.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mAddress.setFont("default-18-bold.font");
			mAddress.setShadow(true);
			mAddress.setColor(Tools.darken(Color.WHITE));

			start += 20;

			mTel = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 20, true);
			mTel.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mTel.setFont("default-18-bold.font");
			mTel.setShadow(true);
			mTel.setColor(Tools.darken(Color.WHITE));

			start += 30;

			mScrollText = new ScrollText(getNormal(), BORDER_LEFT, start, BODY_WIDTH - 25, getHeight() - 2
					* SAFE_TITLE_V - 175 - 80, "");

			setFooter("upcoming.org");

			BButton button = new BButton(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
					.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.5), 35);
			button.setResource(createText("default-24.font", Color.white, "Return"));
			button.setBarAndArrows(BAR_HANG, BAR_DEFAULT, "pop", null, null, null, true);
			setFocus(button);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);
			updateView();

			return super.handleEnter(arg, isReturn);
		}

		private void updateView() {
			Event event = (Event) mTracker.getList().get(mTracker.getPos());
			setSmallTitle(Tools.cleanHTML(event.getName()));

			try {
				String displayDate = "";
				if (event.getStartDate() != null && event.getStartDate().trim().length() > 0
						&& !event.getStartDate().equals("0000-00-00")) {
					Date date = mEventDateFormat.parse(event.getStartDate());
					displayDate = mDisplayDateFormat.format(date);
				}
				if (event.getEndDate() != null && event.getEndDate().trim().length() > 0
						&& !event.getEndDate().equals("0000-00-00")) {
					Date date = mEventDateFormat.parse(event.getEndDate());
					displayDate = mDisplayDateFormat.format(date);
				}
				String displayTime = "";
				if (event.getStartTime() != null && event.getStartTime().trim().length() > 0
						&& !event.getStartTime().equals("00:00:00")) {
					Date date = mEventTimeFormat.parse(event.getStartTime());
					displayTime = displayTime + mDisplayTimeFormat.format(date);
				}
				if (event.getEndTime() != null && event.getEndTime().trim().length() > 0
						&& !event.getEndTime().equals("00:00:00")) {
					Date date = mEventTimeFormat.parse(event.getEndTime());
					if (displayTime.length() > 0)
						displayTime = displayTime + " - ";
					displayTime = displayTime + mDisplayTimeFormat.format(date);
				}
				if (displayTime.length() > 0) {
					displayTime = "(" + displayTime + ")";
					mDate.setValue(displayDate + " " + displayTime);
				} else
					mDate.setValue(displayDate);
			} catch (Exception ex) {
				Tools.logException(Upcoming.class, ex, "Could not display date: " + event);
			}

			if (event.getVenueId() != null && event.getVenueId().trim().length() > 0) {
				List venues = UpcomingServices.getVenueInfo(event.getVenueId());
				if (venues != null && venues.size() > 0) {
					Venue venue = (Venue) venues.get(0);
					mVenue.setValue(venue.getName());
					if (venue.getAddress() != null && venue.getAddress().trim().length() > 0) {
						String address = venue.getAddress();
						if (venue.getCity() != null && venue.getCity().trim().length() > 0) {
							address = address + ", " + venue.getCity();
							if (venue.getZip() != null && venue.getZip().trim().length() > 0) {
								address = address + ", " + venue.getZip();
							}
						}
						mAddress.setValue(address);
					}
					mTel.setValue(venue.getPhone());
				}
			}

			mScrollText.setText(Tools.cleanHTML(event.getDescription()));
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
				updateView();
				return true;
			case KEY_CHANNELDOWN:
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				updateView();
				return true;
			case KEY_UP:
			case KEY_DOWN:
				return mScrollText.handleKeyPress(code, rawcode);
			}
			return super.handleKeyPress(code, rawcode);
		}

		public void getNextPos() {
			if (mTracker != null) {
				int pos = mTracker.getNextPos();
			}
		}

		public void getPrevPos() {
			if (mTracker != null) {
				int pos = mTracker.getPrevPos();
			}
		}

		private BList mList;

		private BText mDate;

		private BText mVenue;

		private BText mAddress;

		private BText mTel;

		private ScrollText mScrollText;

		private Tracker mTracker;

		private SimpleDateFormat mDisplayDateFormat = new SimpleDateFormat();

		private SimpleDateFormat mDisplayTimeFormat = new SimpleDateFormat();
	}

	// //////////////////////////

	public class UpcomingVenuesLocationsMenuScreen extends DefaultMenuScreen {
		public UpcomingVenuesLocationsMenuScreen(Upcoming app, List list) {
			super(app, "Metros");

			mList = list;

			getBelow().setResource(mMenuBackground);

			for (int i = 0; i < list.size(); i++) {
				UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) list.get(i);
				mMenuList.add(location);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) mMenuList.get(mMenuList
							.getFocus());

					UpcomingVenuesMenuScreen upcomingVenuesMenuScreen = new UpcomingVenuesMenuScreen(
							(Upcoming) getBApp(), location);
					getBApp().push(upcomingVenuesMenuScreen, TRANSITION_LEFT);
					getBApp().flush();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 10, 3, 30, 30);
			icon.setResource(mFolderIcon);

			UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) mMenuList.get(index);
			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(location.getMetro(), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		private List mList;
	}

	public class UpcomingVenuesMenuScreen extends DefaultMenuScreen {
		public UpcomingVenuesMenuScreen(Upcoming app, UpcomingConfiguration.Location location) {
			super(app, "Metro Venues");

			mLocation = location;

			getBelow().setResource(mMenuBackground);

			String metroId = UpcomingServices.getMetroId(location.getCountry(), location.getState(), location
					.getMetro());

			log.debug(metroId);

			if (metroId != null) {
				mVenues = UpcomingServices.getVenues(metroId);

				Venue[] sortArray = (Venue[]) mVenues.toArray(new Venue[0]);
				Arrays.sort(sortArray, new Comparator() {
					public int compare(Object o1, Object o2) {
						Venue venue1 = (Venue) o1;
						Venue venue2 = (Venue) o2;

						// TODO Handle The...
						return venue1.getName().toLowerCase().compareTo(venue2.getName().toLowerCase());
					}
				});

				mVenues = new ArrayList();
				for (int i = 0; i < sortArray.length; i++) {
					Venue venue = (Venue) sortArray[i];
					mVenues.add(venue);
				}

				for (int i = 0; i < mVenues.size(); i++) {
					Venue venue = (Venue) mVenues.get(i);
					mMenuList.add(venue);
				}
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					Venue venue = (Venue) mMenuList.get(mMenuList.getFocus());

					mVenuesTracker = new Tracker(mVenues, mMenuList.getFocus());

					UpcomingVenuesScreen upcomingVenuesScreen = new UpcomingVenuesScreen((Upcoming) getBApp(),
							mVenuesTracker);
					getBApp().push(upcomingVenuesScreen, TRANSITION_LEFT);
					getBApp().flush();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 10, 3, 30, 30);
			icon.setResource(mFolderIcon);

			Venue venue = (Venue) mMenuList.get(index);
			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(venue.getName(), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			if (mVenuesTracker != null) {
				mFocus = mVenuesTracker.getPos();
				mVenuesTracker = (Tracker)mVenuesTracker.clone();
			}
			return super.handleEnter(arg, isReturn);
		}

		private UpcomingConfiguration.Location mLocation;

		private List mVenues;

		private Tracker mVenuesTracker;
	}

	public class UpcomingVenuesScreen extends DefaultScreen {

		public UpcomingVenuesScreen(Upcoming app, Tracker tracker) {
			super(app, true);

			setTitle("Venue");

			mTracker = tracker;

			getBelow().setResource(mInfoBackground);

			int start = TOP;

			mVenue = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH - 40, 30);
			mVenue.setFlags(RSRC_HALIGN_LEFT | RSRC_VALIGN_TOP);
			mVenue.setFont("system-25.font");
			mVenue.setShadow(true);

			start += 30;

			mAddress = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 20, true);
			mAddress.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mAddress.setFont("default-18-bold.font");
			mAddress.setShadow(true);
			mAddress.setColor(Tools.darken(Color.WHITE));

			start += 20;

			mTel = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 20, true);
			mTel.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mTel.setFont("default-18-bold.font");
			mTel.setShadow(true);
			mTel.setColor(Tools.darken(Color.WHITE));

			setFooter("upcoming.org");

			BButton button = new BButton(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
					.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.5), 35);
			button.setResource(createText("default-24.font", Color.white, "Return"));
			button.setBarAndArrows(BAR_HANG, BAR_DEFAULT, "pop", null, null, null, true);
			setFocus(button);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);
			updateView();

			return super.handleEnter(arg, isReturn);
		}

		private void updateView() {
			Venue venue = (Venue) mTracker.getList().get(mTracker.getPos());
			mVenue.setValue(venue.getName());
			List venues = UpcomingServices.getVenueInfo(venue.getId());
			if (venues != null && venues.size() > 0) {
				venue = (Venue) venues.get(0);
				if (venue.getAddress() != null && venue.getAddress().trim().length() > 0) {
					String address = venue.getAddress();
					if (venue.getCity() != null && venue.getCity().trim().length() > 0) {
						address = address + ", " + venue.getCity();
						if (venue.getZip() != null && venue.getZip().trim().length() > 0) {
							address = address + ", " + venue.getZip();
						}
					}
					mAddress.setValue(address);
				}
			}
			mTel.setValue(venue.getPhone());
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
				updateView();
				return true;
			case KEY_CHANNELDOWN:
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				updateView();
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		public void getNextPos() {
			if (mTracker != null) {
				int pos = mTracker.getNextPos();
			}
		}

		public void getPrevPos() {
			if (mTracker != null) {
				int pos = mTracker.getPrevPos();
			}
		}

		private BText mVenue;

		private BText mAddress;

		private BText mTel;

		private Tracker mTracker;
	}

	public class UpcomingSearchScreen extends DefaultScreen {

		public UpcomingSearchScreen(Upcoming app) {
			super(app, false);

			setTitle("Search");

			getBelow().setResource(mInfoBackground);

			int start = TOP;

			Point p = BKeyboard.getKeyboardSize(BKeyboard.getStandardKeyboard(BKeyboard.STANDARD_KEYBOARD_LOWERCASE),
					false, BKeyboard.INPUT_WIDTH_SAME_AS_WIDGET);

			mKeyboard = new BKeyboard(getNormal(), 100, 150, p.x, p.y, BKeyboard
					.getStandardKeyboard(BKeyboard.STANDARD_KEYBOARD_LOWERCASE), false,
					BKeyboard.INPUT_WIDTH_SAME_AS_WIDGET, true) {
				public boolean handleKeyPress(int code, long rawcode) {
					mLastTime = System.currentTimeMillis();
					return super.handleKeyPress(code, rawcode);
				}
			};

			setFocus(mKeyboard);

			mEventsList = new EventsList(getNormal(), 400, 150, getWidth() - (SAFE_ACTION_H + 400), 210, 30);
			mEventsList.add("Start typing to");
			mEventsList.add("get events list");

			BButton button = new BButton(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
					.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.5), 35);
			button.setResource(createText("default-24.font", Color.white, "Return to menu"));
			button.setBarAndArrows(BAR_HANG, BAR_DEFAULT, "pop", null, null, null, true);
			// setFocus(button);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);
			updateView();

			return super.handleEnter(arg, isReturn);
		}

		private void updateView() {
		}

		public boolean handleAction(BView view, Object action) {
			if ("left".equals(action)) {
				if (view.getParent() == mEventsList) {
					setFocus(mKeyboard);
					return true;
				} else {
					getBApp().pop();
				}
			}
			return super.handleAction(view, action);
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SELECT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_CHANNELUP:
			case KEY_CHANNELDOWN:
			}
			return super.handleKeyPress(code, rawcode);
		}

		public boolean handleEvent(HmeEvent event) {
			boolean result = super.handleEvent(event);
			final long thisTime = System.currentTimeMillis();
			if (event instanceof KeyboardEvent) {
				final KeyboardEvent localEvent = (KeyboardEvent) event;
				if (mUpdateThread != null) {
					if (mUpdateThread.isAlive())
						mUpdateThread.interrupt();
				}

				mUpdateThread = new Thread() {
					public void run() {
						try {
							mBusy.setVisible(true);

							if (localEvent.getValue() != null && localEvent.getValue().length() > 0) {
								sleep(2000); // wait for the user to finish..
								if (mLastTime > thisTime)
									sleep(2000);
							}

							update(localEvent.getValue());
						} catch (Exception ex) {
						} finally {
							mBusy.setVisible(false);
							getBApp().flush();
						}
					}
				};
				mUpdateThread.start();
				mLastTime = thisTime;
			}
			return result;
		}

		public void update(String word) {
			try {
				mEventsList.clear();
				if (word == null) {
					return;
				}

				if (word.trim().length() == 0) {
					mEventsList.add("Start typing to");
					mEventsList.add("get events list");
					return;
				}

				boolean found = false;
				UpcomingConfiguration upcomingConfiguration = (UpcomingConfiguration) ((UpcomingFactory) getFactory())
						.getAppContext().getConfiguration();
				for (int i = 0; i < upcomingConfiguration.getLocations().size(); i++) {
					UpcomingConfiguration.Location location = (UpcomingConfiguration.Location) upcomingConfiguration
							.getLocations().get(i);

					String metroId = UpcomingServices.getMetroId(location.getCountry(), location.getState(), location
							.getMetro());
					if (metroId != null) {
						List list = UpcomingServices.searchEvents(word, "", "", metroId);
						if (list != null && list.size() > 0) {
							Iterator iterator = list.iterator();
							while (iterator.hasNext()) {
								Event event = (Event) iterator.next();
								mEventsList.add(event);
								if (!found) {
									if (mEvents != null)
										mEvents.clear();
									mEvents = new ArrayList();
								}
								mEvents.add(event);
								found = true;
							}
						}
					}
				}
				if (found)
					return;

				mEventsList.add("No events for");
				mEventsList.add(word);
			} finally {
				getBApp().flush();
			}
		}

		class EventsList extends BList {

			public EventsList(BView parent, int x, int y, int width, int height, int rowHeight) {
				super(parent, x, y, width, height, rowHeight);
				setBarAndArrows(BAR_DEFAULT, BAR_HANG, "left", null);
			}

			protected void createRow(BView parent, int index) {
				BText text = new BText(parent, 30, 0, parent.getWidth() - 20, parent.getHeight());
				text.setFlags(RSRC_HALIGN_LEFT);
				text.setShadow(true);
				Object object = get(index);
				if (object instanceof String)
					text.setValue(object);
				else {
					Event event = (Event) object;
					text.setValue(event.getName());
				}
			}

			public boolean handleKeyPress(int code, long rawcode) {
				switch (code) {
				case KEY_SELECT:
					Object object = mEventsList.get(mEventsList.getFocus());
					if (!(object instanceof String)) {
						Tracker tracker = new Tracker(mEvents, mEventsList.getFocus());

						UpcomingEventsScreen upcomingEventsScreen = new UpcomingEventsScreen((Upcoming) getBApp(),
								tracker);
						getBApp().push(upcomingEventsScreen, TRANSITION_LEFT);
						getBApp().flush();
						return true;
					}
				}
				return super.handleKeyPress(code, rawcode);
			}
		}

		private EventsList mEventsList;

		private BKeyboard mKeyboard;

		private long mLastTime;

		private Thread mUpdateThread;

		private List mEvents;
	}

	public static class UpcomingFactory extends AppFactory {

		public void updateAppContext(AppContext appContext) {
			super.updateAppContext(appContext);

			mEventDateFormat.applyPattern("yyyy-MM-dd"); // 2005-10-27
			mEventTimeFormat.applyPattern("HH:mm:ss"); // 21:00:00

			updateChannels();
		}

		private void updateChannels() {
			final UpcomingConfiguration upcomingConfiguration = (UpcomingConfiguration) getAppContext()
					.getConfiguration();
		}

		public void initialize() {

			Server.getServer().scheduleShortTerm(new ReloadTask(new ReloadCallback() {
				public void reload() {
					try {
						log.debug("Upcoming");
						updateChannels();
					} catch (Exception ex) {
						log.error("Could not download upcoming data", ex);
					}
				}
			}), 5);
		}
	}

	private static SimpleDateFormat mEventDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat mEventTimeFormat = new SimpleDateFormat("HH:mm:ss");
}