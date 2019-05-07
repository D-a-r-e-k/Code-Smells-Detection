package org.lnicholls.galleon.database;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * Auto-generated using Hibernate hbm2java tool.
 * Copyright (C) 2005, 2006 Leon Nicholls
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * See the file "COPYING" for more details.
 *     
*/
public class Video implements org.lnicholls.galleon.media.Media,Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String title;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String channel;

    /** nullable persistent field */
    private String station;

    /** nullable persistent field */
    private String rating;

    /** nullable persistent field */
    private Date dateRecorded;

    /** persistent field */
    private int duration;

    /** persistent field */
    private long size;

    /** persistent field */
    private int status;

    /** persistent field */
    private String path;

    /** nullable persistent field */
    private String url;

    /** nullable persistent field */
    private String icon;

    /** nullable persistent field */
    private String choreographers;

    /** persistent field */
    private int colorCode;

    /** nullable persistent field */
    private String directors;

    /** nullable persistent field */
    private String episodeTitle;

    /** persistent field */
    private int episodeNumber;

    /** nullable persistent field */
    private String execProducers;

    /** nullable persistent field */
    private String programGenre;

    /** nullable persistent field */
    private String guestStars;

    /** nullable persistent field */
    private String actors;

    /** nullable persistent field */
    private String hosts;

    /** nullable persistent field */
    private Boolean episodic;

    /** nullable persistent field */
    private Date originalAirDate;

    /** nullable persistent field */
    private String producers;

    /** nullable persistent field */
    private String seriesGenre;

    /** nullable persistent field */
    private String seriesTitle;

    /** nullable persistent field */
    private String showType;

    /** nullable persistent field */
    private String writers;

    /** nullable persistent field */
    private String advisories;

    /** persistent field */
    private int channelMajorNumber;

    /** persistent field */
    private int channelMinorNumber;

    /** nullable persistent field */
    private String callsign;

    /** nullable persistent field */
    private String recordingQuality;

    /** nullable persistent field */
    private Date startTime;

    /** nullable persistent field */
    private Date stopTime;

    /** nullable persistent field */
    private Date expirationTime;

    /** nullable persistent field */
    private Date dateModified;

    /** persistent field */
    private String mimeType;

    /** nullable persistent field */
    private String source;

    /** nullable persistent field */
    private String bookmarks;

    /** persistent field */
    private int downloadTime;

    /** persistent field */
    private long downloadSize;

    /** nullable persistent field */
    private String origen;

    /** nullable persistent field */
    private Integer playCount;

    /** nullable persistent field */
    private String tone;

    /** nullable persistent field */
    private String videoResolution;

    /** nullable persistent field */
    private String videoCodec;

    /** nullable persistent field */
    private Float videoRate;

    /** nullable persistent field */
    private Integer videoBitRate;

    /** nullable persistent field */
    private String audioCodec;

    /** nullable persistent field */
    private Float audioRate;

    /** nullable persistent field */
    private Integer audioBitRate;

    /** nullable persistent field */
    private Integer audioChannels;

    /** nullable persistent field */
    private String color;

    /** nullable persistent field */
    private Integer showTypeValue;

    /** nullable persistent field */
    private Integer ratingValue;

    /** nullable persistent field */
    private Integer recordingQualityValue;

    /** nullable persistent field */
    private Integer partCount;

    /** nullable persistent field */
    private Integer partIndex;

    /** nullable persistent field */
    private Date datePlayed;

    /** nullable persistent field */
    private Date dateDownloaded;

    /** nullable persistent field */
    private Date dateUploaded;

    /** nullable persistent field */
    private String uploaded;

    /** nullable persistent field */
    private Integer availability;

    /** nullable persistent field */
    private Boolean parentalControls;

    /** nullable persistent field */
    private String tivo;

    /** full constructor */
    public Video(String title, String description, String channel, String station, String rating, Date dateRecorded, int duration, long size, int status, String path, String url, String icon, String choreographers, int colorCode, String directors, String episodeTitle, int episodeNumber, String execProducers, String programGenre, String guestStars, String actors, String hosts, Boolean episodic, Date originalAirDate, String producers, String seriesGenre, String seriesTitle, String showType, String writers, String advisories, int channelMajorNumber, int channelMinorNumber, String callsign, String recordingQuality, Date startTime, Date stopTime, Date expirationTime, Date dateModified, String mimeType, String source, String bookmarks, int downloadTime, long downloadSize, String origen, Integer playCount, String tone, String videoResolution, String videoCodec, Float videoRate, Integer videoBitRate, String audioCodec, Float audioRate, Integer audioBitRate, Integer audioChannels, String color, Integer showTypeValue, Integer ratingValue, Integer recordingQualityValue, Integer partCount, Integer partIndex, Date datePlayed, Date dateDownloaded, Date dateUploaded, String uploaded, Integer availability, Boolean parentalControls, String tivo) {
        this.title = title;
        this.description = description;
        this.channel = channel;
        this.station = station;
        this.rating = rating;
        this.dateRecorded = dateRecorded;
        this.duration = duration;
        this.size = size;
        this.status = status;
        this.path = path;
        this.url = url;
        this.icon = icon;
        this.choreographers = choreographers;
        this.colorCode = colorCode;
        this.directors = directors;
        this.episodeTitle = episodeTitle;
        this.episodeNumber = episodeNumber;
        this.execProducers = execProducers;
        this.programGenre = programGenre;
        this.guestStars = guestStars;
        this.actors = actors;
        this.hosts = hosts;
        this.episodic = episodic;
        this.originalAirDate = originalAirDate;
        this.producers = producers;
        this.seriesGenre = seriesGenre;
        this.seriesTitle = seriesTitle;
        this.showType = showType;
        this.writers = writers;
        this.advisories = advisories;
        this.channelMajorNumber = channelMajorNumber;
        this.channelMinorNumber = channelMinorNumber;
        this.callsign = callsign;
        this.recordingQuality = recordingQuality;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.expirationTime = expirationTime;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.source = source;
        this.bookmarks = bookmarks;
        this.downloadTime = downloadTime;
        this.downloadSize = downloadSize;
        this.origen = origen;
        this.playCount = playCount;
        this.tone = tone;
        this.videoResolution = videoResolution;
        this.videoCodec = videoCodec;
        this.videoRate = videoRate;
        this.videoBitRate = videoBitRate;
        this.audioCodec = audioCodec;
        this.audioRate = audioRate;
        this.audioBitRate = audioBitRate;
        this.audioChannels = audioChannels;
        this.color = color;
        this.showTypeValue = showTypeValue;
        this.ratingValue = ratingValue;
        this.recordingQualityValue = recordingQualityValue;
        this.partCount = partCount;
        this.partIndex = partIndex;
        this.datePlayed = datePlayed;
        this.dateDownloaded = dateDownloaded;
        this.dateUploaded = dateUploaded;
        this.uploaded = uploaded;
        this.availability = availability;
        this.parentalControls = parentalControls;
        this.tivo = tivo;
    }

    /** default constructor */
    public Video() {
    }

    /** minimal constructor */
    public Video(String title, int duration, long size, int status, String path, int colorCode, int episodeNumber, int channelMajorNumber, int channelMinorNumber, String mimeType, int downloadTime, long downloadSize) {
        this.title = title;
        this.duration = duration;
        this.size = size;
        this.status = status;
        this.path = path;
        this.colorCode = colorCode;
        this.episodeNumber = episodeNumber;
        this.channelMajorNumber = channelMajorNumber;
        this.channelMinorNumber = channelMinorNumber;
        this.mimeType = mimeType;
        this.downloadTime = downloadTime;
        this.downloadSize = downloadSize;
    }

    public Integer getId() {
        return this.id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStation() {
        return this.station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Date getDateRecorded() {
        return this.dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getChoreographers() {
        return this.choreographers;
    }

    public void setChoreographers(String choreographers) {
        this.choreographers = choreographers;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public String getDirectors() {
        return this.directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getEpisodeTitle() {
        return this.episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public int getEpisodeNumber() {
        return this.episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getExecProducers() {
        return this.execProducers;
    }

    public void setExecProducers(String execProducers) {
        this.execProducers = execProducers;
    }

    public String getProgramGenre() {
        return this.programGenre;
    }

    public void setProgramGenre(String programGenre) {
        this.programGenre = programGenre;
    }

    public String getGuestStars() {
        return this.guestStars;
    }

    public void setGuestStars(String guestStars) {
        this.guestStars = guestStars;
    }

    public String getActors() {
        return this.actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getHosts() {
        return this.hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public Boolean getEpisodic() {
        return this.episodic;
    }

    public void setEpisodic(Boolean episodic) {
        this.episodic = episodic;
    }

    public Date getOriginalAirDate() {
        return this.originalAirDate;
    }

    public void setOriginalAirDate(Date originalAirDate) {
        this.originalAirDate = originalAirDate;
    }

    public String getProducers() {
        return this.producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getSeriesGenre() {
        return this.seriesGenre;
    }

    public void setSeriesGenre(String seriesGenre) {
        this.seriesGenre = seriesGenre;
    }

    public String getSeriesTitle() {
        return this.seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public String getShowType() {
        return this.showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getWriters() {
        return this.writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getAdvisories() {
        return this.advisories;
    }

    public void setAdvisories(String advisories) {
        this.advisories = advisories;
    }

    public int getChannelMajorNumber() {
        return this.channelMajorNumber;
    }

    public void setChannelMajorNumber(int channelMajorNumber) {
        this.channelMajorNumber = channelMajorNumber;
    }

    public int getChannelMinorNumber() {
        return this.channelMinorNumber;
    }

    public void setChannelMinorNumber(int channelMinorNumber) {
        this.channelMinorNumber = channelMinorNumber;
    }

    public String getCallsign() {
        return this.callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getRecordingQuality() {
        return this.recordingQuality;
    }

    public void setRecordingQuality(String recordingQuality) {
        this.recordingQuality = recordingQuality;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getExpirationTime() {
        return this.expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    /** 
     * When the video was modified
     */
    public Date getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBookmarks() {
        return this.bookmarks;
    }

    public void setBookmarks(String bookmarks) {
        this.bookmarks = bookmarks;
    }

    /** 
     * Download time in seconds
     */
    public int getDownloadTime() {
        return this.downloadTime;
    }

    public void setDownloadTime(int downloadTime) {
        this.downloadTime = downloadTime;
    }

    /** 
     * Download size in bytes
     */
    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getOrigen() {
        return this.origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Integer getPlayCount() {
        return this.playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    /** 
     * The mood of the track
     */
    public String getTone() {
        return this.tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getVideoResolution() {
        return this.videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    public String getVideoCodec() {
        return this.videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public Float getVideoRate() {
        return this.videoRate;
    }

    public void setVideoRate(Float videoRate) {
        this.videoRate = videoRate;
    }

    public Integer getVideoBitRate() {
        return this.videoBitRate;
    }

    public void setVideoBitRate(Integer videoBitRate) {
        this.videoBitRate = videoBitRate;
    }

    public String getAudioCodec() {
        return this.audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public Float getAudioRate() {
        return this.audioRate;
    }

    public void setAudioRate(Float audioRate) {
        this.audioRate = audioRate;
    }

    public Integer getAudioBitRate() {
        return this.audioBitRate;
    }

    public void setAudioBitRate(Integer audioBitRate) {
        this.audioBitRate = audioBitRate;
    }

    public Integer getAudioChannels() {
        return this.audioChannels;
    }

    public void setAudioChannels(Integer audioChannels) {
        this.audioChannels = audioChannels;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getShowTypeValue() {
        return this.showTypeValue;
    }

    public void setShowTypeValue(Integer showTypeValue) {
        this.showTypeValue = showTypeValue;
    }

    public Integer getRatingValue() {
        return this.ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Integer getRecordingQualityValue() {
        return this.recordingQualityValue;
    }

    public void setRecordingQualityValue(Integer recordingQualityValue) {
        this.recordingQualityValue = recordingQualityValue;
    }

    public Integer getPartCount() {
        return this.partCount;
    }

    public void setPartCount(Integer partCount) {
        this.partCount = partCount;
    }

    public Integer getPartIndex() {
        return this.partIndex;
    }

    public void setPartIndex(Integer partIndex) {
        this.partIndex = partIndex;
    }

    /** 
     * When the track was last played
     */
    public Date getDatePlayed() {
        return this.datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public Date getDateDownloaded() {
        return this.dateDownloaded;
    }

    public void setDateDownloaded(Date dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }

    public Date getDateUploaded() {
        return this.dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getUploaded() {
        return this.uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public Integer getAvailability() {
        return this.availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Boolean getParentalControls() {
        return this.parentalControls;
    }

    public void setParentalControls(Boolean parentalControls) {
        this.parentalControls = parentalControls;
    }

    public String getTivo() {
        return this.tivo;
    }

    public void setTivo(String tivo) {
        this.tivo = tivo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

// The following is extra code specified in the hbm.xml files


    public boolean equals(Object object)

    {

        Video video = (Video)object;

        if (url!=null && video.url!=null)

	        return url.equals(video.url);

		else

			return false;

    }



    public String getStatusString() {

        switch (status) {

        case 1:

            return "Saving";

        case 2:

            return "Saved";

        case 4:

            return "Rule Matched";

        case 8:

            return "Save Cancelled";

        case 16:

            return "Recorded";

        case 32:

            return "Recording";

        case 64:

            return "Save Error";

        case 128:

            return "Save Selected";

        case 256:

            return "Deleted";

        }

        return "Recorded";

    }



    public static int STATUS_DOWNLOADING = 1;



    public static int STATUS_DOWNLOADED = 2;



    public static int STATUS_RULE_MATCHED = 4;



    public static int STATUS_USER_CANCELLED = 8;



    public static int STATUS_RECORDED = 16;



    public static int STATUS_RECORDING = 32;



    public static int STATUS_INCOMPLETE = 64;



    public static int STATUS_USER_SELECTED = 128;



    public static int STATUS_DELETED = 256;



    public static int RECORDING_AVAILABLE = 1;



    public static int RECORDING_DELETED = 2;



  
// end of extra code specified in the hbm.xml files
}
