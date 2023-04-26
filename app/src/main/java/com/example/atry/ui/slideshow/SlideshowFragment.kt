package com.example.atry.ui.slideshow

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView

import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.atry.MainActivity
import com.example.atry.R
import com.example.atry.databinding.FragmentSlideshowBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.File

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private var inputtext : String = ""
    private var sortedList : List<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow

        val resultGridView: GridView = binding.resultGridView

        mainActivity = activity as MainActivity

        refreshImageList()
        binding.textSlideshow.text = "Search your MEME! \n You have "+mainActivity.indexmap.size+" MEMEs Indexed"

//        binding.textSlideshow.text = "Search your MEME! "



        binding.submitButton.setOnClickListener {
            inputtext = binding.inputText.text.toString()
            handlebutton()
            // Do something with the text input

            // Hide the keyboard.
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.inputText.windowToken, 0)

        }

        // Handle the action when the user presses the "Enter" button on the keyboard
        binding.inputText.setOnEditorActionListener{textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                inputtext = binding.inputText.text.toString()
                handlebutton()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.inputText.windowToken, 0)
                true
            } else {
                false
            }
        }

        resultGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val imagePath = binding.resultGridView.adapter.getItem(position).toString()
//            Toast.makeText(context, imagePath, Toast.LENGTH_SHORT).show()
            shareImage(requireContext(), imagePath)
            Log.d("GalleryFragment", imagePath)

        }


        return root
    }

    fun handlebutton() {

        binding.textSlideshow.visibility = View.GONE
        binding.resultGridView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val listener = object : OnTextEmbeddingResultListener {
            override fun onSuccess(textEmbedding: FloatArray) {
                sortedList = sortMapByDotProduct(mainActivity.indexmap, textEmbedding as FloatArray)
                binding.progressBar.visibility = View.GONE
                binding.resultGridView.visibility = View.VISIBLE
//        Toast.makeText(mainActivity, sortedList[0], Toast.LENGTH_LONG).show()
                showImages(sortedList)
            }

            override fun onFailure(error: String) {
                Toast.makeText(mainActivity, error, Toast.LENGTH_LONG).show()
            }

            override fun getContext(): Context {
                return requireContext()
            }
        }

        getTextEmbedding(inputtext, listener)

    }

