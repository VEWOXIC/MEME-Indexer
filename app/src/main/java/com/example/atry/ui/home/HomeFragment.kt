package com.example.atry.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.atry.MainActivity
import com.example.atry.databinding.FragmentHomeBinding
import java.io.File

class HomeFragment : Fragment() {

    val PICK_DIRECTORY = 1
    private lateinit var adapter: ArrayAdapter<String>
    lateinit var mainActivity: MainActivity

    private var _binding: FragmentHomeBinding? = null
//    var data = mutableListOf<String>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        mainActivity = activity as MainActivity
//        mainActivity.pathList.add("Item 1")

//        if (data.isEmpty()) {
//            data.add("点击右下角按钮添加新项")
//        }

//        (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, mainActivity.pathList)

        binding.listview1.adapter = adapter
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        binding.swipeRefreshLayout.setOnRefreshListener {
            (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.floatingButton.setOnClickListener {
            selectDirectory()
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Add new path")
//
//            val input = EditText(requireContext())
//            input.setText("storage/emulated/0/Pictures") // 默认值
//            builder.setView(input)
//
//            builder.setPositiveButton("OK") { _, _ ->
//                val newItem = input.text.toString()
//                if (newItem in mainActivity.pathList)
//                    Toast.makeText(requireContext(), "Duplicated path", Toast.LENGTH_SHORT).show()
//                else
//                {
//                    if (File(newItem).isDirectory){
//                    adapter.add(newItem)
//                    (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()
//                    }
//                    else
//                        Toast.makeText(requireContext(), "Cannot find path"+newItem, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//            builder.setNegativeButton("Cancel") { dialog, _ ->
//                dialog.cancel()
//            }
//
//            builder.show()
        }
        binding.listview1.setOnItemClickListener { parent, view, position, id ->
            val clickedPath = mainActivity.pathList[position]

            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.parse(clickedPath)
            Toast.makeText(requireContext(), uri.toString(), Toast.LENGTH_SHORT).show()

            AlertDialog.Builder(context)
                .setTitle("Delete path")
                .setMessage("Are you sure you want to delete this path?")
                .setPositiveButton("Delete") { dialog, which ->
                    // 从 pathsList 中删除该条目
                    mainActivity.pathList.removeAt(position)
                    // 更新 ListView
                    (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        return root
    }

    private fun selectDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, PICK_DIRECTORY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            var selectedDirPath = uri.path ?: return
            // replace the first /tree/primary: with /storage/emulated/0/
            selectedDirPath = selectedDirPath.replaceFirst("/tree/primary:", "/storage/emulated/0/")
            if (selectedDirPath in mainActivity.pathList)
                    Toast.makeText(requireContext(), "Duplicated path", Toast.LENGTH_SHORT).show()
                else
                {
                    if (File(selectedDirPath).isDirectory){
//                    adapter.add(selectedDirPath)
//                    (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                        val adapter = binding.listview1.adapter as? ArrayAdapter<String>
                        adapter?.add(selectedDirPath)
                        adapter?.notifyDataSetChanged()
                    }
                    else
                        Toast.makeText(requireContext(), "Cannot find path"+selectedDirPath, Toast.LENGTH_SHORT).show()
                }
//            val selectedDir = DocumentFile.fromTreeUri(requireContext(), uri) ?: return
//            val selectedDirPath = selectedDir.uri.path ?: return
//            val selectedDirPath = uri.toString()
//            // 将所选目录添加到 ListView 中

        }
    }

//    private fun getDirectoryPathFromUri(uri: Uri): String {
//        val documentFile = DocumentFile.fromTreeUri(requireContext(), uri) ?: return ""
//        return documentFile.uri.path ?: ""
//    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (binding.listview1.adapter as ArrayAdapter<String>).notifyDataSetChanged()

    }


}