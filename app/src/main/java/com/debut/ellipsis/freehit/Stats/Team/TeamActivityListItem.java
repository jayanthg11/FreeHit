package com.debut.ellipsis.freehit.Stats.Team;


public class TeamActivityListItem {
    public class StatsItem {

        private int mStatsIcon;

        private int mStatsName;


        public StatsItem(int StatsIcon, int StatsName) {
            mStatsIcon = StatsIcon;
            mStatsName = StatsName;

        }

        public int getmStatsIcon() {
            return mStatsIcon;
        }

        public int getmStatsName() {
            return mStatsName;
        }

    }

}
