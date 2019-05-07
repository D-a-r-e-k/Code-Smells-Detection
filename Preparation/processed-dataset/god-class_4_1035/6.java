public static Videocast getVideocast(Videocast videocast) {
    PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Videocasting.class.getName() + "." + videocast.getPath());
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
                    for (Iterator i = channel.elementIterator("image"); i.hasNext(); ) {
                        Element item = (Element) i.next();
                        if (item.element("url") != null) {
                            if ((value = Tools.getAttribute(item, "url")) != null) {
                                channelImage1 = value;
                            }
                        } else if (item.attribute("href") != null) {
                            channelImage2 = item.attributeValue("href");
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
                        if (channelImage1 != null && (channelImage1.endsWith(".png") || channelImage1.endsWith(".jpg") || channelImage1.endsWith(".gif")))
                            videocast.setImage(channelImage1);
                        else
                            videocast.setImage(channelImage3);
                    } else {
                        if (channelImage2.endsWith(".png") || channelImage2.endsWith(".jpg"))
                            videocast.setImage(channelImage2);
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
                        if (enclosureUrl != null && enclosureUrl.length() > 0) {
                            VideocastTrack videocastTrack = null;
                            boolean existing = false;
                            for (int k = 0; k < tracks.size(); k++) {
                                VideocastTrack current = (VideocastTrack) tracks.get(k);
                                if (current != null && current.getUrl() != null && enclosureUrl != null && current.getUrl().equals(enclosureUrl)) {
                                    existing = true;
                                    videocastTrack = current;
                                    break;
                                }
                            }
                            if (videocastTrack == null)
                                videocastTrack = new VideocastTrack();
                            if (title != null && title.length() > 0 && (videocastTrack.getTitle() == null || videocastTrack.getTitle().trim().length() == 0))
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
                ChannelIF channel = FeedParser.parse(builder, new ByteArrayInputStream((content.getBytes("UTF-8"))));
                if (channel != null) {
                    videocast.setDescription(Tools.cleanHTML(channel.getDescription()));
                    videocast.setDateUpdated(channel.getLastBuildDate());
                    videocast.setTtl(new Integer(channel.getTtl()));
                    List items = getListing(channel);
                    if (items != null && items.size() > 0) {
                        ArrayList tracks = new ArrayList();
                        for (Iterator i = items.iterator(); i.hasNext(); ) {
                            ItemIF item = (ItemIF) i.next();
                            String description = Tools.trim(item.getDescription(), 4096);
                            tracks.add(new VideocastTrack(Tools.cleanHTML(item.getTitle()), null, null, Tools.trim(Tools.cleanHTML(item.getDescription()), 4096), null, null, null, null, Boolean.FALSE, Boolean.FALSE, null, item.getDate(), item.getEnclosure().getLocation().toExternalForm(), "audio/mpeg", item.getEnclosure().getLength(), 0, new Long(0), new Integer(0), 0, 0, videocast.getId(), new Integer(0), null));
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
                PersistentValueManager.savePersistentValue(Videocasting.class.getName() + "." + videocast.getPath(), content, ttl * 60);
            }
        } catch (Exception ex) {
            Tools.logException(Videocasting.class, ex, "Could not download listing: " + videocast.getPath());
            return null;
        }
    }
    return videocast;
}
