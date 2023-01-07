package ru.starksoft.commons.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val logger by lazy { Logger(this, "log.txt") }
    private val logger2 by lazy { Logger(this, "log2.txt") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logger.logMisc("Testing logger t={}") { this }

        logger.logException(Logger.LogType.MISC, null, IllegalStateException("Test exception"))

        logger.logException(Logger.LogType.MISC, "Test exception printed={}", IllegalStateException("Test exception"))

        logger.logDisabled("This entry should not be in log file") {
            throw IllegalStateException("This exception will never be thrown")
        }

        logger2.logMisc("Test record in second logger")

    }
}
