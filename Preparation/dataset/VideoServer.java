package org.lnicholls.galleon.goback;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.database.PersistentValueManager;
import org.lnicholls.galleon.database.Video;
import org.lnicholls.galleon.database.VideoManager;
import org.lnicholls.galleon.server.Constants;
import org.lnicholls.galleon.server.GoBackConfiguration;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.server.ServerConfiguration;
import org.lnicholls.galleon.util.FileFilters;
import org.lnicholls.galleon.util.FileSystemContainer;
import org.lnicholls.galleon.util.NameValue;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.util.FileSystemContainer.Item;
import org.lnicholls.galleon.media.*;

import com.tivo.hme.host.http.server.HttpRequest;
import com.tivo.hme.host.http.server.HttpServer;
import com.tivo.hme.host.http.share.Headers;
import com.tivo.hme.host.util.Config;

public class VideoServer extends HttpServer {

	// http://<your tivoip>/TiVoConnect?Command=QueryContainer&Container=%2F

	// http://<your tivoip>/TiVoConnect?Command=QueryContainer&Container=GalleonRecordings&Recurse=Yes&SortOrder=!CaptureDate&ItemCount=8&Filter=x-tivo-container%2Ftivo-videos,x-tivo-container%2Ffolder,video%2Fx-tivo-mpeg,video%2F*

	private static Logger log = Logger.getLogger(VideoServer.class.getName());

	public VideoServer(Config config) throws IOException {
		super(config);
		config.put("http.acceptor.name", "VideoServer");

		InetAddress inetAddress = InetAddress.getLocalHost();
		mHost = inetAddress.getHostName();

		mFileDateFormat = new SimpleDateFormat();
		mFileDateFormat.applyPattern("EEE MMM d yyyy hh mma");
		mTimeDateFormat = new SimpleDateFormat();
		mTimeDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		mTimeDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"); // 2005-02-23T11:59:58Z
		mDurationFormat = new SimpleDateFormat();
		mDurationFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		mDurationFormat.applyPattern("'PT'HH'H'mm'M'"); // PT1H30M
		mCalendar = new GregorianCalendar();

		start();
	}

	protected void handleException(Object obj, Throwable throwable) {
		if (throwable instanceof SocketException)
			return;
		else
			return;
	}

	void reply(HttpRequest httprequest, int i, String s) throws IOException {
		log.debug(httprequest.getInetAddress().getHostAddress() + " " + httprequest.getURI() + " HTTP "
				+ httprequest.getMethod() + " - " + i + " - " + s);
		httprequest.reply(i, s);
	}

