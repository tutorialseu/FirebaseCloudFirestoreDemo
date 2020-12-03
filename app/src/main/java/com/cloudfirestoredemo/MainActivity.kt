package com.cloudfirestoredemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // TODO Step 12: Override the function and call MultiDex.install(this) in it
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)

    }

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_main)

        // TODO Step 17: Call the function to get the users list.
        // START
        getUsersList()
        // END

        // TODO Step 5: Assign the click event to the SAVE button and add the feature to add the data to the cloud firestore.
        // START
        btn_save.setOnClickListener {

            // Get the name from the input field.
            val name: String = et_name.text.toString().trim { it <= ' ' }
            // Get the email id from the input field.
            val email: String = et_email_id.text.toString().trim { it <= ' ' }

            // If the entered value for name and email is blank, then show the error message or continue with the implemented feature.
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    // TODO Step 7: Create an instance for User data model class and assign the values.
                    // START
                    // We have pass the user name and email as the required param.
                    val user = User(name, email)
                    // END

                    // TODO Step 8: Now make an entry in the cloud firestore database as we have all the required fields.
                    // START
                    // With the use of firestore instance we will make an entry of user with name and email in the cloud firestore database.
                    // The "users" is collection name. If the collection is already created then it will not create the same one again.
                    FirebaseFirestore.getInstance().collection("users")
                        // Document ID for users fields.
                        // Here the document it is auto created. You can also define whatever you want by passing the value as param.
                        .document()
                        // Here the userInfo are Fields for the database with the values.
                        .set(user)
                        .addOnSuccessListener {

                            et_name.setText("")
                            et_email_id.setText("")

                            Toast.makeText(
                                this@MainActivity,
                                "Your data saved successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // TODO Step 18: Call the function to get the users list.
                            // START
                            getUsersList()
                            // END
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this@MainActivity,
                                javaClass.simpleName + e,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    // END
                }
            }
        }
        // END
    }

    // TODO Step 13: Create a function to get the list of users from the cloud firestore.
    // START
    /**
     * A function to get the users list.
     */
    fun getUsersList() {
        // The collection name for USERS
        FirebaseFirestore.getInstance().collection("users")
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of users in the form of documents.
                Log.e("Users List", document.documents.toString())

                // Here we have created a new instance for Users ArrayList.
                val usersList: ArrayList<User> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }

                // TODO Step 16: Populate the list in the UI using RecyclerView.
                // START
                rv_users_list.layoutManager = LinearLayoutManager(this@MainActivity)
                rv_users_list.setHasFixedSize(true)

                val listAdapter = UsersListAdapter(this@MainActivity, usersList)
                rv_users_list.adapter = listAdapter
                // END
            }
            .addOnFailureListener { e ->
                Log.e("Get Users List", "Error while getting users list.", e)
            }
    }
    // END
}