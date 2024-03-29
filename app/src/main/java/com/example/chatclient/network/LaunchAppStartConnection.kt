package com.example.chatclient.network
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.example.chatclient.home_screen.HomeScreenActivity
import com.example.chatclient.R
import com.example.chatclient.user.UserManagement
import com.example.chatclient.account.Credentials
import java.net.Socket

class LaunchAppStartConnection : AppCompatActivity(), SocketListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_app_start_connection)

        SocketManager.createSocket(Config.CHAT_SERVER_IP,Config.CHAT_SERVER_PORT, this)

    }

    override fun socketCreated(socket: Socket) {

// when the socket is ready, first check if user has signed up/signed in, if not, go to Credentials

        checkIfUserHasSignedInAndStartHomeScreenActivity()
    }



    fun checkIfUserHasSignedInAndStartHomeScreenActivity() {
        val uid = FirebaseAuth.getInstance().uid

        //if user has not registered/logged in, go to register/login activity
        if (uid == null) {
            val intentRegisterAndLoginActivity = Intent(this, Credentials::class.java)

            startActivity(intentRegisterAndLoginActivity)

            //if user has logged in/registered, send user name to server
        } else {
            val userName = UserManagement.getUsername()//intent.getStringExtra("userName")//host user name

            FirebaseAuth.getInstance().currentUser

            Log.d("Public Group Chat", if (userName == null) "USERNAME IS NOT SET" else userName)

            SocketManager.send(":user $userName")

            val intentHomeScreenActivity = Intent(this, HomeScreenActivity::class.java)
            startActivity(intentHomeScreenActivity)

        }


    }



}