	public void handle(HttpRequest httprequest) throws IOException {
		String s = httprequest.getURI();
		log.debug(s);
		String remoteAddress = httprequest.getInetAddress().getHostAddress();
		log.debug(remoteAddress);

		ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
		GoBackConfiguration goBackConfiguration = serverConfiguration.getGoBackConfiguration();

		ItemURL itemURL = new ItemURL(s);

		if (itemURL.getParameter(Constants.COMMAND) != null
				&& itemURL.getParameter(Constants.COMMAND).equalsIgnoreCase(Constants.COMMAND_QUERY_CONTAINER)) {
			try {
				PrintWriter printWriter = null;
				try {
					httprequest.reply(200, "Success");
					String container = getContainer(itemURL, remoteAddress);
					OutputStream outputstream = httprequest.getOutputStream(container.length());
					printWriter = new PrintWriter(outputstream);
					printWriter.print(container);
				} finally {
					if (printWriter != null)
						printWriter.close();
				}
			} catch (IOException ioexception1) {
				log.debug(httprequest.getInetAddress().getHostAddress() + " I/O Exception handling " + " HTTP "
						+ httprequest.getMethod() + " " + s + ": " + ioexception1.getMessage());
			}
		} else {
			if (itemURL.getPath().startsWith("/TiVoConnect/TivoNowPlaying/Galleon")) {
				String path = null;
				if (itemURL.getPath().startsWith("/TiVoConnect/TivoNowPlaying/GalleonRecordings/"))
					path = serverConfiguration.getRecordingsPath() + "/"
							+ itemURL.getPath().substring("/TiVoConnect/TivoNowPlaying/GalleonRecordings/".length());
				else {
					String container = itemURL.getPath()
							.substring("/TiVoConnect/TivoNowPlaying/GalleonExtra/".length());
					StringTokenizer tokenizer = new StringTokenizer(container, "/");
					if (tokenizer.hasMoreTokens()) {
						String name = tokenizer.nextToken();
						List paths = goBackConfiguration.getPaths();
						List published = new ArrayList();
						published.addAll(mPublished);
						published.addAll(paths);

						Iterator iterator = published.iterator();
						while (iterator.hasNext()) {
							NameValue nameValue = (NameValue) iterator.next();
							if (nameValue.getName().equals(name)) {
								path = nameValue.getValue();
								while (tokenizer.hasMoreTokens())
									path = path + "/" + tokenizer.nextToken();
								break;
							}
						}
					}
				}
				File file = new File(path);				if (file.exists()) {
					try {
						PrintWriter printWriter = null;
						try {
							httprequest.reply(200, "Success");

							String details = getVideoDetails(file);
							//details = Tools.getFile(new File("d:/galleon/TiVoVideoDetails.xml"));
							if (details!=null)
							{
								OutputStream outputstream = httprequest.getOutputStream(details.length());
								printWriter = new PrintWriter(outputstream);
								printWriter.print(details);
							}
						} finally {
							if (printWriter != null)
								printWriter.close();
						}
						return;
					} catch (IOException ioexception1) {
						log.debug(httprequest.getInetAddress().getHostAddress() + " I/O Exception handling " + " HTTP "
								+ httprequest.getMethod() + " " + s + ": " + ioexception1.getMessage());
					}
				}
			}

			try {
				String path = null;
				if (itemURL.getPath().startsWith("/TiVoConnect/GalleonRecordings/"))
					path = serverConfiguration.getRecordingsPath() + "/"
							+ itemURL.getPath().substring("/TiVoConnect/GalleonRecordings/".length());
				else
				if (itemURL.getPath().startsWith("/TiVoConnect/GalleonExtra/"))
				{
					String container = itemURL.getPath().substring("/TiVoConnect/GalleonExtra/".length());
					StringTokenizer tokenizer = new StringTokenizer(container, "/");
					if (tokenizer.hasMoreTokens()) {
						String name = tokenizer.nextToken();

						List paths = goBackConfiguration.getPaths();
						List published = new ArrayList();
						published.addAll(mPublished);
						published.addAll(paths);
						Iterator iterator = published.iterator();
						while (iterator.hasNext()) {
							NameValue nameValue = (NameValue) iterator.next();
							if (nameValue.getName().equals(name)) {
								path = nameValue.getValue();
								while (tokenizer.hasMoreTokens())
									path = path + "/" + tokenizer.nextToken();
								break;
							}
						}
					}
				}
				if (path!=null)
				{
					File file = new File(path);
					if (!file.exists()) {
						// Handle shortcuts
						File directory = new File(serverConfiguration.getRecordingsPath());
						FileSystemContainer fileSystemContainer = new FileSystemContainer(directory.getCanonicalPath(),
								true);
						List files = fileSystemContainer.getItems(FileFilters.videoFilter);
						for (int i = 0; i < files.size(); i++) {
							Item nameFile = (Item) files.get(i);
							File match = (File) nameFile.getValue();
							if (match.getName().equals(path)) {
								file = match;
								break;
							}
						}
					}
					if (file.exists()) {
						InputStream inputstream = new FileInputStream(file);
						if (inputstream!=null && inputstream.available()>0)
						{
							if (itemURL.getParameter(Constants.PARAMETER_SEEK)!=null)
							{
								httprequest.reply(206, "Partial Content");
								try {
									int seek = Integer.parseInt(itemURL.getParameter(Constants.PARAMETER_SEEK));
									inputstream.skip(seek);
								} catch (Exception ex) {
								}
							}
							else
							{
								Headers headers = httprequest.getHeaders();
								String range = headers.get("range");
								if (range!=null)
								{
									Pattern pattern = Pattern.compile("bytes=(.*)-");
									Matcher matcher = pattern.matcher(range);
									if (matcher.find()) {
							           range = matcher.group(1);
									}
									try {
										int seek = Integer.parseInt(range);
										log.debug("retry at: "+seek);
										if (seek>0)
										{
											httprequest.reply(206, "Partial Content");
											inputstream.skip(seek);
										}
										else
											httprequest.reply(200, "Success");
									} catch (Exception ex) {
									}
								}
								else
									httprequest.reply(200, "Success");
							}

							try {
								long startTime = System.currentTimeMillis();
								long lastTime = startTime;
								PersistentValueManager.savePersistentValue("VideoServer.lastUpdate", new Date().toString());
								OutputStream outputstream = httprequest.getOutputStream(inputstream.available());
								byte abyte0[] = new byte[4380];
								int i;
								while ((i = inputstream.read(abyte0, 0, abyte0.length)) > 0)
								{
									outputstream.write(abyte0, 0, i);

									if ((System.currentTimeMillis()-lastTime) > 1000*30)
									{
										PersistentValueManager.savePersistentValue("VideoServer.lastUpdate", new Date().toString());
										lastTime = System.currentTimeMillis();
									}
								}
								outputstream.close();
								long endTime = System.currentTimeMillis();

								Video video = null;
								try {
									List list = VideoManager.findByPath(file.getCanonicalPath());
									if (list != null && list.size() > 0) {
										video = (Video) list.get(0);
									} else {
										path = file.getAbsolutePath();
										path = path.substring(0, 1).toLowerCase() + path.substring(1);
										list = VideoManager.findByPath(path);
										if (list != null && list.size() > 0) {
											video = (Video) list.get(0);
										}
									}
									if (video!=null)
									{
										video.setDateUploaded(new Date());
										video.setUploaded(remoteAddress);
										VideoManager.updateVideo(video);
									}
								} catch (Exception ex) {
									Tools.logException(VideoServer.class, ex);
								}

								log.info("GoBack upload duration: " + (endTime - startTime) / 1000 + " sec for " + file);
							} finally {
								inputstream.close();
							}
						}
					} else
						httprequest.reply(404, "File not found");
				}
				else
					httprequest.reply(404, "File not found");
			} catch (IOException ioexception1) {
				log.debug(httprequest.getInetAddress().getHostAddress() + " I/O Exception handling " + " HTTP "
						+ httprequest.getMethod() + " " + s + ": " + ioexception1.getMessage());
			} catch (Exception ex) {
				Tools.logException(VideoServer.class, ex);
			}
		}
	}

