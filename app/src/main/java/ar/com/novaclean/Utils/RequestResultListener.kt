package ar.com.novaclean.Utils

import com.android.volley.VolleyError

interface RequestResultListener{
    fun OnRequestResult(requestResult:RequestResult)
}

class RequestResult(val volleyError: VolleyError?, val success: Boolean) {

}
