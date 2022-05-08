package com.brunoponte.everythinglisboa.network

import com.brunoponte.everythinglisboa.domain.RepoResult
import com.google.gson.Gson
import java.net.URL

class GithubRequest {

    companion object {
        //1
        private const val URL = "https://api.github.com/search/repositories"
        private const val SEARCH = "q=super+mario+language:kotlin&sort=stars&order=desc"
        private const val COMPLETE_URL = "$URL?$SEARCH"
    }

}