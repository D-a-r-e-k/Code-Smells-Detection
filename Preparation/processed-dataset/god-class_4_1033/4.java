private String getContainer(ItemURL itemURL, String address) {
    ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
    GoBackConfiguration goBackConfiguration = serverConfiguration.getGoBackConfiguration();
    File directory = new File(serverConfiguration.getRecordingsPath());
    if (itemURL.getParameter(Constants.COMMAND).equalsIgnoreCase(Constants.COMMAND_QUERY_CONTAINER)) {
        if (itemURL.getParameter(Constants.PARAMETER_CONTAINER).equals("/")) {
            StringBuffer buffer = new StringBuffer();
            synchronized (buffer) {
                if (!Tools.isLocal(address) && serverConfiguration.canShare()) {
                    log.info("Remote connection: " + address);
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
                    if (apps.size() > 0) {
                        Pattern pattern = Pattern.compile("^http://(.*):(.*)/(.*)/$");
                        Iterator iterator = apps.iterator();
                        while (iterator.hasNext()) {
                            NameValue nameValue = (NameValue) iterator.next();
                            Matcher matcher = pattern.matcher(nameValue.getValue());
                            if (matcher.find()) {
                                buffer.append("<Item>\n");
                                buffer.append("<Details>\n");
                                buffer.append("<ContentType>application/x-hme</ContentType>\n");
                                buffer.append("<SourceFormat>x-container/folder</SourceFormat>\n");
                                buffer.append("<Title>" + Tools.escapeXMLChars(nameValue.getName()) + "</Title>\n");
                                buffer.append("</Details>\n");
                                buffer.append("<Links>\n");
                                buffer.append("<Content>\n");
                                buffer.append("<Url>" + "http://" + serverConfiguration.getPublicIPAddress() + ":" + serverConfiguration.getPort() + "/" + matcher.group(3) + "/" + "</Url>\n");
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
                } else if (goBackConfiguration.isEnabled()) {
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
                        buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(directory.lastModified())) + "</LastChangeDate>\n");
                        buffer.append("</Details>\n");
                        buffer.append("<Links>\n");
                        buffer.append("<Content>\n");
                        buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":" + Server.getServer().getHMOPort() + "/TiVoConnect?Command=QueryContainer&amp;Container=GalleonRecordings</Url>\n");
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
                            buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(directory.lastModified())) + "</LastChangeDate>\n");
                            buffer.append("</Details>\n");
                            buffer.append("<Links>\n");
                            buffer.append("<Content>\n");
                            buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":" + Server.getServer().getHMOPort() + "/TiVoConnect?Command=QueryContainer&amp;Container=GalleonExtra/" + URLEncoder.encode(nameValue.getName()) + "</Url>\n");
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
                            buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(directory.lastModified())) + "</LastChangeDate>\n");
                            buffer.append("</Details>\n");
                            buffer.append("<Links>\n");
                            buffer.append("<Content>\n");
                            buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":" + Server.getServer().getHMOPort() + "/TiVoConnect?Command=QueryContainer&amp;Container=GalleonExtra/" + URLEncoder.encode(nameValue.getName()) + "</Url>\n");
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
                    String container = itemURL.getParameter(Constants.PARAMETER_CONTAINER);
                    StringTokenizer tokenizer = new StringTokenizer(container, "/");
                    if (tokenizer.hasMoreTokens()) {
                        tokenizer.nextToken();
                        // galleon 
                        if (tokenizer.hasMoreTokens()) {
                            String name = tokenizer.nextToken();
                            String rest = "";
                            while (tokenizer.hasMoreTokens()) rest = rest + "/" + tokenizer.nextToken();
                            List paths = goBackConfiguration.getPaths();
                            List published = new ArrayList();
                            published.addAll(mPublished);
                            published.addAll(paths);
                            Iterator iterator = published.iterator();
                            while (iterator.hasNext()) {
                                NameValue nameValue = (NameValue) iterator.next();
                                if (nameValue.getName().equals(name)) {
                                    directory = new File(nameValue.getValue() + rest);
                                    break;
                                }
                            }
                            // Could be a dir in published dir 
                            File subdir = new File(serverConfiguration.getRecordingsPath() + "/" + name + rest);
                            if (subdir.exists()) {
                                directory = subdir;
                            }
                        }
                    }
                }
                boolean folders = false;
                List files = new ArrayList();
                if (itemURL.getParameter(Constants.PARAMETER_RECURSE) != null) {
                    FileSystemContainer fileSystemContainer = new FileSystemContainer(directory.getCanonicalPath(), true);
                    files = fileSystemContainer.getItemsSorted(FileFilters.videoFilter);
                } else {
                    FileSystemContainer fileSystemContainer = new FileSystemContainer(directory.getCanonicalPath(), false);
                    files = fileSystemContainer.getItemsSorted(FileFilters.videoDirectoryFilter);
                    folders = true;
                }
                if (itemURL.getParameter(Constants.PARAMETER_SORT_ORDER) != null) {
                    if (!itemURL.getParameter(Constants.PARAMETER_SORT_ORDER).equals(Constants.PARAMETER_SORT_TITLE)) {
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
                                video = (Video) MediaManager.getMedia(file.getAbsolutePath());
                            } else if (file.lastModified() != video.getDateModified().getTime()) {
                                video = (Video) MediaManager.getMedia(file.getAbsolutePath());
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
                            path = anchorItemURL.getPath().substring("/TiVoConnect/GalleonRecordings/".length());
                            StringTokenizer tokenizer = new StringTokenizer(path, "/");
                            while (tokenizer.hasMoreTokens()) {
                                path = tokenizer.nextToken();
                            }
                        } else {
                            String container = anchorItemURL.getParameter(Constants.PARAMETER_CONTAINER);
                            if (container != null) {
                                if (container.indexOf("GalleonExtra/") != -1)
                                    container = container.substring("GalleonExtra/".length());
                            } else if (anchorItemURL.getPath().indexOf("/TiVoConnect/GalleonExtra/") != -1)
                                container = anchorItemURL.getPath().substring("/TiVoConnect/GalleonExtra/".length());
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
                                        while (tokenizer.hasMoreTokens()) rest = rest + "/" + tokenizer.nextToken();
                                        path = nameValue.getValue() + rest;
                                        break;
                                    }
                                }
                            }
                        }
                        if (path != null) {
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
                    }
                    if (start < 0)
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
                    buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(directory.lastModified())) + "</LastChangeDate>\n");
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
                            buffer.append("<LastChangeDate>0x" + Tools.dateToHex(new Date(file.lastModified())) + "</LastChangeDate>\n");
                            buffer.append("</Details>\n");
                            buffer.append("<Links>\n");
                            buffer.append("<Content>\n");
                            buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":" + Server.getServer().getHMOPort() + "/TiVoConnect?Command=QueryContainer&amp;Container=" + URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER) + "/" + Tools.escapeXMLChars(filePath)) + "</Url>\n");
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
                                }
                            } catch (Exception ex) {
                                log.error("Video retrieve failed", ex);
                            }
                            if (video == null) {
                                // Try to resync video file with database if moved 
                                try {
                                    List list = VideoManager.findByFilename(file.getName());
                                    if (list != null && list.size() == 1) {
                                        for (int j = 0; j < list.size(); j++) {
                                            video = (Video) list.get(j);
                                            if (!video.getPath().equals(file.getCanonicalPath())) {
                                                video.setPath(file.getCanonicalPath());
                                                VideoManager.updateVideo(video);
                                                break;
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    log.error("Video find failed", ex);
                                }
                                if (video == null) {
                                    try {
                                        video = (Video) MediaManager.getMedia(file.getAbsolutePath());
                                        VideoManager.createVideo(video);
                                    } catch (Exception ex) {
                                        log.error("Video create failed", ex);
                                    }
                                }
                            } else if (file.lastModified() != video.getDateModified().getTime()) {
                                video = (Video) MediaManager.getMedia(file.getAbsolutePath());
                            }
                            if (video == null) {
                                // || !file.getName().toLowerCase().endsWith(".tivo")) { 
                                buffer.append("<Item>\n");
                                buffer.append("<Details>\n");
                                buffer.append("<Title>" + Tools.escapeXMLChars(filename) + "</Title>\n");
                                buffer.append("<ContentType>video/mpeg</ContentType>\n");
                                buffer.append("<SourceFormat>video/mpeg</SourceFormat>\n");
                                buffer.append("<CaptureDate>0x" + Tools.dateToHex(new Date(file.lastModified())) + "</CaptureDate>\n");
                                buffer.append("<SourceSize>" + file.length() + "</SourceSize>\n");
                                buffer.append("<Duration>0</Duration>\n");
                                buffer.append("</Details>\n");
                                buffer.append("<Links>\n");
                                buffer.append("<Content>\n");
                                buffer.append("<ContentType>video/mpeg</ContentType>\n");
                                buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":" + Server.getServer().getHMOPort() + "/TiVoConnect/" + URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER) + "/" + Tools.escapeXMLChars(filePath)) + "</Url>\n");
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
                                    buffer.append("<Title>" + Tools.escapeXMLChars(video.getSeriesTitle()) + "</Title>\n");
                                else
                                    buffer.append("<Title>" + Tools.escapeXMLChars(video.getTitle()) + "</Title>\n");
                                buffer.append("<ContentType>video/x-tivo-mpeg</ContentType>\n");
                                buffer.append("<SourceFormat>video/x-tivo-mpeg</SourceFormat>\n");
                                if (video.getDuration() > 0)
                                    buffer.append("<Duration>" + video.getDuration() + "</Duration>\n");
                                else
                                    buffer.append("<Duration>0</Duration>\n");
                                if (video.getOriginalAirDate() != null)
                                    buffer.append("<CaptureDate>0x" + Tools.dateToHex(video.getOriginalAirDate()) + "</CaptureDate>\n");
                                else
                                    buffer.append("<CaptureDate>0x" + Tools.dateToHex(video.getDateRecorded()) + "</CaptureDate>\n");
                                if (video.getDescription() != null && video.getDescription().trim().length() > 0)
                                    buffer.append("<Description>" + Tools.escapeXMLChars(video.getDescription()) + "</Description>\n");
                                //else 
                                //	buffer.append("<Description></Description>\n"); 
                                if (video.getEpisodeTitle() != null && video.getEpisodeTitle().trim().length() > 0)
                                    buffer.append("<EpisodeTitle>" + Tools.escapeXMLChars(video.getEpisodeTitle()) + "</EpisodeTitle>\n");
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
                                buffer.append("<Url>http://" + serverConfiguration.getIPAddress() + ":" + Server.getServer().getHMOPort() + "/TiVoConnect/" + URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER) + "/" + Tools.escapeXMLChars(filePath)) + "</Url>\n");
                                buffer.append("</Content>\n");
                                buffer.append("<CustomIcon>\n");
                                buffer.append("<ContentType>video/*</ContentType>\n");
                                buffer.append("<AcceptsParams>No</AcceptsParams>\n");
                                buffer.append("<Url>urn:tivo:image:save-until-i-delete-recording</Url>\n");
                                buffer.append("</CustomIcon>\n");
                                buffer.append("<TiVoVideoDetails>\n");
                                buffer.append("<ContentType>text/xml</ContentType>\n");
                                buffer.append("<AcceptsParams>No</AcceptsParams>\n");
                                buffer.append("<Url>/TiVoConnect/TivoNowPlaying/" + URLEncoder.encode(itemURL.getParameter(Constants.PARAMETER_CONTAINER) + "/" + Tools.escapeXMLChars(filePath)) + "?Format=text%2Fxml</Url>\n");
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
