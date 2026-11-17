package com.example.karflex.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

const val JSON_URL = "https://gist.githubusercontent.com/axelkrnwn/01c506ac60735f553af951e550634b59/raw/08489b2ff76e1356a6df38d09948716628024195/karflex.json"
const val ACTION_VEHICLE_DATA = "com.example.karflex.ACTGION_VEHICLE_DATA"
const val EXTRA_JSON_STRING= "EXTRA_JSON_STRING"

// Service untuk mengambil data dari JSON
class JSONService: Service(){

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // membuat request ke URL JSON
        var volley = Volley.newRequestQueue(this)
        var request = JsonArrayRequest(JSON_URL,
            {response -> Log.d("VOLLEY", "DATA: $response")

                // inisialisasi broadcast untuk mengirim data JSON ke Main Activity
                val broadcastIntent = Intent(ACTION_VEHICLE_DATA).apply {
                    setPackage(packageName)
                    putExtra(EXTRA_JSON_STRING, response.toString())
                }
                sendBroadcast(broadcastIntent)
                stopSelf()
            },
            {error -> Log.e("VOLLEY", "ERROR: ${error.message}")
                stopSelf()
            })
        volley.add(request)

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}