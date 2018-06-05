package net.skyscanner.hackameetup.core.provider

import javax.inject.Inject

class TimestampProvider @Inject constructor() {

    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
