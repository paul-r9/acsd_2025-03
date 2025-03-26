package com.rocketninesolutions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SpaceportDepartureBoard {

    private List<LaunchInfo> launchList;

    public SpaceportDepartureBoard(ISpacelineLaunchInfoProvider provider) {
        launchList = provider.getCurrentLaunches();
        sortLaunches();
    }

    public List<LaunchInfo> getLaunchList() {
        return Collections.unmodifiableList(launchList);
    }

    private void sortLaunches() {
        Collections.sort(launchList, new Comparator<LaunchInfo>(){
            public int compare(LaunchInfo o1, LaunchInfo o2){
                return o1.getDestination().compareTo(o2.getDestination());
            }
        });
//        throw new UnsupportedOperationException("TODO - implement the sorting logic!");
    }

    private void addNewLaunch(LaunchInfo launchInfo) {
        launchList.add(launchInfo);
    }

    private void handleDelayedLaunch(LaunchInfo launchInfo) {
        throw new UnsupportedOperationException("delayed launches are not supported");
    }

    private void handleScrubbedLaunch(LaunchInfo launchInfo) {
        throw new UnsupportedOperationException("scrubbed launches are not supported");
    }

    private void handleLaunched(LaunchInfo launchInfo) {
        throw new UnsupportedOperationException("launched launches are not supported");
    }

    protected void removeInactiveLaunches() {
        LocalDateTime now = LocalDateTime.now();

        Iterator<LaunchInfo> iter = launchList.iterator();
        while (iter.hasNext()) {
            LaunchInfo l = iter.next();
            if (LaunchInfo.LaunchStatus.Launched == l.getStatus()) {
                if (l.getTime().plusMinutes(5).compareTo(now) <= 0) {
                    iter.remove();
                }
            } else if (LaunchInfo.LaunchStatus.Scrubbed == l.getStatus()) {
                if (l.getTime().plusMinutes(10).compareTo(now) <= 0) {
                    iter.remove();
                }
            }
        }
    }

    public void onLaunchInfoChanged(LaunchInfo launchInfo) {
        LaunchInfo.LaunchStatus status = launchInfo.getStatus();

        if (status == LaunchInfo.LaunchStatus.Delayed) {
            handleDelayedLaunch(launchInfo);
        } else if (status == LaunchInfo.LaunchStatus.Scrubbed) {
            handleScrubbedLaunch(launchInfo);
        } else if (status == LaunchInfo.LaunchStatus.Launched) {
            handleLaunched(launchInfo);
        } else {
            addNewLaunch(launchInfo);
        }
    }
}
