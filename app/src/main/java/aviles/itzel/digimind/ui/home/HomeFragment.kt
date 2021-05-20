package aviles.itzel.digimind.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import aviles.itzel.digimind.AdaptadorTareas
import aviles.itzel.digimind.R
import aviles.itzel.digimind.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    private var adaptador: AdaptadorTareas? = null
    private lateinit var homeViewModel: HomeViewModel

    companion object{
        var tasks = ArrayList<Task>()
        var first = true
    }



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        var gridView: GridView = root.findViewById(R.id.gridview)
        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        if(first){
            fillTasks()
            first = false
        }

        adaptador = AdaptadorTareas(root.context, tasks)
        gridView.adapter = adaptador



        return root
    }

    fun fillTasks(){
        //tasks.clear()
        storage.collection("actividades")
                .whereEqualTo("email", usuario.currentUser.email)
                .get()
                .addOnSuccessListener {
                    it.forEach{
                        var dias = ArrayList<String>()
                        if(it.getBoolean("lu") == true){
                            dias.add("Monday")
                        }
                        if(it.getBoolean("ma") == true){
                            dias.add("Tuesday")
                        }
                        if(it.getBoolean("mi") == true){
                            dias.add("Wednesday")
                        }
                        if(it.getBoolean("ju") == true){
                            dias.add("Thursday")
                        }
                        if(it.getBoolean("vi") == true){
                            dias.add("Friday")
                        }
                        if(it.getBoolean("sa") == true){
                            dias.add("Saturday")
                        }
                        if(it.getBoolean("do") == true){
                            dias.add("Sunday")
                        }

                        var titulo = it.getString("actividad")
                        var tiempo = it.getString("tiempo")

                        var act = Task(titulo!!, dias, tiempo!!)

                        tasks.add(act)
                        //Toast.makeText(context, act.toString(), Toast.LENGTH_SHORT).show()

                    }

                }

                .addOnFailureListener{
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }

    }

}