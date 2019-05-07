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
            found = info.getPropertyString(TIVO_PLATFORM) != null && info.getPropertyString(TIVO_TSN) != null;
            for (Enumeration names = info.getPropertyNames(); names.hasMoreElements(); ) {
                String prop = (String) names.nextElement();
                if (prop.equals(TIVO_PLATFORM)) {
                    tivo.setPlatform(info.getPropertyString(prop));
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
                        if (!tivo.getPlatform().equals(knownTiVo.getPlatform())) {
                            knownTiVo.setPlatform(tivo.getPlatform());
                            modified = true;
                        }
                        if (!tivo.getServiceNumber().equals(knownTiVo.getServiceNumber())) {
                            knownTiVo.setServiceNumber(tivo.getServiceNumber());
                            modified = true;
                        }
                        if (!tivo.getSoftwareVersion().equals(knownTiVo.getSoftwareVersion())) {
                            knownTiVo.setSoftwareVersion(tivo.getSoftwareVersion());
                            modified = true;
                        }
                        if (!tivo.getPath().equals(knownTiVo.getPath())) {
                            knownTiVo.setPath(tivo.getPath());
                            modified = true;
                        }
                        if (!tivo.getServer().equals(knownTiVo.getServer())) {
                            knownTiVo.setServer(tivo.getServer());
                            modified = true;
                        }
                        if (tivo.getPort() != knownTiVo.getPort()) {
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
}
