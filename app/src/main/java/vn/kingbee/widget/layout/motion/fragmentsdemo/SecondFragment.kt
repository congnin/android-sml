package vn.kingbee.widget.layout.motion.fragmentsdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import vn.kingbee.widget.R

class SecondFragment : Fragment() {

    private lateinit var motionLayout: MotionLayout

    companion object {
        fun newInstance() = SecondFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.motion_21_second_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        motionLayout = view.findViewById(R.id.main)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.i(SecondFragment::class.java.simpleName, "onStart of fragment...")
    }
}