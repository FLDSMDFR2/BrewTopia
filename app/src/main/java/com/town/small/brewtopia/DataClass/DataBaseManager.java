package com.town.small.brewtopia.DataClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * Created by Andrew on 3/1/2015.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 24;//increment to have DB changes take effect
    private static final String DATABASE_NAME = "BeerTopiaDB";

    // Log cat tag
    private static final String LOG = "DataBaseManager";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_BREWS = "Brews";
    private static final String TABLE_BREWS_STYLES = "BrewsStyles";
    private static final String TABLE_BREWS_NOTES = "BrewNotes";
    private static final String TABLE_BOIL_ADDITIONS = "BoilAdditions";
    private static final String TABLE_BREWS_SCHEDULED = "BrewsScheduled";
    private static final String TABLE_BREWS_CALCULATIONS = "Calculations";
    private static final String TABLE_APP_SETTINGS = "Settings";

    // Common column names across Mulit tables
    private static final String ROW_ID = "rowid";
    private static final String CREATED_ON = "CreatedOn";
    private static final String USER_ID = "UserId"; // primary keys
    private static final String USER_NAME = "UserName"; // primary keys
    private static final String BREW_NAME = "BrewName"; // primary keys
    private static final String NOTE = "Note";
    private static final String ORIGINAL_GRAVITY = "OriginalGravity";
    private static final String FINAL_GRAVITY = "FinalGravity";
    private static final String ABV = "ABV";

    // USERS column names
    private static final String PASSWORD = "Password";

    // BREWS column names
    private static final String BOIL_TIME = "BoilTime";
    private static final String PRIMARY = "PrimaryFermentation";
    private static final String SECONDARY = "Secondary";
    private static final String BOTTLE = "Bottle";
    private static final String DESCRIPTION = "Description";
    private static final String STYLE = "Style";
    private static final String FAVORITE = "Favorite";
    private static final String SCHEDULED = "Scheduled";
    private static final String ON_TAP = "OnTap";
    private static final String IBU = "IBU";
    private static final String METHOD = "Method";

    // TABLE_BREWS_STYLES column names
    private static final String STYLE_NAME = "StyleName";
    private static final String STYLE_COLOR = "StyleColor";

    // BOIL_ADDITIONS column names
    private static final String ADDITION_NAME = "AdditionName";
    private static final String ADDITION_TIME = "AdditionTime";
    private static final String ADDITION_QTY = "AdditionQty";
    private static final String ADDITION_UOFM = "AdditionUofM";

    // BREWS_SCHEDULED column names
    private static final String SECONDARY_ALERT_DATE = "SecondaryAlertDate";
    private static final String BOTTLE_ALERT_DATE = "BottleAlertDate";
    private static final String END_BREW_DATE = "EndBrewDate";
    private static final String ACTIVE = "Active";
    private static final String SECONDARY_ALERT_CALENDAR_ID = "SecondaryAlertCalendarId";
    private static final String BOTTLE_ALERT_CALENDAR_ID = "BottleAlertCalendarId";
    private static final String END_BREW_CALENDAR_ID = "EndBrewCalendarId";


    // TABLE_BREWS_CALCULATIONS column names
    private static final String CALCULATION_ABV = "CalculationAbv";
    private static final String CALCULATION_NAME = "CalculationName";

    // TABLE_APP_SETTINGS column names
    private static final String SETTING_NAME = "SettingName";
    private static final String SETTING_VALUE = "SettingValue";

    // Table Create Statements
    //CREATE_TABLE_USERS
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + USER_NAME + " TEXT PRIMARY KEY,"
            + PASSWORD + " TEXT," + CREATED_ON + " DATETIME )";

    //CREATE_TABLE_BREWS
    private static final String CREATE_TABLE_BREWS = "CREATE TABLE "
            + TABLE_BREWS + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + BOIL_TIME + " INTEGER," + PRIMARY + " INTEGER," + SECONDARY + " INTEGER," + BOTTLE + " INTEGER,"
            + DESCRIPTION + " TEXT," + STYLE + " TEXT," + CREATED_ON + " DATETIME," + ORIGINAL_GRAVITY + " REAL," + FINAL_GRAVITY + " REAL," + ABV + " REAL," + FAVORITE + " INTEGER,"
            + SCHEDULED + " INTEGER," + ON_TAP + " INTEGER,"+ IBU + " REAL,"+ METHOD + " TEXT, PRIMARY KEY ("+ BREW_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BREWS_STYLES
    private static final String CREATE_TABLE_BREWS_STYLES = "CREATE TABLE "
            + TABLE_BREWS_STYLES + "(" + STYLE_NAME + " TEXT," + USER_NAME + " TEXT," + STYLE_COLOR + " TEXT, PRIMARY KEY ("+ STYLE_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BOIL_ADDITIONS
    private static final String CREATE_TABLE_BOIL_ADDITIONS = "CREATE TABLE "
            + TABLE_BOIL_ADDITIONS + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + ADDITION_NAME + " TEXT," + ADDITION_TIME + " INTEGER,"
            +  ADDITION_QTY + " INTEGER," +  ADDITION_UOFM + " TEXT, PRIMARY KEY ("+ BREW_NAME +", "+ ADDITION_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BREWS_SCHEDULED
    private static final String CREATE_TABLE_BREWS_SCHEDULED = "CREATE TABLE "
            + TABLE_BREWS_SCHEDULED + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + CREATED_ON + " DATETIME," + SECONDARY_ALERT_DATE + " DATETIME," + BOTTLE_ALERT_DATE + " DATETIME,"
            + END_BREW_DATE + " DATETIME," +  ACTIVE + " INTEGER," +  NOTE + " TEXT," +  STYLE_COLOR + " TEXT," + ORIGINAL_GRAVITY + " REAL," + FINAL_GRAVITY + " REAL,"
            + ABV + " REAL," + SECONDARY_ALERT_CALENDAR_ID + " INTEGER,"+ BOTTLE_ALERT_CALENDAR_ID + " INTEGER,"+ END_BREW_CALENDAR_ID + " INTEGER )";

    //CREATE_TABLE_BREWS_CALCULATIONS
    private static final String CREATE_TABLE_BREWS_CALCULATIONS = "CREATE TABLE "
            + TABLE_BREWS_CALCULATIONS + "(" + CALCULATION_ABV + " TEXT," + CALCULATION_NAME + " TEXT, PRIMARY KEY ("+ CALCULATION_ABV +", "+ CALCULATION_NAME +" ) )";

    //CREATE_TABLE_BREWS_NOTES
    private static final String CREATE_TABLE_BREWS_NOTES = "CREATE TABLE "
            + TABLE_BREWS_NOTES + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + NOTE + " TEXT," + CREATED_ON + " DATETIME )";

    //CREATE_TABLE_APP_SETTINGS
    private static final String CREATE_TABLE_APP_SETTINGS = "CREATE TABLE "
            + TABLE_APP_SETTINGS + "(" + USER_ID + " INTEGER," + SETTING_NAME + " TEXT," + SETTING_VALUE + " TEXT )";


    //Singleton
    private static DataBaseManager mInstance = null;

    public static DataBaseManager getInstance(Context aContext) {
        if (mInstance == null) {
            mInstance = new DataBaseManager(aContext.getApplicationContext());
        }
        return mInstance;
    }
    // constructor
    private DataBaseManager(Context aContext) {
        super(aContext,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase aSQLiteDatabase) {
        // creating required tables
        Log.e(LOG, "Entering: onCreate");
        aSQLiteDatabase.execSQL(CREATE_TABLE_USERS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_STYLES);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_SCHEDULED);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_CALCULATIONS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_NOTES);
        aSQLiteDatabase.execSQL(CREATE_TABLE_APP_SETTINGS);

        //Pre Load Data
        PreLoadAdminUser(aSQLiteDatabase);
        PreLoadBrewStyles(aSQLiteDatabase);
        PreLoadCalculations(aSQLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aSQLiteDatabase, int aOldVersion, int aNewVersion) {
        // on upgrade drop older tables
        Log.e(LOG, "Entering: onUpgrade OldVersion["+aOldVersion+"] NewVersion["+aNewVersion+"]");
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_STYLES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_SCHEDULED);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_CALCULATIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_NOTES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_SETTINGS);

        // create new tables
        onCreate(aSQLiteDatabase);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private void PreLoadAdminUser(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        ContentValues values = new ContentValues();
        values.put(USER_NAME, "ADMIN");
        values.put(PASSWORD, "");
        values.put(CREATED_ON, getDateTime());

        db.insert(TABLE_USERS,null,values);

    }
    private void PreLoadBrewStyles(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        Set mapSet = (Set) APPUTILS.StyleMap.entrySet();
        //Create iterator on Set
        Iterator mapIterator = mapSet.iterator();

        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            // getKey Method of HashMap access a key of map
            String keyValue = (String) mapEntry.getKey();
            //getValue method returns corresponding key's value
            String value = (String) mapEntry.getValue();

            ContentValues values = new ContentValues();
            values.put(STYLE_NAME, keyValue);
            values.put(USER_NAME, "ADMIN");
            values.put(STYLE_COLOR, value);

            db.insert(TABLE_BREWS_STYLES,null,values);

        }
    }
    private void PreLoadCalculations(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        ContentValues values = new ContentValues();
        values.put(CALCULATION_ABV, "ABV");
        values.put(CALCULATION_NAME, "Alcohol by volume");
        db.insert(TABLE_BREWS_CALCULATIONS,null,values);

        values = new ContentValues();
        values.put(CALCULATION_ABV, "BRIX");
        values.put(CALCULATION_NAME, "Brix Calculations");
        db.insert(TABLE_BREWS_CALCULATIONS,null,values);

        values = new ContentValues();
        values.put(CALCULATION_ABV, "SG");
        values.put(CALCULATION_NAME, "Specific Gravity");
        db.insert(TABLE_BREWS_CALCULATIONS,null,values);

    }

    //******************************User Table function*********************************

    /*
    * Creating a User
    */
    public boolean CreateAUser(UserSchema aUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(DoesUserExist(aUser.getUserName()))
            return false;

        ContentValues values = new ContentValues();
        values.put(USER_NAME, aUser.getUserName());
        values.put(PASSWORD, aUser.getPassword());
        values.put(CREATED_ON, getDateTime());

        // insert row
        Log.e(LOG, "Insert: User["+aUser.getUserName()+"] Password["+aUser.getPassword()+"]");
        return db.insert(TABLE_USERS,null,values) > 0;
    }

    /*
    * get single User
    */
    public UserSchema getUser(String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT "+ROW_ID+", * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserSchema user = new UserSchema();
        user.setUserId(c.getInt(c.getColumnIndex(ROW_ID)));
        user.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
        user.setPassword((c.getString(c.getColumnIndex(PASSWORD))));
        user.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

        c.close();
        return user;
    }

    /*
    * Return true if user login exists
    */
    public boolean DoesUserLoginExist(String aUserName, String aPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean retVal = false;

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName + "' AND "
                + PASSWORD + " = '" + aPassword + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // record found return true
        if (c.getCount() > 0)
            retVal = true;

        c.close();
        return retVal;
    }

    /*
* Return true if user exists
*/
    public boolean DoesUserExist(String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean retVal = false;

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // record found return true
        if (c.getCount() > 0)
            retVal = true;

        c.close();
        return retVal;
    }

    //******************************Brews Table function*********************************
    /*
    * Creating a Brew
    */
    public int CreateABrew(BrewSchema aBrew) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: Brew["+aBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrew.getBrewName());
        values.put(USER_NAME, aBrew.getUserName());
        values.put(BOIL_TIME, aBrew.getBoilTime());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(ORIGINAL_GRAVITY, aBrew.getTargetOG());
        values.put(FINAL_GRAVITY, aBrew.getTargetFG());
        values.put(ABV, aBrew.getTargetABV());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(STYLE, aBrew.getStyle());
        values.put(FAVORITE, aBrew.getFavorite());
        values.put(SCHEDULED, aBrew.getScheduled());
        values.put(ON_TAP, aBrew.getOnTap());
        values.put(IBU, aBrew.getIBU());
        values.put(METHOD, aBrew.getMethod());
        values.put(CREATED_ON, getDateTime());

        //Add brew
        if(!(db.insert(TABLE_BREWS,null,values) > 0) )
            return 0; // 0 nothing created
        //Add Boil Additions
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return 1;// 1 brews created
        if(!(addAllBrewNotes(aBrew.getBrewNoteSchemaList())))
            return 2;// 2 additons

        return 3; // All create created
    }


    /*
* get single Brew
*/
    public BrewSchema getBrew(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT "+ ROW_ID+",* FROM " + TABLE_BREWS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        BrewSchema brew = new BrewSchema(aBrewName);
        if (c != null)
        {
            c.moveToFirst();

            brew.setBrewId(c.getInt(c.getColumnIndex(ROW_ID)));
            brew.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
            brew.setBoilTime(c.getInt(c.getColumnIndex(BOIL_TIME)));
            brew.setPrimary(c.getInt(c.getColumnIndex(PRIMARY)));
            brew.setSecondary((c.getInt(c.getColumnIndex(SECONDARY))));
            brew.setBottle((c.getInt(c.getColumnIndex(BOTTLE))));
            brew.setTargetOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
            brew.setTargetFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
            brew.setTargetABV((c.getDouble(c.getColumnIndex(ABV))));
            brew.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
            brew.setStyle((c.getString(c.getColumnIndex(STYLE))));
            brew.setFavorite((c.getInt(c.getColumnIndex(FAVORITE))));
            brew.setScheduled((c.getInt(c.getColumnIndex(SCHEDULED))));
            brew.setOnTap((c.getInt(c.getColumnIndex(ON_TAP))));
            brew.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));
            brew.setIBU((c.getDouble(c.getColumnIndex(IBU))));
            brew.setMethod(c.getString(c.getColumnIndex(METHOD)));

            //set boil additions
            brew.setBoilAdditionlist(get_all_boil_additions_by_brew_name(aBrewName, aUserName));

            //set style
            brew.setStyleSchema(getBrewsStylesByName(brew.getStyle()));

            //set notes
            brew.setBrewNoteSchemaList(getAllBrewNotes(aBrewName, aUserName));
        }


        c.close();
        return brew;
    }

    /*
* getting all Brews for User
*/
    public List<BrewSchema> getAllBrews(String aUserName) {
        List<BrewSchema> brewList = new ArrayList<BrewSchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BREWS + " WHERE "
                + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewSchema brewSchema = new BrewSchema();
                brewSchema.setBrewId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewSchema.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                brewSchema.setUserName(c.getString(c.getColumnIndex(USER_NAME)));

                brewSchema = getBrew(brewSchema.getBrewName(),brewSchema.getUserName());

                // adding to brewList
                brewList.add(brewSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewList;
    }

    /*
 * Updating a Brew
 */
    public Boolean updateABrew(BrewSchema aBrew) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateABrew Name["+aBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrew.getBrewName());
        values.put(USER_NAME, aBrew.getUserName());
        values.put(BOIL_TIME, aBrew.getBoilTime());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(ORIGINAL_GRAVITY, aBrew.getTargetOG());
        values.put(FINAL_GRAVITY, aBrew.getTargetFG());
        values.put(ABV, aBrew.getTargetABV());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(STYLE, aBrew.getStyle());
        values.put(FAVORITE, aBrew.getFavorite());
        values.put(SCHEDULED, aBrew.getScheduled());
        values.put(ON_TAP, aBrew.getOnTap());
        values.put(IBU, aBrew.getIBU());
        values.put(METHOD, aBrew.getMethod());
        //values.put(CREATED_ON, getDateTime());

        // updating row
        int retVal = db.update(TABLE_BREWS, values, ROW_ID + " = ?",
                new String[] { Integer.toString(aBrew.getBrewId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;
        //Update Boil Additions
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return false;
        //Update Brew notes
        if(!(addAllBrewNotes(aBrew.getBrewNoteSchemaList())))
            return false;

        return true;
    }

    /*
* Delete a Brew
*/
    public void DeleteBrew(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "DeleteBrew User["+aUserName+"] Name["+aBrewName+"]");

        db.delete(TABLE_BREWS, BREW_NAME + " = ? AND " + USER_NAME + " = ?",
                new String[]{aBrewName, aUserName});

        //Delete all additions for this brew name
        delete_all_boil_additions_by_brew_name(aBrewName,aUserName);

        //Delete all brew notes
        deleteAllBrewNotes(aBrewName,aUserName);
    }

    //******************************Brews Style Table function*********************************
    /*
    * Creating a Brew styles
    */
    public Boolean CreateABrewStyle(BrewStyleSchema aBrewStyle) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: Brew Style["+aBrewStyle.getBrewStyleName()+"]");

        ContentValues values = new ContentValues();
        values.put(STYLE_NAME, aBrewStyle.getBrewStyleName());
        values.put(USER_NAME, aBrewStyle.getUserName());
        values.put(STYLE_COLOR, aBrewStyle.getBrewStyleColor());

        //Add brew style
        if(!(db.insert(TABLE_BREWS_STYLES,null,values) > 0) )
            return false;

        return true;
    }

    /*
* getting all Brews styles
*/
    public List<BrewStyleSchema> getAllBrewsStyles() {
        List<BrewStyleSchema> brewStyleList = new ArrayList<BrewStyleSchema>();
        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_STYLES ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrewsStyles Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewStyleSchema brewStyleSchema = new BrewStyleSchema();
                //brewSchema.setId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewStyleSchema.setBrewStyleName(c.getString(c.getColumnIndex(STYLE_NAME)));
                brewStyleSchema.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
                brewStyleSchema.setBrewStyleColor(c.getString(c.getColumnIndex(STYLE_COLOR)));

                // adding to brewStyleList
                brewStyleList.add(brewStyleSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewStyleList;
    }

    /*
* getting a Brews styles
*/
    public BrewStyleSchema getBrewsStylesByName(String aStyleName) {
        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_STYLES + " WHERE "
                + STYLE_NAME + " = '" + aStyleName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrewsStyles Count["+c.getCount()+"]");
        BrewStyleSchema brewStyleSchema = new BrewStyleSchema();
        if (c.getCount() > 0 ) {
            c.moveToFirst();
                //brewSchema.setId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewStyleSchema.setBrewStyleName(c.getString(c.getColumnIndex(STYLE_NAME)));
                brewStyleSchema.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
                brewStyleSchema.setBrewStyleColor(c.getString(c.getColumnIndex(STYLE_COLOR)));
        }

        c.close();
        return brewStyleSchema;
    }

    /*
* getting all Brews styles count
*/
    public int getBrewStyleCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_STYLES ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }
    //************************************Brew Notes Table functions***************
        /*
* add Brew Note
*/
    public boolean addBrewNote(BrewNoteSchema aBrewNote) {
        Log.e(LOG, "Insert: addBrewNote["+aBrewNote.getBrewName()+", "+ aBrewNote.getUserName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrewNote.getBrewName());
        values.put(USER_NAME, aBrewNote.getUserName());
        values.put(CREATED_ON, getDateTime());
        values.put(NOTE, aBrewNote.getBrewNote());


        // insert row
        return db.insert(TABLE_BREWS_NOTES,null,values) > 0;
    }

    /*
* add All brew notes
*/
    public boolean addAllBrewNotes(List <BrewNoteSchema> aBrewNoteList) {
        Log.e(LOG, "Insert: addAllBrewNotes");

        if(aBrewNoteList.size() < 1)
            return true;

        //TODO: TRY TO FIND A WAY TO NOT DELETE BUT LOOK FOR UPDATE/ADD ONLY
        //Delete all additions for this brew name
        deleteAllBrewNotes(aBrewNoteList.get(0).getBrewName(),aBrewNoteList.get(0).getUserName());

        // for each brew note try to add it to the DB
        for(Iterator<BrewNoteSchema> i = aBrewNoteList.iterator(); i.hasNext(); )
        {
            BrewNoteSchema brewNoteSchema = i.next();
            if(!addBrewNote(brewNoteSchema))
                return false;
        }

        return true;
    }

    /*
* getting All brew notes by brew name and user id
*/
    public List<BrewNoteSchema> getAllBrewNotes(String aBrewName, String aUserName) {
        List<BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();
        String selectQuery = "SELECT "+ ROW_ID +",* FROM " + TABLE_BREWS_NOTES + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrewNotes Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewNoteSchema brewNoteSchema = new BrewNoteSchema();
                brewNoteSchema.setNoteId(c.getInt(c.getColumnIndex(ROW_ID)));
                brewNoteSchema.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                brewNoteSchema.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
                brewNoteSchema.setBrewNote(c.getString(c.getColumnIndex(NOTE)));
                brewNoteSchema.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

                // adding to boilList
                brewNoteSchemaList.add(brewNoteSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewNoteSchemaList;
    }

    /*
* Update Brew notes
*/
    public boolean updateBrewNotes(BrewNoteSchema aBrewNoteSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateBrewNotes Name["+aBrewNoteSchema.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrewNoteSchema.getBrewName());
        values.put(USER_NAME, aBrewNoteSchema.getUserName());
        values.put(CREATED_ON, getDateTime());
        values.put(NOTE, aBrewNoteSchema.getBrewNote());

        // updating row
        int retVal = db.update(TABLE_BREWS_NOTES, values, ROW_ID + " = ?",
                new String[] { Integer.toString(aBrewNoteSchema.getNoteId()) });

        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew notes by Brew Name
*/
    public void deleteAllBrewNotes(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteAllBrewNotes Name["+aBrewName+"]");

        db.delete(TABLE_BREWS_NOTES, BREW_NAME + " = ? AND "+ USER_NAME +  " = ? ",
                new String[] { aBrewName, aUserName});
    }

    /*
* delete brew note by id
*/
    public void deleteBrewNoteById(int brewNoteId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewNoteById Note Id["+Integer.toString(brewNoteId) +"]");

        db.delete(TABLE_BREWS_NOTES, ROW_ID + " = ?",
                new String[] { Integer.toString(brewNoteId) });
    }


    //********************Boil Additions Table function*************
        /*
* add Boil additions
*/
    public boolean add_boil_additions(BoilAdditionsSchema aBoilAddition) {
        Log.e(LOG, "Insert: add_boil_additions["+aBoilAddition.getBrewName()+", "+ aBoilAddition.getAdditionName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBoilAddition.getBrewName());
        values.put(USER_NAME, aBoilAddition.getUserName());
        values.put(ADDITION_NAME, aBoilAddition.getAdditionName());
        values.put(ADDITION_TIME, aBoilAddition.getAdditionTime());
        values.put(ADDITION_QTY, aBoilAddition.getAdditionQty());
        values.put(ADDITION_UOFM, aBoilAddition.getUOfM());

        // insert row
        return db.insert(TABLE_BOIL_ADDITIONS,null,values) > 0;
    }

    /*
* add Boil additions
*/
    public boolean add_all_boil_additions(List <BoilAdditionsSchema> aBoilAdditionList) {
        Log.e(LOG, "Insert: add_all_boil_additions");

        if(aBoilAdditionList.size() < 1)
            return  true;

      //TODO: TRY TO FIND A WAY TO NOT DELETE BUT LOOK FOR UPDATE/ADD ONLY
      //Delete all additions for this brew name
      delete_all_boil_additions_by_brew_name(aBoilAdditionList.get(0).getBrewName(),aBoilAdditionList.get(0).getUserName());

      // for each boil addition try to add it to the DB
      for(Iterator<BoilAdditionsSchema> i = aBoilAdditionList.iterator(); i.hasNext(); )
      {
          BoilAdditionsSchema baSchema = i.next();
          if(!add_boil_additions(baSchema))
              return false;
      }

       return true;
    }

    /*
* Update Boil additions
*/
    public boolean update_boil_addition(BoilAdditionsSchema aBoilAddition) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "update_boil_addition Name["+aBoilAddition.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBoilAddition.getBrewName());
        values.put(USER_NAME, aBoilAddition.getUserName());
        values.put(ADDITION_NAME, aBoilAddition.getAdditionName());
        values.put(ADDITION_TIME, aBoilAddition.getAdditionTime());
        values.put(ADDITION_QTY, aBoilAddition.getAdditionQty());
        values.put(ADDITION_UOFM, aBoilAddition.getUOfM());

        // updating row
        int retVal = db.update(TABLE_BOIL_ADDITIONS, values, ROW_ID + " = ?",
                new String[] { Integer.toString(aBoilAddition.getAdditionId()) });

        if(!(retVal > 0) )
            return false;

        return true;
    }

        /*
* getting all Boil additions for brew name
*/
    public List<BoilAdditionsSchema> get_all_boil_additions_by_brew_name(String aBrewName, String aUserName) {
        List<BoilAdditionsSchema> boilList = new ArrayList<BoilAdditionsSchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BOIL_ADDITIONS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "get_all_boil_additions_by_brew_name Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BoilAdditionsSchema baSchema = new BoilAdditionsSchema();
                baSchema.setAdditionId(c.getInt(c.getColumnIndex(ROW_ID)));
                baSchema.setAdditionName(c.getString(c.getColumnIndex(ADDITION_NAME)));
                baSchema.setAdditionTime(c.getInt(c.getColumnIndex(ADDITION_TIME)));
                baSchema.setAdditionQty(c.getInt(c.getColumnIndex(ADDITION_QTY)));
                baSchema.setUOfM(c.getString(c.getColumnIndex(ADDITION_UOFM)));

                // adding to boilList
                boilList.add(baSchema);
            } while (c.moveToNext());
        }

        c.close();
        return boilList;
    }

    /*
* getting Boil additions for brew name and addition name
*/
    public BoilAdditionsSchema get_boil_additions_by_brew_name_addition_name(String aBrewName, String aAdditionName, String aUserName) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BOIL_ADDITIONS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'"
                + "AND " + ADDITION_NAME + " = '" + aAdditionName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        BoilAdditionsSchema baSchema = new BoilAdditionsSchema();
        baSchema.setAdditionName("");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            baSchema.setAdditionId(c.getInt(c.getColumnIndex(ROW_ID)));
            baSchema.setAdditionName(c.getString(c.getColumnIndex(ADDITION_NAME)));
            baSchema.setAdditionTime(c.getInt(c.getColumnIndex(ADDITION_TIME)));
            baSchema.setAdditionQty(c.getInt(c.getColumnIndex(ADDITION_QTY)));
            baSchema.setUOfM(c.getString(c.getColumnIndex(ADDITION_UOFM)));
        }

        c.close();
        return baSchema;
    }

    /*
* Delete All boil additions by Brew Name
*/
    public void delete_all_boil_additions_by_brew_name(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "delete_all_boil_additions_by_brew_name Name["+aBrewName+"]");

        db.delete(TABLE_BOIL_ADDITIONS, BREW_NAME + " = ? AND "+ USER_NAME +  " = ? ",
                new String[] { aBrewName, aUserName});
    }

    /*
* delete boil additions by id
*/
    public void delete_all_boil_additions_by_id(int boilAdditionId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewNoteById Note Id["+Integer.toString(boilAdditionId) +"]");

        db.delete(TABLE_BOIL_ADDITIONS, ROW_ID + " = ?",
                new String[] { Integer.toString(boilAdditionId) });
    }

