package com.agile.composechronometer

import java.time.Duration

fun Duration.getPartSeconds(): Long{
    return (this.toMillis() / 1000) % 60
}