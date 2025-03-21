// ! Bu araç @keyiflerolsun tarafından | @KekikAkademi için yazılmıştır.

package com.keyiflerolsun

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.INFER_TYPE
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.StringUtils.decodeUri

open class Drive : ExtractorApi() {
    override var name            = "Drive"
    override var mainUrl         = "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Fdrive.google.com%2Fhttps://drive.google.comemr=1https://drive.google.comfollowup=https%3A%2F%2Fdrive.google.com%2Fhttps://drive.google.comifkv=AXH0vVsSdmdRqdrOYtVXZc8lqmngd-pTbFVzhsH091AquhaX4nNj4uSNCQG4xBp9QGE-CAQTDfNvpAhttps://drive.google.comosid=1https://drive.google.compassive=1209600https://drive.google.comservice=wisehttps://drive.google.comflowName=WebLiteSignInhttps://drive.google.comflowEntry=ServiceLoginhttps://drive.google.comdsh=S959194144%3A1742589767644245"
    override val requiresReferer = true

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        val extRef  = referer ?: ""

        val docId        = Regex("""file/d/(.*)/preview""").find(url)?.groupValues?.get(1) ?: throw IllegalArgumentException("docId not found")
        val getVideoLink = "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Fdrive.google.com%2Fhttps://drive.google.comemr=1https://drive.google.comfollowup=https%3A%2F%2Fdrive.google.com%2Fhttps://drive.google.comifkv=AXH0vVsSdmdRqdrOYtVXZc8lqmngd-pTbFVzhsH091AquhaX4nNj4uSNCQG4xBp9QGE-CAQTDfNvpAhttps://drive.google.comosid=1https://drive.google.compassive=1209600https://drive.google.comservice=wisehttps://drive.google.comflowName=WebLiteSignInhttps://drive.google.comflowEntry=ServiceLoginhttps://drive.google.comdsh=S959194144%3A1742589767644245/get_video_info?docid=${docId}&drive_originator_app=303"
        val iSource      = app.get(getVideoLink, referer=extRef).text

        val bakalim = Regex("""&fmt_stream_map=(.*)&url_encoded_fmt_stream_map""").find(iSource)?.groupValues?.get(1) ?: throw IllegalArgumentException("fmt_stream_map not found")
        val decoded = bakalim.decodeUri()
        val m3uLink = decoded.split("|").last()
        Log.d("Kekik_${this.name}", "m3uLink » $m3uLink")

        callback.invoke(
            ExtractorLink(
                source  = this.name,
                name    = this.name,
                url     = m3uLink,
                referer = url,
                quality = Qualities.Unknown.value,
                type    = INFER_TYPE
            )
        )
    }
}