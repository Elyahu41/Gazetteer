package com.havrylyuk.gazetteer.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.havrylyuk.gazetteer.BuildConfig;
import com.havrylyuk.gazetteer.data.GazetteerContract.CountriesEntry;
import com.havrylyuk.gazetteer.model.Countries;
import com.havrylyuk.gazetteer.model.Country;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Igor Havrylyuk on 08.03.2017.
 */

public class GazetteerService extends IntentService {

    private static final String LOG_TAG = GazetteerService.class.getSimpleName();

    public GazetteerService() {
        super("GazetteerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            try {
                long inserted = 0;
                if (BuildConfig.DEBUG) Log.v( LOG_TAG, "Begin load countries" );
                ApiService service = ApiClient.getClient().create(ApiService.class);
                Call<Countries> responseCall =
                        service.getCountries(Locale.getDefault().getLanguage(), BuildConfig.GEONAME_API_KEY, "FULL");
                Countries countries = responseCall.execute().body();
                if (countries.getCountries() != null) {
                    ContentValues[] contentValues = new ContentValues[countries.getCountries().size()];
                    for (int i = 0; i < countries.getCountries().size(); i++) {
                        contentValues[i] = countryToContentValues(countries.getCountries().get(i));
                    }
                    inserted = getContentResolver().bulkInsert(CountriesEntry.CONTENT_URI, contentValues);
                }
                if (BuildConfig.DEBUG) Log.v(LOG_TAG, "End load countries insert=" + inserted);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
    }

    private ContentValues countryToContentValues(Country country) {
        ContentValues cv = new ContentValues();
        cv.put(CountriesEntry.COUNTRY_AREA,  country.getAreaInSqKm());
        cv.put(CountriesEntry.COUNTRY_CAPITAL, country.getCapital());
        cv.put(CountriesEntry.COUNTRY_CONTINENT_NAME, country.getContinentName());
        cv.put(CountriesEntry.COUNTRY_COUNTRY_CODE, country.getCountryCode());
        cv.put(CountriesEntry.COUNTRY_COUNTRY_NAME, country.getCountryName());
        cv.put(CountriesEntry.COUNTRY_WEST, country.getWest());
        cv.put(CountriesEntry.COUNTRY_EAST, country.getEast());
        cv.put(CountriesEntry.COUNTRY_SOUTH, country.getSouth());
        cv.put(CountriesEntry.COUNTRY_NORTH, country.getNorth());
        cv.put(CountriesEntry.COUNTRY_CURRENCY_CODE, country.getCurrencyCode());
        cv.put(CountriesEntry.COUNTRY_GEONAME_ID, country.getGeonameId());
        return cv;
    }

}
