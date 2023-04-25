package com.example.atry

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.atry.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var pathList: MutableList<String> = mutableListOf()
    var imagepaths: MutableList<String> = mutableListOf()
    var indexmap: MutableMap<String, FloatArray> = mutableMapOf()
    var CNindexmap: MutableMap<String, FloatArray> = mutableMapOf()
    var queryurl: String = "https://3542794fy3.imdo.co/api/process"
    val sharedPref: SharedPreferences by lazy { getPreferences(Context.MODE_PRIVATE) }
    var indexQueue: MutableList<String> = mutableListOf()

//    var queryurl: String = "https://adsadawawdasda.com"
//    var UriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // load
//        sharedPref = getPreferences(Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("pathList", null)
        if (jsonString != null) {
            pathList = Gson().fromJson(jsonString, object : TypeToken<MutableList<String>>() {}.type)
        }
        val indexmapjsonString = sharedPref.getString("indexmap", null)
        if (indexmapjsonString != null) {
            indexmap = Gson().fromJson(indexmapjsonString, object : TypeToken<MutableMap<String, FloatArray>>() {}.type)
        }
        val CNindexmapjsonString = sharedPref.getString("CNindexmap", null)
        if (CNindexmapjsonString != null) {
            CNindexmap = Gson().fromJson(CNindexmapjsonString, object : TypeToken<MutableMap<String, FloatArray>>() {}.type)
        }
//        val queryUrlraw = sharedPref.getString("queryurl", null)
//        if (queryUrlraw != null) {
//            queryurl = queryUrlraw
//        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        } else { finish()

        }

//        val indexedImage: MutableList<String> = indexmap.keys.toMutableList()
//        val CNindexedImage: MutableList<String> = CNindexmap.keys.toMutableList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        val pathListjsonString = Gson().toJson(pathList)
        val indexmapjsonString = Gson().toJson(indexmap)
        val CNindexmapjsonString = Gson().toJson(CNindexmap)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("pathList", pathListjsonString)
            putString("indexmap", indexmapjsonString)
            putString("CNindexmap", CNindexmapjsonString)
            putString("queryurl", queryurl)
            apply()
        }
    }


//    override fun onResume() {
//        super.onResume()
//        val sharedPref = getPreferences(Context.MODE_PRIVATE)
//        val jsonString = sharedPref.getString("pathList", null)
//        if (jsonString != null) {
//            pathList = Gson().fromJson(jsonString, object : TypeToken<MutableList<String>>() {}.type)
//        }
//    }


}



class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // reference the mainActivity
        title = "Settings"

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val queryUrlraw = sharedPref.getString("queryurl", "https://3542794fy3.imdo.co/api/process")


        val optionList: List<String> = listOf("Server Address", "Language", "Clear Index")
        val adapter: ArrayAdapter<String> = ArrayAdapter<Any?>(this, android.R.layout.simple_list_item_1, optionList) as ArrayAdapter<String>
        val listView = findViewById<ListView>(R.id.settingListView)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val clickedenrty = parent.getItemAtPosition(position) as String
            if (clickedenrty == "Server Address") {
//                val intent = Intent(this, ServerAddressActivity::class.java)
//                startActivity(intent)
                // 显示一个对话框 用于输入服务器地址
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Add new path")

                val input = EditText(this)
                input.setText(sharedPref.getString("queryurl", "https://3542794fy3.imdo.co/api/process")) // 默认值
                builder.setView(input)

                builder.setPositiveButton("OK") { _, _ ->
                    with (sharedPref.edit()) {
                        putString("queryurl", input.text.toString())
                        apply()
                    }
                    Toast.makeText(this, "Server address changed", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

                builder.show()

            }
            else if (clickedenrty == "Language") {
//                val intent = Intent(this, LanguageActivity::class.java)
//                startActivity(intent)
            }
            else if (clickedenrty == "Clear Index") {
                AlertDialog.Builder(this)
                    .setTitle("Clear Index")
                    .setMessage("Are you sure you want to clear all the index?\nThis action will re-index your meme library!!!")
                    .setPositiveButton("Delete") { dialog, which ->
                        with(sharedPref.edit()) {
                            remove("indexmap")
                            apply()
                        }
                        Log.d("shiiit", sharedPref.getString("indexmap", "null").toString())
                        Toast.makeText(this, "Index map cleared", Toast.LENGTH_SHORT).show()

                    }
                    .setNegativeButton("Cancel", null)
                    .show()

            }


        }
    }

}