	private String getContainer(ItemURL itemURL, String address) {
		ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
		GoBackConfiguration goBackConfiguration = serverConfiguration.getGoBackConfiguration();

		File directory = new File(serverConfiguration.getRecordingsPath());
		if (itemURL.getParameter(Constants.COMMAND).equalsIgnoreCase(Constants.COMMAND_QUERY_CONTAINER)) {
			if (itemURL.getParameter(Constants.PARAMETER_CONTAINER).equals("/")) {
				StringBuffer buffer = new StringBuffer();
				synchronized (buffer) {
					if (!Tools.isLocal(address) && serverConfiguration.canShare())
					{
						log.info("Remote connection: "+address);
						List apps = Server.getServer().getAppUrls(true);
						int counter = apps.size();

						buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
						buffer.append("<TiVoContainer>\n");
						buffer.append("<Details>\n");
						buffer.append("<Title>" + mHost + "</Title>\n");
						buffer.append("<ContentType>x-container/tivo-server</ContentType>\n");
						buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
						buffer.append("<TotalItems>" + counter + "</TotalItems>\n");
						buffer.append("</Details>\n");
						if (apps.size() > 0)
						{
							Pattern pattern = Pattern.compile("^http://(.*):(.*)/(.*)/$");
							Iterator iterator = apps.iterator();
							while (iterator.hasNext())
							{
								NameValue nameValue = (NameValue)iterator.next();
								Matcher matcher = pattern.matcher(nameValue.getValue());
								if (matcher.find()) {
									buffer.append("<Item>\n");
									buffer.append("<Details>\n");
									buffer.append("<ContentType>application/x-hme</ContentType>\n");
									buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
									buffer.append("<Title>"+Tools.escapeXMLChars(nameValue.getName())+"</Title>\n");
									buffer.append("</Details>\n");
									buffer.append("<Links>\n");
									buffer.append("<Content>\n");
									buffer.append("<Url>"+"http://"+serverConfiguration.getPublicIPAddress()+":"+serverConfiguration.getPort()+"/"+matcher.group(3)+"/"+"</Url>\n");
									buffer.append("</Content>\n");
									buffer.append("</Links>\n");
									buffer.append("</Item>\n");
								}
							}
						}

						buffer.append("<ItemStart>0</ItemStart>\n");
						buffer.append("<ItemCount>" + counter + "</ItemCount>\n");
						buffer.append("</TiVoContainer>\n");
						buffer.append("<!-- Copyright (c) 2005, 2006 Leon Nicholls. All rights reserved.-->\n");
					}
					else
					if (goBackConfiguration.isEnabled()) {
						int counter = 0;
						if (goBackConfiguration.isPublishTiVoRecordings())
							counter++;
						if (goBackConfiguration.getPaths().size() > 0)
							counter = counter + goBackConfiguration.getPaths().size();
						if (mPublished.size() > 0)
							counter = counter + mPublished.size();

						buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
						buffer.append("<TiVoContainer>\n");
						buffer.append("<Details>\n");
						buffer.append("<Title>" + mHost + "</Title>\n");
						buffer.append("<ContentType>x-container/tivo-server</ContentType>\n");
						buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
						buffer.append("<TotalItems>" + counter + "</TotalItems>\n");
						buffer.append("</Details>\n");
						if (goBackConfiguration.isPublishTiVoRecordings()) {
							buffer.append("<Item>\n");
							buffer.append("<Details>\n");
							buffer.append("<Title>" + Tools.escapeXMLChars(mHost) + "</Title>\n");
							buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
							buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
							buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(directory.lastModified()))
									+ "</LastChangeDate>\n");
							buffer.append("</Details>\n");
							buffer.append("<Links>\n");
							buffer.append("<Content>\n");
							buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":"
									+ Server.getServer().getHMOPort()
									+ "/TiVoConnect?Command=QueryContainer&amp;Container=GalleonRecordings</Url>\n");
							buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
							buffer.append("</Content>\n");
							buffer.append("</Links>\n");
							buffer.append("</Item>\n");
						}
						if (goBackConfiguration.getPaths().size() > 0) {
							List paths = goBackConfiguration.getPaths();
							Iterator iterator = paths.iterator();
							while (iterator.hasNext()) {
								NameValue nameValue = (NameValue) iterator.next();
								directory = new File(nameValue.getValue());
								buffer.append("<Item>\n");
								buffer.append("<Details>\n");
								buffer.append("<Title>" + Tools.escapeXMLChars(nameValue.getName()) + "</Title>\n");
								buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
								buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
								buffer.append("<LastChangeDate>0x"
										+ Tools.dateToHex(new Date(directory.lastModified())) + "</LastChangeDate>\n");
								buffer.append("</Details>\n");
								buffer.append("<Links>\n");
								buffer.append("<Content>\n");
								buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":"
										+ Server.getServer().getHMOPort()
										+ "/TiVoConnect?Command=QueryContainer&amp;Container=GalleonExtra/"
										+ URLEncoder.encode(nameValue.getName()) + "</Url>\n");
								buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
								buffer.append("</Content>\n");
								buffer.append("</Links>\n");
								buffer.append("</Item>\n");
							}
						}
						if (mPublished.size() > 0) {
							Iterator iterator = mPublished.iterator();
							while (iterator.hasNext()) {
								NameValue nameValue = (NameValue) iterator.next();
								directory = new File(nameValue.getValue());
								buffer.append("<Item>\n");
								buffer.append("<Details>\n");
								buffer.append("<Title>" + Tools.escapeXMLChars(nameValue.getName()) + "</Title>\n");
								buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
								buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
								buffer.append("<LastChangeDate>0x"
										+ Tools.dateToHex(new Date(directory.lastModified())) + "</LastChangeDate>\n");
								buffer.append("</Details>\n");
								buffer.append("<Links>\n");
								buffer.append("<Content>\n");
								buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":"
										+ Server.getServer().getHMOPort()
										+ "/TiVoConnect?Command=QueryContainer&amp;Container=GalleonExtra/"
										+ URLEncoder.encode(nameValue.getName()) + "</Url>\n");
								buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
								buffer.append("</Content>\n");
								buffer.append("</Links>\n");
								buffer.append("</Item>\n");
							}
						}

