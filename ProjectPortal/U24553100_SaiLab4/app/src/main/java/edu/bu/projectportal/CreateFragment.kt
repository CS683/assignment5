package edu.bu.projectportal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import edu.bu.projectportal.databinding.FragmentCreateBinding
import edu.bu.projectportal.datalayer.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        binding.createBtnId.setOnClickListener {
            val projectPortalDatabaseInst = (requireContext().applicationContext as ProjectPortalApplication).projectportalDatabase
            val projectDaoInst = projectPortalDatabaseInst.projectDao()
            var project = Project(
                binding.editProjTitleId.text.toString(),
                binding.editProjectAuthorsId.text.toString(),
                binding.editProjectLinksId.text.toString(),
                binding.favoriteId.isChecked
            )

            Log.i("Sai","Outside adding proj in createFragment")

            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    Log.i("Sai","adding proj in createFragment in IO Thread")
                    projectDaoInst.addProject(project)
                }
            }
//            Project.projects.add(
//                Project(
//                Project.projects[Project.projects.size-1].id+1,
//                binding.editProjTitleId.text.toString(),
//                binding.editProjectAuthorsId.text.toString(),
//                binding.editProjectLinksId.text.toString(),
//            )
//            )

            view.findNavController().
            navigate(R.id.action_createFragment_pop)
        }
        binding.cancelId.setOnClickListener {
            view.findNavController().
            navigate(R.id.action_createFragment_pop)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}