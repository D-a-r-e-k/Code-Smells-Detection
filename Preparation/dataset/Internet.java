package org.lnicholls.galleon.apps.internet;

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
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lnicholls.galleon.app.AppContext;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.apps.internet.InternetConfiguration.SharedUrl;
import org.lnicholls.galleon.apps.rss.RSSConfiguration;
import org.lnicholls.galleon.media.ImageManipulator;
import org.lnicholls.galleon.server.DataConfiguration;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.FileFilters;
import org.lnicholls.galleon.util.FileSystemContainer;
import org.lnicholls.galleon.util.NameValue;
import org.lnicholls.galleon.util.ReloadCallback;
import org.lnicholls.galleon.util.ReloadTask;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.util.FileSystemContainer.FileItem;
import org.lnicholls.galleon.util.FileSystemContainer.FolderItem;
import org.lnicholls.galleon.util.FileSystemContainer.Item;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultList;
import org.lnicholls.galleon.widget.DefaultMenuScreen;
import org.lnicholls.galleon.widget.DefaultOptionsScreen;
import org.lnicholls.galleon.widget.DefaultScreen;
import org.lnicholls.galleon.widget.*;
import org.lnicholls.galleon.widget.OptionsButton;
import org.lnicholls.galleon.widget.DefaultApplication.Tracker;
import org.lnicholls.galleon.widget.DefaultApplication.VersionScreen;
import org.lnicholls.galleon.data.*;
import org.lnicholls.galleon.database.Application;

import com.tivo.hme.bananas.BEvent;
import com.tivo.hme.bananas.BHighlights;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.interfaces.IContext;
import com.tivo.hme.sdk.Resource;

public class Internet extends DefaultApplication {

	private static Logger log = Logger.getLogger(Internet.class.getName());

	private final static Runtime runtime = Runtime.getRuntime();

	public final static String TITLE = "Internet";
	
	private Resource mMenuBackground;

	private Resource mInfoBackground;

	private Resource mFolderIcon;

	private Resource mLargeFolderIcon;

	private Resource mItemIcon;

	public void init(IContext context) throws Exception {
		super.init(context);

		mMenuBackground = getSkinImage("menu", "background");
		mInfoBackground = getSkinImage("info", "background");
		mFolderIcon = getSkinImage("menu", "folder");
		mLargeFolderIcon = getSkinImage("menu", "gridFolder");
		mItemIcon = getSkinImage("menu", "item");

		InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory())
				.getAppContext().getConfiguration();

		Tracker tracker = new Tracker(internetConfiguration.getSharedUrls(), 0);
		
		if (Server.getServer().getDataConfiguration().isConfigured())
			push(new InternetMenuScreen(this), TRANSITION_NONE);
		else
			push(new FavoritesMenuScreen(this, tracker, true), TRANSITION_NONE);
		
