private String getVideoDetails(File file) {
    Video video = getVideo(file);
    if (video != null) {
        try {
            StringBuffer buffer = new StringBuffer();
            synchronized (buffer) {
                buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><TvBusMarshalledStruct:TvBusEnvelope xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:TvBusMarshalledStruct=\"http://tivo.com/developer/xml/idl/TvBusMarshalledStruct\" xmlns:TvPgdRecording=\"http://tivo.com/developer/xml/idl/TvPgdRecording\" xmlns:TvBusDuration=\"http://tivo.com/developer/xml/idl/TvBusDuration\" xmlns:TvPgdShowing=\"http://tivo.com/developer/xml/idl/TvPgdShowing\" xmlns:TvDbShowingBit=\"http://tivo.com/developer/xml/idl/TvDbShowingBit\" xmlns:TvBusDateTime=\"http://tivo.com/developer/xml/idl/TvBusDateTime\" xmlns:TvPgdProgram=\"http://tivo.com/developer/xml/idl/TvPgdProgram\" xmlns:TvDbColorCode=\"http://tivo.com/developer/xml/idl/TvDbColorCode\" xmlns:TvPgdSeries=\"http://tivo.com/developer/xml/idl/TvPgdSeries\" xmlns:TvDbShowType=\"http://tivo.com/developer/xml/idl/TvDbShowType\" xmlns:TvPgdChannel=\"http://tivo.com/developer/xml/idl/TvPgdChannel\" xmlns:TvDbTvRating=\"http://tivo.com/developer/xml/idl/TvDbTvRating\" xmlns:TvDbRecordQuality=\"http://tivo.com/developer/xml/idl/TvDbRecordQuality\" xmlns:TvDbBitstreamFormat=\"http://tivo.com/developer/xml/idl/TvDbBitstreamFormat\" xs:schemaLocation=\"http://tivo.com/developer/xml/idl/TvBusMarshalledStruct TvBusMarshalledStruct.xsd http://tivo.com/developer/xml/idl/TvPgdRecording TvPgdRecording.xsd http://tivo.com/developer/xml/idl/TvBusDuration TvBusDuration.xsd http://tivo.com/developer/xml/idl/TvPgdShowing TvPgdShowing.xsd http://tivo.com/developer/xml/idl/TvDbShowingBit TvDbShowingBit.xsd http://tivo.com/developer/xml/idl/TvBusDateTime TvBusDateTime.xsd http://tivo.com/developer/xml/idl/TvPgdProgram TvPgdProgram.xsd http://tivo.com/developer/xml/idl/TvDbColorCode TvDbColorCode.xsd http://tivo.com/developer/xml/idl/TvPgdSeries TvPgdSeries.xsd http://tivo.com/developer/xml/idl/TvDbShowType TvDbShowType.xsd http://tivo.com/developer/xml/idl/TvPgdChannel TvPgdChannel.xsd http://tivo.com/developer/xml/idl/TvDbTvRating TvDbTvRating.xsd http://tivo.com/developer/xml/idl/TvDbRecordQuality TvDbRecordQuality.xsd http://tivo.com/developer/xml/idl/TvDbBitstreamFormat TvDbBitstreamFormat.xsd\" xs:type=\"TvPgdRecording:TvPgdRecording\">\n");
                if (video.getDuration() > 0)
                    buffer.append("<recordedDuration>" + mDurationFormat.format(new Date(video.getDuration())) + "</recordedDuration>\n");
                else
                    buffer.append("<recordedDuration>PT00M</recordedDuration>\n");
                // PT59M59S 
                buffer.append("<vActualShowing>\n");
                buffer.append("<element>\n");
                buffer.append("<showingBits value=\"1027\"/>\n");
                buffer.append("<time>" + mTimeDateFormat.format(video.getDateRecorded()) + "</time>\n");
                if (video.getDuration() > 0)
                    buffer.append("<duration>" + mDurationFormat.format(new Date(video.getDuration())) + "</duration>\n");
                else
                    buffer.append("<duration>PT00M</duration>\n");
                // PT1H, 
                // PT30M 
                if (video.getPartCount() != null)
                    buffer.append("<partCount>" + video.getPartCount() + "</partCount>\n");
                if (video.getPartIndex() != null)
                    buffer.append("<partIndex>" + video.getPartIndex() + "</partIndex>\n");
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
                if (video.getColorCode() != 0)
                    buffer.append("<colorCode value=\"" + video.getColorCode() + "\">COLOR</colorCode>\n");
                else
                    buffer.append("<colorCode value=\"4\">COLOR</colorCode>\n");
                if (video.getDescription() != null && video.getDescription().trim().length() > 0)
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
                if (video.getEpisodeTitle() != null && video.getEpisodeTitle().trim().length() > 0)
                    buffer.append("<episodeTitle>" + Tools.escapeXMLChars(video.getEpisodeTitle()) + "</episodeTitle>\n");
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
                    buffer.append("<originalAirDate>" + mTimeDateFormat.format(video.getOriginalAirDate()) + "</originalAirDate>\n");
                else
                    buffer.append("<originalAirDate>" + mTimeDateFormat.format(new Date(file.lastModified())) + "</originalAirDate>\n");
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
                buffer.append("<recordingQuality value=\"" + recordingQualityValue + "\">" + recordingQuality + "</recordingQuality>\n");
                buffer.append("<showing>\n");
                buffer.append("<showingBits value=\"1027\"/>\n");
                if (video.getDateRecorded() != null)
                    buffer.append("<time>" + mTimeDateFormat.format(video.getDateRecorded()) + "</time>\n");
                else
                    buffer.append("<time>" + mTimeDateFormat.format(new Date(file.lastModified())) + "</time>\n");
                if (video.getDuration() > 0)
                    buffer.append("<duration>" + mDurationFormat.format(new Date(video.getDuration())) + "</duration>\n");
                else
                    buffer.append("<duration>PT00M</duration>\n");
                // PT1H, 
                // PT30M 
                if (video.getPartCount() != null)
                    buffer.append("<partCount>" + video.getPartCount() + "</partCount>\n");
                if (video.getPartIndex() != null)
                    buffer.append("<partIndex>" + video.getPartIndex() + "</partIndex>\n");
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
                if (video.getColorCode() != 0)
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
                if (video.getEpisodeTitle() != null && video.getEpisodeTitle().trim().length() > 0)
                    buffer.append("<episodeTitle>" + Tools.escapeXMLChars(video.getEpisodeTitle()) + "</episodeTitle>\n");
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
                    buffer.append("<originalAirDate>" + mTimeDateFormat.format(video.getOriginalAirDate()) + "</originalAirDate>\n");
                else
                    buffer.append("<originalAirDate>" + mTimeDateFormat.format(new Date(file.lastModified())) + "</originalAirDate>\n");
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
                    buffer.append("<startTime>" + mTimeDateFormat.format(new Date(file.lastModified())) + "</startTime>\n");
                if (video.getStopTime() != null)
                    buffer.append("<stopTime>" + mTimeDateFormat.format(video.getStopTime()) + "</stopTime>\n");
                else
                    buffer.append("<stopTime>" + mTimeDateFormat.format(new Date(file.lastModified())) + "</stopTime>\n");
                if (video.getExpirationTime() != null)
                    buffer.append("<expirationTime>" + mTimeDateFormat.format(video.getExpirationTime()) + "</expirationTime>\n");
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
