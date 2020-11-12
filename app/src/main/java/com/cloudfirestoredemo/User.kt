package com.cloudfirestoredemo

// TODO Step 6: Create a data model class for users that we will use to set the data and store it to the cloud firestore also will use it for the RecyclerView.
// START
// A data model class for users that we will use to set the values and store it to the cloud firestore also will use it for the RecyclerView.
data class User(
    val name: String = "",
    val email: String = ""
)
// END