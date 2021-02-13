package ru.starksoft.commons.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val logger by lazy {
        Logger(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logger.logMisc("Testing logger t={}") { this }

        logger.logException(Logger.LogType.MISC, IllegalStateException("Test exception"))

        logger.logDisabled("This entry should not be in log file") {
            throw IllegalStateException("This exception will never be thrown")
        }


    }
}
