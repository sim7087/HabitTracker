package com.newsapp071997.habittracker.habitdata;

import android.provider.BaseColumns;

public class HabitContract {

    private HabitContract() {
    }

    public static final class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "habit";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_WEEK_DAY = "week_day";
        public static final String COLUMN_HABIT_DESC = "habitDescription";
        public static final int WEEK_DAY_SUNDAY = 0;
        public static final int WEEK_DAY_MONDAY = 1;
        public static final int WEEK_DAY_TUESDAY = 2;
        public static final int WEEK_DAY_WEDNESDAY = 3;
        public static final int WEEK_DAY_THURSDAY = 4;
        public static final int WEEK_DAY_FRIDAY = 5;
        public static final int WEEK_DAY_SATURDAY = 6;
    }
}
