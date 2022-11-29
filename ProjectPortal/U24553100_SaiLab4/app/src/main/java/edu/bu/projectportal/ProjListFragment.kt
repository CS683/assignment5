package edu.bu.projectportal

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import edu.bu.projectportal.databinding.FragmentProjListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class ProjListFragment : Fragment() {
    private var _binding: FragmentProjListBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentProjListBinding.inflate(inflater,
            container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        binding.projListFavId.isChecked = sharedPref.getInt("f",0) == 1
        super.onViewCreated(view, savedInstanceState)
        val projectPortalDatabaseInst = (requireContext().applicationContext as ProjectPortalApplication).projectportalDatabase
        val projectDao = projectPortalDatabaseInst.projectDao()
        binding.projlist?.apply{
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            Log.i("Sai", "Outside kotlinx.coroutines.MainScope Project List Fragments")
//            CoroutineScope(Dispatchers.Main).launch {
                //Log.i("Sai", "kotlinx.coroutines.MainScope Project List Fragments")]
            viewLifecycleOwner.lifecycleScope.launch {
                adapter =
                    if (binding.projListFavId.isChecked) MyProjListRecyclerViewAdapter(projectDao.getFavProjects()) else MyProjListRecyclerViewAdapter(
                        projectDao.getAllProjects()
                    )
            }
//            }
        }

        binding.addProject.setOnClickListener {
            view.findNavController().navigate(R.id.action_projListRecycleViewFragment_to_createFragment)
        }

        binding.projListFavId.setOnClickListener {
            //CoroutineScope(Dispatchers.Main).launch {
                //Log.i("Sai", "kotlinx.coroutines.MainScope binding.projListFavId.setOnClickListener")
            viewLifecycleOwner.lifecycleScope.launch {
                binding.projlist.adapter =
                    if (binding.projListFavId.isChecked) MyProjListRecyclerViewAdapter(projectDao.getFavProjects()) else MyProjListRecyclerViewAdapter(
                        projectDao.getAllProjects()
                    )
            }
            //}
            with (sharedPref.edit()) {
                putInt("f", if(binding.projListFavId.isChecked) 1 else 0)
                apply()
            }
        }
    }

    companion object {
        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
//        @JvmStatic
//        fun newInstance(columnCount: Int) =
//                ProjListFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt(ARG_COLUMN_COUNT, columnCount)
//                    }
//                }
    }
}