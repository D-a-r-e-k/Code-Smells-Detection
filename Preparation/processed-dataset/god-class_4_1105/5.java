/** Generate photons from all sources until the desired number has been collected. */
public void generatePhotons(PhotonSource source[]) {
    Thread currentThread = Thread.currentThread();
    double totalIntensity = 0.0, currentIntensity, totalRequested = 0.0;
    double sourceIntensity[] = new double[source.length], totalSourceIntensity = 0.0;
    // Determine the total intensity of all sources. 
    for (int i = 0; i < source.length; i++) {
        sourceIntensity[i] = source[i].getTotalIntensity();
        totalSourceIntensity += sourceIntensity[i];
    }
    currentIntensity = 0.1 * numWanted;
    // Generate photons. 
    photonList = new ArrayList<Photon>((int) (1.1 * numWanted));
    int iteration = 0;
    ThreadManager threads = new ThreadManager();
    try {
        while (photonList.size() < numWanted) {
            for (int i = 0; i < source.length; i++) {
                if (rt.renderThread != currentThread)
                    return;
                source[i].generatePhotons(this, currentIntensity * sourceIntensity[i] / totalSourceIntensity, threads);
                totalRequested += currentIntensity * sourceIntensity[i] / totalSourceIntensity;
            }
            if (photonList.size() >= numWanted * 0.9)
                break;
            if (photonList.size() == 0 && currentIntensity > 5.0 && iteration > 2)
                break;
            // Insignificant numbers of photons will be stored no matter how many we send out. 
            totalIntensity += currentIntensity;
            if (photonList.size() < 10)
                currentIntensity *= 10.0;
            else
                currentIntensity = (numWanted - photonList.size()) * totalIntensity / photonList.size();
            iteration++;
        }
    } finally {
        threads.finish();
    }
    lightScale = totalSourceIntensity / totalRequested;
    if (filter == 2)
        lightScale *= 3.0f;
    else if (filter == 1)
        lightScale *= 1.5f;
    // Create the balanced kd-tree. 
    int numPhotons = photonList.size();
    workspace = photonList.toArray(new Photon[numPhotons]);
    photonList = null;
    photon = new Photon[numPhotons];
    buildTree(0, numPhotons - 1, 0);
    workspace = null;
    // Select a maximum search radius.  We use two different methods to select cutoffs, one based on photon 
    // intensity and one based on density, then keep whichever cutoff is smaller.  First, find the N brightest 
    // photons in the map.  The PhotonList can help us to do this. 
    PhotonList nearbyPhotons = new PhotonList(numEstimate);
    RGBColor tempColor = new RGBColor();
    nearbyPhotons.init(0.0f);
    for (int i = 0; i < photon.length; i++) {
        tempColor.setERGB(photon[i].ergb);
        float intensity = -(tempColor.getRed() + tempColor.getGreen() + tempColor.getBlue());
        if (intensity <= nearbyPhotons.cutoff2)
            nearbyPhotons.addPhoton(photon[i], intensity);
    }
    float red = 0.0f, green = 0.0f, blue = 0.0f;
    for (int i = 0; i < nearbyPhotons.numFound; i++) {
        tempColor.setERGB(nearbyPhotons.photon[i].ergb);
        red += tempColor.getRed();
        green += tempColor.getGreen();
        blue += tempColor.getBlue();
    }
    float max = Math.max(Math.max(red, green), blue);
    double cutoff1;
    if (includeVolume)
        cutoff1 = Math.pow(max * lightScale / ((4.0 / 3.0) * Math.PI * 0.1), 1.0 / 3.0);
    else
        cutoff1 = Math.sqrt(max * lightScale / (Math.PI * 0.1));
    double volume = (bounds.maxx - bounds.minx) * (bounds.maxy - bounds.miny) * (bounds.maxz - bounds.minz);
    double cutoff2 = Math.pow(0.5 * volume * nearbyPhotons.photon.length / photon.length, 1.0 / 3.0);
    cutoffDist2 = (float) (cutoff1 < cutoff2 ? cutoff1 * cutoff1 : cutoff2 * cutoff2);
}
