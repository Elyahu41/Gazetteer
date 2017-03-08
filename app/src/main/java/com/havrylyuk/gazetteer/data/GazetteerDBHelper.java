/*
 * Copyright (c)  2017. Igor Gavriluyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.havrylyuk.gazetteer.data;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.havrylyuk.gazetteer.service.GazetteerService;

import static com.havrylyuk.gazetteer.data.GazetteerContract.CountriesEntry;

/**
 *
 * Created by Igor Havrylyuk on 08.03.2017.
 */

public class GazetteerDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gazetteer.db";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    private Context mContext;

    private final String SQL_CREATE_COUNTRIES_TABLE = "CREATE TABLE " +
            CountriesEntry.TABLE_NAME + " (" +
            CountriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CountriesEntry.COUNTRY_GEONAME_ID + " INTEGER NOT NULL , " +
            CountriesEntry.COUNTRY_CONTINENT_NAME + " TEXT NOT NULL DEFAULT '', " +
            CountriesEntry.COUNTRY_COUNTRY_CODE + " TEXT NOT NULL DEFAULT '', " +
            CountriesEntry.COUNTRY_COUNTRY_NAME + " TEXT NOT NULL DEFAULT '', " +
            CountriesEntry.COUNTRY_CAPITAL + " TEXT NOT NULL DEFAULT '', " +
            CountriesEntry.COUNTRY_AREA + " REAL NOT NULL DEFAULT 0, " +
            CountriesEntry.COUNTRY_POPULATION + " INTEGER NOT NULL DEFAULT 0, " +
            CountriesEntry.COUNTRY_CURRENCY_CODE + " TEXT NOT NULL DEFAULT '', " +
            CountriesEntry.COUNTRY_LANGUAGES + " TEXT NOT NULL DEFAULT '', " +
            CountriesEntry.COUNTRY_SOUTH + " REAL NOT NULL DEFAULT 0, " +
            CountriesEntry.COUNTRY_WEST + " REAL NOT NULL DEFAULT 0, " +
            CountriesEntry.COUNTRY_EAST + " REAL NOT NULL DEFAULT 0, " +
            CountriesEntry.COUNTRY_NORTH + " REAL NOT NULL DEFAULT 0, " +
            CountriesEntry.COUNTRY_FAVORITE + " INTEGER NOT NULL DEFAULT 0, " +
            " UNIQUE (" + CountriesEntry.COUNTRY_GEONAME_ID + ") ON CONFLICT REPLACE);";


    public GazetteerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_COUNTRIES_TABLE);
        Intent intent = new Intent(mContext, GazetteerService.class);
        mContext.startService(intent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
    }
}
