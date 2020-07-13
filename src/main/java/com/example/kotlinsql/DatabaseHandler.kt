package com.example.kotlinsql
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import java.time.Clock
import java.time.ZonedDateTime

//Create the DatabaseHandler.kt class that extends SQLiteOpenHelper class and override its onCreate(),
// onUpgrade() functions. Insert data into the database by passing
// a ContentValues object to the insert() meth

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION)
{
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "BolusTable"
        private val KEY_ID = "btime"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        /*val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("// this was original clever code
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)

         */
        db?.execSQL("CREATE TABLE BolusTable(btime TEXT PRIMARY KEY,name TEXT,email TEXT)")//this is simple code
   //create another table for Carbs

    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }
    //method to insert data
    fun addEmployee(bolus: BolusModelClass): Long {// try changing addEmployee to saveRecord//changed emp to bolus
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, bolus.bolusTime)
        contentValues.put(KEY_NAME, bolus.name) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,bolus.email ) // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success

    }
    //method to read data
    fun viewEmployee():List<BolusModelClass>{
        val empList:ArrayList<BolusModelClass> = ArrayList<BolusModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? //= null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var bolusTime: String
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                bolusTime = cursor.getString(cursor.getColumnIndex("btime")).toString()
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val bolus= BolusModelClass(
                    bolusTime = bolusTime,
                    name = userName,
                    email = userEmail
                )
                empList.add(bolus)
            } while (cursor.moveToNext())
        }
        return empList
    }
   /*
    //method to update data
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    */
 /*   //method to delete data
    fun deleteEmployee(emp: BolusModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.bolusTimeMins) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"btime="+emp.bolusTimeMins,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success }
*/
//experimental function to delete older records//could or should be moved to or called from OnCreate

    fun cullData(){
        var noow = ZonedDateTime.now(Clock.systemUTC())
        var noowSecs:Long  = noow.toEpochSecond()
        var noowMins:Long  = (noowSecs)/60
        var bolusLifeMins:Long = 220// this has to come from a database or store of "preferences"
        var minTimeMins = noowMins - bolusLifeMins - 24*60//maybe make to text
        var minTimeInText =minTimeMins.toString()
        val db = this.writableDatabase

        db.delete(
            "BolusTable",
            "btime <"+ minTimeInText ,
            null
        )
        db.close()

    }
}

