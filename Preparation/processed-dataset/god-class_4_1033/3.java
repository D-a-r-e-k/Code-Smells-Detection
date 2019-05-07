public void handle(HttpRequest httprequest) throws IOException {
    String s = httprequest.getURI();
    log.debug(s);
    String remoteAddress = httprequest.getInetAddress().getHostAddress();
    log.debug(remoteAddress);
    ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
    GoBackConfiguration goBackConfiguration = serverConfiguration.getGoBackConfiguration();
    ItemURL itemURL = new ItemURL(s);
    if (itemURL.getParameter(Constants.COMMAND) != null && itemURL.getParameter(Constants.COMMAND).equalsIgnoreCase(Constants.COMMAND_QUERY_CONTAINER)) {
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
            log.debug(httprequest.getInetAddress().getHostAddress() + " I/O Exception handling " + " HTTP " + httprequest.getMethod() + " " + s + ": " + ioexception1.getMessage());
        }
    } else {
        if (itemURL.getPath().startsWith("/TiVoConnect/TivoNowPlaying/Galleon")) {
            String path = null;
            if (itemURL.getPath().startsWith("/TiVoConnect/TivoNowPlaying/GalleonRecordings/"))
                path = serverConfiguration.getRecordingsPath() + "/" + itemURL.getPath().substring("/TiVoConnect/TivoNowPlaying/GalleonRecordings/".length());
            else {
                String container = itemURL.getPath().substring("/TiVoConnect/TivoNowPlaying/GalleonExtra/".length());
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
                            while (tokenizer.hasMoreTokens()) path = path + "/" + tokenizer.nextToken();
                            break;
                        }
                    }
                }
            }
            File file = new File(path);
            if (file.exists()) {
                try {
                    PrintWriter printWriter = null;
                    try {
                        httprequest.reply(200, "Success");
                        String details = getVideoDetails(file);
                        //details = Tools.getFile(new File("d:/galleon/TiVoVideoDetails.xml")); 
                        if (details != null) {
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
                    log.debug(httprequest.getInetAddress().getHostAddress() + " I/O Exception handling " + " HTTP " + httprequest.getMethod() + " " + s + ": " + ioexception1.getMessage());
                }
            }
        }
        try {
            String path = null;
            if (itemURL.getPath().startsWith("/TiVoConnect/GalleonRecordings/"))
                path = serverConfiguration.getRecordingsPath() + "/" + itemURL.getPath().substring("/TiVoConnect/GalleonRecordings/".length());
            else if (itemURL.getPath().startsWith("/TiVoConnect/GalleonExtra/")) {
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
                            while (tokenizer.hasMoreTokens()) path = path + "/" + tokenizer.nextToken();
                            break;
                        }
                    }
                }
            }
            if (path != null) {
                File file = new File(path);
                if (!file.exists()) {
                    // Handle shortcuts 
                    File directory = new File(serverConfiguration.getRecordingsPath());
                    FileSystemContainer fileSystemContainer = new FileSystemContainer(directory.getCanonicalPath(), true);
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
                    if (inputstream != null && inputstream.available() > 0) {
                        if (itemURL.getParameter(Constants.PARAMETER_SEEK) != null) {
                            httprequest.reply(206, "Partial Content");
                            try {
                                int seek = Integer.parseInt(itemURL.getParameter(Constants.PARAMETER_SEEK));
                                inputstream.skip(seek);
                            } catch (Exception ex) {
                            }
                        } else {
                            Headers headers = httprequest.getHeaders();
                            String range = headers.get("range");
                            if (range != null) {
                                Pattern pattern = Pattern.compile("bytes=(.*)-");
                                Matcher matcher = pattern.matcher(range);
                                if (matcher.find()) {
                                    range = matcher.group(1);
                                }
                                try {
                                    int seek = Integer.parseInt(range);
                                    log.debug("retry at: " + seek);
                                    if (seek > 0) {
                                        httprequest.reply(206, "Partial Content");
                                        inputstream.skip(seek);
                                    } else
                                        httprequest.reply(200, "Success");
                                } catch (Exception ex) {
                                }
                            } else
                                httprequest.reply(200, "Success");
                        }
                        try {
                            long startTime = System.currentTimeMillis();
                            long lastTime = startTime;
                            PersistentValueManager.savePersistentValue("VideoServer.lastUpdate", new Date().toString());
                            OutputStream outputstream = httprequest.getOutputStream(inputstream.available());
                            byte abyte0[] = new byte[4380];
                            int i;
                            while ((i = inputstream.read(abyte0, 0, abyte0.length)) > 0) {
                                outputstream.write(abyte0, 0, i);
                                if ((System.currentTimeMillis() - lastTime) > 1000 * 30) {
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
                                if (video != null) {
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
            } else
                httprequest.reply(404, "File not found");
        } catch (IOException ioexception1) {
            log.debug(httprequest.getInetAddress().getHostAddress() + " I/O Exception handling " + " HTTP " + httprequest.getMethod() + " " + s + ": " + ioexception1.getMessage());
        } catch (Exception ex) {
            Tools.logException(VideoServer.class, ex);
        }
    }
}
