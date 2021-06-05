package com.princechat.utils

import java.util.*

fun getRandomNumberString(): String? {
    // It will generate 6 digit random Number.
    // from 0 to 999999
    val rnd = Random()
    val number: Int = rnd.nextInt(999999)

    // this will convert any number sequence into 6 character.
    return String.format("%06d", number)
}