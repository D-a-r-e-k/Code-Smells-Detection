package org.lnicholls.galleon.server;

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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.net.*;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.util.Tools;

public class TiVoListener implements ServiceListener, ServiceTypeListener {
    private static Logger log = Logger.getLogger(TiVoListener.class.getName());

    // TiVo with 7.1 software supports rendevouz and has a web server
    private final static String HTTP_SERVICE = "_http._tcp.local.";

    private final static String TIVO_PLATFORM = "platform";

    private final static String TIVO_PLATFORM_PREFIX = "tcd"; // platform=tcd/Series2

    private final static String TIVO_TSN = "TSN";

    private final static String TIVO_SW_VERSION = "swversion";

    private final static String TIVO_PATH = "path";

    public TiVoListener() {
        try {
            ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
            String address = serverConfiguration.getIPAddress();
            InetAddress inetAddress = InetAddress.getLocalHost();
            if (address!=null && address.length()>0)
                inetAddress = InetAddress.getByName(address);

            mJmDNS = new JmDNS(inetAddress);
            mJmDNS.addServiceListener(HTTP_SERVICE, this);
            log.debug("Interface: "+mJmDNS.getInterface());
        } catch (IOException ex) {
            Tools.logException(TiVoListener.class, ex);
        }
    }    public void close() {    	if (mJmDNS!=null) {    		mJmDNS.close();    	}    }
    public void addService(JmDNS jmdns, String type, String name) {
        if (name.endsWith("." + type)) {
            name = name.substring(0, name.length() - (type.length() + 1));
        }
        log.debug("addService: " + name);

        ServiceInfo service = jmdns.getServiceInfo(type, name);
        if (service == null) {
            log.error("Service not found: " + type + " (" + name + ")");
        } else {
            if (!name.endsWith(".")) {
                name = name + "." + type;
            }
            jmdns.requestServiceInfo(type, name);
        }
    }

    public void removeService(JmDNS jmdns, String type, String name) {
        if (name.endsWith("." + type)) {
            name = name.substring(0, name.length() - (type.length() + 1));
        }
        log.debug("removeService: " + name);
    }

    public void addServiceType(JmDNS jmdns, String type) {
        log.debug("addServiceType: " + type);
    }

    public void resolveService(JmDNS jmdns, String type, String name, ServiceInfo info) {
        log.debug("resolveService: " + type + " (" + name + ")");

        /*
         * DVR AAB0._http._tcp.local. // name DVR-AAB0.local.:80 // server:port 192.168.0.5:80 // address:port
         * platform=tcd/Series2 TSN=24020348251AAB0 swversion=7.1.R1-01-2-240 path=/index.html
         */

        if (type.equals(HTTP_SERVICE)) {
            if (info == null) {
                log.error("Service not found: " + type + "(" + name + ")");
            } else {
                boolean found = false;
                TiVo tivo = new TiVo();
                tivo.setName(name.substring(0, name.length() - (type.length() + 1)));
                tivo.setServer(info.getServer());
                tivo.setPort(info.getPort());
                tivo.setAddress(info.getAddress());

                found = info.getPropertyString(TIVO_PLATFORM)!=null && info.getPropertyString(TIVO_TSN)!=null;
                for (Enumeration names = info.getPropertyNames(); names.hasMoreElements();) {
                    String prop = (String) names.nextElement();
                    if (prop.equals(TIVO_PLATFORM)) {
                        tivo.setPlatform(info.getPropertyString(prop));
                        /*
                        if (tivo.getPlatform().startsWith(TIVO_PLATFORM_PREFIX))
                            found = true;
                        */
                    } else if (prop.equals(TIVO_TSN)) {
                        tivo.setServiceNumber(info.getPropertyString(prop));
                    } else if (prop.equals(TIVO_SW_VERSION)) {
                        tivo.setSoftwareVersion(info.getPropertyString(prop));
                    } else if (prop.equals(TIVO_PATH)) {
                        tivo.setPath(info.getPropertyString(prop));
                    }
                }

                if (found) {
                    List tivos = Server.getServer().getTiVos();
                    boolean matched = false;
                    Iterator iterator = tivos.iterator();
                    while (iterator.hasNext()) {
                        TiVo knownTiVo = (TiVo) iterator.next();
                        if (knownTiVo.getAddress().equals(tivo.getAddress())) {
                            matched = true;
                            boolean modified = false;
                            if (!tivo.getPlatform().equals(knownTiVo.getPlatform()))
                            {
                                knownTiVo.setPlatform(tivo.getPlatform());
                                modified = true;
                            }
                            if (!tivo.getServiceNumber().equals(knownTiVo.getServiceNumber()))
                            {
                                knownTiVo.setServiceNumber(tivo.getServiceNumber());
                                modified = true;
                            }
                            if (!tivo.getSoftwareVersion().equals(knownTiVo.getSoftwareVersion()))
                            {
                                knownTiVo.setSoftwareVersion(tivo.getSoftwareVersion());
                                modified = true;
                            }
                            if (!tivo.getPath().equals(knownTiVo.getPath()))
                            {
                                knownTiVo.setPath(tivo.getPath());
                                modified = true;
                            }
                            if (!tivo.getServer().equals(knownTiVo.getServer()))
                            {
                                knownTiVo.setServer(tivo.getServer());
                                modified = true;
                            }
                            if (tivo.getPort()!=knownTiVo.getPort())
                            {
                                knownTiVo.setPort(tivo.getPort());
                                modified = true;
                            }
                            if (modified)
                                Server.getServer().updateTiVos(tivos);
                        }
                    }
                    if (!matched) {
                        tivos.add(tivo);
                        Server.getServer().updateTiVos(tivos);
                        log.info("Found TiVo: " + tivo.toString());
                    }
                }
            }
        }
    }    private JmDNS mJmDNS;
}