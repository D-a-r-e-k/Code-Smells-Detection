package org.lnicholls.galleon.apps.videocasting;

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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lnicholls.galleon.app.AppContext;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.database.Audio;
import org.lnicholls.galleon.database.AudioManager;
import org.lnicholls.galleon.database.PersistentValue;
import org.lnicholls.galleon.database.PersistentValueManager;
import org.lnicholls.galleon.database.Video;
import org.lnicholls.galleon.database.VideoManager;
import org.lnicholls.galleon.database.Videocast;
import org.lnicholls.galleon.database.VideocastManager;
import org.lnicholls.galleon.database.VideocastTrack;
import org.lnicholls.galleon.media.MediaManager;
import org.lnicholls.galleon.media.VideoFile;
import org.lnicholls.galleon.server.MusicPlayerConfiguration;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.NameValue;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.util.FileSystemContainer.FileItem;
import org.lnicholls.galleon.util.FileSystemContainer.Item;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultMenuScreen;
import org.lnicholls.galleon.widget.DefaultOptionList;
import org.lnicholls.galleon.widget.DefaultOptionsScreen;
import org.lnicholls.galleon.widget.DefaultPlayer;
import org.lnicholls.galleon.widget.DefaultScreen;
import org.lnicholls.galleon.widget.MusicInfo;
import org.lnicholls.galleon.widget.MusicPlayer;
import org.lnicholls.galleon.widget.OptionsButton;
import org.lnicholls.galleon.widget.ScreenSaver;
import org.lnicholls.galleon.widget.DefaultApplication.Tracker;
import org.lnicholls.galleon.winamp.WinampPlayer;

import com.tivo.hme.bananas.BEvent;
import com.tivo.hme.bananas.BList;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.interfaces.IContext;
import com.tivo.hme.sdk.IHmeProtocol;
import com.tivo.hme.sdk.Resource;

import de.nava.informa.core.ChannelBuilderIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

public class Videocasting extends DefaultApplication {

	private static Logger log = Logger.getLogger(Videocasting.class.getName());

	public final static String TITLE = "Videocasting";

	private Resource mMenuBackground;

	private Resource mInfoBackground;

	private Resource mPlayerBackground;

	private Resource mLyricsBackground;

	private Resource mImagesBackground;

	private Resource mFolderIcon;

	private Resource mItemIcon;

	public void init(IContext context) throws Exception {
		super.init(context);

		mMenuBackground = getSkinImage("menu", "background");
		mInfoBackground = getSkinImage("info", "background");
		mPlayerBackground = getSkinImage("player", "background");
		mLyricsBackground = getSkinImage("lyrics", "background");
		mImagesBackground = getSkinImage("images", "background");
		mFolderIcon = getSkinImage("menu", "folder");
		mItemIcon = getSkinImage("menu", "item");

		VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
				.getAppContext().getConfiguration();

		push(new VideocastingMenuScreen(this), TRANSITION_NONE);

		initialize();
	}

