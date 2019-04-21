package vn.kingbee.widget.spinner.demo

import vn.kingbee.widget.BaseActivity
import android.widget.Toast
import android.widget.AdapterView
import android.text.SpannableString
import android.text.Spannable
import vn.kingbee.widget.spinner.nice.SimpleSpinnerTextFormatter
import android.os.Bundle
import android.view.View
import vn.kingbee.widget.R
import vn.kingbee.widget.spinner.nice.NiceSpinner
import java.util.*
import kotlin.collections.ArrayList


class SpinnerDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner_demo)
        setupDefault()
        setupTintedWithCustomClass()
        setupXml()
    }

    private fun setupXml() {
        val spinner = findViewById<NiceSpinner>(R.id.niceSpinnerXml)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View,
                                        position: Int,
                                        id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SpinnerDemoActivity, "Selected: $item", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
    }

    private fun setupTintedWithCustomClass() {
        val spinner = findViewById<NiceSpinner>(R.id.tinted_nice_spinner)
        val persons : List<Person> = ArrayList()

        (persons as ArrayList).add(Person("Tony", "Stark"))
        persons.add(Person("Steve", "Rogers"))
        persons.add(Person("Bruce", "Banner"))

        val textFormatter = object : SimpleSpinnerTextFormatter() {
            override fun format(item: Any): Spannable {
                val person = item as Person
                return SpannableString(person.name + " " + person.surname)
            }
        }

        spinner.setSpinnerTextFormatter(textFormatter)
        spinner.setSelectedTextFormatter(textFormatter)
        spinner.addOnItemClickListener { parent, view, position, id ->
            val person = spinner.getSelectedItem() as Person //parent.getItemAtPosition(position).toString();
            Toast.makeText(
                this@SpinnerDemoActivity, "Selected: $person", Toast.LENGTH_SHORT
            ).show()
        }
        spinner.attachDataSource(persons)
    }

    private fun setupDefault() {
        val spinner = findViewById<NiceSpinner>(R.id.nice_spinner)
        val dataset = LinkedList(Arrays.asList("One", "Two", "Three", "Four", "Five"))
        spinner.attachDataSource(dataset)
        spinner.addOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position).toString()
            Toast.makeText(this@SpinnerDemoActivity, "Selected: $item", Toast.LENGTH_SHORT).show()
        }
    }
}

internal class Person(val name: String, val surname: String) {

    override fun toString(): String {
        return "$name $surname"
    }
}