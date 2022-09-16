package com.tngen.sgms_android.domain.repository

import com.tngen.sgms_android.data.entity.settings.BaselineEntity

interface BaselineRepository {
    suspend fun getBaselineList(): List<BaselineEntity>

    suspend fun getLocalBaselineList(): List<BaselineEntity>

    suspend fun insertBaselineItem(baselineItem: BaselineEntity): Long

    suspend fun insertBaselineList(baselineList: List<BaselineEntity>)

    suspend fun updateBaselineItem(baselineItem: BaselineEntity)

    suspend fun getBaselineItem(itemId: Long): BaselineEntity?

    suspend fun deleteAll()

    suspend fun deleteBaselineItem(id: Long)
}