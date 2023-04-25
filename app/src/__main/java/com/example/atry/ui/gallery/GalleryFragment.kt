package com.example.atry.ui.gallery

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.atry.MainActivity
import com.example.atry.R
import com.example.atry.databinding.FragmentGalleryBinding
import java.io.File


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    lateinit var mainActivity: MainActivity
    private lateinit var adapter: ImageAdapter
    private var showingImages = mutableListOf<String>()



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        mainActivity = activity as MainActivity

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        val gridView: GridView = binding.gridView

        showingImages = mainActivity.imagepaths

        if (mainActivity.pathList.isEmpty()) {
            textView.visibility = View.VISIBLE
            gridView.visibility = View.GONE
            textView.text = "Please add a folder"
        } else {
            textView.visibility = View.GONE
            gridView.visibility = View.VISIBLE
            Log.d("GalleryFragment", mainActivity.pathList[0])
            refreshImageList()
            showImages(showingImages)
//            textView.text = mainActivity.pathList[0]
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val imagePath = binding.gridView.adapter.getItem(position).toString()
                Toast.makeText(context, imagePath, Toast.LENGTH_SHORT).show()
                shareImage(requireContext(), imagePath)
                Log.d("GalleryFragment", imagePath)
            // 测试embedding添加用
                if (!mainActivity.indexmap.containsKey(imagePath)){
                mainActivity.indexmap[imagePath] = getImageEmbedding(imagePath)}

            }

        }

        binding.swipeRefreshLayoutGallary.setOnRefreshListener {
            refreshImageList()
            showImages(showingImages)
//            (gridView.adapter as ImageAdapter).notifyDataSetChanged()
            binding.swipeRefreshLayoutGallary.isRefreshing = false
        }


        binding.filterButton.setOnClickListener {
            showFilterOptions()
        }
        return root
    }


    private fun showFilterOptions() {
        val items = arrayOf("All", "Indexed", "Unindexed")
        AlertDialog.Builder(requireContext())
            .setTitle("Select filter")
            .setItems(items) { dialog, which ->
                // Handle item selection
                refreshImageList()
                when (which) {
                    0 -> {
                        showingImages = mainActivity.imagepaths
                        showImages(showingImages)
//                        (binding.gridView.adapter as ImageAdapter).notifyDataSetChanged()

                    }
                    1 -> {
                        // Show only indexed images
                        showingImages = mainActivity.indexmap.keys.toMutableList()
                        showImages(showingImages)
//                        (binding.gridView.adapter as ImageAdapter).notifyDataSetChanged()
                    }
                    2 -> {
                        // Show only unindexed images val dif=indexKeys.minus(mainActivity.imagepaths.toSet()).toList()
                        val indexKeys = mainActivity.indexmap.keys.toSet()
                        showingImages = mainActivity.imagepaths.toSet().minus(indexKeys).toMutableList()
                        showImages(showingImages)
//                        (binding.gridView.adapter as ImageAdapter).notifyDataSetChanged()
                    }
                }
            }
            .show()
    }

    private fun getImageEmbedding(imagePath: String): FloatArray {
        val image = File(imagePath)
        // 生成三个随机数
        val random = FloatArray(3)
        random[0] = (-1..1).random().toFloat()
        random[1] = (-1..1).random().toFloat()
        random[2] = (-1..1).random().toFloat()
        return random
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

    private fun showImages(imagepaths: List<String>) {
        val paths = imagepaths
        if (paths.isNotEmpty()) {
            val adapter = ImageAdapter(requireContext(), paths, mainActivity)
            binding.gridView.adapter = adapter
        }
    }

    private fun refreshImageList(){
        binding.gallaryProgressBar.visibility = View.VISIBLE
        mainActivity.imagepaths.clear()
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
        binding.gallaryProgressBar.visibility = View.GONE
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

//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
//            val imageView = ImageView(context)
//            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//            imageView.layoutParams = ViewGroup.LayoutParams(250, 250)
//            Glide.with(context).load(paths[position]).into(imageView)
//
//            val cTextView = view.findViewById<TextView>(R.id.ctextView111)
//            cTextView.visibility = View.VISIBLE
////            if (paths[position] in a) {
////
////            } else {
////                cTextView.visibility = View.GONE
////            }
//
//            return view
//        }
//    }

        override fun getView(position: Int, convertView: View?,  parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val cTextView: TextView = view.findViewById(R.id.ctextView111)
            if (convertView == null) {
//                cTextView = view.findViewById(R.id.ctextView111)
//                imageView = ImageView(context)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.layoutParams = RelativeLayout.LayoutParams(250, 250)


            } else {
//                imageView = convertView.findViewById(R.id.imageView)
//                cTextView = view.findViewById(R.id.ctextView111)
//                cTextView.visibility = View.VISIBLE
            }
            Glide.with(context).load(paths[position]).into(imageView)
            if (mainActivity.indexmap.keys.contains(paths[position])) {
                cTextView.background = ContextCompat.getDrawable(context, R.drawable.circle_green)
            } else {
                cTextView.background = ContextCompat.getDrawable(context, R.drawable.circle_red)
            }
            cTextView.visibility = View.VISIBLE
            return view
        }


    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}