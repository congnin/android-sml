package vn.kingbee.widget.spinner.demo

import android.annotation.SuppressLint
import vn.kingbee.widget.BaseActivity
import android.widget.Toast
import android.widget.AdapterView
import android.text.SpannableString
import android.text.Spannable
import vn.kingbee.widget.spinner.nice.SimpleSpinnerTextFormatter
import android.os.Bundle
import android.text.InputType
import android.view.View
import io.reactivex.Observable
import org.apache.commons.lang3.StringUtils
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.model.Province
import vn.kingbee.model.ProvinceResponse
import vn.kingbee.model.Town
import vn.kingbee.utils.FileUtils
import vn.kingbee.widget.R
import vn.kingbee.widget.edittext.state.BeeInput
import vn.kingbee.widget.recyclerview.location.ProvinceListDialog
import vn.kingbee.widget.recyclerview.location.TownListDialog
import vn.kingbee.widget.spinner.MaterialSpinner
import vn.kingbee.widget.spinner.PaymentNotificationOptionAdapter
import vn.kingbee.widget.spinner.nice.NiceSpinner
import java.util.*
import kotlin.collections.ArrayList


class SpinnerDemoActivity : BaseActivity() {

    lateinit var mProvinceSelection: BeeInput
    lateinit var mTownSelection: BeeInput
    var provinceList: List<Province>? = null
    var provinceListDialog: ProvinceListDialog? = null
    var townListDialog: TownListDialog? = null
    private var materialSpinner: MaterialSpinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner_demo)
        setupDefault()
        setupTintedWithCustomClass()
        setupXml()

        mProvinceSelection = findViewById(R.id.ip_province)
        mTownSelection = findViewById(R.id.ip_town)
        materialSpinner = findViewById(R.id.spPaymentNotificationMethod)

        initViews()
        addEvents()
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
        val persons: List<Person> = ArrayList()

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
            val person =
                spinner.selectedItem as Person //parent.getItemAtPosition(position).toString();
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

    private fun initViews() {
        mProvinceSelection.setInputType(InputType.TYPE_NULL)
        mProvinceSelection.getInput()?.isFocusable = false
        mProvinceSelection.getInput()?.hint = "Pilih provinsi"
        mProvinceSelection.getInput()?.setOnClickListener { v -> showProvince() }

        mTownSelection.setInputType(InputType.TYPE_NULL)
        mTownSelection.getInput()?.isFocusable = false
        mTownSelection.getInput()?.hint = "Pilih kota"
        mTownSelection.getInput()
            ?.setOnClickListener { v -> showTown(mProvinceSelection.getInput()?.text.toString()) }
    }

    @SuppressLint("CheckResult")
    private fun addEvents() {
        if (provinceList.isNullOrEmpty()) {
            showProgressDialog()
            getProvinceFromResource().subscribe({ provinceInfo ->
                provinceList = provinceInfo.provinces
            }, { e ->
                hideProgressDialog()
                Timber.d("error load province: %s", e.message)
            }, { hideProgressDialog() })
        }


        val adapter = PaymentNotificationOptionAdapter(this)
        materialSpinner?.setAdapter(adapter)
        materialSpinner?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

        })
    }

    private fun getProvince(): Observable<List<Province>> {
        return Observable.fromArray(provinceList)
    }

    @SuppressLint("CheckResult")
    private fun showProvince() {
        if (!provinceList.isNullOrEmpty()) {
            showProvinceListDialog(provinceList!!)
        }
    }

    fun showTown(provinceName: String) {
        if (!StringUtils.isEmpty(provinceName) && !provinceName.equals(
                "Pilih provinsi", ignoreCase = true
            )
        ) {
            val townList = getTownsByProvinceName(provinceName)
            townListDialog = TownListDialog(
                this@SpinnerDemoActivity,
                townList,
                object : TownListDialog.TownDialogListener {
                    override fun onTownSelected(town: Town) {
                        mTownSelection.setText(town.mName!!)
                    }
                })
            townListDialog?.show()
        }
    }

    fun getTownsByProvinceName(provinceName: String): List<Town> {
        if (provinceList.isNullOrEmpty()) {
            return emptyList()
        }
        return provinceList?.first { it.mName.equals(provinceName) }?.mTown!!
    }

    private fun getProvinceFromResource(): Observable<ProvinceResponse> {
        return FileUtils.getProvinceFromResource(MyApp.getInstance())
    }

    fun showProvinceListDialog(province: List<Province>) {
        provinceListDialog = ProvinceListDialog(this@SpinnerDemoActivity,
            province,
            object : ProvinceListDialog.ProvinceDialogListener {
                override fun onProvinceSelected(province: Province) {
                    mProvinceSelection.setText(province.mName!!)
                    mTownSelection.setText("")
                }

            })
        provinceListDialog?.show()
    }
}

internal class Person(val name: String, val surname: String) {

    override fun toString(): String {
        return "$name $surname"
    }
}