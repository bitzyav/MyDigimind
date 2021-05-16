package aviles.itzel.digimind.ui.dashboard

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import aviles.itzel.digimind.R
import aviles.itzel.digimind.Task
import aviles.itzel.digimind.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var usuario: FirebaseAuth
    private lateinit var storage: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        storage =  FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        btn_time.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                btn_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(root.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true).show()

        }

        btn_save.setOnClickListener{

            var titulo = et_task.text.toString()
            var time = btn_time.text.toString()

            var days = ArrayList<String>()
            val actividad = hashMapOf(
                    "actividad" to et_task.text.toString(),
                    "email" to usuario.currentUser.email.toString(),
                    "do" to checkSunday.isChecked,
                    "lu" to checkMonday.isChecked,
                    "ma" to checkTuesday.isChecked,
                    "mi" to checkWednesday.isChecked,
                    "ju" to checkThursday.isChecked,
                    "vi" to checkFriday.isChecked,
                    "sa" to checkSaturday.isChecked,
                    "tiempo" to btn_time.toString()
            )

            storage.collection("actividades")
                    .add(actividad)
                    .addOnSuccessListener {
                        Toast.makeText(root.context, "Task Agregada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(root.context, "Error: intente de nuevo", Toast.LENGTH_SHORT).show()
                    }

            if(checkMonday.isChecked)
                days.add("Monday")
            if(checkTuesday.isChecked)
                days.add("Tuesday")
            if(checkWednesday.isChecked)
                days.add("Wednesday")
            if(checkThursday.isChecked)
                days.add("Thursday")
            if(checkFriday.isChecked)
                days.add("Friday")
            if(checkSaturday.isChecked)
                days.add("Saturday")
            if(checkSunday.isChecked)
                days.add("Sunday")

            var task = Task(titulo, days, time)

            HomeFragment.tasks.add(task)

            Toast.makeText(root.context, "new task added", Toast.LENGTH_SHORT).show()
        }


        return root
    }


}