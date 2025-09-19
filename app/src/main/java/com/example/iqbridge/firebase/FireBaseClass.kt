package com.example.iqbridge.firebase

import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.example.iqbridge.R
import com.example.iqbridge.models.LeaderBoardModel
import com.example.iqbridge.models.UserModel
import com.example.iqbridge.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage


class FireBaseClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(userInfo: UserModel) {
        mFireStore.collection(Constants.user)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
    }

    fun getUserInfo(callback: UserInfoCallback) {
        val userDocument =
            FirebaseFirestore.getInstance().collection(Constants.user).document(getCurrentUserId())

        userDocument.get().addOnSuccessListener { documentSnapshot ->
            val userInfo = documentSnapshot.toObject(UserModel::class.java)
            callback.onUserInfoFetched(userInfo)
        }.addOnFailureListener { e ->
            // Handle the error here
            callback.onUserInfoFetched(null)
        }
    }

    fun updateProfile(name:String?, imgUri: Uri?)
    {
        val userDocument = mFireStore.collection(Constants.user).document(getCurrentUserId())
        if (!name.isNullOrEmpty())
        {
            userDocument.update("name",name)
        }
        if (imgUri!=null)
        {
            uploadImage(imgUri)
        }
    }

    fun setProfileImage(imageUrl: String?, view: ShapeableImageView) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl)
                .placeholder(R.drawable.profile_pic) // optional
                .into(view)
        }
    }


    private fun uploadImage(imgUri: Uri) {
        val userDocument = mFireStore.collection(Constants.user).document(getCurrentUserId())
        val storageRef = FirebaseStorage.getInstance().reference
        val profilePicRef = storageRef.child("profile_pictures/${getCurrentUserId()}")

        profilePicRef.putFile(imgUri).addOnSuccessListener {
            profilePicRef.downloadUrl.addOnSuccessListener { uri ->
                userDocument.update("image", uri.toString())
            }
        }.addOnFailureListener {
            Log.e("ImageUpload", "Unsuccessful", it)
        }
    }


    fun updateScore(newScore: Double) {
        val userDocument = mFireStore.collection(Constants.user).document(getCurrentUserId())
        getUserInfo(object : UserInfoCallback {
            override fun onUserInfoFetched(userInfo: UserModel?) {
                userInfo?.let {
                    val newAllTimeScore = userInfo.allTimeScore + newScore
                    val newWeeklyScore = userInfo.weeklyScore + newScore
                    val newMonthlyScore = userInfo.monthlyScore + newScore
                    userDocument.update(
                        Constants.allTimeScore, newAllTimeScore,
                        Constants.weeklyScore, newWeeklyScore,
                        Constants.monthlyScore, newMonthlyScore,
                        Constants.lastGameScore, newScore
                    ).addOnSuccessListener {
                        Log.e("DataUpdate", "Updated")
                    }.addOnFailureListener {
                        Log.e("DataUpdate", "Failed")
                    }
                }
            }
        })
    }

    fun getUserRank(type: String,callback:UserRankCallback){
        var rank: Int
        mFireStore.collection(Constants.user).orderBy(type, Query.Direction.DESCENDING)
            .get().addOnSuccessListener { result ->
                rank = 1
                val usrId = getCurrentUserId()
                for (document in result) {
                    if (document.id == usrId)
                        break
                    rank = rank + 1
                }
                callback.onUserRankFetched(rank)
            }.addOnFailureListener {
                Log.e("QueryResult", "Failure")
                callback.onUserRankFetched(null)
            }
    }

    fun getLeaderBoardData(type: String, callBack: LeaderBoardDataCallback) {
        mFireStore.collection(Constants.user)
            .orderBy(type, Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.map { it.toObject(UserModel::class.java) }
                callBack.onLeaderBoardDataFetched(LeaderBoardModel(users))
            }
            .addOnFailureListener {
                callBack.onLeaderBoardDataFetched(LeaderBoardModel())
            }
    }



    fun doesDocumentExist(documentId: String): Task<Boolean> {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection(Constants.user).document(documentId)

        return documentRef.get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    task.result?.exists() ?: false
                } else {
                    // Handle the error
                    false
                }
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = Firebase.auth.currentUser
        var currentUserId = ""
        if (currentUser != null)
            currentUserId = currentUser.uid
        return currentUserId
    }

    interface UserInfoCallback {
        fun onUserInfoFetched(userInfo: UserModel?)
    }

    interface UserRankCallback {
        fun onUserRankFetched(rank:Int?)
    }

    interface LeaderBoardDataCallback{
        fun onLeaderBoardDataFetched(leaderBoardModel: LeaderBoardModel?)
    }
}