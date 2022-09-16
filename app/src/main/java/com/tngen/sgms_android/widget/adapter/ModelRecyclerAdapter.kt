package com.tngen.sgms_android.widget.adapter


import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.model.CellType
import com.tngen.sgms_android.model.Model
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.mapper.ModelViewHolderMapper
import com.tngen.sgms_android.utility.prodiver.DefaultResourcesProvider
import com.tngen.sgms_android.utility.prodiver.ResourcesProvider
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener
import com.tngen.sgms_android.widget.adapter.viewholder.ModelViewHolder

class ModelRecyclerAdapter<M: Model, VM: BaseViewModel>(
    private var modelList: List<Model>,
    private var viewModel: VM,
    private val resourcesProvider: ResourcesProvider = DefaultResourcesProvider(SgmsApplication.appContext!!),
    private val adapterListener: AdapterListener,
    ) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {
    override fun getItemCount(): Int = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: List<Model>?) {
        list?.let { modelList = it }
        super.submitList(list)
    }

}