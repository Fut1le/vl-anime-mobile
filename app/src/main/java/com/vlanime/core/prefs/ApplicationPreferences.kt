package com.vlanime.core.prefs

import com.vlanime.core.model.LatestWatchedTitleEpisode
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonPref

object ApplicationPreferences : KotprefModel() {
    override val commitAllPropertiesByDefault: Boolean = true

    var watchedList by gsonPref(mutableListOf<String>())
    var watchedTitles by gsonPref(mutableListOf<LatestWatchedTitleEpisode>())

}