	public static Element getDocument(String location) {
		try {
			Document document = null;

			PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Videocasting.class.getName()
					+ "." + location);
			String content = persistentValue == null ? null : persistentValue.getValue();
			// log.debug("content1="+content);
			if (PersistentValueManager.isAged(persistentValue) && !location.equals("local")) {
				try {
					String page = Tools.getPage(new URL(location));
					if (page != null && page.length() > 0) {
						content = page;
					}
				} catch (Exception ex) {
					Tools.logException(Videocasting.class, ex, "Could not cache document: " + location);
				}
			} else {
				if (location.equals("local"))
					content = "";
			}
			// log.debug("content2="+content);
			if (content != null) {
				SAXReader saxReader = new SAXReader();
				try {
					if (location.equals("local")) {
						document = saxReader.read(new File("d:/galleon/ivideoder.opml"));
					} else {
						StringReader stringReader = new StringReader(content);
						document = saxReader.read(stringReader);
						if (PersistentValueManager.isAged(persistentValue)) {
							PersistentValueManager.savePersistentValue(Videocasting.class.getName() + "." + location,
									content, 60 * 60 * 6);
						}
						stringReader.close();
						stringReader = null;
					}
				} catch (Throwable ex) {
					if (persistentValue != null) {
						StringReader stringReader = new StringReader(persistentValue.getValue());
						document = saxReader.read(stringReader);
						stringReader.close();
						stringReader = null;
					}
				}

				if (document != null) {
					Element root = document.getRootElement();
					document.clearContent();
					document = null;
					if (root.getName().equals("opml"))
						return root.element("body");
				}
			}
		} catch (Exception ex) {
			Tools.logException(Videocasting.class, ex, "Could not download document: " + location);
		}
		return null;
	}

	private static List getElements(Element element) {
		ArrayList list = new ArrayList();

		try {
			if (element.getName().equals("outline") || element.getName().equals("body")) // OPML
			{
				List outlines = element.elements("outline");
				if (outlines.size() == 1) {
					element = (Element) outlines.get(0);
				}
				for (Iterator i = element.elementIterator("outline"); i.hasNext();) {
					Element outline = (Element) i.next();
					list.add(outline);
				}
			}
		} catch (Exception ex) {
			Tools.logException(Videocasting.class, ex, "Could not determine data");
		}

		if (list.size() > 0) {
			Element[] elementArray = (Element[]) list.toArray(new Element[0]);
			/*
			 * Arrays.sort(elementArray, new Comparator() { public int
			 * compare(Object o1, Object o2) { String element1 =
			 * Tools.getAttribute((Element) o1, "text"); String element2 =
			 * Tools.getAttribute((Element) o2, "text"); if (element1 != null &&
			 * element2 != null) return element1.compareTo(element2); else
			 * return 0; } });
			 */

			list.clear();
			for (int i = 0; i < elementArray.length; i++)
				list.add(elementArray[i]);
		}

		return list;
	}

	private boolean isFolder(Element element) {
		if (element.getName().equals("outline") || element.getName().equals("body")) // OPML
		{
			String type = Tools.getAttribute(element, "type");
			String xmlUrl = Tools.getAttribute(element, "xmlUrl");
			if (xmlUrl != null)
				return false;
			else if (type == null)
				return true;
			else if (type.equalsIgnoreCase("link"))
				return false;
			else if (type.equalsIgnoreCase("rss"))
				return false;

			return true;
		}
		return false;
	}

	private String getUrl(Element element) {
		if (element.getName().equals("outline")) {
			String url = Tools.getAttribute(element, "xmlUrl");
			if (url != null)
				return url;
			url = Tools.getAttribute(element, "url");
			if (url != null)
				return url;
		}
		return null;
	}

	public static Videocast getVideocast(Videocast videocast) {
		PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Videocasting.class.getName() + "."
				+ videocast.getPath());
		String content = persistentValue == null ? null : persistentValue.getValue();
		// log.debug("content3="+content);
		if (PersistentValueManager.isAged(persistentValue)) {
			try {
				String page = Tools.getPage(new URL(videocast.getPath()));
				if (page != null && page.length() > 0)
					content = page;
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex, "Could not cache listing: " + videocast.getPath());
			}
		}
		// log.debug("content4="+content);
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
				if (root != null && root.getName().equals("rss")) {
					Element channel = root.element("channel");
					if (channel != null) {
						String channelTitle = null;
						String channelLink = null;
						String channelDescription = null;
						String channelSubtitle = null;
						String channelImage1 = null;
						String channelImage2 = null;
						String channelImage3 = null;
						String channelExplicit = null;
						String channelBlock = null;
						String channelCategory = null;
						String channelKeywords = null;
						String channelSummary = null;
						String channelTtl = null;
						String channelAuthor = null;

						String value = null;
						if ((value = Tools.getAttribute(channel, "author")) != null) {
							channelAuthor = value;
						}

						if ((value = Tools.getAttribute(channel, "title")) != null) {
							channelTitle = value;
						}

						if ((value = Tools.getAttribute(channel, "link")) != null) {
							channelLink = value;
						}

						if ((value = Tools.getAttribute(channel, "description")) != null) {
							channelDescription = value;
						}

						if ((value = Tools.getAttribute(channel, "subtitle")) != null) {
							channelSubtitle = value;
						}

						for (Iterator i = channel.elementIterator("image"); i.hasNext();) {
							Element item = (Element) i.next();

							if (item.element("url") != null) {
								if ((value = Tools.getAttribute(item, "url")) != null) {
									channelImage1 = value; // rss
								}
							} else if (item.attribute("href") != null) {
								channelImage2 = item.attributeValue("href"); // itunes
							}
						}

						Element thumbnailElement = channel.element("thumbnail");
						if (thumbnailElement != null) {
							if ((value = thumbnailElement.attributeValue("url")) != null) {
								channelImage3 = value;
							}
						}

						if ((value = Tools.getAttribute(channel, "explicit")) != null) {
							channelExplicit = value;
						}

						if ((value = Tools.getAttribute(channel, "block")) != null) {
							channelBlock = value;
						}

						if ((value = Tools.getAttribute(channel, "keywords")) != null) {
							channelKeywords = value;
						}

						if ((value = Tools.getAttribute(channel, "summary")) != null) {
							channelSummary = value;
						}

						if ((value = Tools.getAttribute(channel, "ttl")) != null) {
							channelTtl = value;
						}

						Element categoryElement = channel.element("category");
						if (categoryElement != null) {
							if ((value = categoryElement.attributeValue("text")) != null) {
								channelCategory = value;

								Element subCategory = categoryElement.element("category");
								if (subCategory != null) {
									if ((value = subCategory.attributeValue("text")) != null)
										channelCategory = channelCategory + ", " + value;
								}
							}
						}

						if (channelAuthor != null && channelAuthor.length() > 0)
							videocast.setAuthor(Tools.cleanHTML(channelAuthor));
						if (channelTitle != null && channelTitle.length() > 0)
							videocast.setTitle(Tools.cleanHTML(channelTitle));
						if (channelLink != null && channelLink.length() > 0)
							videocast.setLink(channelLink);
						if (channelDescription != null && channelDescription.length() > 0)
							videocast.setDescription(Tools.trim(Tools.cleanHTML(channelDescription), 4096));
						if (channelSubtitle != null && channelSubtitle.length() > 0)
							videocast.setSubtitle(Tools.trim(Tools.cleanHTML(channelSubtitle), 4096));
						if (channelImage2 == null) {
							if (channelImage1 != null
									&& (channelImage1.endsWith(".png") || channelImage1.endsWith(".jpg") || channelImage1
											.endsWith(".gif")))
								videocast.setImage(channelImage1); // rss
							else
								videocast.setImage(channelImage3);
						} else {
							if (channelImage2.endsWith(".png") || channelImage2.endsWith(".jpg"))
								videocast.setImage(channelImage2); // itunes
						}
						if (channelBlock != null)
							videocast.setBlock(new Boolean(!channelBlock.toLowerCase().equals("no")));
						else
							videocast.setBlock(Boolean.FALSE);
						if (channelCategory != null && channelCategory.length() > 0)
							videocast.setCategory(Tools.cleanHTML(channelCategory));
						if (channelExplicit != null)
							videocast.setExplicit(new Boolean(!channelExplicit.toLowerCase().equals("no")));
						else
							videocast.setExplicit(Boolean.FALSE);
						if (channelKeywords != null && channelKeywords.length() > 0)
							videocast.setKeywords(Tools.cleanHTML(channelKeywords));
						if (channelSummary != null && channelSummary.length() > 0)
							videocast.setSummary(Tools.trim(channelSummary, 4096));
						if (channelTtl != null && channelTtl.length() > 0) {
							try {
								videocast.setTtl(new Integer(channelTtl));
							} catch (Exception ex) {
							}
						} else
							videocast.setTtl(new Integer(0));

						List tracks = videocast.getTracks();
						if (tracks == null)
							tracks = new ArrayList();
						for (Iterator i = channel.elementIterator("item"); i.hasNext();) {
							Element item = (Element) i.next();

							String title = null;
							String description = null;
							String link = null;
							String pubDate = null;
							String guid = null;
							String category = null;
							String explicit = null;
							String author = null;
							String summary = null;
							String enclosureUrl = null;
							String enclosureLength = null;
							String enclosureType = null;
							String block = null;
							String duration = null;
							String keywords = null;
							String subtitle = null;

							if ((value = Tools.getAttribute(item, "title")) != null) {
								title = value;
							}

							if ((value = Tools.getAttribute(item, "description")) != null) {
								description = value;
							}

							if ((value = Tools.getAttribute(item, "link")) != null) {
								link = value;
							}

							if ((value = Tools.getAttribute(item, "pubDate")) != null) {
								pubDate = value;
							}

							if ((value = Tools.getAttribute(item, "guid")) != null) {
								guid = value;
							}

							if ((value = Tools.getAttribute(item, "category")) != null) {
								category = value;
							}

							if ((value = Tools.getAttribute(item, "explicit")) != null) {
								explicit = value;
							}

							for (Iterator j = item.elementIterator("author"); j.hasNext();) {
								Element authorItem = (Element) j.next();
								author = authorItem.getTextTrim();
							}

							if ((value = Tools.getAttribute(item, "summary")) != null) {
								summary = value;
							}

							Element enclosureElement = item.element("enclosure");
							if (enclosureElement != null) {
								if ((value = enclosureElement.attributeValue("url")) != null) {
									enclosureUrl = value;
								}

								if ((value = enclosureElement.attributeValue("length")) != null) {
									enclosureLength = value;
								}

								if ((value = enclosureElement.attributeValue("type")) != null) {
									enclosureType = value;
								}
							}

							if ((value = Tools.getAttribute(item, "block")) != null) {
								block = value;
							}

							categoryElement = item.element("category");
							if (categoryElement != null) {
								if ((value = categoryElement.attributeValue("text")) != null) {
									category = value;

									Element subCategory = categoryElement.element("category");
									if (subCategory != null) {
										if ((value = subCategory.attributeValue("text")) != null)
											category = category + ", " + value;
									}
								}
							}

							if ((value = Tools.getAttribute(item, "duration")) != null) {
								duration = value;
							}

							if ((value = Tools.getAttribute(item, "keywords")) != null) {
								keywords = value;
							}

							if ((value = Tools.getAttribute(item, "subtitle")) != null) {
								subtitle = value;
							}

							if (enclosureUrl != null && enclosureUrl.length() > 0) {
								VideocastTrack videocastTrack = null;

								boolean existing = false;
								for (int k = 0; k < tracks.size(); k++) {
									VideocastTrack current = (VideocastTrack) tracks.get(k);
									if (current != null && current.getUrl() != null && enclosureUrl != null
											&& current.getUrl().equals(enclosureUrl)) {
										existing = true;
										videocastTrack = current;
										break;
									}
								}

								if (videocastTrack == null)
									videocastTrack = new VideocastTrack();

								if (title != null && title.length() > 0 && (videocastTrack.getTitle()==null || videocastTrack.getTitle().trim().length()==0))
									videocastTrack.setTitle(Tools.cleanHTML(title));
								if (description != null && description.length() > 0)
									videocastTrack.setDescription(Tools.trim(Tools.cleanHTML(description), 4096));
								if (link != null && link.length() > 0)
									videocastTrack.setLink(link);
								if (pubDate != null && pubDate.length() > 0) {
									try {
										videocastTrack.setPublicationDate(new Date(pubDate));
									} catch (Exception ex) {
									}
								}
								if (guid != null && guid.length() > 0)
									videocastTrack.setGuid(guid);
								if (category != null && category.length() > 0)
									videocastTrack.setCategory(Tools.cleanHTML(category));
								if (explicit != null && explicit.length() > 0)
									videocastTrack.setExplicit(new Boolean(!explicit.toLowerCase().equals("no")));
								else
									videocastTrack.setExplicit(Boolean.FALSE);
								if (author != null && author.length() > 0)
									videocastTrack.setAuthor(Tools.cleanHTML(author));
								if (summary != null && summary.length() > 0)
									videocastTrack.setSummary(Tools.trim(Tools.cleanHTML(summary), 4096));
								if (enclosureUrl != null && enclosureUrl.length() > 0)
									videocastTrack.setUrl(enclosureUrl);
								if (enclosureLength != null && enclosureLength.length() > 0) {
									try {
										videocastTrack.setSize(Long.parseLong(enclosureLength));
									} catch (Exception ex) {
									}
								}
								if (enclosureType != null && enclosureType.length() > 0)
									videocastTrack.setMimeType(enclosureType);
								if (block != null && block.length() > 0)
									videocastTrack.setBlock(new Boolean(!block.toLowerCase().equals("no")));
								else
									videocastTrack.setBlock(Boolean.FALSE);
								if (duration != null && duration.length() > 0) {
									try {
										SimpleDateFormat timeDateFormat = new SimpleDateFormat();
										timeDateFormat.applyPattern("HH:mm:ss");
										ParsePosition pos = new ParsePosition(0);
										Date date = timeDateFormat.parse(duration, pos);
										if (date == null) {
											timeDateFormat.applyPattern("mm:ss");
											date = timeDateFormat.parse(duration, pos);
										}
										videocastTrack.setDuration(new Long(date.getTime()));
									} catch (Exception ex) {
									}
								}
								if (keywords != null && keywords.length() > 0)
									videocastTrack.setKeywords(Tools.cleanHTML(keywords));
								if (subtitle != null && subtitle.length() > 0)
									videocastTrack.setSubtitle(Tools.trim(Tools.cleanHTML(subtitle), 4096));

								if (videocastTrack.getMimeType() == null)
									videocastTrack.setMimeType(VideoFile.DEFAULT_MIME_TYPE);
								videocastTrack.setVideocast(videocast.getId());
								if (!existing)
									tracks.add(videocastTrack);
							}
						}
						videocast.setTracks(tracks);
					}
				} else {
					ChannelBuilderIF builder = new ChannelBuilder();
					ChannelIF channel = FeedParser
							.parse(builder, new ByteArrayInputStream((content.getBytes("UTF-8"))));

					if (channel != null) {
						videocast.setDescription(Tools.cleanHTML(channel.getDescription()));
						videocast.setDateUpdated(channel.getLastBuildDate());
						videocast.setTtl(new Integer(channel.getTtl()));

						List items = getListing(channel);
						if (items != null && items.size() > 0) {
							ArrayList tracks = new ArrayList();
							for (Iterator i = items.iterator(); i.hasNext(); /* Nothing */) {
								ItemIF item = (ItemIF) i.next();
								String description = Tools.trim(item.getDescription(), 4096);

								tracks.add(new VideocastTrack(Tools.cleanHTML(item.getTitle()), null, null, Tools.trim(
										Tools.cleanHTML(item.getDescription()), 4096), null, null, null, null,
										Boolean.FALSE, Boolean.FALSE, null, item.getDate(), item.getEnclosure()
												.getLocation().toExternalForm(), "audio/mpeg", item.getEnclosure()
												.getLength(), 0, new Long(0), new Integer(0), 0, 0, videocast.getId(),
												new Integer(0), null));
							}
							videocast.setTracks(tracks);
						}
					}
					builder.close();
					builder = null;
				}
				document.clearContent();
				document = null;

				if (PersistentValueManager.isAged(persistentValue)) {
					int ttl = videocast.getTtl().intValue();
					if (ttl < 10)
						ttl = 60;
					else
						ttl = 60 * 6;

					PersistentValueManager.savePersistentValue(
							Videocasting.class.getName() + "." + videocast.getPath(), content, ttl * 60);
				}
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex, "Could not download listing: " + videocast.getPath());
				return null;
			}
		}
		return videocast;
	}

	public static List getListing(ChannelIF channel) {
		ArrayList listing = new ArrayList();

		if (channel != null) {
			try {
				if (channel.getItems().size() > 0) {
					int count = 0;
					Iterator chs = channel.getItems().iterator();
					while (chs.hasNext()) {
						ItemIF item = (ItemIF) chs.next();
						if (item.getEnclosure() != null) {
							if ((item.getEnclosure().getLocation() != null)
									|| (item.getEnclosure().getType() != null && item.getEnclosure().getType().equals(
											"audio/mpeg")))
								listing.add(item);
						}
					}
				}
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex);
			}
		}
		return listing;
	}

	public class OptionsScreen extends DefaultOptionsScreen {

		public OptionsScreen(DefaultApplication app) {
			super(app);

			getBelow().setResource(mInfoBackground);

			VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
					.getAppContext().getConfiguration();

			int start = TOP;
			int width = 280;
			int increment = 37;
			int height = 25;
			BText text = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			text.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_CENTER);
			text.setFont("default-24-bold.font");
			text.setShadow(true);
			text.setValue("Download");

			NameValue[] nameValues = new NameValue[] { new NameValue("All", "-1"), new NameValue("1", "1"),
					new NameValue("2", "2"), new NameValue("3", "3"), new NameValue("4", "4"), new NameValue("5", "5") };
			mDownloadButton = new OptionsButton(getNormal(), BORDER_LEFT + BODY_WIDTH - width, start, width, height,
					true, nameValues, String.valueOf(videocastingConfiguration.getDownload()));
			setFocusDefault(mDownloadButton);
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
					VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
							.getAppContext().getConfiguration();
					videocastingConfiguration.setDownload(Integer.parseInt(mDownloadButton.getValue()));

					Server.getServer().updateApp(((VideocastingFactory) getFactory()).getAppContext());
				}
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex, "Could not configure Videocasting app");
			}
			return super.handleExit();
		}

		private OptionsButton mDownloadButton;
	}

	public class VideocastingMenuScreen extends DefaultMenuScreen {
		public VideocastingMenuScreen(Videocasting app) {
			super(app, "Videocasting");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);

			mMenuList.add(new NameValue("Now Playing", "Now Playing"));
			mMenuList.add(new NameValue("Subscriptions", "Subscriptions"));
			mMenuList.add(new NameValue("Directories", "Directories"));
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();

					new Thread() {
						public void run() {
							try {
								switch (mMenuList.getFocus()) {
								case 0:
									NowPlayingMenuScreen nowPlayingMenuScreen = new NowPlayingMenuScreen(
											(Videocasting) getApp());
									push(nowPlayingMenuScreen, TRANSITION_LEFT);
									break;
								case 1:
									SubscribedMenuScreen subscribedMenuScreen = new SubscribedMenuScreen(
											(Videocasting) getApp());
									push(subscribedMenuScreen, TRANSITION_LEFT);
									break;
								case 2:
									DirectoriesMenuScreen directoriesMenuScreen = new DirectoriesMenuScreen(
											(Videocasting) getApp());
									push(directoriesMenuScreen, TRANSITION_LEFT);
									break;
								default:
									break;
								}
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
							getBApp().flush();
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			NameValue nameValue = (NameValue) mMenuList.get(index);
			icon.setResource(mFolderIcon);

			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(Tools.cleanHTML(nameValue.getName()), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
			}

			return super.handleKeyPress(code, rawcode);
		}
	}

	public class NowPlayingMenuScreen extends DefaultMenuScreen {
		public NowPlayingMenuScreen(Videocasting app) {
			super(app, "Now Playing");

			setFooter("Press ENTER for options");

			mDateFormat = new SimpleDateFormat();
			mDateFormat.applyPattern("EEE M/d/yyyy hh:mm a");

			mDayFormat = new SimpleDateFormat();
			mDayFormat.applyPattern("M/d/yy");

			getBelow().setResource(mMenuBackground);

			VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
					.getAppContext().getConfiguration();

			List list = null;
			try {
				list = VideocastManager.listAll();
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex);
			}

			mList = new ArrayList();
			if (list != null && list.size() > 0) {
				for (Iterator i = list.iterator(); i.hasNext();) {
					Videocast videocast = (Videocast) i.next();
					List tracks = videocast.getTracks();
					if (tracks != null && tracks.size() > 0) {
						for (Iterator trackIterator = tracks.iterator(); trackIterator.hasNext();) {
							VideocastTrack videocastTrack = (VideocastTrack) trackIterator.next();
							if ((videocastTrack.getStatus() == VideocastTrack.STATUS_DOWNLOADED || videocastTrack
									.getStatus() == VideocastTrack.STATUS_PLAYED)
									&& videocastTrack.getTrack() != null) {
								mList.add(videocastTrack);
							}
						}
					}
				}
			}

			VideocastTrack[] videocastTrackArray = (VideocastTrack[]) mList.toArray(new VideocastTrack[0]);
			mList.clear();
			Arrays.sort(videocastTrackArray, new Comparator() {
				public int compare(Object o1, Object o2) {
					VideocastTrack videocastTrack1 = (VideocastTrack) o1;
					VideocastTrack videocastTrack2 = (VideocastTrack) o2;
					if (videocastTrack1 != null && videocastTrack2 != null
							&& videocastTrack1.getPublicationDate() != null
							&& videocastTrack2.getPublicationDate() != null)
						return -videocastTrack1.getPublicationDate().compareTo(videocastTrack2.getPublicationDate());
					else
						return 0;
				}
			});

			for (int i = 0; i < videocastTrackArray.length; i++) {
				mMenuList.add(videocastTrackArray[i]);
				mList.add(videocastTrackArray[i]);
			}
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			if (mTracker != null)
			{
				mFocus = mTracker.getPos();
				mTracker = (Tracker)mTracker.clone();
			}
			return super.handleEnter(arg, isReturn);
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();

					new Thread() {
						public void run() {
							try {
								mTracker = new Tracker(mList, mMenuList.getFocus());

								getBApp().push(new VideocastItemScreen((Videocasting) getBApp(), mTracker),
										TRANSITION_LEFT);
								getBApp().flush();
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			VideocastTrack videocastTrack = (VideocastTrack) mMenuList.get(index);
			icon.setResource(mItemIcon);

			if (videocastTrack.getTitle().length() == 0) {
				BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
				name.setShadow(true);
				name.setFlags(RSRC_HALIGN_LEFT);
				name.setValue(mDateFormat.format(videocastTrack.getPublicationDate()));
			} else {
				if (videocastTrack.getPublicationDate() != null) {
					BText name = new BText(parent, 50, 4, parent.getWidth() - 140, parent.getHeight() - 4);
					name.setShadow(true);
					name.setFlags(RSRC_HALIGN_LEFT);
					name.setValue(Tools.trim(Tools.cleanHTML(videocastTrack.getTitle()), 30));

					BText ratedText = new BText(parent, parent.getWidth() - 100 - parent.getHeight(), 4, 100, parent
							.getHeight() - 4);
					ratedText.setShadow(true);
					ratedText.setFlags(RSRC_HALIGN_RIGHT);
					ratedText.setValue(mDayFormat.format(videocastTrack.getPublicationDate()));
				} else {
					BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
					name.setShadow(true);
					name.setFlags(RSRC_HALIGN_LEFT);
					name.setValue(Tools.trim(Tools.cleanHTML(videocastTrack.getTitle()), 40));
				}
			}
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		List mList;

		Tracker mTracker;

		private SimpleDateFormat mDateFormat;

		private SimpleDateFormat mDayFormat;
	}

	public class SubscribedMenuScreen extends DefaultMenuScreen {
		public SubscribedMenuScreen(Videocasting app) {
			super(app, "Subscriptions");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);

			VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
					.getAppContext().getConfiguration();

			try {
				mList = VideocastManager.listAllSubscribed();
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex);
			}

			if (mList != null && mList.size() > 0) {
				for (Iterator i = mList.iterator(); i.hasNext(); /* Nothing */) {
					Videocast videocast = (Videocast) i.next();
					mMenuList.add(videocast);
				}
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();

					new Thread() {
						public void run() {
							try {
								Tracker tracker = new Tracker(mList, mMenuList.getFocus());

								getBApp().push(new VideocastScreen((Videocasting) getBApp(), tracker, true),
										TRANSITION_LEFT);
								getBApp().flush();
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			Videocast videocast = (Videocast) mMenuList.get(index);
			icon.setResource(mFolderIcon);

			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(Tools.cleanHTML(videocast.getTitle()), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		List mList;
	}

	public class DirectoriesMenuScreen extends DefaultMenuScreen {
		public DirectoriesMenuScreen(Videocasting app) {
			super(app, "Videocast Directories");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);

			VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
					.getAppContext().getConfiguration();

			for (Iterator i = videocastingConfiguration.getDirectorys().iterator(); i.hasNext(); /* Nothing */) {
				NameValue nameValue = (NameValue) i.next();
				mMenuList.add(nameValue);
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();

					new Thread() {
						public void run() {
							try {
								NameValue nameValue = (NameValue) (mMenuList.get(mMenuList.getFocus()));
								String location = nameValue.getValue();

								Element element = getDocument(location);
								if (element != null) {
									Tracker tracker = new Tracker(getElements(element), 0);

									DirectoryScreen directoryScreen = new DirectoryScreen((Videocasting) getBApp(),
											tracker);
									getBApp().push(directoryScreen, TRANSITION_LEFT);
									getBApp().flush();
								} else {
									ArrayList list = new ArrayList();
									Videocast videocast = null;
									try {
										List videocasts = VideocastManager.findByPath(location);
										if (videocasts != null && videocasts.size() > 0) {
											videocast = (Videocast) videocasts.get(0);
										} else {
											videocast = new Videocast(nameValue.getName(), 0, location, 0,
													new ArrayList());
											VideocastManager.createVideocast(videocast);
										}
									} catch (Exception ex) {
										Tools.logException(Videocasting.class, ex);
									}

									if (videocast != null) {
										list.add(videocast);

										Tracker tracker = new Tracker(list, 0);

										getBApp().push(new VideocastScreen((Videocasting) getBApp(), tracker),
												TRANSITION_LEFT);
										getBApp().flush();
									}
								}
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			NameValue nameValue = (NameValue) mMenuList.get(index);
			icon.setResource(mFolderIcon);

			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(Tools.cleanHTML(nameValue.getName()), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}
	}

	public class DirectoryScreen extends DefaultMenuScreen {

		public DirectoryScreen(Videocasting app, Tracker tracker) {
			this(app, tracker, false);
		}

		public DirectoryScreen(Videocasting app, Tracker tracker, boolean first) {
			super(app, "Videocasting");

			setFooter("Press ENTER for options");

			getBelow().setResource(mMenuBackground);

			mTracker = tracker;
			mFirst = first;

			Iterator iterator = mTracker.getList().iterator();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				mMenuList.add(element);
			}
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			mFocus = mTracker.getPos();
			mTracker = (Tracker)mTracker.clone();
			return super.handleEnter(arg, isReturn);
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();
					final Element element = (Element) (mMenuList.get(mMenuList.getFocus()));
					log.debug("rss0:" + element);
					if (isFolder(element)) {
						new Thread() {
							public void run() {
								try {
									mTracker.setPos(mMenuList.getFocus());
									Tracker tracker = new Tracker(getElements(element), 0);
									DirectoryScreen directoryScreen = new DirectoryScreen((Videocasting) getBApp(),
											tracker);
									getBApp().push(directoryScreen, TRANSITION_LEFT);
									getBApp().flush();
								} catch (Exception ex) {
									Tools.logException(Videocasting.class, ex);
									unload();
								}
							}
						}.start();
					} else {
						log.debug("rss01:" + element);
						new Thread() {
							public void run() {
								try {
									mTracker.setPos(mMenuList.getFocus());

									String location = getUrl(element);
									if (location != null) {
										if (location.toLowerCase().endsWith(".mp3")) {
											/*
											 * mTracker.setPos(mMenuList.getFocus());
											 * VideocastItemScreen
											 * videocastItemScreen = new
											 * VideocastItemScreen((Videocasting)
											 * getBApp(), mTracker);
											 *
											 * getBApp().push(videocastItemScreen,
											 * TRANSITION_LEFT);
											 * getBApp().flush();
											 */
										} else {
											log.debug("rss1");
											Element document = getDocument(location);
											log.debug("rss2:" + document);
											if (document != null) {
												if (isFolder(document)) {
													Tracker tracker = new Tracker(getElements(document), 0);

													getBApp().push(
															new DirectoryScreen((Videocasting) getBApp(), tracker),
															TRANSITION_LEFT);
													getBApp().flush();
												} else {
													log.debug("rss3:");
													ArrayList list = new ArrayList();
													Iterator iterator = mTracker.getList().iterator();
													while (iterator.hasNext()) {
														Element element = (Element) iterator.next();
														log.debug("rss4:" + element);
														location = getUrl(element);
														log.debug("rss5:" + location);

														Videocast videocast = null;
														try {
															List videocasts = VideocastManager.findByPath(location);
															if (videocasts != null && videocasts.size() > 0) {
																videocast = (Videocast) videocasts.get(0);
															} else {
																String title = element.attributeValue("text");
																if (title == null) {
																	title = "Unknown";
																} else if (title.length() > 255)
																	title = title.substring(0, 255);
																videocast = new Videocast(title, 0, location, 0,
																		new ArrayList());

																VideocastManager.createVideocast(videocast);
															}
														} catch (Exception ex) {
															Tools.logException(Videocasting.class, ex);
														}

														if (videocast != null)
															list.add(videocast);
													}

													log.debug("rss6:");
													Tracker tracker = new Tracker(list, mTracker.getPos());

													getBApp().push(
															new VideocastScreen((Videocasting) getBApp(), tracker),
															TRANSITION_LEFT);
													getBApp().flush();
												}
											} else {
												ArrayList list = new ArrayList();
												Iterator iterator = mTracker.getList().iterator();
												while (iterator.hasNext()) {
													Element element = (Element) iterator.next();
													location = getUrl(element);

													Videocast videocast = null;
													try {
														List videocasts = VideocastManager.findByPath(location);
														if (videocasts != null && videocasts.size() > 0) {
															videocast = (Videocast) videocasts.get(0);
														} else {
															String title = element.attributeValue("text");
															if (title == null) {
																title = "Unknown";
															} else if (title.length() > 255)
																title = title.substring(0, 255);
															videocast = new Videocast(title, 0, location, 0,
																	new ArrayList());
															VideocastManager.createVideocast(videocast);
														}
													} catch (Exception ex) {
														Tools.logException(Videocasting.class, ex);
													}

													if (videocast != null)
														list.add(videocast);
												}

												Tracker tracker = new Tracker(list, mTracker.getPos());

												getBApp().push(new VideocastScreen((Videocasting) getBApp(), tracker),
														TRANSITION_LEFT);
												getBApp().flush();
											}
										}
									}
								} catch (Exception ex) {
									Tools.logException(Videocasting.class, ex);
									unload();
								}
							}
						}.start();
					}

					return true;
				}
			} else if (action.equals("play")) {
				if (mMenuList.size() > 0) {
					load();
					final Element element = (Element) (mMenuList.get(mMenuList.getFocus()));
					if (isFolder(element)) {
						new Thread() {
							public void run() {
								try {
									mTracker.setPos(mMenuList.getFocus());
									// TODO Recurse
									Tracker tracker = new Tracker(getElements(element), 0);

									// MusicPlayerConfiguration
									// musicPlayerConfiguration =
									// Server.getServer().getMusicPlayerConfiguration();
									// tracker.setRandom(musicPlayerConfiguration.isRandomPlayFolders());
									getBApp()
											.push(new PlayerScreen((Videocasting) getBApp(), tracker), TRANSITION_LEFT);
									getBApp().flush();
								} catch (Exception ex) {
									Tools.logException(Videocasting.class, ex);
								}
							}
						}.start();
					} else {
						new Thread() {
							public void run() {
								try {
									mTracker.setPos(mMenuList.getFocus());
									// TODO Recurse
									Tracker tracker = new Tracker(getElements(element), 0);

									// MusicPlayerConfiguration
									// musicPlayerConfiguration =
									// Server.getServer().getMusicPlayerConfiguration();
									// tracker.setRandom(musicPlayerConfiguration.isRandomPlayFolders());
									getBApp()
											.push(new PlayerScreen((Videocasting) getBApp(), tracker), TRANSITION_LEFT);
									getBApp().flush();
								} catch (Exception ex) {
									Tools.logException(Videocasting.class, ex);
								}
							}
						}.start();
					}
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			Element element = (Element) mMenuList.get(index);
			String location = getUrl(element);
			if (location == null || location.indexOf(".") == -1)
				icon.setResource(mFolderIcon);
			else
				icon.setResource(mItemIcon);

			String text = Tools.getAttribute(element, "text");
			if (text == null)
				text = "Unknown";
			BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
			name.setShadow(true);
			name.setFlags(RSRC_HALIGN_LEFT);
			name.setValue(Tools.trim(Tools.cleanHTML(text), 40));
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				if (!mFirst) {
					postEvent(new BEvent.Action(this, "pop"));
					return true;
				}
				break;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		private Tracker mTracker;

		private boolean mFirst;
	}

	public class VideocastScreen extends DefaultScreen {

		private BList list;

		public VideocastScreen(Videocasting app, Tracker tracker) {
			this(app, tracker, false);
		}

		public VideocastScreen(Videocasting app, Tracker tracker, boolean showView) {
			super(app, "", true);

			setFooter("Press ENTER for options");

			mTracker = tracker;
			mShowView = showView;

			getBelow().setResource(mInfoBackground);

			int start = TOP;

			mBack = new BView(getNormal(), BORDER_LEFT + BODY_WIDTH - 50, start - 25, 50, 20);
			mBack.setResource(Color.BLACK);
			mBack.setVisible(false);
			mExplicitText = new BText(getNormal(), BORDER_LEFT, start - 25, BODY_WIDTH, 20);
			mExplicitText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
			mExplicitText.setFont("default-18.font");
			mExplicitText.setColor(Color.WHITE);
			mExplicitText.setShadow(true);

			mDateFormat = new SimpleDateFormat();
			mDateFormat.applyPattern("EEE M/d/yyyy hh:mm a");

			mDescriptionText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 90);
			mDescriptionText.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
			mDescriptionText.setFont("default-18-bold.font");
			mDescriptionText.setShadow(true);

			start += 100;

			mDateText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			mDateText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mDateText.setFont("default-18.font");
			mDateText.setShadow(true);

			mCategoryText = new BText(getNormal(), BORDER_LEFT, getHeight() - SAFE_TITLE_V - 20, BODY_WIDTH - 20, 20);
			mCategoryText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
			mCategoryText.setFont("default-16.font");
			mCategoryText.setShadow(true);

			start += 20;

			mAuthorText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH - 200, 40);
			mAuthorText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP);
			mAuthorText.setFont("default-18.font");
			mAuthorText.setShadow(true);

			mImage = new BView(getNormal(), BORDER_LEFT + BODY_WIDTH - 180, 220, 180, 180, false);
			mImage.setResource(Color.BLACK);
			mImage.setTransparency(0.75f);

			if (mShowView) {
				list = new DefaultOptionList(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 115,
						(int) Math.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.0), 125, 35);
				list.add("View");
			} else {
				list = new DefaultOptionList(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 80,
						(int) Math.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.0), 125, 35);
			}
			list.add("Subscribe");
			list.add("Don't do anything");

			setFocusDefault(list);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);
			updateView();

			return super.handleEnter(arg, isReturn);
		}

		private void updateView() {
			mBusy.setVisible(true);
			mBusy.flush();
			try {
				setPainting(false);
				final Videocast videocast = (Videocast) mTracker.getList().get(mTracker.getPos());
				if (videocast.getTracks() == null || videocast.getTracks().size() == 0) {
					if (getVideocast(videocast) != null) {
						try {
							VideocastManager.updateVideocast(videocast);
						} catch (Exception ex) {
							Tools.logException(Videocasting.class, ex);
						}
					}
				}

				setSmallTitle(videocast.getTitle());
				String description = null;
				if (videocast.getDescription() == null) {
					if (videocast.getSummary() == null) {
						description = videocast.getSubtitle();
					} else
						description = videocast.getSummary();
				} else
					description = videocast.getDescription();
				mDescriptionText.setValue(description);
				if (videocast.getDateUpdated() != null)
					mDateText.setValue(mDateFormat.format(videocast.getDateUpdated()));
				else
					mDateText.setValue(mDateFormat.format(new Date()));
				mCategoryText.setValue(videocast.getCategory());
				mAuthorText.setValue(videocast.getAuthor());
				if (videocast.getExplicit() != null && videocast.getExplicit().booleanValue()) {
					mExplicitText.setValue("Explicit");
					mBack.setVisible(true);
				} else {
					mExplicitText.setValue("");
					mBack.setVisible(false);
				}

				if (videocast.getImage() != null && videocast.getImage().length() > 0) {
					clearImage();

					mImageThread = new Thread() {
						public void run() {
							int x = mImage.getX();
							int y = mImage.getY();

							try {
								Image image = Tools.retrieveCachedImage(new URL(videocast.getImage()));
								if (image == null) {
									image = Tools.getImage(new URL(videocast.getImage()), -1, -1);
									if (image != null)
										Tools.cacheImage(image, image.getWidth(null), image.getHeight(null), videocast
												.getImage());
								}
								if (image != null) {
									synchronized (this) {
										mImage.setResource(createImage(image), RSRC_IMAGE_BESTFIT);
										mImage.setVisible(true);
										mImage.setTransparency(0.0f, mAnim);
										getBApp().flush();
									}
								} else {
									synchronized (this) {
										mImage.setVisible(false);										mImage.flush();										mImage.clearResource();
										getBApp().flush();
									}
								}
							} catch (Exception ex) {
								Tools.logException(MusicInfo.class, ex, "Could not retrieve cover");
							}
						}
					};
					mImageThread.start();
				} else {
					mImage.setVisible(false);
					if (mImage.getResource() != null)					{
						mImage.getResource().flush();						mImage.getResource().remove();					}
					getBApp().flush();
				}

				if (mShowView) {
					if (videocast.getStatus() == Videocast.STATUS_SUBSCRIBED)
						list.set(1, "Cancel subscription");
					else
						list.set(1, "Subscribe");
				} else {
					if (videocast.getStatus() == Videocast.STATUS_SUBSCRIBED)
						list.set(0, "Cancel subscription");
					else
						list.set(0, "Subscribe");
				}
			} finally {
				setPainting(true);
			}
			mBusy.setVisible(false);
		}

		private void clearImage() {
			try {
				setPainting(false);
				if (mImageThread != null && mImageThread.isAlive()) {
					mImageThread.interrupt();
					mImageThread = null;
				}
				mImage.setVisible(false);
				if (mImage.getResource() != null)				{
					mImage.getResource().flush();					mImage.getResource().remove();				}
				getBApp().flush();
			} finally {
				setPainting(true);
			}
		}

		public boolean handleExit() {
			clearImage();
			return super.handleExit();
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SELECT:
			case KEY_RIGHT:
				if (mShowView) {
					if (list.getFocus() == 0) {
						postEvent(new BEvent.Action(this, "view"));
						return true;
					} else if (list.getFocus() == 1) {
						DefaultApplication application = (DefaultApplication)getApp();
						if (!application.isDemoMode())
						{
							try {
								Videocast videocast = (Videocast) mTracker.getList().get(mTracker.getPos());
								if (videocast.getStatus() == Videocast.STATUS_SUBSCRIBED) {
									videocast.setStatus(Videocast.STATUS_DELETED);
									VideocastingFactory.remove(videocast);
									list.set(1, "Subscribe");
								} else {
									videocast.setStatus(Videocast.STATUS_SUBSCRIBED);
									try {
										VideocastManager.updateVideocast(videocast);
										((VideocastingFactory) getApp().getFactory()).update();
									} catch (Exception ex) {
										Tools.logException(Videocasting.class, ex);
									}
									list.set(1, "Cancel subscription");
								}

								list.flush();
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
						}
						return true;
					} else {
						postEvent(new BEvent.Action(this, "pop"));
						return true;
					}
				} else {
					if (list.getFocus() == 0) {
						DefaultApplication application = (DefaultApplication)getApp();
						if (!application.isDemoMode())
						{
							try {
								Videocast videocast = (Videocast) mTracker.getList().get(mTracker.getPos());
								if (videocast.getStatus() == Videocast.STATUS_SUBSCRIBED) {
									videocast.setStatus(Videocast.STATUS_DELETED);
									list.set(0, "Subscribe");
								} else {
									videocast.setStatus(Videocast.STATUS_SUBSCRIBED);
									list.set(0, "Cancel subscription");
								}

								try {
									VideocastManager.updateVideocast(videocast);
									((VideocastingFactory) getApp().getFactory()).update();
								} catch (Exception ex) {
									Tools.logException(Videocasting.class, ex);
								}

								list.flush();
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
						}
						return true;
					} else {
						postEvent(new BEvent.Action(this, "pop"));
						return true;
					}
				}
			case KEY_LEFT:
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
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
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

		public boolean handleAction(BView view, Object action) {
			if (action.equals("view")) {

				getBApp().play("select.snd");
				getBApp().flush();

				new Thread() {
					public void run() {
						Videocast videocast = (Videocast) mTracker.getList().get(mTracker.getPos());

						VideocastTrack[] videocastTrackArray = (VideocastTrack[]) videocast.getTracks().toArray(
								new VideocastTrack[0]);
						Arrays.sort(videocastTrackArray, new Comparator() {
							public int compare(Object o1, Object o2) {
								VideocastTrack videocastTrack1 = (VideocastTrack) o1;
								VideocastTrack videocastTrack2 = (VideocastTrack) o2;

								if (videocastTrack1.getPublicationDate() != null
										&& videocastTrack2.getPublicationDate() != null)
									return -videocastTrack1.getPublicationDate().compareTo(
											videocastTrack2.getPublicationDate());
								else
									return 0;
							}
						});

						ArrayList list = new ArrayList();
						for (int i = 0; i < videocastTrackArray.length; i++) {
							list.add(videocastTrackArray[i]);
						}

						Tracker tracker = new Tracker(list, 0);

						getBApp().push(new VideocastMenuScreen((Videocasting) getBApp(), tracker, videocast),
								TRANSITION_LEFT);
						getBApp().flush();
					}
				}.start();
				return true;
			}

			return super.handleAction(view, action);
		}

		private boolean mShowView;

		private Tracker mTracker;

		private SimpleDateFormat mDateFormat;

		private BText mDescriptionText;

		private BText mDateText;

		private BText mCategoryText;

		private BText mAuthorText;

		private BText mExplicitText;

		private BView mBack;

		private BView mImage;

		private Thread mImageThread;

		private Resource mAnim = getResource("*1000");
	}

	public class VideocastMenuScreen extends DefaultMenuScreen {
		public VideocastMenuScreen(Videocasting app, Tracker tracker, Videocast videocast) {
			super(app, "");

			setFooter("Press ENTER for options");

			setSmallTitle(videocast.getTitle());

			mTracker = tracker;
			mVideocast = videocast;

			mDateFormat = new SimpleDateFormat();
			mDateFormat.applyPattern("EEE M/d/yyyy hh:mm a");

			mDayFormat = new SimpleDateFormat();
			mDayFormat.applyPattern("M/d/yy");

			getBelow().setResource(mMenuBackground);

			VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) ((VideocastingFactory) getFactory())
					.getAppContext().getConfiguration();

			for (Iterator i = mTracker.getList().iterator(); i.hasNext(); /* Nothing */) {
				VideocastTrack videocastTrack = (VideocastTrack) i.next();
				mMenuList.add(videocastTrack);
			}
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			mFocus = mTracker.getPos();
			mTracker = (Tracker)mTracker.clone();
			return super.handleEnter(arg, isReturn);
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("push")) {
				if (mMenuList.size() > 0) {
					load();

					new Thread() {
						public void run() {
							mTracker.setPos(mMenuList.getFocus());
							VideocastItemScreen videocastItemScreen = new VideocastItemScreen((Videocasting) getBApp(),
									mTracker);
							getBApp().push(videocastItemScreen, TRANSITION_LEFT);
							getBApp().flush();
						}
					}.start();
					return true;
				}
			}
			return super.handleAction(view, action);
		}

		protected void createRow(BView parent, int index) {
			BView icon = new BView(parent, 9, 2, 32, 32);
			VideocastTrack videocastTrack = (VideocastTrack) mMenuList.get(index);
			icon.setResource(mItemIcon);

			if (videocastTrack.getTitle().length() == 0) {
				BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
				name.setShadow(true);
				name.setFlags(RSRC_HALIGN_LEFT);
				name.setValue(mDateFormat.format(videocastTrack.getPublicationDate()));
			} else {
				if (videocastTrack.getPublicationDate() != null) {
					BText name = new BText(parent, 50, 4, parent.getWidth() - 140, parent.getHeight() - 4);
					name.setShadow(true);
					name.setFlags(RSRC_HALIGN_LEFT);
					name.setValue(Tools.trim(Tools.cleanHTML(videocastTrack.getTitle()), 30));

					BText ratedText = new BText(parent, parent.getWidth() - 100 - parent.getHeight(), 4, 100, parent
							.getHeight() - 4);
					ratedText.setShadow(true);
					ratedText.setFlags(RSRC_HALIGN_RIGHT);
					ratedText.setValue(mDayFormat.format(videocastTrack.getPublicationDate()));
				} else {
					BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
					name.setShadow(true);
					name.setFlags(RSRC_HALIGN_LEFT);
					name.setValue(Tools.trim(Tools.cleanHTML(videocastTrack.getTitle()), 40));
				}
			}
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		private Tracker mTracker;

		private Videocast mVideocast;

		private SimpleDateFormat mDateFormat;

		private SimpleDateFormat mDayFormat;
	}

	public class VideocastItemScreen extends DefaultScreen {

		public VideocastItemScreen(Videocasting app, Tracker tracker) {
			super(app, "", true);

			setFooter("Press ENTER for options");

			mTracker = tracker;

			getBelow().setResource(mInfoBackground);

			int start = TOP;

			mBack = new BView(getNormal(), BORDER_LEFT + BODY_WIDTH - 50, start - 25, 50, 20);
			mBack.setResource(Color.BLACK);
			mBack.setVisible(false);
			mExplicitText = new BText(getNormal(), BORDER_LEFT, start - 25, BODY_WIDTH, 20);
			mExplicitText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
			mExplicitText.setFont("default-18.font");
			mExplicitText.setColor(Color.WHITE);
			mExplicitText.setShadow(true);

			mDateFormat = new SimpleDateFormat();
			mDateFormat.applyPattern("EEE M/d/yyyy hh:mm a");

			mDescriptionText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 90);
			mDescriptionText.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
			mDescriptionText.setFont("default-18-bold.font");
			mDescriptionText.setShadow(true);

			start += 100;

			mDateText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			mDateText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT);
			mDateText.setFont("default-18.font");
			mDateText.setShadow(true);

			mCategoryText = new BText(getNormal(), BORDER_LEFT, getHeight() - SAFE_TITLE_V - 20, BODY_WIDTH - 20, 20);
			mCategoryText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
			mCategoryText.setFont("default-16.font");
			mCategoryText.setShadow(true);

			start += 20;

			mAuthorText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH - 200, 40);
			mAuthorText.setFlags(IHmeProtocol.RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP);
			mAuthorText.setFont("default-18.font");
			mAuthorText.setShadow(true);

			start += 10;

			mStatusText = new BText(getAbove(), BORDER_LEFT, start, BODY_WIDTH, 30);
			mStatusText.setFlags(IHmeProtocol.RSRC_HALIGN_RIGHT);
			mStatusText.setFont("default-24-bolditalic.font");
			// mStatusText.setColor(new Color(150, 100, 100));
			mStatusText.setColor(Color.ORANGE);
			mStatusText.setShadow(true);
			mStatusText.setVisible(false);

			start += 120;

			mStatusBarBg = new BView(getAbove(), getWidth() - SAFE_TITLE_H - BODY_WIDTH / 3, start, BODY_WIDTH / 3, 30);
			// mStatusBarBg.setResource(Color.WHITE);
			mStatusBarBg.setResource(Color.BLACK);
			mStatusBarBg.setTransparency(.5f);
			mStatusBarBg.setVisible(false);
			mStatusBar = new BView(getAbove(), getWidth() - SAFE_TITLE_H - BODY_WIDTH / 3 + 2, start + 2,
					BODY_WIDTH / 3 - 4, 30 - 4);
			mStatusBar.setResource(Color.GREEN);
			mStatusBar.setVisible(false);

			mImage = new BView(getNormal(), BORDER_LEFT + BODY_WIDTH - 180, 220, 180, 180, false);
			mImage.setResource(Color.BLACK);
			mImage.setTransparency(0.75f);

			mListPlay = new DefaultOptionList(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 80,
					(int) Math.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.3), 90, 35);
			mListPlay.add("Play");
			mListPlay.add("Don't do anything");
			setFocus(mListPlay);

			// setFocusDefault(mListPlay);

			mListDelete = new DefaultOptionList(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 80,
					(int) Math.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.3), 120, 35);
			// mListDelete.add("Play");
			mListDelete.add("Delete");
			mListDelete.add("Don't do anything");
			mListDelete.setVisible(false);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);

			try {
				setPainting(false);
				if (mUpdateThread != null && mUpdateThread.isAlive())
					mUpdateThread.interrupt();
			} finally {
				setPainting(true);
			}

			updateView(false);

			mUpdateThread = new Thread() {
				public void run() {
					int counter = 0;
					while (getApp().getContext() != null) {
						try {
							if (counter++ < 10)
								sleep(1000 * 5);
							else
								sleep(1000 * 10);

							synchronized (this) {
								updateView(true);
							}
						} catch (InterruptedException ex) {
							return;
						} // handle silently for waking up
						catch (Exception ex2) {
							Tools.logException(Videocasting.class, ex2);
							return;
						}
					}
				}

				public void interrupt() {
					synchronized (this) {
						super.interrupt();
					}
				}
			};
			mUpdateThread.start();

			return super.handleEnter(arg, isReturn);
		}

		private void updateView(boolean isThread) {
			VideocastTrack videocastTrack = (VideocastTrack) mTracker.getList().get(mTracker.getPos());
			try {
				Videocast currentVideocast = VideocastManager.retrieveVideocast(videocastTrack.getVideocast());
				videocastTrack = currentVideocast.getTrack(videocastTrack.getUrl());
				if (videocastTrack != null) {
					setSmallTitle(videocastTrack.getTitle());
					String description = null;
					if (videocastTrack.getDescription() == null) {
						if (videocastTrack.getSummary() == null) {
							description = videocastTrack.getSubtitle();
						} else
							description = videocastTrack.getSummary();
					} else
						description = videocastTrack.getDescription();
					mDescriptionText.setValue(description);
					if (videocastTrack.getPublicationDate() != null)
						mDateText.setValue(mDateFormat.format(videocastTrack.getPublicationDate()));
					else
						mDateText.setValue(mDateFormat.format(new Date()));

					mCategoryText.setValue(videocastTrack.getCategory());
					mAuthorText.setValue(videocastTrack.getAuthor());

					if (videocastTrack.getExplicit() != null && videocastTrack.getExplicit().booleanValue()) {
						mBack.setVisible(true);
						mExplicitText.setValue("Explicit");
					} else {
						mExplicitText.setValue("");
						mBack.setVisible(false);
					}

					final Videocast videocast = VideocastManager.retrieveVideocast(videocastTrack.getVideocast());

					if (videocast != null && videocast.getImage() != null && videocast.getImage().length() > 0) {
						if (!isThread) {
							clearImage();
							mImageThread = new Thread() {
								public void run() {
									int x = mImage.getX();
									int y = mImage.getY();

									try {
										Image image = Tools.retrieveCachedImage(new URL(videocast.getImage()));
										if (image == null) {
											image = Tools.getImage(new URL(videocast.getImage()), -1, -1);
											if (image != null)
												Tools.cacheImage(image, image.getWidth(null), image.getHeight(null),
														videocast.getImage());
										}
										if (image != null) {
											synchronized (this) {
												mImage.setResource(createImage(image), RSRC_IMAGE_BESTFIT);
												mImage.setVisible(true);
												mImage.setTransparency(0.0f, mAnim);
												getBApp().flush();
											}
										} else {
											synchronized (this) {
												mImage.setVisible(false);												mImage.flush();												mImage.clearResource();

												getBApp().flush();
											}
										}
									} catch (Exception ex) {
										Tools.logException(MusicInfo.class, ex, "Could not retrieve cover");
									}
								}
							};
							mCurrentImage = videocast.getImage();
							mImageThread.start();
						}
					} else {
						try {
							setPainting(false);
							mImage.setVisible(false);
							if (mImage.getResource() != null)							{
								mImage.getResource().flush();								mImage.getResource().remove();							}
							getBApp().flush();
						} finally {
							setPainting(true);
						}
					}

					if (videocastTrack.getStatus() == VideocastTrack.STATUS_DOWNLOADING) {
						// || videocastTrack.getStatus() ==
						// VideocastTrack.STATUS_DOWNLOADED) {
						mStatusBarBg.setVisible(true);
						mStatusBar.setVisible(true);
						mStatusBar.setSize(1, mStatusBar.getHeight());
						// speedText.setVisible(true);

						if (videocastTrack.getDownloadTime() > 0) {
							long rate = (videocastTrack.getDownloadSize() / 1024) / videocastTrack.getDownloadTime();
							mStatusText.setValue(videocastTrack.getStatusString() + ": " + rate + " KB/Sec");
							// speedText.setValue(rate+" KB/Sec");
							if (videocastTrack.getStatus() == Video.STATUS_DOWNLOADED) {
								// mStatusBar.setSize(mStatusBarBg.getWidth() -
								// 4,
								// mStatusBar.getHeight());
								mStatusBar.setVisible(false);
								mStatusBarBg.setVisible(false);
							} else {
								float barFraction = videocastTrack.getDownloadSize() / (float) videocastTrack.getSize();
								if ((mStatusBarBg.getWidth() - 4) * barFraction < 1)
									mStatusBar.setSize(1, mStatusBar.getHeight());
								else
									mStatusBar.setSize((int) (barFraction * (mStatusBarBg.getWidth() - 4)), mStatusBar
											.getHeight());
							}
						} else if (videocastTrack.getStatus() != VideocastTrack.STATUS_DOWNLOADED) {
							String progress = "";
							/*
							 * for (int i = 0; i < mCounter; i++) progress =
							 * progress + "."; mStatusText.setValue("Connecting" +
							 * progress); // mSpeedText.setValue("0 KB/Sec");
							 * mStatusBar.setVisible(false); mCounter++;
							 */
						} else {
							mStatusText.setValue(videocastTrack.getStatusString());
							mStatusBar.setVisible(false);
							mStatusBarBg.setVisible(false);
						}
					} else {
						mStatusBarBg.setVisible(false);
						mStatusBar.setVisible(false);
						// speedText.setVisible(false);
						mStatusText.setValue(" ");
					}

					if (videocastTrack.getStatus() == VideocastTrack.STATUS_PLAYED
							|| videocastTrack.getStatus() == VideocastTrack.STATUS_DOWNLOADED) {
						if (!mListDelete.getVisible()) {
							mListPlay.setVisible(false);
							mListDelete.setFocus(0, false);
							mListDelete.setVisible(true);
							setFocus(mListDelete);
						}
					} else if (videocastTrack.getStatus() == VideocastTrack.STATUS_QUEUED
							|| videocastTrack.getStatus() == VideocastTrack.STATUS_DOWNLOADING) {
						mListPlay.set(0, "Cancel download");
						mListPlay.set(1, "Don't do anything");
						if (!mListPlay.getVisible()) {
							mListPlay.setFocus(0, false);
							mListPlay.setVisible(true);
							mListDelete.setVisible(false);
							setFocus(mListPlay);
						}
					} else {
						mListPlay.set(0, "Download");
						mListPlay.set(1, "Don't do anything");
						if (!mListPlay.getVisible()) {
							mListPlay.setFocus(0, false);
							mListPlay.setVisible(true);
							mListDelete.setVisible(false);
							setFocus(mListPlay);
						}
					}
					getBApp().flush();
				}
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex);
			}
		}

		private void clearImage() {
			try {
				setPainting(false);
				if (mImageThread != null && mImageThread.isAlive()) {
					mImageThread.interrupt();
					mImageThread = null;
				}
				mImage.setVisible(false);
				if (mImage.getResource() != null)				{
					mImage.getResource().flush();					mImage.getResource().remove();				}
				getBApp().flush();
			} finally {
				setPainting(true);
			}
		}

		public boolean handleExit() {
			clearImage();
			try {
				setPainting(false);
				if (mUpdateThread != null && mUpdateThread.isAlive())
					mUpdateThread.interrupt();
			} finally {
				setPainting(true);
			}
			mUpdateThread = null;
			return super.handleExit();
		}

		private String getCommand() {
			if (mListPlay.getVisible())
				return (String) mListPlay.get(mListPlay.getFocus());
			else
				return (String) mListDelete.get(mListDelete.getFocus());
		}

		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_SELECT:
			case KEY_RIGHT:
				String command = getCommand();
				if (command.equals("Delete")) {
					DefaultApplication application = (DefaultApplication)getApp();
					if (!application.isDemoMode())
					{
						VideocastTrack videocastTrack = (VideocastTrack) mTracker.getList().get(mTracker.getPos());
						try {
							Videocast videocast = VideocastManager.retrieveVideocast(videocastTrack.getVideocast());

							VideocastTrack track = videocast.getTrack(videocastTrack.getUrl());
							track.setStatus(VideocastTrack.STATUS_DELETED);
							track.setDownloadSize(0);
							track.setDownloadTime(0);
							Video video = track.getTrack();
							if (((DefaultApplication) getApp()).getCurrentAudio() != null
									&& ((DefaultApplication) getApp()).getCurrentAudio().getId().equals(video.getId())) {
								((DefaultApplication) getApp()).getPlayer().stopTrack();
							}
							track.setTrack(null);
							VideocastManager.updateVideocast(videocast);
							VideoManager.deleteVideo(video);
							File file = new File(video.getPath());
							if (file.exists()) {
								file.delete();
							}
							((VideocastingFactory) getApp().getFactory()).update();
							mListPlay.set(0, "Download");
							mListPlay.set(1, "Don't do anything");
							mListPlay.setFocus(0, false);
							mListPlay.setVisible(true);
							mListDelete.setVisible(false);
							setFocus(mListPlay);
						} catch (Exception ex) {
							Tools.logException(Videocasting.class, ex);
						}
					}
					break;
				} else if (command.equals("Play")) {
					postEvent(new BEvent.Action(this, "play"));
					return true;
				} else if (command.equals("Download")) {
					DefaultApplication application = (DefaultApplication)getApp();
					if (!application.isDemoMode())
					{
						VideocastTrack videocastTrack = (VideocastTrack) mTracker.getList().get(mTracker.getPos());
						try {
							Videocast videocast = VideocastManager.retrieveVideocast(videocastTrack.getVideocast());

							VideocastTrack track = videocast.getTrack(videocastTrack.getUrl());
							track.setStatus(VideocastTrack.STATUS_QUEUED);
							VideocastManager.updateVideocast(videocast);
							((VideocastingFactory) getApp().getFactory()).update();
							mListPlay.set(0, "Cancel download");
							mListPlay.set(1, "Don't do anything");
							mListPlay.setFocus(0, false);
							mListPlay.setVisible(true);
							mListDelete.setVisible(false);
							setFocus(mListPlay);
						} catch (Exception ex) {
							Tools.logException(Videocasting.class, ex);
						}
					}
					return true;
				} else if (command.equals("Cancel download")) {
					DefaultApplication application = (DefaultApplication)getApp();
					if (!application.isDemoMode())
					{
						VideocastTrack videocastTrack = (VideocastTrack) mTracker.getList().get(mTracker.getPos());
						try {
							Videocast videocast = VideocastManager.retrieveVideocast(videocastTrack.getVideocast());

							VideocastTrack track = videocast.getTrack(videocastTrack.getUrl());
							track.setStatus(VideocastTrack.STATUS_DOWNLOAD_CANCELLED);
							VideocastManager.updateVideocast(videocast);
							((VideocastingFactory) getApp().getFactory()).update();
							mListPlay.set(0, "Download");
							mListPlay.set(1, "Don't do anything");
							mListPlay.setFocus(0, false);
							mListPlay.setVisible(true);
							mListDelete.setVisible(false);
							setFocus(mListPlay);
						} catch (Exception ex) {
							Tools.logException(Videocasting.class, ex);
						}
					}
					return true;
				} else {
					postEvent(new BEvent.Action(this, "pop"));
					return true;
				}
			case KEY_LEFT:
				postEvent(new BEvent.Action(this, "pop"));
				return true;
			case KEY_CHANNELUP:
				getBApp().play("pageup.snd");
				getBApp().flush();
				getPrevPos();
				updateView(false);
				return true;
			case KEY_CHANNELDOWN:
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				updateView(false);
				return true;
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Videocasting) getBApp()), TRANSITION_LEFT);
				return true;
			}
			return super.handleKeyPress(code, rawcode);
		}

		public void getNextPos() {
			if (mTracker != null) {
				int pos = mTracker.getNextPos();
				Object object = mTracker.getList().get(pos);
				while (object == null) {
					pos = mTracker.getNextPos();
					object = mTracker.getList().get(pos);
				}
			}
		}

		public void getPrevPos() {
			if (mTracker != null) {
				int pos = mTracker.getPrevPos();
				Object object = mTracker.getList().get(pos);
				while (object == null) {
					pos = mTracker.getPrevPos();
					object = mTracker.getList().get(pos);
				}
			}
		}

		public boolean handleAction(BView view, Object action) {
			if (action.equals("play")) {

				getBApp().play("select.snd");
				getBApp().flush();

				new Thread() {
					public void run() {
						try {
							ArrayList tracks = new ArrayList();
							for (Iterator i = mTracker.getList().iterator(); i.hasNext();) {
								VideocastTrack videocastTrack = (VideocastTrack) i.next();
								Videocast videocast = VideocastManager.retrieveVideocast(videocastTrack.getVideocast());
								VideocastTrack track = videocast.getTrack(videocastTrack.getUrl());
								if (track.getTrack() != null) {
									tracks.add(new FileItem(track.getTitle(), new File(track.getTrack().getPath())));
								} else
									tracks.add(null);
							}

							Tracker tracker = new Tracker(tracks, mTracker.getPos());
							getBApp().push(new PlayerScreen((Videocasting) getBApp(), tracker), TRANSITION_LEFT);
							getBApp().flush();
						} catch (Exception ex) {
							Tools.logException(Videocasting.class, ex);
						}
					}
				}.start();
				return true;
			}

			return super.handleAction(view, action);
		}

		private SimpleDateFormat mDateFormat;

		private BText mDescriptionText;

		private BText mDateText;

		private BText mCategoryText;

		private BText mAuthorText;

		private BView mBack;

		private BText mExplicitText;

		private Tracker mTracker;

		private BText mStatusText;

		private BView mStatusBarBg;

		private BView mStatusBar;

		private Thread mUpdateThread;

		private BView mImage;

		private Thread mImageThread;

		private Resource mAnim = getResource("*1000");

		private BList mListPlay;

		private BList mListDelete;

		private String mCurrentImage;
	}

	public class PlayerScreen extends DefaultScreen {

		public PlayerScreen(Videocasting app, Tracker tracker) {
			super(app, true);

			getBelow().setResource(mPlayerBackground);

			boolean sameTrack = false;
			DefaultApplication defaultApplication = (DefaultApplication) getApp();
			Audio currentAudio = defaultApplication.getCurrentAudio();
			Tracker currentTracker = defaultApplication.getTracker();
			if (currentTracker != null && currentAudio != null) {
				try {
					Item newItem = (Item) tracker.getList().get(tracker.getPos());
					if (currentAudio.getPath().equals(((File) newItem.getValue()).getCanonicalPath())) {
						mTracker = currentTracker;
						sameTrack = true;
					} else {
						mTracker = tracker;
						app.setTracker(mTracker);
					}
				} catch (Exception ex) {
					mTracker = tracker;
					app.setTracker(mTracker);
				}
			} else {
				mTracker = tracker;
				app.setTracker(mTracker);
			}

			setTitle(" ");

			if (!sameTrack || getPlayer().getState() == Player.STOP)
				getPlayer().startTrack();
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			new Thread() {
				public void run() {
					mBusy.setVisible(true);
					mBusy.flush();

					synchronized (this) {
						try {
							setPainting(false);
							MusicPlayerConfiguration musicPlayerConfiguration = Server.getServer()
									.getMusicPlayerConfiguration();
							Videocast videocast = null;
							VideocastTrack track = null;
							try {
								Audio audio = ((DefaultApplication) getApp()).getCurrentAudio();
								boolean found = false;
								List list = VideocastManager.listAll();
								for (Iterator i = list.iterator(); i.hasNext(); /* Nothing */) {
									videocast = (Videocast) i.next();
									List tracks = videocast.getTracks();
									for (Iterator j = tracks.iterator(); j.hasNext(); /* Nothing */) {
										VideocastTrack videocastTrack = (VideocastTrack) j.next();
										if (videocastTrack.getTrack() != null
												&& videocastTrack.getTrack().getId().equals(audio.getId())) {
											track = videocastTrack;
											found = true;
											break;
										}
									}
									if (found)
										break;
								}
							} catch (Exception ex) {
								Tools.logException(Videocasting.class, ex);
							}
							if (musicPlayerConfiguration.getPlayer().equals(MusicPlayerConfiguration.CLASSIC))
								player = new MusicPlayer(PlayerScreen.this, BORDER_LEFT, SAFE_TITLE_H, BODY_WIDTH,
										BODY_HEIGHT, false, (DefaultApplication) getApp(), mTracker, false);
							else
								player = new WinampPlayer(PlayerScreen.this, 0, 0, PlayerScreen.this.getWidth(),
										PlayerScreen.this.getHeight(), false, (DefaultApplication) getApp(), mTracker);
							if (player != null) {
								player.updatePlayer();
								player.setVisible(true);
							}

							if (videocast != null && track != null) {
								try {
									track.setStatus(VideocastTrack.STATUS_PLAYED);
									videocast.setDatePlayed(new Date());
									int count = 0;
									if (videocast.getPlayCount()!=null)
										count = videocast.getPlayCount().intValue();
									videocast.setPlayCount(new Integer(count+1));
									VideocastManager.updateVideocast(videocast);
								} catch (Exception ex) {
									Tools.logException(Videocasting.class, ex);
								}
							}
						} finally {
							setPainting(true);
						}
					}
					if (player != null) {
						setFocusDefault(player);
						setFocus(player);
					}
					mBusy.setVisible(false);

					MusicPlayerConfiguration musicPlayerConfiguration = Server.getServer()
							.getMusicPlayerConfiguration();
					if (musicPlayerConfiguration.isScreensaver()) {
						mScreenSaver = new ScreenSaver(PlayerScreen.this);
						mScreenSaver.start();
					}
					getBApp().flush();
				}

				public void interrupt() {
					synchronized (this) {
						super.interrupt();
					}
				}
			}.start();

			return super.handleEnter(arg, isReturn);
		}

		public boolean handleExit() {
			try {
				setPainting(false);

				if (mScreenSaver != null && mScreenSaver.isAlive()) {
					mScreenSaver.interrupt();
					mScreenSaver = null;
				}
				if (player != null) {
					player.stopPlayer();
					player.setVisible(false);
					player.flush();					player.remove();
					player = null;
				}
			} finally {
				setPainting(true);
			}
			return super.handleExit();
		}

		public boolean handleKeyPress(int code, long rawcode) {
			if (mScreenSaver != null)
				mScreenSaver.handleKeyPress(code, rawcode);
			return super.handleKeyPress(code, rawcode);
		}

		private DefaultPlayer player;

		private Tracker mTracker;

		private ScreenSaver mScreenSaver;
	}

	public static class VideocastingFactory extends AppFactory {

		public void updateAppContext(AppContext appContext) {
			super.updateAppContext(appContext);

			update();
		}

		public void update() {
			if (mVideocastingThread != null) {
				mVideocastingThread.update();
			}
		}

		public void initialize() {
			VideocastingConfiguration videocastingConfiguration = (VideocastingConfiguration) getAppContext()
					.getConfiguration();

			mVideocastingThread = new VideocastingThread(videocastingConfiguration);
			mVideocastingThread.start();

			Server.getServer().publishVideo(
					new NameValue(videocastingConfiguration.getName(), System.getProperty("data") + File.separator
							+ "videocasts"));
		}

		public void remove()
		{
			try {
				List list = VideocastManager.listAllSubscribed();
				Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					Videocast videocast = (Videocast)iterator.next();
					remove(videocast);
				}
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex);
			}
		}

		public static void remove(Videocast videocast)
		{
			try {
				List tracks = videocast.getTracks();
				if (tracks!=null && tracks.size()>0)
				{
					Iterator trackIterator = tracks.iterator();
					while (trackIterator.hasNext())
					{
						VideocastTrack track = (VideocastTrack) trackIterator.next();
						if (track.getTrack()!=null)
						{
							File file = new File(track.getTrack().getPath());
							if (file.exists())
								file.delete();
							VideoManager.deleteVideo(track.getTrack());
						}
					}
					tracks.clear();
				}
				VideocastManager.deleteVideocast(videocast);
			} catch (Exception ex) {
				Tools.logException(Videocasting.class, ex);
			}
		}

		VideocastingThread mVideocastingThread;
	}
}