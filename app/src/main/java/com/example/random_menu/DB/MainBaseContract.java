package com.example.random_menu.DB;

import android.provider.BaseColumns;

public final class MainBaseContract {
    private MainBaseContract(){}

    public static class Elements implements BaseColumns{
        public static final String TABLE_NAME = "Elements";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_COMMENT = "Comment";
        public static final String COLUMN_NAME_PRIORITY = "Priority";
    }

    public static class Groups implements BaseColumns{
        public static final String TABLE_NAME = "Groups";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_COMMENT = "Comment";
        public static final String COLUMN_NAME_PRIORITY = "Priority";
        public static final String COLUMN_NAME_COUNT_ELEMS = "CountElems";
   }

    public static class Components implements BaseColumns{
        public static final String TABLE_NAME = "Components";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_ELEMENT = "Element";
        public static final String COLUMN_NAME_COMMENT = "Comment";
        public static final String COLUMN_NAME_QUANTITY = "Quantity";
    }

    public static class ElemGroup implements BaseColumns{
        public static final String TABLE_NAME = "ElemGroup";
        public static final String COLUMN_NAME_ELEMENT = "Element";
        public static final String COLUMN_NAME_GROUP = "Group_";
    }

}