//******************************Scheduled Table function*********************************
/*
* Create A Scheduled Brew
*/
    public boolean CreateAScheduledBrew(ScheduledBrewSchema aSBrew) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: ScheduledBrew["+aSBrew.getUserName()+", " +aSBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aSBrew.getBrewName());
        values.put(USER_NAME, aSBrew.getUserName());
        values.put(CREATED_ON, getDateTime());//start date
        values.put(SECONDARY_ALERT_DATE, aSBrew.getAlertSecondaryDate());
        values.put(BOTTLE_ALERT_DATE, aSBrew.getAlertBottleDate());
        values.put(END_BREW_DATE, aSBrew.getEndBrewDate());
        values.put(ACTIVE, aSBrew.getActive());
        values.put(NOTE, "");
        values.put(STYLE_COLOR, aSBrew.getColor());
        values.put(ORIGINAL_GRAVITY, aSBrew.getOG());
        values.put(FINAL_GRAVITY, aSBrew.getFG());
        values.put(ABV, aSBrew.getABV());
        values.put(SECONDARY_ALERT_CALENDAR_ID, aSBrew.getAlertSecondaryCalendarId());
        values.put(BOTTLE_ALERT_CALENDAR_ID, aSBrew.getAlertBottleCalendarId());
        values.put(END_BREW_CALENDAR_ID, aSBrew.getEndBrewCalendarId());

        //Add ScheduledBrew
        if(!(db.insert(TABLE_BREWS_SCHEDULED,null,values) > 0) )
            return false;

        return true;
    }
    /*
* Get Active Scheduled Brew by name and date
*/
    public ScheduledBrewSchema getActiveScheduledBrewByNameDate(String aBrewName, String aUserName, String aStartDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + BREW_NAME + " = '" + aBrewName +"' "
                + "AND " + USER_NAME + " = '" + aUserName +"' "
                + "AND " + CREATED_ON + " = '" + aStartDate +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
        if (c != null) {
            c.moveToFirst();
            sBrew.setScheduleId(c.getInt(c.getColumnIndex(ROW_ID)));
            sBrew.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
            sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
            sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
            sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
            sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
            sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
            sBrew.setOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
            sBrew.setFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
            sBrew.setABV((c.getDouble(c.getColumnIndex(ABV))));
            sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
            sBrew.setNotes((c.getString(c.getColumnIndex(NOTE))));
            sBrew.setColor((c.getString(c.getColumnIndex(STYLE_COLOR))));
            sBrew.setAlertSecondaryCalendarId((c.getLong(c.getColumnIndex(SECONDARY_ALERT_CALENDAR_ID))));
            sBrew.setAlertBottleCalendarId((c.getLong(c.getColumnIndex(BOTTLE_ALERT_CALENDAR_ID))));
            sBrew.setEndBrewCalendarId((c.getLong(c.getColumnIndex(END_BREW_CALENDAR_ID))));
        }
        c.close();
        return sBrew;
    }

    /*
* Get Active Scheduled Brew by Id
*/
    public ScheduledBrewSchema getActiveScheduledBrewId(int ScheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + ROW_ID + " = '" + Integer.toString(ScheduleId) +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
        if (c != null) {
            c.moveToFirst();
            sBrew.setScheduleId(c.getInt(c.getColumnIndex(ROW_ID)));
            sBrew.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
            sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
            sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
            sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
            sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
            sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
            sBrew.setOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
            sBrew.setFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
            sBrew.setABV((c.getDouble(c.getColumnIndex(ABV))));
            sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
            sBrew.setNotes((c.getString(c.getColumnIndex(NOTE))));
            sBrew.setColor((c.getString(c.getColumnIndex(STYLE_COLOR))));
            sBrew.setAlertSecondaryCalendarId((c.getLong(c.getColumnIndex(SECONDARY_ALERT_CALENDAR_ID))));
            sBrew.setAlertBottleCalendarId((c.getLong(c.getColumnIndex(BOTTLE_ALERT_CALENDAR_ID))));
            sBrew.setEndBrewCalendarId((c.getLong(c.getColumnIndex(END_BREW_CALENDAR_ID))));

        }
        c.close();
        return sBrew;
    }

    /*
* Get All Active Scheduled Brews
*/
    public List<ScheduledBrewSchema> getAllActiveScheduledBrews(String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ScheduledBrewSchema> sBrewList = new ArrayList<ScheduledBrewSchema>();
        String selectQuery = "SELECT " + ROW_ID + ",* FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + USER_NAME + " = '" + aUserName+"' "
                + "ORDER BY " + CREATED_ON;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllActiveScheduledBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
                sBrew.setScheduleId(c.getInt(c.getColumnIndex(ROW_ID)));
                sBrew.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
                sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
                sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
                sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
                sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
                sBrew.setOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
                sBrew.setFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
                sBrew.setABV((c.getDouble(c.getColumnIndex(ABV))));
                sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
                sBrew.setNotes((c.getString(c.getColumnIndex(NOTE))));
                sBrew.setColor((c.getString(c.getColumnIndex(STYLE_COLOR))));
                sBrew.setAlertSecondaryCalendarId((c.getLong(c.getColumnIndex(SECONDARY_ALERT_CALENDAR_ID))));
                sBrew.setAlertBottleCalendarId((c.getLong(c.getColumnIndex(BOTTLE_ALERT_CALENDAR_ID))));
                sBrew.setEndBrewCalendarId((c.getLong(c.getColumnIndex(END_BREW_CALENDAR_ID))));

                // adding to Scheduled list if still active else set not active
                if(sBrew.getEndBrewDate().compareTo(getDateTime()) >= 0)
                    sBrewList.add(sBrew);
                else
                {
                    setBrewScheduledNotActive(sBrew.getScheduleId());
                    ScheduleNoteWriterHelper(sBrew);
                }

            } while (c.moveToNext());
        }
        c.close();
        return sBrewList;
    }

    /*
* Updating a Brew Scheduled
*/
    public Boolean updateScheduledBrew(ScheduledBrewSchema aSBrew) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateABrew Name["+aSBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aSBrew.getBrewName());
        values.put(USER_NAME, aSBrew.getUserName());
        values.put(CREATED_ON, aSBrew.getStartDate());
        values.put(SECONDARY_ALERT_DATE, aSBrew.getAlertSecondaryDate());
        values.put(BOTTLE_ALERT_DATE, aSBrew.getAlertBottleDate());
        values.put(END_BREW_DATE, aSBrew.getEndBrewDate());
        values.put(ACTIVE, aSBrew.getActive());
        values.put(NOTE, aSBrew.getNotes());
        values.put(STYLE_COLOR, aSBrew.getColor());
        values.put(ORIGINAL_GRAVITY, aSBrew.getOG());
        values.put(FINAL_GRAVITY, aSBrew.getFG());
        values.put(ABV, aSBrew.getABV());
        values.put(SECONDARY_ALERT_CALENDAR_ID, aSBrew.getAlertSecondaryCalendarId());
        values.put(BOTTLE_ALERT_CALENDAR_ID, aSBrew.getAlertBottleCalendarId());
        values.put(END_BREW_CALENDAR_ID, aSBrew.getEndBrewCalendarId());

        // updating row
        int retVal = db.update(TABLE_BREWS_SCHEDULED, values, ROW_ID + " = ?",
                new String[] { Integer.toString(aSBrew.getScheduleId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
    * Set A users Scheduled brew to not active
     */
    public void setBrewScheduledNotActive(int ScheduleId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "setBrewScheduledNotActive Schedule Id["+Integer.toString(ScheduleId) +"]");

        ContentValues values = new ContentValues();
        values.put(ACTIVE, 0);

        // updating row
        db.update(TABLE_BREWS_SCHEDULED, values, ROW_ID + " = ?",
                new String[] { Integer.toString(ScheduleId) });
    }

    /*
* delete A users Scheduled brew
 */
    public void deleteBrewScheduledById(int ScheduleId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewScheduled Schedule Id["+Integer.toString(ScheduleId) +"]");

        db.delete(TABLE_BREWS_SCHEDULED, ROW_ID + " = ?",
                new String[] { Integer.toString(ScheduleId) });
    }

    /*
* delete A users Scheduled brew
*/
    public void deleteBrewScheduled(String aBrewName, String aUserName, String aStartDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewScheduled Brew Name["+aBrewName+"] Started Date["+aStartDate+"]");

        db.delete(TABLE_BREWS_SCHEDULED, BREW_NAME + " = ? AND "+ USER_NAME +  " = ? AND "+ CREATED_ON +  " = ? ",
                new String[] { aBrewName, aUserName, aStartDate});
    }

    //************************************Calculations Table functions***************
            /*
* getting all Calculations
*/
    public List<CalculationsSchema> getAllCalculations() {
        List<CalculationsSchema> calcList = new ArrayList<CalculationsSchema>();
        String selectQuery = "SELECT * FROM " + TABLE_BREWS_CALCULATIONS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllCalculations Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                CalculationsSchema cSchema = new CalculationsSchema();
                cSchema.setCalculationAbv(c.getString(c.getColumnIndex(CALCULATION_ABV)));
                cSchema.setCalculationName(c.getString(c.getColumnIndex(CALCULATION_NAME)));

                // adding to boilList
                calcList.add(cSchema);
            } while (c.moveToNext());
        }

        c.close();
        return calcList;
    }
    //************************************App settings Table functions***************
                /*
    * add App setting
    */
    public boolean addAppSetting(AppSettingsSchema aAppSettingsSchema) {
        Log.e(LOG, "Insert: addAppSetting["+aAppSettingsSchema.getUserId()+", "+ aAppSettingsSchema.getSettingName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aAppSettingsSchema.getUserId());
        values.put(SETTING_NAME, aAppSettingsSchema.getSettingName());
        values.put(SETTING_VALUE, aAppSettingsSchema.getSettingValue());


        // insert row
        return db.insert(TABLE_APP_SETTINGS,null,values) > 0;
    }
    /*
    * add All App settings
    */
    public boolean addAllAppSettings(List <AppSettingsSchema> aAppSettingsSchema) {
        Log.e(LOG, "Insert: addAllBrewNotes");

        if(aAppSettingsSchema.size() < 1)
            return true;

        // for each brew note try to add it to the DB
        for(Iterator<AppSettingsSchema> i = aAppSettingsSchema.iterator(); i.hasNext(); )
        {
            AppSettingsSchema appSettingsSchema = i.next();
            if(!addAppSetting(appSettingsSchema))
                return false;
        }

        return true;
    }


    /*
    * getting all App Settings by user Id
    */
    public List<AppSettingsSchema> getAllAppSettingsByUserId(int aUserId) {
        List<AppSettingsSchema> appSettingsSchemaList = new ArrayList<AppSettingsSchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_APP_SETTINGS + " WHERE "
                + USER_ID + " = " +Integer.toString(aUserId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllAppSettingsByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
                appSettingsSchema.setUserId(aUserId);
                appSettingsSchema.setAppSetttingId(c.getInt(c.getColumnIndex(ROW_ID)));
                appSettingsSchema.setSettingName(c.getString(c.getColumnIndex(SETTING_NAME)));
                appSettingsSchema.setSettingValue(c.getString(c.getColumnIndex(SETTING_VALUE)));

                // adding to boilList
                appSettingsSchemaList.add(appSettingsSchema);
            } while (c.moveToNext());
        }

        c.close();
        return appSettingsSchemaList;
    }

    /*
* Updating a App Setting
*/
    public Boolean updateAppSetting(AppSettingsSchema aAppSettingsSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateAppSetting Name["+aAppSettingsSchema.getSettingName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aAppSettingsSchema.getUserId());
        values.put(SETTING_NAME, aAppSettingsSchema.getSettingName());
        values.put(SETTING_VALUE, aAppSettingsSchema.getSettingValue());

        // updating row
        int retVal = db.update(TABLE_APP_SETTINGS, values, ROW_ID + " = ?",
                new String[] { Integer.toString(aAppSettingsSchema.getAppSetttingId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    //************************************Helper functions***************
    /*
    * Convert Bitmap to BLOB storage byte array
     */
    private byte[] GetBitmapByteArray(Bitmap aBitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        aBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }


    /**
     * get datetime
     * */
    private String getDateTime() {
        Date date = new Date();
        return APPUTILS.dateFormat.format(date);
    }
/*
    Add Brew Note on completion of a schedule
 */
    private void ScheduleNoteWriterHelper(ScheduledBrewSchema aSBrew)
    {
        String brewNote = "******Auto Added by Scheduler******" +"\r\n "
                         + "Start Date: "+aSBrew.getStartDate() +"\r\n "
                         +"Orignal Gravity:  "+aSBrew.getOG() +"\r\n "
                         +"Final Gravity:  "+aSBrew.getFG();


        BrewNoteSchema nBrew = new BrewNoteSchema();
        nBrew.setBrewName(aSBrew.getBrewName());
        nBrew.setUserName(aSBrew.getUserName());
        nBrew.setCreatedOn(getDateTime());
        nBrew.setBrewNote(brewNote);

        addBrewNote(nBrew);
    }
}
