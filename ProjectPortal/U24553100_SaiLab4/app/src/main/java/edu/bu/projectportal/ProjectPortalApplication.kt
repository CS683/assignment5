package edu.bu.projectportal

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
//import edu.bu.projectportal.datalayer.LoginRepository
import edu.bu.projectportal.datalayer.Project
import edu.bu.projectportal.datalayer.ProjectPortalDatabase
//import edu.bu.projectportal.datalayer.ProjectPortalRepository
//import edu.bu.projectportal.firebaseutil.FirebaseLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


/*
This class defines an application class
Define the application name as ProjectPortalApplication
in the manifest file under the application tag.
 */
class ProjectPortalApplication: Application() {
    lateinit var projectportalDatabase: ProjectPortalDatabase

    override fun onCreate() {
        super.onCreate()
        projectportalDatabase =
            Room.databaseBuilder(
                applicationContext, ProjectPortalDatabase::class.java,
                "projectportal-db"
            )
                // add a callback to modify onCreate() to
                // add some initial projects.
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        addInitProjects()
                    }
                })
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                .build()
    }

    // this is only used to add some initial projects
    // when the database is first created.
    // this is done through
    fun addInitProjects() {
        // need to execute in a separate thread
        CoroutineScope(Dispatchers.IO).launch {
            projectportalDatabase.projectDao().addProject(
                Project(
                    "Person Data",
                    "Sai", "facebook.com", false
                )
            )
        }
    }
}