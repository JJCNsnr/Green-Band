package com.example.kotlinsql//copied this from a tutorial
//https://www.javatpoint.com/kotlin-android-sqlite-tutorial
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity //superseded
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.content.Intent

import com.example.kotlinsql.R.layout.activity_main
import java.time.Clock
import java.time.ZonedDateTime



class MainActivity() : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        btnSettingsInput.setOnClickListener {
            val second = Intent(this, SettingsInput::class.java)//sets "second" to be SettingsInput
            // start your next activity
            startActivity(second)
        }
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        databaseHandler.cullData()  //[experimental] function  to delete old data


    }


        //method for saving records in database
        fun saveRecord(view: View) {
            var noow = ZonedDateTime.now(Clock.systemUTC())
            var noowSecs:Long  = noow.toEpochSecond()
            var noowMins:Long  = (noowSecs)/60
            //var bolusLifeMins:Long = 220// this has to come from a database or store of "preferences" NOT NEEDED HERE

            var bolusTimeMins:Long = noowMins;//this has to go to records database or array.
            // val id = u_id.text.toString()//this is the original to take from button
            val bolusTime = bolusTimeMins.toString()
            val name = u_name.text.toString()
            val email = u_email.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (name.trim() != "" && email.trim() != "") {//removed bolusTime.trim() != "" &&
                val status =
                    databaseHandler.addEmployee(BolusModelClass(bolusTime, name , email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()

                    u_name.text.clear()
                    u_email.text.clear()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "name or email cannot be blank ",//TO DO update toast text
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //method for read records from database in ListView
        fun viewRecord(view: View) {
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the viewEmployee method of DatabaseHandler class to read the records
            val bolus: List<BolusModelClass> = databaseHandler.viewEmployee()
            val bolusArrayId = Array<String>(bolus.size) { "null" }
            val bolusArrayName = Array<String>(bolus.size) { "null" }
            val bolusArrayEmail = Array<String>(bolus.size) { "null" }
            var index = 0
            for (e in bolus) {
                bolusArrayId[index] = e.bolusTime
                bolusArrayName[index] = e.name
                bolusArrayEmail[index] = e.email
                index++
            }
            //creating custom ArrayAdapter
            val myListAdapter = MyListAdapter(
                context = this,
                id = bolusArrayId,
                name = bolusArrayName,
                email = bolusArrayEmail
            )
            listView.adapter = myListAdapter
        }
/*
        //method for updating records based on user id
        fun updateRecord(view: View) {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.update_dialogue, null)
            dialogBuilder.setView(dialogView)

            val edtId = dialogView.findViewById(R.id.updateId) as EditText
            val edtName = dialogView.findViewById(R.id.updateName) as EditText
            val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText

            dialogBuilder.setTitle("Update Record")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

                val updateId = edtId.text.toString()
                val updateName = edtName.text.toString()
                val updateEmail = edtEmail.text.toString()
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if (updateId.trim() != "" && updateName.trim() != "" && updateEmail.trim() != "") {
                    //calling the updateEmployee method of DatabaseHandler class to update record
                    val status = databaseHandler.updateEmployee(
                        EmpModelClass(
                            Integer.parseInt(updateId),
                            updateName,
                            updateEmail
                        )
                    )
                    if (status > -1) {
                        Toast.makeText(applicationContext, "record update", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "id or name or email cannot be blank",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
            dialogBuilder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    //pass
                })
            val b = dialogBuilder.create()
            b.show()
        }
*/
 /*
        //method for deleting records based on id
        fun deleteRecord(view: View) {
            //creating AlertDialog for taking user id
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.delete_dialogue, null)
            dialogBuilder.setView(dialogView)

            val dltId = dialogView.findViewById(R.id.deleteId) as EditText
            dialogBuilder.setTitle("Delete Record")
            dialogBuilder.setMessage("Enter bolus time below")
            dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

                val deleteId = dltId.text.toLong()
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if (deleteId.trim() != "") {
                    //calling the deleteEmployee method of DatabaseHandler class to delete record
                    val status = databaseHandler.deleteEmployee(
                        BolusModelClass(
                            deleteId,
                            "",
                            ""
                        )
                    )
                    if (status > -1) {
                        Toast.makeText(applicationContext, "record deleted", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "id or name or email cannot be blank",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                //pass
            })
            val b = dialogBuilder.create()
            b.show()
        }
*/





}

//private fun Editable.toLong() {}




