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
    if (imdb == null || imdb.length() == 0) {
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
            filter = new AndFilter(new TagNameFilter("IMG"), new HasParentFilter(new AndFilter(new TagNameFilter("A"), new HasAttributeFilter("name", "poster"))));
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
            filter = new AndFilter(new AndFilter(new TagNameFilter("STRONG"), new HasAttributeFilter("class", "title")), new HasParentFilter(new TagNameFilter("TD")));
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
                        if (cleanSpaces(value.getText()).startsWith("directedby")) {
                            //Directed by 
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value != null && value instanceof LinkTag) {
                                    LinkTag link = (LinkTag) value;
                                    director = link.getLinkText();
                                    break;
                                }
                            }
                        } else if (cleanSpaces(value.getText()).startsWith("writingcredits")) {
                            //Writing credits 
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value != null && value instanceof LinkTag) {
                                    LinkTag link = (LinkTag) value;
                                    if (link.getLinkText().trim().equals("(more)"))
                                        break;
                                    else {
                                        if (link.getLink().indexOf("/name") != -1) {
                                            if (credits == null)
                                                credits = link.getLinkText();
                                            else
                                                credits = credits + ", " + link.getLinkText();
                                        }
                                    }
                                }
                            }
                        } else if (cleanSpaces(value.getText()).startsWith("castoverview,firstbilledonly")) {
                            //Cast overview, first billed only: 
                            parent = (CompositeTag) parent.getParent();
                            // tr 
                            parent = (CompositeTag) parent.getParent();
                            // table 
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
                        if (cleanSpaces(text).startsWith("genre")) {
                            //Genre: 
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
                        } else if (cleanSpaces(text).startsWith("plotoutline")) {
                            //Plot Outline: 
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value != null && value.getText().equals("/b")) {
                                    value = parent.childAt(++position);
                                    plotOutline = value.getText();
                                    break;
                                }
                            }
                        } else if (cleanSpaces(text).startsWith("userrating")) {
                            //User Rating: 
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
                        } else if (cleanSpaces(text).startsWith("mpaa")) {
                            //MPAA 
                            boolean foundFirst = false;
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value != null && value.getText().equals("/b")) {
                                    value = parent.childAt(++position);
                                    rated = value.getText();
                                    String REGEX = "Rated (.*) for (.*)";
                                    // Rated 
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
                            title = token.trim();
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
                            genre = token.substring(6).trim();
                    } else if (lower.equals("genre:") || lower.equals("genre")) {
                        genreNext = true;
                    } else if (lower.startsWith("directedby") && lower.length() > 11) {
                        if (director == null)
                            director = token.substring(11).trim();
                    } else if (lower.equals("directedby:") || lower.equals("directedby")) {
                        directedNext = true;
                    } else if (lower.startsWith("plotoutline") && lower.length() > 13) {
                        if (plotOutline == null)
                            plotOutline = token.substring(13).trim();
                    } else if (lower.equals("plotoutline:") || lower.equals("plotoutline")) {
                        plotOutlineNext = true;
                    } else if (lower.startsWith("userrating") && lower.length() > 12) {
                        if (rating == null)
                            rating = token.substring(12).trim();
                    } else if (lower.equals("userrating:") || lower.equals("userrating")) {
                        userRatingNext = true;
                    } else if (lower.startsWith("top250") && lower.length() > 8) {
                        if (top250 == null)
                            top250 = token.substring(8).trim();
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
            if (movie.getRating() == 0 && rating != null) {
                try {
                    movie.setRating(Integer.parseInt(rating));
                } catch (Exception ex) {
                }
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
