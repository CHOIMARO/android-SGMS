package com.tngen.sgms_android.data.repository

import com.tngen.sgms_android.data.db.dao.BaselineDao
import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.domain.repository.BaselineRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaselineRepositoryImpl
    @Inject constructor(
    private val baselineDao: BaselineDao,
    private val ioDispatcher: CoroutineDispatcher
) : BaselineRepository {
    override suspend fun getBaselineList(): List<BaselineEntity> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalBaselineList(): List<BaselineEntity> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun insertBaselineItem(baselineItem: BaselineEntity): Long = withContext(ioDispatcher) {
        baselineDao.insert(baselineItem)
    }

    override suspend fun insertBaselineList(baselineList: List<BaselineEntity>) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun updateBaselineItem(baselineItem: BaselineEntity) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getBaselineItem(itemId: Long): BaselineEntity? = withContext(ioDispatcher) {
        baselineDao.getById(itemId)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBaselineItem(id: Long) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}