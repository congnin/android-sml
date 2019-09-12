package vn.kingbee.widget.layout.motion.fragmentsdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.kingbee.widget.R

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.i(ListFragment::class.java.simpleName, "onCreateView, container is $container")
        return inflater.inflate(R.layout.motion_22_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(
            context, RecyclerView.VERTICAL, false)
        val users = ArrayList<User>()
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))

        val adapter = CustomAdapter(users)
        recyclerView.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }
}