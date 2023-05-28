package jp.chocofac.charlie.data.service.senryu

import retrofit2.Response
import javax.inject.Inject

class SenryuRepository @Inject constructor(
    private val senryuService: SenryuService
) {
    suspend fun postImpressions(impressions: String):Response<Senryu> {
        return senryuService.postImpressions(Impressions(impressions))
    }
}