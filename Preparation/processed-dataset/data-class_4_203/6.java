public static Podcast getPodcast(Podcast podcast) {
    PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Podcasting.class.getName() + "." + podcast.getPath());
    String content = persistentValue == null ? null : persistentValue.getValue();
    if (PersistentValueManager.isAged(persistentValue)) {
        try {
            String page = Tools.getPage(new URL(podcast.getPath()));
            if (page != null && page.length() > 0)
                content = page;
        } catch (Exception ex) {
            Tools.logException(Podcasting.class, ex, "Could not cache listing: " + podcast.getPath());
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
            Element root = document.getRootElement();
            // check for errors 
            if (root != null && root.getName().equals("rss")) {
                Element channel = root.element("channel");
                if (channel != null) {
                    String channelTitle = null;
                    String channelLink = null;
                    String channelDescription = null;
                    String channelSubtitle = null;
                    String channelImage1 = null;
                    String channelImage2 = null;
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
                    for (Iterator i = channel.elementIterator("image"); i.hasNext(); ) {
                        Element item = (Element) i.next();
                        if (item.element("url") != null) {
                            if ((value = Tools.getAttribute(item, "url")) != null) {
                                channelImage1 = value;
                            }
                        } else if (item.element("image") != null) {
                            item = item.element("image");
                            if ((value = Tools.getAttribute(item, "href")) != null) {
                                channelImage1 = value;
                            }
                        } else if (item.attribute("href") != null) {
                            channelImage2 = item.attributeValue("href");
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
                        podcast.setAuthor(Tools.cleanHTML(channelAuthor));
                    if (channelTitle != null && channelTitle.length() > 0)
                        podcast.setTitle(Tools.cleanHTML(channelTitle));
                    if (channelLink != null && channelLink.length() > 0)
                        podcast.setLink(channelLink);
                    if (channelDescription != null && channelDescription.length() > 0)
                        podcast.setDescription(Tools.trim(Tools.cleanHTML(channelDescription), 4096));
                    if (channelSubtitle != null && channelSubtitle.length() > 0)
                        podcast.setSubtitle(Tools.trim(Tools.cleanHTML(channelSubtitle), 4096));
                    if (channelImage2 == null) {
                        if (channelImage1 != null && (channelImage1.endsWith(".png") || channelImage1.endsWith(".jpg") || channelImage1.endsWith(".gif")))
                            podcast.setImage(channelImage1);
                    } else {
                        if (channelImage2.endsWith(".png") || channelImage2.endsWith(".jpg"))
                            podcast.setImage(channelImage2);
                    }
                    if (channelBlock != null)
                        podcast.setBlock(new Boolean(!channelBlock.toLowerCase().equals("no")));
                    else
                        podcast.setBlock(Boolean.FALSE);
                    if (channelCategory != null && channelCategory.length() > 0)
                        podcast.setCategory(Tools.cleanHTML(channelCategory));
                    if (channelExplicit != null)
                        podcast.setExplicit(new Boolean(!channelExplicit.toLowerCase().equals("no")));
                    else
                        podcast.setExplicit(Boolean.FALSE);
                    if (channelKeywords != null && channelKeywords.length() > 0)
                        podcast.setKeywords(Tools.cleanHTML(channelKeywords));
                    if (channelSummary != null && channelSummary.length() > 0)
                        podcast.setSummary(Tools.trim(Tools.cleanHTML(channelSummary), 4096));
                    if (channelTtl != null && channelTtl.length() > 0) {
                        try {
                            podcast.setTtl(new Integer(channelTtl));
                        } catch (Exception ex) {
                        }
                    } else
                        podcast.setTtl(new Integer(0));
                    List tracks = podcast.getTracks();
                    if (tracks == null)
                        tracks = new ArrayList();
                    for (Iterator i = channel.elementIterator("item"); i.hasNext(); ) {
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
                        for (Iterator j = item.elementIterator("author"); j.hasNext(); ) {
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
                        if (enclosureUrl != null && enclosureUrl.length() > 0 && enclosureUrl.endsWith(".mp3")) {
                            PodcastTrack podcastTrack = null;
                            boolean existing = false;
                            for (int k = 0; k < tracks.size(); k++) {
                                PodcastTrack current = (PodcastTrack) tracks.get(k);
                                if (current != null && current.getUrl() != null && enclosureUrl != null && current.getUrl().equals(enclosureUrl)) {
                                    existing = true;
                                    podcastTrack = current;
                                    break;
                                }
                            }
                            if (podcastTrack == null)
                                podcastTrack = new PodcastTrack();
                            if (title != null && title.length() > 0)
                                podcastTrack.setTitle(Tools.cleanHTML(title));
                            if (description != null && description.length() > 0)
                                podcastTrack.setDescription(Tools.trim(Tools.cleanHTML(description), 4096));
                            if (link != null && link.length() > 0)
                                podcastTrack.setLink(link);
                            if (pubDate != null && pubDate.length() > 0) {
                                try {
                                    podcastTrack.setPublicationDate(new Date(pubDate));
                                } catch (Exception ex) {
                                }
                            }
                            if (guid != null && guid.length() > 0)
                                podcastTrack.setGuid(guid);
                            if (category != null && category.length() > 0)
                                podcastTrack.setCategory(Tools.cleanHTML(category));
                            if (explicit != null && explicit.length() > 0)
                                podcastTrack.setExplicit(new Boolean(!explicit.toLowerCase().equals("no")));
                            else
                                podcastTrack.setExplicit(Boolean.FALSE);
                            if (author != null && author.length() > 0)
                                podcastTrack.setAuthor(Tools.cleanHTML(author));
                            if (summary != null && summary.length() > 0)
                                podcastTrack.setSummary(Tools.trim(Tools.cleanHTML(summary), 4096));
                            if (enclosureUrl != null && enclosureUrl.length() > 0)
                                podcastTrack.setUrl(enclosureUrl);
                            if (enclosureLength != null && enclosureLength.length() > 0) {
                                try {
                                    podcastTrack.setSize(Long.parseLong(enclosureLength));
                                } catch (Exception ex) {
                                }
                            }
                            if (enclosureType != null && enclosureType.length() > 0)
                                podcastTrack.setMimeType(enclosureType);
                            if (block != null && block.length() > 0)
                                podcastTrack.setBlock(new Boolean(!block.toLowerCase().equals("no")));
                            else
                                podcastTrack.setBlock(Boolean.FALSE);
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
                                    podcastTrack.setDuration(new Long(date.getTime()));
                                } catch (Exception ex) {
                                }
                            }
                            if (keywords != null && keywords.length() > 0)
                                podcastTrack.setKeywords(Tools.cleanHTML(keywords));
                            if (subtitle != null && subtitle.length() > 0)
                                podcastTrack.setSubtitle(Tools.trim(Tools.cleanHTML(subtitle), 4096));
                            if (podcastTrack.getMimeType() == null)
                                podcastTrack.setMimeType(Mp3File.DEFAULT_MIME_TYPE);
                            podcastTrack.setPodcast(podcast.getId());
                            if (!existing)
                                tracks.add(podcastTrack);
                        }
                    }
                    podcast.setTracks(tracks);
                }
            } else {
                ChannelBuilderIF builder = new ChannelBuilder();
                ChannelIF channel = FeedParser.parse(builder, new ByteArrayInputStream((content.getBytes("UTF-8"))));
                if (channel != null) {
                    podcast.setDescription(Tools.cleanHTML(channel.getDescription()));
                    podcast.setDateUpdated(channel.getLastBuildDate());
                    podcast.setTtl(new Integer(channel.getTtl()));
                    List items = getListing(channel);
                    if (items != null && items.size() > 0) {
                        ArrayList tracks = new ArrayList();
                        for (Iterator i = items.iterator(); i.hasNext(); ) {
                            ItemIF item = (ItemIF) i.next();
                            String description = Tools.trim(item.getDescription(), 4096);
                            tracks.add(new PodcastTrack(Tools.cleanHTML(item.getTitle()), null, null, Tools.trim(Tools.cleanHTML(item.getDescription()), 4096), null, null, null, null, Boolean.FALSE, Boolean.FALSE, null, item.getDate(), item.getEnclosure().getLocation().toExternalForm(), "audio/mpeg", item.getEnclosure().getLength(), 0, new Long(0), new Integer(0), 0, 0, podcast.getId(), new Integer(0), null));
                        }
                        podcast.setTracks(tracks);
                    }
                }
                builder.close();
                builder = null;
            }
            document.clearContent();
            document = null;
            if (PersistentValueManager.isAged(persistentValue)) {
                int ttl = podcast.getTtl().intValue();
                if (ttl < 10)
                    ttl = 60;
                else
                    ttl = 60 * 6;
                PersistentValueManager.savePersistentValue(Podcasting.class.getName() + "." + podcast.getPath(), content, ttl * 60);
            }
        } catch (Exception ex) {
            Tools.logException(Podcasting.class, ex, "Could not download listing: " + podcast.getPath());
            return null;
        }
    }
    return podcast;
}