						buffer.append("<ItemStart>0</ItemStart>\n");
						buffer.append("<ItemCount>" + counter + "</ItemCount>\n");
						buffer.append("</TiVoContainer>\n");
						buffer.append("<!-- Copyright (c) 2005, 2006 Leon Nicholls. All rights reserved.-->\n");
					} else {
						buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
						buffer.append("<TiVoContainer>\n");
						buffer.append("<Details>\n");
						buffer.append("<Title>" + mHost + "</Title>\n");
						buffer.append("<ContentType>x-container/tivo-server</ContentType>\n");
						buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
						buffer.append("<TotalItems>0</TotalItems>\n");
						buffer.append("</Details>\n");
						buffer.append("<ItemStart>0</ItemStart>\n");
						buffer.append("<ItemCount>0</ItemCount>\n");
						buffer.append("</TiVoContainer>\n");
						buffer.append("<!-- Copyright (c) 2005, 2006 Leon Nicholls. All rights reserved.-->\n");
					}
				}
				return buffer.toString();
			} else if (itemURL.getParameter(Constants.PARAMETER_CONTAINER).startsWith("Galleon")) {
				try {
					if (!itemURL.getParameter(Constants.PARAMETER_CONTAINER).equals("GalleonRecordings")) {
						String container = itemURL.getParameter(Constants.PARAMETER_CONTAINER);						StringTokenizer tokenizer = new StringTokenizer(container, "/");
						if (tokenizer.hasMoreTokens()) {
							tokenizer.nextToken(); // galleon
							if (tokenizer.hasMoreTokens()) {
								String name = tokenizer.nextToken();								String rest = "";								while (tokenizer.hasMoreTokens())									rest = rest + "/" + tokenizer.nextToken();								List paths = goBackConfiguration.getPaths();
								List published = new ArrayList();
								published.addAll(mPublished);
								published.addAll(paths);
								Iterator iterator = published.iterator();
								while (iterator.hasNext()) {
									NameValue nameValue = (NameValue) iterator.next();
									if (nameValue.getName().equals(name)) {
										directory = new File(nameValue.getValue()+rest);
										break;
									}
								}								// Could be a dir in published dir								File subdir = new File(serverConfiguration.getRecordingsPath()+"/"+name+rest);								if (subdir.exists())								{									directory = subdir;								}							}
						}
					}

					boolean folders = false;
					List files = new ArrayList();
					if (itemURL.getParameter(Constants.PARAMETER_RECURSE) != null)
					{
						FileSystemContainer fileSystemContainer = new FileSystemContainer(directory.getCanonicalPath(),
								true);
						files = fileSystemContainer.getItemsSorted(FileFilters.videoFilter);
					}
					else
					{
						FileSystemContainer fileSystemContainer = new FileSystemContainer(directory.getCanonicalPath(),
								false);
						files = fileSystemContainer.getItemsSorted(FileFilters.videoDirectoryFilter);
						folders = true;
					}

					if (itemURL.getParameter(Constants.PARAMETER_SORT_ORDER) != null) {
						if (!itemURL.getParameter(Constants.PARAMETER_SORT_ORDER)
								.equals(Constants.PARAMETER_SORT_TITLE)) {
							final HashMap dates = new HashMap();
							Iterator iterator = files.iterator();
							while (iterator.hasNext()) {
								Item item = (Item) iterator.next();
								File file = (File) item.getValue();
								Video video = null;
								try {
									List list = VideoManager.findByPath(file.getCanonicalPath());
									if (list != null && list.size() > 0) {
										video = (Video) list.get(0);
									} else {
										String path = file.getAbsolutePath();
										path = path.substring(0, 1).toLowerCase() + path.substring(1);
										list = VideoManager.findByPath(path);
										if (list != null && list.size() > 0) {
											video = (Video) list.get(0);
										}
									}
								} catch (Exception ex) {
									Tools.logException(VideoServer.class, ex);
								}

								if (video == null) {
									video = (Video)MediaManager.getMedia(file.getAbsolutePath());
								}
								else
								if (file.lastModified()!=video.getDateModified().getTime())
								{
									video = (Video)MediaManager.getMedia(file.getAbsolutePath());
								}

								long date = file.lastModified();
								if (video != null) {
									if (video.getOriginalAirDate() != null)
										date = video.getOriginalAirDate().getTime();
									else
										date = video.getDateRecorded().getTime();
								}
								dates.put(file, new Long(date));
							}

							// sort by capture date
							Item[] sortArray = (Item[]) files.toArray(new Item[0]);
							Arrays.sort(sortArray, new Comparator() {
								public int compare(Object o1, Object o2) {
									Item item1 = (Item) o1;
									File file1 = (File) item1.getValue();
									Item item2 = (Item) o2;
									File file2 = (File) item2.getValue();

									Long date1 = (Long) dates.get(file1);
									Long date2 = (Long) dates.get(file2);

									return -date1.compareTo(date2);
								}
							});

							files = new ArrayList();
							for (int i = 0; i < sortArray.length; i++) {
								Item item = (Item) sortArray[i];
								files.add(item);
							}
						}
					}

					int itemCount = files.size();
					if (itemURL.getParameter(Constants.PARAMETER_ITEM_COUNT) != null) {
						itemCount = Integer.parseInt(itemURL.getParameter(Constants.PARAMETER_ITEM_COUNT));
					}

					StringBuffer buffer = new StringBuffer();
					synchronized (buffer) {
						int start = 0;
						String anchorItem = itemURL.getParameter(Constants.PARAMETER_ANCHOR_ITEM);
						if (anchorItem != null) {
							ItemURL anchorItemURL = new ItemURL(anchorItem);
							String path = null;
							if (anchorItemURL.getPath().startsWith("/TiVoConnect/GalleonRecordings/")) {
								// TODO add real path
								path = anchorItemURL.getPath().substring("/TiVoConnect/GalleonRecordings/".length());								StringTokenizer tokenizer = new StringTokenizer(path, "/");								while (tokenizer.hasMoreTokens()) {									path = tokenizer.nextToken();								}
							} else {
								String container = anchorItemURL.getParameter(Constants.PARAMETER_CONTAINER);
								if (container!=null)
								{
									if (container.indexOf("GalleonExtra/")!=-1)
										container = container.substring(
											"GalleonExtra/".length());
								}
								else
								if (anchorItemURL.getPath().indexOf("/TiVoConnect/GalleonExtra/")!=-1)
									container = anchorItemURL.getPath().substring(
										"/TiVoConnect/GalleonExtra/".length());
								StringTokenizer tokenizer = new StringTokenizer(container, "/");
								if (tokenizer.hasMoreTokens()) {
									String extra = tokenizer.nextToken();

									List paths = goBackConfiguration.getPaths();
									List published = new ArrayList();
									published.addAll(mPublished);
									published.addAll(paths);
									Iterator iterator = published.iterator();
									while (iterator.hasNext()) {
										NameValue nameValue = (NameValue) iterator.next();
										if (nameValue.getName().equals(extra)) {
											String rest = "";
											while (tokenizer.hasMoreTokens())
												rest = rest + "/" + tokenizer.nextToken();
											path = nameValue.getValue()+rest;
											break;
										}
									}

								}
							}							if (path!=null)
							{
								path = path.replaceAll("\\\\", "/");
								File pathFile = new File(path);
								for (int i = 0; i < files.size(); i++) {
									Item nameFile = (Item) files.get(i);
									File file = (File) nameFile.getValue();
									String filePath = getFilePath(file, directory);
									boolean match = pathFile.equals(file) || filePath.equals(path);
									if (match) {
										start = i;
										String anchorOffset = itemURL.getParameter(Constants.PARAMETER_ANCHOR_OFFSET);
										if (anchorOffset != null) {
											try {
												start = start + Integer.parseInt(anchorOffset) + 1;
											} catch (Exception ex) {
											}
										}
										break;
									}
								}
							}
						}						if (start < 0)
							start = 0;

						int limit = start + itemCount;
						if (limit > files.size()) {
							limit = files.size();
							itemCount = limit - start;
						} else if (limit < 0) {
							itemCount = -itemCount;
							limit = files.size();
							start = files.size() - itemCount;
							if (limit < 0) {
								start = 0;
								limit = -limit;
							}
						}
						if (itemCount == 1) {
							Item nameFile = (Item) files.get(start);
							String filename = nameFile.getName();
							File file = (File) nameFile.getValue();
							String filePath = getFilePath(file, directory);
							// TODO Hack for skipping folders
							if (nameFile.isFolder()) {
								start = start + 1;
								itemCount = limit - start;
							}
						}

						buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
						buffer.append("<TiVoContainer>\n");
						buffer.append("<ItemStart>" + start + "</ItemStart>\n");
						buffer.append("<ItemCount>" + itemCount + "</ItemCount>\n");
						buffer.append("<Details>\n");
						buffer.append("<Title>" + mHost + "</Title>\n");
						buffer.append("<ContentType>x-container/tivo-videos</ContentType>\n");
						buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
						buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(directory.lastModified()))
								+ "</LastChangeDate>\n");
						buffer.append("<TotalItems>" + files.size() + "</TotalItems>\n");
						// buffer.append("<TotalDuration>26797000</TotalDuration>\n");
						buffer.append("</Details>\n");

						for (int i = start; i < limit; i++) {
							Item nameFile = (Item) files.get(i);
							String filename = nameFile.getName();
							File file = (File) nameFile.getValue();
							String filePath = getFilePath(file, directory);
							if (nameFile.isFolder()) {
								buffer.append("<Item>\n");
								buffer.append("<Details>\n");
								buffer.append("<Title>" + Tools.escapeXMLChars(filename) + "</Title>\n");
								buffer.append("<ContentType>x-tivo-container/folder</ContentType>\n");
								buffer.append("<SourceFormat>x-tivo-container/tivo-dvr</SourceFormat>\n");
								buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(file.lastModified()))
										+ "</LastChangeDate>\n");
								buffer.append("</Details>\n");
								buffer.append("<Links>\n");
								buffer.append("<Content>\n");
								buffer.append("<Url>http://"
										+ serverConfiguration.getIPAddress()
										+ ":"
										+ Server.getServer().getHMOPort()
										+ "/TiVoConnect?Command=QueryContainer&amp;Container="
										+ URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER) + "/"
												+ Tools.escapeXMLChars(filePath)) + "</Url>\n");
								buffer.append("<ContentType>x-tivo-container/folder</ContentType>\n");
								buffer.append("</Content>\n");
								buffer.append("</Links>\n");
								buffer.append("</Item>\n");
							} else {
								Video video = null;
								try {
									List list = VideoManager.findByPath(file.getCanonicalPath());
									if (list != null && list.size() > 0) {
										video = (Video) list.get(0);
									} else {
										String path = file.getAbsolutePath();
										path = path.substring(0, 1).toLowerCase() + path.substring(1);
										list = VideoManager.findByPath(path);
										if (list != null && list.size() > 0) {
											video = (Video) list.get(0);
										}
									}								} catch (Exception ex) {
									log.error("Video retrieve failed", ex);
								}
								if (video == null) {
									// Try to resync video file with database if moved									try {										List list = VideoManager.findByFilename(file.getName());										if (list != null && list.size() == 1) {											for (int j=0;j<list.size();j++)											{												video = (Video) list.get(j);												if (!video.getPath().equals(file.getCanonicalPath()))												{													video.setPath(file.getCanonicalPath());													VideoManager.updateVideo(video);													break;												}											}										}									} catch (Exception ex) {										log.error("Video find failed", ex);									}									if (video==null)									{										try {
											video = (Video)MediaManager.getMedia(file.getAbsolutePath());
											VideoManager.createVideo(video);
										} catch (Exception ex) {
											log.error("Video create failed", ex);
										}									}
								}								else								if (file.lastModified()!=video.getDateModified().getTime())
								{
									video = (Video)MediaManager.getMedia(file.getAbsolutePath());
								}

								if (video == null ) { // || !file.getName().toLowerCase().endsWith(".tivo")) {
									buffer.append("<Item>\n");
									buffer.append("<Details>\n");
									buffer.append("<Title>" + Tools.escapeXMLChars(filename) + "</Title>\n");
									buffer.append("<ContentType>video/mpeg</ContentType>\n");
									buffer.append("<SourceFormat>video/mpeg</SourceFormat>\n");
									buffer.append("<CaptureDate>0x" + Tools.dateToHex(new Date(file.lastModified()))
											+ "</CaptureDate>\n");
									buffer.append("<SourceSize>" + file.length() + "</SourceSize>\n");
									buffer.append("<Duration>0</Duration>\n");
									buffer.append("</Details>\n");
									buffer.append("<Links>\n");
									buffer.append("<Content>\n");
									buffer.append("<ContentType>video/mpeg</ContentType>\n");
									buffer.append("<Url>http://"
											+ serverConfiguration.getIPAddress()
											+ ":"
											+ Server.getServer().getHMOPort()
											+ "/TiVoConnect/"
											+ URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER)
													+ "/" + Tools.escapeXMLChars(filePath)) + "</Url>\n");
									buffer.append("</Content>\n");
									buffer.append("<CustomIcon>\n");
									buffer.append("<ContentType>video/*</ContentType>\n");
									buffer.append("<AcceptsParams>No</AcceptsParams>\n");
									buffer.append("<Url>urn:tivo:image:save-until-i-delete-recording</Url>\n");
									buffer.append("</CustomIcon>\n");
									buffer.append("</Links>\n");
									buffer.append("</Item>\n");
								} else {
									buffer.append("<Item>\n");
									buffer.append("<Details>\n");
									if (video.getSeriesTitle() != null)
										buffer.append("<Title>" + Tools.escapeXMLChars(video.getSeriesTitle())
												+ "</Title>\n");
									else
										buffer
												.append("<Title>" + Tools.escapeXMLChars(video.getTitle())
														+ "</Title>\n");
									buffer.append("<ContentType>video/x-tivo-mpeg</ContentType>\n");
									buffer.append("<SourceFormat>video/x-tivo-mpeg</SourceFormat>\n");
									if (video.getDuration() > 0)
										buffer.append("<Duration>" + video.getDuration() + "</Duration>\n");
									else
										buffer.append("<Duration>0</Duration>\n");
									if (video.getOriginalAirDate() != null)
										buffer.append("<CaptureDate>0x" + Tools.dateToHex(video.getOriginalAirDate())
												+ "</CaptureDate>\n");
									else
										buffer.append("<CaptureDate>0x" + Tools.dateToHex(video.getDateRecorded())
												+ "</CaptureDate>\n");
									if (video.getDescription() != null && video.getDescription().trim().length()>0)
										buffer.append("<Description>" + Tools.escapeXMLChars(video.getDescription())
												+ "</Description>\n");
									//else
									//	buffer.append("<Description></Description>\n");
									if (video.getEpisodeTitle()!=null && video.getEpisodeTitle().trim().length()>0)
										buffer.append("<EpisodeTitle>" + Tools.escapeXMLChars(video.getEpisodeTitle())
											+ "</EpisodeTitle>\n");
									String channel = String.valueOf(video.getChannelMajorNumber());
									if (channel.equals("0")) {
										channel = video.getChannel();
										if (channel == null)
											channel = "0";
									}
									buffer.append("<SourceChannel>" + channel + "</SourceChannel>\n");
									if (video.getStation() != null)
										buffer.append("<SourceStation>" + video.getStation() + "</SourceStation>\n");
									else
										buffer.append("<SourceStation>0</SourceStation>\n");
									buffer.append("<SourceSize>" + file.length() + "</SourceSize>\n");
									buffer.append("</Details>\n");
									buffer.append("<Links>\n");
									buffer.append("<Content>\n");
									buffer.append("<ContentType>video/x-tivo-mpeg</ContentType>\n");
									buffer.append("<Url>http://"
											+ serverConfiguration.getIPAddress()
											+ ":"
											+ Server.getServer().getHMOPort()
											+ "/TiVoConnect/"
											+ URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER)
													+ "/" + Tools.escapeXMLChars(filePath)) + "</Url>\n");
									buffer.append("</Content>\n");
									buffer.append("<CustomIcon>\n");
									buffer.append("<ContentType>video/*</ContentType>\n");
									buffer.append("<AcceptsParams>No</AcceptsParams>\n");
									buffer.append("<Url>urn:tivo:image:save-until-i-delete-recording</Url>\n");
									buffer.append("</CustomIcon>\n");
									buffer.append("<TiVoVideoDetails>\n");
									buffer.append("<ContentType>text/xml</ContentType>\n");
									buffer.append("<AcceptsParams>No</AcceptsParams>\n");
									buffer.append("<Url>/TiVoConnect/TivoNowPlaying/"
											+ URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER)
													+ "/" + Tools.escapeXMLChars(filePath))
											+ "?Format=text%2Fxml</Url>\n");
									buffer.append("</TiVoVideoDetails>\n");
									buffer.append("</Links>\n");
									buffer.append("</Item>\n");
								}
							}
						}
						buffer.append("</TiVoContainer>\n");
						buffer.append("<!-- Copyright (c) 2005, 2006 Leon Nicholls. All rights reserved.-->\n");
						return buffer.toString();
					}
				} catch (Exception ex) {
					Tools.logException(VideoServer.class, ex);
				}
			}
		}
		return "";
	}

	private String getVideoDetails(File file) {
		Video video = getVideo(file);
		if (video != null) {
			try {
				StringBuffer buffer = new StringBuffer();
				synchronized (buffer) {
					buffer
							.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><TvBusMarshalledStruct:TvBusEnvelope xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:TvBusMarshalledStruct=\"http://tivo.com/developer/xml/idl/TvBusMarshalledStruct\" xmlns:TvPgdRecording=\"http://tivo.com/developer/xml/idl/TvPgdRecording\" xmlns:TvBusDuration=\"http://tivo.com/developer/xml/idl/TvBusDuration\" xmlns:TvPgdShowing=\"http://tivo.com/developer/xml/idl/TvPgdShowing\" xmlns:TvDbShowingBit=\"http://tivo.com/developer/xml/idl/TvDbShowingBit\" xmlns:TvBusDateTime=\"http://tivo.com/developer/xml/idl/TvBusDateTime\" xmlns:TvPgdProgram=\"http://tivo.com/developer/xml/idl/TvPgdProgram\" xmlns:TvDbColorCode=\"http://tivo.com/developer/xml/idl/TvDbColorCode\" xmlns:TvPgdSeries=\"http://tivo.com/developer/xml/idl/TvPgdSeries\" xmlns:TvDbShowType=\"http://tivo.com/developer/xml/idl/TvDbShowType\" xmlns:TvPgdChannel=\"http://tivo.com/developer/xml/idl/TvPgdChannel\" xmlns:TvDbTvRating=\"http://tivo.com/developer/xml/idl/TvDbTvRating\" xmlns:TvDbRecordQuality=\"http://tivo.com/developer/xml/idl/TvDbRecordQuality\" xmlns:TvDbBitstreamFormat=\"http://tivo.com/developer/xml/idl/TvDbBitstreamFormat\" xs:schemaLocation=\"http://tivo.com/developer/xml/idl/TvBusMarshalledStruct TvBusMarshalledStruct.xsd http://tivo.com/developer/xml/idl/TvPgdRecording TvPgdRecording.xsd http://tivo.com/developer/xml/idl/TvBusDuration TvBusDuration.xsd http://tivo.com/developer/xml/idl/TvPgdShowing TvPgdShowing.xsd http://tivo.com/developer/xml/idl/TvDbShowingBit TvDbShowingBit.xsd http://tivo.com/developer/xml/idl/TvBusDateTime TvBusDateTime.xsd http://tivo.com/developer/xml/idl/TvPgdProgram TvPgdProgram.xsd http://tivo.com/developer/xml/idl/TvDbColorCode TvDbColorCode.xsd http://tivo.com/developer/xml/idl/TvPgdSeries TvPgdSeries.xsd http://tivo.com/developer/xml/idl/TvDbShowType TvDbShowType.xsd http://tivo.com/developer/xml/idl/TvPgdChannel TvPgdChannel.xsd http://tivo.com/developer/xml/idl/TvDbTvRating TvDbTvRating.xsd http://tivo.com/developer/xml/idl/TvDbRecordQuality TvDbRecordQuality.xsd http://tivo.com/developer/xml/idl/TvDbBitstreamFormat TvDbBitstreamFormat.xsd\" xs:type=\"TvPgdRecording:TvPgdRecording\">\n");
					if (video.getDuration() > 0)
						buffer.append("<recordedDuration>" + mDurationFormat.format(new Date(video.getDuration()))
								+ "</recordedDuration>\n"); // PT59M59S
					else
						buffer.append("<recordedDuration>PT00M</recordedDuration>\n"); // PT59M59S
					buffer.append("<vActualShowing>\n");
					buffer.append("<element>\n");
					buffer.append("<showingBits value=\"1027\"/>\n");
					buffer.append("<time>" + mTimeDateFormat.format(video.getDateRecorded()) + "</time>\n");
					if (video.getDuration() > 0)
						buffer.append("<duration>" + mDurationFormat.format(new Date(video.getDuration()))
								+ "</duration>\n"); // PT1H, PT30M
					else
						buffer.append("<duration>PT00M</duration>\n"); // PT1H,
					// PT30M
					if (video.getPartCount() != null)
						buffer.append("<partCount>"+video.getPartCount()+"</partCount>\n");
					if (video.getPartIndex() != null)
						buffer.append("<partIndex>"+video.getPartIndex()+"</partIndex>\n");
					buffer.append("<program>\n");
					buffer.append("<vActor>\n");
					if (video.getActors() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getActors(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + tokenizer.nextToken() + "</element>\n");
						}
					}
					buffer.append("</vActor>\n");
					buffer.append("<vAdvisory/>\n");
					buffer.append("<vChoreographer/>\n");
					if (video.getColorCode()!=0)
						buffer.append("<colorCode value=\"" + video.getColorCode() + "\">COLOR</colorCode>\n");
					else
						buffer.append("<colorCode value=\"4\">COLOR</colorCode>\n");
					if (video.getDescription() != null && video.getDescription().trim().length()>0)
						buffer.append("<description>" + Tools.escapeXMLChars(video.getDescription()) + "</description>\n");
					buffer.append("<vDirector>\n");
					if (video.getDirectors() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getDirectors(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vDirector>\n");
					buffer.append("<episodeNumber>" + video.getEpisodeNumber() + "</episodeNumber>\n");
					if (video.getEpisodeTitle()!=null && video.getEpisodeTitle().trim().length()>0)
						buffer.append("<episodeTitle>" + Tools.escapeXMLChars(video.getEpisodeTitle())
							+ "</episodeTitle>\n");
					buffer.append("<vExecProducer>\n");
					if (video.getExecProducers() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getExecProducers(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vExecProducer>\n");
					buffer.append("<vProgramGenre>\n");
					if (video.getProgramGenre() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getProgramGenre(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vProgramGenre>\n");
					buffer.append("<vGuestStar/>\n");
					buffer.append("<vHost/>\n");
					if (video.getEpisodic() != null)
						buffer.append("<isEpisode>" + video.getEpisodic() + "</isEpisode>\n");
					else
						buffer.append("<isEpisode>false</isEpisode>\n");
					if (video.getOriginalAirDate() != null)
						buffer.append("<originalAirDate>" + mTimeDateFormat.format(video.getOriginalAirDate())
								+ "</originalAirDate>\n");
					else
						buffer.append("<originalAirDate>" + mTimeDateFormat.format(new Date(file.lastModified()))
								+ "</originalAirDate>\n");
					buffer.append("<vProducer>\n");
					if (video.getProducers() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getProducers(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vProducer>\n");
					buffer.append("<series>\n");
					if (video.getEpisodic() != null)
						buffer.append("<isEpisodic>" + video.getEpisodic() + "</isEpisodic>\n");
					else
						buffer.append("<isEpisodic>false</isEpisodic>\n");
					buffer.append("<vSeriesGenre>\n");
					if (video.getSeriesGenre() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getSeriesGenre(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vSeriesGenre>\n");
					buffer.append("<seriesTitle>" + Tools.escapeXMLChars(video.getSeriesTitle()) + "</seriesTitle>\n");
					buffer.append("</series>\n");
					String showTypeValue = String.valueOf(video.getShowTypeValue());
					if (video.getShowTypeValue() == null)
						showTypeValue = "5";
					String showType = video.getShowType();
					if (video.getShowType() == null)
						showType = "SERIES";
					buffer.append("<showType value=\"" + showTypeValue + "\">" + showType + "</showType>\n");
					buffer.append("<title>" + Tools.escapeXMLChars(video.getTitle()) + "</title>\n");
					buffer.append("<vWriter>\n");
					if (video.getWriters() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getWriters(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vWriter>\n");
					buffer.append("</program>\n");
					buffer.append("<channel>\n");
					buffer.append("<displayMajorNumber>" + video.getChannelMajorNumber() + "</displayMajorNumber>\n");
					buffer.append("<displayMinorNumber>" + video.getChannelMinorNumber() + "</displayMinorNumber>\n");
					buffer.append("<callsign>" + Tools.escapeXMLChars(video.getCallsign()) + "</callsign>\n");
					buffer.append("</channel>\n");
					String ratingValue = String.valueOf(video.getRatingValue());
					if (video.getRatingValue() == null)
						ratingValue = "4";
					if (video.getRating() != null)
						buffer.append("<tvRating value=\"" + ratingValue + "\">" + video.getRating() + "</tvRating>\n");
					else
						buffer.append("<tvRating value=\"" + ratingValue + "\">PG</tvRating>\n");
					buffer.append("</element>\n");
					buffer.append("</vActualShowing>\n");
					buffer.append("<vBookmark/>\n");
					String recordingQualityValue = String.valueOf(video.getRecordingQualityValue());
					if (video.getRecordingQualityValue() == null)
						recordingQualityValue = "75";
					String recordingQuality = video.getRecordingQuality();
					if (video.getRecordingQuality() == null)
						recordingQuality = "HIGH";
					buffer.append("<recordingQuality value=\"" + recordingQualityValue + "\">" + recordingQuality
							+ "</recordingQuality>\n");
					buffer.append("<showing>\n");
					buffer.append("<showingBits value=\"1027\"/>\n");
					if (video.getDateRecorded() != null)
						buffer.append("<time>" + mTimeDateFormat.format(video.getDateRecorded()) + "</time>\n");
					else
						buffer.append("<time>" + mTimeDateFormat.format(new Date(file.lastModified())) + "</time>\n");
					if (video.getDuration() > 0)
						buffer.append("<duration>" + mDurationFormat.format(new Date(video.getDuration()))
								+ "</duration>\n"); // PT1H, PT30M
					else
						buffer.append("<duration>PT00M</duration>\n"); // PT1H,
					// PT30M
					if (video.getPartCount() != null)
						buffer.append("<partCount>"+video.getPartCount()+"</partCount>\n");
					if (video.getPartIndex() != null)
						buffer.append("<partIndex>"+video.getPartIndex()+"</partIndex>\n");
					buffer.append("<program>\n");
					buffer.append("<vActor>\n");
					if (video.getActors() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getActors(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vActor>\n");
					buffer.append("<vAdvisory/>\n");
					buffer.append("<vChoreographer/>\n");
					if (video.getColorCode()!=0)
						buffer.append("<colorCode value=\"" + video.getColorCode() + "\">COLOR</colorCode>\n");
					else
						buffer.append("<colorCode value=\"4\">COLOR</colorCode>\n");
					buffer.append("<description>" + Tools.escapeXMLChars(video.getDescription()) + "</description>\n");
					buffer.append("<vDirector>\n");
					if (video.getDirectors() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getDirectors(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vDirector>\n");
					buffer.append("<episodeNumber>" + video.getEpisodeNumber() + "</episodeNumber>\n");
					if (video.getEpisodeTitle()!=null && video.getEpisodeTitle().trim().length()>0)
						buffer.append("<episodeTitle>" + Tools.escapeXMLChars(video.getEpisodeTitle())
							+ "</episodeTitle>\n");
					buffer.append("<vExecProducer>\n");
					if (video.getExecProducers() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getExecProducers(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vExecProducer>\n");
					buffer.append("<vProgramGenre>\n");
					if (video.getProgramGenre() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getProgramGenre(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vProgramGenre>\n");
					buffer.append("<vGuestStar>\n");
					if (video.getGuestStars() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getGuestStars(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vGuestStar>\n");
					buffer.append("<vHost>\n");
					if (video.getHosts() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getHosts(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vHost>\n");
					if (video.getEpisodic() != null)
						buffer.append("<isEpisode>" + video.getEpisodic() + "</isEpisode>\n");
					else
						buffer.append("<isEpisode>false</isEpisode>\n");
					if (video.getOriginalAirDate() != null)
						buffer.append("<originalAirDate>" + mTimeDateFormat.format(video.getOriginalAirDate())
								+ "</originalAirDate>\n");
					else
						buffer.append("<originalAirDate>" + mTimeDateFormat.format(new Date(file.lastModified()))
								+ "</originalAirDate>\n");
					buffer.append("<vProducer/>\n");
					buffer.append("<series>\n");
					if (video.getEpisodic() != null)
						buffer.append("<isEpisodic>" + video.getEpisodic() + "</isEpisodic>\n");
					else
						buffer.append("<isEpisodic>false</isEpisodic>\n");
					buffer.append("<vSeriesGenre>\n");
					if (video.getSeriesGenre() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getSeriesGenre(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vSeriesGenre>\n");
					buffer.append("<seriesTitle>" + Tools.escapeXMLChars(video.getSeriesTitle()) + "</seriesTitle>\n");
					buffer.append("</series>\n");
					buffer.append("<showType value=\"" + showTypeValue + "\">" + showType + "</showType>\n");
					buffer.append("<title>" + video.getTitle() + "</title>\n");
					buffer.append("<vWriter>\n");
					if (video.getWriters() != null) {
						StringTokenizer tokenizer = new StringTokenizer(video.getWriters(), ";");
						while (tokenizer.hasMoreTokens()) {
							buffer.append("<element>" + Tools.escapeXMLChars(tokenizer.nextToken()) + "</element>\n");
						}
					}
					buffer.append("</vWriter>\n");
					buffer.append("</program>\n");
					buffer.append("<channel>\n");
					buffer.append("<displayMajorNumber>" + video.getChannelMajorNumber() + "</displayMajorNumber>\n");
					buffer.append("<displayMinorNumber>" + video.getChannelMinorNumber() + "</displayMinorNumber>\n");
					buffer.append("<callsign>" + Tools.escapeXMLChars(video.getCallsign()) + "</callsign>\n");
					buffer.append("</channel>\n");
					if (video.getRating() != null)
						buffer.append("<tvRating value=\"" + ratingValue + "\">" + video.getRating() + "</tvRating>\n");
					else
						buffer.append("<tvRating value=\"" + ratingValue + "\">PG</tvRating>\n");
					buffer.append("</showing>\n");
					if (video.getStartTime() != null)
						buffer.append("<startTime>" + mTimeDateFormat.format(video.getStartTime()) + "</startTime>\n");
					else
						buffer.append("<startTime>" + mTimeDateFormat.format(new Date(file.lastModified()))
								+ "</startTime>\n");
					if (video.getStopTime() != null)
						buffer.append("<stopTime>" + mTimeDateFormat.format(video.getStopTime()) + "</stopTime>\n");
					else
						buffer.append("<stopTime>" + mTimeDateFormat.format(new Date(file.lastModified()))
								+ "</stopTime>\n");
					if (video.getExpirationTime() != null)
						buffer.append("<expirationTime>" + mTimeDateFormat.format(video.getExpirationTime())
								+ "</expirationTime>\n");
					buffer.append("</TvBusMarshalledStruct:TvBusEnvelope>\n");
				}
				log.debug(buffer.toString());
				return buffer.toString();
			} catch (Exception ex) {
				Tools.logException(VideoServer.class, ex);
			}
		}
		return null;
	}

	private Video getVideo(File file) {
		Video video = null;
		try {
			List list = VideoManager.findByPath(file.getCanonicalPath());
			if (list != null && list.size() > 0) {
				return (Video) list.get(0);
			} else {
				String path = file.getAbsolutePath();
				path = path.substring(0, 1).toLowerCase() + path.substring(1);
				list = VideoManager.findByPath(path);
				if (list != null && list.size() > 0) {
					return (Video) list.get(0);
				}
			}
		} catch (Exception ex) {
			log.error("Video retrieve failed", ex);
		}
		return (Video)MediaManager.getMedia(file.getAbsolutePath());
	}

	public void publish(NameValue nameValue) {
		try
		{
			File file = new File(nameValue.getValue());
			mPublished.add(new NameValue(nameValue.getName(), file.getCanonicalPath()));
		}
		catch (Exception ex)
		{
			mPublished.add(nameValue);
		}
	}

	private String getFilePath(File file, File directory) {
		try {
			if (file != null && directory != null) {
				if (directory.getCanonicalPath().length() > 0) {
					String sub = file.getCanonicalPath().substring(directory.getCanonicalPath().length() + 1);
					if (sub.length() > 0) {
						return sub.replaceAll("\\\\", "/");
					}
				}
			}
		} catch (Exception ex) {
		}
		return "";
	}

	private String mHost = "Galleon";

	private SimpleDateFormat mFileDateFormat;

	private SimpleDateFormat mTimeDateFormat;

	private SimpleDateFormat mDurationFormat;

	private GregorianCalendar mCalendar;

	private List mPublished = new ArrayList();
}