		initialize();
	}
	
	public class InternetMenuScreen extends DefaultMenuScreen {
		public InternetMenuScreen(Internet app) {
			super(app, "Internet");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);

			mMenuList.add("My Favorites");
			mMenuList.add("Find by Tag");
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					
					final InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory()).getAppContext().getConfiguration();

					new Thread() {
						public void run() {
							try {
								if (mMenuList.getFocus()==0)
								{
									Tracker tracker = new Tracker(internetConfiguration.getSharedUrls(), 0);
									getBApp().push(new FavoritesMenuScreen((Internet)getBApp(), tracker), TRANSITION_NONE);
								}
								else
									getBApp().push(new TagsMenuScreen((Internet)getBApp()), TRANSITION_NONE);
								getBApp().flush();
								
							} catch (Exception ex) {
								Tools.logException(Internet.class, ex);
							}
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			try
			{
				BView icon = new BView(parent, 9, 2, 32, 32);
				icon.setResource(mFolderIcon);
				String title = (String) mMenuList.get(index);

				BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
				name.setShadow(true);
				name.setFlags(RSRC_HALIGN_LEFT);
				name.setValue(Tools.trim(Tools.clean(title), 40));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SELECT:
				postEvent(new BEvent.Action(this, "push"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Internet) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}
	}	

	public class OptionsScreen extends DefaultOptionsScreen {

		public OptionsScreen(DefaultApplication app) {
			super(app);

			getBelow().setResource(mInfoBackground);

			InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory())
					.getAppContext().getConfiguration();

			int start = TOP;
			int width = 280;
			int increment = 37;
			int height = 25;
			BText text = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			text.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_CENTER);
			text.setFont("default-24-bold.font");
			text.setShadow(true);
			text.setValue("Reload");

			NameValue[] nameValues = new NameValue[] { new NameValue("5 minutes", "5"),
					new NameValue("10 minutes", "10"), new NameValue("20 minutes", "20"),
					new NameValue("30 minutes", "30"), new NameValue("1 hour", "60"), new NameValue("2 hours", "120"),
					new NameValue("4 hours", "240"), new NameValue("6 hours", "720"),
					new NameValue("10 minutes", "10"), new NameValue("24 hours", "1440") };
			mReloadButton = new OptionsButton(getNormal(), BORDER_LEFT + BODY_WIDTH - width, start, width, height,
					true, nameValues, String.valueOf(internetConfiguration.getReload()));

			start = start + increment;

			text = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			text.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_CENTER);
			text.setFont("default-24-bold.font");
			text.setShadow(true);
			text.setValue("Sort");
			nameValues = new NameValue[] { new NameValue("Yes", "true"), new NameValue("No", "false") };
			mSortedButton = new OptionsButton(getNormal(), BORDER_LEFT + BODY_WIDTH - width, start, width, height,
					true, nameValues, String.valueOf(internetConfiguration.isSorted()));
			
			
			setFocusDefault(mReloadButton);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);

			return super.handleEnter(arg, isReturn);
		}

		public boolean handleExit() {

			try {
				DefaultApplication application = (DefaultApplication)getApp();
				if (!application.isDemoMode())
				{
					InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory())
							.getAppContext().getConfiguration();
					internetConfiguration.setReload(Integer.parseInt(mReloadButton.getValue()));
					internetConfiguration.setSorted(Boolean.valueOf(mSortedButton.getValue()).booleanValue());
	
					Server.getServer().updateApp(((InternetFactory) getFactory()).getAppContext());
				}
			} catch (Exception ex) {
				Tools.logException(Internet.class, ex, "Could not configure internet app");
			}
			return super.handleExit();
		}

		private OptionsButton mReloadButton;
		
		private OptionsButton mSortedButton;
	}
	
	public class FavoritesMenuScreen extends DefaultMenuScreen {
		public FavoritesMenuScreen(Internet app, Tracker tracker) {
			this(app, tracker, false);
		}

		public FavoritesMenuScreen(Internet app, Tracker tracker, boolean first) {
			super(app, "Internet");
			
			mTracker = tracker;
			mFirst = first;
			
			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);

			createMenu();
		}
		
		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
    		if (isReturn)
    		{
    			createMenu();
    		}
			return super.handleEnter(arg, isReturn);
		}
		
		private void createMenu()
		{
			InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory()).getAppContext().getConfiguration();
			mMenuList.clear();			
			InternetConfiguration.SharedUrl urls[] = new InternetConfiguration.SharedUrl[0];
			urls = (InternetConfiguration.SharedUrl[]) mTracker.getList().toArray(urls);
			if (internetConfiguration.isSorted())
			{
				Arrays.sort(urls, new Comparator() {
					public int compare(Object o1, Object o2) {
						InternetConfiguration.SharedUrl url1 = (InternetConfiguration.SharedUrl) o1;
						InternetConfiguration.SharedUrl url2 = (InternetConfiguration.SharedUrl) o2;
						
						return url1.getName().compareTo(url2.getName());
					}
				});
			}
			ArrayList list = new ArrayList();
			for (int i = 0; i < urls.length; i++) {
				mMenuList.add(urls[i]);
				list.add(urls[i]);
			}
			mTracker = new Tracker(list, 0);
		}
		
		public DefaultList createMenuList()
		{
			BlockList defaultList = new BlockList(this, SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 290, getWidth()
	                - ((SAFE_TITLE_H * 2) + 32), 280, 280/3) {
	                	protected void createRow(BView parent, int index) {
         			   		FavoritesMenuScreen.this.createRow(parent, index);
        				}
	                };
			return defaultList;
		}
		
		public boolean handleExit() {
			mTop = mMenuList.getTop();
			//mMenuList.clear();
			return super.handleExit();
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("play")) {
				getBApp().play("select.snd");
				getBApp().flush();

				mTop = mMenuList.getTop();

				final InternetConfiguration.SharedUrl nameValue = (InternetConfiguration.SharedUrl) mMenuList.get(mMenuList.getFocus());
				new Thread() {
					public void run() {
						try {
							ImageScreen imageScreen = new ImageScreen((Internet) getBApp());
							mTracker.setPos(mMenuList.getFocus());
							imageScreen.setTracker(mTracker);

							getBApp().push(imageScreen, TRANSITION_LEFT);
							getBApp().flush();
						} catch (Exception ex) {
							Tools.logException(Internet.class, ex);
						}
					}
				}.start();

				return true;
			}
			return super.handleAction(view, action);
		}


		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 10, 10, parent.getHeight()-20, parent.getHeight()-20);
			InternetConfiguration.SharedUrl value = (InternetConfiguration.SharedUrl) mMenuList.get(index);
			Image image = getImage(value.getValue(), false);
			if (image.getWidth(null) > 640 || image.getHeight(null) > 480)
			{
				BufferedImage scaled = ImageManipulator.getScaledImage((BufferedImage)image, icon.getWidth(), icon.getHeight());
				icon.setResource(createImage(scaled), RSRC_IMAGE_BESTFIT);
			}
			else
				icon.setResource(createImage(image), RSRC_IMAGE_BESTFIT);

			BText name = new BText(parent, parent.getHeight() + 20, 10, parent.getWidth() - 20 - parent.getHeight(), parent.getHeight() - 20);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
			name.setFont("default-18-bold.font");
			if (value.getDescription()!=null)
				name.setValue(Tools.trim(value.getName() + " : " + value.getDescription(), 120));
			else
				name.setValue(Tools.trim(value.getName(), 120));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_PLAY:
			case KEY_SELECT:
				postEvent(new BEvent.Action(this, "play"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Internet) getBApp()), TRANSITION_LEFT);
			case KEY_LEFT:
				if (!mFirst) {
					postEvent(new BEvent.Action(this, "pop"));
					return true;
				}
				break;
			case KEY_NUM1:
			case KEY_THUMBSUP:
				try
				{
					InternetConfiguration.SharedUrl value = (InternetConfiguration.SharedUrl) mMenuList.get(mMenuList.getFocus());
					
					InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory()).getAppContext().getConfiguration();
					List list = internetConfiguration.getSharedUrls();
					boolean duplicate = false;
					Iterator iterator = list.iterator();
					while (iterator.hasNext())
					{
						InternetConfiguration.SharedUrl sharedUrl = (InternetConfiguration.SharedUrl)iterator.next();
						if (sharedUrl.getValue().equals(value.getValue()))
						{
							duplicate = true;
							break;
						}
					}
					if (!duplicate)
					{
						getApp().play("thumbsup.snd");
						getApp().flush();
						mMenuList.flash();
						list.add(value);
						Server.getServer().updateApp(((InternetFactory) getFactory()).getAppContext());
					}
				}
				catch (Exception ex) {
					Tools.logException(Internet.class, ex);
				}
				break;				
			}

			return super.handleKeyPress(code, rawcode);
		}
		
		private Tracker mTracker;
		
		private boolean mFirst;

		private int mTop;		
	}	

	public class ImageScreen extends DefaultScreen {

		public ImageScreen(Internet app) {
			super(app, null, null, false);

			// getBelow().setResource(mInfoBackground);

			mImage = new BView(this, BORDER_LEFT, SAFE_TITLE_V, BODY_WIDTH, BODY_HEIGHT - 20, true);

			setFooter("Press PLAY to reload");
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			updateView(false);
			return super.handleEnter(arg, isReturn);
		}

		public boolean handleExit() {
			stopReload();
			return super.handleExit();
		}

		private void updateView(boolean reload) {
			final Image image = currentImage(reload);
			if (image != null) {
				try {
					setPainting(false);
					
					if (image.getWidth(null) > 640 || image.getHeight(null) > 480)
					{
						BufferedImage scaled = ImageManipulator.getScaledImage((BufferedImage)image, getWidth(), getHeight());
						mImage.setResource(createImage(scaled), RSRC_IMAGE_BESTFIT);
					}
					else
						mImage.setResource(createImage(image), RSRC_IMAGE_BESTFIT);

				} finally {
					setPainting(true);
				}
			}
			flush();
		}

		private void startReload() {
			if (mImageThread != null && mImageThread.isAlive())
				mImageThread.interrupt();

			mImageThread = new Thread() {
				public void run() {
					try {
						while (getApp().getContext() != null) {
							synchronized (this) {
								updateView(true);
							}
							sleep(1000);
						}
					} catch (Exception ex) {
						Tools.logException(Internet.class, ex, "Could not retrieve internet image");
					}
				}

				public void interrupt() {
					synchronized (this) {
						super.interrupt();
					}
				}
			};
			mImageThread.start();
			mPlaying = true;
			setFooter("Press PAUSE to stop reloading");
		}

		private void stopReload() {
			if (mImageThread != null && mImageThread.isAlive())
				mImageThread.interrupt();
			mImageThread = null;
			mPlaying = false;
			setFooter("Press PLAY to reload");
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SLOW:
			case KEY_PAUSE:
				if (mPlaying)
					stopReload();
				else
					startReload();
				break;
			case KEY_PLAY:
				startReload();
				break;
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_CHANNELUP:
				stopReload();
				getBApp().play("pageup.snd");
				getBApp().flush();
				getPrevPos();
				updateView(false);
				return true;
			case KEY_CHANNELDOWN:
				stopReload();
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				updateView(false);
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

		public void setTracker(Tracker value) {
			mTracker = value;
		}

		private Image currentImage(boolean reload) {
			if (mTracker != null) {
				try {
					InternetConfiguration.SharedUrl nameValue = (InternetConfiguration.SharedUrl) mTracker.getList().get(mTracker.getPos());
					if (nameValue != null) {
						return getImage(nameValue.getValue(), reload);
					}
				} catch (Exception ex) {
					Tools.logException(Internet.class, ex);
				}
			}
			return null;
		}

		private BView mImage;

		private Tracker mTracker;

		private Thread mImageThread;

		private boolean mPlaying;
	}

	private static Image getImage(String address, boolean reload) {
		try {
			URL url = new URL(address);
			if (reload)
				Tools.cacheImage(url, address);
			
			Image image = Tools.retrieveCachedImage(url);
			if (image==null)
			{
				image = Tools.getImage(url, -1, -1);
			}
			return image;
		} catch (Exception ex) {
			Tools.logException(Internet.class, ex);
		}
		return null;
	}
	
	public class TagsMenuScreen extends DefaultMenuScreen {
		public TagsMenuScreen(Internet app) {
			super(app, "Internet Tags");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);
			
			DataConfiguration dataConfiguration = Server.getServer().getDataConfiguration();
			
			try
			{
				String result = Users.retrieveInternetTags(dataConfiguration);
	            if (result != null && result.length()>0)
	            {
	            	SAXReader saxReader = new SAXReader();
		            log.debug("Tags: " + result);
		            StringReader stringReader = new StringReader(result);
		            Document document = saxReader.read(stringReader);
		            //Document document = saxReader.read(new File("d:/galleon/location.xml"));
		
		            Element root = document.getRootElement(); 
		            Element tags = root.element("tags");
		            if (tags!=null)
		            {
			            for (Iterator i = tags.elementIterator("tag"); i.hasNext();) {
			                Element element = (Element) i.next();
			                mMenuList.add(Tools.getAttribute(element, "name"));
			            }
		            }
	            }
			} catch (Exception ex) {
				Tools.logException(Internet.class, ex);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					
					final InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory()).getAppContext().getConfiguration();

					new Thread() {
						public void run() {
							try {
								String tag = (String)mMenuList.get(mMenuList.getFocus()); 
								//Tracker tracker = new Tracker(mMenuList.getRows(), 0);
								
								DataConfiguration dataConfiguration = Server.getServer().getDataConfiguration();
								
								ArrayList list = new ArrayList();
								String result = Users.retrieveInternetFromTag(dataConfiguration, tag);
					            if (result != null && result.length()>0)
					            {
					            	SAXReader saxReader = new SAXReader();
						            log.debug("Tags: " + result);
						            StringReader stringReader = new StringReader(result);
						            Document document = saxReader.read(stringReader);
						            //Document document = saxReader.read(new File("d:/galleon/location.xml"));
						
						            Element root = document.getRootElement(); 
						            Element internet = root.element("internet");
						            if (internet!=null)
						            {
							            for (Iterator i = internet.elementIterator("url"); i.hasNext();) {
							                Element element = (Element) i.next();
							                InternetConfiguration.SharedUrl sharedUrl = new InternetConfiguration.SharedUrl(Tools.getAttribute(element, "name"), Tools.getAttribute(element, "url"), Tools.getAttribute(element, "description"), tag, SharedUrl.PRIVATE); 
							                list.add(sharedUrl);
							            }
						            }
					            }
								
					            Tracker tracker = new Tracker(list, 0);
					            getBApp().push(new FavoritesMenuScreen((Internet)getBApp(), tracker), TRANSITION_NONE);
								//getBApp().push(new TagMenuScreen((Internet)getBApp(), tag), TRANSITION_NONE);
								getBApp().flush();
								
							} catch (Exception ex) {
								Tools.logException(Internet.class, ex);
							}
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			try
			{
				BView icon = new BView(parent, 9, 2, 32, 32);
				icon.setResource(mFolderIcon);
				String title = (String) mMenuList.get(index);

				BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
				name.setShadow(true);
				name.setFlags(RSRC_HALIGN_LEFT);
				name.setValue(Tools.trim(Tools.clean(title), 40));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SELECT:
				postEvent(new BEvent.Action(this, "push"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Internet) getBApp()), TRANSITION_LEFT);
				return true;
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}
	}	
	
	public class TagMenuScreen extends DefaultMenuScreen {
		public TagMenuScreen(Internet app, String tag) {
			super(app, "Internet Tag");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);
			
			DataConfiguration dataConfiguration = Server.getServer().getDataConfiguration();
			
			try
			{
				String result = Users.retrieveInternetFromTag(dataConfiguration, tag);
	            if (result != null && result.length()>0)
	            {
	            	SAXReader saxReader = new SAXReader();
		            log.debug("Tags: " + result);
		            StringReader stringReader = new StringReader(result);
		            Document document = saxReader.read(stringReader);
		            //Document document = saxReader.read(new File("d:/galleon/location.xml"));
		
		            Element root = document.getRootElement(); 
		            Element internet = root.element("internet");
		            if (internet!=null)
		            {
			            for (Iterator i = internet.elementIterator("url"); i.hasNext();) {
			                Element element = (Element) i.next();
			                mMenuList.add(Tools.getAttribute(element, "name"));
			            }
		            }
	            }
			} catch (Exception ex) {
				Tools.logException(Internet.class, ex);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					
					final InternetConfiguration internetConfiguration = (InternetConfiguration) ((InternetFactory) getFactory()).getAppContext().getConfiguration();

					new Thread() {
						public void run() {
							try {
								/*
								if (mMenuList.getFocus()==0)
								{
									Tracker tracker = new Tracker(internetConfiguration.getSharedUrls(), 0);
									getBApp().push(new FavoritesMenuScreen((Internet)getBApp(), tracker), TRANSITION_NONE);
								}
								else
									getBApp().push(new FavoritesMenuScreen(this, tracker), TRANSITION_NONE);
								getBApp().flush();
								*/
								
							} catch (Exception ex) {
								Tools.logException(Internet.class, ex);
							}
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			try
			{
				BView icon = new BView(parent, 9, 2, 32, 32);
				icon.setResource(mFolderIcon);
				String title = (String) mMenuList.get(index);

				BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
				name.setShadow(true);
				name.setFlags(RSRC_HALIGN_LEFT);
				name.setValue(Tools.trim(Tools.clean(title), 40));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SELECT:
				postEvent(new BEvent.Action(this, "push"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Internet) getBApp()), TRANSITION_LEFT);
				return true;
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}
	}	

	public static class InternetFactory extends AppFactory {

		public void initialize() {
			final InternetConfiguration internetConfiguration = (InternetConfiguration) getAppContext().getConfiguration();

			Server.getServer().scheduleShortTerm(new ReloadTask(new ReloadCallback() {
				public void reload() {
					try {
						updateImages();
					} catch (Exception ex) {
						log.error("Could not download internet images", ex);
					}
				}
			}), internetConfiguration.getReload());
			
			Server.getServer().scheduleData(new ReloadTask(new ReloadCallback() {
				public void reload() {
					try {
						updateData();
					} catch (Exception ex) {
						log.error("Could not updata internet data", ex);
					}
				}
			}), 60*24);
		}

		public void updateAppContext(AppContext appContext) {
			super.updateAppContext(appContext);

			updateImages();
			updateData();
		}

		private void updateImages() {
			final InternetConfiguration internetConfiguration = (InternetConfiguration) getAppContext()
					.getConfiguration();

			new Thread() {
				public void run() {
					try
					{
						Iterator iterator = internetConfiguration.getSharedUrls().iterator();
						while (iterator.hasNext()) {
							InternetConfiguration.SharedUrl nameValue = (InternetConfiguration.SharedUrl) iterator.next();
	
							try {
								URL url = new URL(nameValue.getValue());
								Tools.cacheImage(url, nameValue.getValue());
							} catch (Exception ex) {
								Tools.logException(Internet.class, ex);
							}
	
						}
					}
					catch (Exception ex) {}
				}
			}.start();
		}
		
		private void updateData() {
			InternetConfiguration internetConfiguration = (InternetConfiguration) getAppContext()
					.getConfiguration();

			try {
				StringBuffer buffer = new StringBuffer();
				synchronized(buffer)
				{
					buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
					buffer.append("<data>\n");
					buffer.append("<internet>\n");
					Iterator iterator = internetConfiguration.getSharedUrls().iterator();
					while (iterator.hasNext())
					{
						InternetConfiguration.SharedUrl nameValue = (InternetConfiguration.SharedUrl) iterator.next();
						if (nameValue.getPrivacy().equals(InternetConfiguration.SharedUrl.PUBLIC))
						{
							buffer.append("<url");
							buffer.append(" name=\""+Tools.escapeXMLChars(nameValue.getName())+"\"");
							buffer.append(" url=\""+Tools.escapeXMLChars(nameValue.getValue())+"\"");
							buffer.append(" description=\""+Tools.escapeXMLChars(nameValue.getDescription())+"\"");
							buffer.append(" tags=\""+Tools.escapeXMLChars(nameValue.getTags())+"\"");
							if (nameValue.getPrivacy().equals(InternetConfiguration.SharedUrl.PRIVATE))
								buffer.append(" privacy=\"0\"");
							else
							if (nameValue.getPrivacy().equals(InternetConfiguration.SharedUrl.PUBLIC))
								buffer.append(" privacy=\"1\"");
							else
							if (nameValue.getPrivacy().equals(InternetConfiguration.SharedUrl.FRIENDS))
								buffer.append(" privacy=\"2\"");
							else
								buffer.append(" privacy=\"0\"");
							buffer.append(" />\n");
						}
					}
					buffer.append("</internet>\n");
					buffer.append("</data>\n");
				}
				Users.updateInternet(Server.getServer().getDataConfiguration(), buffer.toString());
			} catch (Exception ex) {
				log.error("Could not update internet data", ex);
			}
		}
	}
}