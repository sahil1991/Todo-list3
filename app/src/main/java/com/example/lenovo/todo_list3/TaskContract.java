package com.example.lenovo.todo_list3;

import android.provider.BaseColumns;

/**
 * Created by lenovo on 27-10-2016.
 */
public class TaskContract {
    public static final String DB_NAME = "com.aziflaj.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
    }
}