//    fun getTextEmbedding(text: String): FloatArray? {
//        val url = "https://3542794fy3.imdo.co/api/process"
//        val data = JSONObject().apply { put("text", text) }
//        }

    fun getTextEmbedding(text: String, listener: OnTextEmbeddingResultListener) {
        val url = mainActivity.sharedPref.getString("queryurl", "https://7b29232h51.goho.co/api/process")
//        Toast.makeText(mainActivity, url, Toast.LENGTH_LONG).show()
//        val url = mainActivity.queryurl
        val data = JSONObject().apply { put("text", text) }
        val request = JsonObjectRequest(Request.Method.POST, url, data,
            { response ->
                val resultArray = response.optJSONArray("result_text")
                if (resultArray != null) {
                    val result = FloatArray(resultArray.length())
                    for (i in 0 until resultArray.length()) {
                        result[i] = resultArray.getDouble(i).toFloat()
                    }
                    listener.onSuccess(result)
                } else {
                    listener.onFailure("Failed to parse server response")
                }
            },
            { error ->
//                Toast.makeText(mainActivity, data.toString(), Toast.LENGTH_LONG).show()
                listener.onFailure(error.toString())

            })
        Volley.newRequestQueue(listener.getContext()).add(request)
    }

    interface OnTextEmbeddingResultListener {
        fun onSuccess(embedding: FloatArray)
        fun onFailure(error: String)
        fun getContext(): Context
    }

    fun sortMapByDotProduct(map: Map<String, FloatArray>, query: FloatArray): List<String> {
        // 将Map转换为List
        val templist = map.toList()

        // 对List进行排序
        val sortedList = templist.sortedByDescending { (_, index) ->
            // 计算每个FloatArray和query的点积，并返回结果
            dotProduct(query, index)
        }

        // 返回排序后的结果
        return sortedList.map { it.first }
    }

    fun dotProduct(query: FloatArray, index: FloatArray): Float {
        require(query.size == index.size) { "Matrix column number must be equal to vector row number" }
        val dotProduct = query.zip(index).fold(0f) { acc, pair -> acc + pair.first * pair.second }
        return dotProduct
    }

    private fun showImages(imagepaths: List<String>) {
        val paths = imagepaths
        if (paths.isNotEmpty()) {
            val adapter = ImageAdapter(requireContext(), paths, mainActivity)
            binding.resultGridView.adapter = adapter
        }
    }

    private fun shareImage(context: Context,imagePath: String) {
        checkUriExposure()
        val image = File(imagePath)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image))
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share image using"))
    }

    private fun checkUriExposure() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }


    class ImageAdapter(private val context: Context, private val paths: List<String>, private val mainActivity: MainActivity) : BaseAdapter() {


        override fun getCount(): Int {
            return paths.size
        }

        override fun getItem(position: Int): Any {
            return paths[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


//        override fun getView(position: Int, convertView: View?,  parent: ViewGroup?): View {
//            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.result_image, parent, false)
//            val imageView: ImageView = view.findViewById(R.id.resultimageView)
////            val cTextView: TextView = view.findViewById(R.id.ctextView111)
//            if (convertView == null) {
//                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//                imageView.layoutParams = RelativeLayout.LayoutParams(250, 250)
//            }
//            Glide.with(context).load(paths[position]).into(imageView)
////            cTextView.visibility = View.GONE
//            return view
//        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val imageView: ImageView
            if (convertView == null) {
                imageView = ImageView(context)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.layoutParams = ViewGroup.LayoutParams(250, 250)
            } else {
                imageView = convertView as ImageView
            }
            Glide.with(context).load(paths[position]).into(imageView)
            return imageView
        }


    }

    private fun refreshImageList(){
        mainActivity.imagepaths.clear()
        // check if the indexmap is in the sharedPref if not flush the mainActivity.indexmap
//        val shiiit = mainActivity.sharedPref.getString("indexmap", "NOT FOUND")
//        if (shiiit == "NOT FOUND") {
//            Toast.makeText(context, "indexmap cleared", Toast.LENGTH_SHORT).show()
//            mainActivity.indexmap.clear()
//        }
        for (i in mainActivity.pathList.indices) {
            if (File(mainActivity.pathList[i]).isDirectory) {
                //                Toast.makeText(context, "文件夹存在"+mainActivity.pathList[i], Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "Path not exist: "+mainActivity.pathList[i], Toast.LENGTH_SHORT).show()
            }
            var imagenames = getFileNames(mainActivity.pathList[i])
            for (j in imagenames.indices) {
                val newimage=mainActivity.pathList[i] + "/" + imagenames[j]
                if (mainActivity.imagepaths.contains(newimage)) {
                    continue
                }
                else {
                    mainActivity.imagepaths.add(newimage)
                }

            }
        }
        // 检查在mainActivity.imagepaths中 但是在indexmap.keys中不存在的图片路径 从indexmap中移除这些键值对
        val indexKeys = mainActivity.indexmap.keys.toSet()
        val dif=indexKeys.minus(mainActivity.imagepaths.toSet()).toList()
        for (k in dif.indices) {
            mainActivity.indexmap.remove(dif[k])
        }
    }
    private fun getFileNames(path: String): List<String> {
        val file = File(path)
        val fileNames = mutableListOf<String>()
        if (file.exists() && file.isDirectory) {
            if (file.listFiles() == null) {
                Toast.makeText(context, "Path not exist: ", Toast.LENGTH_SHORT).show()
            }
            var filelist=file.listFiles()
            for (i in filelist!!.indices) {
//                Toast.makeText(context, filelist[i].name, Toast.LENGTH_SHORT).show()
                if (filelist[i].isFile) {
                    if (filelist[i].name.endsWith(".jpg") || filelist[i].name.endsWith(".png") || filelist[i].name.endsWith(".jpeg")){ //only take non gif images
                        fileNames.add(filelist[i].name)}
                }
            }
        }
        return fileNames
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}