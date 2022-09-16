package com.tngen.sgms_android.widget.adapter.listview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.tngen.sgms_android.R
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.model.home.HomeSensorModel
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.utility.loading_dialog.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

import net.cachapa.expandablelayout.ExpandableLayout
import java.text.SimpleDateFormat

class HomeSensorListAdapter(
    private val items: List<HomeSensorModel>,
    private val loadingDialog: LoadingDialog,
) : BaseAdapter() {
    private var contextView: View? = null

    private val _sharedFlowScroll = MutableSharedFlow<Int>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlowScroll = _sharedFlowScroll.asSharedFlow()

    private val _sharedFlowCalibration = MutableSharedFlow<SerialEntity>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlowCalibration = _sharedFlowCalibration.asSharedFlow()

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): HomeSensorModel = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    val simpleDateFormat = SimpleDateFormat("HH:mm:ss")

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var convertView = view
        if (convertView == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.viewholder_home_sensor_list, parent, false)
        this.contextView = convertView

        val item: HomeSensorModel = items[position]
        val sensorIdTextView = convertView!!.findViewById<TextView>(R.id.sensorIdTextView)
        val sensorLocationTextView = convertView.findViewById<TextView>(R.id.sensorLocationTextView)
        val receivedDataTimeTextView = convertView.findViewById<TextView>(R.id.receivedDataTimeTextView)
        sensorIdTextView.text = item.sensorId.toString()
        sensorLocationTextView.text = item.sensorLocation
        receivedDataTimeTextView.text =  item.createdAt?.let { simpleDateFormat.format(item.createdAt) }

        val expandableLayout = convertView.findViewById<ExpandableLayout>(R.id.expandableLayout)
        val sensorLinearLayout = convertView.findViewById<LinearLayout>(R.id.sensorLinearLayout)
        val arrowImageButton = convertView.findViewById<ImageButton>(R.id.arrowImageButton)

        val o2ValueTextView = convertView.findViewById<TextView>(R.id.o2ValueTextView)
        val co2ValueTextView = convertView.findViewById<TextView>(R.id.co2ValueTextView)
        val coValueTextView = convertView.findViewById<TextView>(R.id.coValueTextView)
        val h2sValueTextView = convertView.findViewById<TextView>(R.id.h2sValueTextView)
        val lelValueTextView = convertView.findViewById<TextView>(R.id.lelValueTextView)

        val o2ValueInterfaceTextView = convertView.findViewById<TextView>(R.id.o2ValueInterfaceTextView)
        val co2ValueInterfaceTextView = convertView.findViewById<TextView>(R.id.co2ValueInterfaceTextView)
        val coValueInterfaceTextView = convertView.findViewById<TextView>(R.id.coValueInterfaceTextView)
        val h2sValueInterfaceTextView = convertView.findViewById<TextView>(R.id.h2sValueInterfaceTextView)
        val lelValueInterfaceTextView = convertView.findViewById<TextView>(R.id.lelValueInterfaceTextView)

        val o2ValueMeasureTextView = convertView.findViewById<TextView>(R.id.o2ValueMeasureTextView)
        val co2ValueMeasureTextView = convertView.findViewById<TextView>(R.id.co2ValueMeasureTextView)
        val coValueMeasureTextView = convertView.findViewById<TextView>(R.id.coValueMeasureTextView)
        val h2sValueMeasureTextView = convertView.findViewById<TextView>(R.id.h2sValueMeasureTextView)
        val lelValueMeasureTextView = convertView.findViewById<TextView>(R.id.lelValueMeasureTextView)

        val o2LineChart = convertView.findViewById<LineChart>(R.id.o2LineChart)
        val co2LineChart = convertView.findViewById<LineChart>(R.id.co2LineChart)
        val coLineChart = convertView.findViewById<LineChart>(R.id.coLineChart)
        val h2sLineChart = convertView.findViewById<LineChart>(R.id.h2sLineChart)
        val lelLineChart = convertView.findViewById<LineChart>(R.id.lelLineChart)

        val o2LineChartButton = convertView.findViewById<Button>(R.id.o2LineChartButton)
        val co2LineChartButton = convertView.findViewById<Button>(R.id.co2LineChartButton)
        val coLineChartButton = convertView.findViewById<Button>(R.id.coLineChartButton)
        val h2sLineChartButton = convertView.findViewById<Button>(R.id.h2sLineChartButton)
        val lelLineChartButton = convertView.findViewById<Button>(R.id.lelLineChartButton)

        val calibrationSettingButton = convertView.findViewById<Button>(R.id.calibrationSettingButton)


        if(item.o2 != null) {
            o2ValueTextView.text = String.format("%.1f",item.o2)
            co2ValueTextView.text = item.co2.toString()
            coValueTextView.text = item.co.toString()
            h2sValueTextView.text = item.h2s.toString()
            lelValueTextView.text = item.lel.toString()

        }

        calibrationSettingButton.setOnClickListener {
            val dialogView = LayoutInflater.from(parent?.context).inflate(R.layout.dialog_calibration_setting, parent, false)
            AlertDialog.Builder(it.context)
                .setTitle("캘리브레이션 설정")
                .setMessage("캘리브레이션 설정값을 입력해주세요.")
                .setCancelable(false)
                .setPositiveButton("설정", DialogInterface.OnClickListener { _, _ ->

                    val o2 = dialogView.findViewById<EditText>(R.id.o2Value).text.toString()
                    val lel = dialogView.findViewById<EditText>(R.id.lelValue).text.toString()
                    val co = dialogView.findViewById<EditText>(R.id.coValue).text.toString()
                    val co2 = dialogView.findViewById<EditText>(R.id.co2Value).text.toString()
                    val h2s = dialogView.findViewById<EditText>(R.id.h2sValue).text.toString()

                    if(o2.isEmpty() || lel.isEmpty() || co.isEmpty() || co2.isEmpty() || h2s.isEmpty()) {
                        Preferences.showToast(it.context, "빈 값이 존재합니다. 다시 입력해주세요.")
                    }else {
                        GlobalScope.launch(Dispatchers.Default) {
                            _sharedFlowCalibration.emit(
                                SerialEntity(
                                    sensorId = sensorIdTextView.text.toString().toLong(),
                                    o2 = o2.toDouble(),
                                    lel = lel.toDouble(),
                                    co = co.toDouble(),
                                    co2 = co2.toDouble(),
                                    h2s = h2s.toDouble()
                                )
                            )
                        }

                        loadingDialog.show()
                    }
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { _, _ ->

                })
                .setView(dialogView)
                .show()

        }

        expandableLayout.duration = 300
        expandableLayout.setOnExpansionUpdateListener { _, state ->
            if( state == 2) {
                GlobalScope.launch(Dispatchers.Main) {
                    _sharedFlowScroll.emit(position)
                }
            }
        }
        sensorLinearLayout.setOnClickListener {
            if (expandableLayout.isExpanded) {
                arrowImageButton.animate().rotation(0f).duration = 300
                expandableLayout.collapse()
            } else {
                expandableLayout.expand()
                arrowImageButton.animate().rotation(180f).duration = 300
            }
        }
        arrowImageButton.setOnClickListener {
            if (expandableLayout.isExpanded) {
                arrowImageButton.animate().rotation(0f).duration = 300
                expandableLayout.collapse()
            } else {
                expandableLayout.expand()
                arrowImageButton.animate().rotation(180f).duration = 300
            }
        }
        sensorLinearLayout.setBackgroundResource(R.color.green)
        o2ValueTextView.setTextColor(Color.parseColor("#0BE30B"))
        o2ValueInterfaceTextView.setTextColor(Color.parseColor("#0BE30B"))
        o2ValueMeasureTextView.setTextColor(Color.parseColor("#0BE30B"))
        co2ValueTextView.setTextColor(Color.parseColor("#0BE30B"))
        co2ValueInterfaceTextView.setTextColor(Color.parseColor("#0BE30B"))
        co2ValueMeasureTextView.setTextColor(Color.parseColor("#0BE30B"))
        coValueTextView.setTextColor(Color.parseColor("#0BE30B"))
        coValueInterfaceTextView.setTextColor(Color.parseColor("#0BE30B"))
        coValueMeasureTextView.setTextColor(Color.parseColor("#0BE30B"))
        h2sValueTextView.setTextColor(Color.parseColor("#0BE30B"))
        h2sValueInterfaceTextView.setTextColor(Color.parseColor("#0BE30B"))
        h2sValueMeasureTextView.setTextColor(Color.parseColor("#0BE30B"))
        lelValueTextView.setTextColor(Color.parseColor("#0BE30B"))
        lelValueInterfaceTextView.setTextColor(Color.parseColor("#0BE30B"))
        lelValueMeasureTextView.setTextColor(Color.parseColor("#0BE30B"))

            if(Preferences.baselineEntity != null && item.o2 != null) {
                if (item.o2!! < Preferences.baselineEntity!!.o2 ||
                    item.co2!! > Preferences.baselineEntity!!.co2 ||
                    item.co!! > Preferences.baselineEntity!!.co ||
                    item.h2s!! > Preferences.baselineEntity!!.h2s ||
                    item.lel!! > Preferences.baselineEntity!!.lel
                ) {
                    sensorLinearLayout.setBackgroundResource(R.color.red)
                    if (item.o2!! < Preferences.baselineEntity!!.o2) {
                        o2ValueTextView.setTextColor(Color.parseColor("#FA3838"))
                        o2ValueInterfaceTextView.setTextColor(Color.parseColor("#FA3838"))
                        o2ValueMeasureTextView.setTextColor(Color.parseColor("#FA3838"))
                    }
                    if (item.co2!! > Preferences.baselineEntity!!.co2) {
                        co2ValueTextView.setTextColor(Color.parseColor("#FA3838"))
                        co2ValueInterfaceTextView.setTextColor(Color.parseColor("#FA3838"))
                        co2ValueMeasureTextView.setTextColor(Color.parseColor("#FA3838"))
                    }
                    if (item.co!! > Preferences.baselineEntity!!.co) {
                        coValueTextView.setTextColor(Color.parseColor("#FA3838"))
                        coValueInterfaceTextView.setTextColor(Color.parseColor("#FA3838"))
                        coValueMeasureTextView.setTextColor(Color.parseColor("#FA3838"))
                    }
                    if (item.h2s!! > Preferences.baselineEntity!!.h2s) {
                        h2sValueTextView.setTextColor(Color.parseColor("#FA3838"))
                        h2sValueInterfaceTextView.setTextColor(Color.parseColor("#FA3838"))
                        h2sValueMeasureTextView.setTextColor(Color.parseColor("#FA3838"))
                    }
                    if (item.lel!! > Preferences.baselineEntity!!.lel) {
                        lelValueTextView.setTextColor(Color.parseColor("#FA3838"))
                        lelValueInterfaceTextView.setTextColor(Color.parseColor("#FA3838"))
                        lelValueMeasureTextView.setTextColor(Color.parseColor("#FA3838"))
                    }
                }
            }

        //Chart

        o2LineChartButton.setOnClickListener {
            o2LineChart.isVisible = true
            co2LineChart.isGone = true
            coLineChart.isGone = true
            h2sLineChart.isGone = true
            lelLineChart.isGone = true
        }
        co2LineChartButton.setOnClickListener {
            o2LineChart.isGone = true
            co2LineChart.isVisible = true
            coLineChart.isGone = true
            h2sLineChart.isGone = true
            lelLineChart.isGone = true
        }
        coLineChartButton.setOnClickListener {
            o2LineChart.isGone = true
            co2LineChart.isGone = true
            coLineChart.isVisible = true
            h2sLineChart.isGone = true
            lelLineChart.isGone = true
        }
        h2sLineChartButton.setOnClickListener {
            o2LineChart.isGone = true
            co2LineChart.isGone = true
            coLineChart.isGone = true
            h2sLineChart.isVisible = true
            lelLineChart.isGone = true
        }
        lelLineChartButton.setOnClickListener {
            o2LineChart.isGone = true
            co2LineChart.isGone = true
            coLineChart.isGone = true
            h2sLineChart.isGone = true
            lelLineChart.isVisible = true
        }

        val o2Dataset = LineDataSet(item.o2Entries, "O2")
        o2LineChart.getTransformer(YAxis.AxisDependency.LEFT)
        o2LineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val o2Data = LineData(o2Dataset)
        o2LineChart.data = o2Data
        o2LineChart.invalidate()

        val co2Dataset = LineDataSet(item.co2Entries, "CO2")
        co2LineChart.getTransformer(YAxis.AxisDependency.LEFT)
        co2LineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val co2Data = LineData(co2Dataset)
        co2LineChart.data = co2Data
        co2LineChart.invalidate()

        val coDataset = LineDataSet(item.coEntries, "CO")
        coLineChart.getTransformer(YAxis.AxisDependency.LEFT)
        coLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val coData = LineData(coDataset)
        coLineChart.data = coData
        coLineChart.invalidate()

        val h2sDataset = LineDataSet(item.h2sEntries, "H2S")
        h2sLineChart.getTransformer(YAxis.AxisDependency.LEFT)
        h2sLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val h2sData = LineData(h2sDataset)
        h2sLineChart.data = h2sData
        h2sLineChart.invalidate()

        val lelDataset = LineDataSet(item.lelEntries, "LEL")
        lelLineChart.getTransformer(YAxis.AxisDependency.LEFT)
        lelLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val lelData = LineData(lelDataset)
        lelLineChart.data = lelData
        lelLineChart.invalidate()

        return convertView
    }

    fun receivedSerialData(serialEntity: SerialEntity) {
        items.forEach {
            if (it.sensorId == serialEntity.sensorId) {
                it.co = serialEntity.co
                it.co2 = serialEntity.co2
                it.lel = serialEntity.lel
                it.h2s = serialEntity.h2s
                it.o2 = serialEntity.o2
                it.o2Entries.add(Entry(it.o2Entries.size.toFloat(), (serialEntity.o2).toFloat()))
                it.co2Entries.add(Entry(it.co2Entries.size.toFloat(), (serialEntity.co2).toFloat()))
                it.coEntries.add(Entry(it.coEntries.size.toFloat(), (serialEntity.co).toFloat()))
                it.h2sEntries.add(Entry(it.h2sEntries.size.toFloat(), (serialEntity.h2s).toFloat()))
                it.lelEntries.add(Entry(it.lelEntries.size.toFloat(), (serialEntity.lel).toFloat()))
                it.createdAt = System.currentTimeMillis()
            }
        }

        notifyDataSetChanged()
    }
}