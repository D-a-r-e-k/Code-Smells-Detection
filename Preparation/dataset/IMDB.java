package org.lnicholls.galleon.util;

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

import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.lnicholls.galleon.database.Movie;

public class IMDB {

	private static final Logger log = Logger.getLogger(IMDB.class.getName());

	public static String getIMDBID(String key) {
		String imdb = null;
		if (key != null) {
			try {
				Parser parser = new Parser("http://us.imdb.com/Tsearch?title=" + URLEncoder.encode(key));

				NodeFilter filter = null;
				NodeList list = list = new NodeList();

				filter = new TagNameFilter("B");
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Tag tag = (Tag) list.elementAt(i);
						CompositeTag parent = (CompositeTag) tag.getParent();
						int position = parent.findPositionOf(tag);
						Node value = parent.childAt(position + 1);
						if (value != null) {
							if (cleanSpaces(value.getText()).startsWith("populartitles")) {  //Popular Titles
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value.getText().equals("ol")) {
										filter = new NodeClassFilter(LinkTag.class);
										NodeList linkList = new NodeList();
										value.collectInto(linkList, filter);
										for (int j = 0; j < linkList.size(); j++) {
											LinkTag linkTag = (LinkTag) linkList.elementAt(j);
											String REGEX = "/.*/tt(.*)/";
											Pattern p = Pattern.compile(REGEX);
											Matcher m = p.matcher(linkTag.getLink());
											if (m.find()) {
												return m.group(1);
											}
										}
										break;
									}
								}
							} else if (cleanSpaces(value.getText()).startsWith("titles(exactmatches)")) {  //Titles (Exact Matches)
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value.getText().equals("ol")) {
										filter = new NodeClassFilter(LinkTag.class);
										NodeList linkList = new NodeList();
										value.collectInto(linkList, filter);
										for (int j = 0; j < linkList.size(); j++) {
											LinkTag linkTag = (LinkTag) linkList.elementAt(j);
											String REGEX = "/.*/tt(.*)/";
											Pattern p = Pattern.compile(REGEX);
											Matcher m = p.matcher(linkTag.getLink());
											if (m.find()) {
												return m.group(1);
											}
										}
										break;
									}
								}
							} else if (cleanSpaces(value.getText()).startsWith("titles(partialmatches)")) {  //Titles (Partial Matches)
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value.getText().equals("ol")) {
										filter = new NodeClassFilter(LinkTag.class);
										NodeList linkList = new NodeList();
										value.collectInto(linkList, filter);
										for (int j = 0; j < linkList.size(); j++) {
											LinkTag linkTag = (LinkTag) linkList.elementAt(j);
											String REGEX = "/.*/tt(.*)/";
											Pattern p = Pattern.compile(REGEX);
											Matcher m = p.matcher(linkTag.getLink());
											if (m.find()) {
												return m.group(1);
											}
										}
										break;
									}
								}
							}
						}
					}
				}
				if (imdb == null) {
					parser.reset();
					filter = new NodeClassFilter(LinkTag.class);
					list = parser.extractAllNodesThatMatch(filter);
					for (int i = 0; i < list.size(); i++) {
						LinkTag linkTag = (LinkTag) list.elementAt(i);
						String REGEX = ".*/title/tt(.*)/";
						Pattern p = Pattern.compile(REGEX);
						Matcher m = p.matcher(linkTag.getLink());
						if (m.find()) {
							return m.group(1);
						}
					}
				}
			} catch (Exception ex) {
				log.debug("Could not get IMDB ID1: " + key);
				return getIMDBID2(key);
			}
		}
		return imdb;
	}

	public static void getMovie(Movie movie) {
		String poster = null;
		String title = null;
		String director = null;
		String genre = null;
		String plotOutline = null;
		String rating = null;
		String credits = null;
		String cast = null;
		String rated = null;
		String ratedReason = null;
		String top250 = null;

		String imdb = movie.getIMDB();
		if (imdb==null || imdb.length()==0)
		{
			imdb = getIMDBID(movie.getTitle());
		}
		if (imdb != null) {
			movie.setIMDB(imdb);
			try {
				Parser parser = new Parser("http://imdb.com/title/tt" + imdb + "/");

				NodeFilter filter = null;
				NodeList list = list = new NodeList();

				//<a name="poster" href="photogallery" title="ATL"><img border="0" alt="ATL" title="ATL" src="http://ia.imdb.com/media/imdb/01/I/88/39/00/10m.jpg" height="140" width="95"></a>
				list = new NodeList();
				filter = new AndFilter(new TagNameFilter("IMG"),
						new HasParentFilter(new AndFilter(new TagNameFilter("A"), new HasAttributeFilter("name",
								"poster"))));
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					ImageTag tag = (ImageTag) list.elementAt(0);
					poster = tag.getImageURL();
				}

				parser.reset();

				if (poster == null) {
					filter = new AndFilter(new TagNameFilter("IMG"), new HasAttributeFilter("alt", "cover"));
					list = parser.extractAllNodesThatMatch(filter);
					if (list != null && list.size() > 0) {
						ImageTag tag = (ImageTag) list.elementAt(0);
						poster = tag.getImageURL();
					}
				}

				// <title>The Godfather (1972)</title>
				parser.reset();
				Node[] nodes = parser.extractAllNodesThatAre(TitleTag.class);
				if (nodes != null && nodes.length > 0) {
					TitleTag tag = (TitleTag) nodes[0];
					title = tag.getTitle();
				}

				filter = new TagNameFilter("TITLE");
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					Tag tag = (Tag) list.elementAt(0);
					CompositeTag parent = (CompositeTag) tag.getParent();
					int position = parent.findPositionOf(tag);
					Node name = parent.childAt(position + 1);
					title = name.getText();
				}

				// <h1><strong class="title">The Godfather <small>(<a
				// href="/Sections/Years/1972">1972</a>)</small></strong></h1>
				parser.reset();
				filter = new AndFilter(new AndFilter(new TagNameFilter("STRONG"), new HasAttributeFilter("class",
						"title")), new HasParentFilter(new TagNameFilter("TD")));
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					Tag tag = (Tag) list.elementAt(0);
					CompositeTag parent = (CompositeTag) tag.getParent();
					int position = parent.findPositionOf(tag);
					Node name = parent.childAt(position + 1);
					title = name.getText();
				}

				parser.reset();
				filter = new AndFilter(new TagNameFilter("STRONG"), new HasAttributeFilter("class", "title"));
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					Tag tag = (Tag) list.elementAt(0);
					CompositeTag parent = (CompositeTag) tag.getParent();
					int position = parent.findPositionOf(tag);
					Node name = parent.childAt(position + 1);
					title = name.getText();
				}

				// <b class="blackcatheader">Directed by</b><br><a
				// href="/name/nm0000338/">Francis Ford Coppola</a><br><br>
				parser.reset();
				filter = new AndFilter(new TagNameFilter("B"), new HasAttributeFilter("class", "blackcatheader"));
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Tag tag = (Tag) list.elementAt(i);
						CompositeTag parent = (CompositeTag) tag.getParent();
						int position = parent.findPositionOf(tag);
						Node value = parent.childAt(++position);
						if (value != null) {
							if (cleanSpaces(value.getText()).startsWith("directedby")) {  //Directed by
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value != null && value instanceof LinkTag) {
										LinkTag link = (LinkTag) value;
										director = link.getLinkText();
										break;
									}
								}
							} else if (cleanSpaces(value.getText()).startsWith("writingcredits")) {  //Writing credits
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value != null && value instanceof LinkTag) {
										LinkTag link = (LinkTag) value;
										if (link.getLinkText().trim().equals("(more)"))
											break;
										else {
											if (link.getLink().indexOf("/name")!=-1)
											{
												if (credits == null)
													credits = link.getLinkText();
												else
													credits = credits + ", " + link.getLinkText();
											}
										}
									}
								}
							} else if (cleanSpaces(value.getText()).startsWith("castoverview,firstbilledonly")) { //Cast overview, first billed only:
								parent = (CompositeTag) parent.getParent(); // tr
								parent = (CompositeTag) parent.getParent(); // table
								filter = new NodeClassFilter(LinkTag.class);
								NodeList linkList = new NodeList();
								parent.collectInto(linkList, filter);
								for (int j = 0; j < linkList.size(); j++) {
									LinkTag linkTag = (LinkTag) linkList.elementAt(j);
									if (cleanSpaces(linkTag.getLinkText()).startsWith("(more)"))
										break;
									else {
										if (cast == null)
											cast = linkTag.getLinkText();
										else
											cast = cast + ", " + linkTag.getLinkText();
									}
								}
							}
						}
					}
				}

				// <b class="ch">Genre:</b><a
				// href="/Sections/Genres/Crime/">Crime</a> / <a
				// href="/Sections/Genres/Drama/">Drama</a> <a
				// href="/rg/title-tease/keywords/title/tt0068646/keywords">(more)</a>
				parser.reset();
				filter = new AndFilter(new TagNameFilter("B"), new HasAttributeFilter("class", "ch"));
				list = parser.extractAllNodesThatMatch(filter);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Tag tag = (Tag) list.elementAt(i);
						CompositeTag parent = (CompositeTag) tag.getParent();
						int position = parent.findPositionOf(tag);
						Node value = parent.childAt(++position);
						if (value != null) {
							String text = "";
							if ((value instanceof LinkTag))
								text = ((LinkTag) value).getLinkText();
							else
								text = value.getText();
							if (cleanSpaces(text).startsWith("genre")) {  //Genre:
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value != null && value instanceof LinkTag) {
										LinkTag link = (LinkTag) value;
										if (cleanSpaces(link.getLinkText()).startsWith("(more)"))
											break;
										else {
											if (genre == null)
												genre = link.getLinkText();
											else
												genre = genre + ", " + link.getLinkText();
										}
									}
								}
							} else if (cleanSpaces(text).startsWith("plotoutline")) {  //Plot Outline:
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value != null && value.getText().equals("/b")) {
										value = parent.childAt(++position);
										plotOutline = value.getText();
										break;
									}
								}
							} else if (cleanSpaces(text).startsWith("userrating")) {  //User Rating:
								boolean foundFirst = false;
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value != null) {
										if (foundFirst && value.getText().equals("b")) {
											value = parent.childAt(++position);
											if (value.getText().indexOf('.') != -1)
												rating = value.getText().substring(0, value.getText().indexOf('.'));
											else
												rating = value.getText().substring(0, value.getText().indexOf('/'));
											break;
										} else if (value.getText().equals("/b")) {
											foundFirst = true;
										}
									}
								}
							} else if (cleanSpaces(text).startsWith("mpaa")) {  //MPAA
								boolean foundFirst = false;
								while (position < parent.getChildCount()) {
									value = parent.childAt(++position);
									if (value != null && value.getText().equals("/b")) {
										value = parent.childAt(++position);
										rated = value.getText();

										String REGEX = "Rated (.*) for (.*)"; // Rated
																				// PG-13
																				// for
																				// intense
										Pattern p = Pattern.compile(REGEX);
										Matcher m = p.matcher(value.getText());
										if (m.find()) {
											rated = m.group(1);
											ratedReason = m.group(2);
										}

										break;
									}
								}
							}
						}
					}
				}

				StringBean sb = new StringBean();
				sb.setLinks(false);
				sb.setReplaceNonBreakingSpaces(true);
				sb.setCollapse(false);
				parser.reset();
				parser.visitAllNodesWith(sb);

				int count = 0;
				boolean genreNext = false;
				boolean directedNext = false;
				boolean plotOutlineNext = false;
				boolean userRatingNext = false;
				boolean top250Next = false;
				StringTokenizer tokenizer = new StringTokenizer(sb.getStrings(), System.getProperty("line.separator"));
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if (token.trim().length() > 0) {
						String lower = cleanSpaces(token);

						if (count == 0) {
							if (title == null)
								title = token.trim(); // The Godfather (1972)
						} else if (genreNext) {
							if (genre == null)
								genre = token.trim();
							genreNext = false;
						} else if (directedNext) {
							if (director == null)
								director = token.trim();
							directedNext = false;
						} else if (userRatingNext) {
							if (rating == null)
								rating = token.trim();
							userRatingNext = false;
						} else if (plotOutlineNext) {
							if (plotOutline == null)
								plotOutline = token.trim();
							plotOutlineNext = false;
						} else if (top250Next) {
							if (top250 == null)
								top250 = token.trim();
							top250Next = false;
						} else if (lower.startsWith("genre") && lower.length() > 6) {
							if (genre == null)
								genre = token.substring(6).trim(); // Genre:
																	// Crime /
																	// Drama
																	// (more)
						} else if (lower.equals("genre:") || lower.equals("genre")) {
							genreNext = true;
						} else if (lower.startsWith("directedby") && lower.length() > 11) {
							if (director == null)
								director = token.substring(11).trim(); // Directed
																		// by
																		// Francis
																		// Ford
																		// Coppola
						} else if (lower.equals("directedby:") || lower.equals("directedby")) {
							directedNext = true;
						} else if (lower.startsWith("plotoutline") && lower.length() > 13) {
							if (plotOutline == null)
								plotOutline = token.substring(13).trim(); // Plot
																			// Outline:
																			// The
																			// epic
																			// saga
																			// of
																			// how
																			// a
																			// younger
																			// son
																			// rises
																			// to
																			// (more)
																			// (view
																			// trailer)
						} else if (lower.equals("plotoutline:") || lower.equals("plotoutline")) {
							plotOutlineNext = true;
						} else if (lower.startsWith("userrating") && lower.length() > 12) {
							if (rating == null)
								rating = token.substring(12).trim(); // User
																		// Rating:
																		// 9.1/10
																		// (129,095
																		// votes)
						} else if (lower.equals("userrating:") || lower.equals("userrating")) {
							userRatingNext = true;
						} else if (lower.startsWith("top250") && lower.length() > 8) {
							if (top250 == null)
								top250 = token.substring(8).trim(); // User
																	// Rating:
																	// 9.1/10
																	// (129,095
																	// votes)
						} else if (lower.equals("top250:") || lower.equals("top250")) {
							top250Next = true;
						}

						count++;
					}
				}

				//System.out.println("imdb="+imdb);
				//System.out.println("poster="+poster);
				//System.out.println("title="+title);
				//System.out.println("director="+director);
				//System.out.println("genre="+genre);
				//System.out.println("plotOutline="+plotOutline);
				//System.out.println("rating="+rating);
				//System.out.println("credits="+credits);
				//System.out.println("cast="+cast);

				if (empty(movie.getThumbUrl()) && poster != null)
					movie.setThumbUrl(poster);
				if (empty(movie.getDirector()) && director != null)
					movie.setDirector(clean(director));
				if (empty(movie.getGenre()) && genre != null)
					movie.setGenre(clean(genre));
				if (empty(movie.getPlotOutline()) && plotOutline != null)
					movie.setPlotOutline(clean(plotOutline));
				if (movie.getRating() == 0 && rating != null)
				{
					try
					{
						movie.setRating(Integer.parseInt(rating));
					}
					catch (Exception ex){}
				}
				if (empty(movie.getCredits()) && credits != null)
					movie.setCredits(clean(credits));
				if (empty(movie.getActors()) && cast != null)
					movie.setActors(clean(cast));
				if (empty(movie.getRated()) && rated != null)
					movie.setRated(clean(rated));
				if (empty(movie.getRatedReason()) && ratedReason != null)
					movie.setRatedReason(clean(ratedReason));
				movie.setOrigen("IMDB");
			} catch (Exception ex) {
				log.debug("Could not get IMDB data1: " + movie.getTitle());
				getMovie2(movie);
			}
		}
	}

	public static String getIMDBID2(String key) {
		String imdb = null;
		if (key != null) {
			StringBuffer buffer = new StringBuffer();
	        byte[] buf = new byte[1024];
	        int amount = 0;
			try {
				URL url = new URL("http://nicholls.us/imdb/imdbsearchxml.php?name="+URLEncoder.encode(key));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Galleon "+ Tools.getVersion());
                conn.setInstanceFollowRedirects(true);

                InputStream input = conn.getInputStream();
                while ((amount = input.read(buf)) > 0) {
                    buffer.append(new String(buf, 0, amount));
                }
                input.close();
                conn.disconnect();

                SAXReader saxReader = new SAXReader();
                StringReader stringReader = new StringReader(buffer.toString().trim());
                Document document = saxReader.read(stringReader);

                //Document document = saxReader.read(new File("d:/galleon/imdb.xml"));

                Element root = document.getRootElement();
                return Tools.getAttribute(root, "imdb");
			} catch (Exception ex) {
				Tools.logException(IMDB.class, ex, "Could not get IMDB ID: " + key);
			}
		}
		return imdb;
	}

	public static void getMovie2(Movie movie) {
		String imdb = movie.getIMDB();
		if (imdb==null || imdb.length()==0)
		{
			imdb = getIMDBID(movie.getTitle());
		}
		if (imdb != null) {
			movie.setIMDB(imdb);

			StringBuffer buffer = new StringBuffer();
	        byte[] buf = new byte[1024];
	        int amount = 0;
			try {
				URL url = new URL("http://nicholls.us/imdb/imdbxml.php?mid="+imdb);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Galleon "+ Tools.getVersion());
                conn.setInstanceFollowRedirects(true);

                InputStream input = conn.getInputStream();
                while ((amount = input.read(buf)) > 0) {
                    buffer.append(new String(buf, 0, amount));
                }
                input.close();
                conn.disconnect();

                SAXReader saxReader = new SAXReader();
                StringReader stringReader = new StringReader(buffer.toString().trim());
                Document document = saxReader.read(stringReader);

                //Document document = saxReader.read(new File("d:/galleon/imdb.xml"));

                Element root = document.getRootElement();
                movie.setTitle(clean(Tools.getAttribute(root, "title")));
                try
                {
                	movie.setDate(Integer.parseInt(clean(Tools.getAttribute(root, "year"))));
                }
                catch (Exception ex){}
                movie.setThumbUrl(clean(Tools.getAttribute(root, "photoUrl")));
                try
                {
                	movie.setDuration(Integer.parseInt(clean(Tools.getAttribute(root, "runtime"))));
                }
                catch (Exception ex){}
                movie.setRating((int)Float.parseFloat(clean(Tools.getAttribute(root, "rating"))));
                movie.setRated(clean(Tools.getAttribute(root, "rated")));
                movie.setGenre(clean(Tools.getAttribute(root, "genres")));
                movie.setTagline(clean(Tools.getAttribute(root, "tagline")));
                movie.setDirector(clean(Tools.getAttribute(root, "director")));
                movie.setCredits(clean(Tools.getAttribute(root, "writer")));
                movie.setProducer(clean(Tools.getAttribute(root, "producer")));
                movie.setActors(clean(Tools.getAttribute(root, "cast")));
                movie.setPlotOutline(clean(Tools.getAttribute(root, "outline")));
                movie.setPlot(clean(Tools.getAttribute(root, "plot")));
			} catch (Exception ex) {
				Tools.logException(IMDB.class, ex, "Could not get IMDB data: " + movie.getTitle());
			}
		}
	}

	private static boolean empty(String value) {
		if (value == null)
			return true;
		else if (value.trim().length() == 0)
			return true;

		return false;
	}

	private static String clean(String value) {
		if (value == null)
			return "";
		else
			return StringEscapeUtils.unescapeHtml(value.replaceAll("&nbsp;", " ").trim());
	}

	public static String cleanSpaces(String value) {
		StringBuffer buffer = new StringBuffer();
		synchronized (buffer) {
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) != ' ')
					buffer.append(value.charAt(i));
			}
		}
		return buffer.toString().toLowerCase();
	}
}