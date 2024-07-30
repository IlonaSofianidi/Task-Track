package org.lemb.tasktrack.data

interface Platform {
    val name: String
}

expect fun platform(): Platform