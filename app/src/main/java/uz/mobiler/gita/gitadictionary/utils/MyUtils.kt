package uz.mobiler.gita.gitadictionary.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import kotlin.text.indexOf

fun String.foregroundColor(colorResID: Int, query: String): SpannableString {
    val spannable = SpannableString(this)

    val start = this.indexOf(query, ignoreCase = false)
    if (start == -1) return spannable

    val end = start + query.length

    spannable.setSpan(
        ForegroundColorSpan(colorResID),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannable
}
